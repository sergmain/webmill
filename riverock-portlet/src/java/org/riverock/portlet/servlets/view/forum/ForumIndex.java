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

 * Time: 2:56:21 PM

 *

 * $Id$

 */



package org.riverock.portlet.servlets.view.forum;



import org.riverock.common.tools.ExceptionTools;

import org.riverock.generic.db.DatabaseAdapter;

import org.riverock.portlet.forum.ForumConfig;

import org.riverock.portlet.forum.SimpleForum;

import org.riverock.portlet.main.Constants;

import org.riverock.webmill.port.InitPage;

import org.riverock.webmill.utils.ServletUtils;



import org.apache.log4j.Logger;



import javax.servlet.ServletException;

import javax.servlet.http.HttpServlet;

import javax.servlet.http.HttpServletRequest;

import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

import java.io.Writer;





public class ForumIndex extends HttpServlet

{

    private static Logger log = Logger.getLogger("org.riverock.portlet.servlets.view.forum.ForumIndex");



    public ForumIndex()

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



            InitPage.setContentType(response, "utf-8");



            DatabaseAdapter db_ = DatabaseAdapter.getInstance( false );



            InitPage  jspPage =  new InitPage(db_, request, response,

                    "mill.locale.forum",

                Constants.NAME_LANG_PARAM+

                Constants.NAME_YEAR_PARAM+

                Constants.NAME_MONTH_PARAM,

                null, null);



            SimpleForum forum = new SimpleForum( request, response, jspPage );



            if (forum.id_forum == null)

            {

                out.write("Forum's ID not defined.");

                return;

            }



            ForumConfig forumCfg =  ForumConfig.getInstance(db_, forum.id_forum);



            String forumType = "";



            if (forumCfg.isAllThread)

                forumType = "/mill.forum_plain";

            else

                forumType = "/mill.forum_standard";



            out.write("\n<!-- forum: "+forumType+" -->\n");



            ServletUtils.include(request, response, forumType, out);

        }

        catch (Exception e)

        {

            log.error(e);

            out.write(ExceptionTools.getStackTrace(e, 20, "<br>"));

        }



    }

}

