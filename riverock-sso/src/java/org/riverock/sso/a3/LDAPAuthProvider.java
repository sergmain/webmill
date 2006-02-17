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
import java.util.Hashtable;
import java.util.List;
import java.util.Iterator;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;

import org.apache.log4j.Logger;

import org.riverock.interfaces.sso.a3.UserInfo;
import org.riverock.interfaces.sso.a3.AuthSession;
import org.riverock.interfaces.sso.a3.AuthInfo;
import org.riverock.interfaces.sso.a3.AuthProvider;
import org.riverock.interfaces.sso.a3.AuthUserExtendedInfo;
import org.riverock.interfaces.sso.a3.bean.AuthParameterBean;
import org.riverock.interfaces.sso.a3.bean.RoleBean;
import org.riverock.interfaces.portal.bean.Company;
//import org.riverock.interfaces.portal.bean.GroupCompany;
import org.riverock.interfaces.portal.bean.Holding;

/**
 * User: Admin
 * Date: Sep 6, 2003
 * Time: 10:27:42 PM
 *
 * $Id$
 */
public final class LDAPAuthProvider implements AuthProvider, Serializable {
    private static final long serialVersionUID = 20434672384237117L;
    private final static Logger log = Logger.getLogger(LDAPAuthProvider.class);

    private static final String CANONICAL_NAME_MEMBEROF = "memberOf";

    private String providerUrl = null;
    private DirContext ctx = null;
    public static final String PROVIDER_URL_CONST = "provider-url";

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


    public boolean isUserInRole(final AuthSession authSession, final String role) {
        if (ctx == null || role == null || role.trim().length() == 0)
            return false;

        if (internalCheckRole(authSession, "webmill.root"))
            return true;

        return internalCheckRole(authSession, role);
    }

    private boolean internalCheckRole(final AuthSession authSession, final String role) {
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

    public boolean checkAccess(final AuthSession authSession, final String serverName) {
        if (ctx == null) {
            Hashtable<String, String> env = new Hashtable<String, String>(5, 1.1f);
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

    public void setParameters(List<List<AuthParameterBean>> params) {
        if (params == null)
            return;

        Iterator<List<AuthParameterBean>> iteratorMain = params.iterator();
        while (iteratorMain.hasNext()) {
            List<AuthParameterBean> authParameterBeans = iteratorMain.next();
            Iterator<AuthParameterBean> iterator = authParameterBeans.iterator();
            while (iterator.hasNext()) {
                AuthParameterBean bean = iterator.next();
                if (PROVIDER_URL_CONST.equals(bean.getName())) {
                    providerUrl = bean.getValue();
                }
            }

        }
    }

    public UserInfo getUserInfo(AuthSession authSession) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public String getGrantedUserId(AuthSession authSession) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public List<Long> getGrantedUserIdList(AuthSession authSession) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public String getGrantedCompanyId(AuthSession authSession) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public List<Long> getGrantedCompanyIdList(AuthSession authSession) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

//    public String getGrantedGroupCompanyId(AuthSession authSession) {
//        return null;  //To change body of implemented methods use File | Settings | File Templates.
//    }

//    public List<Long> getGrantedGroupCompanyIdList(AuthSession authSession) {
//        return null;  //To change body of implemented methods use File | Settings | File Templates.
//    }

    public String getGrantedHoldingId(AuthSession authSession) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public List<Long> getGrantedHoldingIdList(AuthSession authSession) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Long checkCompanyId(Long companyId, AuthSession authSession) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

//    public Long checkGroupCompanyId(Long groupCompanyId, AuthSession authSession) {
//        return null;  //To change body of implemented methods use File | Settings | File Templates.
//    }

    public Long checkHoldingId(Long holdingId, AuthSession authSession) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public boolean checkRigthOnUser(Long id_auth_user_check, Long id_auth_user_owner) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public AuthInfo getAuthInfo(AuthSession authSession) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public AuthInfo getAuthInfo(AuthSession authSession, Long authUserId) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public List<AuthInfo> getAuthInfoList(AuthSession authSession) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public RoleBean getRole(AuthSession authSession, Long roleId) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public RoleBean loadRole(AuthSession authSession, Long roleId) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public List<RoleBean> getRoleList(AuthSession authSession) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public List<RoleBean> getRoleList(AuthSession authSession, Long authUserId) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    // get list of granted roles for specific user
    public List<RoleBean> getUserRoleList(AuthSession authSession) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Long addRole(AuthSession authSession, RoleBean roleBean) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void updateRole(AuthSession authSession, RoleBean roleBean) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void deleteRole(AuthSession authSession, RoleBean roleBean) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public Long addUser(AuthSession authSession, AuthUserExtendedInfo infoAuth) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void updateUser(AuthSession authSession, AuthUserExtendedInfo infoAuth) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void deleteUser(AuthSession authSession, AuthUserExtendedInfo infoAuth) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public List<UserInfo> getUserList(AuthSession authSession) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public List<Company> getCompanyList(AuthSession authSession) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

//    public List<GroupCompany> getGroupCompanyList(AuthSession authSession) {
//        return null;  //To change body of implemented methods use File | Settings | File Templates.
//    }

    public List<Holding> getHoldingList(AuthSession authSession) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

}
