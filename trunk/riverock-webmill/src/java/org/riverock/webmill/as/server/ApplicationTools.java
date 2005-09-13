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
package org.riverock.webmill.as.server;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.log4j.Logger;

import org.riverock.common.tools.Base64;
import org.riverock.webmill.schema.appl_server.ResourceRequestParameterType;
import org.riverock.webmill.schema.appl_server.types.ResourceCompressionTypeTypeCompressionType;

/**
 * Author: mill
 * Date: Feb 19, 2003
 * Time: 9:07:31 AM
 *
 * $Id$
 */
public class ApplicationTools
{
    private static Logger cat = Logger.getLogger(ApplicationTools.class);

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
}
