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

 * Time: 1:32:02 PM

 *

 * $Id$

 */



package org.riverock.portlet.servlets.controller.image;



import java.io.File;

import java.io.IOException;

import java.io.Writer;

import java.sql.PreparedStatement;



import javax.servlet.ServletException;

import javax.servlet.http.HttpServlet;

import javax.servlet.http.HttpServletRequest;

import javax.servlet.http.HttpServletResponse;

import javax.servlet.http.HttpSession;

import javax.portlet.PortletSession;



import org.apache.log4j.Logger;

import org.riverock.common.config.PropertiesProvider;

import org.riverock.common.tools.ExceptionTools;

import org.riverock.common.tools.RsetTools;

import org.riverock.common.tools.StringTools;

import org.riverock.generic.db.DatabaseAdapter;

import org.riverock.generic.db.DatabaseManager;

import org.riverock.generic.schema.db.CustomSequenceType;

import org.riverock.sso.a3.AuthSession;

import org.riverock.sso.a3.AuthTools;

import org.riverock.sso.schema.MainUserInfoType;

import org.riverock.webmill.main.UploadFile;

import org.riverock.webmill.main.UploadFileException;

import org.riverock.webmill.port.InitPage;

import org.riverock.webmill.portlet.CtxURL;

import org.riverock.webmill.portlet.CtxInstance;



public class ImageUpload extends HttpServlet

{

    private static Logger log = Logger.getLogger( ImageUpload.class );



    public ImageUpload()

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

            CtxInstance ctxInstance =

                (CtxInstance)request_.getSession().getAttribute( org.riverock.webmill.main.Constants.PORTLET_REQUEST_SESSION );



            AuthSession auth_ = AuthTools.check(ctxInstance.getPortletRequest(), response, "/");

            if (auth_ == null)

                return;



            if(log.isDebugEnabled())

                log.debug("Start commit new image from file");



            db_ = DatabaseAdapter.getInstance( true );

            String index_page = CtxURL.url(ctxInstance.getPortletRequest(), response, ctxInstance.page, "mill.image.index" );



            if(log.isDebugEnabled())

                log.debug("right to commit image - "+auth_.isUserInRole("webmill.upload_image"));



            if (auth_.isUserInRole("webmill.upload_image"))

            {



                PortletSession sess = ctxInstance.getPortletRequest().getPortletSession(true);

                if ((sess.getAttribute("MILL.IMAGE.ID_MAIN") == null) ||

                        (sess.getAttribute("MILL.IMAGE.DESC_IMAGE") == null))

                {

                    response.sendRedirect(index_page);

                    return;

                }



                Long id_main = (Long) sess.getAttribute("MILL.IMAGE.ID_MAIN");

                String desc = ((String) sess.getAttribute("MILL.IMAGE.DESC_IMAGE"));



                if(log.isDebugEnabled())

                    log.debug("image description "+desc);



                // Todo this sequence not work

                // 'cos this sequence was used only for recieve unique file name

                CustomSequenceType seq = new CustomSequenceType();

                seq.setSequenceName("seq_image_number_file");

                seq.setTableName( "MAIN_FORUM_THREADS");

                seq.setColumnName( "ID_THREAD" );

                Long currID = new Long(db_.getSequenceNextValue( seq ) );



                // Todo xxx work around with hacked URL - "../../.."

                // Todo check was need PropertiesProvider.getApplicationPath(), not PropertiesProvider.getConfigPath()

                String storage_ = PropertiesProvider.getApplicationPath() + File.separatorChar + "image";

                String fileName =

                        storage_ + File.separator +

                        StringTools.appendString("" + currID, '0', 7, true) + "-";



                if(log.isDebugEnabled())

                    log.debug("image fileName "+fileName);



                String newFileName = "";

                String supportExtension[] = {".jpg", ".jpeg", ".gif", ".png"};

                try

                {

                    // Todo need fix

                    if (true) throw new UploadFileException("Todo need fix");

//                    newFileName =

//                            UploadFile.save(request, 1024 * 128, fileName, true,

//                                    supportExtension);

                }

                catch (UploadFileException e)

                {

                    log.error("Error save image to disk", e);

                    out.write("<html><head></head<body>" +

                            "Error while processing this page:<br>" +

                            ExceptionTools.getStackTrace(e, 20, "<br>") + "<br>" +

                            "<p><a href=\"" + index_page + "\">continue</a></p>" +

                            "</body></html>"

                    );

                    return;

                }



                if(log.isDebugEnabled())

                    log.debug("newFileName "+newFileName);



                PreparedStatement ps = null;

                try

                {

                    MainUserInfoType userInfo = auth_.getUserInfo();



                    CustomSequenceType seqImageDir = new CustomSequenceType();

                    seqImageDir.setSequenceName("seq_image_dir");

                    seqImageDir.setTableName( "IMAGE_DIR");

                    seqImageDir.setColumnName( "ID_IMAGE_DIR" );

                    Long seqValue = new Long(db_.getSequenceNextValue( seqImageDir ));



                    ps = db_.prepareStatement(

                            "insert into IMAGE_DIR " +

                            "( ID_IMAGE_DIR, ID_FIRM, is_group, id, id_main, name_file, description )" +

                            "(?, ?, 0, ?, ?, ?, ?");



                    RsetTools.setLong(ps, 1, seqValue);

                    RsetTools.setLong(ps, 2, userInfo.getIdFirm() );

                    RsetTools.setLong(ps, 3, currID);

                    RsetTools.setLong(ps, 4, id_main);

                    ps.setString(5, "/image/" + newFileName);

                    ps.setString(6, desc);



                    ps.executeUpdate();



                    db_.commit();



                    if(log.isDebugEnabled())

                        log.debug("redirect to indexPage - "+index_page);



                    response.sendRedirect(index_page);

                    return;



                }

                catch (Exception e)

                {

                    db_.rollback();

                    out.write("<html><head></head<body>" +

                            "Error while processing this page:<br>" +

                            ExceptionTools.getStackTrace(e, 20, "<br>") + "<br>" +

                            "<p><a href=\"" + index_page + "\">continue</a></p>" +

                            "</body></html>"

                    );

                    return;

                }

                finally

                {

                    DatabaseManager.close( ps );

                    ps = null;

                }



            }

        }

        catch (Exception e)

        {

            log.error(e);

            out.write(ExceptionTools.getStackTrace(e, 20, "<br>"));

        }

        finally

        {

            DatabaseManager.close( db_ );

            db_ = null;

        }



    }

}

