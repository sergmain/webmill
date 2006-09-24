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

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.portlet.PortletRequest;

import org.apache.log4j.Logger;

import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.interfaces.portal.bean.Template;
import org.riverock.interfaces.portal.dao.PortalDaoProvider;
import org.riverock.interfaces.portlet.member.ClassQueryItem;
import org.riverock.webmill.container.tools.PortletService;

/**
 * User: Admin
 * Date: Nov 24, 2002
 * Time: 3:58:34 PM
 * <p/>
 * $Id$
 */
public final class TemplateMemberClassQuery extends BaseClassQuery {
    private final static Logger log = Logger.getLogger( TemplateMemberClassQuery.class );

    private Long idSiteTemplate = null;

    public final static String nameModule = "site.mem.lang";
    public final static String nameField = "ID_SITE_SUPPORT_LANGUAGE";

    MemberQueryParameter param = null;

    public void setIdSiteTemplate( Long param ) {
        idSiteTemplate = param;

        if( log.isDebugEnabled() ) {
            if( idSiteTemplate != null )
                log.debug( "idSiteTemplate - " + idSiteTemplate );
            else
                log.debug( "idSiteTemplate is null" );
        }
    }

    /**
     * ¬озвращает текущее значение дл€ отображени€ на веб-странице
     *
     * @return String
     */
    public String getCurrentValue( PortletRequest renderRequest, ResourceBundle bundle ) {
        PortalDaoProvider portalDaoProvider = MemberTools.getPortalDaoProvider(renderRequest);
        Template templateBean = portalDaoProvider.getPortalCommonDao().getTemplate( idSiteTemplate );
        if( templateBean != null )
            return templateBean.getTemplateName();

        return "";
    }

    /**
     * ¬озвращает список возможных значений дл€ построени€ <select> элемента
     *
     * @return List of org.riverock.member.ClassQueryItem
     */
    public List<ClassQueryItem> getSelectList( PortletRequest renderRequest, ResourceBundle bundle ) throws Exception {
        DatabaseAdapter db_ = null;
        try {
            db_ = DatabaseAdapter.getInstance();
            List<ClassQueryItem> v = new ArrayList<ClassQueryItem>();

            Long id = PortletService.getLong( renderRequest, nameModule + '.' + nameField );
            PortalDaoProvider portalDaoProvider = MemberTools.getPortalDaoProvider(renderRequest);
            List<Template> beans = portalDaoProvider.getPortalCommonDao().getTemplateLanguageList( id );

            if( log.isDebugEnabled() ) {
                log.debug( "parameter " + nameModule + '.' + nameField + " is " + renderRequest.getParameter( nameModule + '.' + nameField ) );
                log.debug( "id " + id );
            }
            for (Template templateBean : beans) {
                ClassQueryItem item = new ClassQueryItemImpl(
                    templateBean.getTemplateId(), templateBean.getTemplateName());

                if (item.getIndex().equals(idSiteTemplate))
                    item.setSelected(true);
                v.add(item);
            }
            return v;
        }
        finally {
            DatabaseAdapter.close( db_ );
        }
    }

    public void setQueryParameter( MemberQueryParameter parameter ) throws Exception {
        this.param = parameter;
    }
}
