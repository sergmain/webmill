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

 * Time: 12:28:19 PM

 *

 * $Id$

 */



package org.riverock.portlet.servlets.view.image;



import java.io.IOException;

import java.io.Writer;

import java.sql.PreparedStatement;

import java.sql.ResultSet;



import javax.servlet.ServletException;

import javax.servlet.http.HttpServlet;

import javax.servlet.http.HttpServletRequest;

import javax.servlet.http.HttpServletResponse;



import org.apache.log4j.Logger;

import org.riverock.common.tools.ExceptionTools;

import org.riverock.common.tools.RsetTools;

import org.riverock.generic.db.DatabaseAdapter;

import org.riverock.generic.db.DatabaseManager;

import org.riverock.sso.a3.AuthSession;

import org.riverock.sso.a3.AuthTools;

import org.riverock.webmill.portlet.ContextNavigator;

import org.riverock.webmill.portlet.CtxInstance;

import org.riverock.webmill.portlet.CtxURL;

import org.riverock.webmill.portlet.PortletTools;



public class ImageIndex extends HttpServlet

{

    private static Logger cat = Logger.getLogger("org.riverock.portlet.servlets.view.image.ImageIndex");



    public ImageIndex()

    {

    }



    public void doPost(HttpServletRequest request, HttpServletResponse response)

            throws IOException, ServletException

    {

        if (cat.isDebugEnabled())

            cat.debug("method is POST");



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



                String index_page = CtxURL.url(ctxInstance.getPortletRequest(), response, ctxInstance.page, "mill.image.index");



                if (auth_.isUserInRole("webmill.upload_image"))

                {

                    Long id_main_ = PortletTools.getLong(ctxInstance.getPortletRequest(), "id_main");



                    out.write("\r\n");

                    out.write("<a href=\"");

                    out.write(



                            CtxURL.url(ctxInstance.getPortletRequest(), response, ctxInstance.page, "mill.image.desc") + '&' +

                            "id_main=" + id_main_



                    );

                    out.write("\">upload file");

                    out.write("</a>");

                    out.write("<br>\r\n");

                    out.write("<a href=\"");

                    out.write(



                            CtxURL.url(ctxInstance.getPortletRequest(), response, ctxInstance.page, "mill.image.select_url") + '&' +

                            "id_main=" + id_main_



                    );

                    out.write("\">Загрузка изображения из URLa");

                    out.write("</a>");

                    out.write("<br>\r\n            ");





                    String sql_ =

                        "select a.id_main, a.name_file " +

                        "from   image_dir a, SITE_LIST_SITE b, SITE_VIRTUAL_HOST c " +

                        "where  a.id=? and a.is_group=1 and a.ID_FIRM = b.ID_FIRM and " +

                        "b.ID_SITE = c.ID_SITE and NAME_VIRTUAL_HOST = lower(?) ";



                    PreparedStatement ps = db_.prepareStatement(sql_);

                    RsetTools.setLong(ps, 1, id_main_);

                    ps.setString(2, ctxInstance.getPortletRequest().getServerName());

                    ResultSet rs = ps.executeQuery();



                    if (rs.next())

                    {



                        out.write("\r\n");

                        out.write("<table border=\"0\" width=\"100%\">\r\n");

                        out.write("<tr>\r\n");

                        out.write("<td>\r\n");

                        out.write("<a href=\"");

                        out.write(



                                index_page + '&' +

                                "id_main=" + RsetTools.getLong(rs, "id_main")



                        );

                        out.write("\">\r\n                ");

                        out.write(ctxInstance.sCustom.getStr("price.top_level"));

                        out.write("</a>\r\n");

                        out.write("</td>\r\n");

                        out.write("</tr>\r\n");

                        out.write("</table>\r\n                ");



                    }

                    DatabaseManager.close(rs, ps);

                    rs = null;

                    ps = null;



                    out.write("\r\n");

                    out.write("<br>\r\n");

                    out.write("<table border=\"0\" width=\"100%\" cellpadding=\"2px\" cellspacing=\"2px\">\r\n            ");



                    sql_ =

                            "select is_group, id, id_main, name_file " +

                            "from image_dir a, auth_user b \n" +

                            "where a.id_main = ? and a.ID_FIRM = b.ID_FIRM and a.is_group = 1 " +

                            "	and b.user_login=? \n" +

                            "order by id asc ";



                    ps = db_.prepareStatement(sql_);

                    RsetTools.setLong(ps, 1, id_main_);

                    ps.setString(2, auth_.getUserLogin());



                    rs = ps.executeQuery();



                    while (rs.next())

                    {



                        Long id_ = RsetTools.getLong(rs, "id");



                        out.write("\r\n");

                        out.write("<tr>\r\n");

                        out.write("<td class=\"imageDirData\" width=\"100%\">\r\n");

                        out.write("<a href=\"");

                        out.write(

                                index_page + '&' + "id_main=" + id_

                        );

                        out.write("\">");

                        out.write(RsetTools.getString(rs, "name_file"));

                        out.write("</a>\r\n");

                        out.write("</td>\r\n");

                        out.write("</tr>\r\n                ");





                    } // end while(rs.next())



                    DatabaseManager.close(rs, ps);

                    rs = null;

                    ps = null;



                    out.write("\r\n");

                    out.write("</table>\r\n\r\n");

                    out.write("<a href=\"");

                    out.write(



                            CtxURL.url(ctxInstance.getPortletRequest(), response, ctxInstance.page, "mill.image.desc") + '&' +

                            "id_main=" + id_main_



                    );

                    out.write("\">upload file");

                    out.write("</a>");

                    out.write("<br>\r\n");

                    out.write("<a href=\"");

                    out.write(



                            CtxURL.url(ctxInstance.getPortletRequest(), response, ctxInstance.page, "mill.image.select_url") + '&' +

                            "id_main=" + id_main_



                    );

                    out.write("\">upload from URL");

                    out.write("</a>");

                    out.write("<br>\r\n");

                    out.write("<a href=\"");

                    out.write(



                            CtxURL.url(ctxInstance.getPortletRequest(), response, ctxInstance.page, "mill.image.list") + '&' +

                            "id_main=" + id_main_



                    );

                    out.write("\">Browse folder with image");

                    out.write("</a>");

                    out.write("<br>\r\n            ");





                }





            }

            catch (Exception e)

            {

                cat.error(e);

                out.write(ExceptionTools.getStackTrace(e, 20, "<br>"));

            }



        }

        catch (Exception e)

        {

            cat.error(e);

            out.write(ExceptionTools.getStackTrace(e, 20, "<br>"));

        }



    }

}

