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
package org.riverock.portlet.test.cases;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Iterator;

import org.riverock.common.tools.RsetTools;
import org.riverock.generic.config.GenericConfig;
import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.db.DatabaseManager;
import org.riverock.generic.schema.db.CustomSequenceType;
import org.riverock.generic.site.SiteListSite;
import org.riverock.generic.tools.XmlTools;
import org.riverock.interfaces.portlet.menu.MenuInterface;
import org.riverock.interfaces.portlet.menu.MenuItemInterface;
import org.riverock.interfaces.portlet.menu.MenuLanguageInterface;
import org.riverock.portlet.core.GetCashCurrencyStdFullList;
import org.riverock.portlet.core.GetSiteCtxTypeFullList;
import org.riverock.portlet.core.InsertCashCurrValueItem;
import org.riverock.portlet.core.InsertCashCurrencyItem;
import org.riverock.portlet.core.InsertPriceShopTableItem;
import org.riverock.portlet.core.InsertSiteCtxCatalogItem;
import org.riverock.portlet.core.InsertSiteCtxLangCatalogItem;
import org.riverock.portlet.core.InsertSiteSupportLanguageItem;
import org.riverock.portlet.price.CurrencyList;
import org.riverock.portlet.price.CurrencyService;
import org.riverock.portlet.schema.core.*;
import org.riverock.portlet.schema.price.CurrencyCurrentCursType;
import org.riverock.portlet.schema.price.CustomCurrencyItemType;
import org.riverock.webmill.core.InsertSiteListSiteItem;
import org.riverock.webmill.core.InsertSiteTemplateItem;
import org.riverock.webmill.core.InsertSiteVirtualHostItem;
import org.riverock.webmill.port.PortalInfo;
import org.riverock.webmill.portal.menu.SiteMenu;
import org.riverock.webmill.portlet.PortletManager;
import org.riverock.webmill.schema.core.SiteListSiteItemType;
import org.riverock.webmill.schema.core.SiteTemplateItemType;
import org.riverock.webmill.schema.core.SiteVirtualHostItemType;
import org.riverock.webmill.site.SiteService;

import junit.framework.Assert;
import org.apache.log4j.Logger;

/**
 * User: Admin
 * Date: May 6, 2003
 * Time: 10:36:40 PM
 *
 * $Id$
 */
public class TestSite {

    private static Logger log = Logger.getLogger( TestSite.class );

    public final static String TEST_SERVER_NAME = "test-host";
    public final static String TEST_LANGUAGE = "ru_RU";
    public final static String NAME_TEST_SITE = "Пробный сайт";

    public final static String NAME_DEFAULT_MENU = "DEFAULT_MENU";

    public final static String indexPortletType = "mill.index";

    public Long idSite;
    public Long idVirtualHost;
    public Long idRuSiteLanguage;
    public Long idRuLanguage;
    public Long idRuLangCatalog;

    private DatabaseAdapter db_ = null;
    public static String shopCode = null;

    Long idTemplateIndex;
    String nameIndexTemplate = "index_template";
    Long idTemplateDynamic;
    String nameDynamicTemplate = "dynamic_template";

    public Long idCurrencyEURO = null;
    public static Long idCurrencyStdEURO = null;
    public static final String nameEURO = "EURO";
    public static Double cursEURO = new Double(34.51);


    public Long idCurrencyRUB = null;
    public static final String nameRUB = "Руб";
    public static Long idCurrencyStdRUB = null;
    public static Double cursRUB = new Double(1.01);

    public final static int COUNT_TOP_LEVEL_MENU = 4;
    public final static int COUNT_SUB_MENU = 3;
    public final static String menuItem = "Menu item ";

    public static Long idShop = null;

    TestSite( DatabaseAdapter dbTemp_)
        throws Exception
    {
        db_ = dbTemp_;
        initIdRuLanguage();
        initTestSite();
        initIdRuSiteLanguage();
        db_.commit();
    }

    private void initTestSite()
        throws Exception
    {
        dropSite();
        insertTestSite();
        insertDesignData(); // Templates, XSLT, CSS ...
        insertShopTestData();
        insertMenuTestData();

        PortalInfo p = PortalInfo.getInstance(db_, TEST_SERVER_NAME);
        SiteListSite listSite = new SiteListSite();
        listSite.reinit();
        p.reinit();
        p = PortalInfo.getInstance(db_, TEST_SERVER_NAME);

        Long idSite = SiteListSite.getIdSite( TEST_SERVER_NAME );

        CurrencyList currList = CurrencyList.getInstance(db_, idSite);

        Assert.assertFalse("Error init list of currency", currList==null);
        for (int i=0; i<currList.list.getCurrencyListCount(); i++)
        {
            CustomCurrencyItemType item = currList.list.getCurrencyList(i);
            Assert.assertFalse("Error init current curs of currency", item.getRealCurs().doubleValue()==0);
        }

        SiteListSiteItemType site = p.getSites();
        if (site==null || site.getIdSite()==null)
            throw new Exception("PortalInfo for serverName '"+TEST_SERVER_NAME+"' not initialized" );

        if (!NAME_TEST_SITE.equals( site.getNameSite()))
            throw new Exception("Name of site not correct stored in DB" );

        PortletManager.init();
    }

    private Long insertTemplate(String nameTemplate, String templateData)
            throws Exception
    {
        Long countRec;
        CustomSequenceType seqSite = new CustomSequenceType();
        seqSite.setSequenceName("SEQ_SITE_TEMPLATE");
        seqSite.setTableName( "SITE_TEMPLATE");
        seqSite.setColumnName( "ID_SITE_TEMPLATE" );
        Long idTemplate = new Long(db_.getSequenceNextValue( seqSite ));

        SiteTemplateItemType template = new SiteTemplateItemType();
        template.setNameSiteTemplate( nameTemplate );
        template.setIdSiteSupportLanguage( idRuSiteLanguage );
        template.setIdSiteTemplate( idTemplate );
        template.setTemplateData( templateData );

        countRec = InsertSiteTemplateItem.processData( db_, template );
        Assert.assertFalse("Error insert new template", countRec.longValue()==0);

        return idTemplate;
    }

    private void insertDesignData()
            throws Exception
    {
        idTemplateIndex = insertTemplate(nameIndexTemplate, "");
        idTemplateDynamic = insertTemplate(nameDynamicTemplate, "");
    }

    private void insertMenuTestData() throws Exception
    {

        SiteCtxTypeListType ctxList = GetSiteCtxTypeFullList.getInstance(db_, 0).item;
        Long idCtx = null;
        for (int i=0; i<ctxList.getSiteCtxTypeCount(); i++)
        {
            SiteCtxTypeItemType ctx = ctxList.getSiteCtxType(i);
            if (indexPortletType.equals(ctx.getType()))
                idCtx = ctx.getIdSiteCtxType();
        }

        Assert.assertFalse("ID for context '"+indexPortletType+"' not found", idCtx==null);


        SiteCtxLangCatalogItemType langCatalog = new SiteCtxLangCatalogItemType();
        langCatalog.setCatalogCode( NAME_DEFAULT_MENU );
        langCatalog.setIsDefault( Boolean.TRUE );
        langCatalog.setIdSiteSupportLanguage( idRuSiteLanguage );

        CustomSequenceType seqSite = null;
        seqSite =  new CustomSequenceType();
        seqSite.setSequenceName( "SEQ_SITE_CTX_LANG_CATALOG" );
        seqSite.setTableName( "SITE_CTX_LANG_CATALOG" );
        seqSite.setColumnName( "ID_SITE_CTX_LANG_CATALOG" );
        idRuLangCatalog = new Long(db_.getSequenceNextValue( seqSite ));
        langCatalog.setIdSiteCtxLangCatalog( idRuLangCatalog );

        System.out.println("idRuLangCatalog - "+idRuLangCatalog);

        Long countRec = null;
        countRec = InsertSiteCtxLangCatalogItem.processData(db_, langCatalog);

        Assert.assertFalse("Error insert new lang menu", countRec.longValue()==0);

        for (int i=0; i<COUNT_TOP_LEVEL_MENU; i++)
        {
            SiteCtxCatalogItemType menu = new SiteCtxCatalogItemType();
            menu.setIdTopCtxCatalog( null );
            menu.setKeyMessage( menuItem + "#"+(i+1)+".0 " );
            menu.setIsUseProperties( Boolean.TRUE );
            menu.setIdSiteCtxLangCatalog( idRuLangCatalog );
            menu.setIdSiteCtxType( idCtx );
            menu.setIdSiteTemplate( idTemplateIndex );

            seqSite = null;
            seqSite = new CustomSequenceType();
            seqSite.setSequenceName( "SEQ_SITE_CTX_CATALOG" );
            seqSite.setTableName( "SITE_CTX_CATALOG" );
            seqSite.setColumnName( "ID_SITE_CTX_CATALOG" );

            Long idMenu = new Long(db_.getSequenceNextValue( seqSite ));
            menu.setIdSiteCtxCatalog( idMenu );
            menu.setIdTopCtxCatalog( new Long(0) );

            countRec = InsertSiteCtxCatalogItem.processData(db_, menu);
            Assert.assertFalse("Error insert new menu item", countRec.longValue()==0);

            for (int k=0; k<COUNT_SUB_MENU; k++)
            {
                SiteCtxCatalogItemType subMenu = new SiteCtxCatalogItemType();
                subMenu.setIdTopCtxCatalog( idMenu );
                subMenu.setKeyMessage( menuItem + "#"+(i+1)+"."+(k+1) );
                subMenu.setIsUseProperties( Boolean.FALSE );
                subMenu.setIdSiteCtxLangCatalog( idRuLangCatalog );
                subMenu.setIdSiteCtxType( idCtx );
                subMenu.setIdSiteTemplate( idTemplateIndex );

                seqSite = null;
                seqSite =  new CustomSequenceType();
                seqSite.setSequenceName( "SEQ_SITE_CTX_CATALOG" );
                seqSite.setTableName( "SITE_CTX_CATALOG" );
                seqSite.setColumnName( "ID_SITE_CTX_CATALOG" );

                Long idSubMenu = new Long(db_.getSequenceNextValue( seqSite ));
                subMenu.setIdSiteCtxCatalog( idSubMenu );

                countRec = InsertSiteCtxCatalogItem.processData(db_, subMenu);
                Assert.assertFalse("Error insert new sub menu item", countRec.longValue()==0);
            }
        }
        SiteMenu catalog = SiteMenu.getInstance(db_, idSite);


        Assert.assertFalse( "Size of catalog is wrong", catalog.getMenuLanguageCount()!=1);
        MenuLanguageInterface cList = catalog.getMenuLanguage(0);

        Assert.assertFalse( "Wrong language of catalog ", !cList.getLang().equals(TEST_LANGUAGE) );

        MenuInterface c = cList.getMenu(0);

        XmlTools.writeToFile(c, GenericConfig.getGenericDebugDir()+"test-catalog.xml");


        Assert.assertFalse( "Wrong count of top level, value - "+c.getMenuItem().size(),
                c.getMenuItem().size()!=(COUNT_TOP_LEVEL_MENU)
        );

        Iterator iterator = c.getMenuItem().iterator();
        while( iterator.hasNext() ) {
            MenuItemInterface ci = (MenuItemInterface)iterator.next();
            Assert.assertFalse( "Wrong count of top level, value - "+ci.getCatalogItems().size(),
                    ci.getCatalogItems().size()!=(COUNT_SUB_MENU)
            );
        }
    }

    public void insertShopTestData() throws Exception
    {
        CashCurrencyStdListType currencyList = GetCashCurrencyStdFullList.getInstance(db_, 0).item;
        for (int i=0; i<currencyList.getCashCurrencyStdCount(); i++)
        {
            CashCurrencyStdItemType currItem = currencyList.getCashCurrencyStd(i);
            if ( nameRUB.equals(currItem.getConvertCurrency()))
            {
                idCurrencyStdRUB = currItem.getIdStdCurr();
                continue;
            }
            if ( nameEURO.equals(currItem.getConvertCurrency()))
            {
                idCurrencyStdEURO = currItem.getIdStdCurr();
                continue;
            }
        }

        Assert.assertFalse("Error get id for RUB standatd currency", idCurrencyStdRUB==null);
        Assert.assertFalse("Error get id for RUB standatd currency", idCurrencyStdEURO==null);

        CustomSequenceType seqSite = null;
        CashCurrencyItemType currencyItem = null;
        Long id;

        seqSite =  new CustomSequenceType();
        seqSite.setSequenceName("SEQ_CASH_CURRENCY");
        seqSite.setTableName( "CASH_CURRENCY");
        seqSite.setColumnName( "ID_CURRENCY" );
        idCurrencyRUB = new Long(db_.getSequenceNextValue( seqSite ));
        Assert.assertFalse("Error get new value of sequence for table "+seqSite.getTableName(), idCurrencyRUB==null);
        currencyItem = new  CashCurrencyItemType();
        currencyItem.setCurrency(nameRUB);
        currencyItem.setIdCurrency( idCurrencyRUB );
        currencyItem.setIdSite( idSite );
        currencyItem.setIdStandartCurs( idCurrencyStdRUB );
        currencyItem.setIsUsed( Boolean.TRUE );
        currencyItem.setIsUseStandart( Boolean.FALSE );
        currencyItem.setNameCurrency( nameRUB );
        currencyItem.setPercentValue( new Double(0) );
        InsertCashCurrencyItem.processData( db_, currencyItem);
        db_.commit();

        seqSite =  new CustomSequenceType();
        seqSite.setSequenceName("SEQ_CASH_CURRENCY");
        seqSite.setTableName( "CASH_CURRENCY");
        seqSite.setColumnName( "ID_CURRENCY" );
        idCurrencyEURO = new Long(db_.getSequenceNextValue( seqSite ));
        Assert.assertFalse("Error get new value of sequence for table "+seqSite.getTableName(), idCurrencyEURO==null);
        currencyItem = new  CashCurrencyItemType();
        currencyItem.setCurrency(nameEURO);
        currencyItem.setIdCurrency( idCurrencyEURO );
        currencyItem.setIdSite( idSite );
        currencyItem.setIdStandartCurs( idCurrencyStdEURO );
        currencyItem.setIsUsed( Boolean.TRUE );
        currencyItem.setIsUseStandart( Boolean.FALSE );
        currencyItem.setNameCurrency( nameEURO );
        currencyItem.setPercentValue( new Double(0) );
        InsertCashCurrencyItem.processData(db_, currencyItem);
        db_.commit();

        CashCurrValueItemType curs = null;

        curs = new CashCurrValueItemType();
        curs.setCurs( cursRUB );

        Timestamp stampTemp = new Timestamp(System.currentTimeMillis() );
        curs.setDateChange( stampTemp );
        curs.setIdCurrency( idCurrencyRUB );
        seqSite =  new CustomSequenceType();
        seqSite.setSequenceName("SEQ_CASH_CURR_VALUE");
        seqSite.setTableName( "CASH_CURR_VALUE");
        seqSite.setColumnName( "ID_CURVAL" );
        id = new Long(db_.getSequenceNextValue( seqSite ));
        Assert.assertFalse("Error get new value of sequence for table "+seqSite.getTableName(), id==null);
        curs.setIdCurval( id );
        InsertCashCurrValueItem.processData(db_, curs);
        db_.commit();

        curs = new CashCurrValueItemType();
        curs.setCurs( cursEURO );
        curs.setDateChange( new Timestamp(System.currentTimeMillis() ));
        curs.setIdCurrency( idCurrencyEURO );
        seqSite =  new CustomSequenceType();
        seqSite.setSequenceName("SEQ_CASH_CURR_VALUE");
        seqSite.setTableName( "CASH_CURR_VALUE");
        seqSite.setColumnName( "ID_CURVAL" );
        id = new Long(db_.getSequenceNextValue( seqSite ));
        Assert.assertFalse("Error get new value of sequence for table "+seqSite.getTableName(), id==null);
        curs.setIdCurval( id );
        InsertCashCurrValueItem.processData(db_, curs);
        db_.commit();

        CurrencyCurrentCursType currentCurs =
            CurrencyService.getCurrentCurs(db_, idCurrencyEURO, idSite );
        Assert.assertFalse(
            "Fetched curs of EURO from DB is wrong, object is null",
            currentCurs==null
        );
        Assert.assertFalse(
            "Fetched curs of EURO from DB is wrong, inserted: "+cursEURO+", result: "+currentCurs.getCurs(),
            !currentCurs.getCurs().equals(cursEURO)
        );
        currentCurs = CurrencyService.getCurrentCurs(db_, idCurrencyRUB, idSite );
        Assert.assertFalse(
            "Fetched curs of RUB from DB is wrong, object is null",
            currentCurs == null
        );
        Assert.assertFalse(
            "Fetched curs of RUB from DB is wrong",
            !currentCurs.getCurs().equals(cursRUB)
        );

        PriceShopTableItemType shop = new PriceShopTableItemType();
        shop.setIdSite( idSite );

        seqSite = new CustomSequenceType();
        seqSite.setSequenceName("SEQ_PRICE_SHOP_TABLE");
        seqSite.setTableName( "PRICE_SHOP_TABLE");
        seqSite.setColumnName( "ID_SHOP" );

        idShop = new Long(db_.getSequenceNextValue( seqSite ));
        Assert.assertFalse("Error get new value of sequence for table "+seqSite.getTableName(), idShop==null);

        shop.setIdShop( idShop );
        shop.setNameShop( "Test shop" );
        shopCode = "SHOP_CODE_"+idShop;
        shop.setCodeShop( shopCode );
        shop.setIsClose( Boolean.FALSE );
        shop.setIsProcessInvoice( Boolean.TRUE );
        shop.setIsNeedRecalc( Boolean.TRUE );
        shop.setIsActivateEmailOrder( Boolean.TRUE );
        shop.setIsNeedProcessing( Boolean.TRUE );
        shop.setCommasCount( new Integer(2) );
        shop.setDiscount( new Double(0) );

        InsertPriceShopTableItem.processData( db_, shop);
        db_.commit();

    }


    public void dropTestSite()
        throws Exception
    {
        System.out.println("Drop test site");
//        if (true) throw new Exception( "not implemented" );
        SiteService.dropSite( db_, idSite );
    }

    public void insertTestSite()
        throws Exception
    {
        Long countRec = null;
        try
        {
            {
                CustomSequenceType seqSite = new CustomSequenceType();
                seqSite.setSequenceName("SEQ_SITE_LIST_SITE");
                seqSite.setTableName( "SITE_LIST_SITE");
                seqSite.setColumnName( "ID_SITE" );
                idSite = new Long(db_.getSequenceNextValue( seqSite ));

                System.out.println("new ID of site - "+idSite);

                Assert.assertFalse("Error get new value of sequence for table "+seqSite.getTableName(), idSite==null);

                SiteListSiteItemType siteItem = new SiteListSiteItemType();
                siteItem.setIdSite( idSite );
                siteItem.setIdFirm( new Long(1) );
                siteItem.setDefLanguage( "ru" );
                siteItem.setDefCountry( "RU" );
                siteItem.setNameSite( NAME_TEST_SITE );
                countRec = InsertSiteListSiteItem.processData( db_, siteItem );

                Assert.assertFalse("Error insert new site", countRec.longValue()==0);

                db_.commit();
            }
            {
                CustomSequenceType seqVirtualHost = new CustomSequenceType();
                seqVirtualHost.setSequenceName("SEQ_SITE_VIRTUAL_HOST");
                seqVirtualHost.setTableName( "SITE_VIRTUAL_HOST");
                seqVirtualHost.setColumnName( "ID_SITE_VIRTUAL_HOST" );

                idVirtualHost = new Long(db_.getSequenceNextValue( seqVirtualHost ));

                Assert.assertFalse("Error get new value of sequence for table "+seqVirtualHost.getTableName(), idVirtualHost==null);

                SiteVirtualHostItemType hostItem = new SiteVirtualHostItemType();
                hostItem.setIdSiteVirtualHost( idVirtualHost );
                hostItem.setIdSite( idSite );
                hostItem.setNameVirtualHost( TEST_SERVER_NAME );
                countRec = InsertSiteVirtualHostItem.processData(db_, hostItem);

                Assert.assertFalse("Error insert new site", countRec.longValue()==0);

                db_.commit();
            }
            {
                CustomSequenceType seqSupportLanguage = new CustomSequenceType();
                seqSupportLanguage.setSequenceName("SEQ_SITE_SUPPORT_LANGUAGE");
                seqSupportLanguage.setTableName( "SITE_SUPPORT_LANGUAGE");
                seqSupportLanguage.setColumnName( "ID_SITE_SUPPORT_LANGUAGE" );

                idRuSiteLanguage = new Long(db_.getSequenceNextValue( seqSupportLanguage ));

                Assert.assertFalse("Error get new value of sequence for table "+seqSupportLanguage.getTableName(), idVirtualHost==null);

                SiteSupportLanguageItemType supportLangItem = new  SiteSupportLanguageItemType();
                supportLangItem.setIdSiteSupportLanguage( idRuSiteLanguage );
                supportLangItem.setIdSite( idSite );
                supportLangItem.setIdLanguage( idRuLanguage );
                supportLangItem.setCustomLanguage( TEST_LANGUAGE );
                supportLangItem.setNameCustomLanguage( TEST_LANGUAGE );
                countRec = InsertSiteSupportLanguageItem.processData(db_, supportLangItem);
                Assert.assertFalse("Error insert new site_support_language", countRec.longValue()==0);

                db_.commit();
            }
        }
        catch(Exception e)
        {
            db_.rollback();
            throw e;
        }
    }

    private void dropSite() throws Exception
    {
        // удаляем все сайты которые являются тестовыми
        PreparedStatement ps1 = null;
        ResultSet rs1 = null;
        try
        {
            ps1 = db_.prepareStatement(
                "select ID_SITE from SITE_LIST_SITE where NAME_SITE=?"
            );
            ps1.setString(1, NAME_TEST_SITE );

            rs1 = ps1.executeQuery();
            while (rs1.next())
            {
                Long idSiteForDrop = RsetTools.getLong(rs1, "ID_SITE");

                System.out.println("Drop test site, idSite - "+idSiteForDrop);
                SiteService.dropSite( db_, idSiteForDrop );
            }
        }
        finally
        {
            DatabaseManager.close( rs1, ps1 );
            rs1 = null;
            ps1 = null;
        }

        try
        {
            ps1 = db_.prepareStatement(
                "select ID_SITE from SITE_VIRTUAL_HOST where NAME_VIRTUAL_HOST=?"
            );
            ps1.setString(1, TEST_SERVER_NAME );

            rs1 = ps1.executeQuery();
            while (rs1.next())
            {
                Long idSiteForDrop = RsetTools.getLong(rs1, "ID_SITE");

                System.out.println("Drop test site, idSite - "+idSiteForDrop);
                SiteService.dropSite( db_, idSiteForDrop );
            }
        }
        finally
        {
            DatabaseManager.close( rs1, ps1 );
            rs1 = null;
            ps1 = null;
        }
    }

    public void initIdRuLanguage()
        throws Exception
    {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try
        {
            ps = db_.prepareStatement(
                "select ID_LANGUAGE " +
                "from MAIN_LANGUAGE " +
                "where lower(SHORT_NAME_LANGUAGE) like '"+TEST_LANGUAGE.toLowerCase()+"%'"
            );
            rs = ps.executeQuery();

            Assert.assertFalse("Error get id for language '"+TEST_LANGUAGE+"'", !rs.next() );

            idRuLanguage = RsetTools.getLong(rs, "ID_LANGUAGE" );
        }
        finally
        {
            DatabaseManager.close( rs, ps );
            rs = null;
            ps = null;
        }
    }

    public void initIdRuSiteLanguage()
        throws Exception
    {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try
        {
            ps = db_.prepareStatement(
                "select ID_SITE_SUPPORT_LANGUAGE " +
                "from SITE_SUPPORT_LANGUAGE " +
                "where lower(CUSTOM_LANGUAGE) like '"+TEST_LANGUAGE.toLowerCase()+"%' and " +
                "ID_SITE = ?"
            );
            RsetTools.setLong(ps, 1, idSite);

            rs = ps.executeQuery();

            if ( rs.next() )
            {
                if (log.isDebugEnabled())
                    log.debug("IdSiteLanguage "+idRuSiteLanguage);

                idRuSiteLanguage = RsetTools.getLong(rs, "ID_SITE_SUPPORT_LANGUAGE" );

                if (log.isDebugEnabled())
                    log.debug("IdSiteLanguage "+idRuSiteLanguage);
            }
            else
                throw new Exception("Error get id for language '"+TEST_LANGUAGE+"'" );
        }
        finally
        {
            DatabaseManager.close( rs, ps );
            rs = null;
            ps = null;
        }
    }


}
