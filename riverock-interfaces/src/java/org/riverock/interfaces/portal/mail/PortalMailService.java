package org.riverock.interfaces.portal.mail;

/**
 * @author Sergei Maslyukov
 *         Date: 30.05.2006
 *         Time: 15:59:58
 */
public interface PortalMailService {
    public void sendMessageInUTF8(String toAddres, String subj, String messageText) throws javax.mail.MessagingException;
    public void sendMessage(String toAddres, String ccAddreses, String bccAddreses, String subj, String messageText, String subjectCharset, String textCharset) throws javax.mail.MessagingException;
}
