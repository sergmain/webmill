package org.riverock.webmill.test;

import org.apache.log4j.Logger;

import org.riverock.common.resource.CustomXmlResourceBundle;

/**
 * @author smaslyukov
 *         Date: 10.08.2005
 *         Time: 18:29:04
 *         $Id$
 */
public class SampleXmlResourceBundleTest extends CustomXmlResourceBundle {
    private final static Logger log = Logger.getLogger( SampleXmlResourceBundleTest.class );

    public void logError(String msg, Throwable th) {
        log.error( msg, th );
    }
}
