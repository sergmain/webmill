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

import java.lang.reflect.Constructor;

import java.lang.reflect.Method;

import java.util.*;



import javax.portlet.*;

import javax.servlet.http.HttpServletRequest;

import javax.servlet.http.HttpServletResponse;



import org.apache.log4j.Logger;

import org.riverock.common.config.ConfigException;

import org.riverock.common.multipart.*;

import org.riverock.common.tools.StringTools;

import org.riverock.generic.db.DatabaseAdapter;

import org.riverock.interfaces.schema.javax.portlet.InitParamType;

import org.riverock.interfaces.schema.javax.portlet.PortletType;

import org.riverock.webmill.config.WebmillConfig;

import org.riverock.webmill.exception.PortalException;

import org.riverock.webmill.portlet.wrapper.RenderRequestWrapper;

import org.riverock.webmill.schema.site.SiteTemplateParameterType;



public class PortletTools

{

    private static Logger log = Logger.getLogger("org.riverock.webmill.portlet.PortletTools");





    public static final String type_portlet = "type-portlet";

    public static final String is_url = "is-url";

    public static final String name_portlet_id = "name-portlet-id";

    public static final String locale_name_package = "locale-name-package";

    public static final String name_portlet_code_string = "name-portlet-code-string";

    public static final String is_get_instance = "is-get-instance";

    public static final String class_name_get_list = "class-name-get-list";





    public static RenderRequest getRenderRequest(HttpServletRequest request)

    {

        RenderRequestWrapper r = new RenderRequestWrapper();

        if (request == null)

            return r;



        return r;

    }



    public static String getStringParam(PortletType v, String name_)

    {

        return getStringParam(v, name_, null);

    }



    public static String getStringParam(PortletType v, String name_, String defaultValue)

    {

        if (v == null || name_ == null || name_.trim().length() == 0)

            return defaultValue;



        for (int i = 0; i < v.getInitParamCount(); i++)

        {

            InitParamType init = v.getInitParam(i);

            if (name_.equals(init.getName().getContent()))

            {

                return init.getValue() != null ? init.getValue().getContent() : defaultValue;

            }

        }

        return defaultValue;

    }



    public static Boolean getBooleanParam(PortletType v, String name_)

    {

        if (v == null || name_ == null || name_.trim().length() == 0)

            return null;



        for (int i = 0; i < v.getInitParamCount(); i++)

        {

            InitParamType init = v.getInitParam(i);

            if (name_.equals(init.getName().getContent()))

                return new Boolean(init.getValue().getContent());

        }

        return null;

    }



    public static Long getLongParam(List v, String name_)

    {

        if (v == null || name_ == null || name_.trim().length() == 0)

            return null;



        for (int i = 0; i < v.size(); i++)

        {

            InitParamType init = (InitParamType) v.get(i);

            if (name_.equals(init.getName().getContent()))

                return new Long(init.getValue().getContent());

        }

        return null;

    }



    public static Long getIdPortlet(String namePortletID, PortletRequest request)

    {

        return getLong(request, namePortletID);

    }



    public static String getString(List v, String nameParam)

        throws IllegalArgumentException

    {

        return getString(v, nameParam, null);

    }



    public synchronized static String getString(List v, String nameParam, String defValue)

    {

        if (v == null || nameParam == null || nameParam.trim().length() == 0)

            return defValue;



        for (int i = 0; i < v.size(); i++)

        {

            SiteTemplateParameterType item = (SiteTemplateParameterType) v.get(i);

            if (item.getName().equals(nameParam))

                return item.getValue();

        }

        return defValue;

    }



    public static PortletResultObject getPortletObject(

        PortletType desc,

        PortletParameter portletParameter,

        DatabaseAdapter adapter,

        Long portletId,

        PortletRequest portletRequest

        )

        throws Exception

    {

        try

        {

            String codePortlet = portletParameter.getPortletCode();



            if (log.isDebugEnabled())

            {

                log.debug("codePortlet " + codePortlet);

                log.debug("Create instance of " + desc.getPortletClass());

            }



///////////////////

            Constructor constructor = Class.forName(desc.getPortletClass()).getConstructor(null);



            if (log.isDebugEnabled())

                log.debug("#12.12.005  constructor is " + constructor);



            Portlet object = null;

            if (constructor != null)

                object = (Portlet) constructor.newInstance(null);

            else

                throw new Exception("Error get constructor for " + desc.getPortletClass());



            object.setParameter(portletParameter);



///////////

            PortletResultObject portletObject = null;

            // ���� ����� �������� ������ �������� ��� �������� ��� ����

            if (codePortlet == null || codePortlet.length() == 0)

            {

                if (log.isDebugEnabled())

                    log.debug("Get portlet object w/o portlet code. namePortletID - " + getStringParam(desc, name_portlet_id));



                Long id = null;

                if (portletId == null)

                {

                    String namePortletId = getStringParam(desc, name_portlet_id);

                    if (namePortletId != null && namePortletId.length() != 0)

                    {

                        if (log.isDebugEnabled())

                            log.debug("Get ID from request");



                        id = getIdPortlet(namePortletId, portletRequest);



                        if (log.isDebugEnabled())

                            log.debug("ID from request - " + id);



                        if (id == null)

                            return null;

                    }

                }

                else

                    id = portletId;



                portletObject = object.getInstance(adapter, id);

            }

            else

            {

                if (log.isDebugEnabled())

                    log.debug("Get portlet object with portlet code. namePortletID - " + getStringParam(desc, name_portlet_id) + " code " + codePortlet);



                portletObject = object.getInstanceByCode(adapter, codePortlet);

            }



///////////



            return portletObject;

        }

        catch (Exception e)

        {

            log.error("Error getPortletObject", e);

            throw e;

        }

    }



    private static final boolean saveUploadedFilesToDisk = false;



    public static Map getParameters(HttpServletRequest request)

        throws IOException, UploadException, PortalException, MultipartRequestException

    {



        Map p = new HashMap();

        String contentType = request.getContentType();



        if (contentType != null &&

            contentType.toLowerCase().indexOf(MultipartRequestWrapper.MFDHEADER) > -1)

        {			// it's a multipart request



            MultipartRequestWrapper reqWrapper =

                new MultipartRequestWrapper(

                    request,

                    saveUploadedFilesToDisk,

                    null,

                    false,

                    false,

                    1024 * 1024

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



    public static Float getFloat(PortletRequest request, String f)

    {

        return getFloat(request, f, null);

    }



    public static Float getFloat(PortletRequest request, String f, Float def)

    {

        Float s_ = def;

        if (request.getParameter(f) != null)

        {

            try

            {

                s_ = new Float(request.getParameter(f));

            }

            catch (Exception e)

            {

                // not rethrow exception 'cos this method return def value in this case

                log.warn("Exception in getFloat(), def value will be return", e);

            }

        }

        return s_;

    }



    public static void include(RenderRequest renderRequest, HttpServletResponse response,

                               String path, Writer out_

                               )

        throws javax.portlet.PortletException, IOException

    {



//        RequestDispatcher rd = request.getContextgetRequestDispatcher(path);

//

//        rd.include(request,

//            new ServletResponseWrapperInclude(response, out_)

//        );

        PortletContext context = null;

        PortletRequestDispatcher rd = context.getRequestDispatcher(path);

        rd.include(renderRequest, null);

    }



    public static void immediateRemoveAttribute(PortletSession session,

                                                String attr)

    {

        Object obj = session.getAttribute(attr);

        try

        {

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

        catch (Exception e)

        {

            if (log.isInfoEnabled())

                log.info("#12.12.003  method 'clearObject' not found. Error " + e.toString());

        }



        session.removeAttribute(attr);

        obj = null;

    }



    public static void cleanSession(PortletSession session)

        throws Exception

    {

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