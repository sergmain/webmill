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

 * Time: 2:34:28 PM

 *

 * $Id$

 */



package org.riverock.portlet.servlets.controller.forum;



import java.io.IOException;

import java.io.Writer;



import javax.servlet.ServletException;

import javax.servlet.http.HttpServletRequest;

import javax.servlet.http.HttpServletResponse;

import javax.servlet.http.HttpSession;

import javax.servlet.http.HttpServlet;



import org.apache.log4j.Logger;



import org.riverock.generic.db.DatabaseAdapter;

import org.riverock.generic.schema.db.CustomSequenceType;

import org.riverock.portlet.forum.Forum;

import org.riverock.portlet.forum.SimpleForum;

import org.riverock.portlet.main.Constants;

import org.riverock.webmill.port.InitPage;

import org.riverock.webmill.portlet.CtxURL;

import org.riverock.webmill.portlet.ContextNavigator;

import org.riverock.webmill.utils.ServletUtils;

import org.riverock.common.tools.ExceptionTools;

import org.riverock.common.tools.ServletTools;







public class ForumAddMessageCommit extends HttpServlet

{

    private static Logger log = Logger.getLogger("org.riverock.portlet.servlets.controller.forum.ForumAddMessageCommit");



    public ForumAddMessageCommit()

    {

    }



    public void doPost(HttpServletRequest request, HttpServletResponse response)

            throws IOException, ServletException

    {

        if (log.isDebugEnabled())

            log.debug("method is POST");



        doGet(request, response);

    }



    private static CustomSequenceType getNewThreadIdSequence()

    {

        CustomSequenceType seq = new CustomSequenceType();

        seq.setSequenceName( Forum.SEQ_FORUM_THREADS );

        seq.setTableName( Forum.FORUM_THREADS_TABLE );

        seq.setColumnName( "ID_THREAD" );



        return seq;

    }



    public void doGet(HttpServletRequest request, HttpServletResponse response)

            throws IOException, ServletException

    {

        Writer out = null;

        try

        {

            ContextNavigator.setContentType(response);



            out = response.getWriter();



            ContextNavigator.setContentType(response, "utf-8");



            Forum.setCookie(request, response);



            DatabaseAdapter db_ = DatabaseAdapter.getInstance(false);



            InitPage jspPage = new InitPage(db_, request,

                                            "mill.locale.forum"

            );



            SimpleForum forum = new SimpleForum(request, response, jspPage);



            if (forum.id_forum == null)

            {

                out.write("Forum's ID not defined.");

                return;

            }



            Long id = ServletTools.getLong(request,

                    Constants.NAME_ID_MESSAGE_FORUM_PARAM);



// Add forum message

            boolean isAdd = ServletUtils.getString(request, "action").equals("add");

            if (isAdd)

            {



                Long id_main = ServletTools.getLong(request, Constants.NAME_ID_MAIN_FORUM_PARAM);

                Long id_thread = null;

                if (id_main == null)

                {

                    id_main = new Long(0);

                    id_thread = new Long(db_.getSequenceNextValue(getNewThreadIdSequence()) );

                }

                else

                    id_thread = forum.getIdThread(id_main);



                if (id_thread == null)

                {

                    id_main = new Long(0);

                    id_thread = new Long(db_.getSequenceNextValue(getNewThreadIdSequence()));

                }



                id = forum.addMessage(request, id_thread, id_main);





//                String forum_name = "";

//                String forum_email = "";



//                forum_email = ServletUtils.getString(request, "e");

//                forum_name = ServletUtils.getString(request, "n");

            }

            String subject = ServletUtils.getString(request, "nameForumSubject_", "");



            HttpSession session = request.getSession(true);

            subject = (String) session.getAttribute(Constants.FORUM_SUBJECT_SESSION);

            if (subject == null)

                subject = "";

            session.removeAttribute(Constants.FORUM_SUBJECT_SESSION);



            if ((subject.trim().length() > 0) && (!subject.toUpperCase().startsWith("RE: ")))

                subject = "RE: " + subject;



//            String nameTemplate = (String) session.getAttribute(Constants.TEMPLATE_NAME_SESSION);

            String nameTemplate = request.getParameter(

                Constants.NAME_TEMPLATE_CONTEXT_PARAM

            );

            String url = (

                    CtxURL.ctx() + '?' + jspPage.getAsURL() +

                    Constants.NAME_ID_FORUM_PARAM + '=' + forum.id_forum + '&' +

                    Constants.NAME_ID_MESSAGE_FORUM_PARAM + '=' + id + '&' +

                    Constants.NAME_TYPE_CONTEXT_PARAM + '=' + Constants.CTX_TYPE_FORUM + '&' +

                    Constants.NAME_TEMPLATE_CONTEXT_PARAM + '=' + nameTemplate

                    );



            response.sendRedirect(url);

            return;



        }

        catch (Exception e)

        {

            log.error(e);

            out.write(ExceptionTools.getStackTrace(e, 20, "<br>"));

        }



    }

}

