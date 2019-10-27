/*
 * org.riverock.commerce - Commerce application
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
package org.riverock.commerce.shop;

import java.math.BigDecimal;

import junit.framework.TestCase;

import org.riverock.common.startup.StartupApplication;
import org.riverock.commerce.schema.import_price.PriceListItemType;
import org.riverock.commerce.schema.import_price.PriceListType;
import org.riverock.commerce.schema.import_price.PricesType;

/**
 * User: Admin
 * Date: May 9, 2003
 * Time: 10:49:57 AM
 *
 * $Id: TestCaseShop.java 1148 2006-12-19 22:25:11Z serg_main $
 */
public class TestCaseShop extends TestCase {

    public final static String TEST_SERVER_NAME = "test-host";
    public final static String TEST_LANGUAGE = "ru_RU";
    public final static String NAME_TEST_SITE = "Пробный сайт";

    public final static String NAME_DEFAULT_MENU = "DEFAULT_MENU";

    public final static String indexPortletDefinition = "mill.index";

    public Long idSite;
    public Long idVirtualHost;
    public Long idRuSiteLanguage;
    public Long idRuLanguage;
    public Long idRuLangCatalog;

    Long idTemplateIndex;
    String nameIndexTemplate = "index_template";
    Long idTemplateDynamic;
    String nameDynamicTemplate = "dynamic_template";

    public Long idCurrencyEURO = null;
    public static Long idCurrencyStdEURO = null;
    public static final String nameEURO = "EURO";
    public static Double cursEURO = 34.51;


    public Long idCurrencyRUB = null;
    public static final String nameRUB = "Руб";
    public static Long idCurrencyStdRUB = null;
    public static Double cursRUB = 1.01;

    public final static int COUNT_TOP_LEVEL_MENU = 4;
    public final static int COUNT_SUB_MENU = 3;
    public final static String menuItem = "Menu item ";

    public static String shopCode = null;
    public static Long idShop = null;
    private static final String YES_VALUE = "YES";
    private static final String NO_VALUE = "NO";

    public TestCaseShop( String testName ) {
        super( testName );
    }

    private void insertTestData() throws Exception {
        PricesType shopListData = new PricesType();
        PriceListType shopData = new PriceListType();
        shopData.setShopCode( shopCode );

        PriceListItemType item1 = new PriceListItemType();
        item1.setCurr( nameRUB );
        item1.setIsGroup( Boolean.FALSE );
        item1.setIsLoad(YES_VALUE);
        item1.setItemID( new Long( 211 ) );
        item1.setNameItem( "test item " + item1.getItemID() );
        item1.setParentID( new Long( 0 ) );
        item1.setPrice( new BigDecimal( 23.34 ) );
        shopData.getItem().add( item1 );

        PriceListItemType item2 = new PriceListItemType();
        item2.setCurr( nameEURO );
        item2.setIsGroup( Boolean.FALSE );
        item2.setIsLoad(YES_VALUE);
        item2.setItemID( new Long( 212 ) );
        item2.setNameItem( "test item " + item2.getItemID() );
        item2.setParentID( new Long( 0 ) );
        item2.setPrice( new BigDecimal( 4.34 ) );
        shopData.getItem().add( item2 );

        PriceListItemType item21 = new PriceListItemType();
        item21.setCurr( nameEURO );
        item21.setIsGroup( Boolean.FALSE );
        item21.setIsLoad(NO_VALUE);
        item21.setItemID( new Long( 212 ) );
        item21.setNameItem( "test item " + item21.getItemID() );
        item21.setParentID( new Long( 0 ) );
        item21.setPrice( new BigDecimal( 4.34 ) );
        shopData.getItem().add( item21 );

        PriceListItemType item3 = new PriceListItemType();
        item3.setCurr( nameEURO );
        item3.setIsGroup( Boolean.TRUE );
        item3.setIsLoad(YES_VALUE);
        item3.setItemID( new Long( 10 ) );
        item3.setNameItem( "test item " + item3.getItemID() );
        item3.setParentID( new Long( 0 ) );
        item3.setPrice( new BigDecimal( 15.34 ) );
        shopData.getItem().add( item3 );

        PriceListItemType item4 = new PriceListItemType();
        item4.setCurr( nameEURO );
        item4.setIsGroup( Boolean.FALSE );
        item4.setIsLoad(YES_VALUE);
        item4.setItemID( new Long( 510 ) );
        item4.setNameItem( "test item " + item4.getItemID() );
        item4.setParentID( new Long( 10 ) );
        item4.setPrice( new BigDecimal( 11.34 ) );
        shopData.getItem().add( item4 );
        shopListData.getPriceList().add( shopData );

        int countItems = 0;
        for (PriceListItemType priceListItemType : shopData.getItem()) {
            if ( priceListItemType.getIsLoad().equals(YES_VALUE) )
                countItems++;
        }
        System.out.println( "Count item in import - " + shopData.getItem().size() +
            ", valid count - " + countItems );
//        testAbstract.db_.commit();

/*
        byte[] bytes = XmlTools.getXml( shopListData, "Prices", WebmillNamespace.getWebmillNamespace() );
/

        InputSource inSrc = new InputSource( new ByteArrayInputStream( bytes ) );
        PricesType prices = (PricesType)Unmarshaller.unmarshal( PricesType.class, inSrc );

        testAbstract.initRequestSession();
        ImportPriceList.process(prices, idSite, testAbstract.db_ );
*/
//        testAbstract.db_.commit();

//        WmPriceListListType price = GetWmPriceListWithIdShopList.getInstance( testAbstract.db_, idShop ).item;
//        assertFalse( "Error insert items in price-list, count of items in price-list wrong",
//            price.getWmPriceListCount() != countItems );

/*
        ShopPageParam shopParam = new ShopPageParam();
        shopParam.id_currency = idCurrencyEURO;
        shopParam.sortBy = "item";
        shopParam.sortDirect = 0;
        shopParam.id_group = new Long( 0 );
        shopParam.id_shop = idShop;
        shopParam.isProcessInvoice = true;
        shopParam.idSite = idSite;
*/

//        TemplateItemType templateItem = new TemplateItemType();
//        templateItem.setType( TemplateItemTypeTypeType.PORTLET );
//        templateItem.setCode( shopCode );
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

    public void testStub() {
        
    }

    public void doTest() throws Exception {
    }

/*
    public void tearDown()
        throws Exception {
        System.out.println( "start tearDown()" );
    }

*/

    public static void main( String args[] )
        throws Exception {
        StartupApplication.init();

    }
}
