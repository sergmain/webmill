/*
 * org.riverock.sso - Single Sign On implementation
 *
 * Copyright (C) 2006, Riverock Software, All Rights Reserved.
 *
 * Riverock - The Open-source Java Development Community
 * http://www.riverock.org
 *
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.riverock.sso.a3;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import org.riverock.common.tools.MainTools;
import org.riverock.common.tools.StringTools;
import org.riverock.interfaces.portal.bean.Company;
import org.riverock.interfaces.portal.bean.Holding;
import org.riverock.interfaces.sso.a3.AuthInfo;
import org.riverock.interfaces.sso.a3.AuthProvider;
import org.riverock.interfaces.sso.a3.AuthSession;
import org.riverock.interfaces.sso.a3.AuthUserExtendedInfo;
import org.riverock.interfaces.sso.a3.UserInfo;
import org.riverock.interfaces.sso.a3.bean.AuthParameterBean;
import org.riverock.interfaces.sso.a3.bean.RoleBean;
import org.riverock.sso.bean.AuthParameterBeanImpl;
import org.riverock.sso.config.SsoConfig;
import org.riverock.sso.schema.config.AuthProviderParametersType;
import org.riverock.sso.schema.config.AuthProviderType;
import org.riverock.sso.schema.config.AuthType;
import org.riverock.sso.schema.config.ParameterType;

/**
 * $Id$
 */
public final class AuthSessionImpl implements AuthSession, Serializable {
    private static final long serialVersionUID = 20434672384237876L;
    private final static Logger log = Logger.getLogger(AuthSessionImpl.class);

    private static List<AuthProvider> authProviderList = null;
    private AuthProvider activeProvider = null;
    private boolean isAccessChecked = false;
    private boolean isAccessDenied = true;

    private String userLogin;

    /**
     * Field _userPassword
     */
    private String userPassword;

    /**
     * Field _sessionId
     */
    private String sessionId;

    /**
     * Field _userInfo
     */
    private UserInfo userInfo;


    private final static Object syncObj = new Object();
    public static List<AuthProvider> getAuthProviderList() {
        if (authProviderList==null){
            synchronized(syncObj){
                if (authProviderList==null) {
                    AuthType auth;
                    try{
                        auth = SsoConfig.getAuth();
                    }
                    catch(Throwable e){
                        String es = "Error get parameter from SSO config file";
                        log.error(es, e);
                        throw new IllegalStateException(es, e);
                    }


                    authProviderList = new ArrayList<AuthProvider>();
                    if ( auth!=null ){
                        for (Object o : auth.getAuthProviderAsReference()){
                            AuthProviderType provider = (AuthProviderType) o;
                            if (Boolean.TRUE.equals( provider.getIsUse() ) ){
                                if (log.isInfoEnabled()) {
                                    log.info("Add new auth provider "+provider.getProviderName());
                                    ClassLoader cl = Thread.currentThread().getContextClassLoader();
                                    log.debug("    class loader:\n" + cl +"\nhash: "+ cl.hashCode() );
                                }

                                try{
                                    AuthProvider obj = (AuthProvider)MainTools.createCustomObject( provider.getProviderClass() );

                                    List<List<AuthParameterBean>> lists = new ArrayList<List<AuthParameterBean>>();
                                    if (provider.getProviderParameters()!=null) {
                                        for (Object object : provider.getProviderParameters().getParametersListAsReference() ) {
                                            AuthProviderParametersType params = (AuthProviderParametersType) object;
                                            List<AuthParameterBean> list = new ArrayList<AuthParameterBean>();
                                            lists.add(list);
                                            for (Object o1 : params.getParameterAsReference()) {
                                                ParameterType p = (ParameterType) o1;
                                                AuthParameterBeanImpl bean = new AuthParameterBeanImpl();
                                                bean.setName( p.getName() );
                                                bean.setValue( p.getValue() );
                                                list.add(bean);
                                            }
                                        }
                                    }

                                    obj.setParameters( lists );
                                    authProviderList.add( obj );
                                }
                                catch(Throwable e){
                                    String es = "Error create auth provider, name '"+provider.getProviderName()+"', class "+provider.getProviderClass();
                                    log.error(es, e);
                                    throw new IllegalStateException(es, e);
                                }
                            }
                        }
                    }
                }
            }
        }
        return authProviderList;
    }

    public AuthSessionImpl( UserInfo userInfo ) {
        this.userInfo = userInfo;
    }

    public AuthSessionImpl( final String userLogin, final String userPassword ) {
        this.userLogin = userLogin;
        this.userPassword = userPassword;
    }

    public boolean checkAccess( final String serverName ) {

        isAccessChecked = true;
        isAccessDenied = true;
        if ( StringUtils.isBlank(getUserLogin()) || StringUtils.isBlank(getUserPassword()) ) {
            return false;
        }

        boolean status = false;
        for (AuthProvider authProvider : getAuthProviderList()) {
            if (log.isInfoEnabled())
                log.info("Check access with provider named '" + authProvider + "'");

            status = authProvider.checkAccess(this, serverName);
            if (status) {
                activeProvider = authProvider;
                break;
            }

        }

        if (status) {
            userInfo = activeProvider.getUserInfo( this );
        }

        isAccessDenied = !status;
        return status;
    }

    public boolean isUserInRole( final String roleName ) {
        if (activeProvider==null) {
            log.warn("Access denied by all enabled auth providers");
            return false;
        }

        if (!isAccessChecked) {
            log.warn("Access denied. Firstly check grant to access this site");
            return false;
        }

        if (isAccessDenied)
            return false;

        return activeProvider.isUserInRole( this, roleName );
    }

    public String getName() {
        if (userInfo==null)
            return null;

        String name = StringTools.getUserName( userInfo.getFirstName(), userInfo.getMiddleName(), userInfo.getLastName() );

        if (StringUtils.isBlank(name))
            return null;

        return name;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public String getSessionId() {
        return sessionId;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public String getGrantedUserId() {
        return activeProvider.getGrantedUserId( this );
    }

    public List<Long> getGrantedUserIdList() {
        return activeProvider.getGrantedUserIdList( this );
    }

    public String getGrantedCompanyId() {
        return activeProvider.getGrantedCompanyId( this );
    }

    public List<Long> getGrantedCompanyIdList() {
        return activeProvider.getGrantedCompanyIdList( this );
    }

    public String getGrantedHoldingId() {
        return activeProvider.getGrantedHoldingId( this );
    }

    public List<Long> getGrantedHoldingIdList() {
        return activeProvider.getGrantedHoldingIdList( this );
    }

    public Long checkCompanyId(Long companyId ) {
        return activeProvider.checkCompanyId( companyId, this );
    }

    public Long checkHoldingId(Long holdingId) {
        return activeProvider.checkHoldingId( holdingId, this );
    }

    public boolean checkRigthOnUser(Long id_auth_user_check, Long id_auth_user_owner) {
        return activeProvider.checkRigthOnUser( id_auth_user_check, id_auth_user_owner );
    }

    public AuthInfo getAuthInfo() {
        return activeProvider.getAuthInfo( this );
    }

    public AuthInfo getAuthInfo( Long authUserId ) {
        return activeProvider.getAuthInfo( this, authUserId );
    }

    public List<AuthInfo> getAuthInfoList() {
        return activeProvider.getAuthInfoList( this );
    }

    public RoleBean getRole(Long roleId) {
        return activeProvider.getRole( this, roleId );
    }

    public List<RoleBean> getUserRoleList() {
        return activeProvider.getUserRoleList( this );
    }

    public List<RoleBean> getRoleList() {
        return activeProvider.getRoleList( this );
    }

    public List<RoleBean> getRoleList(Long authUserId) {
        return activeProvider.getRoleList( this, authUserId );
    }

    public Long addRole(RoleBean roleBean) {
        return activeProvider.addRole( this, roleBean );
    }

    public void updateRole(RoleBean roleBean) {
        activeProvider.updateRole( this, roleBean );
    }

    public void deleteRole(RoleBean roleBean) {
        activeProvider.deleteRole( this, roleBean );
    }

    public Long addUser(AuthUserExtendedInfo authInfo) {
        return activeProvider.addUser( this, authInfo );
    }

    public void updateUser(AuthUserExtendedInfo authInfo) {
        activeProvider.updateUser( this, authInfo );
    }

    public void deleteUser(AuthUserExtendedInfo authInfo) {
        activeProvider.deleteUser( this, authInfo );
    }

    public List<UserInfo> getUserList() {
        return activeProvider.getUserList( this );
    }

    public List<Company> getCompanyList() {
        return activeProvider.getCompanyList( this );
    }

    public List<Holding> getHoldingList() {
        return activeProvider.getHoldingList( this );
    }
}
