package org.riverock.webmill.portal.dao;

import java.util.Collection;
import java.util.Map;
import java.util.List;
import java.util.concurrent.ConcurrentMap;

import org.riverock.webmill.a3.audit.RequestStatisticBean;
import org.riverock.webmill.main.CssBean;
import org.riverock.webmill.portal.bean.SiteBean;
import org.riverock.webmill.portal.bean.SiteLanguageBean;
import org.riverock.webmill.portal.bean.CatalogLanguageBean;
import org.riverock.webmill.portal.bean.CatalogBean;
import org.riverock.webmill.portal.bean.TemplateBean;
import org.riverock.webmill.portal.bean.PortletNameBean;
import org.riverock.interfaces.portal.xslt.XsltTransformer;

/**
 * @author SergeMaslyukov
 *         Date: 27.01.2006
 *         Time: 0:59:51
 *         $Id$
 */
public interface PortalDao {
    public Collection<String> getSupportedLocales();
    public ConcurrentMap<String, Long> getUserAgentList();
    public ConcurrentMap<String, Long> getUrlList();
    public Map<String, Long> getSiteIdMap();

    public CssBean getCssBean( Long siteId );
    public SiteBean getSiteBean( Long siteId );
    public List<SiteLanguageBean> getSiteLanguageList( Long siteId );

    public void saveRequestStatistic( ConcurrentMap<String, Long> userAgentList, ConcurrentMap<String, Long> urlList, RequestStatisticBean bean );

    public List<CatalogLanguageBean> getCatalogLanguageList(Long siteLanguageId);

    public List<CatalogBean> getCatalogList(Long catalogLanguageId);

    public TemplateBean getTemplateBean(Long templateId);

    public PortletNameBean getPortletNameBean(Long portletId);

    public Map<String,XsltTransformer> getTransformerMap(Long siteId);
}
