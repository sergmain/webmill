/*
 * org.riverock.webmill - Webmill portal with support jsr-168, xml/xslt and others things.
 * For more information, please visit project site http://webmill.riverock.org
 *
 * Copyright (C) 2000-2006, Riverock Software, All Rights Reserved.
 *
 * Riverock - The Open-source Java Development Community
 * http://www.riverock.org
 *
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package org.riverock.webmill.portal.url;

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
    private String templateName = null;

    private String targetTemplateName=null;

    private String defaultPortletName = null;

    private String defaultNamespace = null;

    private Locale locale = null;

    private ExtendedCatalogItemBean extendedCatalogItem = null;

    private Map<String, PortletParameters> parameters = new HashMap<String, PortletParameters>();

    private RequestState defaultRequestState = null;

    private Long contextId;

    public String getTargetTemplateName() {
        return targetTemplateName;
    }

    public void setTargetTemplateName(String targetTemplateName) {
        this.targetTemplateName = targetTemplateName;
    }

    public Long getContextId() {
        return contextId;
    }

    public void setContextId(Long contextId) {
        this.contextId = contextId;
    }

    public RequestState getDefaultRequestState() {
        if (defaultRequestState==null) {
            throw new IllegalStateException("defaultRequestState is null");
        }
        return defaultRequestState;
    }

    public void setDefaultRequestState(RequestState defaultRequestState) {
        if (defaultRequestState==null) {
            throw new IllegalStateException("defaultRequestState is null");
        }
        this.defaultRequestState = defaultRequestState;
    }

    public ExtendedCatalogItemBean getExtendedCatalogItem() {
        return extendedCatalogItem;
    }

    public void setExtendedCatalogItem(ExtendedCatalogItemBean extendedCatalogItem) {
        this.extendedCatalogItem = extendedCatalogItem;
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
        if (defaultPortletName==null) {
            throw new IllegalStateException("defaultPortletName is null");
        }
        this.defaultPortletName = defaultPortletName;
    }

    public Locale getLocale() {
        if (locale==null) {
            throw new IllegalStateException("locale is null");
        }
        return locale;
    }

    public void setLocale(Locale locale) {
        if (locale==null) {
            throw new IllegalStateException("locale is null");
        }
        this.locale = locale;
    }

    /**
     * For some cases default namespace may not be initialized: i.e. page with non-dynamic template
     * @return String default namespace
     */
    public String getDefaultNamespace() {
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
        if (templateName==null) {
            throw new IllegalStateException("templateName is null");
        }
        this.templateName = templateName;
    }

    public String getNamePortletId() {
        if (extendedCatalogItem ==null) {
            throw new IllegalStateException("extendedCatalogItem is null");
        }

        return extendedCatalogItem.getNamePortletId();
    }

    public Long getConcretePortletIdValue() {
        if (extendedCatalogItem ==null) {
            throw new IllegalStateException("extendedCatalogItem is null");
        }

        return extendedCatalogItem.getConcretePortletIdValue();
    }
}
