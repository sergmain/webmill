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

import java.util.List;

import org.riverock.sso.schema.config.AuthProviderParametersListType;
import org.riverock.interfaces.sso.a3.UserInfo;
import org.riverock.interfaces.sso.a3.AuthInfo;
import org.riverock.interfaces.sso.a3.AuthSession;

/**
 * User: Admin
 * Date: Sep 6, 2003
 * Time: 10:32:15 PM
 *
 * $Id$
 */
public interface AuthProvider {
    public boolean isUserInRole( AuthSession authSession, String role_ );
    public boolean checkAccess( AuthSession authSession, String serverName );
    public void setParameters( AuthProviderParametersListType params );

    public UserInfo initUserInfo( AuthSession authSession );

    public String getGrantedUserId( AuthSession authSession );
    public List<Long> getGrantedUserIdList( AuthSession authSession );

    public String getGrantedCompanyId(AuthSession authSession);
    public List<Long> getGrantedCompanyIdList( AuthSession authSession );

    public String getGrantedGroupCompanyId( AuthSession authSession );
    public List<Long> getGrantedGroupCompanyIdList( AuthSession authSession );

    public String getGrantedHoldingId( AuthSession authSession );
    public List<Long> getGrantedHoldingIdList( AuthSession authSession );

    public Long checkCompanyId(Long companyId, AuthSession authSession );
    public Long checkGroupCompanyId(Long groupCompanyId, AuthSession authSession );
    public Long checkHoldingId(Long holdingId, AuthSession authSession );

    public boolean checkRigthOnUser( Long id_auth_user_check, Long id_auth_user_owner );

    public AuthInfo getAuthInfo(AuthSession authSession);
    public AuthInfo getAuthInfo(AuthSession authSession, Long authUserId);
}
