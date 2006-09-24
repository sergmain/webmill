/*
 * org.riverock.webmill - Portal framework implementation
 *
 * Copyright (C) 2006, Riverock Software, All Rights Reserved.
 *
 * Riverock - The Open-source Java Development Community
 * http://www.riverock.org
 *
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
    private String templateName = null;

    private String defaultPortletName = null;

    private String defaultNamespace = null;

    private Locale locale = null;

    private ExtendedCatalogItemBean extendedCatalogItem = null;

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
