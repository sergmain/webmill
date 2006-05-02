package org.riverock.webmill.portal;

import java.io.File;
import java.io.FileInputStream;
import java.util.Locale;
import java.util.List;

import org.apache.log4j.Logger;
import org.exolab.castor.xml.Unmarshaller;
import org.xml.sax.InputSource;

import org.riverock.common.config.PropertiesProvider;
import org.riverock.common.tools.StringTools;
import org.riverock.interfaces.portal.bean.Company;
import org.riverock.interfaces.portal.bean.Site;
import org.riverock.interfaces.portal.bean.VirtualHost;
import org.riverock.webmill.portal.bean.CompanyBean;
import org.riverock.webmill.portal.bean.SiteBean;
import org.riverock.webmill.portal.bean.VirtualHostBean;
import org.riverock.webmill.portal.dao.InternalDaoFactory;
import org.riverock.webmill.portal.dao.InternalSiteDao;
import org.riverock.webmill.schema.site_config.CompanyType;
import org.riverock.webmill.schema.site_config.SiteConfigType;
import org.riverock.webmill.schema.site_config.Sites;

/**
 * @author Sergei Maslyukov
 *         Date: 02.05.2006
 *         Time: 14:48:25
 */
public class PortalOfflineMarkupProcessor {
    private final static Logger log = Logger.getLogger(PortalOfflineMarkupProcessor.class);

    public static final String SITE_CONFIG_FILE = "webmill/site-config.xml";

    public static void process() {
        String applPath = PropertiesProvider.getApplicationPath();

        File file = new File(applPath+File.separatorChar + SITE_CONFIG_FILE);
        if (!file.exists()) {
            log.debug("Config file '" +SITE_CONFIG_FILE+"' not exists");
            log.debug("  full path: " +file.getAbsolutePath());
            return;
        }

        try {
            InputSource inSrc = new InputSource( new FileInputStream( file ) );
            Sites sites = (Sites) Unmarshaller.unmarshal( Sites.class, inSrc );

            InternalSiteDao siteDao = InternalDaoFactory.getInternalSiteDao();
            for (Object o : sites.getSiteConfigAsReference()) {
                SiteConfigType siteConfig = (SiteConfigType)o;

                Company company = InternalDaoFactory.getInternalCompanyDao().loadCompany(siteConfig.getCompany().getName());
                if (company==null) {
                    CompanyBean companyBean = createCompanyBean(siteConfig.getCompany());
                    companyBean.setId( InternalDaoFactory.getInternalCompanyDao().processAddCompany( companyBean, null ) );
                    company = companyBean;
                }

                Site site = siteDao.getSiteBean( siteConfig.getSiteName() );
                boolean isNewSite = false;
                if (site==null) {
                    isNewSite = true;
                    SiteBean siteBean = new SiteBean();
                    //Create new site
                    siteBean = new SiteBean();
                    siteBean.setSiteName(siteConfig.getSiteName());
                    siteBean.setCssDynamic(false);
                    siteBean.setCssFile(siteConfig.getCssFile());
                    Locale locale = StringTools.getLocale( siteConfig.getDefaultLocale() );
                    siteBean.setDefCountry(locale.getCountry());
                    siteBean.setDefLanguage(locale.getLanguage());
                    siteBean.setDefVariant(locale.getVariant());
                    siteBean.setCompanyId(company.getId());
                    siteBean.setSiteId( siteDao.createSite( siteBean ) );
                    site = siteBean;
                }

                List<VirtualHost> hosts = InternalDaoFactory.getInternalVirtualHostDao().getVirtualHosts(site.getSiteId());
                for (Object objHost : siteConfig.getHostAsReference()) {
                    String hostName = (String) objHost;
                    boolean isNotExists = true;
                    for (VirtualHost host : hosts) {
                        if (hostName.equalsIgnoreCase(host.getHost())) {
                            isNotExists = false;
                            break;
                        }
                    }
                    if (isNotExists) {
                        VirtualHostBean virtualHost = new VirtualHostBean();
                        virtualHost.setHost( hostName.toLowerCase() );
                        InternalDaoFactory.getInternalVirtualHostDao().createVirtualHost(virtualHost, site.getSiteId());
                    }
                }


            }
        } catch (Throwable e) {
            String es = "Error unmarshal config file "+file.getAbsolutePath();
            log.error(es, e);
            throw new IllegalStateException( es, e);
        }
    }

    private static CompanyBean createCompanyBean(CompanyType companyType) {
        CompanyBean company = new CompanyBean();
        company.setAddress(companyType.getAddress());
        company.setCeo(companyType.getCeo());
        company.setCfo(companyType.getCfo());
        company.setDeleted( false );
        company.setInfo(companyType.getInfo());
        company.setName(companyType.getName());
        company.setShortName(companyType.getShortName());
        company.setWebsite(companyType.getWebsite());
        return company;
    }

}
