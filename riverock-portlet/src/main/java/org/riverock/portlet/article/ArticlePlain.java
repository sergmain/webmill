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

import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import javax.portlet.PortletConfig;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import org.riverock.common.tools.DateTools;
import org.riverock.interfaces.ContainerConstants;
import org.riverock.interfaces.portal.PortalInfo;
import org.riverock.interfaces.portal.bean.Article;
import org.riverock.interfaces.portal.spi.PortalSpiProvider;
import org.riverock.interfaces.portlet.PortletResultContent;
import org.riverock.interfaces.portlet.PortletResultObject;
import org.riverock.interfaces.portlet.member.ClassQueryItem;
import org.riverock.interfaces.portlet.member.PortletGetList;
import org.riverock.portlet.cms.article.bean.ArticleBean;
import org.riverock.portlet.tools.ContentTypeTools;

/**
 * Author: mill
 * Date: Jan 10, 2003
 * Time: 9:45:31 AM
 *
 * $Id: ArticlePlain.java 1400 2007-09-04 20:25:29Z serg_main $
 */
@SuppressWarnings({"UnusedAssignment"})
public final class ArticlePlain implements PortletResultObject, PortletGetList, PortletResultContent {
    private final static Logger log = Logger.getLogger( ArticlePlain.class );

    public Article article;
    private RenderRequest renderRequest = null;
    private TimeZone serverTimeZone=null;

    public ArticlePlain() {
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
        if( log.isDebugEnabled() ) {
            log.debug( "Article plain. method is 'Plain'" );
        }

        if (article==null || article.getArticleData()==null) {
            return new byte[]{};
        }

        return
            StringUtils.replace( article.getArticleData(), "\n", "<br>\n" )
            .getBytes( ContentTypeTools.CONTENT_TYPE_UTF8 );
    }

    public boolean isXml() {
        return false;
    }

    public boolean isHtml() {
        return true;
    }

    public String getArticleDate() {
        return DateTools.getStringDate( article.getPostDate(), "dd.MMM.yyyy", renderRequest.getLocale(), serverTimeZone);
    }

    public String getArticleTime() {
        return DateTools.getStringDate( article.getPostDate(), "HH:mm", renderRequest.getLocale(), serverTimeZone );
    }

    public String getArticleName() {
        return article.getArticleName();
    }

    public String getArticleText() {
        if( log.isDebugEnabled() )
            log.debug( "Article text - " + article.getArticleData() );

        return article.getArticleData();
    }

/*
    public String getArticleText() {
        if( log.isDebugEnabled() )
            log.debug( "Article text - " + text );

        String s = text;
        if( isTranslateCR )
            s = StringTools.prepareToParsingSimple( s );

        if( isSimpleTextBlock ) {
            if( log.isDebugEnabled() )
                log.debug( "isSimpleTextBlock - " + isSimpleTextBlock );

            s = StringEscapeUtils.unescapeXml( s );
        }

        return s;
    }
*/

    public PortletResultContent getInstance( Long articleId ) throws PortletException {
        PortalSpiProvider provider = (PortalSpiProvider)renderRequest.getAttribute( ContainerConstants.PORTAL_PORTAL_DAO_PROVIDER );
        Article article = provider.getPortalCmsArticleDao().getArticle(articleId);
        PortalInfo portalInfo = ( PortalInfo ) renderRequest.getAttribute( ContainerConstants.PORTAL_INFO_ATTRIBUTE );
        this.article = article;
        this.serverTimeZone = TimeZone.getTimeZone(portalInfo.getSite().getServerTimeZone());
        return this;
    }

    public PortletResultContent getInstanceByCode( String articleCode ) {
        if( log.isDebugEnabled() ) {
            log.debug( "#10.01.01 " + articleCode );
        }

        PortalInfo portalInfo = ( PortalInfo ) renderRequest.getAttribute( ContainerConstants.PORTAL_INFO_ATTRIBUTE );
        Long siteLangaugeId = portalInfo.getSiteLanguageId( renderRequest.getLocale() );

        PortalSpiProvider provider = (PortalSpiProvider)renderRequest.getAttribute( ContainerConstants.PORTAL_PORTAL_DAO_PROVIDER );
        Article article = provider.getPortalCmsArticleDao().getArticleByCode(siteLangaugeId, articleCode);
        if (article==null) {
            ArticleBean bean = new ArticleBean();
            bean.setArticleText("Article with code '"+articleCode+"' not found");
            bean.setCreated(new Date());
            bean.setXml(false);
            bean.setDeleted(false);
            bean.setSiteLanguageId(siteLangaugeId);
            article = bean; 
        }
        this.article = article;
        this.serverTimeZone = TimeZone.getTimeZone(portalInfo.getSite().getServerTimeZone());
        return this;
    }

    public List<ClassQueryItem> getList( Long idSiteCtxLangCatalog, Long idContext ) {

        return ArticleUtils.getListInternal(provider, idSiteCtxLangCatalog, idContext, true);
    }

    private PortalSpiProvider provider;
    public void setPortalDaoProvider(PortalSpiProvider provider) {
        this.provider=provider;
    }

}
