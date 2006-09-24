/*
 * org.riverock.portlet - Portlet Library
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
package org.riverock.portlet.test.cases;

import org.riverock.generic.db.DatabaseAdapter;


import org.riverock.generic.tools.servlet.HttpServletRequestApplWrapper;
import org.riverock.generic.tools.servlet.HttpServletResponseApplWrapper;
import org.riverock.generic.tools.servlet.HttpSessionApplWrapper;
import org.riverock.generic.schema.config.DatabaseConnectionType;
import org.riverock.generic.config.GenericConfig;
import org.riverock.generic.startup.StartupApplication;
import org.riverock.webmill.container.ContainerConstants;

/**
 * User: Admin
 * Date: May 9, 2003
 * Time: 11:53:17 AM
 *
 * $Id$
 */
@SuppressWarnings({"deprecation"})
public class TestCaseSiteAbstract {
    public DatabaseAdapter db_ = null;
    public HttpServletRequestApplWrapper request = null;
    public HttpServletResponseApplWrapper response = null;
    public HttpSessionApplWrapper session = null;
    public static TestSite testSite = null;
    public String nameConnection = null;

    public void initDatabaseConnection() throws Exception {

        System.out.println( "Start nameConnection with nameConnection "+nameConnection);

        db_ = DatabaseAdapter.getInstance( nameConnection);
        if (db_==null)
            throw new Exception( "DatabaseAdapter not created, nameConnection - '"+nameConnection+"'" );
    }

    protected void testSiteStructure( TestCaseInterface testCase )
        throws Exception
    {
//        CacheArray.reinitFullCache();

        DatabaseConnectionType dbConnConfig = GenericConfig.getDatabaseConnection( nameConnection );
        if (dbConnConfig==null)
            throw new Exception("Connection with name '"+nameConnection+"' not present");

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
        GenericConfig.setDefaultConnectionName( nameConnection );
        testSiteStructure( testCase );
    }

    public void testWithIbmDB2Connection( TestCaseInterface testCase )
        throws Exception
    {
        org.riverock.generic.startup.StartupApplication.init();
        nameConnection = "IBM-DB2";
        testSiteStructure( testCase );
    }


    public void testWithMSSQLConnection( TestCaseInterface testCase )
        throws Exception
    {
        org.riverock.generic.startup.StartupApplication.init();
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
