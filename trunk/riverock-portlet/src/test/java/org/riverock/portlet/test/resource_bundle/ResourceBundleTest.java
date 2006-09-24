/*
 * org.riverock.portlet - Portlet Library
 *
 * Copyright (C) 2006, Riverock Software, All Rights Reserved.
 *
 * Riverock - The Open-source Java Development Community
 * http://www.riverock.org
 *
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.riverock.portlet.test.resource_bundle;

import java.util.ResourceBundle;
import java.util.Locale;
import java.io.InputStream;
import java.io.IOException;

import org.apache.commons.digester.Digester;
import org.xml.sax.SAXException;

import org.riverock.common.resource.CustomXmlResourceBundle;

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

        CustomXmlResourceBundle.PairList pairList;
        Digester digester = new Digester();
        digester.setValidating(false);

        digester.addObjectCreate("resource", CustomXmlResourceBundle.PairList.class);

        digester.addObjectCreate("resource/pair", CustomXmlResourceBundle.Pair.class);
        digester.addSetProperties("resource/pair", "key", "key");
        digester.addSetProperties("resource/pair", "value", "value");
        digester.addSetNext("resource/pair", "addPair");

        pairList = (CustomXmlResourceBundle.PairList)digester.parse(stream);

        ResourceBundle bundle = ResourceBundle.getBundle( "org.riverock.portlet.resource.SiteHamradio", locale );

        System.out.println( "nex: " + bundle.getString( "main.next" ) );

    }
}
