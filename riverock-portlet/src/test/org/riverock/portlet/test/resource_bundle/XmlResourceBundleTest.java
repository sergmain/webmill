package org.riverock.portlet.test.resource_bundle;

import java.util.Properties;
import java.util.PropertyResourceBundle;
import java.io.OutputStream;
import java.io.FileOutputStream;

/**
 * @author SergeMaslyukov
 *         Date: 01.12.2005
 *         Time: 16:09:56
 *         $Id$
 */
public class XmlResourceBundleTest {

    public static void main( String[] args ) throws Exception {
        Properties properties = new Properties( );

        properties.setProperty( "aaa", "»»»" );
        properties.setProperty( "aaa1", "»»»" );
        properties.setProperty( "aaa2", "»»»" );
        properties.setProperty( "aaa3", "»»»" );

        OutputStream stream = new FileOutputStream( "test.xml" );
        properties.storeToXML( stream, "test xml resource" );
        properties = null;
        stream.close();
        PropertyResourceBundle bundle = new PropertyResourceBundle( );
    }
}
