package org.riverock.portlet.trash.lucene;

import java.io.StringReader;
import java.util.Date;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.StopAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Hits;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.Directory;

/**
 * User: SMaslyukov
 * Date: 01.06.2007
 * Time: 13:48:51
 */
public class LuceneTest {
    public static void main(String[] args) throws Exception {

        // create an index called 'index' in a temporary directory
//        String indexDir = "c:\\TEMP\\lucene\\lucene17617\\20";
        String indexDir = "/lucene/index/36";
        Directory directory = FSDirectory.getDirectory(indexDir);


        IndexSearcher is = new IndexSearcher(directory);
        Analyzer analyzer = new StandardAnalyzer();
        QueryParser parser = new QueryParser("content", analyzer);
        Query query = parser.parse(args[0]);
        Hits hits = is.search(query);


        for (int i=0; i<hits.length(); i++) {
            Document document = hits.doc(i);
            if (document.getFields().isEmpty()) {
                System.out.println("#"+i+" Fields is empty");
                continue;
            }
            System.out.println("#"+i);
            // display the articles that were found to the user
            Field field;
            field = document.getField("url");
            System.out.println("   url: " + field.stringValue());
            field = document.getField("title");
            System.out.println("   title: " + field.stringValue());
        }
        is.close();

/*
        analyzer = new StopAnalyzer();
        IndexWriter writer = new IndexWriter(directory, analyzer, false);
        writer.deleteDocuments(new Term("url", "/page/about/Germany"));
        writer.optimize();
        writer.close();
*/


        IndexReader indexReader = IndexReader.open(directory);
        int deleted = indexReader.deleteDocuments(new Term("url", "/page/about/Germany"));
        System.out.println("deleted = " + deleted);
        boolean b = indexReader.hasDeletions();
        System.out.println("b = " + b);
        indexReader.close();
    }
}
