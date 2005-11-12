/*
 * org.riverock.webmill -- Portal framework implementation
 *
 * Copyright (C) 2004, Riverock Software, All Rights Reserved.
 *
 * Riverock -- The Open-source Java Development Community
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
 *
 */
package org.riverock.webmill.portal;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletConfig;

import org.riverock.common.html.Header;
import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.interfaces.sso.a3.AuthSession;
import org.riverock.sso.a3.AuthTools;
import org.riverock.webmill.exception.PortalException;
import org.riverock.webmill.port.PortalInfoImpl;
import org.riverock.webmill.port.PortalXslt;
import org.riverock.webmill.portal.impl.ActionRequestImpl;
import org.riverock.webmill.portlet.PortletTools;
import org.riverock.webmill.container.schema.site.SiteTemplate;
import org.riverock.webmill.container.schema.site.TemplateItemType;
import org.riverock.webmill.container.schema.site.types.TemplateItemTypeTypeType;
import org.riverock.webmill.container.portlet.PortletContainer;

import org.apache.log4j.Logger;

/**
 * User: Admin
 * Date: Aug 26, 2003
 * Time: 4:40:19 PM
 * 
 * $Id$
 */
public final class PortalRequestInstance {
    private final static Logger log = Logger.getLogger(PortalRequestInstance.class);

    private List<PageElement> pageElementList = new ArrayList<PageElement>();

    private static final int WEBPAGE_BUFFER_SIZE = 15000;

    ByteArrayOutputStream byteArrayOutputStream = null;
    PortalXslt xslt = null;
    SiteTemplate template = null;

    long startMills;

    private ContextFactory contextFactory = null;
    private PortalInfoImpl portalInfo = null;
    private Locale[] preferredLocale = null;
    private HttpServletRequest httpRequest = null;
    private HttpServletResponse httpResponse = null;
    private ServletConfig portalServletConfig = null;
    private AuthSession auth = null;
    private ActionRequestImpl actionRequest = null;
    private Map httpRequestParameter = null;
    private CookieManager cookieManager = new CookieManager();
    private boolean isTextMimeType = true;
    private String mimeType = null;

    private String errorString = null;
    private String redirectUrl = null;

    public void destroy() {
        Iterator iterator = getPageElementList().iterator();
        while (iterator.hasNext()) {
            PageElement pageElement = (PageElement) iterator.next();
            pageElement.destroy();
        }

        if (byteArrayOutputStream != null) {
            try {
                byteArrayOutputStream.close();
            }
            catch (IOException e) {
            }
            byteArrayOutputStream = null;
        }
        xslt = null;
        template = null;
        contextFactory = null;
        portalInfo = null;
        preferredLocale = null;
        httpRequest = null;
        httpResponse = null;
        portalServletConfig = null;
        auth = null;
        if (actionRequest != null) {
            actionRequest.destroy();
            actionRequest = null;
        }
        httpRequestParameter = null;
        cookieManager = null;
        errorString = null;
        redirectUrl = null;
        mimeType = null;
    }

    public PortalRequestInstance() {
        startMills = System.currentTimeMillis();
        this.byteArrayOutputStream = new ByteArrayOutputStream(WEBPAGE_BUFFER_SIZE);
    }

    PortalRequestInstance(HttpServletRequest request_, HttpServletResponse response_, ServletConfig portalServletConfig, PortletContainer portletContainer)
        throws Throwable {

        startMills = System.currentTimeMillis();
        this.byteArrayOutputStream = new ByteArrayOutputStream(WEBPAGE_BUFFER_SIZE);

        if (log.isInfoEnabled()) {
            log.info("start init PortalRequestInstance ");
        }

        this.httpRequest = request_;
        this.httpResponse = response_;
        this.portalServletConfig = portalServletConfig;
        DatabaseAdapter db = null;
        try {
            db = DatabaseAdapter.getInstance();
            httpRequestParameter = Collections.unmodifiableMap(PortletTools.getParameters(httpRequest));

            this.auth = AuthTools.getAuthSession(httpRequest);
            if (log.isDebugEnabled()) {
                log.debug("auth: " + this.auth);
            }
            this.portalInfo = PortalInfoImpl.getInstance(db, httpRequest.getServerName());

            this.contextFactory = ContextFactory.initTypeContext(db, httpRequest, portalInfo, httpRequestParameter, portletContainer);
            if (contextFactory.getUrlResource() != null) {
                if (log.isDebugEnabled()) {
                    log.debug("portalServletConfig: " + portalServletConfig );
                    if (portalServletConfig!=null) {
                        log.debug("ServletContext: " + portalServletConfig.getServletContext());
                    }
                }
                mimeType = portalServletConfig.getServletContext().getMimeType(contextFactory.getUrlResource());
                if (log.isDebugEnabled()) {
                    log.debug("mimeType: " + mimeType);
                }
                if (mimeType != null && !mimeType.startsWith("text/")) {
                    isTextMimeType = false;
                    return;
                }
            }
            this.preferredLocale = Header.getAcceptLanguageAsLocaleListSorted(httpRequest);

            if (log.isDebugEnabled()) {
                log.debug("#77.0");
            }
            initTemplate();

            if (log.isDebugEnabled()) {
                log.debug("#77.0");
            }

            initXslt();

            if (log.isDebugEnabled()) {
                log.debug("#5.0");
            }

            // init page element list
            for (int i = 0; i < template.getSiteTemplateItemCount(); i++) {

                if (log.isDebugEnabled()) {
                    log.debug("#5.1-" + i);
                }

                TemplateItemType templateItem = template.getSiteTemplateItem(i);

                PageElement element = new PageElement(portletContainer);
                // Todo: check structure namespace
                // The getNamespace method must return a valid identifier as defined in the 3.8 Identifier
                // Section of the Java Language Specification Second Edition.
                element.setNamespace(templateItem.getNamespace() != null ? templateItem.getNamespace() : "p" + i);
                element.setTemplateItemType(templateItem);
                element.setParams(getParameters(element.getNamespace(), templateItem.getTypeObject()));

                if (log.isDebugEnabled()) {
                    log.debug("TemplateItem, idx: " + i + ", " +
                        "type: " + (templateItem.getType() != null ? templateItem.getType().toString() : null) + ", " +
                        "value: " + templateItem.getValue() + ", " +
                        "namespace: " + element.getNamespace() +
                        ", code: " + templateItem.getCode() + ", xmlRoot: " + templateItem.getXmlRoot());
                }

                if (log.isDebugEnabled()) {
                    log.debug("#5.10");
                }

                switch (templateItem.getTypeObject().getType()) {
                    case TemplateItemTypeTypeType.PORTLET_TYPE:
                        element.initPortlet(templateItem.getValue(), this);
                        break;
                    case TemplateItemTypeTypeType.DYNAMIC_TYPE:
                        element.initPortlet(getDefaultPortletDefinition(), this);
                        break;
                    case TemplateItemTypeTypeType.FILE_TYPE:
                    case TemplateItemTypeTypeType.CUSTOM_TYPE:
                        break;
                    default:
                }
                if (log.isDebugEnabled()) {
                    log.debug("#5.20");
                }

                pageElementList.add(element);
            }

        }
        catch (Throwable e) {
            String es = "Error create portal request instance";
            log.error(es, e);
            throw e;
        }
        finally {
            DatabaseAdapter.close(db);
            db = null;

            if (log.isInfoEnabled()) {
                log.info("init PortalRequestInstance for " + (System.currentTimeMillis() - startMills) + " milliseconds");
            }
        }
    }

    public List<PageElement> getPageElementList() {
        return pageElementList;
    }

    public ServletConfig getPortalServletConfig() {
        return portalServletConfig;
    }

    public String getErrorString() {
        return errorString;
    }

    public Map getHttpRequestParameter() {
        return httpRequestParameter;
    }

    ActionRequestImpl getActionRequest() {
        return actionRequest;
    }

    public Locale[] getPreferredLocale() {
        return preferredLocale;
    }

    public AuthSession getAuth() {
        return auth;
    }

    public String getNameTemplate() {
        if (contextFactory == null)
            return null;

        return contextFactory.getNameTemplate();
    }

    public ContextFactory.PortletParameters getParameters(String namespace, TemplateItemTypeTypeType type) {
        if (contextFactory == null)
            return null;

        return contextFactory.getParameters(namespace, type);
    }

    public String getDefaultPortletDefinition() {
        if (contextFactory == null)
            return null;

        return contextFactory.getDefaultPortletName();
    }

    public Long getDefaultPortletId() {
        if (contextFactory == null)
            return null;

        return contextFactory.getPortletId();
    }

    public Locale getLocale() {
        if (contextFactory == null)
            return null;

        return contextFactory.getRealLocale();
    }

    public String getLocaleString() {
        if (contextFactory == null || contextFactory.getRealLocale() == null)
            return null;

        return contextFactory.getRealLocale().toString();
    }

    public HttpServletRequest getHttpRequest() {
        return httpRequest;
    }

    public HttpServletResponse getHttpResponse() {
        return httpResponse;
    }

    public PortalInfoImpl getPortalInfo() {
        return portalInfo;
    }

    private void initTemplate() throws PortalException {

        template = getPortalInfo().getTemplates().getTemplate(getNameTemplate(), getLocaleString());

        if (template == null) {
            String errorString = "Template '" + getNameTemplate() + "', locale " + getLocaleString() + ", not found";
            log.warn(errorString);
            throw new PortalException(errorString);
        }
        log.debug( "template:\n" + template.toString());

    }

    private void initXslt() throws PortalException {
        // prepare Xsl objects
        if (getPortalInfo().getXsltList() == null) {
            String errorString = "XSL template not defined";
            log.error(errorString);
            throw new PortalException(errorString);
        }
        xslt = getPortalInfo().getXsltList().getXslt(getLocaleString());
        if (xslt == null) {
            String errorString = "XSLT for locale " + getLocaleString() + " not defined.";
            log.error(errorString);
            throw new PortalException(errorString);
        }
    }

    public String getUrlResource() {
        if (contextFactory == null)
            return null;

        return contextFactory.getUrlResource();
    }

    public ContextFactory.DefaultCtx getDefaultCtx() {
        if (contextFactory == null)
            return null;

        return contextFactory.getDefaultCtx();
    }

    public CookieManager getCookieManager() {
        return cookieManager;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    public boolean getIsTextMimeType() {
        return isTextMimeType;
    }

    public String getMimeType() {
        return mimeType;
    }
}
