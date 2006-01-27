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
package org.riverock.portlet.register.dao;

import java.sql.SQLException;
import java.util.StringTokenizer;

import javax.mail.internet.InternetAddress;
import javax.portlet.PortletException;

import org.apache.log4j.Logger;

import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.db.DatabaseManager;
import org.riverock.module.action.ModuleActionRequest;
import org.riverock.portlet.register.bean.CreateAccountBean;
import org.riverock.portlet.register.RegisterConstants;

/**
 * @author SergeMaslyukov
 *         Date: 30.11.2005
 *         Time: 23:01:51
 *         $Id$
 */
public class CreateAccountDAO {
    private final static Logger log = Logger.getLogger( CreateAccountDAO.class );

    public int execute( ModuleActionRequest moduleActionRequest, CreateAccountBean bean ) throws Exception {

        log.debug("start createAccountDAO"); 

        DatabaseAdapter dbDyn = null;
        try {


            InternetAddress mailAddr = new InternetAddress( bean.getEmail() );

            dbDyn = DatabaseAdapter.getInstance();

            if( log.isDebugEnabled() ) {
                log.debug( "#1.0005" );
            }

            Long countRecord = DatabaseManager.getLongValue(
                dbDyn,
                "select count(*) COUNT_RECORDS " +
                "from   WM_AUTH_USER " +
                "where  USER_LOGIN=?",
                new Object[]{ bean.getUsername() }
            );

            if( log.isDebugEnabled() ) {
                log.debug( "User exists: " + (countRecord!=0) );
            }

            if (countRecord!=0) {
                return RegisterConstants.USERNAME_ALREADY_EXISTS_STATUS;
            }



            Long userId = InternalAuthProviderTools.addNewUser( dbDyn,
                bean.getFirstName(),
                bean.getLastName(),
                bean.getMiddleName(),
                bean.getCompanyId(),
                mailAddr.toString(),
                "", // address
                ""  // phone
            );

            if( log.isDebugEnabled() ) {
                log.debug( "#1.0006 " + bean.getCompanyId() );
            }

            Long authUserId = InternalAuthProviderTools.addUserAuth( dbDyn,
                userId,
                bean.getCompanyId(), null, null,
                bean.getUsername(), bean.getPassword1(),
                true, false, false );

            if( log.isDebugEnabled() ) {
                log.debug( "#1.0007" );
            }


            StringTokenizer st = new StringTokenizer( bean.getRole(), "," );
            while( st.hasMoreTokens() ) {
                String role = st.nextToken();
                Long roleId = InternalAuthProviderTools.getIDRole( dbDyn, role );
                if( roleId == null ) {
                    throw new PortletException( "Role '" + role + "' not exists" );
                }
                InternalAuthProviderTools.bindUserRole( dbDyn, authUserId, roleId );
            }

            if( log.isDebugEnabled() ) {
                log.debug( "#1.0008" );
            }

            dbDyn.commit();

        }
        catch( SQLException e ) {
            try {
                dbDyn.rollback();
            }
            catch( SQLException e1 ) {

            }

            String es = "Error register new user";
            log.error( es, e );
            throw new Exception( es, e );

        }
        finally {
            DatabaseManager.close( dbDyn );
            dbDyn = null;
        }
        return RegisterConstants.OK_STATUS;
    }
}
