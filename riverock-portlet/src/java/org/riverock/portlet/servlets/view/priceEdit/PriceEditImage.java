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

 * Time: 2:41:25 PM

 *

 * $Id$

 */



package org.riverock.portlet.servlets.view.priceEdit;



import java.io.IOException;

import java.io.Writer;

import java.sql.PreparedStatement;

import java.sql.ResultSet;

import java.util.List;



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

import org.riverock.portlet.portlets.WebmillErrorPage;

import org.riverock.portlet.price.PriceGroup;

import org.riverock.portlet.price.PriceGroupItem;

import org.riverock.portlet.price.PriceListPosition;

import org.riverock.portlet.schema.portlet.shop.PositionItemType;

import org.riverock.portlet.schema.portlet.shop.PricePositionType;

import org.riverock.sso.a3.AuthSession;

import org.riverock.webmill.portlet.ContextNavigator;

import org.riverock.webmill.portlet.CtxInstance;

import org.riverock.webmill.portlet.CtxURL;

import org.riverock.webmill.portlet.PortletTools;





public class PriceEditImage extends HttpServlet

{

    private static Logger log = Logger.getLogger(PriceEditImage.class);



    public PriceEditImage()

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



            DatabaseAdapter db_ = null;

            try

            {

                int itemsPerPage = 30;



                AuthSession auth_ = (AuthSession)ctxInstance.getPortletRequest().getUserPrincipal();

                if ( auth_==null )

                {

                    WebmillErrorPage.process(out, null, "You have not enough right to execute this operation", "/", "continue");

                    return;

                }



                db_ = DatabaseAdapter.getInstance(false);



                String index_page = ctxInstance.url("mill.price.index");



                PortletSession session = ctxInstance.getPortletRequest().getPortletSession();



                ShopPageParam shopParam = new ShopPageParam();



                shopParam.nameTemplate = ctxInstance.getNameTemplate();

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

                        response.sendRedirect(index_page);

                        return;

                    }

                    shopParam.id_shop = id_;

                }



                session.removeAttribute(Constants.ID_SHOP_SESSION);

                session.setAttribute(Constants.ID_SHOP_SESSION, shopParam.id_shop);



                if (auth_.isUserInRole("webmill.upload_image"))

                {



                    shopParam.id_group = PortletTools.getLong(ctxInstance.getPortletRequest(), "id_main");

                    Long id_item = PortletTools.getLong(ctxInstance.getPortletRequest(), "id_item");

                    long pageNum = PortletTools.getInt(ctxInstance.getPortletRequest(), "pageNum", new Integer(0)).intValue();

                    long countImage = PortletTools.getInt(ctxInstance.getPortletRequest(), "countImage", new Integer(itemsPerPage)).intValue();



                    PricePositionType pos = PriceListPosition.getInstance(db_, ctxInstance, shopParam);



                    if (pos != null)

                    {



                        out.write("\r\n");

                        out.write("<table border=\"0\" width=\"100%\">\r\n");

                        out.write("<tr>\r\n");

                        out.write("<td>\r\n        ");



                        if (pos.getPositionItemCount() > 0)

                        {

                            out.write("\r\n");

                            out.write("<a href=\"");

                            out.write(



                                    ctxInstance.url("mill.price.image") + '&' +

                                    "id_item=" + id_item



                            );

                            out.write("\">.");

                            out.write("</a>");





                        }

                        for (int i = pos.getPositionItemCount(); i > 0; i--)

                        {

                            PositionItemType item = pos.getPositionItem(i - 1);



                            out.write(" / ");

                            if (item.getIdGroupCurrent() != shopParam.id_group)

                            {



                                out.write("<a href=\"");

                                out.write(



                                        ctxInstance.url("mill.price.image") + '&' +

                                        "i=" + item.getIdGroupCurrent()



                                );

                                out.write("\">");

                                out.write(item.getPositionName());

                                out.write("</a>");





                            }

                            else

                            {

                                out.write("\r\n                ");

                                out.write(item.getPositionName());

                                out.write("\r\n                ");



                            }

                        }

                        out.write("\r\n");

                        out.write("</td>\r\n");

                        out.write("</tr>\r\n");

                        out.write("</table>\r\n        ");



                    }



// print group from price-list

//                    Vector items = PriceList.getPriceList(db_, shopParam.id_shop, 0, shopParam.id_group, ctxInstance.getPortletRequest().getServerName(), true);



                    List v = PriceGroup.getInstance(db_, shopParam.id_group, shopParam.id_shop,shopParam.idSite);

                    if (v != null)

                    {

                        out.write("<table border=\"0\" width=\"100%\" cellpadding=\"2px\" cellspacing=\"2px\">");



                        for (int i = 0; i < v.size(); i++)

                        {

                            PriceGroupItem itemGroup = (PriceGroupItem)v.get(i);



                            out.write("<tr>\n");

                            out.write("<td class=\"priceData\" colspan=\"5\" width=\"100%\">\r\n");

                            out.write("<a href=\"");

                            out.write(



                                    ctxInstance.url("mill.price.image") + '&' +

                                    "id_main=" + itemGroup.id_group + "&id_item=" + id_item



                            );

                            out.write("\">");

                            out.write(itemGroup.name);

                            out.write("</a></td></tr>");





                        } // for



                        out.write("</table><br>");



                    }



                    String sql_ =

                            "select  a.id_main, a.name_file " +

                            "from    image_dir a, auth_user b " +

                            "where   a.id = ? and a.is_group = 1 and " +

                            "        a.ID_FIRM = b.ID_FIRM and b.user_login = ?";



                    PreparedStatement ps = db_.prepareStatement(sql_);

                    RsetTools.setLong(ps, 1, shopParam.id_group);

                    ps.setString(2, auth_.getUserLogin());

                    ResultSet rs = ps.executeQuery();



                    if (rs.next())

                    {



// xxx wrong work - index.jsp instead image.jsp

                        out.write("<table border=\"0\" width=\"100%\">\r\n");

                        out.write("<tr>\r\n");

                        out.write("<td>\r\n");

                        out.write("<a href=\"");

                        out.write(index_page + '&');

                        out.write("id_main=");

                        out.write("" + shopParam.id_group);

                        out.write("\">\r\n            ");

                        out.write(ctxInstance.sCustom.getStr("price.top_level"));

                        out.write("</a>\r\n");

                        out.write("</td>\r\n");

                        out.write("</tr>\r\n");

                        out.write("</table>");





                    }

                    DatabaseManager.close(rs, ps);

                    rs = null;

                    ps = null;





                    out.write("<br>\r\n");

                    out.write("<table border=\"0\" width=\"100%\" cellpadding=\"2px\" cellspacing=\"2px\">");





                    sql_ =

                            "select id, id_main, name_file, description, ID_IMAGE_DIR " +

                            "from image_dir a, auth_user b \n" +

                            "where a.id_main = ? and a.ID_FIRM = b.ID_FIRM and a.is_group = 0 " +

                            "	and b.user_login=? \n" +

                            "order by id asc ";

                    try

                    {

                        ps = db_.prepareStatement(sql_);

                        RsetTools.setLong(ps, 1, shopParam.id_group);

                        ps.setString(2, auth_.getUserLogin());



                        rs = ps.executeQuery();



                        int counter = 0;

                        while (rs.next())

                        {



                            if (counter >= (pageNum + 1) * countImage)

                                break;



                            if ((counter >= pageNum * countImage) && (counter < (pageNum + 1) * countImage))

                            {

                                Long id_ = RsetTools.getLong(rs, "id");





                                out.write("\r\n");

                                out.write("<tr>\r\n");

                                out.write("<td class=\"imageDirData\" width=\"100%\">\r\n");

                                out.write("<a href=\"");

                                out.write(response.encodeURL("change_desc.jsp") + "?" + ctxInstance.getAsURL());

                                out.write("id=");

                                out.write(""+id_);

                                out.write("\">\r\n                    ");

                                out.write(RsetTools.getString(rs, "description"));

                                out.write("\r\n");

                                out.write("</a>");

                                out.write("<br>\r\n");

                                out.write("<a href=\"");

                                out.write(

                                        ctxInstance.url("mill.price.description") + '&' +

                                        "id_item=" + id_item +

                                        "&id_image=" + RsetTools.getLong(rs, "ID_IMAGE_DIR") +

                                        "&action=new_image"



                                );

                                out.write("\">");

                                out.write("<img src=\"");

                                out.write(RsetTools.getString(rs, "name_file"));

                                out.write("\">");

                                out.write("</a>\r\n");

                                out.write("</td>\r\n");

                                out.write("</tr>\r\n                    ");



                            }

                            counter++;

                        } // end while(rs.next())





                        out.write("\r\n");

                        out.write("<table>");

                        out.write("<tr>\r\n");

                        out.write("<td width=\"50%\" align=\"center\">\r\n            ");



                        if (pageNum > 0)

                        {

                            out.write("\r\n");

                            out.write("<a href=\"");

                            out.write(



                                    ctxInstance.url("mill.price.image") + '&' +

                                    "id_main=" + shopParam.id_group + "&pageNum=" + (pageNum - 1) +

                                    "&id_item=" + id_item



                            );

                            out.write("\">prev");

                            out.write("</a>");





                        }



                        out.write("</td>\r\n");

                        out.write("<td width=50% align=\"center\">\r\n            ");



                        if (rs.next())

                        {



                            out.write("\r\n");

                            out.write("<a href=\"");

                            out.write(



                                    ctxInstance.url("mill.price.image") + '&' +

                                    "id_main=" + shopParam.id_group + "&pageNum=" + (pageNum + 1) +

                                    "&id_item=" + id_item



                            );

                            out.write("\">next");

                            out.write("</a>");

                            out.write("<br>\r\n                ");



                        }



                        out.write("</td>\r\n");

                        out.write("</tr>");

                        out.write("</table>\r\n            ");





                    }

                    catch (Exception e)

                    {

                        out.write("Error create image list - " + ExceptionTools.getStackTrace(e, 20, "<br>"));

                    }

                    finally

                    {

                        DatabaseManager.close(rs, ps);

                        rs = null;

                        ps = null;

                    }



                    out.write("</table>\r\n        ");



                    /**

                     out.write("\r\n");

                     out.write("<a href=\"");

                     out.write( response.encodeURL("desc.jsp"));

                     out.write("?");

                     out.write( jspPage.addURL );

                     out.write("id_main=");

                     out.write( shopParam.id_group  );

                     out.write("\">upload file");

                     out.write("</a>");

                     out.write("<br>\r\n");

                     out.write("<a href=\"");

                     out.write( response.encodeURL("a.jsp"));

                     out.write("?");

                     out.write(jspPage.addURL);

                     out.write("id_main=");

                     out.write( shopParam.id_group  );

                     out.write("\">Browse folder with image");

                     out.write("</a>");

                     out.write("<br>\r\n        ");





                     */

                }





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

        catch (Exception e)

        {

            log.error(e);

            out.write(ExceptionTools.getStackTrace(e, 20, "<br>"));

        }



    }

}

