package org.riverock.webmill.portal.search;

import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.StopAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.ParallelReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;

import org.riverock.interfaces.ContainerConstants;
import org.riverock.interfaces.portal.bean.PortletName;
import org.riverock.interfaces.portal.search.PortalIndexer;
import org.riverock.interfaces.portal.search.PortletIndexer;
import org.riverock.interfaces.portal.search.PortletIndexerShort;
import org.riverock.interfaces.portal.search.PortalIndexerParameter;
import org.riverock.interfaces.portal.search.PortletIndexerContent;
import org.riverock.webmill.container.portlet.PortletContainer;
import org.riverock.webmill.container.portlet.PortletContainerException;
import org.riverock.webmill.container.portlet.PortletEntry;
import org.riverock.webmill.container.portlet.PortletNotRegisteredException;
import org.riverock.webmill.container.tools.PortletService;
import org.riverock.webmill.exception.PortalSearchException;
import org.riverock.webmill.portal.dao.InternalDaoFactory;

/**
 * User: SMaslyukov
 * Date: 01.06.2007
 * Time: 17:25:36
 */
public class PortalIndexerImpl implements PortalIndexer {
    private final static Logger log = Logger.getLogger(PortalIndexerImpl.class);

    private static final int MAX_MERGE_DOCS = 50000;
    private static final int MERGE_FACTOR = 10;

    private Long siteId;
    private PortletContainer container;
    private ClassLoader portalClassLoader;

    static final String URL_FIELD = "url";
    static final String TITLE_FIELD = "title";
    static final String CONTENT_FIELD = "content";
    static final String DESCRIPTION_FIELD = "desc";

    public PortalIndexerImpl(Long siteId, PortletContainer container, ClassLoader portalClassLoader) {
        this.siteId = siteId;
        this.container = container;
        this.portalClassLoader = portalClassLoader;
    }

    public void indexContent(String url, PortalIndexerParameter parameter) {
        ClassLoader oldLoader = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader( portalClassLoader );

            if (log.isDebugEnabled()) {
                log.debug("Start indexContent(). url: " + url+", title: " + parameter.getTitle());
            }

            // create an index called 'index' in a temporary directory
            Directory directory = SearchFactory.getDirectorySearch().getDirectory(siteId);

//            deletePreviousDocument(directory, url);

            indexContent(directory, url, parameter);
        }
        catch (Exception e) {
            String es = "Error index content";
            log.error(es, e);
            throw new PortalSearchException(es, e);
        }
        finally {
            Thread.currentThread().setContextClassLoader( oldLoader );
        }
    }

    static void indexContent(Directory directory, String url, PortalIndexerParameter parameter) throws IOException {
        Analyzer analyzer = new StopAnalyzer();
        IndexWriter writer = new IndexWriter(directory, analyzer, false);

        // set variables that affect speed of indexing
        writer.setMergeFactor(MERGE_FACTOR);
        writer.setMaxMergeDocs(MAX_MERGE_DOCS);

        Document doc = new Document();
//        doc.add(new Field(URL_FIELD, new StringReader(url) ));
        doc.add(new Field(URL_FIELD, url, Field.Store.YES, Field.Index.UN_TOKENIZED ));
        if (parameter.getTitle()!=null) {
            doc.add(new Field(TITLE_FIELD, parameter.getTitle(), Field.Store.YES, Field.Index.TOKENIZED));
        }
        if (parameter.getDescription()!=null) {
            String description;
            if (parameter.getDescription().length()> PortletIndexerContent.MAX_DESCRIPTION_LENGTH) {
                description = parameter.getDescription().substring(0, PortletIndexerContent.MAX_DESCRIPTION_LENGTH);
            }
            else {
                description = parameter.getDescription();
            }
            doc.add(new Field(DESCRIPTION_FIELD, description, Field.Store.YES, Field.Index.UN_TOKENIZED ));
        }
        doc.add(new Field(CONTENT_FIELD, new InputStreamReader(new ByteArrayInputStream(parameter.getContent()))));
        writer.updateDocument(new Term("url", url), doc);
        writer.optimize();
        writer.flush();
        writer.close();
    }

    private static void deletePreviousDocument(Directory directory, String url) throws IOException {
        IndexReader indexReader = IndexReader.open(directory);
        indexReader.deleteDocuments(new Term("url", url));
        indexReader.close();
    }

    public List<PortletIndexerShort> getPortletIndexers(Long siteId) {
        ClassLoader oldLoader = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader( portalClassLoader );

            List<PortletName> names = InternalDaoFactory.getInternalPortletNameDao().getPortletNameList();
            List<PortletIndexerShort> shorts = new ArrayList<PortletIndexerShort>();

            for (PortletName name : names) {
                PortletEntry portletEntry;
                try {
                    portletEntry = container.getPortletInstance(name.getPortletName());
                }
                catch (PortletNotRegisteredException e) {
                    continue;
                }
                catch (PortletContainerException e) {
                    String es = "Error getPortletIndexers()";
                    log.error(es, e);
                    throw new PortalSearchException(es, e);
                }
                if (portletEntry==null || portletEntry.getPortletDefinition()==null) {
                    continue;
                }
                String className = PortletService.getStringParam(
                    portletEntry.getPortletDefinition(), ContainerConstants.WEBMILL_PORTLET_INDEXER_CLASS_NAME
                );

                if (StringUtils.isNotBlank(className)) {
                    shorts.add(
                        new PortletIndexerShortImpl(portletEntry.getClassLoader(), className, name.getPortletId(), name.getPortletName())
                    );
                }
            }
            return shorts;
        }
        finally {
            Thread.currentThread().setContextClassLoader( oldLoader );
        }
    }

    public PortletIndexer getPortletIndexer(Long siteId, Object portletIndexerId) {
        ClassLoader oldPortalClassLoader = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader( portalClassLoader );

            List<PortletIndexerShort> list = getPortletIndexers(siteId);
            for (PortletIndexerShort portletIndexerShort : list) {
                if (portletIndexerShort.getId().equals(portletIndexerId)) {
                    try {
                        String className = portletIndexerShort.getClassName();
                        if (className==null) {
                            return null;
                        }
                        ClassLoader oldLoader = Thread.currentThread().getContextClassLoader();
                        try {
                            Thread.currentThread().setContextClassLoader( portletIndexerShort.getClassLoader() );
                            if (log.isDebugEnabled()) {
                                log.debug("portalClassLoader: " + portalClassLoader);
                                log.debug("oldPortalClassLoader: " + oldPortalClassLoader);
                                log.debug("old portlet classLoader: " + oldLoader);
                                log.debug("portlet classLoader: " + portletIndexerShort.getClassLoader() );
                            }
                            Class clazz = Class.forName(className, true, portletIndexerShort.getClassLoader());
                            PortletIndexer portletIndexer = (PortletIndexer)clazz.newInstance();
                            portletIndexer.init(portletIndexerId, siteId, portletIndexerShort.getClassLoader() );
                            return portletIndexer;
                        }
                        finally {
                            Thread.currentThread().setContextClassLoader( oldLoader );
                        }
                    }
                    catch (Throwable e) {
                        String es = "Error getPortletIndexer()";
                        log.error(es, e);
                        throw new PortalSearchException(es, e);
                    }
                }
            }
            return null;
        }
        finally {
            Thread.currentThread().setContextClassLoader( oldPortalClassLoader );
        }
    }

    public void markAllForIndexing(Long siteId) {
        ClassLoader oldPortalClassLoader = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader( portalClassLoader );

            List<PortletIndexerShort> list = getPortletIndexers(siteId);
            for (PortletIndexerShort portletIndexerShort : list) {
                try {
                    String className = portletIndexerShort.getClassName();
                    if (className==null) {
                        continue;
                    }
                    ClassLoader oldLoader = Thread.currentThread().getContextClassLoader();
                    try {
                        Thread.currentThread().setContextClassLoader( portletIndexerShort.getClassLoader() );
                        Class clazz = Class.forName(className, true, portletIndexerShort.getClassLoader());
                        PortletIndexer portletIndexer = (PortletIndexer)clazz.newInstance();
                        portletIndexer.init(portletIndexerShort.getId(), siteId, portletIndexerShort.getClassLoader() );
                        portletIndexer.markAllForIndexing();
                    }
                    finally {
                        Thread.currentThread().setContextClassLoader( oldLoader );
                    }
                }
                catch (Throwable e) {
                    String es = "Error markAllForIndexing()";
                    log.error(es, e);
                    throw new PortalSearchException(es, e);
                }
            }
        }
        finally {
            Thread.currentThread().setContextClassLoader( oldPortalClassLoader );
        }
    }
}
