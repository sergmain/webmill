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
package org.riverock.portlet.news;

import java.util.TimeZone;

import javax.portlet.PortletConfig;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.log4j.Logger;

import org.riverock.common.portlet.PortletMetadataService;
import org.riverock.common.utils.PortletUtils;
import org.riverock.interfaces.ContainerConstants;
import org.riverock.interfaces.portal.PortalInfo;
import org.riverock.interfaces.portal.bean.News;
import org.riverock.interfaces.portal.spi.PortalSpiProvider;
import org.riverock.interfaces.portlet.PortletResultContent;
import org.riverock.interfaces.portlet.PortletResultObject;
import org.riverock.portlet.tools.ContentTypeTools;

/**
 *
 * $Author: serg_main $
 *
 * $Id: NewsItemSimple.java 1476 2007-10-14 13:18:52Z serg_main $
 *
 */
public final class NewsItemSimple implements PortletResultObject, PortletResultContent {
    private final static Logger log = Logger.getLogger( NewsItemSimple.class );

    private News news;
    private TimeZone serverTimeZone=null;

    private RenderRequest renderRequest = null;

    public void setParameters( RenderRequest renderRequest, RenderResponse renderResponse, PortletConfig portletConfig ) {
        this.renderRequest = renderRequest;
    }

    public NewsItemSimple() {
    }

    public NewsItemSimple getNews() {
        Long newsId = PortletUtils.getLong( renderRequest, NewsSite.NAME_ID_NEWS_PARAM);
        PortalSpiProvider provider = (PortalSpiProvider)renderRequest.getAttribute( ContainerConstants.PORTAL_PORTAL_DAO_PROVIDER );
        PortalInfo portalInfo = ( PortalInfo ) renderRequest.getAttribute( ContainerConstants.PORTAL_INFO_ATTRIBUTE );
        this.news = provider.getPortalCmsNewsDao().getNews(newsId);
        this.serverTimeZone = TimeZone.getTimeZone(portalInfo.getSite().getServerTimeZone());
        return this;
    }

    public PortletResultContent getInstance( Long id ) {
        return getNews();
    }

    public PortletResultContent getInstanceByCode( String newsCode ) {
        return getNews();
    }

    public byte[] getPlainHTML() throws Exception {

        String newsDate = DateFormatUtils.format(news.getPostDate(), "dd.MMM.yyyy", serverTimeZone, renderRequest.getLocale());
        String newsTime = DateFormatUtils.format(news.getPostDate(), "HH:mm", serverTimeZone, renderRequest.getLocale());

        StringBuilder s = new StringBuilder().
            append( "\n<table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">\n").
            append( "<tr><td colspan=\"2\" class=\"newshead\">" ).
            append( news.getNewsHeader() ).
            append( "</td></tr>\n" ).
            append( "<tr><td class=\"newsdate\" valign=\"top\">" ).append( newsDate ).append( "</td><td>&nbsp;</td></tr>\n" ).
            append( "<tr>\n" ).
            append( "<td class=\"newstime\" valign=\"top\">\n" ).
            append( newsTime ).
            append( "</td>\n" ).
            append( "<td width=\"100%\" class=\"newstitle\">\n<h6> " ).
            append( news.getNewsAnons() ).
            append( "</h6>\n" ).
            append( news.getNewsText() ).
            append( "\n</td></tr>\n" ).
            append( "</table>\n" );

        return s.toString().getBytes( ContentTypeTools.CONTENT_TYPE_UTF8 );
    }

    public byte[] getXml() throws Exception {
        return getXml( "NewsItemSimple");
    }

    public byte[] getXml( final String rootElement) throws Exception {

        String newsDate = DateFormatUtils.format(news.getPostDate(), "dd.MMM.yyyy", serverTimeZone, renderRequest.getLocale());
        String newsTime = DateFormatUtils.format(news.getPostDate(), "HH:mm", serverTimeZone, renderRequest.getLocale());

        String root = PortletMetadataService.getMetadata( renderRequest, "xml-root-name", rootElement );

        String xml = new StringBuilder().
            append( "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" ).
            append( '<' ).append( root ).append( '>' ).
            append( "<NewsDate>" ).append( newsDate ).append( "</NewsDate>" ).
            append( "<NewsTime>" ).append( newsTime ).append( "</NewsTime>" ).
            append( "<NewsHeader>" ).append( news.getNewsHeader()!=null? news.getNewsHeader():"" ).append( "</NewsHeader>" ).
            append( "<NewsAnons>" ).append( news.getNewsAnons()!=null? news.getNewsAnons():"" ).append( "</NewsAnons>" ).
            append( "<NewsText>" ).append( news.getNewsText()!=null? news.getNewsText():"" ).append( "</NewsText>" ).
            append( "</" ).append( root ).append( '>' ).toString();

        if (log.isDebugEnabled()) {
            log.debug( "NewsXml. getXml: "+xml );
        }

        return xml.getBytes( ContentTypeTools.CONTENT_TYPE_UTF8 );
    }
}