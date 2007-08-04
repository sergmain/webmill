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

import org.riverock.portlet.main.AuthSessionBean;
import org.riverock.portlet.manager.site.DataProvider;
import org.riverock.portlet.manager.site.SiteSessionBean;
import org.riverock.portlet.manager.site.bean.CssBean;
import org.riverock.portlet.tools.FacesTools;
import org.riverock.portlet.tools.SiteUtils;

/**
 * @author Sergei Maslyukov
 * 16.05.2006
 * 20:14:59
 *
 *
 */
public class CssAction implements Serializable {
    private final static Logger log = Logger.getLogger( CssAction.class );
    private static final long serialVersionUID = 2057005511L;

    private SiteSessionBean siteSessionBean = null;
    private AuthSessionBean authSessionBean = null;
    private DataProvider dataProvider = null;

    public static final String[] ROLES = new String[]{"webmill.authentic"};

    public CssAction() {
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
    public String selectCss() {
        log.info( "Select CSS action." );
        loadCurrentObject();

        return "site";
    }

// Add actions
    public String addCssAction() {
        log.info( "Add CSS action." );

        CssBean cssBean = new CssBean();
        cssBean.setSiteId(siteSessionBean.getId());
        setSessionObject(cssBean);

        return "css-add";
    }

    public String processAddCssAction() {
        log.info( "Procss add CSS action." );

        SiteUtils.checkRights(FacesTools.getPortletRequest(), ROLES);

        if( getSessionObject() !=null ) {
            Long cssId = FacesTools.getPortalDaoProvider().getPortalCssDao().createCss(
                getSessionObject()
            );
            setSessionObject(null);
            siteSessionBean.setId(cssId);
            cleadDataProviderObject();
            loadCurrentObject();
        }

        return "site";
    }

    public String cancelAddCssAction() {
        log.info( "Cancel add CSS action." );

        setSessionObject(null);
        cleadDataProviderObject();

        return "site";
    }

// Edit actions
    public String editCssAction() {
        log.info( "Edit CSS action." );

        return "css-edit";
    }

    public String processEditCssAction() {
        log.info( "Save changes CSS action." );

        SiteUtils.checkRights(FacesTools.getPortletRequest(), ROLES);

        if( getSessionObject() !=null ) {
            FacesTools.getPortalDaoProvider().getPortalCssDao().updateCss(getSessionObject());
            cleadDataProviderObject();
            loadCurrentObject();
        }

        return "site";
    }

    public String cancelEditCssAction() {
        log.info( "Cancel edit CSS action." );

        loadCurrentObject();
        return "site";
    }

// Delete actions
    public String deleteCssAction() {
        log.info( "delete CSS action." );

        setSessionObject( new CssBean(dataProvider.getCss()) );

        return "css-delete";
    }

    public String cancelDeleteCssAction() {
        log.info( "Cancel delete CSS action." );

        return "site";
    }

    public String processDeleteCssAction() {
        log.info( "Process delete CSS action." );

        SiteUtils.checkRights(FacesTools.getPortletRequest(), ROLES);

        if( getSessionObject() != null ) {
            FacesTools.getPortalDaoProvider().getPortalCssDao().deleteCss(getSessionObject().getCssId());
            setSessionObject(null);
            siteSessionBean.setId(null);
            siteSessionBean.setObjectType(SiteSessionBean.UNKNOWN_TYPE);
            cleadDataProviderObject();
        }

        return "site";
    }

    private void setSessionObject(CssBean bean) {
        siteSessionBean.setCss( bean );
    }

    private void loadCurrentObject() {
        siteSessionBean.setCss( new CssBean(dataProvider.getCss()) );
    }

    private void cleadDataProviderObject() {
        dataProvider.clearCss();
    }

    private CssBean getSessionObject() {
        return siteSessionBean.getCss();
    }
}
