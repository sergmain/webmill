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
import org.riverock.portlet.manager.site.bean.XsltBean;
import org.riverock.portlet.tools.FacesTools;

/**
 * @author Sergei Maslyukov
 * 16.05.2006
 * 20:14:59
 *
 *
 */
public class XsltAction implements Serializable {
    private final static Logger log = Logger.getLogger( XsltAction.class );
    private static final long serialVersionUID = 2057005511L;

    private SiteSessionBean siteSessionBean = null;
    private AuthSessionBean authSessionBean = null;
    private DataProvider dataProvider = null;

    public XsltAction() {
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
    public String selectXslt() {
        XsltAction.log.info( "Select xslt action." );
        loadCurrentObject();

        return "site";
    }

// Add actions
    public String addXsltAction() {
        XsltAction.log.info( "Add xslt action." );

        XsltBean xsltBean = new XsltBean();
        xsltBean.setSiteLanguageId(siteSessionBean.getId());
        setSessionObject(xsltBean);

        return "xslt-add";
    }

    public String processAddXsltAction() {
        XsltAction.log.info( "Procss add xslt action." );

        if( getSessionObject() !=null ) {
            Long xsltId = FacesTools.getPortalDaoProvider().getPortalXsltDao().createXslt(
                getSessionObject()
            );
            setSessionObject(null);
            siteSessionBean.setId(xsltId);
            cleadDataProviderObject();
            loadCurrentObject();
        }

        return "site";
    }

    public String cancelAddXsltAction() {
        XsltAction.log.info( "Cancel add xslt action." );

        setSessionObject(null);
        cleadDataProviderObject();

        return "site";
    }

// Edit actions
    public String editXsltAction() {
        XsltAction.log.info( "Edit holding action." );

        return "xslt-edit";
    }

    public String processEditXsltAction() {
        XsltAction.log.info( "Save changes xslt action." );

        if( getSessionObject() !=null ) {
            FacesTools.getPortalDaoProvider().getPortalXsltDao().updateXslt(getSessionObject());
            cleadDataProviderObject();
            loadCurrentObject();
        }

        return "site";
    }

    public String cancelEditXsltAction() {
        XsltAction.log.info( "Cancel edit xslt action." );

        return "site";
    }

// Delete actions
    public String deleteXsltAction() {
        XsltAction.log.info( "delete xslt action." );

        setSessionObject( new XsltBean(dataProvider.getXslt()) );

        return "xslt-delete";
    }

    public String cancelDeleteXsltAction() {
        XsltAction.log.info( "Cancel delete holding action." );

        return "site";
    }

    public String processDeleteXsltAction() {
        XsltAction.log.info( "Process delete xslt action." );

        if( getSessionObject() != null ) {
            FacesTools.getPortalDaoProvider().getPortalXsltDao().deleteXslt(getSessionObject().getId());
            setSessionObject(null);
            siteSessionBean.setId(null);
            siteSessionBean.setObjectType(SiteSessionBean.UNKNOWN_TYPE);
            cleadDataProviderObject();
        }

        return "site";
    }

    private void setSessionObject(XsltBean bean) {
        siteSessionBean.setXslt( bean );
    }

    private void loadCurrentObject() {
        siteSessionBean.setXslt( new XsltBean(dataProvider.getXslt()) );
    }

    private void cleadDataProviderObject() {
        dataProvider.clearXslt();
    }

    private XsltBean getSessionObject() {
        return siteSessionBean.getXslt();
    }
}
