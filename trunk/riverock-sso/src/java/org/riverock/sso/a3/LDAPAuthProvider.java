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
 * User: Admin
 * Date: Sep 6, 2003
 * Time: 10:27:42 PM
 *
 * $Id$
 */
package org.riverock.sso.a3;

import java.io.Serializable;
import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;

import org.apache.log4j.Logger;

import org.riverock.sso.schema.config.AuthProviderParametersListType;
import org.riverock.sso.schema.config.AuthProviderParametersType;
import org.riverock.sso.schema.config.ParameterType;


public final class LDAPAuthProvider implements AuthProviderInterface, Serializable {
    private static final long serialVersionUID = 20434672384237117L;
    private final static Logger log = Logger.getLogger(LDAPAuthProvider.class);

    private static final String CANONICAL_NAME_MEMBEROF = "memberOf";

    private String providerUrl = null;
    private DirContext ctx = null;

    protected void finalize() throws Throwable {
        if (ctx != null) {
            try {
                ctx.close();
                ctx = null;
            }
            catch (Exception e) {
            }
        }
        super.finalize();
    }


    public boolean isUserInRole(final AuthSessionImpl authSession, final String role) {
        if (ctx == null || role == null || role.trim().length() == 0)
            return false;

        if (internalCheckRole(authSession, "webmill.root"))
            return true;

        return internalCheckRole(authSession, role);
    }

    private boolean internalCheckRole(final AuthSessionImpl authSession, final String role) {
        String[] attrs = new String[]{CANONICAL_NAME_MEMBEROF};

        try {
            Attributes result =
                ctx.getAttributes("CN=" + authSession.getUserLogin() + ",CN=Users", attrs);

            javax.naming.directory.Attribute attr =
                result.get(CANONICAL_NAME_MEMBEROF);

            if (attr != null) {
                NamingEnumeration vals = attr.getAll();
                while (vals.hasMoreElements()) {
                    String m = parseCanonicalName((String) vals.nextElement());
                    if (role.equals(m))
                        return true;
                }
            }
        }
        catch (Exception e) {
            log.error("Exception check role '" + role + "'", e);
            return false;
        }

        return false;
    }

    public static String parseCanonicalName(final String m_) {
        String cn = m_;
        if (!cn.startsWith("CN="))
            return null;

        cn = cn.substring(3);

        int idx = cn.indexOf(',');
        if (idx != -1)
            cn = cn.substring(0, idx);

        return cn;
    }

    public boolean checkAccess(final AuthSessionImpl authSession, final String serverName) {
        if (ctx == null) {
            Hashtable env = new Hashtable(5, 1.1f);
            /*
            * Specify the initial context implementation to use.
            * For example,
            * This could also be set by using the -D option to the java program.
            *   java -Djava.naming.factory.initial=com.sun.jndi.ldap.LdapCtxFactory \
            *       Getattrs
            */
            env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");

            /* Specify host and port to use for directory service */
//    env.put(Context.PROVIDER_URL, Env.MY_SERVICE);
            env.put(Context.PROVIDER_URL, providerUrl);

            env.put(Context.SECURITY_PRINCIPAL, authSession.getUserLogin());
            env.put(Context.SECURITY_CREDENTIALS, authSession.getUserPassword());

            try {
                ctx = new InitialDirContext(env);
            }
//            catch(javax.naming.AuthenticationException e)
//            {
//                ctx = null;
//                log.error("providerUrl "+providerUrl );
//                log.error("authSession.getUserLogin() "+authSession.getUserLogin() );
//                log.error("Exception create InitialDirContext", e);
//                return false;
//            }
            catch (Throwable e) {
                ctx = null;
                log.error("providerUrl " + providerUrl);
                log.error("authSession.getUserLogin() " + authSession.getUserLogin());
                log.error("Exception create InitialDirContext", e);
                return false;
            }
        }
        return true;
    }

    public void setParameters(final AuthProviderParametersListType parametersList) throws Exception {
        if (parametersList == null)
            return;

        for (int i = 0; i < parametersList.getParametersListCount(); i++) {
            AuthProviderParametersType params = parametersList.getParametersList(i);
            for (int k = 0; k < params.getParameterCount(); k++) {
                ParameterType p = params.getParameter(k);
                if ("provider-url".equals(p.getName()))
                    providerUrl = p.getValue();
            }
        }
    }

    public void initUserInfo(AuthSessionImpl authSession) {
    }

}
