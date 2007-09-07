/*
 * org.riverock.portlet - Portlet Library
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
package org.riverock.portlet.manager.site.action;

import java.io.Serializable;

import org.apache.log4j.Logger;

import org.riverock.common.utils.PortletUtils;
import org.riverock.portlet.main.AuthSessionBean;
import org.riverock.portlet.manager.bean.TemplateBean;
import org.riverock.portlet.manager.site.DataProvider;
import org.riverock.portlet.manager.site.SiteSessionBean;
import org.riverock.portlet.tools.FacesTools;

/**
 * @author Sergei Maslyukov
 * 16.05.2006
 * 20:14:59
 *
 *
 */
public class TemplateAction implements Serializable {
    private final static Logger log = Logger.getLogger( TemplateAction.class );
    private static final long serialVersionUID = 2057005511L;

    private SiteSessionBean siteSessionBean = null;
    private AuthSessionBean authSessionBean = null;
    private DataProvider dataProvider = null;

    public static final String[] ROLES = new String[]{"webmill.authentic"};

    public TemplateAction() {
    }

    public void setDataProvider(DataProvider dataProvider) {
        this.dataProvider = dataProvider;
    }

    // getter/setter methods
    public void setSiteSessionBean( SiteSessionBean siteSessionBean) {
        this.siteSessionBean = siteSessionBean;
    }

    public AuthSessionBean getAuthSessionBean() {
        return authSessionBean;
    }

    public void setAuthSessionBean( AuthSessionBean authSessionBean) {
        this.authSessionBean = authSessionBean;
    }

// main select action
    public String selectTemplate() {
        log.info( "Select template action." );
        loadCurrentObject();

        return "site";
    }

// Add actions
    public String addTemplateAction() {
        log.info( "Add template action." );

        TemplateBean templateBean = new TemplateBean();
        templateBean.setSiteLanguageId(siteSessionBean.getId());
        setSessionObject(templateBean);

        return "template-add";
    }

    public String processAddTemplateAction() {
        log.info( "Procss add template action." );

        PortletUtils.checkRights(FacesTools.getPortletRequest(), ROLES);

        if( getSessionObject() !=null ) {
            Long templateId = FacesTools.getPortalSpiProvider().getPortalTemplateDao().createTemplate(
                getSessionObject()
            );
            setSessionObject(null);
            siteSessionBean.setId(templateId);
            cleadDataProviderObject();
            loadCurrentObject();
        }

        return "site";
    }

    public String cancelAddTemplateAction() {
        log.info( "Cancel add template action." );

        setSessionObject(null);
        cleadDataProviderObject();

        return "site";
    }

// Edit actions
    public String editTemplateAction() {
        log.info( "Edit holding action." );

        return "tenmplate-edit";
    }

    public String processEditTemplateAction() {
        log.info( "Save changes template action." );

        PortletUtils.checkRights(FacesTools.getPortletRequest(), ROLES);

        if( getSessionObject() !=null ) {
            FacesTools.getPortalSpiProvider().getPortalTemplateDao().updateTemplate(getSessionObject());
            cleadDataProviderObject();
            loadCurrentObject();
        }

        return "site";
    }

    public String cancelEditTemplateAction() {
        log.info( "Cancel edit template action." );

        loadCurrentObject();
        return "site";
    }

// Delete actions
    public String deleteTemplateAction() {
        log.info( "delete template action." );

        setSessionObject( new TemplateBean(dataProvider.getTemplate()) );

        return "template-delete";
    }

    public String cancelDeleteTemplateAction() {
        log.info( "Cancel delete holding action." );

        return "site";
    }

    public String processDeleteTemplateAction() {
        log.info( "Process delete template action." );

        PortletUtils.checkRights(FacesTools.getPortletRequest(), ROLES);

        if( getSessionObject() != null ) {
            FacesTools.getPortalSpiProvider().getPortalTemplateDao().deleteTemplate(getSessionObject().getTemplateId());
            setSessionObject(null);
            siteSessionBean.setId(null);
            siteSessionBean.setObjectType(SiteSessionBean.UNKNOWN_TYPE);
            cleadDataProviderObject();
        }

        return "site";
    }

    private void setSessionObject(TemplateBean bean) {
        siteSessionBean.setTemplate( bean );
    }

    private void loadCurrentObject() {
        siteSessionBean.setTemplate( new TemplateBean(dataProvider.getTemplate()) );
    }

    private void cleadDataProviderObject() {
        dataProvider.clearTemplate();
    }

    private TemplateBean getSessionObject() {
        if (log.isDebugEnabled()) {
            log.debug("siteSessionBean.getTemplate(): " + siteSessionBean.getTemplate());
            if (siteSessionBean.getTemplate()!=null) {
                log.debug("template.isDefaultDynamic: " + siteSessionBean.getTemplate().isDefaultDynamic());
            }
        }
        return siteSessionBean.getTemplate();
    }
}
