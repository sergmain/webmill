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
package org.riverock.portlet.manager.portletname;

import java.io.Serializable;

import org.apache.log4j.Logger;

import org.riverock.interfaces.portal.bean.PortletName;
import org.riverock.portlet.main.AuthSessionBean;
import org.riverock.portlet.tools.FacesTools;

/**
 * @author SergeMaslyukov
 *         Date: 26.02.2006
 *         Time: 15:55:44
 *         $Id$
 */
public class PortletNameAction implements Serializable {
    private final static Logger log = Logger.getLogger(PortletNameAction.class);

    private static final long serialVersionUID = 2057005501L;

    private PortletNameSessionBean portletNameSessionBean = null;
    private AuthSessionBean authSessionBean = null;

    public PortletNameAction() {
    }

    public AuthSessionBean getAuthSessionBean() {
        return authSessionBean;
    }

    public void setAuthSessionBean(AuthSessionBean authSessionBean) {
        this.authSessionBean = authSessionBean;
    }

    public PortletNameSessionBean getSessionBean() {
        return portletNameSessionBean;
    }

    public void setPortletNameSessionBean(PortletNameSessionBean portletNameSessionBean) {
        this.portletNameSessionBean = portletNameSessionBean;
    }

    public String addPortletName() {
        portletNameSessionBean.setPortletName( new PortletNameBean() );

        return "portlet-name-add";
    }

    public String processAddPortletName() {
        Long id = FacesTools.getPortalDaoProvider().getPortalPortletNameDao().createPortletName( portletNameSessionBean.getPortletName() );
        portletNameSessionBean.setCurrentPortletNameId( id );
        loadCurrentPortletName();
        return "portlet-name";
    }

    public String cancelAddPortletName() {
        loadCurrentPortletName();
        return "portlet-name";
    }

    public String processEditPortletName() {
        FacesTools.getPortalDaoProvider().getPortalPortletNameDao().updatePortletName( portletNameSessionBean.getPortletName() );
        return "portlet-name";
    }

    public String cancelEditPortletName() {
        loadCurrentPortletName();
        return "portlet-name";
    }

    public String processDeletePortletName() {
        FacesTools.getPortalDaoProvider().getPortalPortletNameDao().deletePortletName( portletNameSessionBean.getPortletName() );
        portletNameSessionBean.setPortletName( null );
        return "portlet-name";
    }

    public String selectPortletName() {
        loadCurrentPortletName();
        return "portlet-name";
    }

    private void loadCurrentPortletName() {
        PortletName bean = FacesTools.getPortalDaoProvider().getPortalPortletNameDao().getPortletName( portletNameSessionBean.getCurrentPortletNameId() );
        portletNameSessionBean.setPortletName( new PortletNameBean(bean) );
    }
}
