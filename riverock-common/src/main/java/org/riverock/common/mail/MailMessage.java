/*
 * org.riverock.common - Supporting classes and utilities
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

package org.riverock.common.mail;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;

/**
 * @deprecated
 * $Id$
 */
public final class MailMessage {
    private static Logger log = Logger.getLogger(MailMessage.class);

    public static void sendMessage(String message,
                                   String mail_to,
                                   String mail_from,
                                   String subj,
                                   String smtpHost)
        throws javax.mail.MessagingException {

        if (log.isDebugEnabled()) {
            log.debug("message\n" + message);
            log.debug("mail_to " + mail_to);
            log.debug("mail_from " + mail_from);
            log.debug("subj " + subj);
            log.debug("smtpHost " + smtpHost);
        }

        Properties props = System.getProperties();

        if (log.isDebugEnabled())
            log.debug(smtpHost);

        props.put("mail.smtp.host", smtpHost);

        Session MailSess = Session.getDefaultInstance(props, null);

        MimeMessage msg = new MimeMessage(MailSess);
        InternetAddress[] toAddrs = null;

        toAddrs = InternetAddress.parse(mail_to, false);

        if (log.isDebugEnabled())
            log.debug("toAddrs " + toAddrs.toString());

        msg.setRecipients(Message.RecipientType.TO, toAddrs);
        msg.setSubject(subj, "utf-8");

        msg.setFrom(new InternetAddress(mail_from));

        msg.setText(message, "utf-8");

        Transport.send(msg);

    }

 /*
    public class SendMail {
        public boolean sendMail(String from, String to) {
            boolean res = true;
            try {
                //enter the mail host name here
                String host = "hostname";

                // Get system properties

                Properties props = System.getProperties();
                // Setup mail server

                props.put("mail.smtp.host", host);
                props.put("mail.smtp.auth", "true"); // Setup authentication, get session

                Authenticator auth = new PopupAuthenticator();
                Session session = Session.getInstance(props, auth);
                // Define message
                MimeMessage message = new MimeMessage(session);
                message.setFrom(new InternetAddress(from));
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
                message.setSubject("Notification");
                message.setText("A new request has been placed");

                // Send message
                Transport.send(message);
            }
            catch (Exception e) {
                res = false;
                System.out.println("Exception while sending mail" + e.getMessage());
                e.printStackTrace();
            }
            return res;
        }
    }

    //This class is required only when authentication is needed
    class PopupAuthenticator extends Authenticator {
        public PasswordAuthentication getPasswordAuthentication() {
            String username, password;
            return new PasswordAuthentication(username, password);
        }
    }
*/

}