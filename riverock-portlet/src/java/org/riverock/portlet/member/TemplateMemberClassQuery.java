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

import java.util.ArrayList;
import java.util.List;

import javax.portlet.PortletRequest;

import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.webmill.core.GetSiteTemplateItem;
import org.riverock.webmill.core.GetSiteTemplateWithIdSiteSupportLanguageList;
import org.riverock.webmill.portlet.PortletTools;
import org.riverock.webmill.schema.core.SiteTemplateItemType;
import org.riverock.webmill.schema.core.SiteTemplateListType;
import org.riverock.interfaces.portlet.member.ClassQueryItem;

import org.apache.log4j.Logger;


/**
 * User: Admin
 * Date: Nov 24, 2002
 * Time: 3:58:34 PM
 *
 * $Id$
 */
public final class TemplateMemberClassQuery extends BaseClassQuery {
    private final static Logger log = Logger.getLogger( TemplateMemberClassQuery.class );

    private Long idSiteTemplate = null;

    public final static String nameModule = "site.mem.lang";
    public final static String nameField = "ID_SITE_SUPPORT_LANGUAGE";

    MemberQueryParameter param = null;

    public void setIdSiteTemplate(Long param)
    {
        idSiteTemplate = param;

        if (log.isDebugEnabled())
            if (idSiteTemplate!=null)
                log.debug("idSiteTemplate - "+idSiteTemplate.longValue());
            else
                log.debug("idSiteTemplate is null");
    }

    /**
     * ¬озвращает текущее значение дл€ отображени€ на веб-странице
     * @return String
     */
    public String getCurrentValue( PortletRequest renderRequest )
        throws Exception
    {
        DatabaseAdapter db_ = null;
        try
        {
            db_ = DatabaseAdapter.getInstance(false);
            SiteTemplateItemType templateItem =
                GetSiteTemplateItem.getInstance(db_, idSiteTemplate).item;

            if (templateItem!=null)
                return templateItem.getNameSiteTemplate();

            return "";
        }
        finally
        {
            DatabaseAdapter.close( db_ );
            db_ = null;
        }
    }

    /**
     *  ¬озвращает список возможных значений дл€ построени€ <select> элемента
     * @return List of org.riverock.member.ClassQueryItem
     */
    public List getSelectList( PortletRequest renderRequest ) throws Exception
    {
        DatabaseAdapter db_ = null;
        try
        {
            db_ = DatabaseAdapter.getInstance(false);
            List v = new ArrayList();

            Long id = PortletTools.getLong(renderRequest, nameModule+'.'+nameField);
            SiteTemplateListType templateList = GetSiteTemplateWithIdSiteSupportLanguageList.getInstance(db_, id).item;

            if (log.isDebugEnabled())
            {
                log.debug("parameter "+nameModule+'.'+nameField+" is "+renderRequest.getParameter(nameModule+'.'+nameField));
                log.debug("id "+id);
            }
            for (int i=0; i<templateList.getSiteTemplateCount(); i++)
            {
                SiteTemplateItemType templateItem = templateList.getSiteTemplate(i);
                ClassQueryItem item = new ClassQueryItemImpl(
                    templateItem.getIdSiteTemplate(),
                    templateItem.getNameSiteTemplate()
                );
                if (item.getIndex().equals(idSiteTemplate))
                    item.setSelected(true);
                v.add( item );
            }
            return v;
        }
        finally
        {
            DatabaseAdapter.close(db_);
            db_ = null;
        }
    }

    public void setQueryParameter(MemberQueryParameter parameter) throws Exception
    {
        this.param = parameter;
    }
}
