package org.riverock.portlet.manager.indexer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import org.riverock.interfaces.portal.bean.CatalogItem;
import org.riverock.interfaces.portal.bean.CatalogLanguageItem;
import org.riverock.interfaces.portal.bean.PortletName;
import org.riverock.interfaces.portal.bean.SiteLanguage;
import org.riverock.interfaces.portal.dao.PortalDaoProvider;
import org.riverock.interfaces.portal.search.PortalIndexer;
import org.riverock.interfaces.portal.search.PortletIndexer;
import org.riverock.interfaces.portal.search.PortletIndexerShort;
import org.riverock.portlet.tools.FacesTools;
import org.riverock.webmill.container.ContainerConstants;

/**
 * User: SMaslyukov
 * Date: 01.06.2007
 * Time: 21:01:34
 */
public class IndexerAction implements Serializable {
    private final static Logger log = Logger.getLogger(IndexerAction.class);
    private static final long serialVersionUID = 9977111311L;

    private IndexerSessionBean indexerSessionBean = null;
    private List<String> result = new ArrayList<String>();
    private int countTryWithCounnectionTimeout=0;
    private static final int MAX_COUNT_TRY_WITH_TIMEOUT = 5;
    private static final String INDEXER_MANAGER = "indexer-manager";
    private static final int MAX_TIME_FOR_OPERATION_IN_MINUTES = 3;
    private static final int MAX_TIME_FOR_OPERATION = MAX_TIME_FOR_OPERATION_IN_MINUTES*60*1000;

    private List<PortletIndexerShort> portletIndexerShorts;

    public IndexerAction() {
    }

    // getter/setter methods

    public List<PortletIndexerShort> getPortletIndexerShorts() {
        return portletIndexerShorts;
    }

    public void setPortletIndexerShorts(List<PortletIndexerShort> portletIndexerShorts) {
        this.portletIndexerShorts = portletIndexerShorts;
    }

    public List getResult() {
        return result;
    }

    public void setResult(List<String> result) {
        this.result = result;
    }

    public IndexerSessionBean getIndexerSessionBean() {
        return indexerSessionBean;
    }

    public void setIndexerSessionBean(IndexerSessionBean indexerSessionBean) {
        this.indexerSessionBean = indexerSessionBean;
    }

    public String clearResult() {
        // at this moment result is already empty
        return INDEXER_MANAGER;
    }

    public String statisticAction() {
        result = new ArrayList<String>();
        result.add( "Get statistics." );

        Long siteId = getSiteId();
        PortalIndexer portalIndexer = getPortalIndexer();
        List<PortletIndexerShort> indexerShorts = portalIndexer.getPortletIndexers(siteId);
        for (PortletIndexerShort indexerShort : indexerShorts) {
            PortletIndexer indexer = portalIndexer.getPortletIndexer(siteId, indexerShort.getId());
            result.add("Portlet: " + indexerShort.getPortletName()+", total: " + indexer.getTotal()+", not indexed: "+indexer.getNotIndexedCount());
        }
        return INDEXER_MANAGER;
    }

    public String markAllForIndexingAction() {
        result = new ArrayList<String>();
        result.add( "Start mark all contents for reindexing." );
        try {
            PortalIndexer portalIndexer = getPortalIndexer();
            Long siteId = getSiteId();
            portalIndexer.markAllForIndexing(siteId);
            result.add( "All contents marked for reindexing without error." );
        }
        catch (Exception e) {
            result.add( "Error mark all contents for reindexing. "+e.toString() );
        }
        return INDEXER_MANAGER;
    }

    // 'reindex' action
    public String reindexAction() {
        log.debug("start reindexAction().");
        long startMills = System.currentTimeMillis();

        result = new ArrayList<String>();
        result.add( "Start reindexing." );

        Long siteId = getSiteId();
        try {
            result.add("Result of work for "+MAX_TIME_FOR_OPERATION_IN_MINUTES+" minutes.");

            PortalDaoProvider portalDaoProvider = FacesTools.getPortalDaoProvider();
            List<SiteLanguage> languages = portalDaoProvider.getPortalSiteLanguageDao().getSiteLanguageList(siteId);
            log.debug("siteLanguage: " + languages);
            Map<Long, String> map = new HashMap<Long, String>();
            result.add("Count of languages: " + languages.size());
            for (SiteLanguage language : languages) {
                List<CatalogLanguageItem> catalogLanguageItems = portalDaoProvider.getPortalCatalogDao().getCatalogLanguageItemList(language.getSiteLanguageId());
                result.add("Count of catalogs for language '" + language.getCustomLanguage()+"' - "+ catalogLanguageItems.size());
                for (CatalogLanguageItem catalogLanguageItem : catalogLanguageItems) {
                    List<CatalogItem> catalogItems = portalDaoProvider.getPortalCatalogDao().getCatalogItemList(catalogLanguageItem.getCatalogLanguageId());
                    result.add("Count of menu items for catalog '" + catalogLanguageItem.getCatalogCode()+"' - "+ catalogItems.size());
                    for (CatalogItem catalogItem : catalogItems) {
                        if ((System.currentTimeMillis()-startMills)> MAX_TIME_FOR_OPERATION) {
                            result.add("");
                            result.add("Not all contents reindexing. Time limit for operation reached.");
                            return INDEXER_MANAGER;
                        }
                        String portletName = map.get(catalogItem.getPortletId());
                        log.debug("  portletName: "+portletName);
                        if (portletName==null) {
                            PortletName portlet = portalDaoProvider.getPortalPortletNameDao().getPortletName(catalogItem.getPortletId());
                            if (portlet==null) {
                                String s = "  portlet for portletId: " + catalogItem.getPortletId() + " not found";
                                log.debug(s);
                                result.add(s);
                                continue;
                            }
                            portletName = portlet.getPortletName();
                            map.put(catalogItem.getPortletId(), portletName);
                        }
                        log.debug("  result portletName: "+portletName);
/*
                        if (portletName.equals(WebclipConstants.WEBMILL_WIKI_WEBCLIP)) {
                            String msg = reloadWebclipContent(portalDaoProvider, siteId, catalogItem);
                            if (msg!=null) {
                                result.add(msg);
                            }
                            log.debug("    done reloadWebclipContent()");
                        }
                        else {
                            result.add("Not all content indexed. Time limit for operation reached.");
                        }
*/
                        if (countTryWithCounnectionTimeout> MAX_COUNT_TRY_WITH_TIMEOUT) {
                            result.add("Too many connections time outed.");
                            return INDEXER_MANAGER;
                        }
                    }
                }
            }
            return INDEXER_MANAGER;
        }
        finally {
            result.add("");
/*
            result.add("Total webclips: " + statisticBean.getTotalCount());
            result.add("Webclips for reloading: " + statisticBean.getForReloadCount());
            result.add("Webclips for processing: " + statisticBean.getForProcessCount());
            result.add("");
            statisticBean = PortletDaoFactory.getWebclipDao().getStatistic(siteId);
            result.add("Left count of webclips for reloading: " + statisticBean.getForReloadCount());
            result.add("Left count of webclips for processing: " + statisticBean.getForProcessCount());
*/
        }
    }

    private static Long getSiteId() {
        Long siteId = new Long(FacesTools.getPortletRequest().getPortalContext().getProperty(ContainerConstants.PORTAL_PROP_SITE_ID));
        log.debug("siteId: " + siteId);
        return siteId;
    }

    private PortalIndexer getPortalIndexer() {
        PortalIndexer portalIndexer;
        portalIndexer = (PortalIndexer) FacesTools.getAttribute( ContainerConstants.PORTAL_PORTAL_INDEXER_ATTRIBUTE );
        return portalIndexer;
    }

}

