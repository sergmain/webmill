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

import org.riverock.webmill.container.portlet.bean.PortletDefinition;
import org.riverock.webmill.container.portlet.bean.PortletInfo;
import org.riverock.webmill.container.tools.PortletService;

/**
 * User: SergeMaslyukov
 * Date: 10.02.2005
 * Time: 0:26:44
 * $Id$
 */
public final class PortletResourceBundleWithLocale extends ResourceBundle {

    private Map<String, String> properties = null;
    private ResourceBundle resourceBundle = null;
//    private ResourceBundle resourceBundleWebmill = null;
    private String resourceBundleClassName = null;
    private String localeName = null;

    private static final String PORTLET_TITLE = "javax.portlet.title";
    private static final String PORTLET_SHORT_TITLE = "javax.portlet.short-title";
    private static final String PORTLET_KEYWORDS = "javax.portlet.keywords";

    public String getResourceBundleClassName() {
        return resourceBundleClassName;
    }

    public String getLocaleName() {
        return localeName;
    }

    protected static PortletResourceBundleWithLocale getInstance( final PortletDefinition portletDefinition, final String locale ) {
        PortletResourceBundleWithLocale a = new PortletResourceBundleWithLocale( portletDefinition, locale );
        return a;
    }

    private PortletResourceBundleWithLocale( final PortletDefinition portletDefinition, final String localeName ) {
        this.properties = new HashMap<String, String>();
        this.resourceBundleClassName = portletDefinition.getResourceBundle();
        this.localeName = localeName;

        initPropertiesFromResourceBundle(resourceBundleClassName, localeName);
        initPropertiesFromPortletInfo(portletDefinition.getPortletInfo());
    }

    private void initPropertiesFromResourceBundle(String resourceBundleClass, String localeName) {
        if (PortletService.isEmpty(resourceBundleClass)) {
            return;
        }
        Locale locale = PortletService.getLocale( localeName );
        ClassLoader classLoader = null;
        try {
            System.out.println( "Create resource bundle for class " + resourceBundleClass + ", locale: " + locale.toString() );

            classLoader = Thread.currentThread().getContextClassLoader();
            resourceBundle = ResourceBundle.getBundle( resourceBundleClass, locale, classLoader );
        }
        catch (MissingResourceException e) {
/*
            String className = resourceBundleClass+"_"+ locale;
            System.out.println("className = " + className);
            try {
                Class test = Class.forName( className );
            }
            catch (ClassNotFoundException e1) {
                e1.printStackTrace();
            }
            try {
                System.out.println("ClassLoader: " + classLoader );
                final Class<?> aClass = classLoader.loadClass( className );
                System.out.println("aClass = " + aClass);
            }
            catch (ClassNotFoundException e1) {
                e1.printStackTrace();
            }
*/
            System.out.println("Error create resource bundle");
            e.printStackTrace( System.out );
        }
    }

    private void initPropertiesFromPortletInfo(PortletInfo portletInfo) {

        if ( portletInfo != null ) {

            if ( portletInfo.getKeywords() != null ) {
                Iterator<String> iterator = portletInfo.getKeywords();
                while (iterator.hasNext()) {
                    String keyword = iterator.next();
                    properties.put( PORTLET_KEYWORDS, keyword );
                }
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

        Object obj = null;
        if ( properties != null ) {
            obj = properties.get( key );

            if ( obj != null )
                return obj;
        }

        if (resourceBundle!=null) {
            obj = resourceBundle.getObject( key );
            if (obj!=null) {
                return obj;
            }
        }

        return null;
    }
}
