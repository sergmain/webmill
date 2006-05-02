package org.riverock.webmill.portal.dao;

import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

import org.riverock.interfaces.portal.bean.Css;
import org.riverock.interfaces.portal.bean.TemplateBean;
import org.riverock.webmill.a3.audit.RequestStatisticBean;
import org.riverock.webmill.portal.bean.CatalogBean;
import org.riverock.webmill.portal.bean.CatalogLanguageBean;
import org.riverock.webmill.portal.bean.PortletNameBean;
import org.riverock.webmill.portal.bean.SiteLanguageBean;

/**
 * @author SergeMaslyukov
 *         Date: 27.01.2006
 *         Time: 0:59:51
 *         $Id$
 */
public interface InternalDao {
    public Collection<String> getSupportedLocales();
    public ConcurrentMap<String, Long> getUserAgentList();
    public ConcurrentMap<String, Long> getUrlList();
    public Map<String, Long> getSiteIdMap();

    public Css getCssBean( Long siteId );

    public void saveRequestStatistic( ConcurrentMap<String, Long> userAgentList, ConcurrentMap<String, Long> urlList, RequestStatisticBean bean );

    public List<SiteLanguageBean> getSiteLanguageList( Long siteId );
    public SiteLanguageBean getSiteLanguageBean(Long siteLanguageId);

    public List<CatalogLanguageBean> getCatalogLanguageList(Long siteLanguageId);
    public CatalogLanguageBean getCatalogLanguageBean(Long catalogLanguageId );

    public List<CatalogBean> getCatalogList(Long catalogLanguageId);
    public CatalogBean getCatalogBean(Long catalogId);

    public Long getCatalogId(Long siteId, Locale locale, String portletName, String templateName );
    public Long getCatalogId(Long siteId, Locale locale, String pageName );
    public Long getCatalogId(Long siteId, Locale locale, Long catalogId );


    public TemplateBean getTemplateBean(Long templateId);
    public List<TemplateBean> getTemplateLanguageList( Long siteLanguageId );
    public List<TemplateBean> getTemplateList( Long siteId );

    public PortletNameBean getPortletNameBean(Long portletId);


}
