/*
 * org.riverock.webmill.container - Webmill portlet container implementation
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
package org.riverock.webmill.container.resource;

import java.util.*;

import org.riverock.webmill.container.portlet.bean.PortletDefinition;
import org.riverock.webmill.container.portlet.bean.PortletInfo;
import org.riverock.webmill.container.tools.ContainertStringUtils;

/**
 * User: SergeMaslyukov
 * Date: 10.02.2005
 * Time: 0:26:44
 * $Id$
 */
public final class PortletResourceBundleWithLocale extends ResourceBundle {

    private Map<String, String> properties = null;
    private ResourceBundle resourceBundle = null;
    private String resourceBundleClassName = null;
    private Locale locale = null;

    private static final String PORTLET_TITLE = "javax.portlet.title";
    private static final String PORTLET_SHORT_TITLE = "javax.portlet.short-title";
    private static final String PORTLET_KEYWORDS = "javax.portlet.keywords";

    public String getResourceBundleClassName() {
        return resourceBundleClassName;
    }

    public String getLocaleName() {
        return locale.toString();
    }

    protected static PortletResourceBundleWithLocale getInstance(
        final PortletDefinition portletDefinition, final Locale locale, final ClassLoader classLoader  ) {

        return new PortletResourceBundleWithLocale( portletDefinition, locale, classLoader );
    }

    private PortletResourceBundleWithLocale( final PortletDefinition portletDefinition, final Locale locale, final ClassLoader classLoader ) {
        this.properties = new HashMap<String, String>();
        properties.put( PORTLET_KEYWORDS, "" );
        properties.put( PORTLET_SHORT_TITLE, "" );
        properties.put( PORTLET_TITLE, "" );

        this.resourceBundleClassName = portletDefinition.getResourceBundle();
        this.locale = locale;

        initPropertiesFromResourceBundle( resourceBundleClassName, classLoader );
        initPropertiesFromPortletInfo(portletDefinition.getPortletInfo());
    }

    private void initPropertiesFromResourceBundle( final String resourceBundleClass, final ClassLoader classLoader ) {
        if (ContainertStringUtils.isBlank(resourceBundleClass)) {
            return;
        }
        try {
            System.out.println(
                "Create resource bundle for class: " + resourceBundleClass + ", locale: " + locale.toString() +
                ", class loader: " + classLoader );

            resourceBundle = ResourceBundle.getBundle( resourceBundleClass, locale, classLoader );
        }
        catch (MissingResourceException e) {

            System.out.println("Error create resource bundle");
            e.printStackTrace( System.out );

            String name = "/" + resourceBundleClass.replace( '.', '/');

            java.net.URL url;
            url = classLoader.getResource(name);
            System.out.println("resource: " + name+", url: "+url);

            String n = name+".properties";
            url = classLoader.getResource(n);
            System.out.println("resource: " + n+", url: "+url);

            if (locale!=null) {
                if (locale.getCountry()!=null) {
                    n = name + '_' + locale.toString() + ".properties";
                    url = classLoader.getResource(n);
                    System.out.println("resource: " + n+", url: "+url);
                }
                n = name + '_' + locale.getLanguage() + ".properties";
                url = classLoader.getResource(n);
                System.out.println("resource: " + n + ", url: "+url);
            }
            else {
                System.out.println("Locale is null");
            }
        }
    }

    private void initPropertiesFromPortletInfo(PortletInfo portletInfo) {

        if ( portletInfo != null ) {

            if ( portletInfo.getKeywords() != null ) {
                Iterator<String> iterator = portletInfo.getKeywords().iterator();
                StringBuilder keywords = new StringBuilder();
                boolean isFirst = true;
                while (iterator.hasNext()) {
                    if ( isFirst ) {
                        isFirst = false;
                    }
                    else {
                        keywords.append( ',' );
                    }
                    keywords.append( iterator.next() );

                }
                properties.put( PORTLET_KEYWORDS, keywords.toString() );
            }

            if ( portletInfo.getShortTitle() != null )
                properties.put( PORTLET_SHORT_TITLE, portletInfo.getShortTitle() );

            if ( portletInfo.getTitle() != null )
                properties.put( PORTLET_TITLE, portletInfo.getTitle() );
        }

    }

    public Enumeration<String> getKeys() {

        Set<String> keys = new HashSet<String>();
        if (resourceBundle!=null) {
            final Enumeration<String> en = resourceBundle.getKeys();
            while(en.hasMoreElements()) {
                String key = en.nextElement();
                keys.add( key );
            }
        }

        if ( properties != null )
            keys.addAll( properties.keySet() );


        return Collections.enumeration( keys );
    }

    protected Object handleGetObject( final String key ) {

        if (key==null) {
            throw new NullPointerException("Key is null");
        }
        Object obj = null;
        if (resourceBundle!=null) {
            try {
                obj = resourceBundle.getObject( key );
            }
            catch (MissingResourceException e) {
                // not throw java.util.MissingResourceException, search in properties
            }
            if (obj!=null) {
                return obj;
            }
        }

        obj = properties.get( key );
        return obj;
    }
}
