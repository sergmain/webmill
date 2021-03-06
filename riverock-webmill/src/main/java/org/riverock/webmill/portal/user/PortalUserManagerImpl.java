/*
 * org.riverock.webmill - Webmill portal with support jsr-168, xml/xslt and others things.
 * For more information, please visit project site http://webmill.riverock.org
 *
 * Copyright (C) 2000-2006, Riverock Software, All Rights Reserved.
 *
 * Riverock - The Open-source Java Development Community
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
 */
package org.riverock.webmill.portal.user;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.portlet.PortletPreferences;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import org.riverock.common.tools.StringTools;
import org.riverock.interfaces.portal.bean.User;
import org.riverock.interfaces.portal.bean.UserOperationStatus;
import org.riverock.interfaces.portal.bean.UserRegistration;
import org.riverock.interfaces.portal.mail.PortalMailServiceProvider;
import org.riverock.interfaces.portal.user.PortalUserManager;
import org.riverock.interfaces.sso.a3.AuthInfo;
import org.riverock.interfaces.sso.a3.bean.RoleBean;
import org.riverock.interfaces.sso.a3.bean.RoleEditableBean;
import org.riverock.webmill.portal.bean.AuthInfoImpl;
import org.riverock.webmill.portal.bean.RoleEditableBeanImpl;
import org.riverock.webmill.portal.bean.UserBean;
import org.riverock.webmill.portal.bean.UserOperationStatusBean;
import org.riverock.webmill.portal.dao.InternalDaoFactory;

/**
 * @author Sergei Maslyukov
 *         Date: 22.06.2006
 *         Time: 20:08:38
 */
@SuppressWarnings({"UnusedAssignment"})
public class PortalUserManagerImpl implements PortalUserManager {
    private final static Logger log = Logger.getLogger(PortalUserManagerImpl.class);

    public static final String USER_DEFAULT_ROLE_METADATA = "webmill.register-default-role";

    private PortalMailServiceProvider mailServiceProvider = null;
    private Long siteId = null;
    private Long companyId = null;
    private PortletPreferences portletPreferences = null;
    private ClassLoader classLoader = null;

    public PortalUserManagerImpl(Long siteId, Long companyId, PortalMailServiceProvider mailServiceProvider, PortletPreferences portletPreferences, ClassLoader classLoader) {
        this.siteId = siteId;
        this.companyId = companyId;
        this.mailServiceProvider = mailServiceProvider;
        this.portletPreferences = portletPreferences;
        this.classLoader = classLoader;
    }

    public UserOperationStatus sendPassword(String eMail, Map<String, String> messages) {
        ClassLoader oldLoader = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader(classLoader);

            try {
                List<User> users = InternalDaoFactory.getInternalUserDao().getUserByEMail(eMail);

                if (users == null) {
                    return new UserOperationStatusBean(PortalUserManager.STATUS_NO_SUCH_EMAIL);
                }

                List<User> grantedUsers = new ArrayList<User>();
                for (User user : users) {
                    if (user.isDeleted()) {
                        continue;
                    }

                    List<AuthInfo> authInfos = InternalDaoFactory.getInternalAuthDao().getAuthInfo(user.getUserId(), siteId);
                    if (authInfos == null || authInfos.isEmpty()) {
                        continue;
                    }
                    grantedUsers.add(user);
                }

                if (grantedUsers.isEmpty()) {
                    return new UserOperationStatusBean(PortalUserManager.STATUS_NOT_REGISTERED);
                }

                for (User grantedUser : grantedUsers) {
                    String subject = messages.get(PortalUserManager.SEND_PASSWORD_SUBJECT_MESSAGE);
                    if (StringUtils.isBlank(subject)) {
                        subject = "Recovery lost info";
                    }

                    String body;
                    List<AuthInfo> authInfos = InternalDaoFactory.getInternalAuthDao().getAuthInfo(grantedUser.getUserId(), siteId);
                    if (authInfos.size()==1) {
                        body = buildBobyForOnePassword(messages, authInfos);
                    }
                    else {
                        body = buildBobyForManyPasswords(messages, authInfos);
                    }

                    String userName = StringTools.getUserName(grantedUser.getFirstName(), grantedUser.getMiddleName(), grantedUser.getLastName());
                    mailServiceProvider.getPortalMailService().sendMessageInUTF8(
                        userName + " <" + eMail + '>', subject, body
                    );
                }
                return new UserOperationStatusBean(PortalUserManager.STATUS_OK_OPERATION);
            }
            catch (Exception e) {
                String es = "Error send password for e-mail: " + eMail;
                log.error(es, e);
                throw new IllegalStateException(es, e);
            }
        }
        finally {
            Thread.currentThread().setContextClassLoader(oldLoader);
        }

    }

    public UserOperationStatus registerNewUser(UserRegistration userRegistration, Map<String, String> messages) {
        ClassLoader oldLoader = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader(classLoader);

            // TODO!!! all operation must in single transaction
            try {

                if (log.isDebugEnabled()) {
                    log.debug("Register user with e-mail: " + userRegistration.getEmail());
                }
                User userTemp = InternalDaoFactory.getInternalAuthDao().getUser(userRegistration.getUserLogin());
                if (log.isDebugEnabled()) {
                    log.debug("Login " + userRegistration.getUserLogin() + " exists: " + (userTemp!=null));
                }
                if (userTemp!=null) {
                    return new UserOperationStatusBean(PortalUserManager.STATUS_LOGIN_ALREADY_REGISTERED);
                }

                List<User> users = InternalDaoFactory.getInternalUserDao().getUserByEMail(userRegistration.getEmail());
                if (log.isDebugEnabled()) {
                    log.debug("Account for e-mail " + userRegistration.getEmail() + " already registered: " + (users != null));
                }

                List<User> grantedUsers = new ArrayList<User>();
                for (User user : users) {
                    if (user.isDeleted()) {
                        continue;
                    }

                    List<AuthInfo> authInfos = InternalDaoFactory.getInternalAuthDao().getAuthInfo(user.getUserId(), siteId);
                    if (authInfos == null || authInfos.isEmpty()) {
                        continue;
                    }
                    grantedUsers.add(user);
                }

                if (!grantedUsers.isEmpty()) {
                    return new UserOperationStatusBean(PortalUserManager.STATUS_EMAIL_ALREADY_REGISTERED);
                }

/*
                for (User user : users) {
                    AuthSession authSession = new AuthSessionImpl(user);
                    if (authSession.checkCompanyId(companyId)!=null) {
                        return new UserOperationStatusBean(PortalUserManager.STATUS_EMAIL_ALREADY_REGISTERED);
                    }
                }
*/

                UserBean user = new UserBean(userRegistration);
                user.setCreatedDate(new Date(System.currentTimeMillis()));
                user.setDeleted(false);
                user.setDeletedDate(null);
                user.setCompanyId(companyId);

                Long userId = InternalDaoFactory.getInternalUserDao().addUser(user);

                // register-default-role
                String roles = null;
                if (portletPreferences != null) {
                    roles = portletPreferences.getValue(USER_DEFAULT_ROLE_METADATA, null);
                }

                if (StringUtils.isBlank(roles)) {
                    return new UserOperationStatusBean(PortalUserManager.STATUS_ROLE_NOT_DEFINED);
                }

                List<RoleEditableBean> roleList = new ArrayList<RoleEditableBean>();
                StringTokenizer st = new StringTokenizer(roles, ",");
                while (st.hasMoreTokens()) {
                    String role = st.nextToken();
                    RoleBean bean = InternalDaoFactory.getInternalAuthDao().getRole(role);
                    if (bean == null) {
                        log.warn("role specified in metadata for register portlet not exist. role: " + role);
                        continue;
                    }

                    RoleEditableBeanImpl roleBean = new RoleEditableBeanImpl(bean);
                    roleBean.setNew(true);
                    roleBean.setDelete(false);

                    roleList.add(roleBean);
                }

                AuthInfoImpl authInfo = new AuthInfoImpl();
                authInfo.setUserLogin(userRegistration.getUserLogin());
                authInfo.setUserPassword(userRegistration.getUserPassword());
                authInfo.setCompany(true);
                authInfo.setHolding(false);
                authInfo.setUserId(userId);

                InternalDaoFactory.getInternalAuthDao().addUserInfo(authInfo, roleList, companyId, null);

                String subject = messages.get(PortalUserManager.CREATE_ACCOUNT_SUBJECT_MESSAGE);
                String body = messages.get(PortalUserManager.CREATE_ACCOUNT_BODY_MESSAGE);
                mailServiceProvider.getPortalMailService().sendMessageInUTF8(
                    StringTools.getUserName(user.getFirstName(), user.getMiddleName(), user.getLastName()) +
                        " <" + userRegistration.getEmail() + '>', subject, body
                );


                return new UserOperationStatusBean(PortalUserManager.STATUS_OK_OPERATION);
            }
            catch (Exception e) {
                String es = "Error register new user";
                log.error(es, e);
                throw new IllegalStateException(es, e);
            }
        }
        finally {
            Thread.currentThread().setContextClassLoader(oldLoader);
        }
    }

    private String buildBobyForManyPasswords(Map<String, String> messages, List<AuthInfo> authInfos) {
        String bodyStart = messages.get(PortalUserManager.SEND_PASSWORD_START_BODY_MANY_PASSWORDS_MESSAGE);
        String bodyEnd = messages.get(PortalUserManager.SEND_PASSWORD_END_BODY_MANY_PASSWORDS_MESSAGE);

        String body = "";
        for (AuthInfo authInfo : authInfos) {
            body += ("\nlogin: " + authInfo.getUserLogin() + ", password: " + authInfo.getUserPassword() + "\n");
        }

        return bodyStart + body + bodyEnd;
    }

    private String buildBobyForOnePassword(Map<String, String> messages, List<AuthInfo> authInfos) {
        AuthInfo authInfo = authInfos.get(0);
        String message = messages.get(PortalUserManager.SEND_PASSWORD_BODY_ONE_PASSWORD_MESSAGE);
        return MessageFormat.format(message, authInfo.getUserLogin(), authInfo.getUserPassword());
    }

}
