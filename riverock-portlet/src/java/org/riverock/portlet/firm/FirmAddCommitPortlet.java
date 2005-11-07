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

package org.riverock.portlet.firm;

import java.io.IOException;
import java.io.Writer;
import java.sql.PreparedStatement;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.Portlet;
import javax.portlet.PortletConfig;
import javax.portlet.PortletSecurityException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.riverock.common.tools.RsetTools;
import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.db.DatabaseManager;
import org.riverock.generic.schema.db.CustomSequenceType;

import org.riverock.portlet.portlets.WebmillErrorPage;
import org.riverock.portlet.tools.RequestTools;
import org.riverock.portlet.main.Constants;
import org.riverock.interfaces.sso.a3.AuthSession;
import org.riverock.webmill.container.tools.PortletService;
import org.riverock.webmill.container.ContainerConstants;




/**
 * Author: mill
 * Date: Dec 3, 2002
 * Time: 10:28:09 AM
 *
 * $Id$
 */
public final class FirmAddCommitPortlet implements Portlet {

    private final static Log log = LogFactory.getLog( FirmAddCommitPortlet.class );

    public FirmAddCommitPortlet() {
    }

    protected PortletConfig portletConfig = null;

    public void init( PortletConfig portletConfig ) {
        this.portletConfig = portletConfig;
    }

    public void destroy() {
        portletConfig = null;
    }

    public static final String ERROR_TEXT = "ERROR_TEXT";
    public static final String ERROR_URL = "ERROR_URL";
    public void render( RenderRequest renderRequest, RenderResponse renderResponse )
        throws IOException {

        Writer out = renderResponse.getWriter();
        WebmillErrorPage.processPortletError(out, null,
            (String)renderRequest.getAttribute( ERROR_TEXT ),
            PortletService.url(ContainerConstants.CTX_TYPE_INDEX, renderRequest, renderResponse ),
            (String)renderRequest.getAttribute( ERROR_URL ));

        out.flush();
        out.close();
    }

    public void processAction( ActionRequest actionRequest, ActionResponse actionResponse ) {

        PreparedStatement ps = null;
        DatabaseAdapter dbDyn = null;
        try {

            AuthSession auth_ = (AuthSession)actionRequest.getUserPrincipal();
            if ( auth_ == null || !auth_.isUserInRole( "webmill.firm_insert" ) ) {
                throw new PortletSecurityException( "You have not enough right" );
            }

                dbDyn = DatabaseAdapter.getInstance();

                String index_page = PortletService.url( "mill.firm.index", actionRequest, actionResponse );

                CustomSequenceType seq = new CustomSequenceType();
                seq.setSequenceName( "seq_MAIN_LIST_FIRM" );
                seq.setTableName( "MAIN_LIST_FIRM" );
                seq.setColumnName( "ID_FIRM" );
                Long sequenceValue = dbDyn.getSequenceNextValue( seq );


                ps = dbDyn.prepareStatement( "insert into MAIN_LIST_FIRM (" +
                    "	ID_FIRM, " +
                    "	full_name, " +
                    "	short_name, " +
                    "	address, " +
                    "	telefon_buh, " +
                    "	telefon_chief, " +
                    "	chief, " +
                    "	buh, " +
                    "	fax, " +
                    "	email, " +
                    "	icq, " +
                    "	short_client_info, " +
                    "	url, " +
                    "	short_info, " +
                    "	is_work, " +
                    "	is_need_recvizit, " +
                    "	is_need_person, " +
                    "	is_search," +
                    "is_deleted" +
                    ")" +

                    ( dbDyn.getIsNeedUpdateBracket() ?"(" :"" ) +

                    " select " +
                    "	?," +
                    "	?," +
                    "	?," +
                    "	?," +
                    "	?," +
                    "	?," +
                    "	?," +
                    "	?," +
                    "	?," +
                    "	?," +
                    "	?," +
                    "	?," +
                    "	?," +
                    "	?," +
                    "	?," +
                    "	?," +
                    "	?," +
                    "	?,0 from AUTH_USER " +
                    "where USER_LOGIN=? " +
                    ( dbDyn.getIsNeedUpdateBracket() ?")" :"" ) );

                int num = 1;
                RsetTools.setLong( ps, num++, sequenceValue );
                ps.setString( num++, RequestTools.getString( actionRequest, "full_name" ) );
                ps.setString( num++, RequestTools.getString( actionRequest, "short_name" ) );
                ps.setString( num++, RequestTools.getString( actionRequest, "address" ) );
                ps.setString( num++, RequestTools.getString( actionRequest, "telefon_buh" ) );
                ps.setString( num++, RequestTools.getString( actionRequest, "telefon_chief" ) );
                ps.setString( num++, RequestTools.getString( actionRequest, "chief" ) );
                ps.setString( num++, RequestTools.getString( actionRequest, "buh" ) );
                ps.setString( num++, RequestTools.getString( actionRequest, "fax" ) );
                ps.setString( num++, RequestTools.getString( actionRequest, "email" ) );
                RsetTools.setLong( ps, num++, PortletService.getLong( actionRequest, "icq" ) );
                ps.setString( num++, RequestTools.getString( actionRequest, "short_client_info" ) );
                ps.setString( num++, RequestTools.getString( actionRequest, "url" ) );
                ps.setString( num++, RequestTools.getString( actionRequest, "short_info" ) );
                RsetTools.setLong( ps, num++, PortletService.getLong( actionRequest, "is_work" ) );
                RsetTools.setLong( ps, num++, PortletService.getLong( actionRequest, "is_need_recvizit" ) );
                RsetTools.setLong( ps, num++, PortletService.getLong( actionRequest, "is_need_person" ) );
                RsetTools.setLong( ps, num++, PortletService.getLong( actionRequest, "is_search" ) );
                ps.setString( num++, auth_.getUserLogin() );

                int i1 = ps.executeUpdate();

                if ( log.isDebugEnabled() )
                    log.debug( "Count of inserted records - " + i1 );

//                    long servID = InternalAuthProvider.getAuthInfo( auth_ ).serviceID;
//                    AuthTools.setRelateServiceFirm(dbDyn, servID, sequenceValue);

                dbDyn.commit();
                actionResponse.sendRedirect( index_page );
                return;
        }
        catch( Exception e ) {
            try {
                if (dbDyn!=null)
                    dbDyn.rollback();
            }
            catch( Exception e001 ) {
            }

            actionResponse.setRenderParameter(
                ERROR_TEXT,
                "Error add new firm " + WebmillErrorPage.getErrorMessage(e)
            );
            actionResponse.setRenderParameter( ERROR_URL, "continue" );
        }
        finally {
            DatabaseManager.close( dbDyn, ps );
            ps = null;
            dbDyn = null;
        }
    }
}
