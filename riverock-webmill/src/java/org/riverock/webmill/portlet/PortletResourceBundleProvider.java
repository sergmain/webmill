package org.riverock.webmill.portlet;

import java.util.*;

import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.db.DatabaseManager;
import org.riverock.generic.exception.GenericException;
import org.riverock.generic.tools.StringManager;
import org.riverock.interfaces.schema.javax.portlet.PortletType;
import org.riverock.webmill.core.GetMainLanguageFullList;
import org.riverock.webmill.exception.PortalException;
import org.riverock.webmill.schema.core.MainLanguageItemType;
import org.riverock.webmill.schema.core.MainLanguageListType;

import org.apache.log4j.Logger;

/**
 * User: SergeMaslyukov
 * Date: 06.12.2004
 * Time: 14:36:12
 * $Id$
 */
public final class PortletResourceBundleProvider {
    private final static Logger log = Logger.getLogger( PortletResourceBundleProvider.class );

    private static Map messages = null;
    private static Map portlets = new HashMap();

    public static PortletResourceBundle getInstance( final PortletType portletDefinition ) throws GenericException, PortalException {

        if (messages==null)
            messages = StringManager.prepareMessages();

        Object obj = portlets.get( portletDefinition.getPortletName() );
        if (obj!=null)
            return (PortletResourceBundle)obj;

        PortletResourceBundle bundle = PortletResourceBundle.getInstance( portletDefinition );
        portlets.put( portletDefinition.getPortletName(), bundle );
        return bundle;
    }

    public static class PortletResourceBundle {

        private Map portletLocales = new HashMap();

        private static PortletResourceBundle getInstance( final PortletType portletDefinition ) throws PortalException {
            return new PortletResourceBundle(portletDefinition);
        }

        private PortletResourceBundle( final PortletType portletDefinition ) throws PortalException {

            // extract locales from portletDefinition and create for each locales
            // instance of PortletResourceLocale
            List locales = buildLocaleList(portletDefinition);
            Iterator it = locales.iterator();
            while (it.hasNext()){
                String locale = (String)it.next();
                portletLocales.put(locale, PortletResourceLocale.getInstance( portletDefinition, locale ));
            }
        }

        public ResourceBundle getResourceBundle( final Locale locale ){
            if (locale==null)
                return null;

            return (ResourceBundle)portletLocales.get( locale.toString() );
        }
    }

    public final static class PortletResourceLocale extends ResourceBundle {

        private String localePackageName = null;
        private String locale = null;

        protected static PortletResourceLocale getInstance( final PortletType portletDefinition, final String locale ) {
            PortletResourceLocale a = new PortletResourceLocale(portletDefinition, locale);
            return a;
        }

        private PortletResourceLocale( final PortletType portletDefinition, final String locale ){
            this.localePackageName = PortletTools.getStringParam( portletDefinition, PortletTools.locale_name_package );
            this.locale = locale;

            // init resource from portletDefinition (portlet.xml file)

            // init resourceBundle from class
        }

        public Enumeration getKeys() {
            if (messages==null)
                return Collections.enumeration( new ArrayList(0) );

            return Collections.enumeration( messages.keySet() );
        }

        protected Object handleGetObject( final String key ) {
            if ( log.isDebugEnabled() ) {
                log.debug( "Looking for key: "+key);
                log.debug( "messages: "+messages);
            }

            // looking for key in bundle, which defined in portlet definition



            // looking for key in DB
            if ( log.isDebugEnabled() ) {
                log.debug( "full key: "+localePackageName + '-' + locale + '-' + key);
            }
            Object obj = messages.get( localePackageName + '-' + locale + '-' + key );
            if ( messages == null )
                return null;

            if ( log.isDebugEnabled() ) {
                log.debug( "message: "+obj);
            }
            return obj;
        }
    }

    private static List buildLocaleList( final PortletType portletDefinition ) throws PortalException {
        List list = new LinkedList();
        DatabaseAdapter adapter = null;
        try {
            adapter = DatabaseAdapter.getInstance( false );
            MainLanguageListType languages = GetMainLanguageFullList.getInstance( adapter, new Long(1) ).item;
            for (int i=0; i<languages.getMainLanguageCount(); i++){
                MainLanguageItemType item = languages.getMainLanguage( i );
                list.add( item.getShortNameLanguage() );
            }
        } catch (Exception e) {
            String es = "Error get list of locales for portlet "+portletDefinition.getPortletName();
            log.error(es, e);
            throw new PortalException(es,e );
        } finally{
            DatabaseManager.close(adapter);
            adapter = null;
        }

        // Todo: add locales from portlet definition here


        return list;
    }
}
