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

/**
 * $Id$
 */
package org.riverock.webmill.portlet;

import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.portlet.PortletContext;
import javax.portlet.PortletRequest;
import javax.portlet.PortletRequestDispatcher;
import javax.portlet.PortletSession;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.servlet.http.HttpServletRequest;

import org.riverock.common.config.ConfigException;
import org.riverock.common.multipart.AbstractPart;
import org.riverock.common.multipart.MultipartHandler;
import org.riverock.common.multipart.MultipartRequestException;
import org.riverock.common.multipart.MultipartRequestWrapper;
import org.riverock.common.multipart.UploadException;
import org.riverock.common.tools.StringTools;
import org.riverock.interfaces.schema.javax.portlet.InitParamType;
import org.riverock.interfaces.schema.javax.portlet.MimeTypeType;
import org.riverock.interfaces.schema.javax.portlet.PortletType;
import org.riverock.interfaces.schema.javax.portlet.SupportsType;
import org.riverock.webmill.config.WebmillConfig;
import org.riverock.webmill.exception.PortalException;
import org.riverock.webmill.schema.site.SiteTemplateParameterType;

import org.apache.log4j.Logger;

public final class PortletTools {
    private final static Logger log = Logger.getLogger(PortletTools.class);

    public static final String MIME_TYPE_TEXT_XML = "text/xml";
    public static final String MIME_TYPE_TEXT_HTML = "text/html";
    public static final String MIME_TYPE_TEXT_WML = "text/wml";

    public static final String type_portlet = "type-portlet";
    public static final String is_url = "is-url";
    public static final String is_xml = "is-xml";
    public static final String name_portlet_id = "name-portlet-id";
    public static final String locale_name_package = "locale-name-package";
    public static final String name_portlet_code_string = "name-portlet-code-string";
//    public static final String is_get_instance = "is-get-instance";
    public static final String class_name_get_list = "class-name-get-list";
    public static final String is_direct_request = "is-direct-request";

    public static boolean isSupportMimeType(PortletType portletType, String mimeType){
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

    public static String getStringParam(PortletType v, String name_) {
        return getStringParam(v, name_, null);
    }

    public static String getStringParam(PortletType v, String name_, String defaultValue) {
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

    public static Boolean getBooleanParam(PortletType v, String name_) {
        if (v == null || name_ == null || name_.trim().length() == 0)
            return null;

        for (int i = 0; i < v.getInitParamCount(); i++) {
            InitParamType init = v.getInitParam(i);
            if (name_.equals(init.getName().getContent()))
                return new Boolean(init.getValue().getContent());
        }
        return null;
    }

    public static Long getLongParam(List v, String name_) {
        if (v == null || name_ == null || name_.trim().length() == 0)
            return null;

        for (int i = 0; i < v.size(); i++) {
            InitParamType init = (InitParamType) v.get(i);
            if (name_.equals(init.getName().getContent()))
                return new Long(init.getValue().getContent());
        }
        return null;
    }

    public static Long getIdPortlet(String namePortletID, PortletRequest request){
        return getLong(request, namePortletID);
    }

    public static String getString(List v, String nameParam) throws IllegalArgumentException{
        return getString(v, nameParam, null);
    }

    public synchronized static String getString(List v, String nameParam, String defValue) {
        if (v == null || nameParam == null || nameParam.trim().length() == 0)
            return defValue;

        for (int i = 0; i < v.size(); i++) {
            SiteTemplateParameterType item = (SiteTemplateParameterType) v.get(i);
            if (item.getName().equals(nameParam))
                return item.getValue();
        }
        return defValue;
    }

/*
    public static PortletResultObject getPortletObject(
        PortletType desc,
        PortletParameter portletParameter,
        PortletRequest portletRequest
        )
        throws PortalException
    {
        try
        {
            String codePortlet = portletParameter.getPortletCode();

            if (log.isDebugEnabled())
            {
                log.debug("codePortlet " + codePortlet);
                log.debug("Create instance of " + desc.getPortletClass());
            }

            Portlet portlet = PortletContainer.getPortletInstance( desc.getPortletName().getContent() );

            StringWriter writer = new StringWriter(10000);
            RenderResponse renderResponse = new RenderResponseImpl(response, writer);

            portlet.render( , renderResponse);

            PortletResultObject portletObject = null;
            // create portlet without portlet code
            if (codePortlet == null || codePortlet.length() == 0) {
                if (log.isDebugEnabled())
                    log.debug("Get portlet object w/o portlet code. namePortletID - " + getStringParam(desc, name_portlet_id));

                Long id = null;
                String namePortletId = getStringParam(desc, name_portlet_id);
                if ( namePortletId != null && namePortletId.length()!=0 ) {
                    if (log.isDebugEnabled())
                        log.debug("Get ID from request");

                    id = getIdPortlet(namePortletId, portletRequest);

                    if (log.isDebugEnabled())
                        log.debug("ID from request - " + id);

                    if (id == null)
                        return null;
                }

                // need rewrite
//                portletObject = object.getInstance(adapter, id);
                throw new Exception("not implemented");
            }
            else {
                if (log.isDebugEnabled())
                    log.debug("Get portlet object with portlet code. namePortletID - " + getStringParam(desc, name_portlet_id) + " code " + codePortlet);

                // need rewrite
//                portletObject = object.getInstanceByCode(adapter, codePortlet);
                throw new Exception("not implemented");
            }

            // need rewrite
//            return portletObject;
        }
        catch (Throwable e){
            String es = "Error get news block ";
            log.error(es, e);
            throw new PortalException(es, e);
        }
    }
*/
    private static final boolean saveUploadedFilesToDisk = false;

    public static Map getParameters(HttpServletRequest request)
        throws IOException, UploadException, PortalException, MultipartRequestException {

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
                throw new PortalException("MultipartHandler is null");

            Set set = handler.getPartsHash().keySet();
            Iterator e = set.iterator();
            AbstractPart part = null;
            while (e.hasNext())
            {
                String key = (String) e.next();
                part = (AbstractPart) handler.getPartsHash().get(key);
                if (part instanceof List)
                {
                    throw new PortalException("Not implemented");
                }
                else
                {
                    if (part.getType()==AbstractPart.PARAMETER_TYPE)
                        p.put(key, part.getStringValue());
                    else
                        p.put(key, part.getInputStream() );
                }
            }
        }
        else
        {
            Enumeration e = request.getParameterNames();
            for (; e.hasMoreElements() ;)
            {
                Object obj = e.nextElement();

                String value[] = request.getParameterValues((String) obj);
                if (value!=null)
                {
                    if (value.length==1)
                        p.put(obj, value[0]);
                    else
                    {
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

    public static String getString(PortletRequest request, String f)
        throws ConfigException
    {
        return getString(request, f, null);
    }

    public static String getString(PortletRequest request, String f, String def)
        throws ConfigException
    {
        String s_ = def;
        if (request.getParameter(f) != null)
        {
            try
            {
                s_ = StringTools.convertString( request.getParameter(f), WebmillConfig.getServerCharset(), WebmillConfig.getHtmlCharset());
            }

            catch (ConfigException e)
            {
                log.warn("Exception in getString()", e);
                throw e;
            }
            catch (Exception e)
            {
                // not rethrow exception 'cos this method return def value in this case
                log.warn("Exception in getString(), def value will be return", e);
            }
        }
        return s_;
    }

    public static Long getLong(PortletRequest request, String f)
    {
        return getLong(request, f, null);
    }

    public static Long getLong(PortletRequest request, String f, Long def)
    {
        Long s_ = def;
        if (request.getParameter(f) != null)
        {
            try
            {
                s_ = new Long(request.getParameter(f));
            }
            catch (Exception e)
            {
                // not rethrow exception 'cos this method return def value in this case
                log.warn("Exception in getLong(), def value will be return", e);
            }
        }
        return s_;
    }

    public static Integer getInt(PortletRequest request, String f)
    {
        return getInt(request, f, null);
    }

    public static Integer getInt(PortletRequest request, String f, Integer def)
    {
        Integer s_ = def;
        if (request.getParameter(f) != null)
        {
            try
            {
                s_ = new Integer(request.getParameter(f));
            }
            catch (Exception e)
            {
                // not rethrow exception 'cos this method return def value in this case
                log.warn("Exception in getInteger(), def value will be return", e);
            }
        }
        return s_;
    }

    public static Double getDouble(PortletRequest request, String f)
    {
        return getDouble(request, f, null);
    }

    public static Double getDouble(PortletRequest request, String f, Double def)
    {
        Double s_ = def;
        if (request.getParameter(f) != null)
        {
            try
            {
                s_ = new Double(request.getParameter(f));
            }
            catch (Exception e)
            {
                // not rethrow exception 'cos this method return def value in this case
                log.warn("Exception in getDouble(), def value will be return", e);
            }
        }
        return s_;
    }

    public static Float getFloat(PortletRequest request, String f) {
        return getFloat(request, f, null);
    }

    public static Float getFloat(PortletRequest request, String f, Float def) {
        Float s_ = def;
        if (request.getParameter(f) != null) {
            try {
                s_ = new Float(request.getParameter(f));
            }
            catch (Exception e) {
                // not rethrow exception 'cos this method return def value in this case
                log.warn("Exception in getFloat(), def value will be return", e);
            }
        }
        return s_;
    }

    public static void include(
        PortletContext context,
        RenderRequest renderRequest, RenderResponse renderResponse,
        String path, Writer out_
        )
        throws javax.portlet.PortletException, IOException {
        PortletRequestDispatcher rd = context.getRequestDispatcher(path);
        rd.include(renderRequest, renderResponse);
    }

    public static void immediateRemoveAttribute( PortletSession session, String attr) {

        Object obj = session.getAttribute(attr);
        try {
            if (log.isDebugEnabled())
                log.debug("#12.12.001 search method 'clearObject'");

            Class cl = obj.getClass();
            Method m = cl.getMethod("clearObject", null);

            if (log.isDebugEnabled())
                log.debug("#12.12.002 invoke method 'clearObject'");

            if (m != null)
                m.invoke(obj, null);

            if (log.isDebugEnabled())
                log.debug("#12.12.003 complete invoke method 'clearObject'");
        }
        catch (Exception e) {
            if (log.isInfoEnabled())
                log.info("#12.12.003  method 'clearObject' not found. Error " + e.toString());
        }

        session.removeAttribute(attr);
        obj = null;
    }

    public static void cleanSession(PortletSession session) throws Exception {
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

}