package org.riverock.webmill.portal.dao;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Arrays;

import org.riverock.interfaces.portal.bean.SiteLanguage;

/**
 * User: SMaslyukov
 * Date: 24.08.2007
 * Time: 10:54:19
 */
public class OfflineInternalSiteLanguageDao implements InternalSiteLanguageDao {
    public static SiteLanguage siteLanguage = new SiteLanguage() {
        public Long getSiteLanguageId() {
            return 13L;
        }

        public Long getSiteId() {
            return 16L;
        }

        public String getCustomLanguage() {
            return "en";
        }

        public String getNameCustomLanguage() {
            return "English";
        }
    };
    public static List<SiteLanguage> siteLanguages = new ArrayList<SiteLanguage>();
    static {
        siteLanguages.add(siteLanguage);
    }
    public static Map<Long, List<SiteLanguage>> siteLangugeMap = new HashMap<Long, List<SiteLanguage>>();
    static {
        siteLangugeMap.put(siteLanguage.getSiteId(), Arrays.asList(siteLanguage));
    }
    public static Map<Long, SiteLanguage> siteLangugeIdMap = new HashMap<Long, SiteLanguage>();
    static {
        siteLangugeIdMap.put(siteLanguage.getSiteLanguageId(), siteLanguage);
    }

    public List<SiteLanguage> getSiteLanguageList(Long siteId) {
        return siteLangugeMap.get(siteId);
    }

    public SiteLanguage getSiteLanguage(Long siteLanguageId) {
        return siteLangugeIdMap.get(siteLanguageId);
    }

    public SiteLanguage getSiteLanguage(Long siteId, String languageLocale) {
        List<SiteLanguage> list = getSiteLanguageList(siteId);
        for (SiteLanguage language : list) {
            if (language.getCustomLanguage().equalsIgnoreCase(languageLocale)) {
                return language;
            }
        }
        return null;  
    }

    public Long createSiteLanguage(SiteLanguage siteLanguage) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void updateSiteLanguage(SiteLanguage siteLanguage) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void deleteSiteLanguage(Long siteLanguageId) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void deleteSiteLanguageForSite(Long siteId) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
