/*
 * org.riverock.portlet -- Portlet Library
 * 
 * Copyright (C) 2004, Riverock Software, All Rights Reserved.
 * 
 * Riverock -- The Open-source Java Development Community
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
 *
 */

/**
 *
 * $Author$
 *
 * $Id$
 *
 */
package org.riverock.portlet.news;

import java.util.List;
import java.util.ResourceBundle;

import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.PortletConfig;

import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.portlet.main.Constants;
import org.riverock.portlet.schema.portlet.news_block.NewsItemSimpleType;
import org.riverock.webmill.config.WebmillConfig;
import org.riverock.webmill.portlet.PortletGetList;
import org.riverock.webmill.portlet.PortletResultObject;
import org.riverock.webmill.portlet.PortletTools;
import org.riverock.webmill.portlet.PortletResultContent;

import org.apache.log4j.Logger;

public final class NewsItemSimple implements PortletResultObject, PortletGetList, PortletResultContent {
    private final static Logger log = Logger.getLogger( NewsItemSimple.class );

    private NewsItemSimpleType newsItem = new NewsItemSimpleType();
    private NewsItem item = null;

    private RenderRequest renderRequest = null;
    private RenderResponse renderResponse = null;
    private ResourceBundle bundle = null;

    public void setParameters( RenderRequest renderRequest, RenderResponse renderResponse, PortletConfig portletConfig ) {
        this.renderRequest = renderRequest;
        this.renderResponse = renderResponse;
        this.bundle = bundle;
    }

    protected void finalize() throws Throwable {
        item = null;
        newsItem = null;
        super.finalize();
    }

    public NewsItemSimple()
    {
    }

    public PortletResultContent getInstance(DatabaseAdapter db_)
//        , HttpServletRequest request, HttpServletResponse response,
//                     InitPage ctxInstance.page, String localePackage)
            throws PortletException
    {
        Long id__ = PortletTools.getLong( renderRequest, Constants.NAME_ID_NEWS_PARAM);
        try
        {
            item = NewsItem.getInstance(db_, id__);

            newsItem.setNewsAnons( item.newsItem.getNewsAnons() );
            newsItem.setNewsDate( item.newsItem.getNewsDate() );
            newsItem.setNewsHeader( item.newsItem.getNewsHeader() );
            newsItem.setNewsText( item.newsItem.getNewsText() );
            newsItem.setNewsTime( item.newsItem.getNewsTime() );
        }
        catch(Exception e)
        {
            log.error("Error get NewsItem object", e);
            throw (PortletException)e;
        }
        return this;
    }

    public PortletResultContent getInstance( DatabaseAdapter db__, Long id ) throws PortletException {
        return getInstance( db__ );
    }

    public PortletResultContent getInstanceByCode( DatabaseAdapter db__, String portletCode_ ) throws PortletException {
        return getInstance( db__ );
    }

    public boolean isXml(){ return true; }
    public boolean isHtml(){ return false; }

    public byte[] getPlainHTML()
    {
        return null;
    }

    public byte[] getXml()
        throws Exception
    {
        return getXml( "NewsItemSimple");
    }

    public byte[] getXml(String rootElement) throws Exception
    {
        newsItem.setNewsAnons( item.newsItem.getNewsAnons() );
        newsItem.setNewsDate( item.newsItem.getNewsDate() );
        newsItem.setNewsHeader( item.newsItem.getNewsHeader() );
        newsItem.setNewsText( item.newsItem.getNewsText() );
        newsItem.setNewsTime( item.newsItem.getNewsTime() );

        String xml = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"+
            '<'+rootElement+'>' +
            "<NewsDate>"+ newsItem.getNewsDate() + "</NewsDate>"+
            "<NewsTime>"+ newsItem.getNewsTime()+ "</NewsTime>"+
            "<NewsHeader>"+ newsItem.getNewsHeader()+ "</NewsHeader>"+
            "<NewsAnons>"+ newsItem.getNewsAnons() + "</NewsAnons>"+
            "<NewsText>"+ newsItem.getNewsText()+"</NewsText>" +
            "</"+rootElement+'>';

        if (log.isDebugEnabled())
            log.debug( "ArticleXml. getXml - "+xml );

        return xml.getBytes( WebmillConfig.getHtmlCharset() );
//        return XmlTools.getXml( newsItem, rootElement );
    }

    public List getList(Long idSiteCtxLangCatalog, Long idContext)
    {
        return null;
    }
}