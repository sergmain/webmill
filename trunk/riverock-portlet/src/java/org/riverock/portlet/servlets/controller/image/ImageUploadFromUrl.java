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

 * Time: 1:31:13 PM

 *

 * $Id$

 */



package org.riverock.portlet.servlets.controller.image;



import java.io.File;

import java.io.FileOutputStream;

import java.io.IOException;

import java.io.InputStream;

import java.io.Writer;

import java.net.URL;

import java.sql.PreparedStatement;



import javax.servlet.ServletException;

import javax.servlet.http.HttpServlet;

import javax.servlet.http.HttpServletRequest;

import javax.servlet.http.HttpServletResponse;



import org.riverock.common.config.PropertiesProvider;

import org.riverock.common.tools.MainTools;

import org.riverock.common.tools.RsetTools;

import org.riverock.common.tools.ServletTools;

import org.riverock.common.tools.StringTools;

import org.riverock.generic.db.DatabaseAdapter;

import org.riverock.generic.schema.db.CustomSequenceType;

import org.riverock.generic.utils.DateUtils;

import org.riverock.portlet.main.Constants;

import org.riverock.sso.a3.AuthSession;

import org.riverock.sso.a3.AuthTools;

import org.riverock.webmill.main.UploadFileException;

import org.riverock.webmill.port.InitPage;

import org.riverock.webmill.portlet.CtxURL;

import org.riverock.webmill.utils.ServletUtils;



import org.apache.log4j.Logger;



public class ImageUploadFromUrl extends HttpServlet

{

    private static Logger cat = Logger.getLogger(ImageUploadFromUrl.class);



    public ImageUploadFromUrl()

    {

    }



    public void doPost(HttpServletRequest request, HttpServletResponse response)

            throws IOException, ServletException

    {

        if (cat.isDebugEnabled())

            cat.debug("method is POST");



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



            if(cat.isDebugEnabled())

                cat.debug("Start commit new image");



            AuthSession auth_ = AuthTools.check(request, response, "/");

            if (auth_ == null)

                return;



            PreparedStatement ps = null;

            DatabaseAdapter dbDyn = null;



            try

            {

                dbDyn = DatabaseAdapter.getInstance(true);

                InitPage jspPage = new InitPage(dbDyn, request, response,

                        "mill.locale._price_list",

                        Constants.NAME_LANG_PARAM, null, null);



                String indexPage = CtxURL.url(request, response, jspPage.cross, "mill.image.index");



                if(cat.isDebugEnabled())

                    cat.debug("urlString - " + request.getParameter("url_download"));



                if (ServletTools.isNotInit(request, response, "url_download", indexPage))

                    return;

                String urlString = request.getParameter("url_download").trim();



                if(cat.isDebugEnabled())

                    cat.debug("result url_download " + urlString);



                String ext[] = {".jpg", ".jpeg", ".gif", ".png"};

                int i;

                for (i = 0; i < ext.length; i++)

                {

                    if ((ext[i] != null) && urlString.toLowerCase().endsWith(ext[i].toLowerCase()))

                        break;

                }

                if (i == ext.length)

                    throw new UploadFileException("Unsupported file extension. Error #20.03");





                if(cat.isDebugEnabled())

                    cat.debug("id_main - "+ServletTools.getLong(request, "id_main"));



                if (ServletTools.isNotInit(request, response, "id_main", indexPage))

                    return;



                Long id_main = ServletTools.getLong(request, "id_main");





                String desc = ServletUtils.getString(request, "d");





                if (auth_.isUserInRole("webmill.upload_image"))

                {



                    // Todo этот сиквенс просто заглушка, сейчас не работает.

                    // т.к. сиквенс просто использовалс€ чтобы получить уникальное им€ файла

                    CustomSequenceType seq = new CustomSequenceType();

                    seq.setSequenceName("seq_image_number_file");

                    seq.setTableName( "MAIN_FORUM_THREADS");

                    seq.setColumnName( "ID_THREAD" );

                    Long currID = new Long(dbDyn.getSequenceNextValue( seq ));



                    // Todo check was need PropertiesProvider.getApplicationPath(), not PropertiesProvider.getConfigPath()

                    String storage_ = PropertiesProvider.getApplicationPath() + File.separatorChar + "image";

                    String fileName = storage_ + File.separatorChar;



                    if(cat.isDebugEnabled())

                        cat.debug("filename - "+fileName);



                    URL url = new URL(urlString);

                    File fileUrl = new File(url.getFile());



                    if(cat.isDebugEnabled())

                        cat.debug("fileUrl - " + fileUrl);



                    String newFileName = StringTools.appendString("" + currID,

                            '0', 7, true) + "-" + fileUrl.getName();



                    if (cat.isDebugEnabled())

                        cat.debug("newFileName " + newFileName);



                    fileName += newFileName;



                    if (cat.isDebugEnabled())

                        cat.debug("file to write " + fileName);



                    InputStream is = url.openStream();

                    FileOutputStream fos = new FileOutputStream(new File(fileName));

                    byte bytes[] = new byte[1000];

                    int count = 0;

                    while ((count = is.read(bytes)) != -1)

                    {

                        fos.write(bytes, 0, count);

                    }



                    fos.close();

                    fos = null;

                    is.close();

                    is = null;

                    url = null;



                    out.write(DateUtils.getCurrentDate("dd-MMMM-yyyy HH:mm:ss:SS", MainTools.RUlocale()) + "<br>");



                    ps = dbDyn.prepareStatement(

                            "insert into image_dir " +

                            "( id_image_dir, ID_FIRM, is_group, id, id_main, name_file, description )" +

                            "(select seq_image_dir.nextval, ID_FIRM, 0, ?, ?, ?, ? " +

                            " from auth_user where user_login = ? )");



                    RsetTools.setLong(ps, 1, currID);

                    RsetTools.setLong(ps, 2, id_main);

                    ps.setString(3, "/image/" + newFileName);

                    ps.setString(4, desc);

                    ps.setString(5, auth_.getUserLogin());



                    ps.executeUpdate();

                    dbDyn.commit();



                    out.write(

                            "«агрузка данных прошла без ошибок<br>" +

                            "«агружен файл " + newFileName + "<br>" +

                            DateUtils.getCurrentDate("dd-MMMM-yyyy HH:mm:ss:SS", MainTools.RUlocale()) + "<br>" +

                            "<br>" +

                            "<p><a href=\"" + CtxURL.url(request, response, jspPage.cross, "mill.image.index") + "\">«агрузить данные повторно</a></p><br>" +

                            "<p><a href=\"" + response.encodeURL(CtxURL.ctx()) + '?' + jspPage.addURL + "\">Ќа главную страницу</a></p>"

                    );

                }

            }

            catch (Exception e)

            {

                try

                {

                    dbDyn.rollback();

                }

                catch (Exception e1)

                {

                }



                out.write(e.toString());

            }

            finally

            {

                if (ps != null)

                {

                    try

                    {

                        ps.close();

                        ps = null;

                    }

                    catch (Exception e02)

                    {

                    }

                }

                if (dbDyn != null)

                {

                    DatabaseAdapter.close(dbDyn);

                    dbDyn = null;

                }



            }

        }

        catch (Exception e)

        {

            cat.error(e);

            out.write(org.riverock.common.tools.ExceptionTools.getStackTrace(e, 20, "<br>"));

        }



    }

}

