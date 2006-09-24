/*
 * org.riverock.generic - Database connectivity classes, part of Webmill portal
 * For more information about Webmill portal, please visit project site
 * http://webmill.askmore.info
 *
 * Copyright (C) 2000-2006, Riverock Software, All Rights Reserved.
 *
 * Riverock - The Open-source Java Development Community
 * http://www.riverock.org
 *
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.riverock.generic.test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.zip.GZIPOutputStream;

import org.apache.log4j.Logger;

/**
 * Author: mill
 * Date: Feb 13, 2003
 * Time: 4:43:06 PM
 *
 * $Id$
 */
public class GzipTest
{
    private static Logger cat = Logger.getLogger("org.riverock.test.GzipTest");

    public GzipTest()
    {
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

    private static String writeToFile(String file_, byte bytes[])
        throws Exception
    {

        FileOutputStream out = new FileOutputStream(file_, false);

        out.write(bytes, 0, bytes.length);
        out.flush();
        out.close();
        out = null;

        return "File " + file_ + " создан успешно";
    }

    public static void main(String[] s)
        throws Exception
    {
        File tempFile = new File("source");

        byte[] bytes = getFileBytes(
                new FileInputStream(tempFile), 10000000
        );

        cat.debug("Length to gzip " + bytes.length);

        byte[] bytesToEncrypt = null;

        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();

        GZIPOutputStream gzip = new GZIPOutputStream(byteStream);
        gzip.write(bytes);
        gzip.flush();
        gzip.close();

        bytesToEncrypt = byteStream.toByteArray();


        writeToFile("source.gzip", byteStream.toByteArray());

/*
        GZIPInputStream gzip1 = new GZIPInputStream();
        cat.debug("write UnGZiped data in file " + tempDir + "file.gzip.3");
        cat.debug( writeToFile(tempDir + "file.gzip.3", getFileBytes(gzip1, 10000000)) );
*/
    }

}
