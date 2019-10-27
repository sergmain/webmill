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
package org.riverock.portlet.cms.news.action;

import java.io.Serializable;

import org.apache.log4j.Logger;

import org.riverock.portlet.main.AuthSessionBean;
import org.riverock.portlet.cms.news.NewsSessionBean;
import org.riverock.portlet.cms.news.NewsDataProvider;

/**
 * @author Sergei Maslyukov
 *         Date: 23.08.2006
 *         Time: 15:47:16
 */
public class NewsSiteAction implements Serializable {
    private final static Logger log = Logger.getLogger( NewsSiteAction.class );
    private static final long serialVersionUID = 2057005511L;

    private NewsSessionBean newsSessionBean = null;
    private AuthSessionBean authSessionBean = null;
    private NewsDataProvider newsDataProvider = null;

    public NewsSiteAction() {
    }

    public void setNewsDataProvider(NewsDataProvider newsDataProvider) {
        this.newsDataProvider = newsDataProvider;
    }

    public void setNewsSessionBean( NewsSessionBean siteSessionBean) {
        this.newsSessionBean = siteSessionBean;
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
            log.debug( "newsSessionBean type: " +newsSessionBean.getObjectType() );
        }
        newsSessionBean.setSiteExtended( newsDataProvider.getSiteExtended() );
        return "news";
    }

    public String changeSite() {
        log.info( "Change site action." );
        return "news";
    }
}
