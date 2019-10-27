package org.riverock.webmill.portal;

import java.io.ByteArrayOutputStream;

/**
 * User: SergeMaslyukov
 * Date: 28.08.2007
 * Time: 22:03:29
 */
public interface PortalResponse {
    void destroy();

    void setByteArrayOutputStream(ByteArrayOutputStream byteArrayOutputStream);

    ByteArrayOutputStream getByteArrayOutputStream();

    String getRedirectUrl();

    void setRedirectUrl(String redirectUrl);
}
