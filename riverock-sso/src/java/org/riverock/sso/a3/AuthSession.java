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

/**
 * $Id$
 */
package org.riverock.sso.a3;

import java.util.List;
import java.util.ArrayList;
import java.io.Serializable;
import java.security.Principal;

import org.riverock.sso.schema.AuthSessionType;
import org.riverock.sso.schema.config.AuthType;
import org.riverock.sso.schema.config.AuthProviderType;
import org.riverock.sso.config.SsoConfig;
import org.riverock.common.tools.MainTools;

import org.apache.log4j.Logger;

public class AuthSession extends AuthSessionType implements Serializable, Principal
{
    private static Logger log = Logger.getLogger(AuthSession.class);

    private static List authProviderList = null;
    private AuthProviderInterface activeProvider = null;
    private boolean isAccessChecked = false;
    private boolean isAccessDenied = true;

    private static Object syncObj = new Object();
    public static List getAuthProviderList() throws AuthException{
        if (authProviderList==null){
            synchronized(syncObj){
                if (authProviderList==null){
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

    public AuthSession()
    {
    }

    public AuthSession(String l_, String p_)
    {
        setUserLogin(l_);
        setUserPassword(p_);
        if (log.isDebugEnabled())
        {
            log.debug("userLogin param " + l_);
            log.debug("userLogin object " + p_);
        }
    }

    public boolean checkAccess( String serverName)
        throws AuthException
    {
        isAccessChecked = true;
        isAccessDenied = true;
        if ( getUserLogin()==null || getUserLogin().trim().length()==0 || getUserPassword()==null )
            return false;

        boolean status = false;
        for (int i=0; i<getAuthProviderList().size(); i++)
        {
            AuthProviderInterface provider = (AuthProviderInterface)getAuthProviderList().get(i);
            if (log.isInfoEnabled())
                log.info("Check role with provider named '"+provider+"'");

            status = provider.checkAccess( this, serverName );
            if (status)
            {
                activeProvider = provider;
                break;
            }
        }

        if (status)
        {
            activeProvider.initUserInfo( this );
        }

        isAccessDenied = !status;
        return status;
    }

    public boolean isUserInRole(String roleName)
        throws AuthException
    {
        if (activeProvider==null)
        {
            log.warn("Access denied by all enabled auth providers");
            return false;
        }

        if (!isAccessChecked)
        {
            log.warn("Access denied. Firstly check grant to access this site");
            return false;
        }

        if (isAccessDenied)
            return false;

        return activeProvider.isUserInRole( this, roleName );
    }

    public String getName()
    {
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
}
