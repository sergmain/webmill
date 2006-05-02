package org.riverock.webmill.portal.bean;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;
import org.apache.commons.lang.StringUtils;

import org.riverock.common.tools.StringTools;
import org.riverock.interfaces.portal.template.PortalTemplate;
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

    private PortletDefinition portlet = null;
    private String namePortletId = null;
    private Locale locale = null;
    private Long concretePortletIdValue = null;
    private Long templateId = null;
    private Map<String, String> portletMetadata = null;
    private List<String> roleList = null;

    private ExtendedCatalogItemBean() {
    }

    public List<String> getRoleList() {
        return roleList;
    }

    public Map<String, String> getPortletMetadata() {
        return portletMetadata;
    }

    public Long getTemplateId() {
        return templateId;
    }

    public Long getConcretePortletIdValue() {
        return concretePortletIdValue;
    }

    public Locale getLocale() {
        return locale;
    }

    public PortletDefinition getPortletDefinition() {
        return portlet;
    }

    public String getNamePortletId() {
        return namePortletId;
    }

    public static ExtendedCatalogItemBean getInstance(RequestContextParameter factoryParameter, final Long ctxId) {
        if (ctxId == null) {
            log.debug("ctxId is null. return null value.");
            return null;
        }

        ExtendedCatalogItemBean catalogItem = new ExtendedCatalogItemBean();
        CatalogBean ctx = InternalDaoFactory.getInternalDao().getCatalogBean(ctxId);

        if (ctx == null) {
            log.error("Catalog record for id " + ctxId + " not found. process as 'index' page");
            return null;
        }
        catalogItem.concretePortletIdValue = ctx.getContextId();
        catalogItem.templateId = ctx.getTemplateId();
        catalogItem.portletMetadata = initMetadata(ctx);
        List<String> roles = new ArrayList<String>();
        if (!StringUtils.isEmpty(ctx.getPortletRole())) {
            StringTokenizer st = new StringTokenizer(ctx.getPortletRole());
            while (st.hasMoreElements()) {
                roles.add( st.nextToken() );
            }
            catalogItem.roleList = Collections.unmodifiableList(roles);
        }

        CatalogLanguageBean langMenu = InternalDaoFactory.getInternalDao().getCatalogLanguageBean(
            ctx.getCatalogLanguageId()
        );
        if (langMenu == null) {
            log.error("Lang Catalog with id " + ctx.getCatalogLanguageId() + " not found. process as 'index' page");
            return null;
        }

        SiteLanguageBean siteLanguage = InternalDaoFactory.getInternalDao().getSiteLanguageBean(langMenu.getSiteLanguageId());
        if (siteLanguage == null) {
            log.error("Site language with id " + langMenu.getSiteLanguageId() + " not found. process as 'index' page");
            return null;
        }
        catalogItem.locale = StringTools.getLocale( siteLanguage.getCustomLanguage() );

        if (log.isDebugEnabled()) {
            log.debug("portalInfo: " + factoryParameter.getPortalInfo());
            if (factoryParameter.getPortalInfo() != null) {
                log.debug("portalInfo.getSiteId(): " + factoryParameter.getPortalInfo().getSiteId());
            }
            log.debug("siteLanguage: " + siteLanguage);
            log.debug("siteLanguage.getIdSite(): " + siteLanguage.getSiteId());
        }

        if (!factoryParameter.getPortalInfo().getSiteId().equals(siteLanguage.getSiteId())) {
            log.error("Requested context with id " + ctx.getCatalogId() + " is from others site. Process as 'index' page");
            return null;
        }

        if (ctx.getPortletId() == null) {
            log.error("defaultCatalogItemItem.ctx.getPortletId() is null, unknown portlet");
            log.error("idSiteCtxCatalog: " + ctx.getCatalogId());
            log.error("ctxId: " + ctxId);
            return null;
        }

        PortletNameBean portletNameBean = InternalDaoFactory.getInternalDao().getPortletNameBean(ctx.getPortletId());
        if (portletNameBean.getName() == null) {
            log.error("portletName for id " + ctx.getPortletId() + " not found");
            return null;
        }

        initPortletDefinition(factoryParameter, catalogItem, portletNameBean.getName());
        return catalogItem;
    }

    public static ExtendedCatalogItemBean getInstance(RequestContextParameter factoryParameter, String templateName, String portletName, Locale locale) {
        PortalTemplate template = factoryParameter.getPortalInfo().getPortalTemplateManager().getTemplate( templateName, locale.toString());
        if (template==null) {
            return null;
        }
        ExtendedCatalogItemBean extendedCatalogItem = new ExtendedCatalogItemBean();
        extendedCatalogItem.portletMetadata = new HashMap<String, String>();
        extendedCatalogItem.roleList = new ArrayList<String>();
        extendedCatalogItem.templateId = template.getTemplateId();
        extendedCatalogItem.locale = locale;

        initPortletDefinition( factoryParameter, extendedCatalogItem, portletName );
        return extendedCatalogItem;
    }

    private static void initPortletDefinition(RequestContextParameter contextParameter, ExtendedCatalogItemBean extendedCatalogItem, String portletName) {
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

        extendedCatalogItem.portlet = entry.getPortletDefinition();
        if (extendedCatalogItem.portlet != null) {
            extendedCatalogItem.namePortletId =
                PortletService.getStringParam(extendedCatalogItem.portlet, ContainerConstants.name_portlet_id);
        }
    }

    private static Map<String, String> initMetadata( CatalogBean defaultCtx ) {
        HashMap<String, String> map = new HashMap<String, String>();
        if (defaultCtx==null || defaultCtx.getMetadata()==null) {
            return map;
        }

        InputStream stream = new ByteArrayInputStream( defaultCtx.getMetadata().getBytes() );
        try {
            Properties p = new Properties();
            p.load( stream );
            for (Map.Entry entry : p.entrySet()) {
                map.put((String)entry.getKey(), (String)entry.getValue());
            }
            return Collections.unmodifiableMap( map );
        }
        catch( IOException e ) {
            String es = "Error load properties";
            log.error( es, e );
            throw new IllegalStateException( es, e );
        }
    }

}
