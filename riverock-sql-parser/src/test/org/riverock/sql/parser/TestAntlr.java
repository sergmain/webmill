/**
 * User: serg_main
 * Date: 06.05.2004
 * Time: 15:57:01
 * @author Serge Maslyukov
 * $Id$
 */

package org.riverock.sql.parser;

import java.io.*;

import antlr.DumpASTVisitor;
import antlr.RecognitionException;
import antlr.TokenStreamException;
import tt.SqlLexer;
import tt.SqlParser;


public class TestAntlr
{
    static String select[] = {
        "select id, id_main, id_forum, header, fio, email, text, " +
        "       id_thread, ip, date_post " +
        "from   main_forum_threads " +
        "where  id_thread=? and level = ? and id_main=? " +
        "START  WITH id_main=0 " +
        "CONNECT BY PRIOR id=id_main " +
        "order  by id asc",

        "select id, id_main, id_forum, header, fio, email, text, " +
        "       id_thread, ip, date_post " +
        "from   main_forum_threads cs " +
        "where  id_thread=11 and id_thread=:1 and level=:2 and id_main=:bind_3 and " +
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

        "select distinct to_number(year) year " +
        "from ( " +
        "    select distinct to_char(date_post,'yyyy') year from main_forum_threads " +
        "    where id_forum = ? " +
        "    union " +
        "    select to_char(sysdate,'yyyy') year from dual " +
        ") " +
        "order by year desc",
        "select * from \"Site_Vitual_Host\" where ID_SITE_VIRTUAL_HOST=?",
        "select ID_SITE_SUPPORT_LANGUAGE from SITE_SUPPORT_LANGUAGE where lower(CUSTOM_LANGUAGE) like 'ru_ru%' and ID_SITE = ?",
        "select ID_LANGUAGE from WM_LIST_LANGUAGE where lower(SHORT_NAME_LANGUAGE) like 'ru_ru%'"
    };

    static String update[] = {
        "update TEST_TABLE_UPDATE " +
        "set " +
        "id = 1 " +
        "where id in (select a.id_test from TEST_SELECT a) "
    };

    static String delete[] = {
        "delete from TEST_TABLE_UPDATE " +
        "where id in (select a.id_test from TEST_SELECT a) "
    };

    static String insert[] = {
        "insert into WM_PRICE_IMPORT_TABLE " +
        "( is_group, id ) " +
        "values " +
        "( ?, 'abc' )",

        "insert into WM_PRICE_IMPORT_TABLE " +
        "( is_group, id ) " +
        "values " +
        "( ?, upper(?) )",

        "insert into test_insert(i,o,p) " +
        "values " +
        "(1,2,3 ) ",

        "insert into test_insert(i,o,p) " +
        "select 1,2,3 from dual ",

        "insert into test_insert(i,o,p) " +
        "(select 1,2,3 from dual) ",

        "insert into test_insert(i,o,p) " +
        "values " +
        "(?, ?, ?, ?) "
    };

    public static void main(String[] args)
        throws Exception
    {

        processSQL(select);
        processSQL(update);
        processSQL(delete);
        processSQL(insert);
    }

    private static void processSQL(String sql[]) throws RecognitionException, TokenStreamException
    {
        for (int i = 0; i < sql.length; i++)
        {
            System.out.println("run parse #" + i);
            SqlLexer lexer = new SqlLexer(new ByteArrayInputStream(sql[i].getBytes()));
            SqlParser parser = new SqlParser(lexer);
            parser.start_rule();
            if (true)
            {
                if (true)
                {
                    System.out.println("");
                    System.out.println("==> Dump of AST <==");
                    DumpASTVisitor visitor = new DumpASTVisitor();
                    visitor.visit(parser.getAST());
                }
            }
        }
    }
}
