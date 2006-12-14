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

import java.util.*;

import javax.portlet.PortletConfig;
import javax.portlet.PortletException;
import javax.portlet.PortletURL;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.DatatypeConfigurationException;

import org.apache.log4j.Logger;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.time.DateFormatUtils;

import org.riverock.interfaces.portal.PortalInfo;
import org.riverock.interfaces.portal.bean.News;
import org.riverock.interfaces.portal.bean.NewsGroup;
import org.riverock.interfaces.portal.dao.PortalDaoProvider;
import org.riverock.interfaces.portlet.member.ClassQueryItem;
import org.riverock.interfaces.portlet.member.PortletGetList;
import org.riverock.portlet.news.schema.NewsBlockType;
import org.riverock.portlet.news.schema.NewsItemType;
import org.riverock.portlet.news.schema.NewsGroupType;
import org.riverock.webmill.container.ContainerConstants;
import org.riverock.webmill.container.portlet.extend.PortletResultContent;
import org.riverock.webmill.container.portlet.extend.PortletResultObject;
import org.riverock.webmill.container.resource.PortletResourceBundleWithLocale;

/**
 * $Id$
 */
public final class NewsSite implements PortletGetList, PortletResultObject {
    private final static Logger log = Logger.getLogger( NewsSite.class );

    public final static String NAME_ID_NEWS_PARAM = "mill.id_news_item";
    public final static String CTX_TYPE_NEWS_BLOCK = "mill.news_block";
    public final static String NEWS_TYPE = "news.type";  // 'item' or 'block'. default is 'block'
    public final static String NEWS_TYPE_ITEM = "item";  // 'item' type

    private RenderRequest renderRequest = null;
    private RenderResponse renderResponse = null;
    private PortletConfig portletConfig = null;

    public void setParameters( final RenderRequest renderRequest, final RenderResponse renderResponse, final PortletConfig portletConfig ) {
        this.renderRequest = renderRequest;
        this.renderResponse = renderResponse;
        this.portletConfig = portletConfig;
    }

    public NewsSite() {
    }

    private void initNewsItem(NewsBlockType newsBlock) throws PortletException {

        if( newsBlock == null ) {
            return;
        }

        String nextNews = null;
        try {
            ResourceBundle bundle = portletConfig.getResourceBundle( renderRequest.getLocale() );
            if( log.isDebugEnabled() ) {
                log.debug( "resource bundle: " + bundle );
                if ( bundle instanceof PortletResourceBundleWithLocale) {
                    log.debug("bundle class name: " + ((PortletResourceBundleWithLocale)bundle).getResourceBundleClassName() );
                    log.debug("bundle locale name: " + ((PortletResourceBundleWithLocale)bundle).getLocaleName() );
                }
                else {
                    log.debug("bundle is not instanceof PortletResourceBundleWithLocale");
                }
            }


            try {
                nextNews = bundle.getString( "main.next-news" );
            }
            catch( MissingResourceException e ) {
                // catch for try create string again
            }

            if( nextNews == null ) {
                try {
                    nextNews = bundle.getString( "main.next" );
                }
                catch( MissingResourceException e ) {
                    if (log.isDebugEnabled()) {
                        int i=0;
                        Enumeration keys = bundle.getKeys();
                        while(keys.hasMoreElements()) {
                            i++;
                            String key = (String)keys.nextElement();
                            String value = bundle.getString( key );
                            log.debug("bundle key: " + key+", value: " + value);
                        }
                        if (i==0) {
                            log.debug("bundle is empty");
                        }
                    }
                    throw e;
                }
            }
        }
        catch( MissingResourceException e ) {
            log.error( "Error get localized string", e );
            nextNews = "error";
        }

        try {
            for (NewsGroupType newsGroupType : newsBlock.getNewsGroup()) {
                for (NewsItemType item : newsGroupType.getNewsItem()) {
                    item.setToFullItem( nextNews );

                    PortletURL portletUrl = renderResponse.createRenderURL();
                    portletUrl.setParameter(NEWS_TYPE, NewsSite.NEWS_TYPE_ITEM);
                    portletUrl.setParameter(NAME_ID_NEWS_PARAM, ""+item.getNewsItemId());
                    item.setUrlToFullNewsItem( portletUrl.toString() );
                }
            }
        }
        catch( Throwable e ) {
            String es = "Exception in initNewsItem( NewsBlockType newsBlock )";
            log.error( es, e );
            throw new PortletException( es, e );
        }
    }

    private NewsBlockType initNews() throws PortletException {

        PortalInfo portalInfo = ( PortalInfo ) renderRequest.getAttribute( ContainerConstants.PORTAL_INFO_ATTRIBUTE );
        Long siteLangaugeId = portalInfo.getSiteLanguageId( renderRequest.getLocale() );

        PortalDaoProvider provider = (PortalDaoProvider)renderRequest.getAttribute( ContainerConstants.PORTAL_PORTAL_DAO_PROVIDER );
        List<org.riverock.interfaces.portal.bean.NewsGroup> newsGroup = provider.getPortalCmsNewsDao().getNewsGroupList(siteLangaugeId);

        NewsBlockType nb = new NewsBlockType();
        for (NewsGroup group : newsGroup) {
            NewsGroupType newsGroupType = new NewsGroupType();
            newsGroupType.setNewsGroupName( group.getNewsGroupName() );
            newsGroupType.setNewsGroupCode( group.getNewsGroupCode() );
            newsGroupType.setMaxNews( (long)group.getCountNewsPerGroup() );
            newsGroupType.setNewsGroupId( group.getNewsGroupId() );

            List<News> newses = provider.getPortalCmsNewsDao().getNewsList(group.getNewsGroupId());
            for (News news : newses) {
                NewsItemType newsItemType = new NewsItemType();
                newsItemType.setNewsAnons(StringEscapeUtils.unescapeXml(news.getNewsAnons()));

                GregorianCalendar calendar = new GregorianCalendar();
                calendar.setTimeInMillis(news.getPostDate().getTime());
                try {
                    newsItemType.setNewsDateTime(DatatypeFactory.newInstance().newXMLGregorianCalendar(calendar));
                } catch (DatatypeConfigurationException e) {
                    log.error("Error", e);
                    throw new PortletException(e);
                }

                newsItemType.setNewsHeader(StringEscapeUtils.unescapeXml(news.getNewsHeader()));
                newsItemType.setNewsItemId(news.getNewsId());
                newsItemType.setNewsText(news.getNewsText());
                newsItemType.setNewsText(news.getNewsText());
                newsItemType.setNewsDate(DateFormatUtils.format(news.getPostDate(), "dd.MMM.yyyy", TimeZone.getDefault(), renderRequest.getLocale()));
                newsItemType.setNewsTime(DateFormatUtils.format(news.getPostDate(), "HH:mm", TimeZone.getDefault(), renderRequest.getLocale()));

                newsGroupType.getNewsItem().add(newsItemType);
            }
            nb.getNewsGroup().add(newsGroupType);
        }

        return nb;
    }

    public PortletResultContent getInstance( Long id ) throws PortletException {
        if( log.isDebugEnabled() ) log.debug( "getInstance(Long id)" );
        return getInstance();
    }

    public PortletResultContent getInstance() throws PortletException {
        NewsBlockType newsBlock = initNews();
        initNewsItem( newsBlock );
        return new NewsBlock( newsBlock );
    }

    public PortletResultContent getInstanceByCode( String newsGroupCode) throws PortletException {
        NewsBlockType newsBlock = initNews();
        for (NewsGroupType newsGroup : newsBlock.getNewsGroup()) {
            if (newsGroup.getNewsGroupCode()!=null && newsGroup.getNewsGroupCode().equals(newsGroupCode)) {
                newsBlock.getNewsGroup().add(newsGroup);
                break;
            }
        }
        initNewsItem( newsBlock );
        return new NewsBlock( newsBlock );
    }

    public List<ClassQueryItem> getList( Long idSiteCtxLangCatalog, Long idContext ) {
        return null;
    }

    public void setPortalDaoProvider(PortalDaoProvider provider) {
    }
}