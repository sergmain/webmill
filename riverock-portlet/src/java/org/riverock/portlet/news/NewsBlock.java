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
 * $Id$
 *
 */
package org.riverock.portlet.news;

import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.PortletConfig;

import org.riverock.portlet.schema.portlet.news_block.NewsBlockType;
import org.riverock.generic.tools.XmlTools;
import org.riverock.webmill.portlet.PortletResultContent;

import org.apache.log4j.Logger;

public final class NewsBlock implements PortletResultContent {
    private final static Logger log = Logger.getLogger( NewsBlock.class );

    private NewsBlockType newsBlockType = null;
//    private RenderRequest renderRequest = null;
//    private RenderResponse renderResponse = null;
//    private PortletConfig portletConfig = null;
//    private ResourceBundle resourceBundle = null;

    public void setParameters( RenderRequest renderRequest, RenderResponse renderResponse, PortletConfig portletConfig ) {
//        this.renderRequest = renderRequest;
//        this.renderResponse = renderResponse;
//        this.portletConfig = portletConfig;
//        this.resourceBundle = this.portletConfig.getResourceBundle( renderRequest.getLocale() );
    }

    public NewsBlock(NewsBlockType newsBlockType){
        this.newsBlockType = newsBlockType;
    }

    public NewsBlock(){
    }

    protected void finalize() throws Throwable
    {
        super.finalize();
    }

//    public PortletResultContent getInstance( DatabaseAdapter db_, Long id ) throws PortletException {
//        return new NewsBlock();
//    }
//
//    public PortletResultContent getInstanceByCode( DatabaseAdapter db__, String portletCode_ ) throws PortletException {
//        return new NewsBlock();
//        return null;
//    }
//
    public byte[] getPlainHTML()
    {
        return null;
/*
        String x =
                "<table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">";
        for (int i_list_news = 0; i_list_news < v.size(); i_list_news++)
        {
            NewsGroup news = (NewsGroup) v.elementAt(i_list_news);
            int count_news = news.v.size();
            if (count_news > 0)
            {
                x += "<tr><td colspan=\"2\" class=\"newshead\"><%= news.newsGroupName %></td></tr>";
                int i_news;
                Calendar curr_date = null;
                boolean isFirst = true;
                for (i_news = 0; i_news < count_news; i_news++)
                {
                    NewsItem item_news = (NewsItem) news.v.elementAt(i_news);
                    if (!DateTools.compareDate(curr_date, item_news.date))
                    {
                        curr_date = item_news.date;
                        isFirst = true;
                    }
                    if (isFirst)
                    {
                        x += "<tr><td class=\"newsdate\" valign=\"top\">";
                        x += DateTools.getStringDate(curr_date, "dd.MM.yyyy", param.renderRequest.getLocale());
                        x += "</td><td>&nbsp;</td></tr>";

                        isFirst = false;
                    }
                    x += "<tr><td class=\"newstime\" valign=\"top\">";
                    x += DateTools.getStringDate(item_news.date, "HH:mm", param.renderRequest.getLocale());
                    x += "</td><td width=\"100%\" class=\"newstitle\"><h6> ";
                    x += item_news.header;
                    x += "</h6><p>";
                    x += item_news.anons;
                    x += "&nbsp;&nbsp;&nbsp;<a class=\"newsNext\" href=\"";

//                    x += param.response.encodeURL(Constants.URI_NEWS_PAGE) + '?' + param.jspPage.addURL + Constants.NAME_ID_NEWS_PARAM + '=' + item_news.id;
                    x += param.response.encodeURL( ctxInstance.ctx()) + '?' +
                            param.jspPage.addURL + Constants.NAME_ID_NEWS_PARAM + '=' +
                            item_news.id + '&' +
                            Constants.NAME_TYPE_CONTEXT_PARAM + '=' +
                            Constants.CTX_TYPE_NEWS;

                    x += "\">";

                    try
                    {
                        x += param.CtxInstance.getStr("main.next");
                    }
                    catch (Exception e)
                    {
                        log.error("Error encoding string", e);
                        x += "<!-- error encoding string -->";
                    }
                    x += ">></a></p></td></tr>";
                }
                x += "<tr><td>&nbsp;</td></tr>";
            } // if count_news >0
        }
        x += "</table>";
        return x.getBytes();
*/
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
