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
package org.riverock.portlet.cms.article.action;

import java.io.Serializable;

import org.apache.log4j.Logger;

import org.riverock.portlet.main.AuthSessionBean;
import org.riverock.portlet.cms.article.ArticleSessionBean;
import org.riverock.portlet.cms.article.ArticleDataProvider;

/**
 * @author Sergei Maslyukov
 *         Date: 25.08.2006
 *         Time: 21:05:40
 */
public class ArticleSiteAction  implements Serializable {
    private final static Logger log = Logger.getLogger( ArticleSiteAction.class );
    private static final long serialVersionUID = 2057005511L;

    private ArticleSessionBean articleSessionBean = null;
    private AuthSessionBean authSessionBean = null;
    private ArticleDataProvider articleDataProvider = null;

    public ArticleSiteAction() {
    }

    public void setArticleSessionBean(ArticleSessionBean articleSessionBean) {
        this.articleSessionBean = articleSessionBean;
    }

    public void setArticleDataProvider(ArticleDataProvider articleDataProvider) {
        this.articleDataProvider = articleDataProvider;
    }

    public AuthSessionBean getAuthSessionBean() {
        return authSessionBean;
    }

    public void setAuthSessionBean(AuthSessionBean authSessionBean) {
        this.authSessionBean = authSessionBean;
    }

    public String selectSite() {
        if (log.isDebugEnabled()) {
            log.debug( "Select site action." );
            log.debug( "articleSessionBean type: " +articleSessionBean.getObjectType() );
        }
        articleSessionBean.setSiteExtended( articleDataProvider.getSiteExtended() );
        return "article";
    }

    public String changeSite() {
        log.info( "Change site action." );
        return "article";
    }
}
