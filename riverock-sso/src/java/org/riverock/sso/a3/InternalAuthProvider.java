/*
 * org.riverock.sso -- Single Sign On implementation
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
package org.riverock.sso.a3;

import java.io.Serializable;
import java.util.List;

import org.apache.log4j.Logger;

import org.riverock.interfaces.sso.a3.UserInfo;
import org.riverock.interfaces.sso.a3.AuthInfo;
import org.riverock.sso.dao.AuthDao;
import org.riverock.sso.dao.AuthDaoImpl;
import org.riverock.sso.main.MainUserInfo;
import org.riverock.sso.schema.config.AuthProviderParametersListType;

/**
 * User: Admin
 * Date: Sep 6, 2003
 * Time: 10:33:28 PM
 *
 * $Id$
 */
public final class InternalAuthProvider implements AuthProvider, Serializable {
    private static final long serialVersionUID = 20434672384237113L;

    private final static Logger log = Logger.getLogger( InternalAuthProvider.class );

    private AuthDao authDao = new AuthDaoImpl();

    public InternalAuthProvider() {
    }

    public boolean checkAccess( String userLogin, String userPassword, final String serverName ) {
        return authDao.checkAccess( userLogin, userPassword, serverName );
    }

    public void setParameters( final AuthProviderParametersListType params ) {
    }

    public boolean isUserInRole( String userLogin, String userPassword, final String role_ ) {
        return authDao.isUserInRole(userLogin, userPassword, role_);
    }

    public UserInfo initUserInfo( String userLogin ) {
        try {
            return MainUserInfo.getInstance( userLogin );
        }
        catch( Exception e ) {
            final String es = "Error user info for user login: " + userLogin;
            log.error( es, e );
            throw new IllegalStateException( es, e );
        }
    }

    public String getGrantedUserId( String userLogin ) {
        return authDao.getGrantedUserId( userLogin );
    }

    public List<Long> getGrantedUserIdList( String userLogin ) {
        return authDao.getGrantedUserIdList( userLogin );
    }

    public List<Long> getGrantedCompanyIdList( String userLogin ) {
        return authDao.getGrantedCompanyIdList( userLogin );
    }

    public String getGrantedGroupCompanyId( String userLogin ) {
        return authDao.getGrantedGroupCompanyId( userLogin );
    }

    public List<Long> getGrantedGroupCompanyIdList( String userLogin ) {
        return authDao.getGrantedGroupCompanyIdList( userLogin );
    }

    public String getGrantedHoldingId( String userLogin ) {
        return authDao.getGrantedHoldingId( userLogin );
    }

    public List<Long> getGrantedHoldingIdList( String userLogin ) {
        return authDao.getGrantedHoldingIdList( userLogin );
    }

    public Long checkCompanyId(Long companyId , String userLogin ) {
        return authDao.checkCompanyId( companyId, userLogin );
    }

    public Long checkGroupCompanyId(Long groupCompanyId, String userLogin ) {
        return authDao.checkGroupCompanyId( groupCompanyId, userLogin );
    }

    public Long checkHoldingId(Long holdingId, String userLogin ) {
        return authDao.checkHoldingId( holdingId, userLogin );
    }

    public boolean checkRigthOnUser(Long id_auth_user_check, Long id_auth_user_owner) {
        return authDao.checkRigthOnUser( id_auth_user_check, id_auth_user_owner );
    }

    public AuthInfo getAuthInfo(String userLogin, String userPassword) {
        return authDao.getAuthInfo( userLogin, userPassword );
    }

    public AuthInfo getAuthInfo(Long authUserId) {
        return authDao.getAuthInfo( authUserId );
    }
}
