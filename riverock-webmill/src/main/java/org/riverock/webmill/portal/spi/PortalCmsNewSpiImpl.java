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

import org.riverock.interfaces.portal.bean.News;
import org.riverock.interfaces.portal.bean.NewsGroup;
import org.riverock.interfaces.portal.spi.PortalCmsNewsSpi;
import org.riverock.interfaces.sso.a3.AuthSession;
import org.riverock.webmill.portal.dao.InternalDaoFactory;

/**
 * @author Sergei Maslyukov
 *         Date: 20.11.2006
 *         Time: 21:08:19
 *         <p/>
 *         $Id$
 */
public class PortalCmsNewSpiImpl implements PortalCmsNewsSpi {
    private AuthSession authSession = null;
    private ClassLoader classLoader = null;

    PortalCmsNewSpiImpl(AuthSession authSession, ClassLoader classLoader, Long siteId) {
        this.authSession = authSession;
        this.classLoader = classLoader;
    }

    public List<NewsGroup> getNewsGroupList(Long siteLanguageId) {
        ClassLoader oldLoader = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader( classLoader );
            return InternalDaoFactory.getInternalCmsDao().getNewsGroupList(siteLanguageId);
        }
        finally {
            Thread.currentThread().setContextClassLoader( oldLoader );
        }
    }

    public List<News> getNewsList(Long newsGroupId) {
        ClassLoader oldLoader = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader( classLoader );
            return InternalDaoFactory.getInternalCmsDao().getNewsList(newsGroupId);
        }
        finally {
            Thread.currentThread().setContextClassLoader( oldLoader );
        }
    }

    public NewsGroup getNewsGroup(Long newsGroupId) {
        ClassLoader oldLoader = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader( classLoader );
            return InternalDaoFactory.getInternalCmsDao().getNewsGroup(newsGroupId);
        }
        finally {
            Thread.currentThread().setContextClassLoader( oldLoader );
        }
    }

    public News getNews(Long newsId) {
        ClassLoader oldLoader = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader( classLoader );
            return InternalDaoFactory.getInternalCmsDao().getNews(newsId);
        }
        finally {
            Thread.currentThread().setContextClassLoader( oldLoader );
        }
    }

    public Long createNews(News news) {
        ClassLoader oldLoader = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader( classLoader );
            return InternalDaoFactory.getInternalCmsDao().createNews(news);
        }
        finally {
            Thread.currentThread().setContextClassLoader( oldLoader );
        }
    }

    public void updateNews(News news) {
        ClassLoader oldLoader = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader( classLoader );
            InternalDaoFactory.getInternalCmsDao().updateNews(news);
        }
        finally {
            Thread.currentThread().setContextClassLoader( oldLoader );
        }
    }

    public void deleteNews(Long newsId) {
        ClassLoader oldLoader = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader( classLoader );
            InternalDaoFactory.getInternalCmsDao().deleteNews(newsId);
        }
        finally {
            Thread.currentThread().setContextClassLoader( oldLoader );
        }
    }

    public Long createNewsGroup(NewsGroup newsGroup) {
        ClassLoader oldLoader = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader( classLoader );
            return InternalDaoFactory.getInternalCmsDao().createNewsGroup(newsGroup);
        }
        finally {
            Thread.currentThread().setContextClassLoader( oldLoader );
        }
    }

    public void deleteNewsGroup(Long newsGroupId) {
        ClassLoader oldLoader = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader( classLoader );
            InternalDaoFactory.getInternalCmsDao().deleteNewsGroup(newsGroupId);
        }
        finally {
            Thread.currentThread().setContextClassLoader( oldLoader );
        }
    }

    public void updateNewsGroup(NewsGroup newsGroup) {
        ClassLoader oldLoader = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader( classLoader );
            InternalDaoFactory.getInternalCmsDao().updateNewsGroup(newsGroup);
        }
        finally {
            Thread.currentThread().setContextClassLoader( oldLoader );
        }
    }
}
