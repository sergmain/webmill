/*
 * org.riverock.webmill - Webmill portal with support jsr-168, xml/xslt and others things.
 * For more information, please visit project site http://webmill.riverock.org
 *
 * Copyright (C) 2000-2006, Riverock Software, All Rights Reserved.
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
package org.riverock.webmill.portal.spi;

import java.util.List;

import org.riverock.interfaces.portal.bean.Article;
import org.riverock.interfaces.portal.spi.PortalCmsArticleSpi;
import org.riverock.interfaces.sso.a3.AuthSession;
import org.riverock.webmill.portal.bean.ArticleBean;
import org.riverock.webmill.portal.dao.InternalDaoFactory;

/**
 * @author Sergei Maslyukov
 *         Date: 20.11.2006
 *         Time: 21:03:25
 *         <p/>
 *         $Id$
 */
public class PortalCmsArticleSpiImpl implements PortalCmsArticleSpi {
    private AuthSession authSession = null;
    private ClassLoader classLoader = null;

    PortalCmsArticleSpiImpl(AuthSession authSession, ClassLoader classLoader, Long siteId) {
        this.authSession = authSession;
        this.classLoader = classLoader;
    }

    public List<Article> getArticleList(Long siteLanguageId, boolean isXml) {
        ClassLoader oldLoader = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader( classLoader );
            return InternalDaoFactory.getInternalCmsDao().getArticleList(siteLanguageId, isXml);
        }
        finally {
            Thread.currentThread().setContextClassLoader( oldLoader );
        }
    }

    public Article getArticle(Long articleId) {
        ClassLoader oldLoader = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader( classLoader );
            return InternalDaoFactory.getInternalCmsDao().getArticle(articleId);
        }
        finally {
            Thread.currentThread().setContextClassLoader( oldLoader );
        }
    }

    public Article getArticleByCode(Long siteLanguageId, String articleCode) {
        ClassLoader oldLoader = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader( classLoader );
            return InternalDaoFactory.getInternalCmsDao().getArticleByCode(siteLanguageId, articleCode);
        }
        finally {
            Thread.currentThread().setContextClassLoader( oldLoader );
        }
    }

    public Long createArticle(Article article) {
        ClassLoader oldLoader = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader( classLoader );
            ArticleBean articleBean = new ArticleBean(article);
            articleBean.setUserId(authSession.getUser().getUserId());
            return InternalDaoFactory.getInternalCmsDao().createArticle(articleBean);
        }
        finally {
            Thread.currentThread().setContextClassLoader( oldLoader );
        }
    }

    public void updateArticle(Article article) {
        ClassLoader oldLoader = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader( classLoader );
            InternalDaoFactory.getInternalCmsDao().updateArticle(article);
        }
        finally {
            Thread.currentThread().setContextClassLoader( oldLoader );
        }
    }

    public void deleteArticle(Long articleId) {
        ClassLoader oldLoader = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader( classLoader );
            InternalDaoFactory.getInternalCmsDao().deleteArticle(articleId);
        }
        finally {
            Thread.currentThread().setContextClassLoader( oldLoader );
        }
    }
}
