package org.riverock.portlet.jsf.validator;

import junit.framework.TestCase;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * User: SMaslyukov
 * Date: 17.07.2007
 * Time: 17:34:27
 */
public class TestTextValidator extends TestCase {

    public void testXmlValidator() throws Exception {
        String s;

        s = TextValidator.validateAsXml(
            getResourceAsString("/xml/jsf/validator/xml/broken_1.xml")
        );                       
        assertNotNull(s);

        s = TextValidator.validateAsXml(getResourceAsString("/xml/jsf/validator/xml/broken_2.xml"));
        assertNotNull(s);

        s = TextValidator.validateAsXml(getResourceAsString("/xml/jsf/validator/xml/valid_1.xml"));
        assertNull(s);
    }

    public void testXsltValidator() throws Exception {
        String s;

        s = TextValidator.validateAsXslt(
            getResourceAsString("/xml/jsf/validator/xslt/broken_1.xslt")
        );
        assertNotNull(s);

        s = TextValidator.validateAsXslt(
            getResourceAsString("/xml/jsf/validator/xslt/valid_1.xslt")
        );
        assertNull(s);

    }

    public void testEqualsMethod() {
        TextValidator v = new TextValidator("xml");
        TextValidator v1 = new TextValidator("xml");

        TextValidator x = new TextValidator("xslt");
        TextValidator x1 = new TextValidator("xslt");

        assertEquals(v, v);
        assertEquals(v1, v);
        assertEquals(v, v1);
        assertFalse( v.equals(null));
        assertFalse( v.equals(new Object()));

        assertEquals(x, x);
        assertEquals(x1, x);
        assertEquals(x, x1);
        assertFalse( x.equals(null));
        assertFalse( x.equals(new Object()));

        assertFalse( x.equals(v));
        assertFalse( v.equals(x));
    }

    private static String getResourceAsString(String uri) throws IOException {
        InputStream is = TestTextValidator.class.getResourceAsStream(uri);
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
