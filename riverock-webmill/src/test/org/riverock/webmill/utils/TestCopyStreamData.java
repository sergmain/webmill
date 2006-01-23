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
