/*
 * org.riverock.webmill.container - Webmill portlet container implementation
 *
 * Copyright (C) 2006, Riverock Software, All Rights Reserved.
 *
 * Riverock - The Open-source Java Development Community
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
 */
package org.riverock.common.utils;

import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import javax.portlet.PortletConfig;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.portlet.PortletSession;

import org.riverock.interfaces.ContainerConstants;

/**
 * $Id: PortletUtils.java 1301 2007-08-09 07:29:34Z serg_main $
 */
public final class PortletUtils {

    public static void checkRights(PortletRequest request, String[] roles) {
        boolean isOk = false;
        for (String role : roles) {
            if (request.isUserInRole(role)) {
                isOk = true;
                break;
            }
        }
        if (!isOk) {
            throw new SecurityException("Access denied");
        }
    }

    public static String convertString( final String s, final String fromCharset, final String toCharset)
            throws java.io.UnsupportedEncodingException
    {
        if (s == null) {
            return null;
        }

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
        String path = renderRequest.getPortalContext().getProperty( ContainerConstants.PORTAL_PORTAL_CONTEXT_PATH );
        if (path.equals("/") || path.equals("") )
            return ContainerConstants.PAGEID_SERVLET_NAME ;

        return path + ContainerConstants.PAGEID_SERVLET_NAME ;
    }

    public static String pageid( final String portalContext ) {
        if (portalContext.equals("/") || portalContext.equals("") ) {
            return ContainerConstants.PAGEID_SERVLET_NAME;
        }

        return portalContext + ContainerConstants.PAGEID_SERVLET_NAME ;
    }

    public static String page( final PortletRequest renderRequest ) {
        String path = renderRequest.getPortalContext().getProperty( ContainerConstants.PORTAL_PORTAL_CONTEXT_PATH );
        if (path.equals("/") || path.equals("") ) {
            return ContainerConstants.PAGE_SERVLET_NAME;
        }

        return path + ContainerConstants.PAGE_SERVLET_NAME ;
    }

    public static String page( final String portalContext ) {
        if (portalContext.equals("/") || portalContext.equals("") ) {
            return ContainerConstants.PAGE_SERVLET_NAME ;
        }

        return portalContext + ContainerConstants.PAGE_SERVLET_NAME ;
    }

    public static String url( final String portletName, final PortletRequest renderRequest, final PortletResponse renderResponse ) {
        return urlStringBuilder(
            portletName,
            renderRequest,
            renderResponse,
            (String)renderRequest.getAttribute( ContainerConstants.PORTAL_TEMPLATE_NAME_ATTRIBUTE )
        ).toString();
    }

    public static String url(
        final String portletName,
        final PortletRequest renderRequest,
        final PortletResponse renderResponse,
        final String templateName
        ) {
        return urlStringBuilder(portletName,renderRequest,renderResponse,templateName).toString();
    }

    public static StringBuilder urlStringBuilder(
        final String portletName,
        final PortletRequest renderRequest,
        final PortletResponse renderResponse,
        final String templateName
        ) {

        StringBuilder b = new StringBuilder(
            renderResponse.encodeURL(
                ctxStringBuilder( renderRequest, null, templateName ).toString() )
        );

        return
            b.append( '?' ).
            append( ContainerConstants.NAME_TYPE_CONTEXT_PARAM ).append( '=' ).append( portletName ).append( '&' );
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
            Method m = cl.getMethod("destroy");

            if (m != null)
                m.invoke(obj);

        }
        catch (Exception e) {
            // silence remove attribete
        }
        session.removeAttribute(attr);
    }

    public static String getString( final PortletRequest request, final String f ) {
        if (f==null) {
            return null;
        }
        return getString(request, f, null);
    }

    public static String getString(final PortletRequest request, final String f, final String def) {
        if (f==null) {
            return def;
        }
        final String parameter = request.getParameter(f);
        if (parameter != null) {
            return parameter;
        }
        return def;
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
        if (parameter != null) {
            try {
                s_ = new Long(parameter);
            }
            catch (Exception e) {
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
        if (parameter != null) {
            try {
                s_ = new Double(parameter);
            }
            catch (Exception e) {
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

    public static String ctx( final PortletRequest renderRequest ) {
        return ctxStringBuilder(renderRequest).toString();
    }

    public static StringBuilder ctxStringBuilder( final PortletRequest renderRequest ) {
        return ctxStringBuilder( renderRequest, null );
    }

    public static StringBuilder ctxStringBuilder( final PortletRequest renderRequest, final String portletName ) {
        return ctxStringBuilder( renderRequest, portletName, (String)renderRequest.getAttribute( ContainerConstants.PORTAL_TEMPLATE_NAME_ATTRIBUTE ) );
    }

    public static StringBuilder ctxStringBuilder( final PortletRequest renderRequest, final String portletName, final String templateName ) {
        return ctxStringBuilder( renderRequest, portletName, templateName, renderRequest.getLocale() );
    }

    public static StringBuilder ctxStringBuilder( final PortletRequest renderRequest, final String portletName, final String templateName, Locale locale ) {
        StringBuilder b;
        String portalContextPath = renderRequest.getPortalContext().getProperty( ContainerConstants.PORTAL_PORTAL_CONTEXT_PATH );
        if (portalContextPath.equals("/") || portalContextPath.equals("") ) {
            b = new StringBuilder( ContainerConstants.URI_CTX_MANAGER );
        }
        else {
            b = new StringBuilder( portalContextPath ).append( ContainerConstants.URI_CTX_MANAGER );
        }

        b.append( '/' ).append( locale.toString() );
        b.append( ',' );
        if (templateName!=null) {
           b.append( templateName );
        }

        b.append( ',' );
        if (portletName!=null) {
            b.append( portletName );
        }

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
        if (idx == -1) {
            return new Locale(string.toLowerCase(), "");
        }

        String lang = string.substring(0, idx).toLowerCase();
        int idx1 = string.indexOf('_', idx+1);
        if (idx1==-1) {
            return new Locale(lang, string.substring(idx+1).toLowerCase() );
        }

        return new Locale(
            lang, string.substring(idx+1, idx1).toLowerCase( ), string.substring(idx1+1).toLowerCase() );
    }

    public static String getString( final ResourceBundle bundle, final String key, final Object args[] ) {
        return MessageFormat.format( bundle.getString( key ), args );
    }
}