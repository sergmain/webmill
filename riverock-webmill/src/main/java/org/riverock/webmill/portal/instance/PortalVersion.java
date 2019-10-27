package org.riverock.webmill.portal.instance;

import java.util.StringTokenizer;

/**
 * User: SMaslyukov
 * Date: 16.08.2007
 * Time: 11:43:25
 */
public class PortalVersion {
    int major;
    int minor;

    PortalVersion( String version ) {
        StringTokenizer st = new StringTokenizer( version, "." );
        major = new Integer( st.nextToken() );
        minor = new Integer( st.nextToken() );
    }
}
