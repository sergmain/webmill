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
 * Date: Feb 19, 2003
 * Time: 9:07:31 AM
 *
 * $Id$
 */

package org.riverock.webmill.as.server;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.StringReader;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipEntry;

import org.apache.log4j.Logger;
import org.xml.sax.InputSource;
import org.exolab.castor.xml.Unmarshaller;

import org.riverock.webmill.schema.appl_server.ResourceRequestParameterType;
import org.riverock.webmill.schema.appl_server.ResourceRequestType;
import org.riverock.webmill.schema.appl_server.types.ResourceCompressionTypeTypeCompressionType;
import org.riverock.webmill.config.WebmillConfig;
import org.riverock.common.tools.Base64;
import org.riverock.common.tools.MainTools;

public class ApplicationTools
{
    private static Logger cat = Logger.getLogger("org.riverock.webmill.as.server.ApplicationTools");

    private final static int COUNT_READ_BYTES = 1000;

    public ApplicationTools()
    {
    }

    public static String decodeParameter( ResourceRequestParameterType param )
        throws Exception
    {
        int typeCompression = -1;
        if ( param.getCompression() != null )
            typeCompression = param.getCompression().getTypeCompression().getType();

        ByteArrayOutputStream outBytes = null;
        ByteArrayInputStream bytes = null;
        byte[] temp = null;
        int countRead = 0;
        String errorString = null;
        byte[] bb = null;
        if (Boolean.TRUE.equals(param.getIsBase64()) )
            bb = Base64.decode( param.getValueParameter().getBytes());
        else
            bb = param.getValueParameter().getBytes();

        switch( typeCompression )
        {
            case ResourceCompressionTypeTypeCompressionType.ZIP_TYPE:

                outBytes = new ByteArrayOutputStream(10000);

                errorString = "Array of bytes after base64 decode is null. Base64 encode is wrong";
                if (cat.isDebugEnabled())
                    cat.debug( errorString );

                if (bb==null)
                    throw new Exception( errorString );

                bytes = new ByteArrayInputStream( bb );

                if (cat.isDebugEnabled())
                {
//                    MainTools.writeToFile(
//                        InitParam.getMillDebugDir()+"as-request-decode-zip.bin",
//                        Base64.decode( param.getValueParameter().getBytes() )
//                    );
                }
                ZipInputStream zip = new ZipInputStream( bytes );

                ZipEntry entry = zip.getNextEntry();
                temp = new byte[COUNT_READ_BYTES];
                while ( true )
                {
                    countRead = zip.read(temp, 0, COUNT_READ_BYTES);

                    if (countRead==-1)
                        break;

                    if (cat.isDebugEnabled())
                        cat.debug("count of bytes from zip array - "+countRead );

                    outBytes.write(temp, 0, countRead);
                    if (zip.available()==0)
                        break;

                }

//                if (cat.isDebugEnabled())
//                    cat.debug("start encode to base64 object. data length - " + .length);

                break;

            case ResourceCompressionTypeTypeCompressionType.GZIP_TYPE:

                outBytes = new ByteArrayOutputStream(10000);
                errorString = "Array of bytes after base64 decode is null. Base64 encode is wrong";
                if (cat.isDebugEnabled())
                    cat.debug( errorString );

                if (bb==null)
                    throw new Exception( errorString );

                bytes = new ByteArrayInputStream( bb );

                GZIPInputStream gzip = new GZIPInputStream( bytes );

                temp = new byte[COUNT_READ_BYTES];
                while ( true )
                {
                    countRead = gzip.read(temp, 0, COUNT_READ_BYTES);
                    outBytes.write(temp, 0, countRead);
                    if (gzip.available()==0)
                        break;

                }


            case ResourceCompressionTypeTypeCompressionType.NONE_TYPE:
            default:

                if (cat.isDebugEnabled())
                {
                    cat.debug("Process without compression");
                    cat.debug("request parameter - "+bb);
                }

//                MainTools.writeToFile(InitParam.getMillDebugDir()+"as-req-param-wo_compress.xml", bb );

                if (cat.isDebugEnabled())
                    cat.debug("Result - "+new String( bb ));

                return new String( bb );
        }
        byte[] outByteArray = outBytes.toByteArray();

        if (cat.isDebugEnabled())
        {
            cat.debug("");
            cat.debug("request parameter after deconpression - "+outByteArray);
        }
//        MainTools.writeToFile(InitParam.getMillDebugDir()+"as-req-param-1.xml", outByteArray );
        return new String( outByteArray );
    }

    public static void main(String[] s)
        throws Exception
    {
        org.riverock.generic.startup.StartupApplication.init();

        String str =
//            "<ResourceRequest><CodeResource>prr.getIDirectionDBItemTypeList</CodeResource><Parameters><NameParameter>prr.id</NameParameter><ValueParameter>UEsDBBQAAgAIAGViVC6379yDAwAAAAEAAAAOAAAAVmFsdWVQYXJhbWV0ZXIzBABQSwECAAAUAAIACABlYlQut+/cgwMAAAABAAAADgAAAAAAAAAAAAAAAAAAAAAAVmFsdWVQYXJhbWV0ZXJQSwUGAAAAAAEAAQA8AAAALwAAAAAA</ValueParameter><Compression typeCompression=\"ZIP\"/></Parameters><Parameters><NameParameter>prr.test</NameParameter><ValueParameter>UEsDBBQAAgAIAGViVC67/6XO5wAAAI4BAAAOAAAAVmFsdWVQYXJhbWV0ZXJtUO9LwzAQ/VdkXwUjCDohFi5NHRlTl7lmP77V9ugCra1JSpl/vYnD0W3eh8e9d7l34dEF2qYzOS7wq0PrIg==</ValueParameter><Compression typeCompression=\"ZIP\"/></Parameters><ResponseCompressionType typeCompression=\"ZIP\"/></ResourceRequest>";
//            "<ResourceRequest><CodeResource>prr.getIDirectionDBItemTypeList</CodeResource><Parameters><NameParameter>prr.id</NameParameter><ValueParameter>UEsDBBQAAgAIAGViVC6379yDAwAAAAEAAAAOAAAAVmFsdWVQYXJhbWV0ZXIzBABQSwECAAAUAAIACABlYlQut+/cgwMAAAABAAAADgAAAAAAAAAAAAAAAAAAAAAAVmFsdWVQYXJhbWV0ZXJQSwUGAAAAAAEAAQA8AAAALwAAAAAA</ValueParameter><Compression typeCompression=\"ZIP\"/></Parameters><Parameters><NameParameter>prr.test</NameParameter><ValueParameter>UEsDBBQAAgAIAGViVC67/6XO5wAAAI4BAAAOAAAAVmFsdWVQYXJhbWV0ZXJtUO9LwzAQ/VdkXwUjCDohFi5NHRlTl7lmP77V9ugCra1JSpl/vYnD0W3eh8e9d7l34dEF2qYzOS7wq0PrIg==</ValueParameter><Compression typeCompression=\"ZIP\"/></Parameters><ResponseCompressionType typeCompression=\"ZIP\"/></ResourceRequest>";
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?><ResourceRequest><CodeResource>prr.getIDirectionDBItemTypeList</CodeResource><Parameters><NameParameter>prr.id</NameParameter><ValueParameter>MQ==</ValueParameter><Compression typeCompression=\"NONE\"/></Parameters><Parameters><NameParameter>prr.test</NameParameter><ValueParameter>PD94bWwgdmVyc2lvbj0iMS4wIiBlbmNvZGluZz0iVVRGLTgiPz48UmVzb3VyY2VSZXF1ZXN0PjxDb2RlUmVzb3VyY2U+7+4t8PPx8eroPC9Db2RlUmVzb3VyY2U+PFBhcmFtZXRlcnM+PE5hbWVQYXJhbWV0ZXI+cHJyLmlkPC9OYW1lUGFyYW1ldGVyPjxWYWx1ZVBhcmFtZXRlcj5NUT09PC9WYWx1ZVBhcmFtZXRlcj48Q29tcHJlc3Npb24gdHlwZUNvbXByZXNzaW9uPSJOT05FIi8+PC9QYXJhbWV0ZXJzPjwvUmVzb3VyY2VSZXF1ZXN0Pg==</ValueParameter><Compression typeCompression=\"NONE\"/></Parameters><ResponseCompressionType typeCompression=\"ZIP\"/></ResourceRequest>";
/*
            "<ResourceRequest>" +
            "<CodeResource>prr.getIDirectionDBItemTypeList</CodeResource>" +
            "<Parameters>" +
            "<NameParameter>prr.id</NameParameter>" +
            "<ValueParameter>UEsDBBQAAgAIALZcUy6379yDAwAAAAEAAAAOAAAAVmFsd" +
            "WVQYXJhbWV0ZXIzBABQSwECAAAUAAIACAC2XFMut+/cgw" +
            "MAAAABAAAADgAAAAAAAAAAAAAAAAAAAAAAVmFsdWVQYXJhbWV0ZXJQSw" +
            "UGAAAAAAEAAQA8AAAALwAAAAAA</ValueParameter>" +
            "<Compression typeCompression=\"ZIP\"/>" +
            "</Parameters>" +
            "<ResponseCompressionType typeCompression=\"ZIP\"/>" +
            "</ResourceRequest>";
*/
        InputSource inSrc = new InputSource( new StringReader(str) );
        ResourceRequestType res = (ResourceRequestType) Unmarshaller.unmarshal(ResourceRequestType.class, inSrc);


        String result = decodeParameter(res.getParameters(0));

        String result1 = decodeParameter(res.getParameters(1));

        MainTools.writeToFile(WebmillConfig.getWebmillDebugDir()+"as-req-param.xml", result1.getBytes());

        InputSource inSrc1 = new InputSource( new StringReader(result1) );
        ResourceRequestType res1 = (ResourceRequestType) Unmarshaller.unmarshal(ResourceRequestType.class, inSrc1);



        System.out.println(result);
//        System.out.println(  org.apache.commons.httpclient.("+");
    }

}
