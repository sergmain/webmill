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

 * Author: mill

 * Date: Dec 3, 2002

 * Time: 2:41:04 PM

 *

 * $Id$

 */



package org.riverock.portlet.servlets.view.priceEdit;



import java.io.IOException;

import java.io.Writer;

import java.sql.PreparedStatement;



import javax.servlet.ServletException;

import javax.servlet.http.HttpServlet;

import javax.servlet.http.HttpServletRequest;

import javax.servlet.http.HttpServletResponse;

import javax.servlet.http.HttpSession;



import org.riverock.sso.a3.AuthSession;

import org.riverock.sso.a3.AuthTools;

import org.riverock.generic.db.DatabaseAdapter;

import org.riverock.generic.db.DatabaseManager;

import org.riverock.portlet.main.Constants;

import org.riverock.webmill.port.InitPage;

import org.riverock.webmill.portlet.CtxURL;

import org.riverock.webmill.utils.ServletUtils;

import org.riverock.common.tools.ExceptionTools;

import org.riverock.common.tools.ServletTools;

import org.riverock.common.tools.StringTools;

import org.riverock.common.tools.RsetTools;



import org.apache.log4j.Logger;





public class PriceEditDescription extends HttpServlet

{

    private static Logger log = Logger.getLogger(PriceEditDescription.class);



    public PriceEditDescription()

    {

    }



    public void doPost(HttpServletRequest request, HttpServletResponse response)

            throws IOException, ServletException

    {

        if (log.isDebugEnabled())

            log.debug("method is POST");





        doGet(request, response);

    }



    public void doGet(HttpServletRequest request, HttpServletResponse response)

            throws IOException, ServletException

    {

        Writer out = null;

        try

        {

            InitPage.setContentType(response);



            out = response.getWriter();



            try

            {



                AuthSession auth_ = AuthTools.check(request, response, "/");

                if (auth_ == null)

                    return;



                HttpSession session = request.getSession();



                DatabaseAdapter dbDyn = DatabaseAdapter.getInstance(true);

                PreparedStatement st = null;

                InitPage jspPage = null;



                try

                {



                    jspPage = new InitPage(dbDyn, request, response,

                            "mill.locale._price_list",

                            Constants.NAME_LANG_PARAM, null, null);



                    String index_page = CtxURL.url(request, response, jspPage.cross, "mill.price.index");



                    Long id_shop = null;

                    if (request.getParameter(Constants.NAME_ID_SHOP_PARAM) != null)

                    {

                        id_shop = ServletTools.getLong(request, Constants.NAME_ID_SHOP_PARAM);

                    }

                    else

                    {

                        Long id_ = (Long) session.getAttribute(Constants.ID_SHOP_SESSION);

                        if (id_ == null)

                        {

                            response.sendRedirect(index_page);

                            return;

                        }

                        id_shop = id_;

                    }



                    session.removeAttribute(Constants.ID_SHOP_SESSION);

                    session.setAttribute(Constants.ID_SHOP_SESSION, id_shop);



                    if (auth_.isUserInRole("webmill.edit_price_list"))

                    {



                        if (ServletTools.isNotInit(request, response, "id_item", index_page))

                            return;

                        Long id_item = ServletTools.getLong(request, "id_item");



                        if (ServletUtils.getString(request, "action").equals("update"))

                        {

                            dbDyn.conn.setAutoCommit(false);

                            String sql_ =

                                    "delete from PRICE_ITEM_DESCRIPTION a " +

                                    "where exists " +

                                    " ( select null from price_list b " +

                                    "where b.id_shop = ? and b.id_item = ? and " +

                                    "a.id_item=b.id_item ) ";



                            try

                            {

                                st = dbDyn.prepareStatement(sql_);

                                RsetTools.setLong(st, 1, id_shop);

                                RsetTools.setLong(st, 2, id_item);

                                st.executeUpdate();

                            }

                            catch (Exception e0001)

                            {

                                dbDyn.rollback();

                                out.write("Error #1 - " + ExceptionTools.getStackTrace(e0001, 20, "<br>"));

                                return;

                            }

                            finally

                            {

                                DatabaseManager.close(st);

                                st = null;

                            }





                            sql_ =

                                    "insert into PRICE_ITEM_DESCRIPTION " +

                                    "(ID_PRICE_ITEM_DESCRIPTION, ID_ITEM, TEXT)" +

                                    "(select seq_PRICE_ITEM_DESCRIPTION.nextval, ID_ITEM, ? " +

                                    " from price_list b where b.ID_SHOP = ? and b.ID_ITEM = ? )";



                            try

                            {



                                int idx = 0;

                                int offset = 0;

                                int j = 0;



                                byte[] b = StringTools.getBytesUTF(ServletUtils.getString(request, "n"));

                                st = dbDyn.prepareStatement(sql_);

                                while ((idx = StringTools.getStartUTF(b, 2000, offset)) != -1)

                                {

                                    st.setString(1, new String(b, offset, idx - offset, "utf-8"));

                                    RsetTools.setLong(st, 2, id_shop);

                                    RsetTools.setLong(st, 3, id_item);

                                    st.addBatch();



                                    offset = idx;

                                    if (j > 10)

                                        break;

                                    j++;

                                }



                                int[] updateCounts = st.executeBatch();

                                if (log.isDebugEnabled())

                                    log.debug("Number of updated records - " + updateCounts);



                                dbDyn.commit();

                            }

                            catch (Exception e0)

                            {

                                dbDyn.rollback();

                                out.write("Error #2 - " + ExceptionTools.getStackTrace(e0, 20, "<br>"));

//                    out.write("Error 2: "+  e0.toString() );

                                return;

                            }

                            finally

                            {

                                dbDyn.conn.setAutoCommit(true);

                                if (st != null)

                                {

                                    DatabaseManager.close(st);

                                    st = null;

                                }

                            }

                        }



                        if (ServletUtils.getString(request, "action").equals("new_image") &&

                                request.getParameter("id_image") != null

                        )

                        {

                            Long id_image = ServletTools.getLong(request, "id_image");

                            dbDyn.conn.setAutoCommit(false);



                            String sql_ =

                                    "delete from IMAGE_PRICE_ITEMS a " +

                                    "where exists " +

                                    " ( select null from price_list b " +

                                    "where b.id_shop = ? and b.id_item = ? and " +

                                    "a.id_item=b.id_item ) ";



                            try

                            {

                                st = dbDyn.prepareStatement(sql_);

                                RsetTools.setLong(st, 1, id_shop);

                                RsetTools.setLong(st, 2, id_item);

                                st.executeUpdate();

                            }

                            catch (Exception e0001)

                            {

                                dbDyn.rollback();

                                out.write("Error #3 - " + ExceptionTools.getStackTrace(e0001, 20, "<br>"));

//                    out.write("Error 1: "+ e0001.getMessage() );

                                return;

                            }

                            finally

                            {

                                DatabaseManager.close(st);

                                st = null;

                            }





                            sql_ =

                                    "insert into IMAGE_PRICE_ITEMS " +

                                    "(id_IMAGE_PRICE_ITEMS, id_item, ID_IMAGE_DIR)" +

                                    "(select seq_IMAGE_PRICE_ITEMS.nextval, id_item, ? " +

                                    " from price_list b where b.id_shop = ? and b.id_item = ? )";



                            try

                            {



                                st = dbDyn.prepareStatement(sql_);

                                RsetTools.setLong(st, 1, id_image);

                                RsetTools.setLong(st, 2, id_shop);

                                RsetTools.setLong(st, 3, id_item);



                                int updateCounts = st.executeUpdate();

                                if (log.isDebugEnabled())

                                    log.debug("Number of updated records - " + updateCounts);



                                dbDyn.commit();

                            }

                            catch (Exception e0)

                            {

                                dbDyn.rollback();

                                log.error( "Error insert image", e0 );

                                out.write("Error #4 - " + ExceptionTools.getStackTrace(e0, 20, "<br>"));

//                    out.write("Error 2: "+  e0.getMessage() );

                                return;

                            }

                            finally

                            {

                                dbDyn.conn.setAutoCommit(true);

                                DatabaseManager.close(st);

                                st = null;

                            }

                        }



                        if (true)

                            throw new Exception("Need refactoring");



/*

                        PriceListItemExtend item =

                                new PriceListItemExtend(dbDyn, id_shop,

                                        request.getServerName(), id_item);



                        out.write("\r\n                ");

                        out.write(request.getServerName());

                        out.write("\r\nНаименование: ");

                        out.write("<b>");

                        out.write(item.nameItem);

                        out.write("<b>\r\n");

                        out.write("<form action=\"");

                        out.write(



                                CtxURL.url(request, response, jspPage.cross, "mill.price.description")



                        );

                        out.write("\" method=\"POST\">\r\n");

                        out.write("<table border=\"0\">\r\n");

                        out.write("<tr>\r\n");

                        out.write("<td valign=\"top\">Описание товара");

                        out.write("</td>\r\n");

                        out.write("<td>");

                        out.write("<textarea name=\"n\" rows=\"10\" cols=\"50\">");

                        out.write(item.desc);

                        out.write("</textarea>");

                        out.write("</td>\r\n");

                        out.write("</tr>\r\n");

                        out.write("<tr>\r\n");

                        out.write("<td valign=\"top\">URL картинки");

                        out.write("</td>\r\n");

                        out.write("<td>");

                        out.write((item.imageNameFile == null ? "" : item.imageNameFile));

                        out.write("</td>\r\n");

                        out.write("</tr>\r\n");

                        out.write("<tr>\r\n");

                        out.write("<td valign=\"top\">Картинка");

                        out.write("</td>\r\n");

                        out.write("<td>\r\n                ");



                        if (item.imageNameFile != null && !"".equals(item.imageNameFile.trim()))

                        {

                            out.write("\r\n");

                            out.write("<img src=\"");

                            out.write(item.imageNameFile);

                            out.write("\" border=\"0\">\r\n                    ");



                        }

                        out.write("\r\n");

                        out.write("</td>\r\n");

                        out.write("</tr>\r\n");

                        out.write("</table>\r\n\r\n");

                        out.write("<input type=\"hidden\" name=\"id_item\" value=\"");

                        out.write("" + id_item);

                        out.write("\">\r\n");

                        out.write("<input type=\"hidden\" name=\"action\" value=\"update\">\r\n");

                        out.write("<input type=\"submit\" value=\"Изменить\">\r\n");

                        out.write("</form>\r\n\r\n");

                        out.write("<form action=\"");

                        out.write(



                                CtxURL.url(request, response, jspPage.cross, "mill.price.image")



                        );

                        out.write("\" method=\"POST\">\r\n");

                        out.write("<input type=\"hidden\" name=\"id_main\" value=\"");

                        out.write("" + item.id_group);

                        out.write("\">\r\n");

                        out.write("<input type=\"hidden\" name=\"id_item\" value=\"");

                        out.write("" + id_item);

                        out.write("\">\r\n");

                        out.write("<input type=\"submit\" value=\"Изменить картинку\">\r\n");

                        out.write("</form>\r\n                ");

*/



                    } // auth_

                }

                finally

                {

                    DatabaseManager.close(st);

                    st = null;



                    DatabaseAdapter.close(dbDyn);

                    dbDyn = null;

                }



            }

            catch (Exception e)

            {

                log.error(e);

                out.write(ExceptionTools.getStackTrace(e, 20, "<br>"));

            }





        }

        catch (Exception e)

        {

            log.error(e);

            out.write(ExceptionTools.getStackTrace(e, 20, "<br>"));

        }



    }

}

