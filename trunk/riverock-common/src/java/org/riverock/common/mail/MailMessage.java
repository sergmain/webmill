/*
 * org.riverock.common -- Supporting classes, interfaces, and utilities
 * 
 * Copyright (C) 2004, Riverock Software, All Rights Reserved.
 * 
 * Riverock -- The Open-source Java Development Community
 * http://www.riverock.org
 * 
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 *
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
 *
 *  $Id$
 *
 */

public class MailMessage
{
    private static Logger log = Logger.getLogger("org.riverock.common.mail.MailMessage");

    public static void sendMessage(
            String message,
            String mail_to,
            String mail_from,
            String subj,
            String smtpHost
        )
            throws javax.mail.internet.AddressException,
            javax.mail.MessagingException
    {

        if(log.isDebugEnabled())
        {
            log.debug("message\n"+message);
            log.debug("mail_to "+mail_to);
            log.debug("mail_from "+mail_from);
            log.debug("subj "+subj);
            log.debug("smtpHost "+smtpHost);
        }

        Properties props = System.getProperties();

        if(log.isDebugEnabled())
            log.debug(smtpHost);

        props.put("mail.smtp.host", smtpHost);

        Session MailSess = Session.getDefaultInstance(props, null);

        MimeMessage msg = new MimeMessage(MailSess);
        InternetAddress[] toAddrs = null, ccAddrs = null;

        toAddrs = InternetAddress.parse(mail_to, false);

        if(log.isDebugEnabled())
            log.debug("toAddrs "+toAddrs.toString());

        msg.setRecipients(Message.RecipientType.TO, toAddrs);
        msg.setSubject(subj, "utf-8");

        msg.setFrom( new InternetAddress(mail_from) );
/*
	msg.setFrom(
		new InternetAddress( prop.getProperty("mail.smtp.admin")  )
	);
*/

        msg.setText(message, "utf-8");

        Transport.send(msg);

    }
}