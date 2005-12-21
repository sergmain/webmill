/*
 * org.riverock.sql -- Classes for tracking database changes
 * 
 * Copyright (C) 2004, Riverock Software, All Rights Reserved.
 * 
 * Riverock -- The Open-source Java Development Community
 * http://www.riverock.org
 * 
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 *
 */

package org.riverock.sql.parser.test;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

import org.riverock.sql.parser.Parser;
import org.riverock.generic.config.GenericConfig;
import org.riverock.generic.startup.StartupApplication;

import org.exolab.castor.xml.Marshaller;

/**
 * Author: mill
 * Date: Apr 29, 2003
 * Time: 10:13:18 AM
 *
 * $Id$
 */
public class TestParser
{
//    private static Logger log = Logger.getLogger( "org.riverock.sql.parser.test.TestParser" );

    public TestParser()
    {
    }

    public static void writeToFile(Object obj, String fileName  )
        throws Exception
    {
        String encoding = "utf-8";
        FileOutputStream fos = new FileOutputStream( fileName );
        Marshaller marsh = new Marshaller( new OutputStreamWriter(fos, encoding) );

        marsh.setValidation( false );
        marsh.setMarshalAsDocument(true);
        marsh.setEncoding(encoding);

        marsh.marshal(obj);
        fos.flush();
        fos.close();
        fos = null;
    }

    private static Parser doProcess(String sql, String nameFile, String[] tables )
        throws Exception
    {
        Parser parser = null;
        parser = Parser.getInstance( sql );
        Object obj = null;
        switch ( parser.typeStatement )
        {
            case Parser.SELECT:
                obj = parser.select;
                break;
            case Parser.INSERT:
                obj = parser.insert;
                break;
                case Parser.UPDATE:
                obj = parser.update;
                break;
            case Parser.DELETE:
                obj = parser.delete;
                break;
        }
        writeToFile(obj, GenericConfig.getGenericDebugDir()+nameFile);
        System.out.println("object "+obj);
        return parser;
    }

    public static void main(String[] args)
        throws Exception
    {
        StartupApplication.init();

        testSelect();
        testInsert();
//        testUpdate();
//        testDelete();

    }

    private static void testUpdate()
        throws Exception
    {
        doProcess(
            "update TEST_TABLE_UPDATE " +
            "set " +
            "id = 1 " +
            "where id in (select a.id_test from TEST_SELECT a) ",
            "parser-update-1.xml",
            new String[]{""} );
    }

    private static void testDelete()
        throws Exception
    {
        doProcess(
            "delete from TEST_TABLE_UPDATE " +
            "where id in (select a.id_test from TEST_SELECT a) ",
            "parser-delete-1.xml",
            new String[]{""} );
    }

    private static void testInsert()
        throws Exception
    {

        doProcess(
            "insert into WM_PRICE_IMPORT_TABLE " +
            "( is_group, id ) " +
            "values " +
            "( ?, 'abc' )",
            "parser-insert-01.xml",
            new String[]{""} );

        doProcess(
            "insert into WM_PRICE_IMPORT_TABLE " +
            "( is_group, id ) " +
            "values " +
            "( ?, upper(?) )",
            "parser-insert-0.xml",
            new String[]{""} );

        doProcess(
            "insert into test_insert(i,o,p) " +
            "values " +
            "(1,2,3 ) ",
            "parser-insert-1.xml",
            new String[]{""} );

        doProcess(
            "insert into test_insert(i,o,p) " +
            "select 1,2,3 from dual ",
            "parser-insert-2.xml",
            new String[]{""} );

        doProcess(
            "insert into test_insert(i,o,p) " +
            "(select 1,2,3 from dual) ",
            "parser-insert-3.xml",
            new String[]{""} );

        doProcess(
            "insert into test_insert(i,o,p) " +
            "values " +
            "(?, ?, ?, ?) ",
            "parser-insert-4.xml",
            new String[]{""} );
    }

    private static void testSelect()
        throws Exception
    {
        doProcess(
            "select b.id_firm id, concat(b.id_firm, ', ', b.full_name) NAME_FIRM "+
            "from   WM_LIST_COMPANY b "+
            "where  b.ID_FIRM in (10,12) and b.is_deleted=0 "+
            "order  by b.ID_FIRM ASC ",
            "parser-select-22.xml",
            new String[]{"WM_LIST_COMPANY"}
    );
/*
        doProcess(
            "select distinct a.*, b.count_field  " +
            "from test_table a, " +
            "(select count(*) count_field from test_table_1 where id=1) b " +
            "group by b.count_field",
            "parser-select-1.xml"
        );

        Parser parser = doProcess(
            "select a.*, b.count_field, (select distinct id from dual) id_result  " +
            "from test_table a, " +
            "(select count(*) count_field from test_table_1 where id=1) a " +
            "where count_field in (select count(*) count_field from test_table_11 where id=1) " +
            "and count_field in (select count(*) count_field from test_table_1 where id=1) "+
            "and count_field in (select count(*) count_field from test_table_13 where id=1)",
            "parser-select-2.xml"
        );
//        writeToFile( parser.depend, InitParam.getMillDebugDir()+"parser-source-table.xml");

        doProcess(
            "select a.*, b.count_field, (select distinct id from dual) id_result  " +
            "from test_table a, \"test table\" b " +
            "where a.id=b.id and b.id in (select c.id from test_c) " +
            "order by b.count_field DESC ",
            "parser-select-3.xml"
        );

        parser = doProcess(
            "select * from SITE_VIRTUAL_HOST where ID_SITE_VIRTUAL_HOST=?",
            "parser-select-4.xml"
        );
        parser = doProcess(
            "select ID_LANGUAGE from WM_LIST_LANGUAGE where lower(SHORT_NAME_LANGUAGE) like 'ru_ru%'",
            "parser-select-5.xml"
        );
        parser = doProcess(
            "select ID_SITE_SUPPORT_LANGUAGE from SITE_SUPPORT_LANGUAGE where lower(CUSTOM_LANGUAGE) like 'ru_ru%' and ID_SITE = ?",
            "parser-select-6.xml"
        );
        parser = doProcess(
            "select * from \"Site_Vitual_Host\" where ID_SITE_VIRTUAL_HOST=?",
            "parser-select-7.xml"
        );

        doProcess(
            "select distinct to_number(year) year " +
            "from ( " +
            "    select distinct to_char(date_post,'yyyy') year from main_forum_threads " +
            "    where id_forum = ? " +
            "    union " +
            "    select to_char(sysdate,'yyyy') year from dual " +
            ") " +
            "order by year desc",
            "parser-select-8.xml"
        );
        doProcess(
            "select  distinct trunc(month, 'mm') dat, to_number(to_char(month,'mm')) month " +
            "from ( " +
            "    select distinct trunc(date_post,'dd') month " +
            "    from main_forum_threads " +
            "    where id_forum = ? and to_char(date_post,'yyyy')=to_char(?) " +
            "    union " +
            "    select trunc(sysdate,'dd') month " +
            "    from dual " +
            "    where to_char(sysdate,'yyyy')=to_char(?) " +
            ") " +
            "order by month desc",
            "parser-select-9.xml"
        );
        doProcess(
            "select  distinct  rsh.id_x_reper  " +
            "from    x_reper_shoot  rsh,  x_point_coords  pc,  " +
            "x_coord_sk  cs,  x_reper_category  rc,  x_reper  r  " +
            "where  rsh.id_x_point=pc.id_x_point  and  " +
            "pc.id_x_coord_sk=cs.id_x_coord_sk  and  " +
            "cs.id_distance  in  " +
            "(" +
            "    SELECT id_distance  " +
            "    FROM   prr_distance  d,  prr_road_direction  rd  " +
            "    WHERE  d.id_road_direction  =  rd.id_road_direction  and  " +
            "           rc.id_reper_category=rsh.id_x_reper_category  and  " +
            "           r.id_x_reper=rsh.id_x_reper  AND  " +
            "           rd.id_road=?  AND  " +
            "           rd.id_direction=?" +
            ")  and  " +
            "pc.km  between  ?  and  ?  and  " +
            "r.ter_date  is  null  and  " +
            "rc.id_reper_category  in  (7,  8,  9,  10)",
            "parser-select-9.xml"
        );


        doProcess(
            "select id, id_main, id_forum, header, fio, email, text, " +
            "       id_thread, ip, date_post " +
            "from   main_forum_threads " +
            "where  id_thread=? and level = ? and id_main=? " +
            "START  WITH id_main=0 " +
            "CONNECT BY PRIOR id=id_main " +
            "order  by id asc",
            "parser-select-10.xml"
        );

        doProcess(
            "select id, id_main, id_forum, header, fio, email, text, " +
            "       id_thread, ip, date_post " +
            "from   main_forum_threads cs " +
            "where  id_thread=11 and id_thread=:1 and level=:2 and id_main=:bind_3 and "+
            "cs.id_main  in  " +
            "(" +
            "    SELECT id_distance  " +
            "    FROM   prr_distance  d,  prr_road_direction  rd  " +
            "    WHERE  d.id_road_direction  =  rd.id_road_direction  and  " +
            "           rc.id_reper_category=rsh.id_x_reper_category  and  " +
            "           r.id_x_reper=rsh.id_x_reper  AND  " +
            "           rd.id_road=?  AND  " +
            "           rd.id_direction=?" +
            ") ",
            "parser-select-11.xml",
            new String[]{""} );

        doProcess(
            "SELECT DISTINCT (street_address), (postal_code), (city) " +
            "FROM " +
            "     locations NATURAL_JOIN warehouse " +
            "               NATURAL_JOIN inventories " +
            "               NATURAL_JOIN products " +
            "               NATURAL_JOIN order_items " +
            "               NATURAL_JOIN orders " +
            "where  order_status = 10 ",
            "parser-select-12.xml",
            new String[]{
                "locations",
                "warehouse",
                "inventories",
                "products",
                "order_items",
                "orders"
            }
        );
*/
        doProcess(
            "select * from dept partition(id)",
            "parser-select-13.xml",
            new String[]{
                "dept"
            }
        );
    }

}
