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

 * Time: 2:42:04 PM

 *

 * $Id$

 */



package org.riverock.portlet.servlets.view.priceEdit;



import java.io.IOException;

import java.io.Writer;

import java.sql.PreparedStatement;

import java.sql.SQLException;



import javax.portlet.PortletSession;

import javax.servlet.ServletException;

import javax.servlet.http.HttpServlet;

import javax.servlet.http.HttpServletRequest;

import javax.servlet.http.HttpServletResponse;



import org.apache.log4j.Logger;

import org.riverock.common.tools.ExceptionTools;

import org.riverock.common.tools.RsetTools;

import org.riverock.generic.db.DatabaseAdapter;

import org.riverock.generic.db.DatabaseManager;

import org.riverock.portlet.main.Constants;

import org.riverock.portlet.portlets.ShopPageParam;

import org.riverock.sso.a3.AuthSession;

import org.riverock.sso.a3.AuthTools;

import org.riverock.webmill.portlet.ContextNavigator;

import org.riverock.webmill.portlet.CtxInstance;

import org.riverock.webmill.portlet.CtxURL;

import org.riverock.webmill.portlet.PortletTools;





public class PriceEditShop extends HttpServlet

{

    private static Logger log = Logger.getLogger("org.riverock.servlets.view.priceEdit.PriceEditShop");



    public PriceEditShop()

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

        try

        {

            CtxInstance ctxInstance =

                (CtxInstance)request_.getSession().getAttribute( org.riverock.webmill.main.Constants.PORTLET_REQUEST_SESSION );



            ContextNavigator.setContentType(response);



            out = response.getWriter();



            try

            {

                AuthSession auth_ = AuthTools.check(ctxInstance.getPortletRequest(), response, "/");

                if (auth_ == null)

                    return;



                DatabaseAdapter db_ = DatabaseAdapter.getInstance(false);



//                InitPage jspPage = new InitPage(db_, request,

//                                                "mill.locale._price_list"

//                );





                ShopPageParam shopParam = new ShopPageParam();

                PortletSession session = ctxInstance.getPortletRequest().getPortletSession();



                shopParam.nameTemplate = ctxInstance.getPortletRequest().getParameter(Constants.NAME_TEMPLATE_CONTEXT_PARAM);

                shopParam.setServerName(ctxInstance.getPortletRequest().getServerName());



                if (ctxInstance.getPortletRequest().getParameter(Constants.NAME_ID_SHOP_PARAM) != null)

                {

                    shopParam.id_shop = PortletTools.getLong(ctxInstance.getPortletRequest(), Constants.NAME_ID_SHOP_PARAM);

                }

                else

                {

                    Long id_ = (Long) session.getAttribute(Constants.ID_SHOP_SESSION);

                    if (id_ == null)

                    {

                        response.sendRedirect(

                                CtxURL.url(ctxInstance.getPortletRequest(), response, ctxInstance.page, "mill.price.index")

                        );

                        return;

                    }

                    shopParam.id_shop = id_;

                }



                session.removeAttribute(Constants.ID_SHOP_SESSION);

                session.setAttribute(Constants.ID_SHOP_SESSION, shopParam.id_shop);



                if (auth_.isUserInRole("webmill.edit_price_list"))

                {



                    shopParam.id_group = PortletTools.getLong(ctxInstance.getPortletRequest(), "i");



                    if (PortletTools.getString(ctxInstance.getPortletRequest(), "action").equals("update"))

                    {

                        int countItem = PortletTools.getInt(ctxInstance.getPortletRequest(), "count_item", new Integer(0)).intValue();



                        PreparedStatement st = null;

                        String sql_ =

                                "update PRICE_LIST " +

                                "set ITEM=?, PRICE=?, CURRENCY=?, IS_SPECIAL=? " +

                                "where ID_ITEM=? and ID_SHOP=?";



                        try

                        {

                            for (int i = 0; i < countItem; i++)

                            {

                                if (st == null)

                                    st = db_.prepareStatement(sql_);



                                Long idCode = PortletTools.getLong(ctxInstance.getPortletRequest(), "id_code_" + i);



                                st.setString(1, PortletTools.getString(ctxInstance.getPortletRequest(), "name_" + i + "_" + idCode));

                                RsetTools.setDouble(st, 2, PortletTools.getDouble(ctxInstance.getPortletRequest(), "price_" + i + "_" + idCode));

                                st.setString(3, PortletTools.getString(ctxInstance.getPortletRequest(), "curr_" + i + "_" + idCode));

                                RsetTools.setInt(st, 4, PortletTools.getInt(ctxInstance.getPortletRequest(), "cb_" + i + "_" + idCode, new Integer(0) ));

                                RsetTools.setLong(st, 5, idCode);

                                RsetTools.setLong(st, 6, shopParam.id_shop);



                                st.addBatch();

                            }

                            int[] updateCounts = st.executeBatch();



                            if (log.isDebugEnabled())

                                log.debug("count ouf updated record - "+updateCounts.length);

                        }

                        catch (SQLException e0)

                        {

                            out.write(e0.toString());

                            return;

                        }

                        finally

                        {

                            DatabaseManager.close(st);

                            st = null;

                        }

                    }





/*

                    PriceListPosition pos = PriceListPosition.getInstance(

                            db_, response,

                            ctxInstance.page,

                            shopParam

                    );



                    if (pos != null)



                    {



                        out.write("\n");

                        out.write("<table border=\"0\" width=\"100%\">\n");

                        out.write("<tr>\n");

                        out.write("<td>\n                ");



                        if (pos.group.size() > 0)

                        {

                            out.write("\n");

                            out.write("<a href=\"");

                            out.write(



                                    CtxURL.url(ctxInstance.getPortletRequest(), response, ctxInstance.page, "mill.price.index")



                            );

                            out.write("\">.");

                            out.write("</a>\n                    ");



                        }

                        for (int i = pos.group.size(); i > 0; i--)

                        {

                            PositionItem item = (PositionItem) pos.group.elementAt(i - 1);



                            out.write(" / ");

                            if (item.id_group_current != shopParam.id_group)

                            {

                                out.write("\n");

                                out.write("<a href=\"");

                                out.write(



                                        CtxURL.url(ctxInstance.getPortletRequest(), response, ctxInstance.page, "mill.price.shop") + '&' +

                                        "i=" + item.id_group_current + '&' +

                                        Constants.NAME_ID_SHOP_PARAM + '=' + shopParam.id_shop



                                );

                                out.write("\">");

                                out.write(item.name);

                                out.write("</a>");



                            }

                            else

                            {

                                out.write(item.name);

                            }

                        }

                        out.write("</td>\n");

                        out.write("</tr>\n");

                        out.write("</table>\n");

                        out.write("<br>\n");



                    }



// print group from price-list

                    PriceGroup group = PriceGroup.getInstance(db_, shopParam.id_group, shopParam.id_shop,shopParam.idSite);



//                    Vector items = PriceList.getPriceList(

//                            db_, shopParam.id_shop, 0,

//                            shopParam.id_group,

//                            ctxInstance.getPortletRequest().getServerName(),

//                            true);



                    if (group != null && group.v != null)

                    {

                        out.write("<table border=\"0\" width=\"100%\" cellpadding=\"2px\" cellspacing=\"2px\">\n");



                        for (int i = 0; i < group.v.size(); i++)

                        {

                            PriceGroupItem itemGroup = (PriceGroupItem) group.v.elementAt(i);

                            out.write("<tr>\n");

                            out.write("<td class=\"priceData\" colspan=\"5\" width=\"100%\">\n");

                            out.write("<a href=\"");

                            out.write(



                                    CtxURL.url(ctxInstance.getPortletRequest(), response, ctxInstance.page, "mill.price.shop") + '&' +

                                    "i=" + itemGroup.id_group + '&' +

                                    Constants.NAME_ID_SHOP_PARAM + '=' + shopParam.id_shop



                            );

                            out.write("\">");

                            out.write(itemGroup.name);

                            out.write("</a></td></tr>\n");



                        } // for

                        out.write("</table>\n");

                    }



// print items from price-list

                    // ���������� ������ �� ���������

//                    if (shopParam.id_currency==0)

//                        shopParam.id_currency = shop.currencyID;



//                    PriceListItemList items = PriceListItemList.getInstance( db_, shopParam  );

                    Vector items = PriceList.getPriceList(db_, shopParam.id_shop, 0, shopParam.id_group, ctxInstance.getPortletRequest().getServerName());

                    if (items != null)

                    {

                        out.write("<form action=\"");

                        out.write(



                                CtxURL.url(ctxInstance.getPortletRequest(), response, ctxInstance.page, "mill.price.shop")



                        );

                        out.write("\" method=\"POST\">\n");

                        out.write(

                            "<hidden name=\""+Constants.NAME_ID_SHOP_PARAM+

                            "\" value=\""+shopParam.id_shop+"\">\n"

                        );

                        out.write("<table border=\"1\" width=\"100%\" cellpadding=\"2px\" cellspacing=\"2px\">\n");



                        int i = 0;

                        boolean headerFlag = true;

                        for (i = 0; i < items.size(); i++)

                        {

                            PriceListItem item = null;

                            item = (PriceListItem) items.elementAt(i);

//                            out.write("\n");

//                            out.write("<tr>\n                    ");



                            if (headerFlag)

                            {

                                out.write("<tr align=\"center\">\n");

                                out.write("<th class=\"priceData\" width=\"7%\">��������");

                                out.write("</th>\n");

                                out.write("<th class=\"priceData\" width=\"70%\">������������");

                                out.write("</th>\n");

                                out.write("<th class=\"priceData\" width=\"7%\">����");

                                out.write("</th>\n");

                                out.write("<th class=\"priceData\" width=\"7%\" align=\"center\">������");

                                out.write("</th>\n");

                                out.write("<th class=\"priceData\" width=\"7%\" align=\"center\">���� ������-�����");

                                out.write("</th>\n");

                                out.write("</tr>");





                                headerFlag = false;

                            }



                            out.write("<tr>\n");

                            out.write("<td>\n");

                            out.write("<input type=\"button\" value=\"��������\"\nonclick=\"window.open('");

                            out.write(



                                    CtxURL.url(ctxInstance.getPortletRequest(), response, ctxInstance.page, "mill.price.description") + '&' +

                                    "id_item=" + item.idPK



                            );

                            out.write("', '��������',\n'scrollbars=1,status=1,location=1,toolbar=1,menubar=1,resizable=1,height=350,width=700')\">");

//                            out.write("<br>\n");

                            out.write("</td>\n");

                            out.write("<td>");

                            out.write("<textarea name=\"name_"+i+"_"+ item.idPK);

                            out.write("\" rows=\"2\" cols=\"40\">");

                            out.write(item.nameItem);

                            out.write("</textarea>");

                            out.write("</td>\n");

                            out.write("<td>");

                            out.write(

                                "<input type=text name=\"price_"+i+"_"+item.idPK

                            );

                            out.write("\" size=\"10\" value=\"");

                            out.write("" + item.priceItem);

                            out.write("\">");

                            out.write("</td>\n");

                            out.write("<td>");

                            out.write("<input type=text name=\"curr_"+i+"_"+item.idPK);

                            out.write("\" size=\"5\" value=\"");

                            out.write(item.codeCurrency);

                            out.write("\">");

                            out.write("</td>\n");

                            out.write("<td>");

                            out.write("<input type=\"checkbox\" ");

                            out.write((item.isSpecial == 1 ? "checked" : ""));

                            out.write(" name=\"cb_"+i+"_"+ item.idPK);

                            out.write("\" value=\"1\">");

                            out.write("</td>\n");

                            out.write("<input type=\"hidden\" name=\"id_code_"+i);

                            out.write("\" value=\"" + item.idPK);

                            out.write("\">\n");

                            out.write("</tr>\n");



                        }



                        out.write("</table>\n");

                        out.write("<input type=\"hidden\" name=\"i\" value=\"");

                        out.write("" + shopParam.id_group);

                        out.write("\">\n");

                        out.write("<input type=\"hidden\" name=\"action\" value=\"update\">\n");

                        out.write("<input type=\"hidden\" name=\"count_item\" value=\"");

                        out.write(""+i);

                        out.write("\">\n");

                        out.write(ctxInstance.page.getAsForm());

                        out.write("<input type=\"submit\" value=\"��������\">\n");

                        out.write("</form>\n");

                    }

*/

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

