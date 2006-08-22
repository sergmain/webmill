/*
 * org.riverock.webmill.container -- Webmill portlet container implementation
 *
 * Copyright (C) 2004, Riverock Software, All Rights Reserved.
 *
 * Riverock -- The Open-source Java Development Community
 * http://www.riverock.org
 *
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 *
 */
package org.riverock.webmill.container.resource;

import java.util.*;

import org.apache.commons.lang.StringUtils;

import org.riverock.webmill.container.portlet.bean.PortletDefinition;
import org.riverock.webmill.container.portlet.bean.PortletInfo;

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

        PortletResourceBundleWithLocale a = new PortletResourceBundleWithLocale( portletDefinition, locale, classLoader );
        return a;
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
        if (StringUtils.isBlank(resourceBundleClass)) {
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
                Iterator<String> iterator = portletInfo.getKeywords();
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
