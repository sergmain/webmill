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

 * $Id$

 */

package org.riverock.webmill.portlet;



import java.io.ByteArrayInputStream;

import java.io.ByteArrayOutputStream;

import java.io.File;

import java.io.FileWriter;

import java.io.IOException;

import java.io.StringWriter;

import java.io.Writer;

import java.util.*;



import javax.servlet.RequestDispatcher;

import javax.servlet.ServletException;

import javax.servlet.http.HttpServlet;

import javax.servlet.http.HttpServletRequest;

import javax.servlet.http.HttpServletResponse;

import javax.servlet.http.HttpSession;

import javax.xml.transform.Source;

import javax.xml.transform.stream.StreamResult;

import javax.xml.transform.stream.StreamSource;



import org.riverock.webmill.main.Constants;

import org.riverock.webmill.schema.site.SitePortletDataListType;

import org.riverock.webmill.schema.site.SitePortletDataType;

import org.riverock.webmill.schema.site.TemplateItemType;

import org.riverock.webmill.schema.site.types.TemplateItemTypeTypeType;

import org.riverock.interfaces.schema.javax.portlet.PortletType;

import org.riverock.interfaces.schema.portlet.types.PortletDescriptionTypeTypePortletType;

import org.riverock.common.tools.ExceptionTools;

import org.riverock.common.tools.MainTools;

import org.riverock.common.config.ConfigException;

import org.riverock.common.config.PropertiesProvider;

import org.riverock.webmill.utils.ServletUtils;

import org.riverock.webmill.port.InitPage;

import org.riverock.webmill.config.WebmillConfig;

import org.riverock.generic.db.DatabaseAdapter;



import org.apache.log4j.Logger;

import org.apache.log4j.NDC;

import org.exolab.castor.xml.Marshaller;



public class ContextNavigator extends HttpServlet

{

    private static Logger log = Logger.getLogger( "org.riverock.portlet.ContextNavigator" );





    private static final String copyright =

        "<!--\n" +

        "  Engine: WebMill\n" +

        " Release: @@WEBMILL_RELEASE@@\n" +

        "   Build: @@WEBMILL_BUILD@@\n" +

        "Homepage: http://webmill.askmore.info\n" +

        "-->\n";





    public ContextNavigator()

    {

    }



    private SitePortletDataType processPortlet( TemplateItemType templateItem, CtxInstance ctxInstance, Long portletId)

    {

        SitePortletDataType item = new SitePortletDataType();



        PortletType portlet = PortletManager.getPortlet( ctxInstance );

        if ( portlet==null )

        {

            item.setData( "Error getPortlet()".getBytes() );

            item.setIsXml( Boolean.FALSE );

            item.setIsError( Boolean.TRUE );

            return item;

        }



        try

        {

            PortletParameter portletParameter =

                new PortletParameter(

                    ctxInstance,

                    PortletTools.getStringParam(

                        portlet, PortletTools.locale_name_package

                    ),

                    templateItem

                );



            PortletResultObject obj = PortletTools.getPortletObject(

                portlet,

                portletParameter,

                ctxInstance.db,

                portletId,

                ctxInstance.getPortletRequest()

            );

            ctxInstance.session.removeAttribute( Constants.NAME_PORTLET_PARAM );



            if ( obj==null )

            {

                item.setData(

                    ( "Error create portlet object. Object is null.  PortletName - "+portlet.getPortletName().getContent()+

                    ", typePortlet "+

                    PortletTools.getStringParam(

                        portlet, PortletTools.type_portlet

                    )+



                    ". Template, value "+

                    templateItem.getValue()+", code "+templateItem.getCode()+", xmlRoot "+

                    templateItem.getXmlRoot()

                    ).getBytes()

                );

                item.setIsXml( Boolean.FALSE );

                item.setIsError( Boolean.TRUE );

                return item;

            }



            if ( log.isDebugEnabled() )

            {

                log.debug( "Portlet object successfull created" );

                log.debug( "isXml() - "+obj.isXml()+", isHtml() - "+obj.isHtml() );

                log.debug( "class name of object  - "+obj.getClass().getName() );

            }



            byte portletBytes[] = null;

            if ( obj.isXml() )

            {

                if ( log.isDebugEnabled() )

                {

                    log.debug( "XmlRoot - "+templateItem.getXmlRoot() );

                }



                String xmlRoot = null;

                if ( templateItem.getType().getType()==TemplateItemTypeTypeType.DYNAMIC_TYPE )

                    xmlRoot = PortletTools.getString( ctxInstance.getPortletRequest(), Constants.NAME_XMLROOT_PARAM );



                if ( xmlRoot==null || xmlRoot.length()==0 )

                    xmlRoot = templateItem.getXmlRoot();



                if ( xmlRoot!=null && xmlRoot.length()>0 )

                    portletBytes = obj.getXml( xmlRoot );

                else

                    portletBytes = obj.getXml();



                if ( log.isDebugEnabled() )

                    log.debug( "Array of bytes is recieved" );



                // write all without XML header - <? ..... ?>



                int idx = MainTools.indexOf( portletBytes, (byte)'>' );

                if ( idx==-1 )

                {

                    // This situation not be raised

                }

                else

                {

                    item.setData( MainTools.getBytes( portletBytes, idx+1 ) );

                    item.setIsXml( Boolean.TRUE );

                }

            }

            else if ( obj.isHtml() )

            {

                item.setData( obj.getPlainHTML() );

                item.setIsXml( Boolean.FALSE );

            }

            else

            {

                item.setData(

                    ( "Portlet not implemented both getXml() and getPlainHtml() - "+

                    PortletTools.getStringParam(

                        portlet, PortletTools.type_portlet

                    )

                    ).getBytes()

                );

                item.setIsXml( Boolean.FALSE );

            }



            if ( log.isDebugEnabled() )

                log.debug( "Portlet string - "+portletBytes );



            item.setIsError( Boolean.FALSE );

            return item;



        }

        catch (Exception e)

        {

            log.error( "Error process Portlet '"+portlet.getPortletName().getContent()+"'", e );

            item.setData(

                ( "Error process Portlet '"+portlet.getPortletName().getContent()+"'\n"+

                e.toString()+"\n"+

                "<!-- "+

                ExceptionTools.getStackTrace( e, 15 )+

                "\n-->\n"

                ).getBytes()

            );

            item.setIsError( Boolean.TRUE );

            item.setIsXml( Boolean.FALSE );

            return item;

        }

    }



    private void processTemplateItem( CtxInstance ctxInstance )

        throws Exception

    {

        StringWriter writer = new StringWriter();



        ctxInstance.portlets = new ArrayList( ctxInstance.template.getSiteTemplateItemCount() );



        for ( int i = 0; i<ctxInstance.template.getSiteTemplateItemCount(); i++ )

        {

            TemplateItemType item = ctxInstance.template.getSiteTemplateItem( i );



            // Portlets 'MODEL' is skipped

            if ( item.getType().getType()==TemplateItemTypeTypeType.PORTLET_TYPE )

            {

                PortletType d = PortletManager.getPortletDescription( item.getValue() );



                if ( d==null )

                    continue;



                String typePortletTemp =

                    PortletTools.getStringParam(

                        d, PortletTools.type_portlet

                    );



                if ( typePortletTemp!=null &&

                    typePortletTemp.equals(

                        PortletDescriptionTypeTypePortletType.MODEL.toString()

                    )

                )

                    continue;

            }



            if ( log.isDebugEnabled() )

                log.debug( "idx - "+i+" TemplateItem type  - "+item.getType() );



            SitePortletDataType data = new SitePortletDataType();



            log.warn("portlet type: "+ctxInstance.page.getType());

            PortletType d = null;

            if (ctxInstance.page.getPortletDescription()!=null)

            {

                d = ctxInstance.page.getPortletDescription().getPortletConfig();

            }

            ctxInstance.setParameters(

                PortletTools.getParameters(ctxInstance.req),

                PortletTools.getStringParam(d, PortletTools.locale_name_package)

            );

            ctxInstance.session.setAttribute( Constants.PORTLET_REQUEST_SESSION, ctxInstance );



            try

            {

                switch (item.getType().getType())

                {

                    case TemplateItemTypeTypeType.PORTLET_TYPE:



                        if ( log.isDebugEnabled() )

                            log.debug( "Portlet item  - "+item.getValue() );



                        ctxInstance.session.removeAttribute( Constants.NAME_PORTLET_PARAM );

                        ctxInstance.session.setAttribute( Constants.NAME_PORTLET_PARAM, item.getValue() );



                        ctxInstance.setParameters(new HashMap(), null);

                        data = processPortlet( item, ctxInstance, null);



                        ctxInstance.session.removeAttribute( Constants.NAME_PORTLET_PARAM );

                        break;



                    case TemplateItemTypeTypeType.DYNAMIC_TYPE:

                        if ( Constants.CTX_TYPE_INDEX.equals( ctxInstance.getType() ) )

                        {

                            String errorString = "Context type is 'index_page'. Template can not contain 'dynamic' type of portlet.";

                            log.error( errorString );

                            data.setData( errorString.getBytes() );

                            data.setIsError( Boolean.TRUE );

                            data.setIsXml( Boolean.FALSE );

                        }

                        else

                        {

                            if ( ctxInstance.page.isDynamic() )

                            {

                                if ( log.isDebugEnabled() )

                                    log.debug( "process dynamic content - "+ctxInstance.page.isDynamic() );



                                PortletType portlet = ctxInstance.page.getPortletDescription().getPortletConfig();

                                Boolean isUrlTemp =

                                    PortletTools.getBooleanParam(

                                        portlet, PortletTools.is_url

                                    );



                                if ( portlet!=null && isUrlTemp!=null && !isUrlTemp.booleanValue() )

                                {

                                    if ( log.isDebugEnabled() )

                                        log.debug( "process dynamic content. portlet !=null && !portlet.isUrl" );



                                    ctxInstance.session.removeAttribute( Constants.NAME_PORTLET_PARAM );

                                    ctxInstance.session.setAttribute( Constants.NAME_PORTLET_PARAM, ctxInstance.getType() );



                                    data = processPortlet( item, ctxInstance, ctxInstance.page.getPortletId() );



                                    ctxInstance.session.removeAttribute( Constants.NAME_PORTLET_PARAM );

                                }

                                else if ( portlet!=null && isUrlTemp!=null && isUrlTemp.booleanValue() )

                                {

                                    if ( log.isDebugEnabled() )

                                        log.debug( "process dynamic content. include "+portlet.getPortletClass()+", isUrl - "+isUrlTemp );



                                    boolean flag = true;

                                    // check file present



                                    //Todo This code need fix

/*

                                    if ( !Boolean.TRUE.equals(isUrlTemp) )

                                    {

                                        String nameFile = WebmillParam.millApplPath+portlet.getPortletClass();

                                        if ( nameFile.indexOf( '?' )!=-1 )

                                            nameFile = nameFile.substring( 0, nameFile.indexOf( '?' ) );



                                        File testFile = new File( nameFile );

                                        if ( !testFile.exists() )

                                        {

                                            String errorString = "Portlet "+dynamicType+" refered to file "+portlet.getPortletClass()+" is broken";

                                            ctxInstance.byteArrayOutputStream.write( errorString.getBytes() );

                                            log.error( errorString );



                                            data.setData( errorString.getBytes() );

                                            data.setIsError( true );

                                            data.setIsXml( Boolean.FALSE );



                                            flag = false;

                                        }

                                        testFile = null;

                                    }

*/



                                    if ( flag )

                                    {

                                        ServletUtils.include( ctxInstance.req, ctxInstance.response, portlet.getPortletClass(), writer );

                                        String s = writer.toString();

/*

                                        if (log.isDebugEnabled())

                                        {

                                            log.debug("portlet.getFileName() - "+portlet.getFileName());

                                            log.debug("ctx result 1 page - "+s);

                                            log.debug("ctx result 2 page - "+

                                                StringTools.convertString(s,

                                                    "utf-8",

                                                    System.getProperty("file.encoding")

                                                )

                                            );

                                            log.debug("ctx result 3 page - "+

                                                StringTools.convertString(s,

                                                    System.getProperty("file.encoding"),

                                                    "utf-8"

                                                )

                                            );

                                        }

*/

/*

                                        data.setData( StringTools.convertString(s,

                                                    "utf-8",

                                                    System.getProperty("file.encoding")

                                                ).getBytes()

                                        );

*/

                                        data.setData( s.getBytes( WebmillConfig.getHtmlCharset() ) );

                                        data.setIsError( Boolean.FALSE );

                                        data.setIsXml( Boolean.FALSE );

                                    }

                                }

                                else

                                {

                                    if ( log.isDebugEnabled() )

                                        log.debug( "process dynamic content as static file not permit - file not found" );

                                }

                            }

                            else

                            {

                                String errorString = "Unknown error. dynamicType is null.";

                                log.error( errorString );

                                data.setData( errorString.getBytes() );

                                data.setIsError( Boolean.TRUE );

                                data.setIsXml( Boolean.FALSE );

                            }

                        }

                        break;

                    case TemplateItemTypeTypeType.FILE_TYPE:



                        Boolean isUrlTemp =

                            PortletTools.getBooleanParam(

                                ctxInstance.page.getPortletDescription().getPortletConfig(),

                                PortletTools.is_url

                            );



                        if ( log.isDebugEnabled() )

                            log.debug( "process  included file - "+item.getValue()+", isUrl - "+isUrlTemp );



                        boolean flag = true;

                        if ( isUrlTemp!=null && !isUrlTemp.booleanValue())

                        {

                            String nameFile = PropertiesProvider.getApplicationPath()+item.getValue();

                            if ( nameFile.indexOf( '?' )!=-1 )

                                nameFile = nameFile.substring( 0, nameFile.indexOf( '?' ) );



                            File testFile = new File( nameFile );

                            if ( !testFile.exists() )

                            {

                                String errorString = "Portlet "+ctxInstance.getType()+" refered to file "+testFile+" is broken";

                                log.error( errorString );

                                ctxInstance.byteArrayOutputStream.write( errorString.getBytes() );



                                data.setData( errorString.getBytes() );

                                data.setIsError( Boolean.TRUE );

                                data.setIsXml( Boolean.FALSE );



                                flag = false;

                            }

                            testFile = null;

                        }



                        if ( flag )

                        {

                            ServletUtils.include( ctxInstance.req, ctxInstance.response, item.getValue(), writer );



                            data.setData( writer.toString().getBytes() );

                            data.setIsError( Boolean.FALSE );

                            data.setIsXml( Boolean.FALSE );

                        }



                        break;



                    case TemplateItemTypeTypeType.CUSTOM_TYPE:

                        if ( log.isDebugEnabled() )

                            log.debug( "Template item  type - "+item.getType()+"  value "+item.getValue() );



                        data.setData( ( "<"+item.getValue()+"/>" ).getBytes() );

                        data.setIsError( Boolean.FALSE );

                        data.setIsXml( Boolean.TRUE );

                        break;

                    default:

                        log.warn( "Unknown template item  - "+item.getType()+"  "+item.getValue() );

                }

            }

            catch (Exception e)

            {

                log.error( "Error process template item", e );

                data.setData( ExceptionTools.getStackTrace( e, 30, "<br>" ).getBytes() );

                data.setIsError( Boolean.TRUE );

                data.setIsXml( Boolean.FALSE );

            }

            ctxInstance.session.removeAttribute( Constants.PORTLET_REQUEST_SESSION );

            ctxInstance.portlets.add( data );

        }

    }



    private static Object syncCtxDebug = new Object();

    private void processPortletData(CtxInstance ctxInstance)

        throws Exception

    {



        ByteArrayOutputStream xml = null;

        for ( int i = 0; i<ctxInstance.portlets.size(); i++ )

        {

            SitePortletDataType item = (SitePortletDataType)ctxInstance.portlets.get( i );



            if ( log.isDebugEnabled() )

                log.debug( "getIsError() - "+item.getIsError()+", getIsXml() - "+item.getIsXml() );



            if ( Boolean.TRUE.equals(item.getIsError()) || !Boolean.TRUE.equals(item.getIsXml()) )

            {

                if (xml!=null)

                {

                    processTransforming( xml, ctxInstance );

                    xml = null;

                }



                if ( log.isDebugEnabled() )

                {

                    synchronized(syncCtxDebug)

                    {

                        MainTools.writeToFile( WebmillConfig.getWebmillDebugDir()+"ctx-from-url.xml", item.getData() );

                    }

                }



                ctxInstance.byteArrayOutputStream.write( item.getData() );

            }

            else

            {

                // 1st xml element

                if ( xml==null )

                {

                    xml = new ByteArrayOutputStream();

//<META HTTP-EQUIV="Expires" CONTENT="Wed, 01 Jan 1986 00:00:01 GMT">

//<META HTTP-EQUIV="Last-Modified" CONTENT="Wed, 17 Mar 2010 10:13:25 GMT">

                    xml.write(

                        ( "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"+

                        "<SiteTemplate language=\""+

                        ctxInstance.page.currentLocale+"\" type=\""+ctxInstance.getType()+"\">" ).getBytes()

                    );

                }



                xml.write( item.getData() );



                if ( log.isDebugEnabled() )

                {

//                    log.debug("#1.1 "+(i+1));

//                    log.debug("#1.2 "+(i+1 >= portlets.size()) );

//                    log.debug("#1.3 "+!((SitePortletDataType)portlets.elementAt(i)).getIsXml() );

                    log.debug(

                        "#1.4 "+( i+1>=ctxInstance.portlets.size() ||

                        !Boolean.TRUE.equals(( (SitePortletDataType)ctxInstance.portlets.get( i+1 ) ).getIsXml())

                        )

                    );

                }



                if ( i+1>=ctxInstance.portlets.size() ||

                    !Boolean.TRUE.equals(( (SitePortletDataType)ctxInstance.portlets.get( i+1 ) ).getIsXml())

                )

                {

                    processTransforming( xml, ctxInstance );

                    xml = null;

                }

            }

        }

    }



//    private static Object syncTransform = new Object();

/*

    private void processTransforming( ByteArrayOutputStream xml, CtxInstance ctxInstance )

        throws IOException, TransformerException

    {

        xml.write( "</SiteTemplate>".getBytes() );



        if ( log.isDebugEnabled() )

            log.debug( "string to transforming\n"+xml );



        xml.flush();

        xml.close();



        ByteArrayInputStream stream = new ByteArrayInputStream( xml.toByteArray() );

        Source xmlSource = new StreamSource( stream );



        synchronized(syncTransform)

        {

            ctxInstance.transformer.transform( xmlSource, new StreamResult( ctxInstance.byteArrayOutputStream ) );

        }

    }

*/

    private void processTransforming( ByteArrayOutputStream xml, CtxInstance ctxInstance )

        throws Exception

    {

        xml.write( "</SiteTemplate>".getBytes() );



        if ( log.isDebugEnabled() )

            log.debug( "string to transforming\n"+xml );



        xml.flush();

        xml.close();



        ByteArrayInputStream stream = new ByteArrayInputStream( xml.toByteArray() );

        Source xmlSource = new StreamSource( stream );



        try

        {

            ctxInstance.xslt.getTransformer().transform( xmlSource, new StreamResult( ctxInstance.byteArrayOutputStream ) );

        }

        catch(javax.xml.transform.TransformerException e)

        {

            try

            {

                log.error("Xalan version - " + org.apache.xalan.Version.getVersion());

            }

            catch(Throwable e1)

            {

                log.error("Error get version of xalan", e1);

            }

            try

            {

                log.error("Xerces version - " + org.apache.xerces.impl.Version.getVersion() );

            }

            catch(Exception e2)

            {

                log.error("Error get version of xerces", e2);

            }

            log.error("TransformerException, try to recreate Transformer", e);



            try

            {

                ctxInstance.xslt.reinitTransformer();

            }

            catch(Exception e01)

            {

                log.error("General exception reintTransformer()", e01);

                throw e01;

            }

            catch(Error e02)

            {

                log.error("General error reintTransformer()", e02);

                throw e02;

            }

            ctxInstance.xslt.getTransformer().transform( xmlSource, new StreamResult( ctxInstance.byteArrayOutputStream ) );

        }

    }



    private static Object syncSitePortletDataList = new Object();

    /**

     *  Main method for processing pages with portlets

     * @param ctxInstance

     */

    private void processPage( CtxInstance ctxInstance )

    {

        if ( log.isDebugEnabled() )

        {

            log.debug( "Dynamic content  type - "+ctxInstance.getType() );

            log.debug( "name template - "+ctxInstance.getNameTemplate() );

        }



        try

        {

            ctxInstance.template = ctxInstance.page.p.templates.getTemplate(

                ctxInstance.getNameTemplate(), ctxInstance.page.currentLocale.toString()

            );



            if ( ctxInstance.template==null )

            {

                String errorString = "Template for  "+ctxInstance.getNameTemplate()+" not found";

                log.warn( errorString );

                ctxInstance.byteArrayOutputStream.write( errorString.getBytes() );

                return;

            }



            if ( log.isDebugEnabled() )

                log.debug( " start process 'model' portlet" );



            // run all 'models' portlet

            for ( int i = 0; i<ctxInstance.template.getSiteTemplateItemCount(); i++ )

            {

                TemplateItemType item = ctxInstance.template.getSiteTemplateItem( i );



                if ( log.isDebugEnabled() )

                {

                    log.debug( "item - "+item );

                    if ( item!=null )

                    {

                        log.debug( "item.getType() - "+item.getType() );

                    }

                }



                if ( item.getType().getType()==TemplateItemTypeTypeType.PORTLET_TYPE )

                {

                    PortletType d = PortletManager.getPortletDescription( item.getValue() );



                    if ( log.isDebugEnabled() )

                    {

                        log.debug( " portlet - "+item.getValue() );

                        log.debug( " portlet  desc - "+d );

                        if ( d!=null )

                        {

                            String typePortletTemp =

                                PortletTools.getStringParam(

                                    d, PortletTools.type_portlet

                                );



                            log.debug( " portlet  desc.type - "+typePortletTemp );

                        }

                    }





                    if ( d!=null)

                    {

                        String typePortletTemp =

                            PortletTools.getStringParam(

                                d, PortletTools.type_portlet

                            );



                        if ( typePortletTemp!=null &&

                            typePortletTemp.equals(

                                PortletDescriptionTypeTypePortletType.MODEL.toString()

                            )

                        )

                        {

                            ctxInstance.setParameters(

                                PortletTools.getParameters(ctxInstance.req),

                                PortletTools.getStringParam(d, PortletTools.locale_name_package)

                            );

                            ctxInstance.session.setAttribute( Constants.PORTLET_REQUEST_SESSION, ctxInstance );



                            Boolean isUrlTemp =

                                PortletTools.getBooleanParam(

                                    d, PortletTools.is_url

                                );



                            if ( log.isDebugEnabled() )

                                log.debug( "process 'model' portlet - "+item.getValue()+" file "+d.getPortletClass()+", isUrl - "+isUrlTemp );



                            boolean flag = true;

                            if ( isUrlTemp!=null && !isUrlTemp.booleanValue() )

                            {

                                String nameFile = PropertiesProvider.getApplicationPath()+d.getPortletClass();

                                if ( nameFile.indexOf( '?' )!=-1 )

                                    nameFile = nameFile.substring( 0, nameFile.indexOf( '?' ) );



                                File testFile = new File( nameFile );

                                if ( !testFile.exists() )

                                {

                                    String errorString = "Portlet "+ctxInstance.getType()+" refered to file "+testFile+" is broken";

                                    ctxInstance.byteArrayOutputStream.write( errorString.getBytes() );

                                    log.error( errorString );

                                }

                            }



                            if ( flag )

                                ServletUtils.include( ctxInstance.req, ctxInstance.response, d.getPortletClass(), new StringWriter() );



                            ctxInstance.session.removeAttribute( Constants.PORTLET_REQUEST_SESSION );

                        }

                    }

                }

            }

            if ( log.isDebugEnabled() )

                log.debug( " end process 'model' portlets" );



            // 'model' portlets is processed

            // run of others portlets





            ctxInstance.byteArrayOutputStream.write( copyright.getBytes() );



            if ( log.isDebugEnabled() )

                log.debug( "Locale request - "+ctxInstance.page.currentLocale.toString() );



            // prepare Xsl objects

            if ( ctxInstance.page.p.xsltList==null )

            {

                String errorString =

                    "<html><head></head<body><p>XSL template not defined</p></body></html>";



                log.error( errorString );

                ctxInstance.byteArrayOutputStream.write( errorString.getBytes() );

                return;

            }



            ctxInstance.xslt = ctxInstance.page.p.xsltList.getXslt( ctxInstance.page.currentLocale.toString() );



            if ( ctxInstance.xslt==null )

            {

                String errorString = "Index XSLT for locale "+ctxInstance.page.currentLocale+" not defined.";

                log.error( errorString );

                ctxInstance.byteArrayOutputStream.write( errorString.getBytes() );

                return;

            }



            processTemplateItem( ctxInstance );



            if ( log.isDebugEnabled() )

            {

                synchronized(syncSitePortletDataList)

                {

                    if (ctxInstance.portlets!=null)

                    {

                        SitePortletDataListType tmp = new SitePortletDataListType();



                        tmp.setPortlet( (ArrayList)ctxInstance.portlets );



                        log.debug( "Unmarshal SitePortletDataListType object" );

                        FileWriter w = new FileWriter( WebmillConfig.getWebmillDebugDir()+"site-portlets-data.xml" );

                        Marshaller marsh = new Marshaller( w );

                        marsh.setMarshalAsDocument( true );

                        marsh.setEncoding( "windows-1251" );

                        marsh.marshal( tmp );

                        marsh = null;

                        w.flush();

                        w.close();

                        w = null;

                    }

                    else

                        log.debug("(ArrayList)ctxInstance.portlets is null");

                }

            }

            processPortletData( ctxInstance );



        }

        catch (Exception e)

        {

            log.error( "Error build page.<br>Name template - "+ctxInstance.getNameTemplate()+

                "<br>"+"Dynamic type - "+ctxInstance.getType(), e );

            try

            {

                String errorString = "Error build page.<br>Name template - "+ctxInstance.getNameTemplate()+

                    "<br>"+"Dynamic type - "+ctxInstance.getType()+"<br>"+

                    ExceptionTools.getStackTrace( e, 15, "<BR>" );

                ctxInstance.byteArrayOutputStream.write( errorString.getBytes() );

            }

            catch (Exception e01)

            {

            }

        }

    }



    private void processIndexPage( CtxInstance ctxInstance)

    {

        try

        {

            processPage( ctxInstance );



        }

        catch (Exception e)

        {

            log.error( "Error build index page", e );

            try

            {

                String errorString = "Error build index page"+

                    ExceptionTools.getStackTrace( e, 15, "<BR>" );

                ctxInstance.byteArrayOutputStream.write( errorString.getBytes() );

            }

            catch (Exception e01)

            {

            }

        }

    }



    public void doPost( HttpServletRequest request, HttpServletResponse response )

        throws IOException, ServletException

    {

        if ( log.isDebugEnabled() )

            log.debug( "method is POST" );



        doGet( request, response );

    }



    private static Object syncCounter = new Object();

    private static int counterNDC = 0;

    public void doGet( HttpServletRequest request_, HttpServletResponse response_ )

        throws IOException, ServletException

    {

        boolean isAnotherLoop = true;

        CtxInstance ctxInstance = new CtxInstance();



        synchronized(syncCounter)

        {

            if (log.isDebugEnabled())

                log.debug("counterNDC #1 "+counterNDC);



            ctxInstance.counter = counterNDC;

            ++counterNDC;



            if (log.isDebugEnabled())

            {

                log.debug("counterNDC #2 "+counterNDC);

                log.debug("counter #3 "+ctxInstance.counter);

            }



            NDC.push( ""+ctxInstance.counter );



            if (log.isDebugEnabled())

            {

                log.debug("counterNDC #4 "+counterNDC);

                log.debug("counter #5 "+ctxInstance.counter);

            }

        }





        if (log.isDebugEnabled())

        {

            log.debug("counter #6 "+ctxInstance.counter);

            log.debug("this "+this);

            log.debug("request_ "+request_);

            log.debug("response_ "+response_);

        }



        int countLoop = 0;

        while ( isAnotherLoop && countLoop++<3)

        {

            isAnotherLoop = false;



            try

            {

                boolean isSessionValid = request_.isRequestedSessionIdValid();



                if ( log.isDebugEnabled() )

                    log.debug( "session ID is valid - "+isSessionValid );



                if ( !isSessionValid )

                {

                    if ( log.isInfoEnabled() )

                        log.info( "invalidate current session " );



                    try

                    {

                        HttpSession tempSession = request_.getSession( false );

                        if ( tempSession!=null )

                            tempSession.invalidate();



                        request_.getSession( true );

                    }

                    catch (java.lang.IllegalStateException e)

                    {

                        log.warn( "Error invalidate session", e );

                    }



                    if ( log.isInfoEnabled() )

                        log.info( "Status of new session ID is - "+request_.isRequestedSessionIdValid() );



                }



                ctxInstance.session = request_.getSession( true );

                ctxInstance.req = request_;

                ctxInstance.response = response_;



                ctxInstance.byteArrayOutputStream = new ByteArrayOutputStream( 10000 );



                setContentType( response_ );



                try

                {

                    ctxInstance.db = DatabaseAdapter.getInstance( false );

                }

                catch (Exception e)

                {

                    log.error( "Error create DBconnect", e );



                    String errorString = "Error create DBconnect<br>"+

                        ExceptionTools.getStackTrace( e, 30 );

                    ctxInstance.byteArrayOutputStream.write( errorString.getBytes() );

                    break;

                }



                if ( log.isDebugEnabled() )

                {

                    log.debug( "Request URL - "+ctxInstance.req.getRequestURL() );

                    log.debug( "Request query string - "+ctxInstance.req.getQueryString() );



                    for ( Enumeration e = request_.getParameterNames(); e.hasMoreElements(); )

                    {

                        String s = (String)e.nextElement();

                        log.debug( "Request attr - "+s+", value - "+ServletUtils.getString( request_, s ) );

                    }

                }



                // create InitPage object

                long jspPageMills = 0;

                try

                {

                    if ( log.isInfoEnabled() )

                    {

                        log.info( "start create InitPage object " );

                        jspPageMills = System.currentTimeMillis();

                    }

                    ctxInstance.page = new InitPage( ctxInstance.db, ctxInstance.req );

                }

                catch (Exception e)

                {

                    log.error( "Error create InitPage ", e );



                    String errorString = "Error create object InitPage<br>"+

                        ExceptionTools.getStackTrace( e, 30, "<BR>" );

                    ctxInstance.byteArrayOutputStream.write( errorString.getBytes() );

                    break;

                }

                finally

                {

                    if ( log.isInfoEnabled() )

                    {

                        log.info( "Create InitPage object for  "

                            +( System.currentTimeMillis()-jspPageMills )+" milliseconds" );

                    }

                }



                if ( log.isDebugEnabled() )

                    log.debug( "type "+ctxInstance.getType()+

                        " type is '"+Constants.CTX_TYPE_INDEX+"' - "+Constants.CTX_TYPE_INDEX.equals( ctxInstance.getType() ) );



                // if menu item with requested type of context not found or requested 'index'.

                // going to index page

                if ( ctxInstance.page.getPortletDescription()==null || Constants.CTX_TYPE_INDEX.equals( ctxInstance.getType() ) )

                {

                    processIndexPage( ctxInstance );

                    break;

                }



                switch (ctxInstance.page.getPortletDescription().getPortletType().getType())

                {

                    case PortletDescriptionTypeTypePortletType.CONTROLLER_TYPE:



                        PortletType portlet = ctxInstance.page.getPortletDescription().getPortletConfig();



                        ctxInstance.setParameters(

                            PortletTools.getParameters(ctxInstance.req),

                            PortletTools.getStringParam(portlet, PortletTools.locale_name_package)

                        );

                        ctxInstance.session.setAttribute( Constants.PORTLET_REQUEST_SESSION, ctxInstance );



                        Boolean isUrlTemp =

                            PortletTools.getBooleanParam( portlet, PortletTools.is_url );



                        if ( isUrlTemp!=null && isUrlTemp.booleanValue() )

                        {

                            RequestDispatcher dispatcher = ctxInstance.req.getRequestDispatcher( ctxInstance.page.getPortletDescription().getPortletConfig().getPortletClass() );

                            if ( log.isDebugEnabled() )

                                log.debug( "RequestDispatcher - "+dispatcher );



                            if ( dispatcher==null )

                            {

                                String errorString = "Error get dispatcher for path "+ctxInstance.page.getPortletDescription().getPortletConfig().getPortletClass();

                                log.error( errorString );

                                ctxInstance.byteArrayOutputStream.write( errorString.getBytes() );

                            }

                            else

                                dispatcher.forward( ctxInstance.req, ctxInstance.response );

                        }

                        else

                        {

                            ctxInstance.byteArrayOutputStream.write(

                                "Error forward to 'controller' portlet. Portlet path is null".getBytes()

                            );

                        }

                        ctxInstance.session.removeAttribute( Constants.PORTLET_REQUEST_SESSION );

                        break;



                    case PortletDescriptionTypeTypePortletType.MODEL_TYPE:



                        String errorString = "Portlet with type 'model' can't bind to Menu";

                        log.error( errorString );

                        ctxInstance.byteArrayOutputStream.write( errorString.getBytes() );

                        break;



                    case PortletDescriptionTypeTypePortletType.VIEW_TYPE:

                        if ( log.isDebugEnabled() )

                        {

                            log.debug( "Portlet desc - "+ctxInstance.page.getPortletDescription() );

                            log.debug( "namePortlet ID - "+

                                PortletTools.getStringParam(

                                    ctxInstance.page.getPortletDescription().getPortletConfig(), PortletTools.name_portlet_id

                                )

                            );

                        }

                        processPage( ctxInstance );

                        break;



                    default:

                        errorString = "Unknown type of portlet - "+

                            PortletTools.getStringParam(

                                ctxInstance.page.getPortletDescription().getPortletConfig(), PortletTools.type_portlet

                            );

                        log.error( errorString );

                        ctxInstance.byteArrayOutputStream.write( errorString.getBytes() );

                        break;

                }

                break;

            }

            catch (Throwable e)

            {

                log.error( "Error processing page with ContextNavigator", e );

                log.error( "CN debug. desc "+ctxInstance.page.getPortletDescription() );

                if ( ctxInstance.page.getPortletDescription()!=null )

                {

                    String typePortletTemp = PortletTools.getStringParam(

                            ctxInstance.page.getPortletDescription().getPortletConfig(), PortletTools.type_portlet

                        );

                    log.error( "CN debug. desc.getTypePortlet() "+ typePortletTemp );



                    if ( typePortletTemp!=null )

                        log.error( "CN debug. desc.getTypePortlet().getType() "+typePortletTemp );

                }

                log.error( "CN debug. type "+ctxInstance.getType() );

                if ( log.isDebugEnabled() )

                {

                    log.error( "CN debug. Request URL - "+ctxInstance.req.getRequestURL() );

                    log.error( "CN debug. Request query string - "+ctxInstance.req.getQueryString() );



                    for ( Enumeration en = request_.getParameterNames(); en.hasMoreElements(); )

                    {

                        String s = (String)en.nextElement();

                        try

                        {

                            log.error( "CN debug. Request attr - "+s+", value - "+ServletUtils.getString( request_, s ) );

                        }

                        catch(ConfigException exc)

                        {



                        }

                    }

                }



                String errorString = "Error processing page with ContextNavigator<br>"+

                    ExceptionTools.getStackTrace( e, 30, "<br>");

                ctxInstance.byteArrayOutputStream.write( errorString.getBytes() );

                break;

            }

            finally

            {

                try

                {

                    String timeString = "\n<!-- NDC #"+ctxInstance.counter+", page with portlets processed for "+( System.currentTimeMillis()-ctxInstance.startMills )+" milliseconds -->";



                    if ( log.isInfoEnabled() )

                        log.info( timeString );



                    if ( ctxInstance.byteArrayOutputStream==null )

                    {

                        log.info("byteArrayOutputStream is null, try to recover data and start another loop for this request");

                        isAnotherLoop = true;

                        continue;

                    }



                    ctxInstance.byteArrayOutputStream.write( timeString.getBytes() );



                    Writer out = response_.getWriter();

                    if ( out==null )

                    {

                        String fatalString = "fatal error - response.getWriter() is null";

                        log.fatal( fatalString );

                        throw new ServletException( fatalString );

                    }



                    ctxInstance.byteArrayOutputStream.flush();

                    ctxInstance.byteArrayOutputStream.close();

                    response_.setContentLength( ctxInstance.byteArrayOutputStream.size() );

                    out.write(

                        new String( ctxInstance.byteArrayOutputStream.toByteArray(), WebmillConfig.getHtmlCharset() )

                    );



                    out.flush();

                    out.close();

                    out = null;

                    ctxInstance.byteArrayOutputStream = null;

                    DatabaseAdapter.close( ctxInstance.db );

                    ctxInstance.db = null;



                    Long maxMemory = null;

                    try

                    {

                        maxMemory = MainTools.getMaxMemory();

                    }

                    catch (Exception e)

                    {

                    }



                    log.warn(

                        "free memory "+Runtime.getRuntime().freeMemory()+

                        " total memory "+Runtime.getRuntime().totalMemory()+

                        ( maxMemory!=null ? " max memory "+maxMemory : "" )

                    );

                }

                catch(Throwable th)

                {

                    log.error("Throwable ", th);

                }

                finally

                {

                }

            }

        }

        NDC.remove();

    }



    public static void setContentType(HttpServletResponse response, String charset)

            throws Exception

    {

        if (log.isDebugEnabled())

            log.debug("set new charset - "+charset);



        try

        {

            response.setContentType("text/html; charset=" + charset);

        }

        catch(Exception e)

        {

            log.error("Error set new content type to "+charset);

            throw e;

        }

    }



    public static void setContentType(HttpServletResponse response)

            throws Exception

    {

        setContentType(response, WebmillConfig.getHtmlCharset());

    }

}