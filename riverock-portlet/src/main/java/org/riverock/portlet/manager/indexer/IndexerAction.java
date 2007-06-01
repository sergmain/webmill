package org.riverock.portlet.manager.indexer;

import java.io.Serializable;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import org.riverock.interfaces.portal.bean.CatalogItem;
import org.riverock.interfaces.portal.bean.CatalogLanguageItem;
import org.riverock.interfaces.portal.bean.PortletName;
import org.riverock.interfaces.portal.bean.SiteLanguage;
import org.riverock.interfaces.portal.dao.PortalDaoProvider;
import org.riverock.interfaces.portal.search.PortletIndexerShort;
import org.riverock.interfaces.portal.search.PortalIndexer;
import org.riverock.interfaces.portal.search.PortletIndexer;
import org.riverock.portlet.dao.PortletDaoFactory;
import org.riverock.portlet.tools.FacesTools;
import org.riverock.portlet.webclip.WebclipBean;
import org.riverock.portlet.webclip.WebclipConstants;
import org.riverock.portlet.webclip.WebclipUtils;
import org.riverock.portlet.webclip.manager.bean.WebclipStatisticBean;
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
        result.add( "Start mark all webclips for reloading." );
        try {
            PortalIndexer portalIndexer = getPortalIndexer();
            Long siteId = getSiteId();
            portalIndexer.markAllForIndexing(siteId);
            result.add( "All webclips marked for reloading without error." );
        }
        catch (Exception e) {
            result.add( "Error mark all content for reload. "+e.toString() );
        }
        return INDEXER_MANAGER;
    }

    // 'Reload' action
    public String reloadAllContentAction() {
        log.debug("start reloadAllContentAction().");
        long startMills = System.currentTimeMillis();

        result = new ArrayList<String>();
        result.add( "Start reloading webclips." );

        Long siteId = getSiteId();
        WebclipStatisticBean statisticBean = PortletDaoFactory.getWebclipDao().getStatistic(siteId);
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
                            result.add("Not all webclips reloaded. Time limit for operation reached.");
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
                        if (portletName.equals(WebclipConstants.WEBMILL_WIKI_WEBCLIP)) {
                            String msg = reloadWebclipContent(portalDaoProvider, siteId, catalogItem);
                            if (msg!=null) {
                                result.add(msg);
                            }
                            log.debug("    done reloadWebclipContent()");
                        }
                        else {
                            result.add("Not all webclips reloaded. Time limit for operation reached.");
                        }
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
            result.add("Total webclips: " + statisticBean.getTotalCount());
            result.add("Webclips for reloading: " + statisticBean.getForReloadCount());
            result.add("Webclips for processing: " + statisticBean.getForProcessCount());
            result.add("");
            statisticBean = PortletDaoFactory.getWebclipDao().getStatistic(siteId);
            result.add("Left count of webclips for reloading: " + statisticBean.getForReloadCount());
            result.add("Left count of webclips for processing: " + statisticBean.getForProcessCount());
        }
    }

    // 'Process' actions
    public String processAllContentAction() {
        log.debug("start processAllContentAction().");
        long startMills = System.currentTimeMillis();

        result = new ArrayList<String>();
        result.add( "Start processing webclips." );

        Long siteId = getSiteId();
        WebclipStatisticBean statisticBean = PortletDaoFactory.getWebclipDao().getStatistic(siteId);

        try {
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
                            result.add("Not all webclips processed. Time limit for operation reached.");
                            return INDEXER_MANAGER;
                        }
                        String portletName = map.get(catalogItem.getPortletId());
                        log.debug("  portletName: "+portletName);
                        if (portletName==null) {
                            PortletName portlet = portalDaoProvider.getPortalPortletNameDao().getPortletName(catalogItem.getPortletId());
                            if (portlet==null) {
                                log.debug("  portlet for portletId: "+catalogItem.getPortletId() + " not found");
                                continue;
                            }
                            portletName = portlet.getPortletName();
                            map.put(catalogItem.getPortletId(), portletName);
                        }
                        log.debug("  result portletName: "+portletName);
                        if (portletName.equals(WebclipConstants.WEBMILL_WIKI_WEBCLIP)) {
                            String msg = processWebclipContent(portalDaoProvider, siteId, catalogItem);
                            if (msg!=null) {
                                result.add(msg);
                            }
                            log.debug("    done processAllContentActions()");
                        }
                    }
                }
            }

            return INDEXER_MANAGER;
        }
        finally {
            result.add("");
            result.add("Total webclips: " + statisticBean.getTotalCount());
            result.add("Webclips for reloading: " + statisticBean.getForReloadCount());
            result.add("Webclips for processing: " + statisticBean.getForProcessCount());
            result.add("");
            statisticBean = PortletDaoFactory.getWebclipDao().getStatistic(siteId);
            result.add("Result count of webclips for reloading: " + statisticBean.getForReloadCount());
            result.add("Result count of webclips for processing: " + statisticBean.getForProcessCount());
        }
    }

    // Private methods

    private static class WebclipBeanExtended {
        private WebclipBean webclip=null;
        private String url;
        private String href;
        private String prefix;
        private String status=null;
    }

    private WebclipBeanExtended getWebclip(PortalDaoProvider portalDaoProvider, Long siteId, CatalogItem catalogItem, String msg) {
        WebclipBeanExtended w = new WebclipBeanExtended();
        Map<String, List<String>> m = portalDaoProvider.getPortalPreferencesDao().initMetadata(catalogItem.getMetadata());
        if (m.isEmpty()) {
            w.status = msg + "preferences is empty.";
            return w;
        }

        w.url = getPreferenceValue(m, msg, w, "url not defined.", "too many urls - ", WebclipConstants.URL_SOURCE_PREF);
        if (w.status!=null){
            return w;
        }
        w.href = getPreferenceValue(m, msg, w, "href not defined.", "too many hrefs - ", WebclipConstants.NEW_HREF_PREFIX_PREF);
        if (w.status!=null){
            return w;
        }
        w.prefix = getPreferenceValue(m, msg, w, "prefix not defined.", "too many prefixes - ", WebclipConstants.HREF_START_PAGE_PREF);
        if (w.status!=null){
            return w;
        }

        String id = getPreferenceValue(m, msg, w, "webclipId not defined.", "too many webclipId - ", WebclipConstants.WEBCLIP_ID_PREF);
        if (w.status!=null){
            return w;
        }
        Long webclipId = new Long(id);

        WebclipBean webclip = PortletDaoFactory.getWebclipDao().getWebclip(siteId, webclipId, false);
        if (webclip==null) {
            w.status = msg + "webclip for id "+ webclipId+" not found";
            return w;
        }
        w.webclip = webclip;
        return w;
    }

    private String getPreferenceValue(Map<String, List<String>> m, String msg, WebclipBeanExtended w, String notDefined, String tooMany, String preferenceName) {
        List<String> list = m.get(preferenceName);
        if (list==null || list.isEmpty()) {
            w.status = msg + notDefined;
            return null;
        }
        if (list.size()>1) {
            w.status = msg + tooMany +list.size();
            return null;
        }
        String s = list.get(0);
        if (StringUtils.isBlank(s)) {
            w.status = msg + preferenceName + " preference is empty.";
            return null;
        }
        return s;
    }

    private String reloadWebclipContent(PortalDaoProvider portalDaoProvider, Long siteId, CatalogItem catalogItem) {
        return reloadWebclipContent(portalDaoProvider, siteId, catalogItem, false);
    }

    private String reloadWebclipContent(PortalDaoProvider portalDaoProvider, Long siteId, CatalogItem catalogItem, boolean isForce) {
        log.debug("    start reloadWebclipContent()");

        String msg = catalogItem.getKeyMessage()+", url: "+ catalogItem.getUrl()+". Status: ";

        WebclipBeanExtended w = getWebclip(portalDaoProvider, siteId, catalogItem, msg);
        if (!isForce && !w.webclip.isLoadContent()) {
            return null;
        }

        if (w.status!=null) {
            return w.status;
        }

        try {
            WebclipUtils.loadContentFromSource(w.webclip, w.url);
        }
        catch (Throwable e) {
            if (e instanceof ConnectException && e.toString().indexOf("Connection timed out")!=-1) {
                countTryWithCounnectionTimeout++;
            }
            String es = "Error refresh content for webclip, id: "+ w.webclip.getWebclipId()+", url: "+w.url;
            log.error(es, e);
            return msg + "error load of content - " + e.toString();
        }
        return msg + "OK";
    }

    private String processWebclipContent(PortalDaoProvider portalDaoProvider, Long siteId, CatalogItem catalogItem) {
        return processWebclipContent(portalDaoProvider, siteId, catalogItem, false);
    }

    private String processWebclipContent(PortalDaoProvider portalDaoProvider, Long siteId, CatalogItem catalogItem, boolean isForce) {
        log.debug("    start processWebclipContent()");

        String msg = catalogItem.getKeyMessage()+", url: "+ catalogItem.getUrl()+". Status: ";

        WebclipBeanExtended w = getWebclip(portalDaoProvider, siteId, catalogItem, msg);
        if (!isForce && !w.webclip.isProcessContent()) {
            return null;
        }
        if (w.status!=null) {
            return w.status;
        }
        byte[] bytes = w.webclip.getZippedOriginContentAsBytes();
        if (bytes==null || bytes.length==0) {
            return msg + "zipped content of webclip is empty.";
        }
        try {
            CatalogLanguageItem catalogLanguageItem = portalDaoProvider.getPortalCatalogDao().getCatalogLanguageItem(catalogItem.getCatalogLanguageId());
            WebclipUtils.processStoredContent(w.webclip, w.href, w.prefix, portalDaoProvider, catalogLanguageItem.getSiteLanguageId());
        }
        catch (Throwable e) {
            String es = "Error refresh content for webclip, id: "+ w.webclip.getWebclipId()+", url: "+w.url;
            log.error(es, e);
            return msg + "error load of content - " + e.toString();
        }
        return msg + "OK";
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

