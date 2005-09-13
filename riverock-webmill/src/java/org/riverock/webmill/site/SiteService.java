/*
 * org.riverock.webmill -- Portal framework implementation
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
package org.riverock.webmill.site;

import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.webmill.core.*;
import org.riverock.webmill.schema.core.*;


/**
 * Author: mill
 * Date: Mar 18, 2003
 * Time: 11:09:59 AM
 *
 * $Id$
 */
public final class SiteService {
    public SiteService() {
    }

    public static void dropSiteLanguageData( DatabaseAdapter db_, Long idSiteLanguageForDrop )
        throws Exception
    {
        System.out.println("drop news");
        MainListNewsListType newsFullList =
            new GetMainListNewsWithIdSiteSupportLanguageList(db_, idSiteLanguageForDrop ).item;
        for (int i=0; i<newsFullList.getMainListNewsCount(); i++)
        {
            MainListNewsItemType newsGroupList = newsFullList.getMainListNews(i);
            MainNewsListType newsList = new GetMainNewsWithIdNewsList(db_, newsGroupList.getIdNews()).item;
            for (int j=0; j<newsList.getMainNewsCount(); j++)
            {
                MainNewsItemType newsItem = newsList.getMainNews(j);
                DeleteMainNewsTextWithId.processData(db_, newsItem.getId());
            }
            DeleteMainNewsWithIdNews.processData(db_, newsGroupList.getIdNews() );
        }
        DeleteMainListNewsWithIdSiteSupportLanguage.process( db_, idSiteLanguageForDrop );
        db_.commit();

        System.out.println("drop articles");
        SiteCtxArticleListType articleList =
            new GetSiteCtxArticleWithIdSiteSupportLanguageList(db_, idSiteLanguageForDrop).item;
        for (int i=0; i<articleList.getSiteCtxArticleCount(); i++)
        {
            SiteCtxArticleItemType articleItem  = articleList.getSiteCtxArticle(i);
            DeleteSiteCtxArticleDataWithIdSiteCtxArticle.process(db_, articleItem.getIdSiteCtxArticle());
        }
        DeleteSiteCtxArticleWithIdSiteSupportLanguage.process(db_, idSiteLanguageForDrop);
        db_.commit();

        System.out.println("drop catalog context");
        SiteCtxLangCatalogListType catalogList =
            new GetSiteCtxLangCatalogWithIdSiteSupportLanguageList(db_, idSiteLanguageForDrop).item;
        for (int i=0; i<catalogList.getSiteCtxLangCatalogCount(); i++)
        {
            SiteCtxLangCatalogItemType catalogItem = catalogList.getSiteCtxLangCatalog(i);
            DeleteSiteCtxCatalogWithIdSiteCtxLangCatalog.process(db_, catalogItem.getIdSiteCtxLangCatalog());
        }
        DeleteSiteCtxLangCatalogWithIdSiteSupportLanguage.process(db_, idSiteLanguageForDrop);
        DeleteSiteTemplateMemberWithIdSiteSupportLanguage.process(db_, idSiteLanguageForDrop);
        DeleteSiteTemplateWithIdSiteSupportLanguage.process(db_, idSiteLanguageForDrop);

        SiteXsltListType xsltList =
            new GetSiteXsltWithIdSiteSupportLanguageList(db_, idSiteLanguageForDrop).item;
        for (int i=0; i<xsltList.getSiteXsltCount(); i++)
        {
            SiteXsltItemType xsltItem = xsltList.getSiteXslt(i);
            DeleteSiteXsltDataWithIdSiteXslt.process(db_, xsltItem.getIdSiteXslt());
        }
        DeleteSiteXsltWithIdSiteSupportLanguage.process(db_, idSiteLanguageForDrop);
    }

    public static void dropSite( DatabaseAdapter DatabaseAdapter_, Long idSiteForDrop )
        throws Exception
    {
        System.out.println("drop data for site idSite - "+idSiteForDrop);

        SiteSupportLanguageListType siteLanguageList =
            new GetSiteSupportLanguageWithIdSiteList(DatabaseAdapter_, idSiteForDrop).item;
        for (int i=0; i<siteLanguageList.getSiteSupportLanguageCount(); i++)
        {
            SiteSupportLanguageItemType siteLanguageItem = siteLanguageList.getSiteSupportLanguage(i);
            dropSiteLanguageData( DatabaseAdapter_, siteLanguageItem.getIdSiteSupportLanguage());

            DeleteSiteSupportLanguageItem.processData( DatabaseAdapter_, siteLanguageItem );
        }
        DatabaseAdapter_.commit();

        // Todo Fix acces to CSS with new structure
/*
        System.out.println("delete CSS data");
        SiteContentCssListType cssList = new GetSiteContentCssWithIdSiteList(DatabaseAdapter_, idSiteForDrop).item;
        for (int i=0; i<cssList.getSiteContentCssCount(); i++)
        {
            SiteContentCssItemType cssItem = cssList.getSiteContentCss(i);
            DeleteSiteContentCssDataWithIdSiteContentCss.process(DatabaseAdapter_, cssItem.getIdSiteContentCss());
        }
        DeleteSiteContentCssWithIdSite.process(DatabaseAdapter_, idSiteForDrop);
*/


        System.out.println("delete shop data");
//        deleteShopData(DatabaseAdapter_, idSiteForDrop);

        System.out.println("delete virtual host");
        DeleteSiteVirtualHostWithIdSite.process(DatabaseAdapter_, idSiteForDrop);
        DatabaseAdapter_.commit();

        System.out.println("delete test site description");
        SiteListSiteItemType site = new GetSiteListSiteItem(DatabaseAdapter_, idSiteForDrop).item;
        DeleteSiteListSiteItem.processData(DatabaseAdapter_, site);
        DatabaseAdapter_.commit();
        System.out.println("Deleting test data is done.");
    }
/*
    private static void deleteShopData( DatabaseAdapter DatabaseAdapter_, long idSiteForDrop )
        throws Exception
    {
        CashCurrencyListType currencyList = GetCashCurrencyWithIdSiteList.getInstance( DatabaseAdapter_, idSiteForDrop ).item;

        for (int i=0; i<currencyList.getCashCurrencyCount(); i++)
            DeleteCashCurrValueWithIdCurrency.processData(DatabaseAdapter_, currencyList.getCashCurrency(i).getIdCurrency());

        DeleteCashCurrencyWithIdSite.processData(DatabaseAdapter_, idSiteForDrop);
        DatabaseAdapter_.commit();

        PriceShopTableListType shopList = GetPriceShopTableWithIdSiteList.getInstance(DatabaseAdapter_,idSiteForDrop).item;
        for (int i=0; i<shopList.getPriceShopTableCount(); i++)
        {
            PriceShopTableItemType shop = shopList.getPriceShopTable(i);
            PriceListListType itemList = GetPriceListWithIdShopList.getInstance(DatabaseAdapter_, shop.getIdShop()).item;

            for (int k=0; k<itemList.getPriceListCount(); k++)
            {
                PriceListItemType shopItem = itemList.getPriceList(k);
                PriceItemDescriptionItemType itemDesc = GetPriceItemDescriptionItem.getInstance(DatabaseAdapter_,shopItem.getIdItem()).item;
                DeletePriceItemDescDataWithIdPriceItemDescription.processData(DatabaseAdapter_, itemDesc.getIdPriceItemDescription());
                DeletePriceItemDescriptionWithIdPriceItemDescription.processData(DatabaseAdapter_, itemDesc.getIdPriceItemDescription() );
            }

            PreparedStatement ps = null;
            ResultSet rs = null;
            Vector v = new Vector();
            try
            {
                ps=DatabaseAdapter_.conn.prepareStatement(
                    "select distinct b.ID_ORDER_V2 " +
                    "from PRICE_LIST a, PRICE_ORDER_V2 b " +
                    "where a.ID_ITEM=b.ID_ITEM and a.ID_SHOP=?");
                ps.setLong(1, shop.getIdShop());
                rs = ps.executeQuery();
                while(rs.next())
                {
                    v.add( new Long(RsetTools.getLong(rs, "ID_ORDER_V2")) );
                }
            }
            finally
            {
                RsetTools.closeRsPs(rs, ps);
                rs=null;
                ps=null;
            }
            for (int j=0; j<v.size(); j++)
            {
                long idOrder = ((Long)v.elementAt(j)).longValue();
                DeletePriceOrderV2WithIdOrderV2.processData(DatabaseAdapter_, idOrder);
                DeletePriceRelateUserOrderV2WithIdOrderV2.processData(DatabaseAdapter_, idOrder);
            }
            DeletePriceListWithIdShop.processData( DatabaseAdapter_, shop.getIdShop() );
            DeletePriceUserShopDiscountWithIdShop.processData(DatabaseAdapter_, shop.getIdShop());
            DeletePriceShopPrecisionWithIdShop.processData(DatabaseAdapter_, shop.getIdShop());
        }

        DeletePriceShopTableWithIdSite.processData( DatabaseAdapter_, idSiteForDrop );
        DatabaseAdapter_.commit();
    }

    public static SiteExtendItemType getExtendData( String host )
        throws Exception
    {
        DatabaseAdapter db_ = null;
        try
        {
            db_ = DatabaseAdapter.getInstance();
            return getExtendData(db_, host);
        }
        finally
        {
            DatabaseAdapter.close(db_);
            db_ = null;
        }
    }

    public static SiteExtendItemType getExtendData( DatabaseAdapter db_, String host )
        throws Exception
    {
        PortalInfo p = PortalInfo.getInstance(db_, host);
        Long idSite = p.sites.getIdSite();
        if (idSite==null)
        {
            System.out.println("Host '"+host+"' not found, idSite - "+idSite);
            return null;
        }

        return getExtendData(db_, idSite);
    }

    public static SiteExtendItemType getExtendData( DatabaseAdapter db_, long idSite )
        throws Exception
    {
        if (idSite==-1)
        {
            System.out.println("Host not found, idSite - "+idSite);
            return null;
        }

        SiteExtendItemType site = new SiteExtendItemType();
        GetSiteListSiteItem siteItem = GetSiteListSiteItem.getInstance(db_, idSite);
        siteItem.copyItem( site );
        SiteContentCssListType cssList =
            GetSiteContentCssWithIdSiteList.getInstance(db_, idSite).item;

        site.setCtxType( GetSiteCtxTypeFullList.getInstance(db_, 0).item );

        // получаем CSS
        for (int i=0; i<cssList.getSiteContentCssCount(); i++)
        {
            SiteContentCssItemType cssItem = cssList.getSiteContentCss(i);
            SiteContentCssDataListType cssDataList =
                GetSiteContentCssDataWithIdSiteContentCssList.getInstance(db_, cssItem.getIdSiteContentCss()).item;

            String cssData = "";
            for (int k=0; k<cssDataList.getSiteContentCssDataCount(); k++)
                cssData += cssDataList.getSiteContentCssData(k).getCssData();
            cssItem.setCssData( cssData );

        }
        site.setCss( cssList );

        // получаем список языков поддерживаемых сайтом
        SiteExtendLanguageListType lang = new SiteExtendLanguageListType();
        SiteSupportLanguageListType listLang = GetSiteSupportLanguageWithIdSiteList.getInstance(db_, idSite).item;
        for (int i=0; i<listLang.getSiteSupportLanguageCount(); i++)
        {
            SiteSupportLanguageItemType itemLang = listLang.getSiteSupportLanguage(i);
            SiteExtendLanguageType extendItemLang = new SiteExtendLanguageType();
            GetSiteSupportLanguageItem.copyItem( itemLang, extendItemLang);

            // получаем список статей
            SiteCtxArticleListType articles =
                GetSiteCtxArticleWithIdSiteSupportLanguageList.getInstance
                (
                    db_, extendItemLang.getIdSiteSupportLanguage()
                ).item;

            SiteCtxArticleListType articlesPlain = new SiteCtxArticleListType();
            SiteCtxArticleListType articlesXml = new SiteCtxArticleListType();
            for (int j=0; j<articles.getSiteCtxArticleCount(); j++)
            {
                SiteCtxArticleItemType article = articles.getSiteCtxArticle(j);
                SiteCtxArticleDataListType articleDataList =
                    GetSiteCtxArticleDataWithIdSiteCtxArticleList.getInstance(db_, article.getIdSiteCtxArticle()).item;
                String artcileData = "";
                for (int k=0;k<articleDataList.getSiteCtxArticleDataCount();k++)
                    artcileData+= articleDataList.getSiteCtxArticleData(k).getArticleData();

                article.setArticleData( artcileData );

                if (article.getIsPlainHtml())
                    articlesPlain.addSiteCtxArticle( article );
                else
                    articlesXml.addSiteCtxArticle( article );
            }
            extendItemLang.setArticlePlain( articlesPlain );
            extendItemLang.setArticleXml( articlesXml );

            SiteCtxLangCatalogListType langCatalog =
                  GetSiteCtxLangCatalogWithIdSiteSupportLanguageList.getInstance(db_, extendItemLang.getIdSiteSupportLanguage()).item;
            SiteExtendCtxLangCatalogListType extendLangCatalogList = new SiteExtendCtxLangCatalogListType();
            for (int j=0; j<langCatalog.getSiteCtxLangCatalogCount(); j++)
            {
                SiteCtxLangCatalogItemType langCatalogItem = langCatalog.getSiteCtxLangCatalog(j);
                SiteExtendCtxLangCatalogType extendLangCatalog = new SiteExtendCtxLangCatalogType();
                GetSiteCtxLangCatalogItem.copyItem(langCatalogItem, extendLangCatalog);
                extendLangCatalog.setContextList(
                    GetSiteCtxCatalogWithIdSiteCtxLangCatalogList.getInstance(
                        db_, extendLangCatalog.getIdSiteCtxLangCatalog()
                    ).item
                );
                extendLangCatalogList.addItem( extendLangCatalog );
            }
            extendItemLang.setContext( extendLangCatalogList );

            MainListNewsListType newsGroupCore = GetMainListNewsWithIdSiteSupportLanguageList.getInstance(
                db_, extendItemLang.getIdSiteSupportLanguage()
            ).item;
            SiteExtendNewsGroupListType extendNewsGroupList = new SiteExtendNewsGroupListType();
            for (int j=0; j<newsGroupCore.getMainListNewsCount(); j++)
            {
                MainListNewsItemType newsGroup = newsGroupCore.getMainListNews(j);
                SiteExtendNewsGroupType extendNewsGroup = new  SiteExtendNewsGroupType();
                GetMainListNewsItem.copyItem(newsGroup, extendNewsGroup);
                MainNewsListType newsList =
                    GetMainNewsWithIdNewsList.getInstance(
                        db_, extendNewsGroup.getIdNews()
                    ).item;
                extendNewsGroup.setNewsItem( newsList );
                for (int k=0; k<newsList.getMainNewsCount(); k++)
                {
                    MainNewsItemType newsItem = newsList.getMainNews(k);
                    MainNewsTextListType newsTextItem =
                        GetMainNewsTextWithIdList.getInstance(db_, newsItem.getId()).item;
                    String newsText = "";
                    for (int l=0; l<newsTextItem.getMainNewsTextCount();l++)
                        newsText += newsTextItem.getMainNewsText(l).getText();
                    newsItem.setText( newsText );
                }

                extendNewsGroupList.addGroup( extendNewsGroup );
            }
            extendItemLang.setNewsGroup( extendNewsGroupList );

            extendItemLang.setTemplate(
                GetSiteTemplateWithIdSiteSupportLanguageList.getInstance(
                    db_, extendItemLang.getIdSiteSupportLanguage()
                ).item
            );
            SiteTemplateMemberListType memberTemplaeList =
            GetSiteTemplateMemberWithIdSiteSupportLanguageList.getInstance(
                    db_, extendItemLang.getIdSiteSupportLanguage()
                ).item;
            if (memberTemplaeList.getSiteTemplateMemberCount()>0)
                extendItemLang.setMemberTemplate( memberTemplaeList.getSiteTemplateMember(0) );

            SiteXsltListType xsltList =
                GetSiteXsltWithIdSiteSupportLanguageList.getInstance(
                    db_, extendItemLang.getIdSiteSupportLanguage()
                ).item;
            for (int j=0; j<xsltList.getSiteXsltCount(); j++)
            {
                SiteXsltItemType xsltItem = xsltList.getSiteXslt(j);
                SiteXsltDataListType xsltDataList =
                    GetSiteXsltDataWithIdSiteXsltList.getInstance(db_, xsltItem.getIdSiteXslt()).item;
                String xsltData = "";
                for (int k=0; k<xsltDataList.getSiteXsltDataCount(); k++)
                    xsltData += xsltDataList.getSiteXsltData(k).getXslt();
                xsltItem.setXslt( xsltData );
            }
            extendItemLang.setXslt( xsltList );
            lang.addItem( extendItemLang );
        }

        site.setSiteLanguage( lang );
        site.setVirtualHost(
            GetSiteVirtualHostWithIdSiteList.getInstance(
                db_, idSite
            ).item
        );

        return site;
    }
*/
}
