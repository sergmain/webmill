package org.riverock.portlet.search.lucene;

import java.io.StringReader;
import java.util.Date;

import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.StopAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Fieldable;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Hits;

/**
 * User: SMaslyukov
 * Date: 01.06.2007
 * Time: 13:48:51
 */
public class LuceneGettingStartedTest {
    public static void main(String[] args) throws Exception {
        int docsInIndex = Integer.parseInt(args[0]);

        // create an index called 'index' in a temporary directory
        String indexDir =
            System.getProperty("java.io.tmpdir", "tmp") +
                System.getProperty("file.separator") + "index";

        Analyzer analyzer = new StopAnalyzer();
        IndexWriter writer = new IndexWriter(indexDir, analyzer, true);

        // set variables that affect speed of indexing
        writer.setMergeFactor(Integer.parseInt(args[1]));
        writer.setMaxMergeDocs(Integer.parseInt(args[2]));

        long startTime = System.currentTimeMillis();
        Document doc = new Document();
        doc.add(new Field("url", "тест".getBytes("Cp1251"), Field.Store.YES));
        doc.add(new Field("contents", new StringReader("Bibamus, moriendum est")));
        writer.addDocument(doc);
        writer.close();
        long stopTime = System.currentTimeMillis();
        System.out.println("Total time: " + (stopTime - startTime) + " ms");

        IndexSearcher is = new IndexSearcher(indexDir);
        analyzer = new StandardAnalyzer();
        QueryParser parser = new QueryParser("contents", analyzer);
        Query query = parser.parse("Bibamus");
        Hits hits = is.search(query);

        for (int i=0; i<hits.length(); i++) {
            Document document = hits.doc(i);
            // display the articles that were found to the user
            Field field = document.getField("url");
            System.out.println("Doc: " + field);
        }
        is.close();
    }

    private Document createDocument(String article, String author,
                                        String title, String topic,
                                        String url, Date dateWritten) {

            Document document = new Document();
//            document.add(new Field("date", dateWritten));
//            document.add(new Field("article", article));
            return document;
        }
}
