/*

 * org.riverock.portlet -- Portlet Library

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

 * User: Admin

 * Date: Nov 24, 2002

 * Time: 3:58:34 PM

 *

 * $Id$

 */

package org.riverock.portlet.member;



import java.lang.reflect.Constructor;

import java.lang.reflect.InvocationTargetException;

import java.sql.PreparedStatement;

import java.sql.ResultSet;

import java.util.Vector;

import java.util.List;

import java.util.ArrayList;



import javax.servlet.http.HttpServletRequest;



import org.riverock.generic.db.DatabaseAdapter;

import org.riverock.generic.db.DatabaseManager;

import org.riverock.interfaces.schema.javax.portlet.PortletType;

import org.riverock.common.tools.RsetTools;

import org.riverock.portlet.member.BaseClassQuery;

import org.riverock.webmill.portlet.PortletManager;

import org.riverock.webmill.portlet.PortletTools;

import org.riverock.webmill.portlet.PortletGetList;



import org.apache.log4j.Logger;



public class ContextDataClassQuery extends BaseClassQuery

{

    private static Logger log = Logger.getLogger( "org.riverock.portlet.member.ContextDataClassQuery" );



    // ID_SITE_CTX_TYPE

    private Long idSiteCtxType = null;



    // ID_CONTEXT

    private Long idContext = null;



    // ID_SITE_CTX_LANG_CATALOG

    private Long idSiteCtxLangCatalog = null;



    public void setIdSiteCtxLangCatalog(Long param)

    {

        idSiteCtxLangCatalog = param;



        if (log.isDebugEnabled())

            log.debug("idSiteCtxLangCatalog - "+idSiteCtxLangCatalog);

    }



    public void setIdSiteCtxType(Long param)

    {

        idSiteCtxType = param;



        if (log.isDebugEnabled())

            log.debug("idSiteCtxType - "+idSiteCtxType);

    }



    public void setIdContext(Long param)

    {

        idContext = param;



        if (log.isDebugEnabled())

            log.debug("idContext - "+idContext);

    }



    /**

     * ¬озвращает текущее значение дл€ отображени€ на веб-странице

     * @return String

     */

    public String getCurrentValue(HttpServletRequest request)

        throws Exception

    {

        PreparedStatement ps = null;

        ResultSet rs = null;

        DatabaseAdapter db_ = DatabaseAdapter.getInstance( false );



        try {

            ps = db_.prepareStatement(

                "select ID_SITE_CTX_TYPE, TYPE "+

                "from SITE_CTX_TYPE "+

                "where ID_SITE_CTX_TYPE=?"

            );



            RsetTools.setLong(ps, 1, idContext );



            rs = ps.executeQuery();

            if (rs.next())

                return RsetTools.getString(rs, "TYPE");

        }

        finally

        {

            org.riverock.generic.db.DatabaseManager.close(rs, ps);

            rs = null;

            ps = null;

        }



        return "";

    }



    /**

     *  ¬озвращает список возможных значений дл€ построени€ <select> элемента

     * @return Vector of org.riverock.member.ClassQueryItem

     */

    public List getSelectList(HttpServletRequest request)

        throws Exception

    {

        PreparedStatement ps = null;

        ResultSet rs = null;



        List v = new ArrayList();

        String namePortlet = null;

        DatabaseAdapter db_ = null;

        try {

            db_ = DatabaseAdapter.getInstance( false );

            ps = db_.prepareStatement(

                    "select ID_SITE_CTX_TYPE, TYPE "+

                    "from SITE_CTX_TYPE "+

                    "where ID_SITE_CTX_TYPE=?"

            );



            RsetTools.setLong(ps, 1, idSiteCtxType );



            rs = ps.executeQuery();

            if (!rs.next())

                return v;



            namePortlet = RsetTools.getString( rs, "TYPE" );



        }

        finally

        {

            DatabaseManager.close(rs, ps);

            rs = null;

            ps = null;

        }



        if (log.isDebugEnabled())

            log.debug("namePortlet "+namePortlet);



        PortletType portlet = PortletManager.getPortletDescription( namePortlet );



        if (log.isDebugEnabled())

            log.debug("portlet "+portlet);



        if (portlet==null)

            return v;



        String classNameTemp =

            PortletTools.getStringParam(

                portlet, PortletTools.class_name_get_list

            );



        if (classNameTemp==null)

            return v;



        Constructor constructor = null;

        try

        {

            constructor = Class.forName(classNameTemp).getConstructor(null);

        }

        catch (Exception e)

        {

            log.error("Error getConstructor()", e);

            throw e;

        }



        if (log.isDebugEnabled())

            log.debug("#12.12.005  constructor is " + constructor);



        if (constructor != null)

        {

            PortletGetList obj = null;

            try

            {

                obj = (PortletGetList) constructor.newInstance( null );

            }

            catch(InvocationTargetException e)

            {

                log.error("Error invoke constructor ",e);

                throw e;

            }



            if (log.isDebugEnabled())

            {

                log.debug("#12.12.008 object " + obj);

                log.debug("#12.12.009 localePack  " +

                    PortletTools.getStringParam(

                        portlet, PortletTools.locale_name_package

                    )

                );

            }



            v = obj.getList( idSiteCtxLangCatalog, idContext);



            if (v==null)

                return new Vector();



        }



        return v;

    }



    MemberQueryParameter param = null;



    public void setQueryParameter(MemberQueryParameter parameter) throws Exception

    {

        this.param = parameter;

    }

}

