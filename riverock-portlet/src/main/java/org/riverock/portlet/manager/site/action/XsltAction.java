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
import org.riverock.portlet.manager.site.bean.XsltBean;
import org.riverock.portlet.tools.FacesTools;
import org.riverock.common.utils.PortletUtils;

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

    public static final String[] ROLES = new String[]{"webmill.authentic"};

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
        log.info( "Select xslt action." );
        loadCurrentObject();

        return "site";
    }

// Add actions
    public String addXsltAction() {
        log.info( "Add xslt action." );

        XsltBean xsltBean = new XsltBean();
        xsltBean.setSiteLanguageId(siteSessionBean.getId());
        setSessionObject(xsltBean);

        return "xslt-add";
    }

    public String processAddXsltAction() {
        log.info( "Procss add xslt action." );

        PortletUtils.checkRights(FacesTools.getPortletRequest(), ROLES);

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
        log.info( "Cancel add xslt action." );

        setSessionObject(null);
        cleadDataProviderObject();

        return "site";
    }

// Edit actions
    public String editXsltAction() {
        log.info( "Edit holding action." );

        return "xslt-edit";
    }

    public String processEditXsltAction() {
        log.info( "Save changes xslt action." );

        PortletUtils.checkRights(FacesTools.getPortletRequest(), ROLES);

        if( getSessionObject() !=null ) {
            FacesTools.getPortalDaoProvider().getPortalXsltDao().updateXslt(getSessionObject());
            cleadDataProviderObject();
            loadCurrentObject();
        }

        return "site";
    }

    public String cancelEditXsltAction() {
        log.info( "Cancel edit xslt action." );

        loadCurrentObject();
        return "site";
    }

// Delete actions
    public String deleteXsltAction() {
        log.info( "delete xslt action." );

        setSessionObject( new XsltBean(dataProvider.getXslt()) );

        return "xslt-delete";
    }

    public String cancelDeleteXsltAction() {
        log.info( "Cancel delete holding action." );

        return "site";
    }

    public String processDeleteXsltAction() {
        log.info( "Process delete xslt action." );

        PortletUtils.checkRights(FacesTools.getPortletRequest(), ROLES);

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
