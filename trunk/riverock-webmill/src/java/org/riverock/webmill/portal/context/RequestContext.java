package org.riverock.webmill.portal.context;

import java.util.Locale;
import java.util.Map;
import java.util.HashMap;

import org.riverock.webmill.portal.bean.ExtendedCatalogItemBean;
import org.riverock.webmill.portal.PortletParameters;

/**
 * @author Sergei Maslyukov
 *         Date: 20.04.2006
 *         Time: 13:01:59
 */
public class RequestContext {
    private Long ctxId = null;
    private String templateName = null;
    private String defaultPortletName = null;
    private String defaultNamespace = null;
    private Locale locale = null;
    private ExtendedCatalogItemBean defaultCatalogItem = null;
    private Map<String, PortletParameters> parameters = new HashMap<String, PortletParameters>();
    private RequestState defaultRequestState = null;

    public RequestState getDefaultRequestState() {
        if (defaultRequestState==null) {
            throw new IllegalStateException("defaultRequestState is null");
        }
        return defaultRequestState;
    }

    public void setDefaultRequestState(RequestState defaultRequestState) {
        this.defaultRequestState = defaultRequestState;
    }

    public ExtendedCatalogItemBean getDefaultCatalogItem() {
//        if (defaultCatalogItem==null) {
//            throw new IllegalStateException("defaultCatalogItem is null");
//        }
        return defaultCatalogItem;
    }

    public void setDefaultCatalogItem(ExtendedCatalogItemBean defaultCatalogItem) {
        this.defaultCatalogItem = defaultCatalogItem;
    }

    public Map<String, PortletParameters> getParameters() {
        return parameters;
    }

    public String getDefaultPortletName() {
        if (defaultPortletName==null) {
            throw new IllegalStateException("defaultPortletName is null");
        }
        return defaultPortletName;
    }

    public void setDefaultPortletName(String defaultPortletName) {
        this.defaultPortletName = defaultPortletName;
    }

    public Locale getLocale() {
        if (locale==null) {
            throw new IllegalStateException("locale is null");
        }
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public String getDefaultNamespace() {
        if (defaultNamespace ==null) {
            throw new IllegalStateException("defaultNamespace is null");
        }
        return defaultNamespace;
    }

    public void setDefaultNamespace(String defaultNamespace) {
        this.defaultNamespace = defaultNamespace;
    }

    public String getTemplateName() {
        if (templateName==null) {
            throw new IllegalStateException("templateName is null");
        }
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public Long getCtxId() {
        if (ctxId==null) {
            throw new IllegalStateException("ctxId is null");
        }
        return ctxId;
    }

    public void setCtxId(Long ctxId) {
        this.ctxId = ctxId;
    }


    public String getNamePortletId() {
        if (defaultCatalogItem==null) {
            throw new IllegalStateException("defaultCatalogItem is null");
        }

        return defaultCatalogItem.getNamePortletId();
    }

    public Long getDefaultPortletId() {
        if (defaultCatalogItem==null) {
            throw new IllegalStateException("defaultCatalogItem is null");
        }

        return defaultCatalogItem.getCtx().getContextId();
    }
}
