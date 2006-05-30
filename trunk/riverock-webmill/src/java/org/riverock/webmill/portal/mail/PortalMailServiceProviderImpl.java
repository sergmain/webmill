package org.riverock.webmill.portal.mail;

import org.riverock.interfaces.portal.mail.PortalMailServiceProvider;
import org.riverock.interfaces.portal.mail.PortalMailService;

/**
 * @author Sergei Maslyukov
 *         Date: 30.05.2006
 *         Time: 16:06:32
 */
public class PortalMailServiceProviderImpl implements PortalMailServiceProvider {

    private PortalMailService portalMailService = null;

    public PortalMailServiceProviderImpl(String smtpHost, String sender) {
        portalMailService = new PortalMailServiceImpl(smtpHost, sender);
    }

    public PortalMailService getPortalMailService() {
        return portalMailService;
    }
}
