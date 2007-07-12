package org.riverock.webmill.portal.search;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.StopAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import org.riverock.webmill.exception.PortalSearchException;

/**
 * User: SMaslyukov
 * Date: 01.06.2007
 * Time: 17:13:40
 */
public class DirectorySearchImpl implements DirectorySearch {
    private final static Logger log = Logger.getLogger(DirectorySearchImpl.class);

    public static final String DIR = "/lucene/index";

    private String luceneDirectoryPath = DIR;

    public String getLuceneDirectoryPath() {
        return luceneDirectoryPath;
    }

    public void setLuceneDirectoryPath(String luceneDirectoryPath) {
        this.luceneDirectoryPath = luceneDirectoryPath;
    }

    public Directory getDirectory(Long siteId) {
        return initDirectory(siteId);
    }

    private synchronized Directory initDirectory(Long siteId) {
        File path = new File(getLuceneDirectoryPath()+File.separatorChar+siteId);
        boolean isNew = false;
        if (!path.exists()) {
            isNew = true;
            path.mkdirs();
        }
        else if (!path.isDirectory()) {
            String es = path.getAbsolutePath() + " is not directory.";
            log.error(es);
            throw new PortalSearchException(es);
        }
        if (!path.canWrite()) {
            String es = path.getAbsolutePath() + " is not writable.";
            log.error(es);
            throw new PortalSearchException(es);
        }
        boolean isSegmentPresent = checkSegmentFiles(path);
        try {
            Directory directory = FSDirectory.getDirectory(path);
            if (isNew || !isSegmentPresent) {
                Analyzer analyzer = new StopAnalyzer();
                IndexWriter writer = new IndexWriter(directory, analyzer, true);
                writer.close();
            }
            return directory;
        }
        catch (IOException e) {
            String es = "Error get FSDirectory for path " + path.getName();
            log.error(es, e);
            throw new PortalSearchException(es, e);
        }
    }

    static boolean checkSegmentFiles(File path) {
        File[] files = path.listFiles(
            new FileFilter() {
                public boolean accept(File pathname) {
                    return pathname.isFile() && pathname.getName().startsWith("segments");
                }
            }
        );
        return files!=null && files.length>0;
    }
}
