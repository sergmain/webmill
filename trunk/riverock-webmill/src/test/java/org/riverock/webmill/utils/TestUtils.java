package org.riverock.webmill.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.ByteArrayOutputStream;

/**
 * User: SMaslyukov
 * Date: 18.07.2007
 * Time: 12:54:10
 */
public class TestUtils {
    
    public static String getResourceAsString(String uri) throws IOException {
        InputStream is = TestUtils.class.getResourceAsStream(uri);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        int count;
        byte[] bytes = new byte[0x100];
        while((count=is.read(bytes))!=-1) {
            stream.write(bytes, 0, count);
        }
        is.close();
        stream.close();
        return new String(stream.toByteArray());
    }
}
