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
package org.riverock.portlet.shop.search;

import java.io.IOException;
import java.io.Writer;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.portlet.RenderRequest;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.riverock.common.tools.ExceptionTools;
import org.riverock.common.tools.NumberTools;
import org.riverock.common.tools.RsetTools;
import org.riverock.common.tools.StringTools;
import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.schema.db.CustomSequenceType;
import org.riverock.portlet.tools.ContentTypeTools;
import org.riverock.portlet.tools.RequestTools;
import org.riverock.webmill.container.tools.PortletService;


/**
 * Author: mill
 * Date: Dec 3, 2002
 * Time: 3:15:26 PM
 *
 * $Id$
 */
public class ShopSearch extends HttpServlet
{
    private static Log log = LogFactory.getLog(ShopSearch.class);

    class ParseException extends Exception
    {
    }

    private double to_digit(String s_)
            throws Exception
    {
        return to_digit(s_, false, 0, 0);
    }

    private double to_digit
            (String s_,
             boolean is_comma, //,  -- нужна ли точка в числе
             int precision, //in number default 0,      -- количество знаков после запятой
             double default_return// in number default 0
             )
            throws Exception
    {
        try
        {
            if (StringTools.isEmpty(s_))
                return default_return;

            double d = Double.parseDouble(replcd(s_));

            if (log.isDebugEnabled())
            {
                log.debug("" + d);
                log.debug(s_);
            }

            if (is_comma)
            {
                if (log.isDebugEnabled())
                    log.debug("is comma");

                d = NumberTools.truncate(d, precision);

                if (log.isDebugEnabled())
                    log.debug("new double - " + d + " precision " + precision);

                return d;
            }
            else
                return NumberTools.truncate(d, 0);

        }
        catch (Exception e)
        {
            return default_return;
        }
    }

    private String replcd(String s_)
    {
        return StringTools.replaceString(s_, ",", ".");
    };

    private String decode_logic(String s_)
            throws Exception
    {
        double v_i;

        v_i = to_digit(s_);

        if (v_i == 1)
            return " and ";
        else if (v_i == 2)
            return " or ";
        else if (v_i == 3)
            return " and not ";

        return " and ";

    }

    private int get_length(String s_)
    {
        if (s_ == null)
            return 0;

        return s_.length();
    }

    private String process_request(
            String s, //       in string default '', -- строка поиска
            String s1, //      in string default '', -- строка поиска
            String s2, //      in string default '', -- строка поиска
            String bool1, //   in string default '', -- строка поиска
            String bool2, //   in string default '', -- строка поиска
            String minp, //    in string default '',
            String maxp //    in string default '',
            )
            throws Exception
    {
        String out_str; // out string
        String v_str = ""; //    varchar2(1000) := '';
        String v_result = ""; //     varchar2(1000)  := '';
        String v_bool; //   varchar2(15);
        boolean v_flag = true;
        boolean v_multi = false; // -- Участвует ли в выражении больше 1 условия
        boolean v_df;
        String v_max = ""; //    varchar2(30);
        String v_min = ""; //   varchar2(30);
        int v_min_symbol = 1; //     integer := 1;

        try
        {
            if (minp == null)
                minp = "";

            if (maxp == null)
                maxp = "";

//        -- Проверка, что бы "мин" был меньше "макс"
            if ((minp.length() != 0) && (maxp.length() != 0))
            {
                if (to_digit(minp) > to_digit(maxp))
                {
                    v_min = maxp;
                    v_max = minp;
                }
                else
                {
                    v_min = minp;
                    v_max = maxp;
                }
            }

            if ((get_length(s) > v_min_symbol) || (get_length(s1) > v_min_symbol) || (get_length(s2) > v_min_symbol))
            {
                if (get_length(s) > v_min_symbol)
                {
                    v_result = s;
                    v_str = " ( UPPER(a.item) like ''%' ||UPPER(s)||'%'' ) ";
                    v_flag = false;
                }
                if (to_digit(bool1) == 0)
                {
                    throw new ParseException();
                }
                if (get_length(s1) > v_min_symbol)
                {
                    if (!v_flag)
                    {
                        v_bool = decode_logic(bool1);
                        v_str += v_bool;
                        v_result += v_bool;
                    }
                    v_flag = false;
                    v_multi = true;
                    v_result += s1;
                    v_str += " (UPPER(a.item) like ''%' ||UPPER(s1)||'%'') ";
                }
                if (to_digit(bool2) == 0)
                {
                    throw new ParseException();
                }
                if (get_length(s2) > v_min_symbol)
                {
                    if (!v_flag)
                    {
                        v_bool = decode_logic(bool2);
                        v_str += v_bool;
                        v_result += v_bool;
                    }
                    v_flag = false;
                    v_result += s2;
                    v_str += " (UPPER(a.item) like ''%' ||UPPER(s2)||'%'') ";
                }

            }
            throw new ParseException();

        }
        catch (ParseException e)
        {
            if (get_length(v_str) == 0)
                return "";

            if ((get_length(minp) != 0) || (get_length(maxp) != 0))
            {
                if (get_length(v_min) != 0)
                {
                    if (!v_flag)
                    {
                        v_str += " and (";
                        v_result += " and (";
                    }
                    v_flag = false;
                    v_multi = true;
                    v_result = v_result + " price >= " + v_min;
                    v_str = v_str + " price >= " + to_digit(v_min) + " ";
                }

                if (get_length(v_max) != 0)
                {
                    if (!v_flag)
                    {
                        v_str += " and ";
                        v_result += " and ";
                    }

                    v_flag = false;
                    v_multi = true;
                    v_result = v_result + " price <= " + v_max;
                    v_str = v_str + " price <= " + to_digit(v_max) + " ";
                }

                v_str += " ) ";
                v_result += " ) ";

            }

            out_str = v_result;

            if (v_multi)
            {
                return " ('||v_str||') ";
            }

            return v_str;
        }
    }

    public ShopSearch()
    {
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException
    {
        if (log.isDebugEnabled())
            log.debug("method is POST");

        doGet(request, response);
    }

    public void doGet(HttpServletRequest request_, HttpServletResponse response)
            throws IOException, ServletException
    {
        Writer out = null;
        DatabaseAdapter db_ = null;
        try
        {
            RenderRequest renderRequest = null;
            ContentTypeTools.setContentType(response, ContentTypeTools.CONTENT_TYPE_UTF8);

            out = response.getWriter();


            out.write("\r\n");
            out.write("<form action=\"/search.jsp\" method=\"GET\">\r\n");
            out.write("<input type=\"hidden\" name=\"action\" value=\"search\">\r\n");
            out.write("<table border=\"1\" cellspacing=\"0\" width=\"100%\">\r\n");
            out.write("<tr>\r\n");
            out.write("<td colspan=3 >");
            out.write("<font size=\"2\">");
            out.write("<strong>Строки для поиска:");
            out.write("</strong>");
            out.write("</font>");
            out.write("</td>\r\n");
            out.write("</tr>\r\n");
            out.write("<tr>\r\n");
            out.write("<td colspan=3>");
            out.write("<input type=\"text\" size=\"15\" maxlength=20 name=\"s\">");
            out.write("<select name=\"bool1\" size=\"1\">\r\n");
            out.write("<option selected value=\"0\">--");
            out.write("</option>\r\n");
            out.write("<option value=\"1\">И");
            out.write("</option>\r\n");
            out.write("<option value=\"2\">ИЛИ");
            out.write("</option>\r\n");
            out.write("<option value=\"3\">НЕ");
            out.write("</option>\r\n");
            out.write("</select>");
            out.write("<input type=\"text\" size=\"15\" maxlength=20  name=\"s1\">");
            out.write("<select name=\"bool2\" size=\"1\">\r\n");
            out.write("<option selected value=\"0\">--");
            out.write("</option>\r\n");
            out.write("<option value=\"1\">И");
            out.write("</option>\r\n");
            out.write("<option value=\"2\">ИЛИ");
            out.write("</option>\r\n");
            out.write("<option value=\"3\">НЕ");
            out.write("</option>\r\n");
            out.write("</select>");
            out.write("<input type=\"text\" size=\"15\" maxlength=20  name=\"s2\">\r\n");
            out.write("</td>\r\n");
            out.write("</tr>\r\n");
            out.write("<tr>\r\n");
            out.write("<td>");
            out.write("<small>");
            out.write("<strong>Стоимость от:");
            out.write("</strong>");
            out.write("</small>");
            out.write("</td>\r\n");
            out.write("<td>");
            out.write("<input type=\"text\" size=\"15\" maxlength=20  name=\"minPrice\">");
            out.write("</td>\r\n");
            out.write("<td valign=\"bottom\" align=\"right\">\r\n");
            out.write("<input type=\"submit\" name=\"button\" value=\"Найти\">");
            out.write("<br>");
            out.write("</td>\r\n");
            out.write("</tr>\r\n");
            out.write("<tr>\r\n");
            out.write("<td>");
            out.write("<small><strong>Стоимость до:</strong></small>");
            out.write("</td>\r\n");
            out.write("<td>");
            out.write("<input type=\"text\" size=\"15\" maxlength=20  name=\"maxPrice\">");
            out.write("</td>\r\n");
            out.write("<td>&nbsp;");
            out.write("</td>\r\n");
            out.write("</tr>\r\n");
            out.write("</table>\r\n");
            out.write("</form>\r\n");


            db_ = DatabaseAdapter.getInstance();

            if (RequestTools.getString(renderRequest, "action").toLowerCase().equals("search"))
            {

                int v_count_search = 2147483647;
                int v_display_item = 100; // constant of count diplay item

                String v_str_ip = ""; //request.getRemoteAddr();

                CallableStatement call = db_.getConnection().prepareCall(
                        "begin ? := tools.process_request(?, ?, ?, ?, ?, ?, ?, ?); end;");

                call.registerOutParameter(1, oracle.jdbc.driver.OracleTypes.VARCHAR);
                call.registerOutParameter(9, oracle.jdbc.driver.OracleTypes.VARCHAR);

                call.setString(2, RequestTools.getString(renderRequest, "s"));
                call.setString(3, RequestTools.getString(renderRequest, "s1"));
                call.setString(4, RequestTools.getString(renderRequest, "s2"));
                call.setString(5, RequestTools.getString(renderRequest, "bool1"));
                call.setString(6, RequestTools.getString(renderRequest, "bool2"));
                call.setString(7, RequestTools.getString(renderRequest, "minPrice"));
                call.setString(8, RequestTools.getString(renderRequest, "maxPrice"));

                call.execute();
                String v_str = StringTools.truncateString(call.getString(1), 400);
                String v_query = call.getString(9);

                call.close();
                call = null;

                if (log.isDebugEnabled())
                {
                    log.debug(v_str);
                    log.debug(v_query);
                }
//return;

                int v_len = v_str.length();
                int v_number_page = PortletService.getInt(renderRequest, "p", 1);

                // Todo this is simple stub, current not work
                // Todo because sequence was used to simple getting of next id of file
                CustomSequenceType seq = new CustomSequenceType();
                seq.setSequenceName("SEQ_WM_PRICE_QUERY_TABLE");
                seq.setTableName( "WM_PRICE_QUERY_TABLE");
                seq.setColumnName( "ID_THREAD" );
                Long v_id_query = db_.getSequenceNextValue( seq );

                String sql_ =
                        "insert into WM_PRICE_QUERY_TABLE " +
                        "( id_query, id_type_query, query_text, client_ip_address, client_host_name, date_query, count_exist ) " +
                        "VALUES ( ?, ?, ?, ?, ?, SYSDATE, 0 ) ";

                PreparedStatement ps = db_.prepareStatement(sql_);
                RsetTools.setLong(ps, 1, v_id_query);
                RsetTools.setLong(ps, 2, PortletService.getLong(renderRequest, "i"));
                ps.setString(3, v_query);
                ps.setString(4, v_str_ip);
                ps.setString(5, v_str_ip);

                if (log.isDebugEnabled())
                    log.debug("#1.0001");

                ps.executeUpdate();
                ps.close();
                ps = null;

                out.write("\r\n\r\n");
                out.write("<table border=\"1\" cellspacing=\"0\" cellpadding=\"0\" width=\"100%\">\r\n");
                out.write("<tr>\r\n");
                out.write("<td>\r\n        ");


                if (v_len < 3)
                {
                    out.write("\r\n");
                    out.write("<center>Строка поиска должна быть больше 2 символов");
                    out.write("</center>\r\n            ");

                }
                else
                {
/*	in this version not implemented count result record
int v_count_search := tools.calc_count_items(v_str);

UPDATE millenium.query_table
SET count_exist = v_count_search
WHERE id_query = v_id_query;
*/
                    int v_count_item = 0;


//<!--p align="center">Результат поиска: найдено '|| TO_CHAR(v_count_search)||'</p-->

                    sql_ =
                            "select distinct b.ID_FIRM, b.full_name, b.is_work " +
                            "from   WM_LIST_COMPANY b, WM_PRICE_LIST a, WM_PRICE_SHOP_LIST c, " +
                            "       WM_PORTAL_LIST_SITE d " +
                            "where  b.ID_FIRM = d.ID_FIRM and c.id_shop = c.id_shop and " +
                            "       a.id_shop = c.id_shop and a.absolete = 0 and c.is_close = 0 " +
                            "       and b.is_deleted = 0 and " + v_str + " order by b.full_name";

                    ps = db_.prepareStatement(sql_);
                    ResultSet rs = ps.executeQuery();

                    if (log.isDebugEnabled())
                        log.debug("#1 " + sql_);

                    while (rs.next())
                    {

                        Long sclient_rec_id_client = RsetTools.getLong(rs, "ID_FIRM");
                        String sclient_rec_full_name = RsetTools.getString(rs, "full_name");
                        int is_work = RsetTools.getInt(rs, "is_work", 0);

                        boolean v_flag = false;
                        boolean v_flag_firm = true;

                        String v_str_firm = "Предприятие: <i><a href=\"/firm/info.jsp?i=" + sclient_rec_id_client + "\">" + sclient_rec_full_name + "</a></i>";

//    shop s = new shop(ora_, sclient_rec_id_client);

                        String sql_item =
                                "select	distinct c.id_shop, c.name_shop_for_price_list " +
                                "from   WM_PRICE_LIST a, WM_PRICE_SHOP_LIST c, WM_PORTAL_VIRTUAL_HOST e " +
                                "where  a.id_shop = c.id_shop and c.ID_SITE=e.ID_SITE and " +
                                "a.absolete=0 and c.is_close=0 and " +
                                "e.name_virtual_host = lower(?) and " + v_str + " " +
                                "order by c.id_shop";

                        if (log.isDebugEnabled())
                            log.debug("#2 " + sql_item);

                        PreparedStatement ps_item = db_.prepareStatement(sql_item);
                        RsetTools.setLong(ps_item, 1, sclient_rec_id_client);
                        ResultSet rs_item = ps_item.executeQuery();


                        while (rs_item.next())
                        {

                            sql_ = "insert into WM_PRICE_QUERY_LIST (id_shop, id_query) values ( ?, ?) ";

                            PreparedStatement ps_tmp = db_.prepareStatement(sql_);
                            RsetTools.setLong(ps_tmp, 1, RsetTools.getLong(rs_item, "id_shop"));
                            RsetTools.setLong(ps_tmp, 2, v_id_query);

                            ps_tmp.executeUpdate();
                            ps_tmp.close();

                            int v_col_span = 4;

                            if ( RsetTools.getInt(rs_item, "is_artikul")==1 )
                                v_col_span++;

                            boolean v_flag_shop = true;

                            String v_str_shop = "<br><b><a href=\"/price/price.jsp?id=" + RsetTools.getString(rs_item, "id_shop") +
                                "\"  target=\"blank\">Перейти на прайс-лист " + RsetTools.getString(rs_item, "name_shop_for_price_list") + "</a></b>";

                            boolean v_first_step_shop = true;

                            String sql_detail =
                                "select	is_group, id, id_main, item, TO_CHAR( price, '999,999,999,990.99' ) price, absolete, a.id_shop, \n" +
                                "	currency, d.ID_FIRM, name_shop, " +
                                "	is_close " +
                                "from	WM_PRICE_LIST a, WM_PRICE_SHOP_LIST b, WM_LIST_COMPANY d, 		\n" +
                                "	WM_PORTAL_LIST_SITE e 						" +
                                "where  a.id_shop = e.id_shop and a.id_shop = b.id_shop and 		" +
                                "	e.ID_FIRM = d.ID_FIRM and a.absolete = 0 and b.is_close = 0 	" +
                                "	and d.is_deleted = 0 				 		" +
                                "	and e.ID_FIRM = ? and " + v_str + " order by a.id_shop asc, is_group desc, id asc";

                            if (log.isDebugEnabled())
                                log.debug("#3 " + sql_detail);

//out.writeln(sql_detail);
//if (true) return;

                            PreparedStatement ps_detail = db_.prepareStatement(sql_detail);
                            RsetTools.setLong(ps_detail, 1, sclient_rec_id_client);

                            ResultSet rs_detail = ps_detail.executeQuery();

                            boolean is_exists;
                            while ((is_exists = rs_detail.next()) == true)
                            {

                                v_count_item++;

// эту страницу надо выводить?

                                if ((v_count_item > v_number_page * v_display_item)
                                        && (v_count_item <= ((v_number_page + 1) * v_display_item))
                                )
                                {
                                    if (v_flag_firm)
                                    {
                                        out.write(v_str_firm);
                                        v_flag_firm = false;
                                    }

                                    if (v_flag_shop)
                                    {
                                        v_flag = true;
                                        out.write(v_str_shop);
                                        v_flag_shop = false;
                                    }

                                    if (v_first_step_shop)
                                    {
                                        out.write("<table border=\"1\" width=\"100%\">");
                                        if ( RsetTools.getInt(rs_item, "is_header_table")==1 )
                                        {
                                            out.write("<tr align=\"center\"><td width=\"67%\">" + RsetTools.getString(rs_item, "name_items") + "</td>");
                                            if ( RsetTools.getInt(rs_item, "is_artikul")==1 )
                                            {
                                                out.write("<td width=\"8%\">" + RsetTools.getString(rs_item, "name_artikul") + "</td>");
                                            }
                                            out.write("<td width=\"8%\">" + RsetTools.getString(rs_item, "name_price") + "</td>" +
                                                    "<td width=\"7%\">" + RsetTools.getString(rs_item, "name_curr") + "</td></tr>");
                                        }
                                        v_first_step_shop = false;
                                    }

                                    out.write("<tr>");

                                    if ( RsetTools.getInt(rs_detail, "is_group")==1 )
                                    {
                                        out.write("<td colspan=\"" + v_col_span + "\"><a href=\"/price/price.jsp?i=" +
                                                RsetTools.getLong(rs_detail, "id") + "&id=" + RsetTools.getString(rs_detail, "id_shop") +
                                                "\" target=\"blank\">" + RsetTools.getString(rs_detail, "item") + "</a></TD>");
                                    }
                                    else
                                    {
                                        out.write("<td width=\"85%\">" + RsetTools.getString(rs_detail, "item") + "</td>");
                                        out.write("<td width=\"10%\" align=\"right\">" + RsetTools.getString(rs_detail, "price", "&nbsp") + "</td>");
                                        out.write("<td  width=\"5%\" align=\"center\">" + RsetTools.getString(rs_detail, "currency", "&nbsp") + "</td>");
                                    }
                                    out.write("</tr>");
                                } //-- конец условия вывода страницы

                            }
                            if (!is_exists)
                                v_count_search = v_count_item;

                            ps_detail.close();
                            ps_detail = null;

                            if (v_flag)
                                out.write("</table>\n<br>");

                        }
                        ps_item.close();
                        ps_item = null;

                    }	// end while()


                }   // if (v_len <3)
                out.write("\r\n\r\n");
                out.write("</td>\r\n");
                out.write("</tr>\r\n");
                out.write("</table>\r\n\r\n");
                out.write("<table border=\"0\" width=\"100%\">\r\n");
                out.write("<tr>\r\n");
                out.write("<td width=\"50%\" align=\"center\">");

                if (v_number_page > 0)
                {
                    out.write("<a href=\"search_in_shop.jsp?s=");
                    out.write(RequestTools.getString(renderRequest, "s") + "&p=" + (v_number_page - 1));
                    out.write("\">Предыдущая страница");
                    out.write("</a>\"");

                }
                else
                {
                    out.write("&nbsp;");
                }
                out.write("</td>\r\n");
                out.write("<td width=\"50%\" align=\"center\">");


                if ((v_count_search - ((v_number_page + 1) * v_display_item)) >= 0)
                {
                    out.write("<a href=\"search_in_shop.jsp?s=");
                    out.write(RequestTools.getString(renderRequest, "s") + "&p=" + (v_number_page + 1));
                    out.write("\">Следующая страница");
                    out.write("</a>");

                }
                else
                {
                    out.write("&nbsp;");
                }
                out.write("</td>\r\n");
                out.write("</tr>\r\n");
                out.write("</table>\r\n        ");


            }

            out.write("\r\n");


        }
        catch (Exception e)
        {
            log.error(e);
            out.write(ExceptionTools.getStackTrace(e, 20, "<br>"));
        }
        finally
        {
            DatabaseAdapter.close(db_);
            db_ = null;
        }
    }
}
