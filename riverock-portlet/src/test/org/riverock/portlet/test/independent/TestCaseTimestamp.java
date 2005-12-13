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
 * Date: Sep 3, 2003
 * Time: 9:07:51 PM
 *
 * $Id$
 */
package org.riverock.portlet.test.independent;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import org.riverock.common.tools.RsetTools;

import junit.framework.Assert;
import junit.framework.TestCase;

public class TestCaseTimestamp extends TestCase
{
    Connection conn = null;
    TimeZone timeZone = null;
    Long id = new Long(10);

    String checkTimestamp  = "2003-09-04 16:42:58.739";
    String checkTimestampOra  = "2003-09-04 16:42:58";

    public TestCaseTimestamp(String testName)
    {
        super(testName);
    }

    private void initOracle()
            throws Exception
    {
        Class.forName("oracle.jdbc.driver.OracleDriver");

        conn = DriverManager.getConnection
                ("jdbc:oracle:thin:@oracle:1521:MILL", "aaa", "qqq");

        conn.setAutoCommit( false);
    }

    private void initHypersonic()
            throws Exception
    {
        Class.forName("org.hsqldb.jdbcDriver");

        conn = DriverManager.getConnection
                ("jdbc:hsqldb:hsql://localhost", "sa", "");

        conn.setAutoCommit( false);
    }

    public void testOracle()
        throws Exception
    {
        nameTable = "AAA_TEST";
        checkTimestamp = checkTimestampOra;
        setTimeZone();
        initOracle();
        dropAllTables();

        createTestTable("CREATE TABLE "+nameTable+"(T DATE, id DECIMAL)");
        createTestTable("create table \"CASH_CURRENCY\" ( \"ID_CURRENCY\" DECIMAL NOT NULL , \"ID_SITE\" DECIMAL )");
        createTestTable("create table \"CASH_CURR_VALUE\" ( \"ID_CURRENCY\" DECIMAL NOT NULL , \"DATE_CHANGE\" DATE DEFAULT sysdate, \"CURS\" DECIMAL, \"ID_CURVAL\" DECIMAL NOT NULL )");
        insertTestData();

        conn.createStatement().executeUpdate("INSERT INTO \"CASH_CURR_VALUE\" VALUES(134, to_date('2003-09-04 16:42:58', 'yyyy-mm-dd hh24:mi:ss'),1.01,155)");
        conn.createStatement().executeUpdate("INSERT INTO \"CASH_CURR_VALUE\" VALUES(135, to_date('"+checkTimestamp+"', 'yyyy-mm-dd hh24:mi:ss'),34.51,156)");


        doTest();
//        dropTestTable();
    }

    private void checkExceptionTableExistsOracle(SQLException e)
    {
    }
    private void checkExceptionTableExistsHsql(SQLException e)
    {
        Assert.assertFalse("Error code of SQLException is wrong", e.getErrorCode() != -org.hsqldb.Trace.TABLE_ALREADY_EXISTS);
    }

    public void testHypersonic() throws Exception {
        nameTable = "\"AAA_TEST\"";
        setTimeZone();
        initHypersonic();
        dropAllTables();

        createTestTable("CREATE TABLE "+nameTable+" (T timestamp, id DECIMAL)");
        try{
            createTestTable("CREATE TABLE "+nameTable+" (T timestamp, id DECIMAL)");
        }
        catch(SQLException e)
        {
            checkExceptionTableExistsHsql(e);
        }


//        conn.createStatement().execute("create table \"SITE_LIST_SITE\" ( \"ID_SITE\" DECIMAL NOT NULL , \"ID_FIRM\" DECIMAL, \"DEF_LANGUAGE\" VARCHAR NOT NULL , \"DEF_COUNTRY\" VARCHAR NOT NULL , \"DEF_VARIANT\" VARCHAR, \"NAME_SITE\" VARCHAR NOT NULL , \"ADMIN_EMAIL\" VARCHAR, \"IS_CSS_DYNAMIC\" DECIMAL DEFAULT 0 NOT NULL , \"CSS_FILE\" VARCHAR DEFAULT '/front_styles.css', \"IS_REGISTER_ALLOWED\" DECIMAL DEFAULT 1 NOT NULL , \"ORDER_EMAIL\" VARCHAR, \"IS_ACTIVATE_EMAIL_ORDER\" DECIMAL DEFAULT 0 NOT NULL , CONSTRAINT ID_SITE_SLS_PK PRIMARY KEY ( ID_SITE ) )");
//        conn.createStatement().execute("create table \"SITE_VIRTUAL_HOST\" ( \"ID_SITE_VIRTUAL_HOST\" DECIMAL NOT NULL , \"ID_SITE\" DECIMAL NOT NULL , \"NAME_VIRTUAL_HOST\" VARCHAR NOT NULL , CONSTRAINT ID_VIRT_HST_SVH_PK PRIMARY KEY ( ID_SITE_VIRTUAL_HOST ) )");
//        conn.createStatement().execute("create table \"SITE_SUPPORT_LANGUAGE\" ( \"ID_SITE_SUPPORT_LANGUAGE\" DECIMAL NOT NULL , \"ID_SITE\" DECIMAL, \"ID_LANGUAGE\" DECIMAL, \"CUSTOM_LANGUAGE\" VARCHAR, \"NAME_CUSTOM_LANGUAGE\" VARCHAR, CONSTRAINT ID_SITE_LNG_SSL_PK PRIMARY KEY ( ID_SITE_SUPPORT_LANGUAGE ) )");
//        conn.createStatement().execute("create table \"CASH_CURRENCY\" ( \"ID_CURRENCY\" DECIMAL NOT NULL , \"CURRENCY\" VARCHAR, \"IS_USED\" DECIMAL, \"NAME_CURRENCY\" VARCHAR, \"IS_USE_STANDART\" DECIMAL DEFAULT 0, \"ID_STANDART_CURS\" DECIMAL, \"ID_SITE\" DECIMAL, \"PERCENT_VALUE\" DECIMAL, CONSTRAINT PK_CURRENCY PRIMARY KEY ( ID_CURRENCY ) )");
//        conn.createStatement().execute("create table \"CASH_CURR_VALUE\" ( \"ID_CURRENCY\" DECIMAL NOT NULL , \"DATE_CHANGE\" TIMESTAMP DEFAULT sysdate, \"CURS\" DECIMAL, \"ID_CURVAL\" DECIMAL NOT NULL , CONSTRAINT ID_CURVAL_CCV_PK PRIMARY KEY ( ID_CURVAL ) )");
        createTestTable("create table \"CASH_CURRENCY\" ( \"ID_CURRENCY\" DECIMAL NOT NULL , \"ID_SITE\" DECIMAL )");
        createTestTable("create table \"CASH_CURR_VALUE\" ( \"ID_CURRENCY\" DECIMAL NOT NULL , \"DATE_CHANGE\" TIMESTAMP DEFAULT sysdate, \"CURS\" DECIMAL, \"ID_CURVAL\" DECIMAL NOT NULL )");


        insertTestData();
        conn.createStatement().executeUpdate("INSERT INTO \"CASH_CURR_VALUE\" VALUES(134,'2003-09-04 16:42:58.729',1.01,155)");
        conn.createStatement().executeUpdate("INSERT INTO \"CASH_CURR_VALUE\" VALUES(135,'"+checkTimestamp+"',34.51,156)");


        doTest();
        conn.close();
        conn = null;
//        dropTestTable();
    }

    private void dropAllTables()
        throws Exception
    {
        dropTestTable( nameTable );
        dropTestTable( "\"SITE_LIST_SITE\"");
        dropTestTable( "\"SITE_VIRTUAL_HOST\"");
        dropTestTable( "\"SITE_SUPPORT_LANGUAGE\"");
        dropTestTable( "\"CASH_CURRENCY\"");
        dropTestTable( "\"CASH_CURR_VALUE\"");

    }

    private String nameTable = null;

    private void doTest()
        throws Exception
    {

        PreparedStatement ps = conn.prepareStatement(
            "select max(T) T1 from "+nameTable+" where ID=?"
        );

        RsetTools.setLong(ps, 1, id);
        ResultSet rs = ps.executeQuery();
        boolean isRecordFound = rs.next();

        Assert.assertFalse("Record in DB not found", !isRecordFound);

        Timestamp ts = rs.getTimestamp( "T1" );
        ps.close();
        ps = null;
        {
            Assert.assertFalse("Timestamp not found", ts==null);

            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
            df.setTimeZone( timeZone );
            String tsString = df.format( ts );
            String testTsString = df.format( testTS );
            System.out.println("db timestamp "+tsString+ ", test timestamp "+testTsString);

            Assert.assertFalse("Timestamp is wrong", !tsString.equals(testTsString));

        }
        {
            Timestamp cursTs = getCurrentCurs();

            Assert.assertFalse("Timestamp curs not found", cursTs==null);
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
            df.setTimeZone( timeZone );
            String tsString = df.format( cursTs );
            Assert.assertFalse("Timestamp curs is wrong", !tsString.equals(checkTimestamp));

            System.out.println("db timestamp curs "+tsString+ ", test timestamp curs "+checkTimestamp);
        }

    }

    private static Timestamp testTS = new Timestamp(System.currentTimeMillis());

    private void insertTestData()
        throws Exception
    {

//        conn.createStatement().executeUpdate("INSERT INTO \"SITE_LIST_SITE\" VALUES(23,1,'ru','RU',NULL,'\u041f\u0440\u043e\u0431\u043d\u044b\u0439 \u0441\u0430\u0439\u0442',NULL,0,'''/front_styles.css''',1,NULL,0)");
//        conn.createStatement().executeUpdate("INSERT INTO \"SITE_VIRTUAL_HOST\" VALUES(36,23,'test-host')");
//        conn.createStatement().executeUpdate("INSERT INTO \"SITE_SUPPORT_LANGUAGE\" VALUES(115,23,1,'ru_RU','ru_RU')");

//        conn.createStatement().executeUpdate("INSERT INTO \"CASH_CURRENCY\" VALUES(134,'\u0420\u0443\u0431',1,'\u0420\u0443\u0431',0,3,23,0.0)");
//        conn.createStatement().executeUpdate("INSERT INTO \"CASH_CURRENCY\" VALUES(135,'EURO',1,'EURO',0,7,23,0.0)");
        conn.createStatement().executeUpdate("INSERT INTO \"CASH_CURRENCY\" VALUES(134,23)");
        conn.createStatement().executeUpdate("INSERT INTO \"CASH_CURRENCY\" VALUES(135,23)");

        PreparedStatement ps = conn.prepareStatement(
            "insert into "+nameTable+"(T, ID) values (?, ?)"
        );

        ps.setTimestamp(1, testTS);
        RsetTools.setLong(ps, 2, id);
        ps.executeUpdate();
        ps.close();
        ps = null;
        conn.commit();
    }

    private void createTestTable(String sql)
        throws Exception
    {
        Statement ps = conn.createStatement();
        ps.execute( sql );
        ps.close();
        ps = null;
    }

    private void dropTestTable(String nameTableDrop)
        throws Exception
    {
        String sql = "drop table "+nameTableDrop;
        Statement ps = conn.createStatement();
        try
        {
            ps.execute( sql );
        }
        catch(SQLException e){}
        ps.close();
        ps = null;
    }

    private void setTimeZone()
    {
        timeZone = TimeZone.getTimeZone( "Asia/Irkutsk" );
        TimeZone.setDefault( timeZone );
    }

    private Timestamp getCurrentCurs()
        throws Exception
    {
        Long idCurrency = new Long(135);
        Long idSite = new Long(23);

        String sql_ =
            "select max(f.DATE_CHANGE) LAST_DATE " +
            "from  CASH_CURR_VALUE f, CASH_CURRENCY b " +
            "where f.ID_CURRENCY=b.ID_CURRENCY and b.ID_SITE=? and f.ID_CURRENCY=? ";

        PreparedStatement ps = null;
        ResultSet rs = null;
        Timestamp stamp  = null;
        try
        {
            ps = conn.prepareStatement(sql_);
            RsetTools.setLong(ps, 1, idSite);
            RsetTools.setLong(ps, 2, idCurrency);

            rs = ps.executeQuery();

            if (rs.next())
            {
                stamp = rs.getTimestamp("LAST_DATE");
            }
            else
                return null;
        }
        finally
        {
            rs.close();
            ps.close();
            rs = null;
            ps = null;
        }

        System.out.println("ts in db "+stamp);
        if (stamp==null)
            return null;

        try
        {
            SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss.SSS", Locale.ENGLISH);
            df.setTimeZone( timeZone );

            String st = df.format( stamp );
            System.out.println("String ts in db "+st);

        }catch(Throwable th){
            System.out.println("Error get timestamp "+th.toString());
        }

        sql_ =
            "select  a.ID_CURRENCY, a.DATE_CHANGE, a.CURS " +
            "from CASH_CURR_VALUE a, CASH_CURRENCY b " +
            "where a.ID_CURRENCY=b.ID_CURRENCY and " +
            "b.ID_SITE=? and " +
            "a.ID_CURRENCY=? and " +
            "DATE_CHANGE = ?";

        ps = null;
        rs = null;
        double curs;
        Timestamp tsCurs = null;
        long idCurrencyCurs;
        try
        {
            ps = conn.prepareStatement(sql_);
            RsetTools.setLong(ps, 1, idSite);
            RsetTools.setLong(ps, 2, idCurrency);
            ps.setTimestamp(3, stamp);

            rs = ps.executeQuery();

            if (rs.next())
            {

                curs = rs.getDouble("CURS");
                tsCurs = rs.getTimestamp("DATE_CHANGE");
            }
            return tsCurs;
        }
        finally
        {
            rs.close();
            ps.close();
            rs = null;
            ps = null;
        }
    }
}