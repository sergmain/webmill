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

package org.riverock.portlet.forum;

import java.io.IOException;
import java.io.Writer;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.Portlet;
import javax.portlet.PortletConfig;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.riverock.common.tools.ExceptionTools;
import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.portlet.core.GetMainForumItem;
import org.riverock.portlet.schema.core.MainForumItemType;
import org.riverock.webmill.portlet.PortletTools;

import org.apache.log4j.Logger;


public class ForumPortlet implements Portlet {

    private static Logger log = Logger.getLogger( ForumPortlet.class );

    public ForumPortlet(){}

    private PortletConfig portletConfig = null;
    public void init(PortletConfig portletConfig) throws PortletException {
        this.portletConfig = portletConfig;
    }

    public void destroy() {
    }

    public void processAction(ActionRequest actionRequest, ActionResponse actionResponse) throws PortletException, IOException {
    }

    public void render(RenderRequest renderRequest, RenderResponse renderResponse) throws PortletException, IOException
    {
        Writer out = null;
        DatabaseAdapter db_ = null;
        try
        {
            out = renderResponse.getWriter();

//            ContextNavigator.setContentType(renderResponse, "utf-8");

            db_ = DatabaseAdapter.getInstance( false );

            ForumInstance forum = new ForumInstance( renderRequest, renderResponse );

            if (forum.id_forum == null)
            {
                out.write("ForumPortlet's ID not defined.");
                return;
            }

            MainForumItemType forumCfg = GetMainForumItem.getInstance(db_, forum.id_forum).item;

            String forumType = "";

            if (Boolean.TRUE.equals(forumCfg.getIsAllThread()) )
                forumType = "/mill.forum_plain";
            else
                forumType = "/mill.forum_standard";

            out.write("\n<!-- forum: "+forumType+" -->\n");

            PortletTools.include(
                portletConfig.getPortletContext(),
                renderRequest, renderResponse, forumType, out
            );
//            ServletUtils.include(request_, response, null, forumType, out);
        }
        catch (Exception e){
            log.error(e);
            out.write(ExceptionTools.getStackTrace(e, 20, "<br>"));
        }
        finally{
            DatabaseAdapter.close(db_);
            db_ = null;
        }
    }
}
