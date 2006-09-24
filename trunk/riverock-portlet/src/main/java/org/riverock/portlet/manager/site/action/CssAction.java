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
package org.riverock.portlet.manager.site.action;

import java.io.Serializable;

import org.apache.log4j.Logger;

import org.riverock.portlet.main.AuthSessionBean;
import org.riverock.portlet.manager.site.DataProvider;
import org.riverock.portlet.manager.site.SiteSessionBean;
import org.riverock.portlet.manager.site.bean.CssBean;
import org.riverock.portlet.tools.FacesTools;

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

        if( getSessionObject() !=null ) {
            FacesTools.getPortalDaoProvider().getPortalCssDao().updateCss(getSessionObject());
            cleadDataProviderObject();
            loadCurrentObject();
        }

        return "site";
    }

    public String cancelEditCssAction() {
        log.info( "Cancel edit CSS action." );

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
