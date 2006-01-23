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
package org.riverock.portlet.news;

import javax.portlet.PortletConfig;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.db.DatabaseManager;
import org.riverock.portlet.schema.portlet.news_block.NewsItemSimpleType;
import org.riverock.portlet.tools.ContentTypeTools;
import org.riverock.webmill.container.portlet.extend.PortletResultObject;
import org.riverock.webmill.container.portlet.extend.PortletResultContent;
import org.riverock.webmill.container.tools.PortletService;
import org.riverock.webmill.container.tools.PortletMetadataService;

/**
 *
 * $Author$
 *
 * $Id$
 *
 */
public final class NewsItemSimple implements PortletResultObject, PortletResultContent {
    private final static Log log = LogFactory.getLog( NewsItemSimple.class );

    private NewsItemSimpleType newsItem = new NewsItemSimpleType();

    private RenderRequest renderRequest = null;

    public void setParameters( RenderRequest renderRequest, RenderResponse renderResponse, PortletConfig portletConfig ) {
        this.renderRequest = renderRequest;
    }

    protected void finalize() throws Throwable {
        newsItem = null;
        super.finalize();
    }

    public NewsItemSimple()
    {
    }

    public PortletResultContent getInstance() throws PortletException
    {
        Long id__ = PortletService.getLong( renderRequest, NewsSite.NAME_ID_NEWS_PARAM);
        DatabaseAdapter db_ = null;
        try {
            db_ = DatabaseAdapter.getInstance();
            NewsItem item = NewsItem.getInstance(db_, id__);

            newsItem.setNewsAnons( item.newsItem.getNewsAnons() );
            newsItem.setNewsDate( item.newsItem.getNewsDate() );
            newsItem.setNewsHeader( item.newsItem.getNewsHeader() );
            newsItem.setNewsText( item.newsItem.getNewsText() );
            newsItem.setNewsTime( item.newsItem.getNewsTime() );
        }
        catch(Exception e)
        {
            final String es = "Error get NewsItem object";
            log.error(es, e);
            throw new PortletException( es, e );
        }
        finally {
            DatabaseManager.close(db_);
            db_ = null;
        }
        return this;
    }

    public PortletResultContent getInstance( Long id ) throws PortletException {
        return getInstance();
    }

    public PortletResultContent getInstanceByCode( String portletCode_ ) throws PortletException {
        return getInstance();
    }

    public byte[] getPlainHTML() throws Exception {

        StringBuilder s = new StringBuilder().
            append( "\n<table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">\n").
            append( "<tr><td colspan=\"2\" class=\"newshead\">" ).
            append( newsItem.getNewsHeader() ).
            append( "</td></tr>\n" ).
            append( "<tr><td class=\"newsdate\" valign=\"top\">" ).append( newsItem.getNewsDate() ).append( "</td><td>&nbsp;</td></tr>\n" ).
            append( "<tr>\n" ).
            append( "<td class=\"newstime\" valign=\"top\">\n" ).
            append( newsItem.getNewsTime() ).
            append( "</td>\n" ).
            append( "<td width=\"100%\" class=\"newstitle\">\n<h6> " ).
            append( newsItem.getNewsAnons() ).
            append( "</h6>\n" ).
            append( newsItem.getNewsText() ).
            append( "\n</td></tr>\n" ).
            append( "</table>\n" );

        return s.toString().getBytes( ContentTypeTools.CONTENT_TYPE_UTF8 );
    }

    public byte[] getXml()
        throws Exception
    {
        return getXml( "NewsItemSimple");
    }

    public byte[] getXml( final String rootElement) throws Exception {

        String root = PortletMetadataService.getMetadata( renderRequest, "xml-root-name", rootElement );

        String xml = new StringBuilder().
            append( "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" ).
            append( '<' ).append( root ).append( '>' ).
            append( "<NewsDate>" ).append( newsItem.getNewsDate() ).append( "</NewsDate>" ).
            append( "<NewsTime>" ).append( newsItem.getNewsTime() ).append( "</NewsTime>" ).
            append( "<NewsHeader>" ).append( newsItem.getNewsHeader()!=null?newsItem.getNewsHeader():"" ).append( "</NewsHeader>" ).
            append( "<NewsAnons>" ).append( newsItem.getNewsAnons()!=null?newsItem.getNewsAnons():"" ).append( "</NewsAnons>" ).
            append( "<NewsText>" ).append( newsItem.getNewsText()!=null?newsItem.getNewsText():"" ).append( "</NewsText>" ).
            append( "</" ).append( root ).append( '>' ).toString();

        if (log.isDebugEnabled()) {
            log.debug( "NewsXml. getXml: "+xml );
        }

        return xml.getBytes( ContentTypeTools.CONTENT_TYPE_UTF8 );
    }
}