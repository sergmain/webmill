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
package org.riverock.portlet.member;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.ArrayList;

import javax.portlet.PortletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.riverock.common.tools.RsetTools;
import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.db.DatabaseManager;
import org.riverock.interfaces.portlet.member.PortletGetList;
import org.riverock.interfaces.portlet.member.ClassQueryItem;
import org.riverock.webmill.container.ContainerConstants;
import org.riverock.webmill.container.bean.PortletItem;
import org.riverock.webmill.container.portlet.PortletContainer;
import org.riverock.webmill.container.tools.PortletService;

/**
 * User: Admin
 * Date: Nov 24, 2002
 * Time: 3:58:34 PM
 *
 * $Id$
 */
public class ContextDataClassQuery extends BaseClassQuery {
    private static Log log = LogFactory.getLog( ContextDataClassQuery.class );

    private Long idSiteCtxType = null;

    private Long idContext = null;

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
    public String getCurrentValue( PortletRequest renderRequest, ResourceBundle bundle )
        throws Exception
    {
        PreparedStatement ps = null;
        ResultSet rs = null;
        DatabaseAdapter db_ = null;

        try {
            db_ = DatabaseAdapter.getInstance();
            ps = db_.prepareStatement(
                "select ID_SITE_CTX_TYPE, TYPE "+
                "from   WM_PORTAL_PORTLET_NAME "+
                "where  ID_SITE_CTX_TYPE=?"
            );

            RsetTools.setLong(ps, 1, idContext );

            rs = ps.executeQuery();
            if (rs.next())
                return RsetTools.getString(rs, "TYPE");
        }
        finally
        {
            DatabaseManager.close(db_, rs, ps);
            rs = null;
            ps = null;
            db_ = null;
        }

        return "";
    }

    /**
     *  ¬озвращает список возможных значений дл€ построени€ <select> элемента
     * @return Vector of org.riverock.member.ClassQueryItem
     */
    public List<ClassQueryItem> getSelectList( PortletRequest renderRequest, ResourceBundle bundle )
        throws Exception
    {
        PreparedStatement ps = null;
        ResultSet rs = null;

        List<ClassQueryItem> v = new ArrayList<ClassQueryItem>();
        String namePortlet = null;
        DatabaseAdapter db_ = null;
        try {
            db_ = DatabaseAdapter.getInstance();
            ps = db_.prepareStatement(
                    "select ID_SITE_CTX_TYPE, TYPE "+
                    "from   WM_PORTAL_PORTLET_NAME "+
                    "where  ID_SITE_CTX_TYPE=?"
            );

            RsetTools.setLong(ps, 1, idSiteCtxType );

            rs = ps.executeQuery();
            if (!rs.next())
                return v;

            namePortlet = RsetTools.getString( rs, "TYPE" );

        }
        finally
        {
            DatabaseManager.close(db_, rs, ps);
            rs = null;
            ps = null;
            db_ = null;
        }

        if (log.isDebugEnabled())
            log.debug("namePortlet "+namePortlet);

        PortletContainer portletContainer = (PortletContainer)renderRequest.getAttribute( ContainerConstants.PORTAL_CURRENT_CONTAINER );
        PortletItem portletItem = portletContainer.searchPortletItem( namePortlet );

        if (log.isDebugEnabled())
            log.debug("portletItem "+portletItem);

        if ( portletItem==null )
            return v;

        String classNameTemp =
            PortletService.getStringParam(
                portletItem.getPortletDefinition(), ContainerConstants.class_name_get_list
            );

        if (classNameTemp==null)
            return v;

        Constructor constructor = null;
        try
        {
            constructor = Class.forName(classNameTemp).getConstructor(new Class[]{});
        }
        catch (Exception e)
        {
            log.error("Error getConstructor()", e);
            throw e;
        }

        if (log.isDebugEnabled())
            log.debug("#12.12.005  constructor is " + constructor);

        if (constructor != null) {
            PortletGetList obj = null;
            Object o = null;
            try {
                o = constructor.newInstance(new Object[]{});
                obj = (PortletGetList)o;
            }
            catch (InvocationTargetException e) {
                log.error("Error invoke constructor ", e);
                throw e;
            }
            catch (ClassCastException e) {
                if (o!=null) {
                    log.error("ClassCastException to PortletGetList.class  from "+o.getClass().getName(), e);
                }
                else {
                    log.error("ClassCastException to PortletGetList.class  from null", e);
                }
                throw e;
            }

            if (log.isDebugEnabled())
            {
                log.debug("#12.12.008 object " + obj);
                log.debug("#12.12.009 localePack  " +
                    PortletService.getStringParam(
                        portletItem.getPortletDefinition(), ContainerConstants.locale_name_package
                    )
                );
            }

            v = obj.getList( idSiteCtxLangCatalog, idContext);

            if (v==null)
                return new ArrayList<ClassQueryItem>();

        }

        return v;
    }

    MemberQueryParameter param = null;

    public void setQueryParameter(MemberQueryParameter parameter) throws Exception
    {
        this.param = parameter;
    }
}
