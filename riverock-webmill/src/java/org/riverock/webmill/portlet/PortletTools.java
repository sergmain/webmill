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

import java.io.IOException;
import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Properties;
import java.util.Locale;

import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.portlet.PortletSession;
import javax.portlet.PortletConfig;
import javax.servlet.http.HttpServletRequest;

import org.riverock.common.multipart.AbstractPart;
import org.riverock.common.multipart.MultipartHandler;
import org.riverock.common.multipart.MultipartRequestException;
import org.riverock.common.multipart.MultipartRequestWrapper;
import org.riverock.common.multipart.UploadException;
import org.riverock.common.multipart.FilePart;
import org.riverock.common.tools.StringTools;
import org.riverock.common.config.ConfigException;
import org.riverock.interfaces.schema.javax.portlet.InitParamType;
import org.riverock.interfaces.schema.javax.portlet.MimeTypeType;
import org.riverock.interfaces.schema.javax.portlet.PortletType;
import org.riverock.interfaces.schema.javax.portlet.SupportsType;
import org.riverock.webmill.config.WebmillConfig;
import org.riverock.webmill.main.Constants;
import org.riverock.webmill.portal.PortalConstants;
import org.riverock.webmill.schema.site.SiteTemplateParameterType;
import org.riverock.generic.tools.StringManager;

import org.apache.log4j.Logger;

/**
 * $Id$
 */
public final class PortletTools {
    private final static Logger log = Logger.getLogger( PortletTools.class );

    public static final String MIME_TYPE_TEXT_XML = "text/xml";
    public static final String MIME_TYPE_TEXT_HTML = "text/html";
    public static final String MIME_TYPE_TEXT_WML = "text/wml";

    public static final String type_portlet = "type-portlet";
    public static final String is_url = "is-url";
    public static final String is_xml = "is-xml";
    public static final String name_portlet_id = "name-portlet-id";
    public static final String locale_name_package = "locale-name-package";
    public static final String name_portlet_code_string = "name-portlet-code-string";
    public static final String class_name_get_list = "class-name-get-list";
    public static final String is_direct_request = "is-direct-request";

    public static boolean getMetadataBoolean( final PortletRequest portletRequest, final String key, final boolean defValue ) {
        String s = getMetadata(portletRequest, key);
        if (s==null)
            return defValue;

        return Boolean.valueOf( s ).booleanValue();
    }

    public static String getMetadata( final PortletRequest portletRequest, final String key) {
        return getMetadata( portletRequest, key, null );
    }

    public static String getMetadata( final PortletRequest portletRequest, final String key, final String defValue ) {
        if (portletRequest==null || key==null)
            return defValue;

        Properties p = (Properties)portletRequest.getAttribute( PortalConstants.PORTAL_PORTLET_METADATA_ATTRIBUTE );
        if (p==null)
            return defValue;

        String value = p.getProperty( key );
        if (value!=null)
            return value;
        else
            return defValue;
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

    public static boolean isSupportMimeType( final PortletType portletType, final String mimeType ) {
        if (portletType==null || mimeType==null)
            return false;

        for (int i = 0; i < portletType.getSupportsCount(); i++) {
            SupportsType supportsType = portletType.getSupports(i);
            MimeTypeType type = supportsType.getMimeType();
            if (type!=null && mimeType.equals( type.getContent()))
                return true;
        }
        return false;
    }

    public static String getStringParam( final PortletType v, final String name_ ) {
        return getStringParam(v, name_, null);
    }

    public static String getStringParam(final PortletType v, final String name_, final String defaultValue) {
        if (v == null || name_ == null || name_.trim().length() == 0)
            return defaultValue;

        for (int i = 0; i < v.getInitParamCount(); i++) {
            InitParamType init = v.getInitParam(i);
            if (name_.equals(init.getName().getContent())) {
                return init.getValue() != null ? init.getValue().getContent() : defaultValue;
            }
        }
        return defaultValue;
    }

    public static Boolean getBooleanParam( final PortletType v, final String name_) {
        return getBooleanParam(v, name_, null);
    }

    public static Boolean getBooleanParam( final PortletType v, final String name_, final Boolean defaultvalue ) {
        if (v == null || name_ == null || name_.trim().length() == 0)
            return defaultvalue;

        for (int i = 0; i < v.getInitParamCount(); i++) {
            InitParamType init = v.getInitParam(i);
            if (name_.equals(init.getName().getContent()))
                return new Boolean(init.getValue().getContent());
        }
        return defaultvalue;
    }

    public static Long getLongParam( final List v, final String name_ ) {
        if (v == null || name_ == null || name_.trim().length() == 0)
            return null;

        for (int i = 0; i < v.size(); i++) {
            InitParamType init = (InitParamType) v.get(i);
            if (name_.equals(init.getName().getContent()))
                return new Long(init.getValue().getContent());
        }
        return null;
    }

    public static Long getIdPortlet( final String namePortletID, final PortletRequest request ) {
        return getLong(request, namePortletID);
    }

    public static String getString( final List v, final String nameParam) throws IllegalArgumentException{
        return getString(v, nameParam, null);
    }

    public synchronized static String getString( final List v, final String nameParam, final String defValue ) {
        if (v == null || nameParam == null || nameParam.trim().length() == 0)
            return defValue;

        for (int i = 0; i < v.size(); i++) {
            SiteTemplateParameterType item = (SiteTemplateParameterType) v.get(i);
            if (item.getName().equals(nameParam))
                return item.getValue();
        }
        return defValue;
    }

    private static final boolean saveUploadedFilesToDisk = false;
    public static Map getParameters( final HttpServletRequest request )
        throws IOException, UploadException, PortletException, MultipartRequestException {

        Map p = new HashMap();
        String contentType = request.getContentType();

        if (contentType != null &&
            contentType.toLowerCase().indexOf(MultipartRequestWrapper.MFDHEADER) > -1) {
            // it's a multipart request

            MultipartRequestWrapper reqWrapper =
                new MultipartRequestWrapper(
                    request,
                    saveUploadedFilesToDisk,
                    null,
                    false,
                    false,
                    3*1024*1024
                );

            MultipartHandler handler = reqWrapper.getMultipartHandler();
            if (handler == null)
                throw new PortletException("MultipartHandler is null");

            Iterator e = handler.getPartsHash().keySet().iterator();
            AbstractPart part = null;
            while (e.hasNext()) {
                String key = (String) e.next();
                part = (AbstractPart) handler.getPartsHash().get(key);
                if (part instanceof List) {
                    throw new PortletException("Not implemented");
                }
                else {
                    if (part.getType()==AbstractPart.PARAMETER_TYPE) {
                        p.put(key, part.getStringValue());
                    }
                    else if ( part instanceof FilePart ) {
                        p.put(key, ((FilePart)part) );
                    }
                    else {
                        throw new PortletException("Unknown type of parameter from multipartrequest: " + part.getClass().getName() );
                    }
                }
            }
        }
        else {
            Enumeration e = request.getParameterNames();
            for (; e.hasMoreElements() ;) {
                Object obj = e.nextElement();

                String value[] = request.getParameterValues( (String)obj );
                if (value!=null) {
                    if (value.length==1)
                        p.put(obj, value[0]);
                    else {
                        List ee = new ArrayList();
                        for (int i=0; i<value.length; i++)
                           ee.add(value[i]);

                        p.put(obj, ee);
                    }
                }
            }
        }

        return p;
    }

    public static String getString( final Map map, final String f ) {
        return getString(map, f, null);
    }

    public static String getRemoteAddr( PortletRequest request ) {
        return (String) request.getAttribute(PortalConstants.PORTAL_REMOTE_ADDRESS_ATTRIBUTE);
    }

    public static String getUserAgent( PortletRequest request ) {
        return (String) request.getAttribute(PortalConstants.PORTAL_USER_AGENT_ATTRIBUTE);
    }

    public static String getString( final Map map, final String f, final String def ) {

        String s_ = def;
        final Object obj = map.get( f );
//        final String parameter = request.getParameter(f);
        if (obj != null)
        {
            try
            {
                s_ = StringTools.convertString( obj.toString(), WebmillConfig.getServerCharset(), WebmillConfig.getHtmlCharset());
            }
            catch (Exception e)
            {
                // not rethrow exception 'cos this method return def value in this case
                log.warn("Exception in getString(), def value will be return", e);
            }
        }
        return s_;
    }

    public static String getString( final PortletRequest request, final String f ) {
        return getString(request, f, null);
    }

    public static String getString(final PortletRequest request, final String f, final String def) {
        String s_ = def;
        final String parameter = request.getParameter(f);
        if (parameter != null) {
            try {
                s_ = StringTools.convertString(parameter, WebmillConfig.getServerCharset(), WebmillConfig.getHtmlCharset());
            }
            catch (Exception e) {
                // not rethrow exception 'cos this method return def value in this case
                log.warn("Exception in getString(), def value will be return", e);
            }
        }
        if (log.isDebugEnabled()){
            log.debug("parameter: "+parameter);
            log.debug("WebmillConfig.getServerCharset(): "+WebmillConfig.getServerCharset());
            log.debug("WebmillConfig.getHtmlCharset(): "+WebmillConfig.getHtmlCharset());
            log.debug("result string: "+s_);
        }
        return s_;
    }

    public static Long getLong( final PortletRequest request, final String f ) {
        return getLong(request, f, null);
    }

    public static Long getLong( final PortletRequest request, final String f, final Long def) {
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
                log.warn("Exception in getLong(), def value will be return", e);
            }
        }
        return s_;
    }

    public static Integer getInt( final PortletRequest request, final String f ) {
        return getInt(request, f, null);
    }

    public static Integer getInt( final PortletRequest request, final String f, final Integer def ) {
        Integer s_ = def;
        final String parameter = request.getParameter(f);
        if (parameter != null) {
            try {
                s_ = new Integer(parameter);
            }
            catch (Exception e) {
                // not rethrow exception 'cos this method return def value in this case
                log.warn("Exception in getInteger(), def value will be return", e);
            }
        }
        return s_;
    }

    public static Double getDouble( final PortletRequest request, final String f ) {
        return getDouble(request, f, null);
    }

    public static Double getDouble( final PortletRequest request, final String f, final Double def ) {
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
                log.warn("Exception in getDouble(), def value will be return", e);
            }
        }
        return s_;
    }

    public static Float getFloat( final PortletRequest request, final String f ) {
        return getFloat(request, f, null);
    }

    public static Float getFloat( final PortletRequest request, final String f, final Float def ) {
        Float s_ = def;
        final String parameter = request.getParameter(f);
        if (parameter != null) {
            try {
                s_ = new Float(parameter);
            }
            catch (Exception e) {
                // not rethrow exception 'cos this method return def value in this case
                log.warn("Exception in getFloat(), def value will be return", e);
            }
        }
        return s_;
    }

    public static void immediateRemoveAttribute( final PortletSession session, final String attr ) {

        Object obj = session.getAttribute(attr);
        try {
            if (log.isDebugEnabled())
                log.debug("#12.12.001 search method 'clearObject'");

            Class cl = obj.getClass();
            Method m = cl.getMethod("clearObject", null);

            if (log.isDebugEnabled())
                log.debug( "#12.12.002 invoke method 'clearObject'" );

            if (m != null)
                m.invoke(obj, null);

            if (log.isDebugEnabled())
                log.debug( "#12.12.003 complete invoke method 'clearObject'" );
        }
        catch (Exception e) {
            if (log.isInfoEnabled())
                log.info("#12.12.003  method 'clearObject' not found. Error " + e.toString());
        }

        session.removeAttribute(attr);
        obj = null;
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

                    if(log.isDebugEnabled())
                        log.debug("Attribute: "+name);

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

    public static String url( final String portletName, final PortletRequest renderRequest, final PortletResponse renderResponse ) {
        return urlStringBuffer(
            portletName,
            renderRequest,
            renderResponse
        ).toString();
    }

    public static StringBuffer urlStringBuffer( final String portletName, final PortletRequest renderRequest, final PortletResponse renderResponse ) {
        return urlStringBuffer( portletName, renderRequest, renderResponse, (String)renderRequest.getAttribute( PortalConstants.PORTAL_TEMPLATE_NAME_ATTRIBUTE ) );
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
            renderResponse.encodeURL( ctxStringBuffer( renderRequest, null, templateName ).toString() )
        );

        return
            b.append( '?' ).
            append( Constants.NAME_TYPE_CONTEXT_PARAM ).append( '=' ).append( portletName ).append( '&' );
    }

    public static String ctx( final PortletRequest renderRequest ) {
        return ctxStringBuffer(renderRequest).toString();
    }

    public static StringBuffer ctxStringBuffer( final PortletRequest renderRequest ) {
        return ctxStringBuffer( renderRequest, null );
    }

    public static StringBuffer ctxStringBuffer( final PortletRequest renderRequest, final String portletName ) {
        return ctxStringBuffer( renderRequest, portletName, (String)renderRequest.getAttribute( PortalConstants.PORTAL_TEMPLATE_NAME_ATTRIBUTE ) );
    }

    public static StringBuffer ctxStringBuffer( final PortletRequest renderRequest, final String portletName, final String templateName ) {
        StringBuffer b = null;
        if (renderRequest.getContextPath().equals("/"))
            b = new StringBuffer( Constants.URI_CTX_MANAGER );
        else
            b = new StringBuffer(renderRequest.getContextPath()).append( Constants.URI_CTX_MANAGER );

        b.append( '/' ).append( renderRequest.getLocale().toString() );
        b.append( ',' ).append( templateName );
        if (portletName!=null)
            b.append( ',' ).append( portletName );

        b.append( "/ctx" );

        return b;
    }

    public static String pageid( final PortletRequest renderRequest ) {
        if (renderRequest.getContextPath().equals("/"))
            return Constants.PAGEID_SERVLET_NAME ;

        return renderRequest.getContextPath() + Constants.PAGEID_SERVLET_NAME ;
    }

    public static String page( final PortletRequest renderRequest ) {
        if (renderRequest.getContextPath().equals("/"))
            return Constants.PAGE_SERVLET_NAME ;

        return renderRequest.getContextPath() + Constants.PAGE_SERVLET_NAME ;
    }

    public static String urlPage( final PortletRequest renderRequest ) {
        if (renderRequest.getContextPath().equals("/"))
            return Constants.URL_SERVLET_NAME ;

        return renderRequest.getContextPath() + Constants.URL_SERVLET_NAME ;
    }

    public static String getString( final ResourceBundle bundle, final String key, final String args[] ) {
        return MessageFormat.format( bundle.getString( key ), args );
    }

    /**
     * @deprecated Use ResourceBundle
     * @param locale
     * @return
     */
    public static StringManager getStringManager( final Locale locale ) {
        return StringManager.getManager("mill.locale.main", locale);
    }
}