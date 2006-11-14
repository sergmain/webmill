/*
 * org.riverock.webmill - Webmill portal with support jsr-168, xml/xslt and others things.
 * For more information, please visit project site http://webmill.riverock.org
 *
 * Copyright (C) 2000-2006, Riverock Software, All Rights Reserved.
 *
 * Riverock - The Open-source Java Development Community
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
 */
package org.riverock.webmill.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Random;

import junit.framework.TestCase;

/**
 * @author SergeMaslyukov
 *         Date: 15.01.2006
 *         Time: 13:20:38
 *         $Id$
 */
public class TestCopyStreamData extends TestCase {

    public void testCopyStream0() throws Exception {
         runCopyStream(0);
    }

    public void testCopyStream100() throws Exception {
         runCopyStream(100);
    }

    public void testCopyStream512() throws Exception {
         runCopyStream(PortletUtils.BUFFER_SIZE);
    }

    public void testCopyStream1024() throws Exception {
         runCopyStream(PortletUtils.BUFFER_SIZE*2);
    }

    public void testCopyStream20013() throws Exception {
         runCopyStream(PortletUtils.BUFFER_SIZE*40 + 13);
    }


    private static void runCopyStream(int bufferSize) throws Exception{
        final byte[] buf = new byte[bufferSize];
        Random random = new Random();
        for( int i=0; i< bufferSize; i++) {
            buf[i] = (byte)random.nextInt();
        }

        ByteArrayInputStream inputStream = new ByteArrayInputStream( buf );
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        PortletUtils.copyData( inputStream, result );

        byte[] resultBytes = result.toByteArray();

        if (buf.length!=resultBytes.length || buf.length!=bufferSize) {
            throw new Exception("Size of buffers mismatch" );
        }
        for (int i=0; i<buf.length; i++) {
            if (buf[i]!=resultBytes[i]) {
                throw new Exception("Error in result buffer");
            }
        }
    }
}
