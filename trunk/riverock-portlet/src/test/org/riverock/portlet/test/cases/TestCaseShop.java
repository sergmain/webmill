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
 * User: Admin
 * Date: May 9, 2003
 * Time: 10:49:57 AM
 *
 * $Id$
 */
package org.riverock.portlet.test.cases;

import java.io.ByteArrayInputStream;

import org.exolab.castor.xml.Unmarshaller;
import org.xml.sax.InputSource;

import junit.framework.TestCase;
import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.portlet.core.GetPriceListWithIdShopList;
import org.riverock.portlet.core.GetSiteVirtualHostItem;
import org.riverock.portlet.price.PriceListItemList;
import org.riverock.portlet.schema.core.PriceListListType;
import org.riverock.portlet.schema.core.SiteVirtualHostItemType;
import org.riverock.portlet.schema.import_price.PriceListItemType;
import org.riverock.portlet.schema.import_price.PriceListType;
import org.riverock.portlet.schema.import_price.PricesType;
import org.riverock.portlet.schema.import_price.types.PriceListItemTypeIsLoadType;
import org.riverock.portlet.schema.portlet.shop.ItemListType;

import org.riverock.generic.tools.XmlTools;
import org.riverock.generic.tools.StringManager;
import org.riverock.generic.startup.StartupApplication;
import org.riverock.webmill.portlet.PortletParameter;
import org.riverock.webmill.portlet.CtxInstance;
import org.riverock.webmill.site.WebmillNamespace;
import org.riverock.webmill.schema.site.TemplateItemType;
import org.riverock.webmill.schema.site.types.TemplateItemTypeTypeType;
import org.riverock.webmill.config.WebmillConfig;
import org.riverock.portlet.portlets.ShopPageParam;

public class TestCaseShop extends TestCase implements TestCaseInterface
{

    private TestCaseSiteAbstract testAbstract = null;

    public TestCaseShop(String testName)
    {
        super(testName);
    }

    public void insertTestData() throws Exception
    {
        PricesType shopListData = new PricesType();
        PriceListType shopData = new  PriceListType();
        shopData.setShopCode(TestSite.shopCode);

        PriceListItemType item1 = new PriceListItemType();
        item1.setCurr( TestSite.nameRUB );
        item1.setIsGroup( Boolean.FALSE );
        item1.setIsLoad( PriceListItemTypeIsLoadType.YES );
        item1.setItemID( new Long(211) );
        item1.setNameItem( "test item "+item1.getItemID() );
        item1.setParentID( new Long(0) );
        item1.setPrice( new Double(23.34) );
        shopData.addItem( item1 );

        PriceListItemType item2 = new PriceListItemType();
        item2.setCurr( TestSite.nameEURO );
        item2.setIsGroup( Boolean.FALSE );
        item2.setIsLoad( PriceListItemTypeIsLoadType.YES );
        item2.setItemID( new Long(212) );
        item2.setNameItem( "test item "+item2.getItemID() );
        item2.setParentID( new Long(0) );
        item2.setPrice( new Double(4.34) );
        shopData.addItem( item2 );

        PriceListItemType item21 = new PriceListItemType();
        item21.setCurr( TestSite.nameEURO );
        item21.setIsGroup( Boolean.FALSE );
        item21.setIsLoad( PriceListItemTypeIsLoadType.NO );
        item21.setItemID( new Long(212) );
        item21.setNameItem( "test item "+item21.getItemID() );
        item21.setParentID( new Long(0) );
        item21.setPrice( new Double(4.34) );
        shopData.addItem( item21 );

        PriceListItemType item3 = new PriceListItemType();
        item3.setCurr( TestSite.nameEURO );
        item3.setIsGroup( Boolean.TRUE );
        item3.setIsLoad( PriceListItemTypeIsLoadType.YES );
        item3.setItemID( new Long(10) );
        item3.setNameItem( "test item "+item3.getItemID() );
        item3.setParentID( new Long(0) );
        item3.setPrice( new Double(15.34) );
        shopData.addItem( item3 );

        PriceListItemType item4 = new PriceListItemType();
        item4.setCurr( TestSite.nameEURO );
        item4.setIsGroup( Boolean.FALSE );
        item4.setIsLoad( PriceListItemTypeIsLoadType.YES );
        item4.setItemID( new Long(510) );
        item4.setNameItem( "test item "+item4.getItemID() );
        item4.setParentID( new Long(10) );
        item4.setPrice( new Double(11.34) );
        shopData.addItem( item4 );
        shopListData.addPriceList( shopData );

        int countItems=0;
        for (int i=0; i<shopData.getItemCount(); i++)
        {
            if (shopData.getItem(i).getIsLoad().getType()==PriceListItemTypeIsLoadType.YES.getType())
                countItems++;
        }
        System.out.println("Count item in import - "+shopData.getItemCount()+
            ", valid count - "+countItems);
        testAbstract.db_.commit();

        byte[] bytes = XmlTools.getXml(shopListData, "Prices", WebmillNamespace.getWebmillNamespace());

        InputSource inSrc = new InputSource( new ByteArrayInputStream(bytes) );
        PricesType prices = (PricesType) Unmarshaller.unmarshal(PricesType.class, inSrc);
        testAbstract.initRequestSession();
//        ImportPriceList.process(prices, testAbstract.jspPage.p.sites.getIdSite());

        testAbstract.db_.commit();

        PriceListListType price = GetPriceListWithIdShopList.getInstance(testAbstract.db_, TestSite.idShop).item;
        assertFalse("Error insert items in price-list, count of items in price-list wrong",
            price.getPriceListCount()!=countItems
        );

        ShopPageParam shopParam = new ShopPageParam();
        shopParam.id_currency = TestCaseSiteAbstract.testSite.idCurrencyEURO;
        shopParam.sm = new StringManager();
        shopParam.sortBy = "item";
        shopParam.sortDirect = 0;
        shopParam.id_group = new Long(0);
        shopParam.id_shop = TestSite.idShop;
        shopParam.isProcessInvoice = true;
        shopParam.idSite = TestCaseSiteAbstract.testSite.idSite;
        shopParam.nameTemplate = "name_template";

        CtxInstance ctxInstance = new CtxInstance(testAbstract.request, testAbstract.response, testAbstract.db_);


        TemplateItemType templateItem = new TemplateItemType();
        templateItem.setType(TemplateItemTypeTypeType.PORTLET );
        templateItem.setCode( TestSite.shopCode );
        templateItem.setValue( "mill.shop" );

        PortletParameter param = new PortletParameter(ctxInstance, null, templateItem) ;

        ItemListType item = PriceListItemList.getInstance(testAbstract.db_, shopParam, param);


    }

    public void doTest() throws Exception
    {
    }

    public void tearDown()
        throws Exception
    {
        System.out.println("start tearDown()");

        if (testAbstract!=null)
        {
            if (testAbstract.db_!=null && testAbstract.db_.conn != null)
            {
                testAbstract.db_.commit();
            }
            DatabaseAdapter.close( testAbstract.db_);
            testAbstract.db_ = null;
        }
    }

    public void testWithOracleConnection()
        throws Exception
    {
        testAbstract = new TestCaseSiteAbstract();
        testAbstract.testWithOracleConnection( this );
    }

    public void testWithHypersonicConnection()
        throws Exception
    {
        testAbstract = new TestCaseSiteAbstract();
        testAbstract.testWithHypersonicConnection( this );
    }

    public void testWithMySqlConnection()
        throws Exception
    {
        testAbstract = new TestCaseSiteAbstract();
        testAbstract.testWithMySqlConnection( this );
    }

    public void testWithIbmDB2Connection()
        throws Exception
    {
        testAbstract = new TestCaseSiteAbstract();
        testAbstract.testWithIbmDB2Connection( this );
    }

    public void testWithMSSQLConnection()
        throws Exception
    {
        testAbstract = new TestCaseSiteAbstract();
        testAbstract.testWithMSSQLConnection( this );
    }

    public static void main(String args[])
        throws Exception
    {
        StartupApplication.init();

        long id = 1;
        SiteVirtualHostItemType resultItem = GetSiteVirtualHostItem.getInstance( DatabaseAdapter.getInstance( false ), id ).item;

        String[][] ns = new String[][]
        {
            { "mill-core", "http://webmill.askmore.info/mill/xsd/mill-core.xsd" }
        };

        XmlTools.writeToFile(
            resultItem,
            WebmillConfig.getWebmillDebugDir()+"test-site_virtual_host-item.xml",
            "utf-8",
            null,
            ns
        );
    }
}
