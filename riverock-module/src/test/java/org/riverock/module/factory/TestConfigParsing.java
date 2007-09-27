package org.riverock.module.factory;

import java.io.InputStream;

import junit.framework.TestCase;

import org.riverock.module.factory.config.ActionConfigInstance;

/**
 * User: SMaslyukov
 * Date: 27.09.2007
 * Time: 18:16:23
 */
public class TestConfigParsing extends TestCase {

    public void testParsing() throws Exception {
        InputStream is = TestConfigParsing.class.getResourceAsStream("/xml/register-action.xml");
        ActionConfigInstance.getInstance(is);
        assertNotNull(is);
    }

}
