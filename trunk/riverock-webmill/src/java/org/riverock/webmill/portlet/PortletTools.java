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



import java.lang.reflect.Constructor;

import java.util.List;



import javax.servlet.http.HttpServletRequest;

import javax.portlet.RenderRequest;



import org.riverock.generic.db.DatabaseAdapter;

import org.riverock.webmill.schema.site.SiteTemplateParameterType;

import org.riverock.webmill.portlet.wrapper.RenderRequestWrapper;

import org.riverock.interfaces.schema.javax.portlet.InitParamType;

import org.riverock.interfaces.schema.javax.portlet.PortletType;

import org.riverock.common.tools.ServletTools;



import org.apache.log4j.Logger;



public class PortletTools

{

    private static Logger log = Logger.getLogger( "org.riverock.webmill.portlet.PortletTools" );





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

        if (request==null)

            return r;



//        request.getP





        return r;

    }



    public static String getStringParam( PortletType v, String name_ )

    {

        return getStringParam( v, name_, null);

    }



    public static String getStringParam( PortletType v, String name_, String defaultValue )

    {

        if (v==null || name_==null || name_.trim().length()==0)

            return defaultValue;



        for (int i=0; i<v.getInitParamCount(); i++)

        {

            InitParamType init = v.getInitParam(i);

            if (name_.equals(init.getName().getContent() ) )

            {

                return init.getValue()!=null?init.getValue().getContent():defaultValue;

            }

        }

        return defaultValue;

    }



    public static Boolean getBooleanParam( PortletType v, String name_ )

    {

        if (v==null || name_==null || name_.trim().length()==0)

            return null;



        for (int i=0; i<v.getInitParamCount(); i++)

        {

            InitParamType init = v.getInitParam(i);

            if (name_.equals(init.getName().getContent() ) )

                return new Boolean(init.getValue().getContent());

        }

        return null;

    }



    public static Long getLongParam( List v, String name_ )

    {

        if (v==null || name_==null || name_.trim().length()==0)

            return null;



        for (int i=0; i<v.size(); i++)

        {

            InitParamType init = (InitParamType)v.get(i);

            if (name_.equals(init.getName().getContent() ) )

                return new Long(init.getValue().getContent());

        }

        return null;

    }



    public static Long getIdPortlet(String namePortletID, HttpServletRequest request)

    {

        return ServletTools.getLong(request, namePortletID);

    }



    public static String getString( List v, String nameParam)

        throws IllegalArgumentException

    {

        return getString( v, nameParam, null );

    }



    public synchronized static String getString(List v, String nameParam, String defValue )

    {

        if (v==null || nameParam==null || nameParam.trim().length()==0)

            return defValue;



        for (int i=0; i<v.size(); i++)

        {

            SiteTemplateParameterType item = (SiteTemplateParameterType)v.get(i);

            if (item.getName().equals(nameParam))

                return item.getValue();

        }

        return defValue;

    }



    public static PortletResultObject getPortletObject(

            PortletType desc,

            PortletParameter portletParameter)

            throws Exception

    {

        try

        {

//            if (log.isDebugEnabled())

//                log.debug("isGetInstance - " + desc.getIsGetInstance());



//            HttpSession session = portletParameter.request.getSession();

//            String codePortlet = (String) session.getAttribute(Constants.PORTLET_CODE_SESSION);

            String codePortlet = portletParameter.getPortletCode();



            if (log.isDebugEnabled())

            {

                log.debug( "codePortlet " + codePortlet );

                log.debug( "Create instance of " + desc.getPortletClass() );

            }



///////////////////

            Constructor constructor = Class.forName(desc.getPortletClass()).getConstructor( null );



            if (log.isDebugEnabled())

                log.debug("#12.12.005  constructor is " + constructor);



            Portlet object = null;

            if (constructor != null)

                object = (Portlet) constructor.newInstance( null );

            else

                throw new Exception("Error get constructor for "+desc.getPortletClass());



            object.setParameter( portletParameter );



///////////

            PortletResultObject portletObject = null;

            // если нужно получить объект портлета без указания его кода

            if (codePortlet==null || codePortlet.length()==0)

            {

                if (log.isDebugEnabled())

                    log.debug("Get portlet object w/o portlet code. namePortletID - " + getStringParam( desc, name_portlet_id) );



                Long id = null;

                String namePortletId = getStringParam( desc, name_portlet_id);

                if ( namePortletId!= null && namePortletId.length()!=0)

                {

                    if (log.isDebugEnabled())

                        log.debug("Get ID from request");



                    id = getIdPortlet(namePortletId, portletParameter.getRequest());



                    if (log.isDebugEnabled())

                        log.debug("ID from request - "+id);



                    if (id == null)

                        return null;

                }

                portletObject = object.getInstance( DatabaseAdapter.getInstance(false), id );

            }

            else

            {

                if (log.isDebugEnabled())

                    log.debug("Get portlet object with portlet code. namePortletID - " + getStringParam( desc, name_portlet_id)+" code "+codePortlet );



                portletObject = object.getInstanceByCode( DatabaseAdapter.getInstance(false), codePortlet );

            }



///////////



            return portletObject;

        }

        catch(Exception e)

        {

            log.error("Error getPortletObject", e);

            throw e;

        }

    }





/*

    public static void processPortlet(

            HttpServletRequest request, HttpServletResponse response, Writer out

            )

            throws Exception

    {

        if (log.isDebugEnabled())

        {

            log.debug("#10.001 Start execute portlet");



            for (Enumeration e = request.getParameterNames(); e.hasMoreElements();)

            {

                String nameParam = (String) e.nextElement();



                log.debug("#10.002 Parameter:  " + nameParam + " value: " + ServletTools.getString(request, nameParam));

            }

        }





        PortletDescriptionType desc = getPortletDescription(request);

        if (desc == null)

        {

            if (log.isDebugEnabled())

                log.debug("PortletDescription is null");



//            response.sendRedirect( jspPage.toPage );

            return;

        }





        DatabaseAdapter db_ = DatabaseAdapter.getInstance(false);

        InitPage jspPage = new InitPage(db_, request, response,

                desc.getLocaleNamePackage(),

                Constants.NAME_LANG_PARAM, null, null);





        Portlet obj = getPortletObject(

                desc,

                request, response,

                jspPage

        );



        if (log.isDebugEnabled())

            log.debug("Portlet object is - " + obj);



        String parsingError = null;

        ContextConfig ctx = (ContextConfig) jspPage.p.ctxHash.get( desc.getPortletName());

//        if (ctx!=null && ctx.isXml && ctx.isXsltData)

        if (log.isDebugEnabled())

        {

            log.debug("ContextConfig  - " + ctx);

            if (ctx != null)

                log.debug("ContextConfig.translet - " + ctx.translet);

        }



        if (ctx != null && ctx.translet != null && obj.isXml() )

        {

            try

            {

                PortletTools.processXML(obj, ctx, out);

            }

            catch (Exception e)

            {

                log.error("Portlet parsing error", e);

                parsingError = ExceptionTools.getStackTrace(e, 5);

            }

        }



//        if (ctx==null ||!ctx.isXml ||!ctx.isXsltData ||

        if (ctx == null || ctx.translet == null || parsingError != null)

        {

            if (parsingError != null)

                out.write("<!--\n" + parsingError + "\n-->\n");



            if ( ctx == null)

                out.write("<!--\nContextConfig is null\n-->\n");



            if (ctx!=null &&  ctx.translet== null)

                out.write("<!--\nContextConfig.translet is null\n-->\n");



            if (log.isDebugEnabled())

                log.debug("Portlet isHtml() - " + obj.isHtml() );



            if (obj.isHtml())

            {

                out.write(obj.getPlainHTML());

            }

            else

            {

                out.write("portlet  '"+ desc.getPortletName()+ "', isHtml() - " + obj.isHtml() );

                return;

            }



        } // if (jspPage.p.ctxNews==null)



    }

*/

}