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

import java.util.Date;

import javax.portlet.PortletConfig;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;

import org.riverock.generic.tools.XmlTools;
import org.riverock.portlet.schema.portlet.news_block.NewsBlockType;
import org.riverock.portlet.schema.portlet.news_block.NewsGroupType;
import org.riverock.portlet.schema.portlet.news_block.NewsItemType;
import org.riverock.portlet.tools.ContentTypeTools;
import org.riverock.webmill.container.portlet.extend.PortletResultContent;

/**
 *
 * $Id$
 *
 */
public final class NewsBlock implements PortletResultContent {
    private final static Logger log = Logger.getLogger( NewsBlock.class );

    private NewsBlockType newsBlockType = null;

    public void setParameters( RenderRequest renderRequest, RenderResponse renderResponse, PortletConfig portletConfig ) {
    }

    public NewsBlock(NewsBlockType newsBlockType){
        this.newsBlockType = newsBlockType;
    }

    public NewsBlock(){
    }

    protected void finalize() throws Throwable {
        super.finalize();
    }

    public byte[] getPlainHTML() throws Exception {
        StringBuilder s =
                new StringBuilder( "\n<table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">\n");

        for (int i = 0; i < newsBlockType.getNewsGroupCount(); i++)
        {
            NewsGroupType newsGroup = newsBlockType.getNewsGroup(i);
            if (newsGroup.getNewsItemCount() > 0)
            {
                s.
                    append( "<tr><td colspan=\"2\" class=\"newshead\">" ).
                    append( newsGroup.getNewsGroupName() ).
                    append( "</td></tr>\n" );

                Date currentDate = null;
                boolean isFirst = true;
                for (int i_news = 0; i_news < newsGroup.getNewsItemCount(); i_news++)
                {
                    NewsItemType newsItem = newsGroup.getNewsItem(i_news);
                    if (currentDate==null || currentDate.getTime()!= newsItem.getNewsDateTime().getTime())
                    {
                        currentDate = newsItem.getNewsDateTime();
                        isFirst = true;
                    }
                    if (isFirst)
                    {

                            s.append( "<tr><td class=\"newsdate\" valign=\"top\">" ).append( newsItem.getNewsDate() ).append( "</td><td>&nbsp;</td></tr>\n" );

                        isFirst = false;
                    }
                    s.
                        append( "<tr>\n" ).
                        append( "<td class=\"newstime\" valign=\"top\">\n" ).
                        append( newsItem.getNewsTime() ).
                        append( "</td>\n" ).
                        append( "<td width=\"100%\" class=\"newstitle\">\n<h6> " ).
                        append( newsItem.getNewsHeader() ).
                        append( "</h6>\n<p>" ).
                        append( newsItem.getNewsAnons() ).
                        append( "&nbsp;&nbsp;&nbsp;<a class=\"newsNext\" href=\"" ).
                        append( newsItem.getUrlToFullNewsItem() ).
                        append( "\">" ).
                        append( newsItem.getToFullItem() ).
                        append( "></a></p>\n</td></tr>\n" );
                }
                s.append( "<tr><td>&nbsp;</td></tr>\n" );
            }
        }
        s.append( "</table>\n" );
        return s.toString().getBytes( ContentTypeTools.CONTENT_TYPE_UTF8 );
    }

    public byte[] getXml(String rootElement) throws Exception
    {
        if (log.isDebugEnabled())
            log.debug("xml string instance - "+XmlTools.getXml( newsBlockType, rootElement ));

        return XmlTools.getXml( newsBlockType, rootElement );
    }

    public byte[] getXml()
        throws Exception
    {
        return getXml("NewsBlock");
    }
}
