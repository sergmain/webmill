/*
 * org.riverock.commerce - Commerce application
 *
 * Copyright (C) 2006, Riverock Software, All Rights Reserved.
 *
 * Riverock - The Open-source Java Development Community
 * http://www.riverock.org
 *
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.riverock.commerce.shop;

import junit.framework.TestCase;

import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.startup.StartupApplication;
import org.riverock.portlet.core.GetWmPriceListWithIdShopList;
import org.riverock.commerce.price.ShopPageParam;
import org.riverock.portlet.schema.import_price.PriceListItemType;
import org.riverock.portlet.schema.import_price.PriceListType;
import org.riverock.portlet.schema.import_price.PricesType;
import org.riverock.portlet.schema.import_price.types.PriceListItemTypeIsLoadType;
import org.riverock.portlet.schema.core.WmPriceListListType;
import org.riverock.commerce.test.cases.TestCaseInterface;
import org.riverock.commerce.test.cases.TestCaseSiteAbstract;
import org.riverock.commerce.test.cases.TestSite;

/**
 * User: Admin
 * Date: May 9, 2003
 * Time: 10:49:57 AM
 *
 * $Id$
 */
public class TestCaseShop extends TestCase implements TestCaseInterface {

    private TestCaseSiteAbstract testAbstract = null;

    public TestCaseShop( String testName ) {
        super( testName );
    }

    public void insertTestData() throws Exception {
        PricesType shopListData = new PricesType();
        PriceListType shopData = new PriceListType();
        shopData.setShopCode( TestSite.shopCode );

        PriceListItemType item1 = new PriceListItemType();
        item1.setCurr( TestSite.nameRUB );
        item1.setIsGroup( Boolean.FALSE );
        item1.setIsLoad( PriceListItemTypeIsLoadType.YES );
        item1.setItemID( new Long( 211 ) );
        item1.setNameItem( "test item " + item1.getItemID() );
        item1.setParentID( new Long( 0 ) );
        item1.setPrice( new Double( 23.34 ) );
        shopData.addItem( item1 );

        PriceListItemType item2 = new PriceListItemType();
        item2.setCurr( TestSite.nameEURO );
        item2.setIsGroup( Boolean.FALSE );
        item2.setIsLoad( PriceListItemTypeIsLoadType.YES );
        item2.setItemID( new Long( 212 ) );
        item2.setNameItem( "test item " + item2.getItemID() );
        item2.setParentID( new Long( 0 ) );
        item2.setPrice( new Double( 4.34 ) );
        shopData.addItem( item2 );

        PriceListItemType item21 = new PriceListItemType();
        item21.setCurr( TestSite.nameEURO );
        item21.setIsGroup( Boolean.FALSE );
        item21.setIsLoad( PriceListItemTypeIsLoadType.NO );
        item21.setItemID( new Long( 212 ) );
        item21.setNameItem( "test item " + item21.getItemID() );
        item21.setParentID( new Long( 0 ) );
        item21.setPrice( new Double( 4.34 ) );
        shopData.addItem( item21 );

        PriceListItemType item3 = new PriceListItemType();
        item3.setCurr( TestSite.nameEURO );
        item3.setIsGroup( Boolean.TRUE );
        item3.setIsLoad( PriceListItemTypeIsLoadType.YES );
        item3.setItemID( new Long( 10 ) );
        item3.setNameItem( "test item " + item3.getItemID() );
        item3.setParentID( new Long( 0 ) );
        item3.setPrice( new Double( 15.34 ) );
        shopData.addItem( item3 );

        PriceListItemType item4 = new PriceListItemType();
        item4.setCurr( TestSite.nameEURO );
        item4.setIsGroup( Boolean.FALSE );
        item4.setIsLoad( PriceListItemTypeIsLoadType.YES );
        item4.setItemID( new Long( 510 ) );
        item4.setNameItem( "test item " + item4.getItemID() );
        item4.setParentID( new Long( 10 ) );
        item4.setPrice( new Double( 11.34 ) );
        shopData.addItem( item4 );
        shopListData.addPriceList( shopData );

        int countItems = 0;
        for( int i = 0; i<shopData.getItemCount(); i++ ) {
            if ( shopData.getItem( i ).getIsLoad().getType() == PriceListItemTypeIsLoadType.YES.getType() )
                countItems++;
        }
        System.out.println( "Count item in import - " + shopData.getItemCount() +
            ", valid count - " + countItems );
        testAbstract.db_.commit();

/*
        byte[] bytes = XmlTools.getXml( shopListData, "Prices", WebmillNamespace.getWebmillNamespace() );
/

        InputSource inSrc = new InputSource( new ByteArrayInputStream( bytes ) );
        PricesType prices = (PricesType)Unmarshaller.unmarshal( PricesType.class, inSrc );

        testAbstract.initRequestSession();
        ImportPriceList.process(prices, TestCaseSiteAbstract.testSite.idSite, testAbstract.db_ );
*/
        testAbstract.db_.commit();

        WmPriceListListType price = GetWmPriceListWithIdShopList.getInstance( testAbstract.db_, TestSite.idShop ).item;
        assertFalse( "Error insert items in price-list, count of items in price-list wrong",
            price.getWmPriceListCount() != countItems );

        ShopPageParam shopParam = new ShopPageParam();
        shopParam.id_currency = TestCaseSiteAbstract.testSite.idCurrencyEURO;
        shopParam.sortBy = "item";
        shopParam.sortDirect = 0;
        shopParam.id_group = new Long( 0 );
        shopParam.id_shop = TestSite.idShop;
        shopParam.isProcessInvoice = true;
        shopParam.idSite = TestCaseSiteAbstract.testSite.idSite;

//        TemplateItemType templateItem = new TemplateItemType();
//        templateItem.setType( TemplateItemTypeTypeType.PORTLET );
//        templateItem.setCode( TestSite.shopCode );
//        templateItem.setValue( "mill.shop" );

        // Todo write test of data which stored in DB after import price-list
/*
        RenderRequest renderRequest = null;
        RenderResponse renderResponse = null;
        ResourceBundle bundle = null;
        ItemListType item = PriceListItemList.getInstance(
            testAbstract.db_, shopParam, renderRequest, renderResponse, bundle );
*/

    }

    public void doTest() throws Exception {
    }

    public void tearDown()
        throws Exception {
        System.out.println( "start tearDown()" );

        if ( testAbstract != null ) {
            if ( testAbstract.db_ != null) {
                testAbstract.db_.commit();
            }
            DatabaseAdapter.close( testAbstract.db_ );
            testAbstract.db_ = null;
        }
    }

    public void testWithOracleConnection()
        throws Exception {
        testAbstract = new TestCaseSiteAbstract();
        testAbstract.testWithOracleConnection( this );
    }

    public void testWithHypersonicConnection()
        throws Exception {
        testAbstract = new TestCaseSiteAbstract();
        testAbstract.testWithHypersonicConnection( this );
    }

    public void testWithMySqlConnection()
        throws Exception {
        testAbstract = new TestCaseSiteAbstract();
        testAbstract.testWithMySqlConnection( this );
    }

    public void testWithIbmDB2Connection()
        throws Exception {
        testAbstract = new TestCaseSiteAbstract();
        testAbstract.testWithIbmDB2Connection( this );
    }

    public void testWithMSSQLConnection()
        throws Exception {
        testAbstract = new TestCaseSiteAbstract();
        testAbstract.testWithMSSQLConnection( this );
    }

    public static void main( String args[] )
        throws Exception {
        StartupApplication.init();

        long id = 1;

        String[][] ns = new String[][]
        {
            {"mill-core", "http://webmill.askmore.info/mill/xsd/mill-core.xsd"}
        };

/*
        XmlTools.writeToFile( resultItem,
            SiteUtils.getTempDir() + "test-WM_PORTAL_VIRTUAL_HOST-item.xml",
            "utf-8",
            null,
            ns );
*/
    }
}
