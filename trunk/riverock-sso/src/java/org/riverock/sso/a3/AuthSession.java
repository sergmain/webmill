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



import java.util.Vector;



import org.riverock.sso.schema.AuthSessionType;

import org.riverock.sso.schema.config.AuthType;

import org.riverock.sso.schema.config.AuthProviderType;

import org.riverock.sso.config.SsoConfig;

import org.riverock.common.tools.MainTools;



import org.apache.log4j.Logger;



public class AuthSession extends AuthSessionType

{

    private static Logger log = Logger.getLogger("org.riverock.sso.a3.AuthSession");



    private static Vector authProviderList = null;

    private AuthProviderInterface activeProvider = null;

    private boolean isAccessChecked = false;

    private boolean isAccessDenied = true;



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



    private static Object syncObj = new Object();

    public AuthSession(String l_, String p_)

        throws AuthException

    {

        setUserLogin(l_);

        setUserPassword(p_);

        if (log.isDebugEnabled())

        {

            log.debug("userLogin param " + l_);

            log.debug("userLogin object " + p_);

        }



        if (authProviderList==null)

        {

            synchronized(syncObj)

            {

                if (authProviderList==null)

                {

                    AuthType auth = null;

                    try

                    {

                        auth = SsoConfig.getAuth();

                    }

                    catch(Exception e)

                    {

                        log.error("Exception get parameter from SSO config file");

                        throw new AuthException(e.toString());

                    }

                    catch(Error e)

                    {

                        log.error("Error get parameter from SSO config file");

                        throw new AuthException(e.toString());

                    }





                    authProviderList = new Vector();

                    if ( auth!=null )

                    {

                        for (int i=0; i<auth.getAuthProviderCount(); i++)

                        {

                            AuthProviderType provider = auth.getAuthProvider(i);

                            if (Boolean.TRUE.equals( provider.getIsUse() ) )

                            {

                                if (log.isInfoEnabled())

                                    log.info("Add new auth provider "+provider.getProviderName());



                                try

                                {

                                    AuthProviderInterface obj = (AuthProviderInterface)MainTools.createCustomObject( provider.getProviderClass() );

                                    obj.setParameters( provider.getProviderParameters() );

                                    authProviderList.add( obj );

                                }

                                catch(Exception e)

                                {

                                    log.error("Exception create auth provider, name '"+provider.getProviderName()+"', class "+provider.getProviderClass());

                                    throw new AuthException(e.toString());

                                }

                                catch(Error e)

                                {

                                    log.error("Error create auth provider, name '"+provider.getProviderName()+"', class "+provider.getProviderClass());

                                    throw new AuthException(e.toString());

                                }

                            }

                        }

                    }

                }

            }

        }

    }



    public boolean checkAccess( String serverName)

        throws Exception

    {

        isAccessChecked = true;

        isAccessDenied = true;

        if ( getUserLogin()==null || getUserLogin().trim().length()==0 || getUserPassword()==null )

            return false;



        boolean status = false;

        for (int i=0; i<authProviderList.size(); i++)

        {

            AuthProviderInterface provider = (AuthProviderInterface)authProviderList.elementAt(i);

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

}

