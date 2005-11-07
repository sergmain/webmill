/*
 * org.riverock.webmill -- Portal framework implementation
 * 
 * Copyright (C) 2004, Riverock Software, All Rights Reserved.
 * 
 * Riverock -- The Open-source Java Development Community
 * http://www.riverock.org
 * 
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 *
 */

/**
 * Author: mill
 * Date: Jan 22, 2003
 * Time: 9:14:52 AM
 *
 * $Id$
 */

package org.riverock.webmill.as.server;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.Writer;
import java.util.Enumeration;
import java.util.zip.Deflater;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.riverock.common.tools.Base64;
import org.riverock.common.tools.ExceptionTools;
import org.riverock.common.tools.MainTools;
import org.riverock.common.tools.ServletTools;
import org.riverock.common.tools.StringTools;
import org.riverock.generic.tools.XmlTools;
import org.riverock.generic.tools.servlet.HttpServletRequestApplWrapper;
import org.riverock.sso.a3.AccessDeniedException;
import org.riverock.interfaces.sso.a3.AuthSession;
import org.riverock.sso.a3.AuthTools;
import org.riverock.sso.a3.AuthSessionImpl;
import org.riverock.webmill.as.ApplicationInterface;
import org.riverock.webmill.schema.appl_server.ApplicationModuleType;
import org.riverock.webmill.schema.appl_server.ResourceCompressionType;
import org.riverock.webmill.schema.appl_server.ResourceRequestParameterType;
import org.riverock.webmill.schema.appl_server.ResourceRequestType;
import org.riverock.webmill.schema.appl_server.ResourceResponseType;
import org.riverock.webmill.schema.appl_server.StringResultType;
import org.riverock.webmill.schema.appl_server.types.ResourceCompressionTypeTypeCompressionType;
import org.riverock.webmill.schema.appl_server.types.ResourceResponseTypeErrorTypeType;
import org.riverock.webmill.config.WebmillConfig;

import org.apache.log4j.Logger;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.xml.sax.InputSource;

public class ApplicationServer extends HttpServlet
{
    private static Logger log = Logger.getLogger("org.riverock.webmill.as.server.ApplicationServer");

    public ApplicationServer()
    {
    }

    private static String processLogin(ResourceRequestType applReq, AuthSession authSession, HttpServletRequest request )
        throws Exception
    {
        if (applReq==null || applReq.getParametersCount()==0)
            return null;

        HttpSession session = request.getSession( false );
        if (session!=null)
        {
            ServletTools.cleanSession( session );
            session.invalidate();
            session = null;
        }

        session = request.getSession( true );

        String userLogin = null;
        String userPassword = null;
        for (int i=0; i<applReq.getParametersCount(); i++)
        {
            ResourceRequestParameterType param = applReq.getParameters(i);
            if (ApplicationServerConstants.USER_LOGIN_NAME_PARAM.equals( param.getNameParameter()))
            {
                String stringParam = ApplicationTools.decodeParameter( param );

                if (log.isDebugEnabled())
                    log.debug("User login parameter is "+stringParam);

                userLogin =  stringParam;
                continue;
            }
            if (ApplicationServerConstants.USER_PASSWORD_NAME_PARAM.equals( param.getNameParameter()))
            {
                String stringParam = ApplicationTools.decodeParameter( param );

                if (log.isDebugEnabled())
                    log.debug("Password parameter is "+stringParam);

                userPassword = stringParam;
                continue;
            }
        }
        authSession.setUserLogin( userLogin );
        authSession.setUserPassword( userPassword );

        if (authSession.checkAccess( request.getServerName() ))
        {
            session.setAttribute( org.riverock.sso.main.Constants.AUTH_SESSION, authSession );
            return session.getId();
        }
        throw new AccessDeniedException();
    }

    public void doPost(HttpServletRequest request_, HttpServletResponse response_)
        throws IOException, ServletException
    {
        if (log.isDebugEnabled())
            log.debug("method is POST");

        doGet(request_, response_);
    }

    private static ResourceResponseType getData(ResourceRequestType applReq, AuthSession authSession, HttpServletRequest request )
    {
        if (applReq==null)
        {
            log.error("applReq is null");
            return null;
        }

        if (log.isDebugEnabled())
        {
            log.debug( "applReq - "+applReq );
            if ( applReq!=null)
                log.debug( "CodeResource - "+applReq.getCodeResource() );
        }

        ResourceResponseType answer = new ResourceResponseType();
        ResourceCompressionType compressionType = new ResourceCompressionType();
        ApplicationModuleType mod = null;
        try
        {
            byte bytes[] = null;
            if (ApplicationServerConstants.NAME_LOGIN_MODULE.equals(applReq.getCodeResource()))
            {
                StringResultType sessionIdResult = new StringResultType();
                sessionIdResult.setResult( processLogin(applReq, authSession, request ) );
                bytes = XmlTools.getXml( sessionIdResult, null );
            }
            else
            {
                mod = ApplicationManager.getApplModule( applReq.getCodeResource() );

                if ( mod==null )
                    throw new IllegalArgumentException("Descriptor for resource '"+applReq.getCodeResource()+"' not found");

                if (log.isDebugEnabled())
                    log.debug("Create class " + mod.getClassName());

                ApplicationInterface obj = (ApplicationInterface)MainTools.createCustomObject( mod.getClassName() );

                if ( mod.getRole()!=null )
                {
                    boolean isAccessDenied = !authSession.isUserInRole( mod.getRole() );
                    if (isAccessDenied)
                    {
                        String errorString = "You must have role '"+mod.getRole()+"' to execute "+mod.getClassName()+" class";
                        log.error( errorString );
                        throw new AccessDeniedException( errorString );
                    }
                }
                bytes = obj.processRequest( applReq, authSession );
            }
            byte b[] = null;
            if (log.isDebugEnabled())
            {
                log.debug("Compression - "+applReq.getResponseCompressionType());
                if ( applReq.getResponseCompressionType() != null )
                    log.debug("Compression type - "+applReq.getResponseCompressionType().getTypeCompression().toString());
            }

            long mills = System.currentTimeMillis();
            answer.setOriginLength( (long)bytes.length );
            ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
            byte[] bytesToEncrypt = null;
            int typeCompression = -1;

            if ( applReq.getResponseCompressionType() != null )
                typeCompression = applReq.getResponseCompressionType().getTypeCompression().getType();

            switch( typeCompression )
            {
                case ResourceCompressionTypeTypeCompressionType.ZIP_TYPE:
                    if (log.isDebugEnabled())
                    {
                        log.debug("start Zip data ");
                        log.debug("Length data to pack " + bytes.length);
                    }

                    ZipOutputStream zip = new ZipOutputStream(byteStream);
                    zip.setLevel( Deflater.BEST_COMPRESSION );
                    zip.putNextEntry( new ZipEntry( ApplicationServerConstants.ZIP_ENTRY_NAME) );

                    zip.write(bytes);
                    zip.flush();
                    zip.close();
                    zip = null;

                    bytesToEncrypt = byteStream.toByteArray();
                    answer.setCompressedLength( (long)bytesToEncrypt.length );

                    if (log.isDebugEnabled())
                    {
                        log.debug("Done pack data for " + (System.currentTimeMillis() - mills)+" milliseconds");
//
                        log.debug("start encode to base64 object. data length - " + bytesToEncrypt.length);
                    }

                    b = Base64.encode( bytesToEncrypt );

                    compressionType.setTypeCompression(
                        ResourceCompressionTypeTypeCompressionType.ZIP
                    );
                    break;

                case ResourceCompressionTypeTypeCompressionType.GZIP_TYPE:
                    if (log.isDebugEnabled())
                    {
                        log.debug("start GZip data ");
                        log.debug("Length data to pack " + bytes.length);
                    }

                    GZIPOutputStream gzip = new GZIPOutputStream(byteStream);

                    gzip.write(bytes);
                    gzip.flush();
                    gzip.close();
                    gzip = null;

                    bytesToEncrypt = byteStream.toByteArray();
                    answer.setCompressedLength( (long)bytesToEncrypt.length );

                    if (log.isDebugEnabled())
                    {
                        log.debug("Done GZip data for " + (System.currentTimeMillis() - mills)+" milliseconds");
                        log.debug("start encode to base64 object. data length - " + bytesToEncrypt.length);
                    }

                    b = Base64.encode( bytesToEncrypt );

                    compressionType.setTypeCompression(
                        ResourceCompressionTypeTypeCompressionType.GZIP
                    );
                    break;

                case ResourceCompressionTypeTypeCompressionType.NONE_TYPE:
                default:

                    if (log.isDebugEnabled())
                        log.debug("Process without compression");

                    compressionType.setTypeCompression(
                        ResourceCompressionTypeTypeCompressionType.NONE
                    );
                    answer.setOriginLength( (long)bytes.length );
                    answer.setCompressedLength( (long)bytes.length );
                    b = Base64.encode( bytes );
            }
            answer.setCompression(compressionType);

            if (log.isDebugEnabled())
                log.debug("done prepare data");

            answer.setData( new String( b ) );
            answer.setErrorText( null );
            answer.setIsError( Boolean.FALSE );
        }
        catch (AccessDeniedException e)
        {
            log.warn("Access is denied");
            compressionType.setTypeCompression(
                ResourceCompressionTypeTypeCompressionType.NONE
            );
            answer.setCompression(compressionType);
            answer.setIsError( Boolean.TRUE );
            if  (request.getSession( false )!=null)
                answer.setErrorType( ResourceResponseTypeErrorTypeType.ACCESS_DENIED );
            else
                answer.setErrorType( ResourceResponseTypeErrorTypeType.NOT_LOGGED );

            answer.setData( null );

            if (log.isDebugEnabled())
                log.debug("Exception stack trace " +ExceptionTools.getStackTrace(e, 200));

            answer.setErrorText(
                new String(
                    Base64.encode(
                        ExceptionTools.getStackTrace(e, 200).getBytes()
                    )
                )
            );
        }
        catch (Throwable ex)
        {
            log.error("Exception in processing getData method", ex);

            ResourceResponseTypeErrorTypeType errorType = ResourceResponseTypeErrorTypeType.RUNTIME;
            Integer errorCode = null;
            if (mod!=null)
            {
                for (int i=0; i<mod.getErrorsCount(); i++)
                {
                    if (ex.getClass().getName().equals(mod.getErrors(i).getErrorClass()))
                    {
                        errorType = ResourceResponseTypeErrorTypeType.USER_DEFINED;
                        errorCode = mod.getErrors(i).getErrorCode();
                        break;
                    }
                }
            }

            compressionType.setTypeCompression(
                ResourceCompressionTypeTypeCompressionType.NONE
            );
            answer.setCompression(compressionType);
            answer.setIsError( Boolean.TRUE );
            answer.setErrorType( errorType );
            answer.setErrorCode( errorCode );
            answer.setData( null );
            answer.setErrorText(
                new String(
                    Base64.encode(
                        ExceptionTools.getStackTrace(ex, 200, null).getBytes()
                    )
                )
            );
        }

        return answer;
    }

    public void doGet(HttpServletRequest request_, HttpServletResponse response_)
        throws IOException, ServletException
    {
        long startMills = System.currentTimeMillis();

        if (log.isDebugEnabled())
            log.debug("Process GET request");

        Writer out = null;
        ResourceResponseType res = null;
        HttpSession session = null;
        try
        {
            session = request_.getSession( false );
            if (log.isDebugEnabled())
                log.debug("Previous session is "+session);

            AuthSession authSession = null;
            if (session==null)
                authSession = new AuthSessionImpl( null, null );
            else
                authSession = AuthTools.getAuthSession( request_ );

            if (authSession==null)
                authSession = new AuthSessionImpl( null, null );

            if (log.isDebugEnabled())
            {
                log.debug("authSession is "+authSession+", warning this object MUST NOT BE NULL!");

                log.debug("count of parameters - "+request_.getParameterMap().size());
                for (Enumeration e = request_.getParameterNames(); e.hasMoreElements() ;)
                {
                    String paramName = (String)e.nextElement();
                    String value  = request_.getParameter(paramName);
                    log.debug( "paramName - "+paramName+" value "+
                        StringTools.truncateString(value, 20) +
                        (value.length()>20?"...":"")
                    );
                }
            }

            boolean status = (request_.getParameter( ApplicationServerConstants.NAME_APPL_PARAM )!=null);
            if (request_.getParameterMap().size()==0 || !status)
            {
                res = new ResourceResponseType();
                res.setIsError( Boolean.TRUE );
                res.setErrorType( ResourceResponseTypeErrorTypeType.APPL_CODE_NOT_FOUND );
                res.setData( null );
                res.setErrorText("Parameter "+ApplicationServerConstants.NAME_APPL_PARAM+" not specified in request");
            }
            else
            {
                ResourceRequestType str = null;

                if (log.isDebugEnabled())
                    log.debug("Parameter is present");

                String decode = ServletTools.getString(request_, ApplicationServerConstants.NAME_APPL_PARAM, "", WebmillConfig.getServerCharset(), WebmillConfig.getHtmlCharset() );

//                if (log.isDebugEnabled())
//                    log.debug("Parameter - "+p);
//                String decode = URIUtil.decode( p );
//                String decode = p;

                if (log.isDebugEnabled())
                    log.debug("Decoded parameter - "+decode);

                InputSource inSrc = new InputSource( new StringReader(decode) );
                str = (ResourceRequestType) Unmarshaller.unmarshal(ResourceRequestType.class, inSrc);

                if (log.isDebugEnabled())
                    log.debug("XML Parameter - "+str);

                res = getData( str, authSession, request_ );
            }

            response_.setContentType("text/html; charset=UTF-8");
            res.setStartProcessing( startMills );
            long endprocessing = System.currentTimeMillis();
            res.setEndProcessing( endprocessing );

            if (log.isDebugEnabled())
                log.debug("Start unmarshal data");

            out = response_.getWriter();

            Marshaller marsh = new Marshaller( out );
            marsh.setRootElement( "ResourceResponse" );
            marsh.setMarshalAsDocument(true);
            marsh.setEncoding( "utf-8" );
            marsh.marshal( res );
            out.flush();
            out.close();
            out = null;

            if (log.isDebugEnabled())
                log.debug("Data successfully was sended to client");

            Long maxMemory = Runtime.getRuntime().maxMemory();

            log.warn(
                "free memory " + Runtime.getRuntime().freeMemory()+
                " total memory "+ Runtime.getRuntime().totalMemory()+
                (maxMemory!=null?" max memory "+maxMemory:"")+
                " request processed for "+(endprocessing-startMills)
            );
        }
        catch(Exception e)
        {
            log.error("Error process answer to client", e);
            throw new ServletException(e);
        }
        finally
        {
            if (out != null)
            {
                out.flush();
                out.close();
                out = null;
            }
        }
    }

/*
    public void doGetTest(HttpServletRequest request_, HttpServletResponse response_)
        throws IOException, ServletException
    {
        long startMills = System.currentTimeMillis();

        try
        {
            boolean status = (request_.getParameter( Constants.NAME_APPL_PARAM )!=null);

            ResourceRequestType str = null;
            if (status)
            {
                if (log.isDebugEnabled())
                    log.debug("Parameter is present");

                String p = ServletTools.getString(request_, Constants.NAME_APPL_PARAM );

                if (log.isDebugEnabled())
                    log.debug("Parameter - "+p);

//                String decode = URIUtil.decode( p );
                String decode = p;

                if (log.isDebugEnabled())
                    log.debug("Decoded parameter - "+decode);

                InputSource inSrc = new InputSource( new StringReader(decode) );
                str = (ResourceRequestType) Unmarshaller.unmarshal(ResourceRequestType.class, inSrc);

                if (log.isDebugEnabled())
                    log.debug("XML Parameter - "+str);
            }

            out = response_.getWriter();

            ResourceResponseType res = new ResourceResponseType();
            response_.setContentType("text/html; charset=UTF-8");

            SiteData data = new SiteData();
            if (log.isDebugEnabled())
                log.debug("get SiteListSite");
            SiteListSiteType site = data.getSiteData("me.askmore");
            if (log.isDebugEnabled())
                log.debug("SiteListSite site - "+site);

            res.setIsError( false );

            String xmlStr = XmlTools.getXml(site, "SiteListSite");
            if (log.isDebugEnabled())
                log.debug("XML SiteListSite site - "+xmlStr);

            String strData = xmlStr + 111;
//            String strData = URIUtil.encode( xmlStr );
//          strData =  StringTools.encodeXml( xmlStr );

            if (log.isDebugEnabled())
                log.debug("strData - "+strData);

            res.setData( strData );


//            res.setIsError( true );
//            res.setErrorText( "GET method not allowed in application server" );
//            if(str != null)
//            {
//                res.setErrorText( res.getErrorText() + str.getCodeResource() );
//            }

            res.setStartProcessing( startMills );
            res.setEndProcessing( System.currentTimeMillis() );

            out.write( XmlTools.getXml(res, "ResourceResponse") );
        }
        catch(Exception e)
        {
            log.error("Error process answer to client", e);
            throw new ServletException(e);
        }
        finally
        {
            if (out != null)
            {
                out.flush();
                out.close();
                out = null;
            }
        }
    }
*/

    public static void main(String args[])
        throws Exception
    {
        org.riverock.generic.startup.StartupApplication.init();

        ResourceRequestType req = new ResourceRequestType();
        req.setCodeResource( "mill.SiteData" );
        ResourceRequestParameterType param = new ResourceRequestParameterType();
        param.setNameParameter("mill.server_name");
        param.setValueParameter("me.askmore");
        req.addParameters( param );

        AuthSession authSession = new AuthSessionImpl();
        HttpServletRequestApplWrapper request = new HttpServletRequestApplWrapper();
        ResourceResponseType res = getData( req, authSession, request );

        System.out.println( WebmillConfig.getWebmillDebugDir()+"test-appl-call-class" );
        XmlTools.writeToFile(
            res,
            WebmillConfig.getWebmillDebugDir()+"test-appl-call-class.xml",
            "utf-8",
            null
        );
    }
}
