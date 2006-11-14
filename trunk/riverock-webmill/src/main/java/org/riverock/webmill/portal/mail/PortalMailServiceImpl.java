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
package org.riverock.webmill.portal.mail;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Transport;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import org.riverock.interfaces.portal.mail.PortalMailService;

/**
 * @author Sergei Maslyukov
 *         Date: 30.05.2006
 *         Time: 16:15:27
 */
public class PortalMailServiceImpl implements PortalMailService {
    private static Logger log = Logger.getLogger(PortalMailServiceImpl.class);

    private static final String UTF_8="utf-8";
    private String smtpHost;
    private String sender = null;

    public PortalMailServiceImpl(String smtpHost, String sender) {
        this.smtpHost = smtpHost;
        this.sender = sender;
    }

    public void sendMessageInUTF8(String to, String subj, String messageText) throws MessagingException {
        sendMessage(to, null, null, subj, messageText, UTF_8, UTF_8);
    }

    public void sendMessage(String to, String subj, String messageText, String subjectCharset, String textCharset) throws MessagingException {
        sendMessage(to, null, null, subj, messageText, subjectCharset, textCharset);
    }

    public void sendMessage(String to, String cc, String bcc, String subj, String messageText, String subjectCharset, String textCharset) throws MessagingException {
        if (StringUtils.isBlank(to)) {
            throw new MessagingException("'to' address is empty");
        }

        if (log.isDebugEnabled()) {
            log.debug(smtpHost);
        }

        Properties props = new Properties();
        props.put("mail.smtp.host", smtpHost);

        if (log.isDebugEnabled()) {
            props.put("mail.debug", "true");
        }

        Session MailSess = Session.getDefaultInstance(props, null);

        MimeMessage msg = new MimeMessage(MailSess);
        InternetAddress[] toAddrs;

        toAddrs = InternetAddress.parse(to, false);
        msg.setRecipients(Message.RecipientType.TO, toAddrs);

        if (StringUtils.isNotBlank(cc)) {
            InternetAddress[] ccAddrs;
            ccAddrs = InternetAddress.parse(cc, false);
            msg.setRecipients(Message.RecipientType.CC, ccAddrs);
        }
        if (StringUtils.isNotBlank(bcc)) {
            InternetAddress[] bccAddrs;
            bccAddrs = InternetAddress.parse(bcc, false);
            msg.setRecipients(Message.RecipientType.BCC, bccAddrs);
        }

        if (log.isDebugEnabled()) {
            log.debug("toAddrs " + toAddrs.toString());
        }

        msg.setSubject(subj, subjectCharset);

        msg.setFrom(new InternetAddress(sender));

        msg.setText(messageText, textCharset);

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
