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
package org.riverock.portlet.cms.news.action;

import java.io.Serializable;

import org.apache.log4j.Logger;

import org.riverock.portlet.main.AuthSessionBean;
import org.riverock.portlet.cms.news.NewsSessionBean;
import org.riverock.portlet.cms.news.NewsDataProvider;
import org.riverock.portlet.cms.news.bean.NewsGroupBean;
import org.riverock.portlet.cms.dao.CmsDaoFactory;

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
            Long newsGroupId = CmsDaoFactory.getCmsNewsDao().createNewsGroup( getSessionObject() );
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
            CmsDaoFactory.getCmsNewsDao().updateNewsGroup(getSessionObject());
            cleadDataProviderObject();
            loadCurrentObject();
        }

        return "news";
    }

    public String cancelEditNewsGroupAction() {
        log.info( "Cancel edit news group action." );

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
            CmsDaoFactory.getCmsNewsDao().deleteNewsGroup(getSessionObject().getNewsGroupId());
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
