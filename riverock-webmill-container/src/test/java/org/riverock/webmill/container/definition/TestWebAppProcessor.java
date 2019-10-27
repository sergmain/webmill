package org.riverock.webmill.container.definition;

import java.io.InputStream;
import java.io.ByteArrayOutputStream;

import junit.framework.TestCase;
import com.sun.xml.bind.v2.util.ByteArrayOutputStreamEx;

/**
 * User: SMaslyukov
 * Date: 02.10.2007
 * Time: 17:49:21
 */
public class TestWebAppProcessor extends TestCase {


    public void testVersion_2_3() throws Exception {
        InputStream is = TestWebAppProcessor.class.getResourceAsStream("/xml/web-app/v2_3/web.1.xml");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStreamEx();
        int count;
        byte[] bytes = new byte[0x200];
        while((count=is.read(bytes))!=-1) {
            outputStream.write(bytes, 0, count);
        }
        bytes = outputStream.toByteArray();

        WebXmlDefinitionProcessor p = DefinitionProcessorFactory.getWebXmlDefinitionProcessor();


        WebAppVersion v = p.getWebAppVersion(bytes);
        assertEquals(WebAppVersion.WEB_APP_2_3, v );
    }

    public void testVersion_2_4() throws Exception {
        InputStream is = TestWebAppProcessor.class.getResourceAsStream("/xml/web-app/v2_4/web.xml");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStreamEx();
        int count;
        byte[] bytes = new byte[0x200];
        while((count=is.read(bytes))!=-1) {
            outputStream.write(bytes, 0, count);
        }
        bytes = outputStream.toByteArray();

        WebXmlDefinitionProcessor p = DefinitionProcessorFactory.getWebXmlDefinitionProcessor();


        WebAppVersion v = p.getWebAppVersion(bytes);
        assertEquals(WebAppVersion.WEB_APP_2_4, v );
    }
}
