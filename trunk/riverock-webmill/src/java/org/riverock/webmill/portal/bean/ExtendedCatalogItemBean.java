package org.riverock.webmill.portal.bean;

import org.apache.log4j.Logger;

import org.riverock.webmill.container.ContainerConstants;
import org.riverock.webmill.container.portlet.PortletContainerException;
import org.riverock.webmill.container.portlet.PortletEntry;
import org.riverock.webmill.container.portlet.bean.PortletDefinition;
import org.riverock.webmill.container.tools.PortletService;
import org.riverock.webmill.portal.context.RequestContextParameter;
import org.riverock.webmill.portal.dao.InternalDaoFactory;

/**
 * @author Sergei Maslyukov
 *         Date: 20.04.2006
 *         Time: 13:12:45
 */
public final class ExtendedCatalogItemBean {
    private static Logger log = Logger.getLogger(ExtendedCatalogItemBean.class);

    private CatalogBean ctx = null;
    private CatalogLanguageBean langMenu = null;
    private SiteLanguageBean siteLanguage = null;
    private PortletDefinition portlet = null;
    private String namePortletId = null;

    private ExtendedCatalogItemBean() {
    }

    public SiteLanguageBean getSiteLanguage() {
        return siteLanguage;
    }

    public CatalogBean getCtx() {
        return ctx;
    }

    public PortletDefinition getPortletDefinition() {
        return portlet;
    }

    public String getNamePortletId() {
        return namePortletId;
    }

    public static ExtendedCatalogItemBean getInstance(RequestContextParameter factoryParameter, final Long ctxId) {
        if (ctxId == null) {
            log.error("ctxId is null. Bad request of method getInstance()");
            try {
                throw new Exception();
            }
            catch (Exception e) {
                log.error("stack trace", e);
            }
            return null;
        }

        ExtendedCatalogItemBean defaultCatalogItem = new ExtendedCatalogItemBean();
        defaultCatalogItem.ctx = InternalDaoFactory.getInternalDao().getCatalogBean(ctxId);

        if (defaultCatalogItem.ctx == null) {
            log.error("Catalog record for id " + ctxId + " not found. process as 'index' page");
            return null;
        }

        defaultCatalogItem.langMenu = InternalDaoFactory.getInternalDao().getCatalogLanguageBean(
            defaultCatalogItem.ctx.getCatalogLanguageId());

        if (defaultCatalogItem.langMenu == null) {
            log.error("Lang Catalog with id " + defaultCatalogItem.ctx.getCatalogLanguageId() + " not found. process as 'index' page");
            return null;
        }

        defaultCatalogItem.siteLanguage = InternalDaoFactory.getInternalDao().getSiteLanguageBean(defaultCatalogItem.langMenu.getSiteLanguageId());

        if (defaultCatalogItem.siteLanguage == null) {
            log.error("Site language with id " + defaultCatalogItem.langMenu.getSiteLanguageId() + " not found. process as 'index' page");
            return null;
        }

        if (log.isDebugEnabled()) {
            log.debug("portalInfo: " + factoryParameter.getPortalInfo());
            if (factoryParameter.getPortalInfo() != null) {
                log.debug("portalInfo.getSiteId(): " + factoryParameter.getPortalInfo().getSiteId());
            }
            log.debug("siteLanguage: " + defaultCatalogItem.siteLanguage);
            if (defaultCatalogItem.siteLanguage != null) {
                log.debug("siteLanguage.getIdSite(): " + defaultCatalogItem.siteLanguage.getSiteId());
            }
        }

        if (!factoryParameter.getPortalInfo().getSiteId().equals(defaultCatalogItem.siteLanguage.getSiteId())) {
            log.error("Requested context with id " + defaultCatalogItem.ctx.getCatalogId() + " is from others site. Process as 'index' page");
            return null;
        }

        if (defaultCatalogItem.ctx.getPortletId() == null) {
            log.error("defaultCatalogItemItem.ctx.getPortletId() is null, unknown portlet");
            log.error("idSiteCtxCatalog: " + defaultCatalogItem.ctx.getCatalogId());
            log.error("ctxId: " + ctxId);
            return null;
        }

        PortletNameBean portletNameBean = InternalDaoFactory.getInternalDao().getPortletNameBean(defaultCatalogItem.ctx.getPortletId());
        if (portletNameBean.getName() == null) {
            log.error("portletName for id " + defaultCatalogItem.ctx.getPortletId() + " not found");
            return null;
        }

        initPortletDefinition(factoryParameter, defaultCatalogItem, portletNameBean.getName());
        return defaultCatalogItem;
    }

/*
    public static ExtendedCatalogItemBean getInstance( RequestContextParameter factoryParameter, final String portletName ) {
        ExtendedCatalogItemBean defaultCatalog = new ExtendedCatalogItemBean();
        initPortletDefinition( factoryParameter, defaultCatalog, portletName );
        return defaultCatalog;
    }
*/

    private static void initPortletDefinition(RequestContextParameter contextParameter, ExtendedCatalogItemBean defaultCatalogItem, String portletName) {
        if (contextParameter == null || contextParameter.getPortletContainer() == null) {
            return;
        }
        PortletEntry entry = null;
        try {
            entry = contextParameter.getPortletContainer().getPortletInstance(portletName);
        }
        catch (PortletContainerException e) {
            log.error("Error get portlet '" + portletName + "'", e);
        }
        if (entry == null) {
            return;
        }

        defaultCatalogItem.portlet = entry.getPortletDefinition();
        if (defaultCatalogItem.portlet != null) {
            defaultCatalogItem.namePortletId =
                PortletService.getStringParam(defaultCatalogItem.portlet, ContainerConstants.name_portlet_id);
        }
    }
}
