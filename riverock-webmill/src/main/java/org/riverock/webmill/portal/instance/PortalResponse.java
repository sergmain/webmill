package org.riverock.webmill.portal.instance;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.log4j.Logger;

/**
 * User: SMaslyukov
 * Date: 28.08.2007
 * Time: 16:56:54
 */
public class PortalResponse {
    private final static Logger log = Logger.getLogger(PortalResponse.class);

    private static final int WEBPAGE_BUFFER_SIZE = 15000;

    private ByteArrayOutputStream byteArrayOutputStream = null;
    private String redirectUrl = null;

    public PortalResponse() {
        this.byteArrayOutputStream = new ByteArrayOutputStream(WEBPAGE_BUFFER_SIZE);
    }

    public void destroy() {
        if (byteArrayOutputStream != null) {
            try {
                // TODO remove?
                byteArrayOutputStream.close();
            }
            catch (IOException e) {
                log.warn("Error close outputStream()");
            }
            byteArrayOutputStream = null;
        }
        redirectUrl = null;
    }

    public void setByteArrayOutputStream(ByteArrayOutputStream byteArrayOutputStream) {
        this.byteArrayOutputStream = byteArrayOutputStream;
    }

    public ByteArrayOutputStream getByteArrayOutputStream() {
        return byteArrayOutputStream;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

}
