/*

 * org.riverock.cache -- Generic cache implementation

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

 * Author: mill

 * Date: Apr 30, 2003

 * Time: 1:45:46 PM

 *

 * $Id$

 */



package org.riverock.cache.impl;



import junit.framework.Assert;

import junit.framework.TestCase;

import org.riverock.main.core.InsertSiteVirtualHostItem;

import org.riverock.main.core.UpdateCashCurrencyItem;

import org.riverock.port.PortalInfo;

import org.riverock.schema.core.CashCurrencyItemType;

import org.riverock.schema.core.SiteVirtualHostItemType;

import org.riverock.schema.db.CustomSequenceType;

import org.riverock.schema.price.CustomCurrencyItemType;

import org.riverock.test.cases.TestCaseInterface;

import org.riverock.test.cases.TestCaseSiteAbstract;

import org.riverock.generic.db.DatabaseAdapter;



public class TestCacheClass extends TestCase implements TestCaseInterface

{

//    private static Logger log = Logger.getLogger( "org.riverock.test.cache.TestCacheClass" );



    private TestCaseSiteAbstract testAbstract = null;

    private long idVirtualHost;



    public TestCacheClass(String testName)

    {

        super(testName);

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



    public void doTest()

        throws Exception

    {

        SiteVirtualHostItemType item = GetTestVirtualHostItem.getInstance(testAbstract.db_, idVirtualHost).item;

        System.out.println("host1 - "+item.getNameVirtualHost());

        SiteVirtualHostItemType itemNew = new SiteVirtualHostItemType();

        GetTestVirtualHostItem.copyItem(item, itemNew);

        itemNew.setNameVirtualHost( itemNew.getNameVirtualHost()+"-1" );

        UpdateTestSiteVirtualHostItem.processData( testAbstract.db_, itemNew );



        PortalInfo p = PortalInfo.getInstance(testAbstract.db_, itemNew.getNameVirtualHost() );

        System.out.println("PortalInfo.xslt - "+p.xsltList);

        System.out.println("PortalInfo.curr - "+p.currencyList.getCurrencyListCount());

        if (p.currencyList.getCurrencyListCount()>1)

        {

            CustomCurrencyItemType currItem = p.currencyList.getCurrencyList(0);

            CashCurrencyItemType currItemNew = new CashCurrencyItemType();



            currItemNew.setCurrency(currItem.getCurrencyCode());

            currItemNew.setIdCurrency(currItem.getIdCurrency());

            currItemNew.setIdSite(currItem.getIdSite());

            currItemNew.setIdStandartCurs(currItem.getIdStandardCurrency());

            currItemNew.setIsUsed(currItem.getIsUsed());

            currItemNew.setIsUseStandart(currItem.getIsUseStandardCurrency());

            currItemNew.setNameCurrency(currItem.getCurrencyName()+"-1");

            currItemNew.setPercentValue(currItem.getPercent());



            testAbstract.db_.commit();

            UpdateCashCurrencyItem.processData(testAbstract.db_, currItemNew);



            PortalInfo p1= PortalInfo.getInstance(testAbstract.db_, itemNew.getNameVirtualHost());

            currItem = p1.currencyList.getCurrencyList(0);

            System.out.println("currItem.name 1 "+currItem.getCurrencyName());

            testAbstract.db_.commit();

            PortalInfo p2= PortalInfo.getInstance(testAbstract.db_, itemNew.getNameVirtualHost());

            currItem = p2.currencyList.getCurrencyList(0);

            System.out.println("currItem.name 2 "+currItem.getCurrencyName());



        }



        SiteVirtualHostItemType item1 = GetTestVirtualHostItem.getInstance( testAbstract.db_, idVirtualHost ).item;

        System.out.println("host2 - "+item1.getNameVirtualHost());

        testAbstract.db_.commit();

        SiteVirtualHostItemType item2 = GetTestVirtualHostItem.getInstance( testAbstract.db_, idVirtualHost ).item;

        System.out.println("host3 - "+item2.getNameVirtualHost());

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

        testAbstract.testWithOracleConnection( this );

    }



    public void insertTestData()

        throws Exception

    {

        CustomSequenceType seqVirtualHost = new CustomSequenceType();

        seqVirtualHost.setSequenceName("SEQ_SITE_VIRTUAL_HOST");

        seqVirtualHost.setTableName( "SITE_VIRTUAL_HOST");

        seqVirtualHost.setColumnName( "ID_SITE_VIRTUAL_HOST" );



        idVirtualHost = testAbstract.db_.getSequenceNextValue( seqVirtualHost );



        Assert.assertFalse("Error get new value of sequence for table "+seqVirtualHost.getTableName(), idVirtualHost==-1);



        SiteVirtualHostItemType hostItem = new SiteVirtualHostItemType();

        hostItem.setIdSiteVirtualHost( idVirtualHost );

        hostItem.setIdSite( testAbstract.testSite.idSite );

        hostItem.setNameVirtualHost( testAbstract.testSite.TEST_SERVER_NAME+"-1" );

        Long countRec = InsertSiteVirtualHostItem.processData( testAbstract.db_, hostItem );



    }



//    public static void main(String[] args)

//    throws Exception

//    {

//        org.riverock.generic.startup.StartupApplication.init();

//        DBconnect db_ = DBconnect.getInstance(false);

//

//        SiteService.dropSite( db_, NAME_TEST_SITE, TEST_SERVER_NAME );

//    }



}

