/**
 * License
 * 
 */
package org.riverock.portlet.manager.site;

import org.apache.log4j.Logger;

import org.riverock.portlet.manager.site.bean.SiteExtended;

/**
 * @author Sergei Maslyukov
 * 16.05.2006
 * 20:27:53
 *
 *
 */
public class DataProvider {
    private final static Logger log = Logger.getLogger(DataProvider.class);
    private static final long serialVersionUID = 2057005500L;

    private SiteService siteService=null;
    private SiteSessionBean siteSessionBean = null;

    private SiteExtended siteExtended = null;

    public DataProvider() {
    }

    public SiteSessionBean getSiteSessionBean() {
        return siteSessionBean;
    }

    public void setSiteSessionBean(SiteSessionBean siteSessionBean) {
        this.siteSessionBean = siteSessionBean;
    }

    public void setSiteService(SiteService siteService) {
        this.siteService = siteService;
    }

    public SiteExtended getSiteExtended() {
        if (siteSessionBean.getObjectType()!=siteSessionBean.getSiteType()) {
            throw new IllegalStateException("Query site info with not site type, current type: " + siteSessionBean.getObjectType());
        }
        Long siteId = siteSessionBean.getId();
        if (siteExtended==null) {
            siteExtended = siteService.getSiteExtended(siteId);
        }
        if (!siteExtended.getSite().equals(siteId)) {
            log.warn("Mismatch siteId");
            siteExtended = siteService.getSiteExtended(siteId);
        }

        return siteExtended;
    }
}
