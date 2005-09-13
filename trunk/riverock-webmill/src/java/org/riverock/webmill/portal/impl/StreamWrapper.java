package org.riverock.webmill.portal.impl;

import java.io.OutputStream;
import java.io.IOException;

import org.riverock.webmill.config.WebmillConfig;

/**
 * @author SMaslyukov
 *         Date: 20.04.2005
 *         Time: 20:13:43
 *         $Id$
 */

/**
 * @deprecated
 */
public class StreamWrapper {
    OutputStream out = null;

    public StreamWrapper(OutputStream outputStream) {
        this.out = outputStream;
    }

    public void write(String s) throws IOException {
        out.write( s.getBytes( WebmillConfig.getHtmlCharset() ) );
    }
}
