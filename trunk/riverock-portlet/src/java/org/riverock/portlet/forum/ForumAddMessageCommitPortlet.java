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

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.Portlet;
import javax.portlet.PortletConfig;
import javax.portlet.PortletException;
import javax.portlet.PortletSession;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.schema.db.CustomSequenceType;
import org.riverock.portlet.main.Constants;
import org.riverock.webmill.portlet.PortletTools;

import org.apache.log4j.Logger;

/**
 * Author: mill
 * Date: Dec 3, 2002
 * Time: 2:34:28 PM
 *
 * $Id$
 */
public final class ForumAddMessageCommitPortlet implements Portlet {

    private final static Logger log = Logger.getLogger( ForumAddMessageCommitPortlet.class );

    public ForumAddMessageCommitPortlet() {
    }

    protected PortletConfig portletConfig = null;
    public void init( PortletConfig portletConfig ) {
        this.portletConfig = portletConfig;
    }

    public void destroy() {
        portletConfig = null;
    }

    private static CustomSequenceType getNewThreadIdSequence()
    {
        CustomSequenceType seq = new CustomSequenceType();
        seq.setSequenceName( ForumInstance.SEQ_FORUM_THREADS );
        seq.setTableName( ForumInstance.FORUM_THREADS_TABLE );
        seq.setColumnName( "ID_THREAD" );

        return seq;
    }

    public void render( RenderRequest renderRequest, RenderResponse renderResponse )
        throws PortletException {
        throw new PortletException( "render() method must never invoked" );
    }

    public void processAction( ActionRequest actionRequest, ActionResponse actionResponse )
        throws PortletException {

        DatabaseAdapter db_ = null;
        try {


            ForumInstance.setCookie( actionRequest );

            ForumInstance forum = new ForumInstance(actionRequest);

            if ( forum.id_forum == null ) {
                throw new PortletException( "Forum's ID not defined." );
            }

            Long id = PortletTools.getLong( actionRequest,
                Constants.NAME_ID_MESSAGE_FORUM_PARAM );

            // Add forum message
            boolean isAdd = PortletTools.getString( actionRequest, "action" ).equals( "add" );
            db_ = DatabaseAdapter.getInstance( false );

            if ( isAdd ) {

                Long id_main = PortletTools.getLong( actionRequest, Constants.NAME_ID_MAIN_FORUM_PARAM );
                Long id_thread = null;
                if ( id_main == null ) {
                    id_main = new Long( 0 );
                    id_thread = new Long( db_.getSequenceNextValue( getNewThreadIdSequence() ) );
                } else
                    id_thread = forum.getIdThread( id_main );

                if ( id_thread == null ) {
                    id_main = new Long( 0 );
                    id_thread = new Long( db_.getSequenceNextValue( getNewThreadIdSequence() ) );
                }

                id = forum.addMessage( id_thread, id_main );
            }
            String subject = PortletTools.getString( actionRequest, "nameForumSubject_", "" );

            PortletSession session = actionRequest.getPortletSession( true );
            subject = (String)session.getAttribute( Constants.FORUM_SUBJECT_SESSION );
            if ( subject == null )
                subject = "";
            session.removeAttribute( Constants.FORUM_SUBJECT_SESSION );

            if ( ( subject.trim().length()>0 ) && ( !subject.toUpperCase().startsWith( "RE: " ) ) )
                subject = "RE: " + subject;

            String url = PortletTools.url( Constants.CTX_TYPE_FORUM, actionRequest, actionResponse ) + '&' +
                Constants.NAME_ID_FORUM_PARAM + '=' + forum.id_forum + '&' +
                Constants.NAME_ID_MESSAGE_FORUM_PARAM + '=' + id;

            actionResponse.sendRedirect( url );
            return;

        }
        catch( Exception e ) {
            String es = "Error add new forum message";
            log.error( es, e );
            throw new PortletException( es, e );
        }
        finally {
            DatabaseAdapter.close( db_ );
            db_ = null;
        }

    }
}
