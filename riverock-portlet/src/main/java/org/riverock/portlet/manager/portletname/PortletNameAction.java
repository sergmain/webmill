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
package org.riverock.portlet.manager.portletname;

import java.io.Serializable;

import org.apache.log4j.Logger;

import org.riverock.interfaces.portal.bean.PortletName;
import org.riverock.portlet.main.AuthSessionBean;
import org.riverock.portlet.tools.FacesTools;
import org.riverock.common.utils.PortletUtils;

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

    public static final String[] ROLES = new String[]{"webmill.portal-manager"};

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
        PortletUtils.checkRights(FacesTools.getPortletRequest(), ROLES);

        Long id = FacesTools.getPortalSpiProvider().getPortalPortletNameDao().createPortletName( portletNameSessionBean.getPortletName() );
        portletNameSessionBean.setCurrentPortletNameId( id );
        loadCurrentPortletName();
        return "portlet-name";
    }

    public String cancelAddPortletName() {
        loadCurrentPortletName();
        return "portlet-name";
    }

    public String processEditPortletName() {
        PortletUtils.checkRights(FacesTools.getPortletRequest(), ROLES);

        FacesTools.getPortalSpiProvider().getPortalPortletNameDao().updatePortletName( portletNameSessionBean.getPortletName() );
        return "portlet-name";
    }

    public String cancelEditPortletName() {
        loadCurrentPortletName();
        return "portlet-name";
    }

    public String processDeletePortletName() {
        PortletUtils.checkRights(FacesTools.getPortletRequest(), ROLES);

        FacesTools.getPortalSpiProvider().getPortalPortletNameDao().deletePortletName( portletNameSessionBean.getPortletName() );
        portletNameSessionBean.setPortletName( null );
        return "portlet-name";
    }

    public String selectPortletName() {
        loadCurrentPortletName();
        return "portlet-name";
    }

    private void loadCurrentPortletName() {
        PortletName bean = FacesTools.getPortalSpiProvider().getPortalPortletNameDao().getPortletName( portletNameSessionBean.getCurrentPortletNameId() );
        portletNameSessionBean.setPortletName( new PortletNameBean(bean) );
    }
}
