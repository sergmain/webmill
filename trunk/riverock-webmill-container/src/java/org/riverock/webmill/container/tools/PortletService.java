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
package org.riverock.webmill.container.tools;

import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.Enumeration;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Map;
import java.util.Locale;
import java.util.ArrayList;

import javax.portlet.PortletConfig;
import javax.portlet.PortletRequest;
import javax.portlet.PortletSession;
import javax.portlet.PortletResponse;

import org.riverock.webmill.container.ContainerConstants;
import org.riverock.webmill.container.portlet.bean.PortletDefinition;
import org.riverock.webmill.container.portlet.bean.Supports;
import org.riverock.webmill.container.portlet.bean.InitParam;

/**
 * $Id$
 */
public final class PortletService {

/*
    public static void put( final Map<String, List<Object>> map, final String key, final Object value ) {
        List<Object> obj = map.get( key );
        if (obj==null) {
            List<Object> list = new ArrayList<Object>();
            list.add( value );
            map.put( key, list );
        }
        else {
            obj.add( value );
        }
    }
*/

    public static boolean isEmpty( final String s) {
        if (s==null || s.trim().length()==0)
            return true;
        else
            return false;
    }

    public static String convertString( final String s, final String fromCharset, final String toCharset)
            throws java.io.UnsupportedEncodingException
    {
        if (s == null)
            return null;

        return new String(s.getBytes(fromCharset), toCharset);
    }

    public static String getString( final Map map, final String f, String serverCharset, String htmlCharset ) {
        return getString(map, f, null, serverCharset, htmlCharset);
    }

    public static String getString( final Map map, final String f, final String def, String serverCharset, String htmlCharset ) {

        String s_ = def;
        final Object obj = map.get( f );
        if (obj != null) {
            try {
                s_ = convertString( obj.toString(), serverCharset, htmlCharset);
            }
            catch (Exception e) {
                // not rethrow exception 'cos this method return def value in this case
            }
        }
        return s_;
    }

    public static String pageid( final PortletRequest renderRequest ) {
        if (renderRequest.getContextPath().equals("/"))
            return ContainerConstants.PAGEID_SERVLET_NAME ;

        return renderRequest.getContextPath() + ContainerConstants.PAGEID_SERVLET_NAME ;
    }

    public static String page( final PortletRequest renderRequest ) {
        if (renderRequest.getContextPath().equals("/"))
            return ContainerConstants.PAGE_SERVLET_NAME ;

        return renderRequest.getContextPath() + ContainerConstants.PAGE_SERVLET_NAME ;
    }

    public static String urlPage( final PortletRequest renderRequest ) {
        if (renderRequest.getContextPath().equals("/"))
            return ContainerConstants.URL_SERVLET_NAME ;

        return renderRequest.getContextPath() + ContainerConstants.URL_SERVLET_NAME ;
    }

    public static String url( final String portletName, final PortletRequest renderRequest, final PortletResponse renderResponse ) {
        return urlStringBuffer(
            portletName,
            renderRequest,
            renderResponse
        ).toString();
    }

    public static StringBuffer urlStringBuffer( final String portletName, final PortletRequest renderRequest, final PortletResponse renderResponse ) {
        return urlStringBuffer( portletName, renderRequest, renderResponse, (String)renderRequest.getAttribute( ContainerConstants.PORTAL_TEMPLATE_NAME_ATTRIBUTE ) );
    }

    public static String url(
        final String portletName,
        final PortletRequest renderRequest,
        final PortletResponse renderResponse,
        final String templateName
        ) {
        return urlStringBuffer(portletName,renderRequest,renderResponse,templateName).toString();
    }

    public static StringBuffer urlStringBuffer(
        final String portletName,
        final PortletRequest renderRequest,
        final PortletResponse renderResponse,
        final String templateName
        ) {

        StringBuffer b = new StringBuffer(
            renderResponse.encodeURL(
                PortletService.ctxStringBuffer( renderRequest, null, templateName ).toString() )
        );

        return
            b.append( '?' ).
            append( ContainerConstants.NAME_TYPE_CONTEXT_PARAM ).append( '=' ).append( portletName ).append( '&' );
    }

    public static String ctx( final PortletRequest renderRequest ) {
        return PortletService.ctxStringBuffer(renderRequest).toString();
    }

    public static Integer getInitParameterInt( final PortletConfig portletConfig, final String name, final Integer defValue ) {
        String value = portletConfig.getInitParameter(name);
        if (value==null) {
            return defValue;
        }
        try {
            return new Integer(value);
        }
        catch(Exception e) {
            return defValue;
        }
    }

    public static Long getInitParameterLong( final PortletConfig portletConfig, final String name, final Long defValue ) {
        String value = portletConfig.getInitParameter(name);
        if (value==null) {
            return defValue;
        }
        try {
            return new Long(value);
        }
        catch(Exception e) {
            return defValue;
        }
    }

    public static Double getInitParameterDouble( final PortletConfig portletConfig, final String name, final Double defValue ) {
        String value = portletConfig.getInitParameter(name);
        if (value==null) {
            return defValue;
        }
        try {
            return new Double(value);
        }
        catch(Exception e) {
            return defValue;
        }
    }

    public static boolean isSupportMimeType( final PortletDefinition portletType, final String mimeType ) {
        if (portletType==null || mimeType==null)
            return false;

        for (int i = 0; i < portletType.getSupportsCount(); i++) {
            Supports supports = portletType.getSupports(i);
            String type = supports.getMimeType();
            if (type!=null && mimeType.equals( type ))
                return true;
        }
        return false;
    }

    public static String getStringParam( final PortletDefinition v, final String name_ ) {
        return getStringParam(v, name_, null);
    }

    public static String getStringParam(final PortletDefinition v, final String name_, final String defaultValue) {
        if (v == null || name_ == null || name_.trim().length() == 0)
            return defaultValue;

        for (int i = 0; i < v.getInitParamCount(); i++) {
            InitParam init = v.getInitParam(i);
            if (name_.equals(init.getName())) {
                return init.getValue() != null ? init.getValue() : defaultValue;
            }
        }
        return defaultValue;
    }

    public static Boolean getBooleanParam( final PortletDefinition v, final String name_) {
        return getBooleanParam(v, name_, null);
    }

    public static Boolean getBooleanParam( final PortletDefinition v, final String name_, final Boolean defaultvalue ) {
        if (v == null || name_ == null || name_.trim().length() == 0)
            return defaultvalue;

        for (int i = 0; i < v.getInitParamCount(); i++) {
            InitParam init = v.getInitParam(i);
            if (name_.equals(init.getName()))
                return new Boolean(init.getValue());
        }
        return defaultvalue;
    }

    public static Long getLongParam( final List v, final String name_ ) {
        if (v == null || name_ == null || name_.trim().length() == 0)
            return null;

        for (int i = 0; i < v.size(); i++) {
            InitParam init = (InitParam) v.get(i);
            if (name_.equals(init.getName()))
                return new Long(init.getValue());
        }
        return null;
    }

    public static String getRemoteAddr( PortletRequest request ) {
        return (String) request.getAttribute(ContainerConstants.PORTAL_REMOTE_ADDRESS_ATTRIBUTE);
    }

    public static String getUserAgent( PortletRequest request ) {
        return (String) request.getAttribute(ContainerConstants.PORTAL_USER_AGENT_ATTRIBUTE);
    }

    public static void immediateRemoveAttribute( final PortletSession session, final String attr ) {

        Object obj = session.getAttribute(attr);
        try {

            Class cl = obj.getClass();
            Method m = cl.getMethod("destroy", new Class[]{});

            if (m != null)
                m.invoke(obj, new Object[]{});

        }
        catch (Exception e) {
        }

        session.removeAttribute(attr);
        obj = null;
    }

    public static Long getIdPortlet( final String namePortletID, final PortletRequest request ) {
        return getLong(request, namePortletID);
    }

    public static String getString( final PortletRequest request, final String f, String serverCharset, String htmlCharset ) {
        if (f==null) {
            return null;
        }
        return getString(request, f, null, serverCharset, htmlCharset);
    }

    public static String getString(final PortletRequest request, final String f, final String def, String serverCharset, String htmlCharset) {
        if (f==null) {
            return def;
        }
        String s_ = def;
        final String parameter = request.getParameter(f);
        if (parameter != null) {
            try {
                s_ = convertString(parameter, serverCharset, htmlCharset);
            }
            catch (Exception e) {
                // not rethrow exception 'cos this method return def value in this case
            }
        }
        return s_;
    }

    public static Long getLong( final PortletRequest request, final String f ) {
        if (f==null) {
            return null;
        }
        return getLong(request, f, null);
    }

    public static Long getLong( final PortletRequest request, final String f, final Long def) {
        if (f==null) {
            return def;
        }
        Long s_ = def;
        final String parameter = request.getParameter(f);
        if (parameter != null)
        {
            try
            {
                s_ = new Long(parameter);
            }
            catch (Exception e)
            {
                // not rethrow exception 'cos this method return def value in this case
            }
        }
        return s_;
    }

    public static Integer getInt( final PortletRequest request, final String f ) {
        if (f==null) {
            return null;
        }
        return getInt(request, f, null);
    }

    public static Integer getInt( final PortletRequest request, final String f, final Integer def ) {
        if (f==null) {
            return def;
        }
        Integer s_ = def;
        final String parameter = request.getParameter(f);
        if (parameter != null) {
            try {
                s_ = new Integer(parameter);
            }
            catch (Exception e) {
                // not rethrow exception 'cos this method return def value in this case
            }
        }
        return s_;
    }

    public static Double getDouble( final PortletRequest request, final String f ) {
        if (f==null) {
            return null;
        }
        return getDouble(request, f, null);
    }

    public static Double getDouble( final PortletRequest request, final String f, final Double def ) {
        if (f==null) {
            return def;
        }
        Double s_ = def;
        final String parameter = request.getParameter(f);
        if (parameter != null)
        {
            try
            {
                s_ = new Double(parameter);
            }
            catch (Exception e)
            {
                // not rethrow exception 'cos this method return def value in this case
            }
        }
        return s_;
    }

    public static Float getFloat( final PortletRequest request, final String f ) {
        if (f==null) {
            return null;
        }
        return getFloat(request, f, null);
    }

    public static Float getFloat( final PortletRequest request, final String f, final Float def ) {
        if (f==null) {
            return def;
        }
        Float s_ = def;
        final String parameter = request.getParameter(f);
        if (parameter != null) {
            try {
                s_ = new Float(parameter);
            }
            catch (Exception e) {
                // not rethrow exception 'cos this method return def value in this case
            }
        }
        return s_;
    }

    public static void cleanSession( final PortletSession session ) throws Exception {
        if (session==null)
            return;

        // delete from session all objects
        int countLoop = 3;
        for (int i=0; i<countLoop; i++)
        {
            try
            {
                for (Enumeration e = session.getAttributeNames();
                     e.hasMoreElements();
                     e = session.getAttributeNames()
                    )
                {
                    String name = (java.lang.String) e.nextElement() ;

/*
                    if(log.isDebugEnabled())
                        log.debug("Attribute: "+name);
*/

                    session.removeAttribute( name );
                }
            }
            catch( java.util.ConcurrentModificationException e)
            {
                if (i==countLoop-1)
                    throw e;
            }
        }
    }


    public static StringBuffer ctxStringBuffer( final PortletRequest renderRequest ) {
        return ctxStringBuffer( renderRequest, null );
    }

    public static StringBuffer ctxStringBuffer( final PortletRequest renderRequest, final String portletName ) {
        return ctxStringBuffer( renderRequest, portletName, (String)renderRequest.getAttribute( ContainerConstants.PORTAL_TEMPLATE_NAME_ATTRIBUTE ) );
    }

    public static StringBuffer ctxStringBuffer( final PortletRequest renderRequest, final String portletName, final String templateName ) {
        return ctxStringBuffer( renderRequest, portletName, (String)renderRequest.getAttribute( ContainerConstants.PORTAL_TEMPLATE_NAME_ATTRIBUTE ), renderRequest.getLocale() );
    }

    public static StringBuffer ctxStringBuffer( final PortletRequest renderRequest, final String portletName, final String templateName, Locale locale ) {
        StringBuffer b = null;
        String portalContextPath = (String)renderRequest.getAttribute( ContainerConstants.PORTAL_PORTAL_CONTEXT_PATH );
        if (portalContextPath.equals("/"))
            b = new StringBuffer( ContainerConstants.URI_CTX_MANAGER );
        else
            b = new StringBuffer( portalContextPath ).append( ContainerConstants.URI_CTX_MANAGER );

        b.append( '/' ).append( locale.toString() );
	b.append( ',' );
        if (templateName!=null) 
           b.append( templateName );

	b.append( ',' );
        if (portletName!=null)
            b.append( portletName );

        b.append( "/ctx" );

        return b;
    }

    /**
     * Build Locale from string
     * @param locale with locale
     * @return java.util.Locale
     */
    public static Locale getLocale( final String locale ) {
        if (locale==null) {
            return null;
        }

        String string = locale.replace( '-', '_' );

        int idx = string.indexOf('_');
        if (idx == -1)
            return new Locale(string.toLowerCase(), "");

        String lang = string.substring(0, idx).toLowerCase();
        int idx1 = string.indexOf('_', idx+1);
        if (idx1==-1)
            return new Locale(lang, string.substring(idx+1).toLowerCase() );

        return new Locale(
            lang, string.substring(idx+1, idx1).toLowerCase( ), string.substring(idx1+1).toLowerCase() );
    }

    public static String getString( final ResourceBundle bundle, final String key, final Object args[] ) {
        return MessageFormat.format( bundle.getString( key ), args );
    }

    /**
     * @deprecated Use ResourceBundle
     * @param locale
     * @return
     */
//    public static StringManager getStringManager( final Locale locale ) {
//        return StringManager.getManager("mill.locale.main", locale);
//    }
//
}