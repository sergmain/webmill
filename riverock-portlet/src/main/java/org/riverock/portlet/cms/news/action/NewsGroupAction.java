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
import org.riverock.portlet.cms.news.bean.NewsGroupBean;
import org.riverock.portlet.tools.FacesTools;

/**
 * @author Sergei Maslyukov
 *         Date: 23.08.2006
 *         Time: 15:21:09
 */
public class NewsGroupAction implements Serializable {
    private final static Logger log = Logger.getLogger( NewsGroupAction.class );
    private static final long serialVersionUID = 2057005511L;

    private NewsSessionBean newsSessionBean = null;
    private AuthSessionBean authSessionBean = null;
    private NewsDataProvider newsDataProvider = null;

    public NewsGroupAction() {
    }

    public void setNewsDataProvider(NewsDataProvider newsDataProvider) {
        this.newsDataProvider = newsDataProvider;
    }

    // getter/setter methods

    public void setNewsSessionBean(NewsSessionBean newsSessionBean) {
        this.newsSessionBean = newsSessionBean;
    }

    public AuthSessionBean getAuthSessionBean() {
        return authSessionBean;
    }

    public void setAuthSessionBean( AuthSessionBean authSessionBean) {
        this.authSessionBean = authSessionBean;
    }

// main select action
    public String selectNewsGroup() {
        log.info( "Select news group action." );
        loadCurrentObject();

        return "news";
    }

    // Add actions
    public String addNewsGroupAction() {
        log.info( "Add news group action." );

        NewsGroupBean newsGroupBean = new NewsGroupBean();
        newsGroupBean.setSiteLanguageId(newsSessionBean.getId());
        setSessionObject(newsGroupBean);

        return "news-group-add";
    }

    public String processAddNewsGroupAction() {
        log.info( "Procss add news group action." );

        if( getSessionObject() !=null ) {
            Long newsGroupId = FacesTools.getPortalSpiProvider().getPortalCmsNewsDao().createNewsGroup( getSessionObject() );
            setSessionObject(null);
            newsSessionBean.setId(newsGroupId);
            cleadDataProviderObject();
            loadCurrentObject();
        }

        return "news";
    }

    public String cancelAddNewsGroupAction() {
        log.info( "Cancel add news group action." );

        setSessionObject(null);
        cleadDataProviderObject();

        return "news";
    }

// Edit actions
    public String editNewsGroupAction() {
        log.info( "Edit news group action." );

        return "news-group-edit";
    }

    public String processEditNewsGroupAction() {
        log.info( "Save changes news group action." );

        if( getSessionObject() !=null ) {
            FacesTools.getPortalSpiProvider().getPortalCmsNewsDao().updateNewsGroup(getSessionObject());
            cleadDataProviderObject();
            loadCurrentObject();
        }

        return "news";
    }

    public String cancelEditNewsGroupAction() {
        log.info( "Cancel edit news group action." );
        loadCurrentObject();
        return "news";
    }

// Delete actions
    public String deleteNewsGroupAction() {
        log.info( "delete news group action." );

        setSessionObject( new NewsGroupBean(newsDataProvider.getNewsGroup()) );

        return "news-group-delete";
    }

    public String cancelDeleteNewsGroupAction() {
        log.info( "Cancel delete news group action." );

        return "news";
    }

    public String processDeleteNewsGroupAction() {
        log.info( "Process delete news group action. getSessionObject().getCatalogLanguageId(): " +getSessionObject().getNewsGroupId() );

        if( getSessionObject() != null ) {
            FacesTools.getPortalSpiProvider().getPortalCmsNewsDao().deleteNewsGroup(getSessionObject().getNewsGroupId());
            setSessionObject(null);
            newsSessionBean.setId(null);
            newsSessionBean.setObjectType(NewsSessionBean.UNKNOWN_TYPE);
            cleadDataProviderObject();
        }

        return "news";
    }

    private void setSessionObject(NewsGroupBean bean) {
        newsSessionBean.setNewsGroup( bean );
    }

    private void loadCurrentObject() {
        newsSessionBean.setNewsGroup( new NewsGroupBean(newsDataProvider.getNewsGroup()) );
    }

    private void cleadDataProviderObject() {
        newsDataProvider.clearNewsGroup();
    }

    private NewsGroupBean getSessionObject() {
        return newsSessionBean.getNewsGroup();
    }

}
