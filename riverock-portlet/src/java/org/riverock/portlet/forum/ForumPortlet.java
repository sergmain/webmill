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

package org.riverock.portlet.forum;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Enumeration;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.Portlet;
import javax.portlet.PortletConfig;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.portlet.core.GetMainForumItem;
import org.riverock.portlet.schema.core.MainForumItemType;
import org.riverock.webmill.portlet.PortletTools;
import org.riverock.webmill.portlet.wrapper.StreamWrapper;

import org.apache.log4j.Logger;

/**
 * Author: mill
 * Date: Dec 3, 2002
 * Time: 2:56:21 PM
 *
 * $Id$
 */
public final class ForumPortlet implements Portlet {

    private final static Logger log = Logger.getLogger( ForumPortlet.class );

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
        DatabaseAdapter db_ = null;
        OutputStream outputStream = null;
        try {
            if (log.isDebugEnabled()) {
                for (Enumeration en = renderRequest.getParameterNames(); en.hasMoreElements();) {
                    String s = (String) en.nextElement();
                    log.debug("Parameter: " + s + ", value - " + PortletTools.getString(renderRequest, s));
                }
            }
            outputStream = renderResponse.getPortletOutputStream();
            StreamWrapper out = new StreamWrapper(outputStream);

            db_ = DatabaseAdapter.getInstance( false );

            ForumInstance forum = new ForumInstance( renderRequest );

            if ( forum.id_forum == null ) {
                out.write( "ForumPortlet's ID not defined." );
                return;
            }

            MainForumItemType forumCfg = GetMainForumItem.getInstance( db_, forum.id_forum ).item;

            if ( Boolean.TRUE.equals( forumCfg.getIsAllThread() ) ) {
                out.write( "\n<!-- plain forum -->\n" );
                ForumPlain.process( outputStream, renderRequest, renderResponse, portletConfig );
            } else {
                out.write( "\n<!-- standard forum -->\n" );
                ForumStandard.process( outputStream, renderRequest, renderResponse, portletConfig );
            }
        }
        catch( Exception e ) {
            String es = "Error render forum portlet";
            log.error( es, e);
            throw new PortletException( es, e );
        }
        finally{
            DatabaseAdapter.close( db_ );
            db_ = null;
        }
        outputStream.flush();
        outputStream.close();
        outputStream = null;
    }
}
