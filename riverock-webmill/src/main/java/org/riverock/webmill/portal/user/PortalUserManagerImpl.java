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

import java.sql.SQLException;
import java.sql.Types;
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
import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.db.DatabaseManager;
import org.riverock.interfaces.portal.bean.User;
import org.riverock.interfaces.portal.bean.UserOperationStatus;
import org.riverock.interfaces.portal.bean.UserRegistration;
import org.riverock.interfaces.portal.mail.PortalMailServiceProvider;
import org.riverock.interfaces.portal.user.PortalUserManager;
import org.riverock.interfaces.sso.a3.AuthInfo;
import org.riverock.interfaces.sso.a3.bean.RoleEditableBean;
import org.riverock.interfaces.sso.a3.bean.RoleBean;
import org.riverock.sso.a3.AuthInfoImpl;
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

    public static final String USER_DEFAULT_ROLE_METADATA = "register-default-role";

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

            DatabaseAdapter db = null;
            try {
                db = DatabaseAdapter.getInstance();
                User user = InternalDaoFactory.getInternalUserDao().getUserByEMail(db, eMail);

                if (user == null) {
                    return new UserOperationStatusBean(PortalUserManager.STATUS_NO_SUCH_EMAIL);
                }
                if (user.isDeleted()) {
                    return new UserOperationStatusBean(PortalUserManager.STATUS_USER_DELETED);
                }

                List<AuthInfo> authInfos = InternalDaoFactory.getInternalAuthDao().getAuthInfo(db, user.getUserId(), siteId);
                if (authInfos == null || authInfos.isEmpty()) {
                    return new UserOperationStatusBean(PortalUserManager.STATUS_NOT_REGISTERED);
                }

                String subject = messages.get(PortalUserManager.SEND_PASSWORD_SUBJECT_MESSAGE);
                if (StringUtils.isBlank(subject)) {
                    subject = "Recovery lost info";
                }

                String body;
                if (authInfos.size() == 1) {
                    body = buildBobyForOnePassword(messages, authInfos);
                } else {
                    body = buildBobyForManyPasswords(messages, authInfos);
                }

                mailServiceProvider.getPortalMailService().sendMessageInUTF8(
                    StringTools.getUserName(user.getFirstName(), user.getMiddleName(),
                        user.getLastName()) + " <" + eMail + '>', subject, body
                );

                return new UserOperationStatusBean(PortalUserManager.STATUS_OK_OPERATION);
            }
            catch (Exception e) {
                String es = "Error search user for e-mail: " + eMail;
                log.error(es, e);
                throw new IllegalStateException(es, e);
            }
            finally {
                DatabaseManager.close(db);
                db = null;
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

            DatabaseAdapter db = null;
            try {
                db = DatabaseAdapter.getInstance();

                if (log.isDebugEnabled()) {
                    log.debug("Register user with e-mail: " + userRegistration.getEmail());
                }
                Long countRecord = DatabaseManager.getLongValue(
                    db,
                    "select count(*) COUNT_RECORDS from WM_AUTH_USER where USER_LOGIN=?",
                    new Object[]{userRegistration.getUserLogin()},
                    new int[] {Types.VARCHAR}
                );
                if (log.isDebugEnabled()) {
                    log.debug("Login " + userRegistration.getUserLogin() + " exists: " + (countRecord > 0));
                }
                if (countRecord > 0) {
                    return new UserOperationStatusBean(PortalUserManager.STATUS_LOGIN_ALREADY_REGISTERED);
                }

                User checkUser = InternalDaoFactory.getInternalUserDao().getUserByEMail(db, userRegistration.getEmail());
                if (log.isDebugEnabled()) {
                    log.debug("Account for e-mail " + userRegistration.getEmail() + " already registered: " + (checkUser != null));
                }
                if (checkUser != null) {
                    return new UserOperationStatusBean(PortalUserManager.STATUS_EMAIL_ALREADY_REGISTERED);
                }

                UserBean user = new UserBean();
                user.setAddress(userRegistration.getAddress());
                user.setCreatedDate(new Date(System.currentTimeMillis()));
                user.setDeleted(false);
                user.setDeletedDate(null);
                user.setEmail(userRegistration.getEmail());
                user.setFirstName(userRegistration.getFirstName());
                user.setLastName(userRegistration.getLastName());
                user.setMiddleName(userRegistration.getMiddleName());
                user.setPhone(userRegistration.getPhone());
                user.setCompanyId(companyId);
                Long userId = InternalDaoFactory.getInternalUserDao().addUser(db, user);

                // register-default-role
                String roles = null;
                if (portletPreferences != null) {
                    roles = portletPreferences.getValue(USER_DEFAULT_ROLE_METADATA, null);
                }
/*
                if (log.isDebugEnabled()) {
                    log.debug("register roles: " + roles);
                    log.debug("metadata map: " + portletMetadata);
                    if (portletMetadata == null) {
                        log.debug("portlet metadata is null");
                    } else {
                        log.debug("portlet metadata:");
                        for (Map.Entry<String, String> entry : portletMetadata.entrySet()) {
                            log.debug("    key: " + entry.getKey() + ", value: " + entry.getValue());
                        }
                    }
                }
*/
                if (StringUtils.isBlank(roles)) {
                    return new UserOperationStatusBean(PortalUserManager.STATUS_ROLE_NOT_DEFINED);
                }

                List<RoleEditableBean> roleList = new ArrayList<RoleEditableBean>();
                StringTokenizer st = new StringTokenizer(roles, ",");
                while (st.hasMoreTokens()) {
                    String role = st.nextToken();
                    RoleBean bean = InternalDaoFactory.getInternalAuthDao().getRole(db, role);
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

                InternalDaoFactory.getInternalAuthDao().addUserInfo(db, authInfo, roleList, companyId, null);

                String subject = messages.get(PortalUserManager.CREATE_ACCOUNT_SUBJECT_MESSAGE);
                String body = messages.get(PortalUserManager.CREATE_ACCOUNT_BODY_MESSAGE);
                mailServiceProvider.getPortalMailService().sendMessageInUTF8(
                    StringTools.getUserName(user.getFirstName(), user.getMiddleName(), user.getLastName()) +
                        " <" + userRegistration.getEmail() + '>', subject, body
                );


                db.commit();
                return new UserOperationStatusBean(PortalUserManager.STATUS_OK_OPERATION);
            }
            catch (Exception e) {
                try {
                    if (db != null)
                        db.rollback();
                }
                catch (SQLException e1) {
                    // catch rollback exception
                }
                String es = "Error register new user";
                log.error(es, e);
                throw new IllegalStateException(es, e);
            }
            finally {
                DatabaseManager.close(db);
                db = null;
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
