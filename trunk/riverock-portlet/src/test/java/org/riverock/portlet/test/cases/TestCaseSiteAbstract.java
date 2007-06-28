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
package org.riverock.portlet.test.cases;


import org.riverock.common.tools.servlet.HttpServletRequestApplWrapper;
import org.riverock.common.tools.servlet.HttpServletResponseApplWrapper;
import org.riverock.common.tools.servlet.HttpSessionApplWrapper;
import org.riverock.common.startup.StartupApplication;
import org.riverock.interfaces.ContainerConstants;

/**
 * User: Admin
 * Date: May 9, 2003
 * Time: 11:53:17 AM
 *
 * $Id$
 */
@SuppressWarnings({"deprecation"})
public class TestCaseSiteAbstract {
    public HttpServletRequestApplWrapper request = null;
    public HttpServletResponseApplWrapper response = null;
    public HttpSessionApplWrapper session = null;
    public static TestSite testSite = null;
    public String nameConnection = null;

    public void initDatabaseConnection() throws Exception {

        System.out.println( "Start nameConnection with nameConnection "+nameConnection);

    }

    protected void testSiteStructure( TestCaseInterface testCase )
        throws Exception
    {
//        CacheArray.reinitFullCache();

//        DatabaseConnectionType dbConnConfig = GenericConfig.getDatabaseConnection( nameConnection );
//        if (dbConnConfig==null)
//            throw new Exception("Connection with name '"+nameConnection+"' not present");

        initDatabaseConnection();

//        testSite = new TestSite(db_);

        testCase.insertTestData();
        testCase.doTest();
//        testSite.dropTestSite();
    }

    public void testWithOracleConnection( TestCaseInterface testCase )
        throws Exception
    {
        System.out.println("Start testWithOracleConnection");
        StartupApplication.init();
        nameConnection = "ORACLE";
        testSiteStructure( testCase );
    }

    public void testWithHypersonicConnection( TestCaseInterface testCase )
        throws Exception
    {
        StartupApplication.init();
        nameConnection = "HSQLDB";
        testSiteStructure( testCase );
    }

    public void testWithMySqlConnection( TestCaseInterface testCase )
        throws Exception
    {
        StartupApplication.init();
        nameConnection = "MYSQL";
//        GenericConfig.setDefaultConnectionName( nameConnection );
        testSiteStructure( testCase );
    }

    public void testWithIbmDB2Connection( TestCaseInterface testCase )
        throws Exception
    {
        org.riverock.common.startup.StartupApplication.init();
        nameConnection = "IBM-DB2";
        testSiteStructure( testCase );
    }


    public void testWithMSSQLConnection( TestCaseInterface testCase )
        throws Exception
    {
        org.riverock.common.startup.StartupApplication.init();
        nameConnection = "MSSQL-JTDS";
        testSiteStructure( testCase );
    }

    public void initRequestSession()
        throws Exception
    {
        request = null;
        request = new HttpServletRequestApplWrapper();
        request.setServerName( TestSite.TEST_SERVER_NAME );
        request.setParameter( ContainerConstants.NAME_LANG_PARAM, TestSite.TEST_LANGUAGE  );

        response = null;
        response = new HttpServletResponseApplWrapper();

        session = null;
        session = new HttpSessionApplWrapper();

        request.setSession( session );

    }
}
