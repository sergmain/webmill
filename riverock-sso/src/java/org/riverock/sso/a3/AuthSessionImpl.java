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
import java.util.ArrayList;

import org.riverock.sso.schema.config.AuthType;
import org.riverock.sso.schema.config.AuthProviderType;
import org.riverock.sso.config.SsoConfig;
import org.riverock.common.tools.MainTools;
import org.riverock.common.tools.StringTools;
import org.riverock.interfaces.sso.a3.AuthSession;
import org.riverock.interfaces.sso.a3.AuthException;
import org.riverock.interfaces.sso.a3.UserInfo;

import org.apache.log4j.Logger;

/**
 * $Id$
 */
public final class AuthSessionImpl implements AuthSession {
    private static final long serialVersionUID = 20434672384237876L;
    private final static Logger log = Logger.getLogger(AuthSessionImpl.class);

    private transient static List authProviderList = null;
    private transient AuthProviderInterface activeProvider = null;
    private transient boolean isAccessChecked = false;
    private transient boolean isAccessDenied = true;


    private java.lang.String userLogin;

    /**
     * Field _userPassword
     */
    private java.lang.String userPassword;

    /**
     * Field _sessionId
     */
    private java.lang.String sessionId;

    /**
     * Field _userInfo
     */
    private UserInfo userInfo;


    private transient static Object syncObj = new Object();
    public static List getAuthProviderList() throws AuthException {
        if (authProviderList==null){
            synchronized(syncObj){
                if (authProviderList==null) {
                    AuthType auth = null;
                    try{
                        auth = SsoConfig.getAuth();
                    }
                    catch(Throwable e){
                        String es = "Error get parameter from SSO config file";
                        log.error(es, e);
                        throw new AuthException(es, e);
                    }


                    authProviderList = new ArrayList();
                    if ( auth!=null ){
                        for (int i=0; i<auth.getAuthProviderCount(); i++){
                            AuthProviderType provider = auth.getAuthProvider(i);
                            if (Boolean.TRUE.equals( provider.getIsUse() ) ){
                                if (log.isInfoEnabled())
                                    log.info("Add new auth provider "+provider.getProviderName());

                                try{
                                    AuthProviderInterface obj = (AuthProviderInterface)MainTools.createCustomObject( provider.getProviderClass() );
                                    obj.setParameters( provider.getProviderParameters() );
                                    authProviderList.add( obj );
                                }
                                catch(Throwable e){
                                    String es = "Error create auth provider, name '"+provider.getProviderName()+"', class "+provider.getProviderClass();
                                    log.error(es, e);
                                    throw new AuthException(es, e);
                                }
                            }
                        }
                    }
                }
            }
        }
        return authProviderList;
    }

    protected void finalize() throws Throwable
    {
        setUserLogin(null);
        setUserPassword(null);
        setUserInfo(null);
        setSessionId(null);

        super.finalize();
    }

    public AuthSessionImpl() {
    }

    public AuthSessionImpl( UserInfo userInfo ) {
        this.userInfo = userInfo;
    }

    public AuthSessionImpl( final String l_, final String p_ ) {
        setUserLogin(l_);
        setUserPassword(p_);
        if (log.isDebugEnabled()) {
            log.debug("userLogin: " + (l_!=null? (l_.toString()+", class: "+l_.getClass().getName()) :"null") );
            log.debug("userPassword: " + (p_!=null? (p_.toString()+", class: "+p_.getClass().getName()) :"null") );
        }
    }

    public boolean checkAccess( final String serverName ) throws AuthException {

        isAccessChecked = true;
        isAccessDenied = true;
        if ( StringTools.isEmpty(getUserLogin()) || StringTools.isEmpty(getUserPassword()) ) {
            return false;
        }

        boolean status = false;
        for (int i=0; i<getAuthProviderList().size(); i++) {
            AuthProviderInterface provider = (AuthProviderInterface)getAuthProviderList().get(i);
            if (log.isInfoEnabled())
                log.info("Check role with provider named '"+provider+"'");

            try {
                status = provider.checkAccess( this, serverName );
            }
            catch( AuthException e ) {
                log.error( "Check with provider "+provider.getClass().getName()+" failed. ", e );
            }
            if (status) {
                activeProvider = provider;
                break;
            }
        }

        if (status) {
            activeProvider.initUserInfo( this );
        }

        isAccessDenied = !status;
        return status;
    }

    public boolean isUserInRole( final String roleName ) throws AuthException {
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
        if (this.getUserInfo()==null)
            return null;

        String name =
            (this.getUserInfo().getFirstName()!=null?this.getUserInfo().getFirstName()+' ':"")+
            (this.getUserInfo().getMiddleName()!=null?this.getUserInfo().getMiddleName()+' ':"")+
            (this.getUserInfo().getLastName()!=null?this.getUserInfo().getLastName()+' ':"");

        if (name.trim().length()==0)
            return null;

        return name;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }
}
