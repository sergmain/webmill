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
import org.riverock.interfaces.schema.javax.portlet.SupportedLocaleType;
import org.riverock.webmill.exception.PortalException;
import org.riverock.webmill.schema.core.MainLanguageListType;
import org.riverock.webmill.schema.core.MainLanguageItemType;
import org.riverock.webmill.core.GetMainLanguageFullList;
import org.riverock.common.tools.StringTools;
import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.db.DatabaseManager;

import org.apache.log4j.Logger;

/**
 * User: SergeMaslyukov
 * Date: 10.02.2005
 * Time: 0:22:19
 * $Id$
 */
public final class PortletResourceBundle {
    private final static Logger log = Logger.getLogger( PortletResourceBundle.class );

    private Map portletLocales = new HashMap();

    static PortletResourceBundle getInstance( final PortletType portletDefinition ) throws PortalException {
        return new PortletResourceBundle(portletDefinition);
    }

    private PortletResourceBundle( final PortletType portletDefinition ) throws PortalException {

        // extract locales from portletDefinition and create for each locales
        // instance of PortletResourceBundleWithLocale
        Collection locales = buildLocaleList(portletDefinition);
        Iterator it = locales.iterator();
        while (it.hasNext()) {
            String locale = (String)it.next();

//            Map map = new HashMap( portletLocales );
//            map.put( locale, PortletResourceBundleWithLocale.getInstance( portletDefinition, locale ) );
//            portletLocales = Collections.unmodifiableMap( map );

            portletLocales.put(
                locale,
                PortletResourceBundleWithLocale.getInstance( portletDefinition, locale )
            );
        }
        portletLocales = Collections.unmodifiableMap( portletLocales );
    }

    public ResourceBundle getResourceBundle( final Locale locale ){
        if (locale==null)
            return null;

        ResourceBundle resourceBundle = null;
        resourceBundle = (ResourceBundle)portletLocales.get( locale.toString() );
        if ( log.isDebugEnabled() ) {
            log.debug( "ResourceBundle for locale " + locale.toString() + " is: " +resourceBundle);
        }
        if (resourceBundle!=null)
            return resourceBundle;

        Locale temp = null;
        if (!StringTools.isEmpty( locale.getVariant()) ) {
            temp = new Locale( locale.getLanguage(), locale.getCountry() );
            resourceBundle = (ResourceBundle)portletLocales.get( temp.toString() );
            if ( log.isDebugEnabled() ) {
                log.debug( "Drop locale variant. ResourceBundle for locale " + temp.toString() + " is: " +resourceBundle);
            }
            if (resourceBundle!=null) {
                return resourceBundle;
            }
        }

        if (!StringTools.isEmpty( locale.getCountry())) {
            temp = new Locale( locale.getLanguage());
            resourceBundle = (ResourceBundle)portletLocales.get( temp.toString() );
            if ( log.isDebugEnabled() ) {
                log.debug( "Drop locale country. ResourceBundle for locale " + temp.toString() + " is: " +resourceBundle);
            }
            if (resourceBundle!=null) {
                return resourceBundle;
            }
        }

        return new PortletResourceBundleEmpty();
    }

    private static Collection buildLocaleList( final PortletType portletDefinition ) throws PortalException {
        Collection list = new HashSet();
        DatabaseAdapter adapter = null;
        try {
            adapter = DatabaseAdapter.getInstance();
            MainLanguageListType languages = GetMainLanguageFullList.getInstance( adapter, new Long(1) ).item;
            for (int i=0; i<languages.getMainLanguageCount(); i++){
                MainLanguageItemType item = languages.getMainLanguage( i );
                list.add( item.getShortNameLanguage() );
            }
        }
        catch (Exception e) {
            String es = "Error get list of locales for portlet "+portletDefinition.getPortletName().getContent();
            log.error(es, e);
            throw new PortalException(es,e );
        }
        finally{
            DatabaseManager.close(adapter);
            adapter = null;
        }

        // add locales from portlet definition
        for (int i=0; i<portletDefinition.getSupportedLocaleCount(); i++) {
            SupportedLocaleType locale = portletDefinition.getSupportedLocale(i);
            if (!StringTools.isEmpty(locale.getContent())) {
                list.add( locale.getContent() );
            }
        }

        return list;
    }
}
