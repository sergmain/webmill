package org.riverock.webmill.test.class_loader;

import java.io.InputStream;

/**
 * @author Serge Maslyukov
 *         Date: 23.04.2005
 *         Time: 23:44:23
 *         $Id$
 */
public class ClassLoaderTest {
    public static void main(String[] s) throws Exception {

        String res = "/org/riverock/webmill/test/class_loader/prop/LocaleTest.properties";
        InputStream inputStream = ClassLoaderTest.class.getClassLoader().getResourceAsStream( res );

        System.out.println("inputStream = " + inputStream);

    }
}
