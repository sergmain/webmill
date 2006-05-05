package org.riverock.webmill.portal;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Locale;

import org.apache.log4j.Logger;
import org.exolab.castor.xml.Unmarshaller;
import org.xml.sax.InputSource;

import org.riverock.common.config.PropertiesProvider;
import org.riverock.common.tools.StringTools;
import org.riverock.interfaces.portal.bean.Company;
import org.riverock.interfaces.portal.bean.Site;
import org.riverock.interfaces.portal.bean.SiteLanguage;
import org.riverock.interfaces.portal.bean.Template;
import org.riverock.interfaces.portal.bean.VirtualHost;
import org.riverock.interfaces.portal.bean.Xslt;
import org.riverock.interfaces.portal.bean.PortletName;
import org.riverock.interfaces.portlet.menu.Menu;
import org.riverock.webmill.portal.bean.CompanyBean;
import org.riverock.webmill.portal.bean.SiteBean;
import org.riverock.webmill.portal.bean.SiteLanguageBean;
import org.riverock.webmill.portal.bean.TemplateBean;
import org.riverock.webmill.portal.bean.VirtualHostBean;
import org.riverock.webmill.portal.bean.PortalXsltBean;
import org.riverock.webmill.portal.dao.InternalDaoFactory;
import org.riverock.webmill.portal.dao.InternalSiteDao;
import org.riverock.webmill.portal.menu.PortalMenu;
import org.riverock.webmill.schema.site_config.CompanyType;
import org.riverock.webmill.schema.site_config.SiteConfigType;
import org.riverock.webmill.schema.site_config.SiteLanguageType;
import org.riverock.webmill.schema.site_config.Sites;
import org.riverock.webmill.schema.site_config.TemplateType;
import org.riverock.webmill.schema.site_config.MenuGroupType;
import org.riverock.webmill.schema.site_config.MenuType;

/**
 * @author Sergei Maslyukov
 *         Date: 02.05.2006
 *         Time: 14:48:25
 */
public class PortalOfflineMarkupProcessor {
    private final static Logger log = Logger.getLogger(PortalOfflineMarkupProcessor.class);

    public static final String SITE_CONFIG_FILE = "webmill/site-config.xml";
    private static final int MAX_BINARY_FILE_SIZE = 0x4000;

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

                Site site = siteDao.getSite( siteConfig.getSiteName() );
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
                    siteBean.setSiteId(siteDao.createSite(siteBean));
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
                for (Object siteLangObj : siteConfig.getSiteLanguageAsReference()){
                    SiteLanguageType siteLanguageConfig = (SiteLanguageType) siteLangObj;
                    SiteLanguage siteLanguage = InternalDaoFactory.getInternalSiteLanguageDao().getSiteLanguage(siteLanguageConfig.getLocale());
                    if (siteLanguage==null) {
                        SiteLanguageBean siteLanguageBean = new SiteLanguageBean();
                        siteLanguageBean.setSiteLanguageId(
                            InternalDaoFactory.getInternalSiteLanguageDao().createSiteLanguage(siteLanguageBean)
                        );
                        siteLanguage = siteLanguageBean;
                    }
                    for (Object templateConfigObj :siteLanguageConfig.getTemplateAsReference()){
                        TemplateType templateItem = (TemplateType) templateConfigObj;
                        Template template = InternalDaoFactory.getInternalTemplateDao().getTemplate(templateItem.getName());
                        if (template==null) {
                            TemplateBean templateBean = new TemplateBean();

                            templateBean.setTemplateData(readBinaryFile(templateItem.getTemplateFile()));
                            templateBean.setSiteLanguageId(siteLanguage.getSiteLanguageId());
                            templateBean.setTemplateName(templateItem.getName());
                            InternalDaoFactory.getInternalTemplateDao().createTemplate(templateBean);
                        }
                    }
                    Xslt xslt = InternalDaoFactory.getInternalXsltDao().getXslt(siteLanguageConfig.getXslt().getName());
                    if (xslt==null) {
                        PortalXsltBean xsltBean = new PortalXsltBean();
                        xsltBean.setSiteLanguageId(siteLanguage.getSiteLanguageId());
                        xsltBean.setName(siteLanguageConfig.getXslt().getName());
                        xsltBean.setXsltData(readBinaryFile(siteLanguageConfig.getXslt().getXsltFile()));
                        xsltBean.setCurrent(true);
                        InternalDaoFactory.getInternalXsltDao().createXslt(xsltBean);
                    }

                    for (MenuGroupType menuGroup : (List<MenuGroupType>)siteLanguageConfig.getMenuGroupAsReference() ) {
                        processMenu( (List<MenuType>)menuGroup.getMenuAsReference() );
                    }
                }

            }
        } catch (Throwable e) {
            String es = "Error unmarshal config file "+file.getAbsolutePath();
            log.error(es, e);
            throw new IllegalStateException( es, e);
        }
    }

    private static void processMenu(List<MenuType> menuList) {
        for (MenuType menuItem : menuList) {
            PortletName portletName = InternalDaoFactory.getInternalPortletNameDao().getPortletName(menuItem.getPortletName());
            if (portletName==null)
                continue;

            Template template = InternalDaoFactory.getInternalTemplateDao().getTemplate(menuItem.getTemplateName());
            if (template==null)
                continue;

            Menu menu = new PortalMenu();

        }
    }

    private static String readBinaryFile(String binaryFileName) throws IOException {
        File templateFile = new File(binaryFileName);
        InputStream inputStream = new FileInputStream(templateFile);
        long size = templateFile.length();
        if (size>MAX_BINARY_FILE_SIZE) {
            throw new IllegalStateException("Template file too big. Max size of template - "+MAX_BINARY_FILE_SIZE);
        }
        byte[] bytes = new byte[(int)size];
        inputStream.read(bytes, 0, (int)size);

        return new String(bytes);
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
