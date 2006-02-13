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
import org.riverock.generic.db.DatabaseManager;
import org.riverock.generic.startup.StartupApplication;
import org.riverock.webmill.core.*;
import org.riverock.webmill.schema.core.*;

/**
 * Author: mill
 * Date: Mar 18, 2003
 * Time: 11:09:59 AM
 * <p/>
 * $Id$
 */
public final class SiteService {
    public SiteService() {
    }

    public static void main(String[] args) throws Exception{
        StartupApplication.init();
        DatabaseAdapter a = DatabaseAdapter.getInstance();
        WmPortalListSiteListType siteList = GetWmPortalListSiteFullList.getInstance(a, 1).item;

        for (final WmPortalListSiteItemType site : siteList.getWmPortalListSite()) {
            if (site.getIdSite()!=16) {
                dropSite( a, site.getIdSite() );
            }
        }
        a.commit();
        DatabaseManager.close(a);
        a = null;

    }

    public static void dropSiteLanguageData(DatabaseAdapter db_, Long idSiteLanguageForDrop)
        throws Exception {
        System.out.println("drop news");
        WmNewsListListType newsFullList =
            new GetWmNewsListWithIdSiteSupportLanguageList(db_, idSiteLanguageForDrop).item;
        for (int i = 0; i < newsFullList.getWmNewsListCount(); i++) {
            WmNewsListItemType newsGroupList = newsFullList.getWmNewsList(i);
            WmNewsItemListType newsList = new GetWmNewsItemWithIdNewsList(db_, newsGroupList.getIdNews()).item;
            for (int j = 0; j < newsList.getWmNewsItemCount(); j++) {
                WmNewsItemItemType newsItem = newsList.getWmNewsItem(j);
                DeleteWmNewsItemTextWithId.processData(db_, newsItem.getId());
            }
            DeleteWmNewsItemWithIdNews.processData(db_, newsGroupList.getIdNews());
        }
        DeleteWmNewsListWithIdSiteSupportLanguage.process(db_, idSiteLanguageForDrop);
        db_.commit();

        System.out.println("drop articles");
        WmPortletArticleListType articleList =
            new GetWmPortletArticleWithIdSiteSupportLanguageList(db_, idSiteLanguageForDrop).item;
        for (int i = 0; i < articleList.getWmPortletArticleCount(); i++) {
            WmPortletArticleItemType articleItem = articleList.getWmPortletArticle(i);
            DeleteWmPortletArticleDataWithIdSiteCtxArticle.process(db_, articleItem.getIdSiteCtxArticle());
        }
        DeleteWmPortletArticleWithIdSiteSupportLanguage.process(db_, idSiteLanguageForDrop);
        db_.commit();

        System.out.println("drop catalog context");
        WmPortalCatalogLanguageListType catalogList =
            new GetWmPortalCatalogLanguageWithIdSiteSupportLanguageList(db_, idSiteLanguageForDrop).item;
        for (int i = 0; i < catalogList.getWmPortalCatalogLanguageCount(); i++) {
            WmPortalCatalogLanguageItemType catalogItem = catalogList.getWmPortalCatalogLanguage(i);
            DeleteWmPortalCatalogWithIdSiteCtxLangCatalog.process(db_, catalogItem.getIdSiteCtxLangCatalog());
        }
        DeleteWmPortalCatalogLanguageWithIdSiteSupportLanguage.process(db_, idSiteLanguageForDrop);
        DeleteWmPortalTemplateMemberWithIdSiteSupportLanguage.process(db_, idSiteLanguageForDrop);
        DeleteWmPortalTemplateWithIdSiteSupportLanguage.process(db_, idSiteLanguageForDrop);

        WmPortalXsltListType xsltList =
            new GetWmPortalXsltWithIdSiteSupportLanguageList(db_, idSiteLanguageForDrop).item;
        for (int i = 0; i < xsltList.getWmPortalXsltCount(); i++) {
            WmPortalXsltItemType xsltItem = xsltList.getWmPortalXslt(i);
            DeleteWmPortalXsltDataWithIdSiteXslt.process(db_, xsltItem.getIdSiteXslt());
        }
        DeleteWmPortalXsltWithIdSiteSupportLanguage.process(db_, idSiteLanguageForDrop);
    }

    public static void dropSite(DatabaseAdapter DatabaseAdapter_, Long idSiteForDrop)
        throws Exception {
        if (idSiteForDrop == 16) {
            throw new IllegalAccessException("Never drop site with id 16");
        }

        System.out.println("drop data for site idSite - " + idSiteForDrop);

        WmPortalSiteLanguageListType siteLanguageList =
            new GetWmPortalSiteLanguageWithIdSiteList(DatabaseAdapter_, idSiteForDrop).item;

        for (int i = 0; i < siteLanguageList.getWmPortalSiteLanguageCount(); i++) {
            WmPortalSiteLanguageItemType siteLanguageItem = siteLanguageList.getWmPortalSiteLanguage(i);
            dropSiteLanguageData(DatabaseAdapter_, siteLanguageItem.getIdSiteSupportLanguage());

            DeleteWmPortalSiteLanguageItem.processData(DatabaseAdapter_, siteLanguageItem);
        }
        DatabaseAdapter_.commit();

        System.out.println("delete CSS data");
        WmPortalCssListType cssList = new GetWmPortalCssWithIdSiteList(DatabaseAdapter_, idSiteForDrop).item;
        for (int i = 0; i < cssList.getWmPortalCssCount(); i++) {
            WmPortalCssItemType cssItem = cssList.getWmPortalCss(i);
            DeleteWmPortalCssDataWithIdSiteContentCss.process(DatabaseAdapter_, cssItem.getIdSiteContentCss());
        }
        DeleteWmPortalCssWithIdSite.process(DatabaseAdapter_, idSiteForDrop);


        System.out.println("delete shop data");
//        deleteShopData(DatabaseAdapter_, idSiteForDrop);

        System.out.println("delete virtual host");
        DeleteWmPortalVirtualHostWithIdSite.process(DatabaseAdapter_, idSiteForDrop);
        DatabaseAdapter_.commit();

        System.out.println("delete test site description");
        WmPortalListSiteItemType site = new GetWmPortalListSiteItem(DatabaseAdapter_, idSiteForDrop).item;
        DeleteWmPortalListSiteItem.processData(DatabaseAdapter_, site);
        DatabaseAdapter_.commit();
        System.out.println("Deleting test data is done.");
    }
/*
    private static void deleteShopData(DatabaseAdapter DatabaseAdapter_, long idSiteForDrop)
        throws Exception {
        WmCashCurrencyListType currencyList = GetWmCashCurrencyWithIdSiteList.getInstance(DatabaseAdapter_, idSiteForDrop).item;

        for (int i = 0; i < currencyList.getWmCashCurrencyCount(); i++)
            DeleteWmCashCurrValueWithIdCurrency.processData(DatabaseAdapter_, currencyList.getWmCashCurrency(i).getIdCurrency());

        DeleteWmCashCurrencyWithIdSite.processData(DatabaseAdapter_, idSiteForDrop);
        DatabaseAdapter_.commit();

        WmPriceShopListListType shopList = GetWmPriceShopListWithIdSiteList.getInstance(DatabaseAdapter_, idSiteForDrop).item;
        for (int i = 0; i < shopList.getWmPriceShopListCount(); i++) {
            WmPriceShopListItemType shop = shopList.getWmPriceShopList(i);
            WmPriceListListType itemList = GetWmPriceListWithIdShopList.getInstance(DatabaseAdapter_, shop.getIdShop()).item;

            for (int k = 0; k < itemList.getWmPriceListCount(); k++) {
                WmPriceListItemType shopItem = itemList.getWmPriceList(k);
//                WmPriceItemDescriptionItemType itemDesc = GetWmPriceItemDescriptionItem.getInstance(DatabaseAdapter_, shopItem.getIdItem()).item;
//                DeleteWmPriceItemDescDataWithIdPriceItemDescription.processData(DatabaseAdapter_, itemDesc.getIdPriceItemDescription());
//                DeleteWmPriceItemDescDataDescWithIdPriceItemDescription.processData(DatabaseAdapter_, itemDesc.getIdPriceItemDescription());
            }

            PreparedStatement ps = null;
            ResultSet rs = null;
            List<Long> v = new ArrayList<Long>();
            try {
                ps = DatabaseAdapter_.getConnection().prepareStatement("select distinct b.ID_ORDER_V2 " +
                    "from   WM_PRICE_LIST a, WM_PRICE_ORDER b " +
                    "where  a.ID_ITEM=b.ID_ITEM and a.ID_SHOP=?");
                ps.setLong(1, shop.getIdShop());
                rs = ps.executeQuery();
                while (rs.next()) {
                    v.add(RsetTools.getLong(rs, "ID_ORDER_V2"));
                }
            }
            finally {
                DatabaseManager.close(rs, ps);
                rs = null;
                ps = null;
            }
            Iterator<Long> iterator = v.iterator();
            while (iterator.hasNext()) {
                Long idOrder = iterator.next();
                DeleteWmPriceOrderWithIdOrderV2.processData(DatabaseAdapter_, idOrder);
                DeleteWmPriceRelateUserOrderWithIdOrderV2.processData(DatabaseAdapter_, idOrder);
            }
            DeleteWmPriceListWithIdShop.processData(DatabaseAdapter_, shop.getIdShop());
//            DeleteWmPriceUserShopDiscountWithIdShop.processData(DatabaseAdapter_, shop.getIdShop());
            DeleteWmPriceShopPrecisionWithIdShop.processData(DatabaseAdapter_, shop.getIdShop());
        }

        DeleteWmPriceShopListWithIdSite.processData(DatabaseAdapter_, idSiteForDrop);
        DatabaseAdapter_.commit();
    }
*/
/*
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
        GetWmPortalListSiteItem siteItem = GetWmPortalListSiteItem.getInstance(db_, idSite);
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
        WmPortalSiteLanguageListType listLang = GetWmPortalSiteLanguageWithIdSiteList.getInstance(db_, idSite).item;
        for (int i=0; i<listLang.getWmPortalSiteLanguageCount(); i++)
        {
            WmPortalSiteLanguageItemType itemLang = listLang.getWmPortalSiteLanguage(i);
            SiteExtendLanguageType extendItemLang = new SiteExtendLanguageType();
            GetSiteSupportLanguageItem.copyItem( itemLang, extendItemLang);

            // получаем список статей
            WmPortletArticleListType articles =
                GetWmPortletArticleWithIdSiteSupportLanguageList.getInstance
                (
                    db_, extendItemLang.getIdSiteSupportLanguage()
                ).item;

            WmPortletArticleListType articlesPlain = new WmPortletArticleListType();
            WmPortletArticleListType articlesXml = new WmPortletArticleListType();
            for (int j=0; j<articles.getWmPortletArticleCount(); j++)
            {
                WmPortletArticleItemType article = articles.getWmPortletArticle(j);
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

            WmPortalCatalogLanguageListType langCatalog =
                  GetWmPortalCatalogLanguageWithIdSiteSupportLanguageList.getInstance(db_, extendItemLang.getIdSiteSupportLanguage()).item;
            SiteExtendCtxLangCatalogListType extendLangCatalogList = new SiteExtendCtxLangCatalogListType();
            for (int j=0; j<langCatalog.getWmPortalCatalogLanguageCount(); j++)
            {
                WmPortalCatalogLanguageItemType langCatalogItem = langCatalog.getWmPortalCatalogLanguage(j);
                SiteExtendCtxLangCatalogType extendLangCatalog = new SiteExtendCtxLangCatalogType();
                GetSiteCtxLangCatalogItem.copyItem(langCatalogItem, extendLangCatalog);
                extendLangCatalog.setContextList(
                    GetWmPortalCatalogWithIdSiteCtxLangCatalogList.getInstance(
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

            WmPortalXsltListType xsltList =
                GetWmPortalXsltWithIdSiteSupportLanguageList.getInstance(
                    db_, extendItemLang.getIdSiteSupportLanguage()
                ).item;
            for (int j=0; j<xsltList.getWmPortalXsltCount(); j++)
            {
                WmPortalXsltItemType xsltItem = xsltList.getWmPortalXslt(j);
                WmPortalXsltDataListType xsltDataList =
                    GetWmPortalXsltDataWithIdSiteXsltList.getInstance(db_, xsltItem.getIdSiteXslt()).item;
                String xsltData = "";
                for (int k=0; k<xsltDataList.getWmPortalXsltDataCount(); k++)
                    xsltData += xsltDataList.getWmPortalXsltData(k).getXslt();
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
