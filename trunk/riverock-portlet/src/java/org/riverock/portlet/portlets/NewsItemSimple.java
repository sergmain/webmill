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

package org.riverock.portlet.portlets;



import java.util.List;



import org.riverock.generic.db.DatabaseAdapter;

import org.riverock.portlet.main.Constants;

import org.riverock.portlet.schema.portlet.news_block.NewsItemSimpleType;

import org.riverock.webmill.config.WebmillConfig;

import org.riverock.webmill.portlet.Portlet;

import org.riverock.webmill.portlet.PortletGetList;

import org.riverock.webmill.portlet.PortletParameter;

import org.riverock.webmill.portlet.PortletResultObject;

import org.riverock.webmill.portlet.PortletTools;



import org.apache.log4j.Logger;



public class NewsItemSimple implements Portlet, PortletResultObject, PortletGetList

{

    private static Logger log = Logger.getLogger( NewsItemSimple.class );



    private NewsItemSimpleType newsItem = new NewsItemSimpleType();

    private NewsItem item = null;

    private PortletParameter param = null;



    public void setParameter(PortletParameter param_)

    {

        this.param = param_;

    }



    protected void finalize() throws Throwable

    {

        item = null;

//        newsItem = null;



        super.finalize();

    }



    public NewsItemSimple()

    {

    }



    public PortletResultObject getInstance(DatabaseAdapter db_)

//        , HttpServletRequest request, HttpServletResponse response,

//                     InitPage ctxInstance.page, String localePackage)

            throws PortletException

    {

        Long id__ = PortletTools.getLong( param.getPortletRequest(), Constants.NAME_ID_NEWS_PARAM);

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



    public PortletResultObject getInstance(DatabaseAdapter db__, Long id) throws Exception

    {

        return getInstance( db__ );

    }



    public PortletResultObject getInstanceByCode(DatabaseAdapter db__, String portletCode_) throws Exception

    {

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