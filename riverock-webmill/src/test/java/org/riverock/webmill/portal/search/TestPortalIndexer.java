package org.riverock.webmill.portal.search;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.apache.lucene.store.Directory;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Hits;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.queryParser.QueryParser;

import junit.framework.TestCase;

import org.riverock.interfaces.portal.search.PortalIndexerParameter;
import org.riverock.interfaces.portal.search.PortalSearchParameter;
import org.riverock.interfaces.portal.search.PortalSearchResult;
import org.riverock.interfaces.portal.search.PortalSearchResultItem;

/**
 * User: SMaslyukov
 * Date: 12.07.2007
 * Time: 14:34:05
 */
public class TestPortalIndexer extends TestCase {

    private File lucenePath;
    private static final String PAGE_ABOUT_TEST_URL = "/page/about/test";
    private static final String TITLE_1 = "#1 title òåñò ÈÈÈ test";
    private static final String DESCRIPTION_1 = "#1 description desc";
    private static final String CONTENT_1 = "#1 content abc qwe 123 proba";

    private static final String TITLE_2 = "#2 java sql oracle";
    private static final String DESCRIPTION_2 = "#2 java sql oracle";
    private static final String CONTENT_2 = "#2 proba j2ee sql oracle SOA";
    private static final long SITE_ID = 20L;

    protected void setUp() throws java.lang.Exception {
        System.out.println("Start setUp()");

        String tmpDir = System.getProperty("java.io.tmpdir");
        if (tmpDir==null) {
            throw new Exception("Temp dir property not defined in VM");
        }
        File tmpDirFile = new File(tmpDir+File.separatorChar+"lucene");
        tmpDirFile.mkdirs();
        assertTrue(tmpDirFile.exists());
        assertTrue(tmpDirFile.isDirectory());
        File tempFile = File.createTempFile("lucene", "", tmpDirFile );
        tempFile = convertToDirAndCreate(tempFile);

        this.lucenePath = tempFile;
    }

    protected void tearDown() throws java.lang.Exception {
        dropDir(10);
        dropDir(15);
        dropDir(20);
        lucenePath.delete();
    }

    private void dropDir(int siteId) {
        File dir = new File(lucenePath, ""+siteId);
        if (!(dir.exists())) {
            return;
        }
        File[] files = dir.listFiles();
        for (File file : files) {
            file.delete();
        }
        dir.delete();
    }

    public void testCreateDir() throws Exception {
        DirectorySearchImpl search = initDirectorySearch();

        prepareDirecotryWithClose(search, 10L);
        prepareDirecotryWithClose(search, 15L);

        File file20 = new File(lucenePath, "20");
        convertToDirAndCreate(file20);
        prepareDirecotryWithClose(search, SITE_ID);
    }

    private void prepareDirecotryWithClose(DirectorySearchImpl search, long siteId) throws IOException {
        Directory d = prepareDirecotry(search, siteId);
        d.close();
    }

    private Directory prepareDirecotry(DirectorySearchImpl search, long siteId) throws IOException {
        Directory directory = search.getDirectory(siteId);
        assertNotNull(directory);
        File file1 = new File(lucenePath, ""+ siteId);
        System.out.println("luceneDirectory = " + file1);
        assertTrue(file1.exists());
        assertTrue(file1.isDirectory());
        assertTrue(DirectorySearchImpl.checkSegmentFiles(file1));
        return directory;
    }

    private DirectorySearchImpl initDirectorySearch() {
        DirectorySearchImpl search = new DirectorySearchImpl();
        System.out.println("lucenePath.getAbsolutePath() = " + lucenePath.getAbsolutePath());

        search.setLuceneDirectoryPath(lucenePath.getAbsolutePath());
        return search;
    }

    public void testInsertContent() throws Exception {
        DirectorySearchImpl search = initDirectorySearch();

        Directory directory = prepareDirecotry(search, SITE_ID);

        PortalIndexerParameter parameter;
        parameter = new PortalIndexerParameter() {
            public String getTitle() { return TITLE_1; }
            public String getDescription() { return DESCRIPTION_1; }
            public byte[] getContent() { return CONTENT_1.getBytes(); }
            public Map<String, Object> getParameters() { return null; }
        };
        PortalIndexerImpl.indexContent(directory, PAGE_ABOUT_TEST_URL, parameter);
        searchDocumentInTitle(directory, "test", parameter, PAGE_ABOUT_TEST_URL);
        searchDocumentInContent(directory, "qwe", parameter, PAGE_ABOUT_TEST_URL);

        // test of rewriteing content for specific URL
        parameter = new PortalIndexerParameter() {
            public String getTitle() { return TITLE_2; }
            public String getDescription() { return DESCRIPTION_2; }
            public byte[] getContent() { return CONTENT_2.getBytes(); }
            public Map<String, Object> getParameters() { return null; }
        };
        PortalIndexerImpl.indexContent(directory, PAGE_ABOUT_TEST_URL, parameter);
        searchDocumentInTitle(directory, "java", parameter, PAGE_ABOUT_TEST_URL);
        searchDocumentInContent(directory, "SOA", parameter, PAGE_ABOUT_TEST_URL);


        directory.close();
    }

    // TODO find solution to index words with digits, ie: 'i2ee'
/*
    public void testWordWithDigit() throws Exception {
        DirectorySearchImpl search = initDirectorySearch();

        Directory directory = prepareDirecotry(search, SITE_ID);
        PortalIndexerParameter parameter;

        // test of rewriteing content for specific URL
        parameter = new PortalIndexerParameter() {
            public String getTitle() { return TITLE_2; }
            public String getDescription() { return DESCRIPTION_2; }
            public byte[] getContent() { return CONTENT_2.getBytes(); }
            public Map<String, Object> getParameters() { return null; }
        };
        PortalIndexerImpl.indexContent(directory, PAGE_ABOUT_TEST_URL, parameter);
        searchDocumentInContent(directory, "j2ee", parameter, PAGE_ABOUT_TEST_URL);
        directory.close();
    }
*/

    private void searchDocumentInTitle(Directory luceneDirectory, String subTitle, PortalIndexerParameter parameter, String url) throws Exception {
        IndexSearcher is = new IndexSearcher(luceneDirectory);
        Analyzer analyzer = new StandardAnalyzer();
        QueryParser parser = new QueryParser(PortalIndexerImpl.TITLE_FIELD, analyzer);
        Query query = parser.parse(subTitle);
        Hits hits = is.search(query);
        validateResultOfSearch(hits, parameter, url);
        is.close();
    }

    private void validateResultOfSearch(Hits hits, PortalIndexerParameter parameter, String url) throws IOException {
        assertTrue(hits.length()==1);

        Document document = hits.doc(0);
        assertNotNull(document);
        Field field;

        field = document.getField(PortalIndexerImpl.URL_FIELD);
        assertNotNull(field);
        assertEquals(field.stringValue(), url);

        field = document.getField(PortalIndexerImpl.DESCRIPTION_FIELD);
        assertNotNull(field);
        assertEquals(field.stringValue(), parameter.getDescription());

        field = document.getField(PortalIndexerImpl.TITLE_FIELD);
        assertNotNull(field);
        assertEquals(field.stringValue(), parameter.getTitle());
    }

    private void searchDocumentInContent(Directory luceneDirectory, final String substring, PortalIndexerParameter parameter, String url) throws Exception {
        PortalSearchParameter p = new PortalSearchParameter() {
            public String getQuery() {
                return substring;
            }

            public Integer getResultPerPage() {
                return null;
            }

            public Integer getStartPage() {
                return null; 
            }
        };
        PortalSearchResult r = PortalIndexerImpl.search(luceneDirectory, p);
        assertNotNull(r);
        assertEquals(1, r.getResultItems().size() );

        PortalSearchResultItem i = r.getResultItems().get(0);
        assertNotNull(i);
        assertEquals(i.getUrl(), url);
        assertEquals(i.getDescription(), parameter.getDescription());
        assertEquals(i.getTitle(), parameter.getTitle());
    }

    private File convertToDirAndCreate(File tempFile) {
        if (tempFile.exists()) {
            tempFile.delete();
        }
        assertTrue(!tempFile.exists());
        tempFile.mkdirs();
        assertTrue(tempFile.exists());
        assertTrue(tempFile.isDirectory());

        return tempFile;
    }

}
