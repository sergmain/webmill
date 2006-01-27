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

/**
 * User: Admin
 * Date: Sep 6, 2003
 * Time: 10:32:15 PM
 *
 * $Id$
 */
public interface AuthProvider {
    public boolean isUserInRole( String userLogin, String userPassword, final String role_ );
    public boolean checkAccess( String userLogin, String userPassword, final String serverName );
    public void setParameters( AuthProviderParametersListType params );

    public UserInfo initUserInfo( String userLogin );

    public String getGrantedUserId( String userLogin );
    public List<Long> getGrantedUserIdList( String userLogin );
    public List<Long> getGrantedCompanyIdList( String userLogin );
    public String getGrantedGroupCompanyId( String userLogin );
    public List<Long> getGrantedGroupCompanyIdList( String userLogin );
    public String getGrantedHoldingId( String userLogin );
    public List<Long> getGrantedHoldingIdList( String userLogin );

    public Long checkCompanyId(Long companyId, String userLogin );
    public Long checkGroupCompanyId(Long groupCompanyId, String userLogin );
    public Long checkHoldingId(Long holdingId, String userLogin );

    public boolean checkRigthOnUser( Long id_auth_user_check, Long id_auth_user_owner );

    public AuthInfo getAuthInfo(String userLogin, String userPassword);
    public AuthInfo getAuthInfo(Long authUserId);
}
