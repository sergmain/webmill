package org.riverock.webmill.portal.dao;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import org.riverock.interfaces.portal.bean.Site;
import org.riverock.interfaces.portal.bean.VirtualHost;
import org.riverock.interfaces.sso.a3.AuthSession;

/**
 * User: SMaslyukov
 * Date: 24.08.2007
 * Time: 10:36:32
 */
public class OfflineInternalSiteDao implements InternalSiteDao {

    public static Site site = new Site() {
        public Long getSiteId() {
            return 16L;
        }

        public Long getCompanyId() {
            return 1L;
        }

        public String getSiteName() {
            return "me.askmore";
        }

        public boolean getCssDynamic() {
            return false;
        }

        public String getCssFile() {
            return "/style.css";
        }

        public boolean getRegisterAllowed() {
            return false;
        }

        public String getDefLanguage() {
            return "en";
        }

        public String getDefCountry() {
            return null;
        }

        public String getDefVariant() {
            return null;
        }

        public String getAdminEmail() {
            return null;
        }

        public String getProperties() {
            return null;
        }

        public String getPortalCharset() {
            return "utf-8";
        }

        public boolean isEnableNavigation() {
            return false;
        }

        public String getServerTimeZone() {
            return "Europe/Moscow";
        }
    };

    public static List<Site> sites = new ArrayList<Site>();
    static {
        sites.add(site);
    }
    public static Map<Long, Site> siteMap = new HashMap<Long, Site>();
    static {
        siteMap.put(site.getSiteId(), site);
    }

    public static Map<String, Site> siteNameMap = new HashMap<String, Site>();
    static {
        siteNameMap.put(site.getSiteName(), site);
    }


    public List<Site> getSites() {
        return sites;
    }

    public List<Site> getSites(AuthSession authSession) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Site getSite(Long siteId) {
        return siteMap.get(siteId);
    }

    public Site getSite(String siteName) {
        return siteNameMap.get(siteName);
    }

    public Long createSite(Site site) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void updateSite(Site site) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void deleteSite(Long siteId) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void updateSite(Site site, List<VirtualHost> hosts) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public Long createSite(Site site, List<VirtualHost> hosts) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
