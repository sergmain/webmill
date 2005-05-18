/*
 * org.riverock.webmill -- Portal framework implementation
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
package org.riverock.webmill.portlet;

import java.util.*;

import org.riverock.interfaces.schema.javax.portlet.PortletType;
import org.riverock.interfaces.schema.javax.portlet.PortletTypeChoice;
import org.riverock.interfaces.schema.javax.portlet.PortletInfoType;
import org.riverock.interfaces.schema.javax.portlet.PortletTypeChoiceSequence;
import org.riverock.common.tools.StringTools;

import org.apache.log4j.Logger;

/**
 * User: SergeMaslyukov
 * Date: 10.02.2005
 * Time: 0:26:44
 * $Id$
 */
public final class PortletResourceBundleWithLocale extends ResourceBundle {
    private final static Logger log = Logger.getLogger( PortletResourceBundleWithLocale.class );

    private String localePackageName = null;
    private String locale = null;
    private Properties properties = null;

    private static final String PORTLET_TITLE = "javax.portlet.title";
    private static final String PORTLET_SHORT_TITLE = "javax.portlet.short-title";
    private static final String PORTLET_KEYWORDS = "javax.portlet.keywords";

    protected static PortletResourceBundleWithLocale getInstance( final PortletType portletDefinition, final String locale ) {
        PortletResourceBundleWithLocale a = new PortletResourceBundleWithLocale( portletDefinition, locale );
        return a;
    }

    private PortletResourceBundleWithLocale( final PortletType portletDefinition, final String locale ) {
        this.localePackageName = PortletTools.getStringParam( portletDefinition, PortletTools.locale_name_package );
        this.locale = locale;
        this.properties = new Properties();

        // init resource from portletDefinition (portlet.xml file)
        PortletTypeChoice choice = portletDefinition.getPortletTypeChoice();
        if ( log.isDebugEnabled() ) {
            log.debug( "portlet name: " + portletDefinition.getPortletName().getContent() + ", choice: " + choice );
        }
        if ( choice != null ) {
            PortletInfoType portletInfoType = choice.getPortletInfo();

            if ( log.isDebugEnabled() ) {
                log.debug( "portletInfoType: " + portletInfoType );
                log.debug( "getPortletTypeChoiceSequence: " + choice.getPortletTypeChoiceSequence() );

                if ( portletInfoType != null ) {
                    log.debug( "portletInfoType.getKeywords(): " + ( portletInfoType.getKeywords() != null ?portletInfoType.getKeywords().getContent() :"null" ) );
                    log.debug( "portletInfoType.getShortTitle(): " + ( portletInfoType.getKeywords() != null ?portletInfoType.getShortTitle().getContent() :"null" ) );
                    log.debug( "portletInfoType.getTitle(): " + ( portletInfoType.getKeywords() != null ?portletInfoType.getTitle().getContent() :"null" ) );
                }
            }
            if (choice.getPortletTypeChoiceSequence()!=null) {
                portletInfoType = choice.getPortletTypeChoiceSequence().getPortletInfo();
                if ( log.isDebugEnabled() ) {
                    log.debug( "portletInfoType: " + portletInfoType );
                    log.debug( "getPortletTypeChoiceSequence: " + choice.getPortletTypeChoiceSequence() );

                    if ( portletInfoType != null ) {
                        log.debug( "portletInfoType.getKeywords(): " + ( portletInfoType.getKeywords() != null ?portletInfoType.getKeywords().getContent() :"null" ) );
                        log.debug( "portletInfoType.getShortTitle(): " + ( portletInfoType.getKeywords() != null ?portletInfoType.getShortTitle().getContent() :"null" ) );
                        log.debug( "portletInfoType.getTitle(): " + ( portletInfoType.getKeywords() != null ?portletInfoType.getTitle().getContent() :"null" ) );
                    }
                }
                initPropertiesFromResourceBundle(choice.getPortletTypeChoiceSequence(), locale);
            }
            initPropertiesFromPortletInfo(portletInfoType);
        }

        // init resourceBundle from class

    }

    private void initPropertiesFromResourceBundle(PortletTypeChoiceSequence portletTypeChoiceSequence, String localeString) {
        log.debug( "in initPropertiesFromResourceBundle" );
        if ( portletTypeChoiceSequence != null && portletTypeChoiceSequence.getResourceBundle()!=null) {
            String className = portletTypeChoiceSequence.getResourceBundle().getContent();
            if (StringTools.isEmpty(className)) {
                return;
            }

            // Todo need implement ResourceBunde on class
//            PortletResourceBundleWithLocale.class.getClassLoader().loadClass("")
//            if ( portletInfoType.getKeywords() != null )
//                properties.load();put( PORTLET_KEYWORDS, portletInfoType.getKeywords().getContent() );
//
//            if ( portletInfoType.getKeywords() != null )
//                properties.put( PORTLET_SHORT_TITLE, portletInfoType.getShortTitle().getContent() );
//
//            if ( portletInfoType.getKeywords() != null )
//                properties.put( PORTLET_TITLE, portletInfoType.getTitle().getContent() );
        }
        log.debug( "out initPropertiesFromPortletInfo" );
    }

    private void initPropertiesFromPortletInfo(PortletInfoType portletInfoType) {
        log.debug( "out initPropertiesFromResourceBundle" );

        if ( portletInfoType != null ) {

            if ( portletInfoType.getKeywords() != null )
                properties.put( PORTLET_KEYWORDS, portletInfoType.getKeywords().getContent() );

            if ( portletInfoType.getKeywords() != null )
                properties.put( PORTLET_SHORT_TITLE, portletInfoType.getShortTitle().getContent() );

            if ( portletInfoType.getKeywords() != null )
                properties.put( PORTLET_TITLE, portletInfoType.getTitle().getContent() );
        }
        log.debug( "out initPropertiesFromPortletInfo" );
    }

    public Enumeration getKeys() {

        Map messages = PortletResourceBundleProvider.getMessagesMapInternal();

        if ( messages == null && properties == null )
            return Collections.enumeration( new LinkedList() );

        List keys = new LinkedList();
        if ( properties != null )
            keys.addAll( properties.keySet() );

        if ( messages != null )
            keys.addAll( messages.keySet() );

        return Collections.enumeration( keys );
    }

    protected Object handleGetObject( final String key ) {

        Map messages = PortletResourceBundleProvider.getMessagesMapInternal();

        if ( log.isDebugEnabled() ) {
            log.debug( "Looking for key: " + key );
            log.debug( "messages " + ( messages == null ?" not " :"is " ) + "null" );
        }

        // looking for key in bundle, which defined in portlet definition
        if ( properties != null ) {
            Object obj = properties.getProperty( key );
            if ( log.isDebugEnabled() ) {
                log.debug( "message from portlet.xml: " + ( obj != null ?obj.toString() :" is null" ) );
            }
            if ( obj != null )
                return obj;
        }

        // looking for key in DB
        if ( log.isDebugEnabled() ) {
            log.debug( "full key: " + localePackageName + '-' + locale + '-' + key );
        }
        Object obj = messages.get( localePackageName + '-' + locale + '-' + key );
        if ( messages == null )
            return null;

        if ( log.isDebugEnabled() ) {
            log.debug( "message: " + obj );
        }
        return obj;
    }
}
