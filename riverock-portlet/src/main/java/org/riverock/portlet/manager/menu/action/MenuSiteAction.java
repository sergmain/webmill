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
package org.riverock.portlet.manager.menu.action;

import java.io.Serializable;

import org.apache.log4j.Logger;

import org.riverock.portlet.main.AuthSessionBean;
import org.riverock.portlet.manager.menu.MenuDataProvider;
import org.riverock.portlet.manager.menu.MenuSessionBean;

/**
 * @author Sergei Maslyukov
 * 16.05.2006
 * 20:14:59
 *
 *
 */
public class MenuSiteAction implements Serializable {
    private final static Logger log = Logger.getLogger( MenuSiteAction.class );
    private static final long serialVersionUID = 2057005511L;

    private MenuSessionBean menuSessionBean = null;
    private AuthSessionBean authSessionBean = null;
    private MenuDataProvider dataProvider = null;

    public MenuSiteAction() {
    }

    public void setDataProvider(MenuDataProvider dataProvider) {
        this.dataProvider = dataProvider;
    }

    public void setMenuSessionBean( MenuSessionBean siteSessionBean) {
        this.menuSessionBean = siteSessionBean;
    }

    public AuthSessionBean getAuthSessionBean() {
        return authSessionBean;
    }

    public void setAuthSessionBean( AuthSessionBean authSessionBean) {
        this.authSessionBean = authSessionBean;
    }

    public String selectSite() {
        if (log.isDebugEnabled()) {
            log.debug( "Select site action." );
            log.debug( "menuSessionBean type: " +menuSessionBean.getObjectType() );
        }
        menuSessionBean.setSiteExtended( dataProvider.getSiteExtended() );
        return "menu";
    }

    public String changeSite() {
        log.info( "Change site action." );
        return "menu";
    }
}
