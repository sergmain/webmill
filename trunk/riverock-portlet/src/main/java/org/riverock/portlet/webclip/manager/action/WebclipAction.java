package org.riverock.portlet.webclip.manager.action;

import java.io.BufferedReader;
import java.io.Serializable;
import java.io.StringReader;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.httpclient.util.URIUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import org.riverock.interfaces.portal.bean.CatalogItem;
import org.riverock.interfaces.portal.bean.CatalogLanguageItem;
import org.riverock.interfaces.portal.bean.PortletName;
import org.riverock.interfaces.portal.bean.SiteLanguage;
import org.riverock.interfaces.portal.bean.Template;
import org.riverock.interfaces.portal.dao.PortalDaoProvider;
import org.riverock.portlet.dao.PortletDaoFactory;
import org.riverock.portlet.tools.FacesTools;
import org.riverock.portlet.webclip.WebclipBean;
import org.riverock.portlet.webclip.WebclipConstants;
import org.riverock.portlet.webclip.WebclipUtils;
import org.riverock.portlet.webclip.manager.WebclipSessionBean;
import org.riverock.portlet.webclip.manager.bean.MenuItem;
import org.riverock.webmill.container.ContainerConstants;

/**
 * User: SMaslyukov
 * Date: 11.05.2007
 * Time: 16:55:04
 */
public class WebclipAction implements Serializable {
    private final static Logger log = Logger.getLogger(WebclipAction.class);
    private static final long serialVersionUID = 5077111311L;

    private WebclipSessionBean webclipSessionBean = null;
    private List<String> result = null;
    private int countTryWithCounnectionTimeout=0;
    private static final int MAX_COUNT_TRY_WITH_TIMEOUT = 5;
    private static final String WEBCLIP_MANAGER = "webclip-manager";

    public WebclipAction() {
    }

    // getter/setter methods

    public List getResult() {
        return result;
    }

    public void setResult(List<String> result) {
        this.result = result;
    }

    public WebclipSessionBean getWebclipSessionBean() {
        return webclipSessionBean;
    }

    public void setWebclipSessionBean(WebclipSessionBean webclipSessionBean) {
        this.webclipSessionBean = webclipSessionBean;
    }

    public String clearResult() {
        // at this moment result is already empty
        return WEBCLIP_MANAGER;
    }

    // 'Reload; action
    public String reloadAllContentAction() {
        log.debug("start reloadAllContentAction().");

        result = new ArrayList<String>();

        Long siteId = new Long( FacesTools.getPortletRequest().getPortalContext().getProperty( ContainerConstants.PORTAL_PROP_SITE_ID ) );
        log.debug("siteId: " + siteId);
        PortalDaoProvider portalDaoProvider = FacesTools.getPortalDaoProvider();
        List<SiteLanguage> languages = portalDaoProvider.getPortalSiteLanguageDao().getSiteLanguageList(siteId);
        log.debug("siteLanguage: " + languages);
        Map<Long, String> map = new HashMap<Long, String>();
        for (SiteLanguage language : languages) {
            List<CatalogLanguageItem> catalogLanguageItems = portalDaoProvider.getPortalCatalogDao().getCatalogLanguageItemList(language.getSiteLanguageId());
            for (CatalogLanguageItem catalogLanguageItem : catalogLanguageItems) {
                List<CatalogItem> catalogItems = portalDaoProvider.getPortalCatalogDao().getCatalogItemList(catalogLanguageItem.getCatalogLanguageId());
                for (CatalogItem catalogItem : catalogItems) {
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
                        result.add( reloadWebclipContent(portalDaoProvider, siteId, catalogItem) );
                        log.debug("    done reloadWebclipContent()");
                    }
                    if (countTryWithCounnectionTimeout> MAX_COUNT_TRY_WITH_TIMEOUT) {
                        result.add("Too many time outed connections");
                        return WEBCLIP_MANAGER;
                    }
                }
            }
        }
        return WEBCLIP_MANAGER;
    }

    // 'Process' actions
    public String processAllContentAction() {
        log.debug("start processAllContentAction().");

        result = new ArrayList<String>();

        Long siteId = new Long( FacesTools.getPortletRequest().getPortalContext().getProperty( ContainerConstants.PORTAL_PROP_SITE_ID ) );
        log.debug("siteId: " + siteId);

        PortalDaoProvider portalDaoProvider = FacesTools.getPortalDaoProvider();
        List<SiteLanguage> languages = portalDaoProvider.getPortalSiteLanguageDao().getSiteLanguageList(siteId);
        log.debug("siteLanguage: " + languages);
        Map<Long, String> map = new HashMap<Long, String>();
        for (SiteLanguage language : languages) {
            List<CatalogLanguageItem> catalogLanguageItems = portalDaoProvider.getPortalCatalogDao().getCatalogLanguageItemList(language.getSiteLanguageId());
            for (CatalogLanguageItem catalogLanguageItem : catalogLanguageItems) {
                List<CatalogItem> catalogItems = portalDaoProvider.getPortalCatalogDao().getCatalogItemList(catalogLanguageItem.getCatalogLanguageId());
                for (CatalogItem catalogItem : catalogItems) {
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
                        result.add( processWebclipContent(portalDaoProvider, siteId, catalogItem) );
                        log.debug("    done processAllContentActions()");
                    }
                }
            }
        }

        return WEBCLIP_MANAGER;
    }

    public static final String meta =
        "webclip.new_prefix=/page/about\n" +
        "webclip.href_start_page=" + WebclipConstants.WIKI_URI + "\n" +
        "webclip.url=";


    public String bulkCreateMenus() {

        result = new ArrayList<String>();

        if (StringUtils.isBlank(webclipSessionBean.getUrls())) {
            result.add("List of urls is empty");
            return WEBCLIP_MANAGER;
        }

        if (webclipSessionBean.getCatalogLanguageId()==null) {
            result.add("CatalogId is null");
            return WEBCLIP_MANAGER;
        }

        Long siteId = new Long( FacesTools.getPortletRequest().getPortalContext().getProperty( ContainerConstants.PORTAL_PROP_SITE_ID ) );

        try {
            PortalDaoProvider portalDaoProvider = FacesTools.getPortalDaoProvider();
            PortletName portlet = portalDaoProvider.getPortalPortletNameDao().getPortletName(WebclipConstants.WEBMILL_WIKI_WEBCLIP);
            CatalogLanguageItem catalogLanguageItem = portalDaoProvider.getPortalCatalogDao().getCatalogLanguageItem(webclipSessionBean.getCatalogLanguageId());
            Template template = portalDaoProvider.getPortalTemplateDao().getDefaultDynamicTemplate(catalogLanguageItem.getSiteLanguageId());
            if (template==null) {
                result.add("Default template not found.");
                return WEBCLIP_MANAGER;
            }

            BufferedReader reader = new BufferedReader( new StringReader(webclipSessionBean.getUrls()) );
            String tempLine;
            Set<String> lines = new HashSet<String>();
            while ((tempLine=reader.readLine())!=null) {
                lines.add(tempLine);
            }

            for (String line : lines) {
                Long webclipId;
                try {
                    webclipId = PortletDaoFactory.getWebclipDao().createWebclip(siteId);
                }
                catch (Throwable e) {
                    result.add("Error create new webclip, "+ e.toString());
                    continue;
                }
                
                try {
                    String uri = URIUtil.decode(line);
                    String path = URIUtil.getPath(uri);

                    // test for blank
                    if (StringUtils.isBlank(path)) {
                        continue;
                    }

                    if (path.startsWith(WebclipConstants.WIKI_URI)) {
                        path = path.substring(WebclipConstants.WIKI_URI.length()+1);
                    }
                    else {
                        result.add( "URI for URL "+line+" not start with " + WebclipConstants.WIKI_URI);
                        continue;
                    }

                    // test for blank after remove /wiki
                    if (StringUtils.isBlank(path)) {
                        result.add( "URI for URL "+line+" is empty");
                        continue;
                    }

                    if (portalDaoProvider.getPortalCatalogDao().getCatalogItemId(catalogLanguageItem.getSiteLanguageId(), path)!=null) {
                        result.add( "Menu with URI "+path+" already exist");
                        continue;
                    }

                    String menuName = path.replace('_', ' ');

                    MenuItem item = new MenuItem();
                    item.setCatalogLanguageId(webclipSessionBean.getCatalogLanguageId());
                    item.setUrl(path);
                    item.setKeyMessage(menuName);
                    item.setContextId(null);
                    item.setPortletId(portlet.getPortletId());
                    item.setTemplateId(template.getTemplateId());
                    item.setMetadata( meta + line + "\n" + WebclipConstants.WEBCLIP_ID_PREF + '=' + webclipId);
                    Long menuItemId = portalDaoProvider.getPortalCatalogDao().createCatalogItem(item);
                    CatalogItem catalogItem = portalDaoProvider.getPortalCatalogDao().getCatalogItem(menuItemId);
                    result.add( reloadWebclipContent(portalDaoProvider, siteId, catalogItem) );
                    result.add( processWebclipContent(portalDaoProvider, siteId, catalogItem) );
                }
                catch (Throwable e) {
                    log.error("Error process url '" +line+"'", e);
                    result.add("Error process url '" +line+"', "+ e.toString());
                }
            }
        }
        catch (Throwable th) {
            result.add("Error process list of urls, " + th.toString());
        }

        return WEBCLIP_MANAGER;
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

        WebclipBean webclip = PortletDaoFactory.getWebclipDao().getWebclip(siteId, webclipId);
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
        log.debug("    start reloadWebclipContent()");

        String msg = catalogItem.getKeyMessage()+", url: "+ catalogItem.getUrl()+". Status: ";

        WebclipBeanExtended w = getWebclip(portalDaoProvider, siteId, catalogItem, msg);
        if (w.status!=null) {
            return w.status;
        }
        if (w.webclip.getDatePost()!=null && (System.currentTimeMillis() - w.webclip.getDatePost().getTime()) < WebclipConstants.MAX_TIME_NOT_REFRESH_DATA) {
            return msg +"skipped. content not obsolete";
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
        log.debug("    start processWebclipContent()");

        String msg = catalogItem.getKeyMessage()+", url: "+ catalogItem.getUrl()+". Status: ";

        WebclipBeanExtended w = getWebclip(portalDaoProvider, siteId, catalogItem, msg);
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
}
