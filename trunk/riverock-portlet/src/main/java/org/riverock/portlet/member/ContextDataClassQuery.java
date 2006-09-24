/*
 * org.riverock.portlet - Portlet Library
 *
 * Copyright (C) 2006, Riverock Software, All Rights Reserved.
 *
 * Riverock - The Open-source Java Development Community
 * http://www.riverock.org
 *
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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

import org.apache.log4j.Logger;

import org.riverock.common.tools.RsetTools;
import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.db.DatabaseManager;
import org.riverock.interfaces.portlet.member.PortletGetList;
import org.riverock.interfaces.portlet.member.ClassQueryItem;
import org.riverock.webmill.container.ContainerConstants;
import org.riverock.webmill.container.bean.PortletWebApplication;
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
    private static Logger log = Logger.getLogger( ContextDataClassQuery.class );

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
        PortletWebApplication portletItem = portletContainer.searchPortletItem( namePortlet );

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
