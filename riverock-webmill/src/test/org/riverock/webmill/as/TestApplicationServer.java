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
 * User: serg_main
 * Date: 11.01.2004
 * Time: 15:23:16
 * @author Serge Maslyukov
 * $Id$
 */

package org.riverock.webmill.as;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipInputStream;

import org.riverock.common.tools.Base64;
import org.riverock.generic.tools.XmlTools;
import org.riverock.webmill.as.server.ApplicationServerConstants;
import org.riverock.webmill.as.service.SimpleApplicationServerProvider;
import org.riverock.webmill.schema.appl_server.ResourceCompressionType;
import org.riverock.webmill.schema.appl_server.ResourceRequestParameterType;
import org.riverock.webmill.schema.appl_server.ResourceRequestType;
import org.riverock.webmill.schema.appl_server.ResourceResponseType;
import org.riverock.webmill.schema.appl_server.StringResultType;
import org.riverock.webmill.schema.appl_server.types.ResourceCompressionTypeTypeCompressionType;
import org.riverock.webmill.schema.appl_server.types.ResourceResponseTypeErrorTypeType;

import com.meterware.httpunit.GetMethodWebRequest;
import com.meterware.httpunit.HttpUnitOptions;
import com.meterware.httpunit.PostMethodWebRequest;
import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebRequest;
import com.meterware.httpunit.WebResponse;
import junit.framework.TestCase;
import org.exolab.castor.xml.Unmarshaller;
import org.xml.sax.InputSource;


public class TestApplicationServer extends TestCase
{

    public TestApplicationServer(String testName)
    {
        super(testName);
    }

    private String nameAppl = null;
    private String username = null;
    private String password = null;
    private boolean isAuth = false;
    private boolean isRoleValid = true;
    private boolean isUserPasswordValid = true;
    private String sessionCode = null;

    public void testApplServer()
        throws Exception
    {

        nameAppl = "webmill.as.simple";
        System.out.println("nameAppl = " + nameAppl);
        testPool();

        nameAppl = "webmill.as.simple-with-auth";
        username = "qqq";
        password = "www";
        isAuth = true;
        System.out.println("nameAppl = " + nameAppl);
        testPool();

        nameAppl = "webmill.as.simple-with-auth";
        username = "qqq";
        password = "beee";
        isAuth = true;
        isRoleValid = true;
        isUserPasswordValid = false;
        System.out.println("wrong password check nameAppl = " + nameAppl);
        testPool();

        nameAppl = "webmill.as.simple-with-wrong-auth";
        password = "www";
        isRoleValid = true; // user 'qqq' have 'root' right
        isUserPasswordValid = true;
        System.out.println("wrong role, but check with root rights, nameAppl = " + nameAppl);
        testPool();

        nameAppl = "webmill.as.simple-with-wrong-auth";
        username = "qqq1";
        password = "www1";
        isRoleValid = false;
        isUserPasswordValid = true;
        System.out.println("wrong role check nameAppl = " + nameAppl);
        testPool();

    }

    private void testPool() throws Exception
    {
        processResource(true, ResourceCompressionTypeTypeCompressionType.NONE);
        processResource(true, ResourceCompressionTypeTypeCompressionType.GZIP);
        processResource(true, ResourceCompressionTypeTypeCompressionType.ZIP);

        processResource(false, ResourceCompressionTypeTypeCompressionType.NONE);
        processResource(false, ResourceCompressionTypeTypeCompressionType.GZIP);
        processResource(false, ResourceCompressionTypeTypeCompressionType.ZIP);
    }

    private void processResource(boolean isPost, ResourceCompressionTypeTypeCompressionType compressType) throws Exception
    {
        ArrayList params = null;
        if (isAuth)
        {
            params = new ArrayList();
            ResourceRequestParameterType p = null;

            p = new ResourceRequestParameterType();
            p.setNameParameter(ApplicationServerConstants.USER_LOGIN_NAME_PARAM);
            p.setValueParameter(username);
            params.add(p);

            p = new ResourceRequestParameterType();
            p.setNameParameter(ApplicationServerConstants.USER_PASSWORD_NAME_PARAM);
            p.setValueParameter(password);
            params.add(p);

            ResourceResponseType asResponse = getResourses(
                isPost, compressType, ApplicationServerConstants.NAME_LOGIN_MODULE, params
            );
//            System.out.println("asResponse.getIsError() = " + asResponse.getIsError());
            assertTrue( (new Boolean(!isUserPasswordValid)).equals(asResponse.getIsError()) );
            if (isUserPasswordValid)
            {
                sessionCode = extractResult(asResponse);

                InputSource inSrc = new InputSource( new StringReader(sessionCode) );
                StringResultType stringResult = (StringResultType) Unmarshaller.unmarshal(StringResultType.class, inSrc);
                assertNotNull(stringResult);

                sessionCode = stringResult.getResult();

                assertNotNull(sessionCode);
            }
            else
            {
                assertTrue( (new Boolean(!isUserPasswordValid)).equals(asResponse.getIsError()) );
                assertNotNull(asResponse.getErrorType());
                assertTrue(asResponse.getErrorType().getType()==ResourceResponseTypeErrorTypeType.ACCESS_DENIED.getType());
                return;
            }
        }

        ResourceResponseType asResponse = getResourses(isPost, compressType, nameAppl, params);
        System.out.println("asResponse.getIsError() = " + asResponse.getIsError());
        assertTrue( (new Boolean(!isRoleValid)).equals(asResponse.getIsError()) );
        if (isRoleValid)
        {
            String hello = extractResult(asResponse);
            assertTrue(SimpleApplicationServerProvider.hello.equals(hello));
            System.out.println("asResponse.getData() = " + hello);
        }
        else
        {
            assertNotNull(asResponse.getErrorType());
            assertTrue(asResponse.getErrorType().getType()==ResourceResponseTypeErrorTypeType.ACCESS_DENIED.getType());
            return;
        }

    }

    private String extractResult(ResourceResponseType asResponse) throws Exception
    {
        byte[] responceBytes = Base64.decode(asResponse.getData().getBytes());

//        System.out.println("responceBytes.length = " + responceBytes.length);
//        System.out.println("comptession = " + asResponse.getCompression().getTypeCompression().toString());
        String hello = null;
        ByteArrayInputStream byteInStream = null;
        InputStreamReader reader = null;
        char chars[] = null;
        StringBuffer buff = null;
        int count;
        switch(asResponse.getCompression().getTypeCompression().getType())
        {
            case ResourceCompressionTypeTypeCompressionType.NONE_TYPE:
                hello = new String(responceBytes);
                break;
            case ResourceCompressionTypeTypeCompressionType.GZIP_TYPE:
                byteInStream = new ByteArrayInputStream(responceBytes);
                GZIPInputStream gzip = new GZIPInputStream(byteInStream);

                reader = new InputStreamReader(gzip, "utf-8");
                chars = new char[1000];
                buff = new StringBuffer();
                count = 0;
                while ((count=reader.read(chars))!=-1)
                    buff.append(chars, 0, count);

                hello = buff.toString();

//                hello = new String(getFileBytes(gzip, 1000000));
                break;
            case ResourceCompressionTypeTypeCompressionType.ZIP_TYPE:
                byteInStream = new ByteArrayInputStream(responceBytes);
                ZipInputStream zip = new ZipInputStream(byteInStream);
                zip.getNextEntry();

                reader = new InputStreamReader(zip, "utf-8");
                chars = new char[1000];
                buff = new StringBuffer();
                count = 0;
                while ((count=reader.read(chars))!=-1)
                    buff.append(chars, 0, count);

                hello = buff.toString();
                break;
            default:
                throw new Exception("wrong type of compression");
        }
        return hello;
    }

    private ResourceResponseType getResourses(
        boolean isPost,
        ResourceCompressionTypeTypeCompressionType compressType,
        String moduleName,
        ArrayList params
        )
        throws Exception
    {
        WebConversation wc = new WebConversation();
        WebRequest req = null;
        WebResponse resp = null;
        HttpUnitOptions.setExceptionsThrownOnScriptError(false);
//        boolean isResponseOutputted = false;

        String serverURL = "http://me.askmore";
        String context = "/mill";
        String asServlet = "/mill.appl";

//        System.out.println("isAuth = " + isAuth);
//        System.out.println("sessionCode = " + sessionCode);
        String fullUrl = serverURL+context+asServlet+(isAuth&&sessionCode!=null?";jsessionid="+sessionCode:"");
        if (isPost)
        {
            PostMethodWebRequest post = new PostMethodWebRequest(fullUrl);
            post.setMimeEncoded(false);
            req = post;
        }
        else
            req = new GetMethodWebRequest(fullUrl);


        ResourceRequestType asReq = new ResourceRequestType();
        asReq.setCodeResource( moduleName );
        if (params!=null)
            asReq.setParameters(params);

        ResourceCompressionType compress = new ResourceCompressionType();
        compress.setTypeCompression(compressType);
        asReq.setResponseCompressionType(compress);

//        String parameter = StringTools.rewriteURL(
//            new String(XmlTools.getXml(asReq, null), "utf-8")
//        );
        String parameter = new String(XmlTools.getXml(asReq, null), "utf-8");

        req.setParameter( ApplicationServerConstants.NAME_APPL_PARAM, parameter);

        resp = wc.getResponse(req);

//        System.out.println("resp.getText() = " + resp.getText());


        InputSource inSrc = new InputSource( new StringReader(resp.getText()) );
        ResourceResponseType asResponse = (ResourceResponseType) Unmarshaller.unmarshal(ResourceResponseType.class, inSrc);
        return asResponse;
    }

    public static byte[] getFileBytes(InputStream in, int sizeBuff)
        throws Exception
    {

        int ch;
        byte buffBytes[] = new byte[sizeBuff];
        int i = 0;
        while (((ch = in.read()) != -1) && (i < sizeBuff))
            buffBytes[i++] = (byte) ch;

        in.close();
        byte retBytes[] = new byte[i];
        for (int j = 0; j < i; j++)
            retBytes[j] = buffBytes[j];

        return retBytes;
    }

}
