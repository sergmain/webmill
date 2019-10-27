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
import org.apache.commons.lang.StringEscapeUtils;

import org.riverock.common.tools.DateTools;
import org.riverock.interfaces.portal.PortalInfo;
import org.riverock.interfaces.portal.spi.PortalSpiProvider;
import org.riverock.interfaces.portal.bean.Article;
import org.riverock.interfaces.portlet.member.ClassQueryItem;
import org.riverock.interfaces.portlet.member.PortletGetList;
import org.riverock.portlet.tools.ContentTypeTools;
import org.riverock.portlet.cms.article.bean.ArticleBean;
import org.riverock.interfaces.ContainerConstants;
import org.riverock.interfaces.portlet.PortletResultContent;
import org.riverock.interfaces.portlet.PortletResultObject;

import javax.portlet.PortletConfig;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import java.util.List;
import java.util.Date;
import java.util.TimeZone;

/**
 * Author: mill
 * Date: Jan 10, 2003
 * Time: 9:41:20 AM
 *
 * $Id: ArticleXml.java 1435 2007-09-23 17:08:57Z serg_main $
 */
@SuppressWarnings({"UnusedAssignment"})
public final class ArticleXml implements PortletResultObject, PortletGetList, PortletResultContent {
    private final static Logger log = Logger.getLogger( ArticleXml.class );

    private static final String DEFAULT_ROOT_NAME = "Article";

    private Article article;
    private TimeZone serverTimeZone=null;

    private RenderRequest renderRequest = null;
    private PortalSpiProvider provider;

    public ArticleXml() {
    }

    public void setParameters( RenderRequest renderRequest, RenderResponse renderResponse, PortletConfig portletConfig ) {
        if(log.isDebugEnabled()) {
            log.debug("renderRequest: "+renderRequest);
        }

        this.renderRequest = renderRequest;
    }

    public byte[] getXml(String rootName) throws Exception {
        if(log.isDebugEnabled()) {
            log.debug( "ArticleXml. method is 'Xml'. Root: "+rootName );
            if (article!=null) {
                log.debug( "Article date: " + article.getPostDate() );
            }
            log.debug( "renderRequest: " + renderRequest );
        }
        StringBuilder sb = new StringBuilder().
            append( "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" ).
            append( "<" ).append( rootName == null ?DEFAULT_ROOT_NAME :rootName ).append( '>');

        if (article!=null) {
            String dateText = DateTools.getStringDate(article.getPostDate(), "dd.MMM.yyyy", renderRequest.getLocale(), serverTimeZone);
            String timeText = DateTools.getStringDate(article.getPostDate(), "HH:mm", renderRequest.getLocale(), serverTimeZone);

            sb.append("<ArticleDate>" ).append( dateText ).append( "</ArticleDate>" ).
                append( "<ArticleTime>" ).append( timeText ).append( "</ArticleTime>" ).
                append( "<ArticleName>" ).
                append( article.getArticleName()!=null? StringEscapeUtils.escapeXml(article.getArticleName()):"" ).
                append( "</ArticleName>" ).
                append( "<ArticleText>" ).append( article.getArticleData()!=null?article.getArticleData():"" ).append( "</ArticleText>");

        }
        sb.append("</" ).append( rootName == null ?DEFAULT_ROOT_NAME :rootName ).append( ">" );
        if (log.isDebugEnabled()) {
            log.debug( "ArticleXml. getXml - "+sb.toString() );
        }

        return sb.toString().getBytes( ContentTypeTools.CONTENT_TYPE_UTF8 );
    }

    public byte[] getXml() throws Exception {
        if(log.isDebugEnabled()) {
            log.debug("ArticleXml. method is 'Xml'");
        }

        return getXml(DEFAULT_ROOT_NAME);
    }

    public byte[] getPlainHTML() {
        return null;
    }

    public PortletResultContent getInstance( Long articleId ) {
        PortalSpiProvider provider = (PortalSpiProvider)renderRequest.getAttribute( ContainerConstants.PORTAL_PORTAL_DAO_PROVIDER );
        Article article = provider.getPortalCmsArticleDao().getArticle(articleId);
        PortalInfo portalInfo = ( PortalInfo ) renderRequest.getAttribute( ContainerConstants.PORTAL_INFO_ATTRIBUTE );
        Long siteLangaugeId = portalInfo.getSiteLanguageId( renderRequest.getLocale() );
        if (article==null) {
            ArticleBean bean = new ArticleBean();
            bean.setArticleText("Article with Id '"+articleId+"' not found");
            bean.setCreated(new Date());
            bean.setXml(true);
            bean.setDeleted(false);
            bean.setSiteLanguageId(siteLangaugeId);
            article = bean;
        }
        this.article = article;
        this.serverTimeZone = TimeZone.getTimeZone(portalInfo.getSite().getServerTimeZone());
        return this;
    }

    public PortletResultContent getInstanceByCode( String articleCode ) throws PortletException {
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
            bean.setXml(true);
            bean.setDeleted(false);
            bean.setSiteLanguageId(siteLangaugeId);
            article = bean;
        }
        this.article = article;
        this.serverTimeZone = TimeZone.getTimeZone(portalInfo.getSite().getServerTimeZone());
        return this;
    }

    public List<ClassQueryItem> getList( Long idSiteCtxLangCatalog, Long idContext ) {
        return ArticleUtils.getListInternal(provider, idSiteCtxLangCatalog, idContext, false);
    }

    public void setPortalDaoProvider(PortalSpiProvider provider) {
        this.provider=provider;
    }
}
