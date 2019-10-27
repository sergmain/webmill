package org.riverock.webmill.common;

import java.io.InputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import junit.framework.TestCase;

/**
 * User: SergeMaslyukov
 * Date: 22.08.2007
 * Time: 22:43:16
 */
public class TestStringConvertional extends TestCase {

    public void testRussianString() throws IOException {
        InputStream is = TestStringConvertional.class.getResourceAsStream("/xml/language/russian-cp1251.xml");
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        int count;
        byte[] bytes = new byte[100];
        while ((count = is.read(bytes))!=-1) {
            os.write(bytes, 0, count);
        }
        is.close();
        os.flush();
        os.close();
        byte[] originBytes = os.toByteArray();

        String s = new String(originBytes, "Cp1251");
        byte[] bytesUtf8 = s.getBytes("utf-8");
        String sUtf8 = new String(bytesUtf8, "utf-8");
        byte[] bytesCp1251 = sUtf8.getBytes("Cp1251");

        assertEquals(originBytes.length, bytesCp1251.length);
        for (int i = 0; i < originBytes.length; i++) {
            assertEquals("Check byte #"+i, bytesCp1251[i], originBytes[i]);
        }

    }

    public void testJapanString() throws IOException {
        InputStream is = TestStringConvertional.class.getResourceAsStream("/xml/language/japan-utf-8.xml");
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        int count;
        byte[] bytes = new byte[100];
        while ((count = is.read(bytes))!=-1) {
            os.write(bytes, 0, count);
        }
        is.close();
        os.flush();
        os.close();
        byte[] originBytes = os.toByteArray();

        String s = new String(originBytes, "utf-8");
        byte[] bytesUtf8 = s.getBytes("utf-8");
        String sUtf8 = new String(bytesUtf8, "utf-8");
        byte[] bytesCp1251 = sUtf8.getBytes("utf-8");

        assertEquals(originBytes.length, bytesCp1251.length);
        for (int i = 0; i < originBytes.length; i++) {
            assertEquals("Check byte #"+i, bytesCp1251[i], originBytes[i]);
        }

    }
}
