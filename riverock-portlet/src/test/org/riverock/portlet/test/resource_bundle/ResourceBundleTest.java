package org.riverock.portlet.test.resource_bundle;

import java.util.ResourceBundle;
import java.util.Locale;
import java.io.InputStream;
import java.io.IOException;

import org.apache.commons.digester.Digester;
import org.xml.sax.SAXException;

import org.riverock.webmill.container.resource.XmlResourceBundle;

/**
 * @author smaslyukov
 *         Date: 10.08.2005
 *         Time: 21:59:10
 *         $Id$
 */
public class ResourceBundleTest {

    public static void main( String[] args ) throws Exception {

        Locale locale = new Locale("ru","RU");
        System.out.println( "locale = " + locale );


        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        InputStream stream = ResourceBundleTest.class.getResourceAsStream( "/org/riverock/portlet/resource/SiteHamradio_ru_RU.xml" );

        XmlResourceBundle.PairList pairList;
        Digester digester = new Digester();
        digester.setValidating(false);

        digester.addObjectCreate("resource", XmlResourceBundle.PairList.class);

        digester.addObjectCreate("resource/pair", XmlResourceBundle.Pair.class);
        digester.addSetProperties("resource/pair", "key", "key");
        digester.addSetProperties("resource/pair", "value", "value");
        digester.addSetNext("resource/pair", "addPair");

        pairList = (XmlResourceBundle.PairList)digester.parse(stream);

        ResourceBundle bundle = ResourceBundle.getBundle( "org.riverock.portlet.resource.SiteHamradio", locale );

        System.out.println( "nex: " + bundle.getString( "main.next" ) );

    }
}
