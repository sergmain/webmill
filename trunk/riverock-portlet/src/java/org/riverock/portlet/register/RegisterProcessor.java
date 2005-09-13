/*
 * org.riverock.portlet -- Portlet Library
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
package org.riverock.portlet.register;

import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.StringTokenizer;
import java.text.MessageFormat;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.portlet.PortletException;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.riverock.common.mail.MailMessage;
import org.riverock.common.tools.StringTools;
import org.riverock.generic.config.GenericConfig;
import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.sso.a3.AuthSession;
import org.riverock.sso.a3.InternalAuthProviderTools;
import org.riverock.webmill.container.portal.PortalInfo;

/**
 * @author Serge Maslyukov
 *         Date: 19.03.2005
 *         Time: 23:22:07
 *         $Id$
 */
public class RegisterProcessor {

    private final static Log log = LogFactory.getLog(RegisterProcessor.class);

    final static int UNKNOWN_STATUS = -1;
    final static int OK_STATUS = 0;
    final static int USERNAME_IS_NULL_STATUS = 1;
    final static int PASSWORD_NOT_EQUALS_STATUS = 2;
    final static int PASSWORD1_IS_NULL_STATUS = 3;
    final static int PASSWORD2_IS_NULL_STATUS = 4;
    final static int EMAIL_IS_NULL_STATUS = 5;
    final static int EMAIL_IS_WRONG_STATUS = 6;
    final static int ROLE_IS_NULL_STATUS = 7;
    final static int USERNAME_ALREADY_EXISTS_STATUS = 8;
    final static int SERVER_NAME_IS_NULL_STATUS = 9;

        String username = null;
        String password1 = null;
        String password2 = null;
        String email = null;
        String firstName = null;
        String lastName = null;
        String middleName = null;
        String role = null;
        String serverName = null;
        ResourceBundle bundle = null;

        int registerStatus = UNKNOWN_STATUS;
        Throwable throwable = null;

        PortalInfo portalInfo = null;

        public RegisterProcessor(String username, String password1, String password2,
            String email, String firstName, String lastName, String middleName,
            String role, String serverName,
            ResourceBundle bundle, PortalInfo portalInfo) {
            this.username = username;
            this.password1 = password1;
            this.password2 = password2;
            this.email = email;
            this.firstName = firstName;
            this.lastName = lastName;
            this.middleName = middleName;
            this.role = role;
            this.portalInfo = portalInfo;
            this.serverName = serverName;
            this.bundle = bundle;

            checkRegisterData();
        }

        void destroy() {
            username = null;
            password1 = null;
            password2 = null;
            email = null;
            firstName = null;
            lastName = null;
            middleName = null;
            role = null;
            throwable = null;
            portalInfo = null;
            serverName = null;
            bundle = null;
        }

        private void checkRegisterData() {

            if (username == null) {
                registerStatus = USERNAME_IS_NULL_STATUS;
                return;
            }

            if (password1 == null) {
                registerStatus = PASSWORD1_IS_NULL_STATUS;
                return;
            }

            if (password2 == null) {
                registerStatus = PASSWORD2_IS_NULL_STATUS;
                return;
            }

            if (email == null) {
                registerStatus = EMAIL_IS_NULL_STATUS;
                return;
            }

            if ( StringTools.isEmpty(role) ) {
                registerStatus = ROLE_IS_NULL_STATUS;
                return;
            }

            if (!password1.equals(password2)) {
                registerStatus = PASSWORD_NOT_EQUALS_STATUS;
                return;
            }

            if (serverName == null) {
                registerStatus = SERVER_NAME_IS_NULL_STATUS;
                return;
            }

            registerStatus = OK_STATUS;
        }

    final void register() {

        DatabaseAdapter dbDyn = null;
        if (registerStatus == OK_STATUS) {
            try {


                InternetAddress mailAddr = new InternetAddress(email);

                dbDyn = DatabaseAdapter.getInstance();

                if (log.isDebugEnabled()) {
                    log.debug("#1.0005");
                }

                Long userId = InternalAuthProviderTools.addNewUser(dbDyn,
                    firstName,
                    lastName,
                    middleName,
                    portalInfo.getCompanyId(),
                    mailAddr.toString(),
                    "", // address
                    ""  // phone
                );

                if (log.isDebugEnabled()) {
                    log.debug("#1.0006 " + portalInfo.getCompanyId());
                }

                Long authUserId = InternalAuthProviderTools.addUserAuth(dbDyn,
                    userId,
                    portalInfo.getCompanyId(), null, null,
                    username, password1,
                    true, false, false);

                if (log.isDebugEnabled()) {
                    log.debug("#1.0007");
                }


                StringTokenizer st = new StringTokenizer(role, ",");
                while (st.hasMoreTokens()) {
                    String role = st.nextToken();
                    Long roleId = InternalAuthProviderTools.getIDRole(dbDyn, role);
                    if (roleId == null) {
                        throw new PortletException("Role '" + role + "' not exists");
                    }
                    InternalAuthProviderTools.bindUserRole(dbDyn, authUserId, roleId);
                }

                if (log.isDebugEnabled()) {
                    log.debug("#1.0008");
                }

                dbDyn.commit();

                String adminEmail = null;
                // Todo uncomment and implement
/*
                String adminEmail = portalInfo.getSites().getAdminEmail();
*/

                if (log.isDebugEnabled()) {
                    log.debug("Admin mail: " + adminEmail);
                }

                String s = bundle.getString( "reg.mail_body" );
                String mailMessage =
                    MessageFormat.format( s, new Object[]{username, password1, serverName} );

                MailMessage.sendMessage(
                    mailMessage + "\n\nProcess of registration was made from IP ",
                    email,
                    adminEmail,
                    "Confirm registration",
                    GenericConfig.getMailSMTPHost()
                );
            }
            catch (SQLException e) {
                try {
                    dbDyn.rollback();
                } catch (SQLException e1) {

                }

                log.error("Error register new user", e);

                if (dbDyn.testExceptionIndexUniqueKey(e, "USER_LOGIN_AU_UK")) {
                    registerStatus = USERNAME_ALREADY_EXISTS_STATUS;
                    return;
                } else {
                    throwable = e;
                    return;
                }

            } catch (Exception e2) {
                log.error("Error register new user", e2);
                throwable = e2;
                return;
            }
        }
    }

    static void sendPassword(AuthSession authSession, String adminEmail, ResourceBundle bundle)
        throws MessagingException {

        String message = "Your password is";
        String subj = "Requested info";

        MailMessage.sendMessage(
            message,
            authSession.getUserInfo().getEmail(),
            adminEmail,
            subj,
            GenericConfig.getMailSMTPHost()
        );
    }
}
