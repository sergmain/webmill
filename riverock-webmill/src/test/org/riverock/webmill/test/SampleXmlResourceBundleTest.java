package org.riverock.webmill.test;

import java.util.Iterator;

import org.apache.log4j.Logger;

import org.riverock.webmill.container.resource.XmlResourceBundle;

/**
 * @author smaslyukov
 *         Date: 10.08.2005
 *         Time: 18:29:04
 *         $Id$
 */
public class SampleXmlResourceBundleTest extends XmlResourceBundle {
    private final static Logger log = Logger.getLogger( SampleXmlResourceBundleTest.class );

    private Object[][] resource = null;
    protected Object[][] getContents() {
        if (resource!=null) {
            return resource;
        }

        PairList list = null;
        try {
            list = digestXmlFile();
        }
        catch (Exception e) {
            String es = "Error digest file " +getFileName();
            log.error(es, e);
            throw new IllegalStateException(es, e);
        }
        resource = new Object[list.getPairs().size()][2];
        Iterator<Pair> iterator = list.getPairs().iterator();
        int i=0;
        while (iterator.hasNext()) {
            Pair pair = iterator.next();
            resource[i][0] = pair.getKey();
            resource[i][1] = pair.getValue();
        }

        return resource;
    }
}
