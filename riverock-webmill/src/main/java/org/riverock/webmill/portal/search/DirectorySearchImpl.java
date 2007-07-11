package org.riverock.webmill.portal.search;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.log4j.Logger;

import org.riverock.webmill.exception.PortalSearchException;

/**
 * User: SMaslyukov
 * Date: 01.06.2007
 * Time: 17:13:40
 */
public class DirectorySearchImpl implements DirectorySearch {
    private final static Logger log = Logger.getLogger(DirectorySearchImpl.class);

    public static final String DIR = "/lucene/index";

    public Directory getDirectory(Long siteId) {
        File path = new File(DIR+File.separatorChar+siteId);
        if (!path.exists()) {
            path.mkdirs();
        }
        else if (!path.isDirectory()) {
            String es = path.getName() + " is not directory.";
            log.error(es);
            throw new PortalSearchException(es);
        }
        if (!path.canWrite()) {
            String es = path.getName() + " is not writable.";
            log.error(es);
            throw new PortalSearchException(es);
        }
        try {
            return FSDirectory.getDirectory(path);
        }
        catch (IOException e) {
            String es = "Error get FSDirectory for path " + path.getName();
            log.error(es, e);
            throw new PortalSearchException(es, e);
        }
    }
}
