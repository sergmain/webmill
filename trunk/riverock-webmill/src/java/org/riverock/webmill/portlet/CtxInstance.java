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

import java.util.Locale;
import java.io.UnsupportedEncodingException;

import javax.portlet.PortletConfig;
import javax.portlet.RenderResponse;

import org.riverock.generic.config.GenericConfig;
import org.riverock.generic.tools.StringManager;
import org.riverock.webmill.main.Constants;

import org.apache.log4j.Logger;

/**
 * User: Admin
 * Date: Aug 26, 2003
 * Time: 4:40:19 PM
 *
 * $Id$
 */
public final class CtxInstance {
    private final static Logger log = Logger.getLogger(CtxInstance.class);

    public static String url(String portlet) {
        return null;
//        return url(portlet, contextFactory.getNameTemplate());
    }

    public static String url( String portlet, String templateParam, RenderResponse renderResponse ) {
//        return null;
        return renderResponse.encodeURL( ctx() ) + '?' +
            Constants.NAME_TYPE_CONTEXT_PARAM + '=' + portlet;
//            getAsURL()+
//            (templateParam!=null?("&" + Constants.NAME_TEMPLATE_CONTEXT_PARAM + '=' +templateParam):"");
    }

    /**
     * @deprecated Use ResourceBundle
     * @param locale
     * @return
     */
    public static StringManager getStringManager( Locale locale ) {
        return StringManager.getManager("mill.locale.main", locale);
    }

    /**
     * @deprecated Use ResourceBundle
     * @param locale
     * @return
     */
    public static String getStr( Locale locale, String key, PortletConfig portletConfig ) {
        String localePackage = portletConfig.getInitParameter( PortletTools.locale_name_package );
        try {
            return StringManager.getManager( localePackage, locale ).getStr(key);
        }
        catch( UnsupportedEncodingException e ) {
            String es = "error getString";
            log.error(es, e);
            return null;
        }
    }

    /**
     * @deprecated Use ResourceBundle
     * @param locale
     * @return
     */
    public static String getStr( Locale locale, String key, String args[], PortletConfig portletConfig ) {
        String localePackage = portletConfig.getInitParameter( PortletTools.locale_name_package );
        try {
            return StringManager.getManager( localePackage, locale ).getStr(key, args);
        }
        catch( UnsupportedEncodingException e ) {
            String es = "error getString";
            log.error(es, e);
            return null;
        }
    }

    public static String ctx() {
        if (GenericConfig.contextName  == null)
            return Constants.URI_CTX_MANAGER ;

        return GenericConfig.contextName + Constants.URI_CTX_MANAGER ;
    }

    public static String pageid() {
        if (GenericConfig.contextName  == null)
            return Constants.PAGEID_SERVLET_NAME ;

        return GenericConfig.contextName + Constants.PAGEID_SERVLET_NAME ;
    }

    public static String page() {
        if (GenericConfig.contextName  == null)
            return Constants.PAGE_SERVLET_NAME ;

        return GenericConfig.contextName + Constants.PAGE_SERVLET_NAME ;
    }

    public static String urlPage() {
        if (GenericConfig.contextName  == null)
            return Constants.URL_SERVLET_NAME ;

        return GenericConfig.contextName + Constants.URL_SERVLET_NAME ;
    }

//    public StringManager sCustom = null;
//    private StringManager stringManager = null;


//    private String nameLocaleBundle = null;

//    private PortletRequest portletRequest = null;

//    private Map parameters = null;

//    public String getRemoteAddr() {
//        return httpRequest.getRemoteAddr();
//    }
//
//    public Cookie[] getCookies() {
//        return httpRequest.getCookies();
//    }

//    public void setParameters(Map parameters, String nameLocaleBunble1) {
//        this.parameters = null;
//        this.portletRequest = null;

//        this.parameters = parameters;
//        addGlobalParameters(this.parameters);

//        this.portletRequest = new RenderRequestImpl(
//            parameters,
//            httpRequest,
//            this.auth,
//            this.contextFactory.getRealLocale(),
//            this.preferredLocale
//        );


//        if (log.isDebugEnabled())
//            log.debug("nameLocaleBunble: "+nameLocaleBunble);

//        this.nameLocaleBundle = nameLocaleBunble;
//        if ((nameLocaleBunble != null) && (nameLocaleBunble.trim().length() != 0))
//            sCustom = StringManager.getManager(nameLocaleBunble, contextFactory.getRealLocale());
//    }

//    private void addGlobalParameters(Map map) {
//        if (map.get(Constants.NAME_TYPE_CONTEXT_PARAM)==null && contextFactory.getDefaultPortletType()!=null)
//            map.put(Constants.NAME_TYPE_CONTEXT_PARAM, contextFactory.getDefaultPortletType());
//
//        if (map.get(Constants.NAME_TEMPLATE_CONTEXT_PARAM)==null && contextFactory.getNameTemplate()!=null)
//            map.put(Constants.NAME_TEMPLATE_CONTEXT_PARAM, contextFactory.getNameTemplate());

//        if (getNamePortletId()!=null && getPortletId()!=null) {
//            if (map.get(getNamePortletId())==null)
//                map.put(getNamePortletId(), getPortletId());
//        }
//    }

//    public Map getParameters() {
//        return parameters;
//    }

//    public PortletRequest getPortletRequest() {
//        return portletRequest;
//    }

//    public String getNameLocaleBundle() {
//        return nameLocaleBundle;
//    }

//    public PortletURL getURL() {
//        return new PortletURLImpl(this, httpRequest, httpResponse);
//    }

//    private static HiddenParamType getHidden(String name, String value) {
//        HiddenParamType hidden = new HiddenParamType();
//        hidden.setHiddenParamName(name);
//        hidden.setHiddenParamValue(value);
//        return hidden;
//    }
//
//    public List getAsList() {
//        List v = new ArrayList(1);
//
//        if (contextFactory.getRealLocale() != null)
//            v.add( getHidden( Constants.NAME_LANG_PARAM, contextFactory.getRealLocale().toString()));
//
//        return v;
//    }

//    public String getAsURL() {
//        return Constants.NAME_LANG_PARAM + "=" + contextFactory.getRealLocale().toString() + "&";
//    }
//
//    public String getAsForm() {
//        return "<input type=\"hidden\" name=\"" + Constants.NAME_LANG_PARAM + "\" value=\"" + contextFactory.getRealLocale().toString() + "\">";
//    }
//
//    public String getAsUrlXML() {
//        return Constants.NAME_LANG_PARAM + "=" + contextFactory.getRealLocale().toString() + "&amp;";
//    }
//
//    public String getAsFormXML() {
//        return "<HiddenParam><HiddenParamName>" + Constants.NAME_LANG_PARAM + "</HiddenParamName><HiddenParamValue>" + contextFactory.getRealLocale().toString() + "</HiddenParamValue></HiddenParam>";
//    }

//    public String urlAsForm(String nameTemplate, String portlet) {
//        return
//            getAsForm()+
//            ServletTools.getHiddenItem(Constants.NAME_TEMPLATE_CONTEXT_PARAM, nameTemplate)+
//            ServletTools.getHiddenItem(Constants.NAME_TYPE_CONTEXT_PARAM, portlet);
//    }

//    public StringManager getStringManager() {
//        return stringManager;
//    }

/*
    public String getNamePortletId() {
        if (contextFactory==null)
            return null;

        return contextFactory.getNamePortletId();
    }

*/
/*
    public Long getPortletId() {
        if (contextFactory==null)
            return null;

        return contextFactory.getPortletId();
    }

*/
/*
    static Map getGlobalParameter(PortletType portlet, Long portletId) {

        Map map = new HashMap();
        if (portlet==null || portletId==null){
            if (log.isDebugEnabled()){
                log.debug("portlet: "+portlet+", portletId is null. Return empty Map");
            }
            return map;
        }

        String key = PortletTools.getStringParam(portlet, PortletTools.name_portlet_id);
        if (log.isDebugEnabled()) log.debug("key: "+key);

        if (key!=null){

            if (log.isDebugEnabled()) log.debug("Put in map, key: "+key+", value: "+portletId);

            if (map.get(key)==null)
                map.put(key, portletId);

        }
        return map;
    }
*/
}
