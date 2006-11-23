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
package org.riverock.portlet.article;

import org.apache.log4j.Logger;
import org.riverock.common.tools.DateTools;
import org.riverock.generic.config.GenericConfig;
import org.riverock.interfaces.portal.PortalInfo;
import org.riverock.interfaces.portal.bean.Article;
import org.riverock.interfaces.portal.dao.PortalDaoProvider;
import org.riverock.interfaces.portlet.member.ClassQueryItem;
import org.riverock.interfaces.portlet.member.PortletGetList;
import org.riverock.portlet.tools.ContentTypeTools;
import org.riverock.webmill.container.ContainerConstants;
import org.riverock.webmill.container.portlet.extend.PortletResultContent;
import org.riverock.webmill.container.portlet.extend.PortletResultObject;

import javax.portlet.PortletConfig;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import java.util.List;

/**
 * Author: mill
 * Date: Jan 10, 2003
 * Time: 9:45:31 AM
 *
 * $Id$
 */
@SuppressWarnings({"UnusedAssignment"})
public final class ArticlePlain implements PortletResultObject, PortletGetList, PortletResultContent {
    private final static Logger log = Logger.getLogger( ArticlePlain.class );

    public Article article;
    private RenderRequest renderRequest = null;

    public ArticlePlain() {
    }

    private ArticlePlain( Article article ) {
        this.article = article;
    }

    public void setParameters( RenderRequest renderRequest, RenderResponse renderResponse, PortletConfig portletConfig ) {
        this.renderRequest = renderRequest;
    }

    public byte[] getXml( String name ) {
        return null;
    }

    public byte[] getXml() {
        return null;
    }

    public byte[] getPlainHTML()
        throws Exception {
        if( log.isDebugEnabled() )
            log.debug( "Article plain. method is 'Plain'" );

        return  article.getArticleData().getBytes( ContentTypeTools.CONTENT_TYPE_UTF8 );
    }

    public boolean isXml() {
        return false;
    }

    public boolean isHtml() {
        return true;
    }

    public String getArticleDate() {
        return DateTools.getStringDate( article.getPostDate(), "dd.MMM.yyyy", renderRequest.getLocale(), GenericConfig.getTZ() );
    }

    public String getArticleTime() {
        return DateTools.getStringDate( article.getPostDate(), "HH:mm", renderRequest.getLocale(), GenericConfig.getTZ() );
    }

    public String getArticleName() {
        return article.getArticleName();
    }

    public String getArticleText() {
        if( log.isDebugEnabled() )
            log.debug( "Article text - " + article.getArticleData() );

        return article.getArticleData();
    }

    public PortletResultContent getInstance( Long articleId ) throws PortletException {
        PortalDaoProvider provider = (PortalDaoProvider)renderRequest.getAttribute( ContainerConstants.PORTAL_PORTAL_DAO_PROVIDER );
        Article article = provider.getPortalCmsArticleDao().getArticle(articleId);
        return new ArticlePlain(article);
    }

    public PortletResultContent getInstanceByCode( String articleCode ) {
        if( log.isDebugEnabled() ) {
            log.debug( "#10.01.01 " + articleCode );
        }

        PortalInfo portalInfo = ( PortalInfo ) renderRequest.getAttribute( ContainerConstants.PORTAL_INFO_ATTRIBUTE );
        Long siteLangaugeId = portalInfo.getSiteLanguageId( renderRequest.getLocale() );

        PortalDaoProvider provider = (PortalDaoProvider)renderRequest.getAttribute( ContainerConstants.PORTAL_PORTAL_DAO_PROVIDER );
        Article article = provider.getPortalCmsArticleDao().getArticleByCode(siteLangaugeId, articleCode);
        return new ArticlePlain(article);
    }

    public List<ClassQueryItem> getList( Long idSiteCtxLangCatalog, Long idContext ) {

        return ArticleUtils.getListInternal(provider, idSiteCtxLangCatalog, idContext, true);
    }

    private PortalDaoProvider provider;
    public void setPortalDaoProvider(PortalDaoProvider provider) {
        this.provider=provider;
    }

}
