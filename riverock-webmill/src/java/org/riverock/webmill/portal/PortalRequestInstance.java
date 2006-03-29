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
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.portlet.PortalContext;
import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileUpload;
import org.apache.commons.fileupload.RequestContext;
import org.apache.commons.fileupload.servlet.ServletRequestContext;
import org.apache.log4j.Logger;

import org.riverock.common.html.Header;
import org.riverock.common.tools.MainTools;
import org.riverock.interfaces.portal.CookieManager;
import org.riverock.interfaces.portal.PortalInfo;
import org.riverock.interfaces.portal.dao.PortalDaoProvider;
import org.riverock.interfaces.portal.template.PortalTemplate;
import org.riverock.interfaces.portal.template.PortalTemplateItem;
import org.riverock.interfaces.portal.template.PortalTemplateItemType;
import org.riverock.interfaces.portal.xslt.XsltTransformer;
import org.riverock.interfaces.sso.a3.AuthSession;
import org.riverock.sso.a3.AuthTools;
import org.riverock.webmill.container.ContainerConstants;
import org.riverock.webmill.container.portlet.PortletContainer;
import org.riverock.webmill.exception.PortalException;
import org.riverock.webmill.port.PortalInfoImpl;
import org.riverock.webmill.portal.dao.PortalDaoProviderImpl;
import org.riverock.webmill.portal.impl.ActionRequestImpl;
import org.riverock.webmill.portal.impl.PortalContextImpl;
import org.riverock.webmill.portal.context.RequestState;
import org.riverock.webmill.portal.namespace.Namespace;
import org.riverock.webmill.portal.namespace.NamespaceFactory;
import org.riverock.webmill.utils.PortletUtils;

/**
 * User: Admin
 * Date: Aug 26, 2003
 * Time: 4:40:19 PM
 * 
 * $Id$
 */
public final class PortalRequestInstance {
    private final static Logger log = Logger.getLogger( PortalRequestInstance.class );

    private List<PageElement> pageElementList = new ArrayList<PageElement>();

    private static final int WEBPAGE_BUFFER_SIZE = 15000;
    private static final int MAX_REQUEST_BODY_SIZE = 25*1024*1024; // 25Mb

    ByteArrayOutputStream byteArrayOutputStream = null;
    XsltTransformer xslt = null;
    PortalTemplate template = null;

    long startMills;

    private ContextFactory contextFactory = null;
    private PortalInfo portalInfo = null;
    private Locale[] preferredLocale = null;
    private HttpServletRequest httpRequest = null;
    private HttpServletResponse httpResponse = null;
    private ServletConfig portalServletConfig = null;
    private AuthSession auth = null;
    private ActionRequestImpl actionRequest = null;
    private Map<String, Object> httpRequestParameter = null;
    private CookieManager cookieManager = new CookieManagerImpl();
    private boolean isTextMimeType = true;
    private String mimeType = null;

    private String errorString = null;
    private String redirectUrl = null;

    private PortalContext portalContext = null;

    private File requestBodyFile = null;
    private boolean isMultiPartRequest = false;

    private PortalDaoProvider portalDaoProvider = null;

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
        portalContext = null;
        MainTools.deleteFile( requestBodyFile );
        requestBodyFile = null;
        portalDaoProvider = null;
    }

    public PortalRequestInstance() {
        startMills = System.currentTimeMillis();
        this.byteArrayOutputStream = new ByteArrayOutputStream(WEBPAGE_BUFFER_SIZE);
    }

    public PortalDaoProvider getPortalDaoProvider() {
        return portalDaoProvider;
    }

    PortalRequestInstance(
        HttpServletRequest request_, 
        HttpServletResponse response_, 
        ServletConfig portalServletConfig, 
        PortletContainer portletContainer,
        String portalName
        ) throws PortalException {

        startMills = System.currentTimeMillis();
        this.byteArrayOutputStream = new ByteArrayOutputStream(WEBPAGE_BUFFER_SIZE);

        if (log.isInfoEnabled()) {
            log.info("start init PortalRequestInstance ");
        }

        this.httpRequest = request_;
        this.httpResponse = response_;
        this.portalServletConfig = portalServletConfig;
        try {
            RequestContext uploadRequestContext = new ServletRequestContext( httpRequest );
            isMultiPartRequest = FileUpload.isMultipartContent( uploadRequestContext );
            if (log.isDebugEnabled()) {
                log.debug( "isMultiPartRequest: " + isMultiPartRequest );
            }
            if (isMultiPartRequest) {
                requestBodyFile = PortletUtils.storeBodyRequest( httpRequest, MAX_REQUEST_BODY_SIZE );
                httpRequestParameter = new HashMap<String, Object>();
            }
            else {
                httpRequestParameter = Collections.unmodifiableMap(PortletUtils.getParameters(httpRequest));
            }

            this.auth = AuthTools.getAuthSession(httpRequest);
            portalDaoProvider = new PortalDaoProviderImpl( auth );
            if (log.isDebugEnabled()) {
                log.debug("auth: " + this.auth);
                ClassLoader cl = Thread.currentThread().getContextClassLoader();
                log.debug("portal requet instance class loader:\n" + cl +"\nhash: "+ cl.hashCode() );
                cl = portletContainer.getClass().getClassLoader();
                log.debug("portlet container class loader:\n" + cl +"\nhash: "+ cl.hashCode() );
            }
            
            this.portalInfo = PortalInfoImpl.getInstance( httpRequest.getServerName());
            this.portalContext = createPortalContext(portalName, portalInfo);

            this.contextFactory = ContextFactory.initTypeContext( httpRequest, portalInfo, httpRequestParameter, portletContainer);
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
            Iterator<PortalTemplateItem> iterator = template.getPortalTemplateItems().iterator();
            int i = 0;
            while (iterator.hasNext()) {
                PortalTemplateItem templateItem = iterator.next();

                String portletName = null;
                Namespace namespace = null;
                if (templateItem.getTypeObject().getType()==PortalTemplateItemType.PORTLET_TYPE) {
                    portletName = templateItem.getValue();
                    namespace = NamespaceFactory.getNamespace( portletName, getNameTemplate(), i++ );
                }
                else if (templateItem.getTypeObject().getType()==PortalTemplateItemType.DYNAMIC_TYPE) {
                    portletName = getDefaultPortletDefinition();
                    namespace = NamespaceFactory.getNamespace( portletName, getNameTemplate(), i++ );
                }
                PageElement element = new PageElement(portletContainer, namespace);
                
                element.setPortalTemplateItem(templateItem);
                //Todo. init parameters of all portlets
                element.setParams(getParameters("-1", templateItem.getTypeObject()));

                if (log.isDebugEnabled()) {
                    log.debug("TemplateItem, " +
                        "type: " + (templateItem.getType() != null ? templateItem.getType().toString() : null) + ", " +
                        "value: " + templateItem.getValue() + ", " +
                        ", code: " + templateItem.getCode() + ", xmlRoot: " + templateItem.getXmlRoot());
                    log.debug("getDefaultPortletDefinition(): "+getDefaultPortletDefinition() );
                }

                switch (templateItem.getTypeObject().getType()) {
                    case PortalTemplateItemType.PORTLET_TYPE:
                        // Todo replace new RequestState() with actual state of not-dynamic portlet
                        element.initPortlet(templateItem.getValue(), this, new RequestState() );
                        break;
                    case PortalTemplateItemType.DYNAMIC_TYPE:
                        element.initPortlet(getDefaultPortletDefinition(), this, getRequestState() );
                        break;
                    case PortalTemplateItemType.FILE_TYPE:
                    case PortalTemplateItemType.CUSTOM_TYPE:
                        break;
                    default:
                }
                if (log.isDebugEnabled()) {
                    log.debug("#5.20");
                }

                pageElementList.add(element);
            }

        }
        finally {
            if (log.isInfoEnabled()) {
                log.info("init PortalRequestInstance for " + (System.currentTimeMillis() - startMills) + " milliseconds");
            }
        }
    }

    private PortalContextImpl createPortalContext(String portalName, PortalInfo portalInfo ) {
        Map<String,String> map = new HashMap<String, String>();

        map.put( ContainerConstants.PORTAL_PROP_SITE_ID, portalInfo.getSiteId().toString() );
        map.put( ContainerConstants.PORTAL_PROP_COMPANY_ID, portalInfo.getCompanyId().toString() );
        map.put( ContainerConstants.PORTAL_PORTAL_CONTEXT_PATH, httpRequest.getContextPath() );
	if (log.isDebugEnabled()) {
		log.debug("portal context path: '" + httpRequest.getContextPath() +"'" );
		log.debug("portal context path in map: '" + 
			map.get(ContainerConstants.PORTAL_PORTAL_CONTEXT_PATH) +"'" );
	}
        map.putAll( portalInfo.getMetadata() );

        return new PortalContextImpl( portalName, map );
    }

    public PortalContext getPortalContext() {
        return portalContext;
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

    public ContextFactory.PortletParameters getParameters(String namespace, PortalTemplateItemType type) {
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

    public PortalInfo getPortalInfo() {
        return portalInfo;
    }

    private void initTemplate() throws PortalException {

        template = getPortalInfo().getPortalTemplateManager().getTemplate( getNameTemplate(), getLocaleString() );

        if (template == null) {
            String errorString = "Template '" + getNameTemplate() + "', locale " + getLocaleString() + ", not found";
            log.warn("contextFactory: " + contextFactory);
            log.warn(errorString);
            throw new PortalException(errorString);
        }
        log.debug( "template:\n" + template.toString());

    }

    private void initXslt() throws PortalException {
        // prepare Xsl objects
        if (getPortalInfo().getXsltTransformerManager() == null) {
            String errorString = "XSL template not defined";
            log.error(errorString);
            throw new PortalException(errorString);
        }
        xslt = getPortalInfo().getXsltTransformerManager().getXslt(getLocaleString());
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

    public RequestState getRequestState() {
        if (contextFactory == null)
            return new RequestState();

        return contextFactory.getRequestState();
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

    public File getRequestBodyFile() {
        return requestBodyFile;
    }

    public boolean isMultiPartRequest() {
        return isMultiPartRequest;
    }
}
