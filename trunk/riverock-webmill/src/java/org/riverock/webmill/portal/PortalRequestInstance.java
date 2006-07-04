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
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import javax.portlet.PortalContext;
import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileUpload;
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
import org.riverock.webmill.container.portlet.PortletContainer;
import org.riverock.webmill.exception.PortalException;
import org.riverock.webmill.port.PortalInfoImpl;
import org.riverock.webmill.portal.bean.ExtendedCatalogItemBean;
import org.riverock.webmill.portal.context.RequestContext;
import org.riverock.webmill.portal.context.RequestContextFactory;
import org.riverock.webmill.portal.context.RequestContextParameter;
import org.riverock.webmill.portal.context.RequestState;
import org.riverock.webmill.portal.dao.PortalDaoProviderImpl;
import org.riverock.webmill.portal.impl.ActionRequestImpl;
import org.riverock.webmill.portal.impl.PortalContextImpl;
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

    private RequestContext requestContext = null;
    private PortalInfo portalInfo = null;
    /** array of preffered locales from http request */
    private Locale[] preferredLocales = null;

    private HttpServletRequest httpRequest = null;
    private HttpServletResponse httpResponse = null;
    private ServletConfig portalServletConfig = null;
    private AuthSession auth = null;
    private ActionRequestImpl actionRequest = null;
    private CookieManager cookieManager = new CookieManagerImpl();

    private String errorString = null;
    private String redirectUrl = null;

    private PortalContext portalContext = null;

    /** File with request data, if request is multipart */
    private File requestBodyFile = null;
    private boolean isMultiPartRequest = false;

    private PortalDaoProvider portalDaoProvider = null;

    private File tempPath = null;

    public void destroy() {
        for (PageElement pageElement : getPageElementList()) {
            pageElement.destroy();
        }

        if (byteArrayOutputStream != null) {
            try {
                byteArrayOutputStream.close();
            }
            catch (IOException e) {
                log.warn("Error close outputStream()");
            }
            byteArrayOutputStream = null;
        }
        xslt = null;
        template = null;
        requestContext = null;
        portalInfo = null;
        preferredLocales = null;
        httpRequest = null;
        httpResponse = null;
        portalServletConfig = null;
        auth = null;
        if (actionRequest != null) {
            actionRequest.destroy();
            actionRequest = null;
        }
        cookieManager = null;
        errorString = null;
        redirectUrl = null;
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
        String portalInfoName
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
            initTempPath();

            org.apache.commons.fileupload.RequestContext uploadRequestContext = new ServletRequestContext( httpRequest );
            this.isMultiPartRequest = FileUpload.isMultipartContent( uploadRequestContext );
            if (log.isDebugEnabled()) {
                log.debug( "isMultiPartRequest: " + isMultiPartRequest );
            }
            if (isMultiPartRequest) {
                requestBodyFile = PortletUtils.storeBodyRequest( httpRequest, MAX_REQUEST_BODY_SIZE );
            }

            this.auth = AuthTools.getAuthSession(httpRequest);
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            this.portalDaoProvider = new PortalDaoProviderImpl( auth, classLoader);
            if (log.isDebugEnabled()) {
                log.debug("auth: " + this.auth);
                log.debug("portal requet instance class loader:\n" + classLoader +"\nhash: "+ classLoader.hashCode() );
                ClassLoader cl = portletContainer.getClass().getClassLoader();
                log.debug("portlet container class loader:\n" + classLoader +"\nhash: "+ cl.hashCode() );
            }
            this.portalInfo = PortalInfoImpl.getInstance( httpRequest.getServerName());
            this.portalContext = new PortalContextImpl(portalInfoName, httpRequest.getContextPath(), portalInfo);
            this.preferredLocales = Header.getAcceptLanguageAsLocaleListSorted(httpRequest);

            RequestContextParameter factoryParameter =
                new RequestContextParameter(httpRequest, portalInfo, portletContainer, isMultiPartRequest, requestBodyFile );

            this.requestContext = RequestContextFactory.createRequestContext( factoryParameter );
            if (requestContext==null) {
                throw new IllegalArgumentException("General error for access portal page");
            }

            initTemplate();
            initXslt();

            // init page element list
            int i = 0;
            for (PortalTemplateItem templateItem : template.getPortalTemplateItems()) {
                String portletName;
                Namespace namespace = null;
                PortletParameters portletParameters = null;
                if (templateItem.getTypeObject().getType() == PortalTemplateItemType.PORTLET_TYPE) {
                    portletName = templateItem.getValueAsPortletName();
                    
                    namespace = NamespaceFactory.getNamespace(portletName, requestContext.getTemplateName(), i++);

                    portletParameters = requestContext.getParameters().get(namespace.getNamespace());
                    if (portletParameters==null) {
                        portletParameters = new PortletParameters(namespace.getNamespace(), new RequestState(), new HashMap<String, List<String>>());
                    }
                } else if (templateItem.getTypeObject().getType() == PortalTemplateItemType.DYNAMIC_TYPE) {
                    portletName = requestContext.getDefaultPortletName();
                    namespace = NamespaceFactory.getNamespace(portletName, requestContext.getTemplateName(), i++);

                    portletParameters = requestContext.getParameters().get(namespace.getNamespace());

                    if (portletParameters==null) {
                        throw new IllegalStateException(
                            "portletParameters object is null, " +
                                "namespace: " + namespace.getNamespace() +", " +
                                "portletName: " + portletName
                        );
                    }
                }

                PageElement element = new PageElement(
                    portletContainer, namespace, templateItem, portletParameters
                );

                if (log.isDebugEnabled()) {
                    log.debug("TemplateItem, " +
                        "type: " + (templateItem.getType() != null ? templateItem.getType() : null) + ", " +
                        "value: " + templateItem.getValue() + ", " +
                        ", code: " + templateItem.getCode() + ", xmlRoot: " + templateItem.getXmlRoot());
                    log.debug("getDefaultPortletDefinition(): " + requestContext.getDefaultPortletName());
                    log.debug("namespace: " + namespace);
                    log.debug("portletParameters: " + portletParameters);
                    log.debug("element.getParameters(): " + element.getParameters());
                }

                switch (templateItem.getTypeObject().getType()) {
                    case PortalTemplateItemType.PORTLET_TYPE:
                        element.initPortlet(templateItem.getValueAsPortletName(), this, new HashMap<String, String>(), new ArrayList<String>());
                        break;
                    case PortalTemplateItemType.DYNAMIC_TYPE:
                        element.initPortlet(requestContext.getDefaultPortletName(), this, requestContext.getExtendedCatalogItem().getPortletMetadata(), requestContext.getExtendedCatalogItem().getRoleList());
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

    ActionRequestImpl getActionRequest() {
        return actionRequest;
    }

    public Locale[] getPreferredLocales() {
        return preferredLocales;
    }

    public AuthSession getAuth() {
        return auth;
    }

    public Locale getLocale() {
        if (requestContext==null)
            return null;
        else
            return requestContext.getLocale();
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

        template = getPortalInfo().getPortalTemplateManager().getTemplate( requestContext.getTemplateName(), getLocale().toString() );

        if (template == null) {
            String errorString = "Template '" + requestContext.getTemplateName() + "', locale " + getLocale().toString() + ", not found";
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
        xslt = getPortalInfo().getXsltTransformerManager().getXslt(getLocale().toString());
        if (xslt == null) {
            String errorString = "XSLT for locale " + getLocale().toString() + " not defined.";
            log.error(errorString);
            throw new PortalException(errorString);
        }
    }

    public ExtendedCatalogItemBean getDefaultCtx() {
        return requestContext.getExtendedCatalogItem();
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

    public File getRequestBodyFile() {
        return requestBodyFile;
    }

    public boolean isMultiPartRequest() {
        return isMultiPartRequest;
    }

    public RequestContext getRequestContext() {
        return requestContext;
    }

    public File getTempPath() {
        return tempPath;
    }

    private void initTempPath() {
        // init temp path for current request
        try {
            tempPath=(File)httpRequest.getAttribute("javax.servlet.context.tempdir");
        } catch (Throwable e) {
            log.error("error get temp path from request attributes, set to java.io.temp", e);
        }
        if (tempPath==null ){
            tempPath = new File(System.getProperty("java.io.tmpdir"));
        }
        if (!tempPath.exists()) {
            log.error("Specified temp directory '" + tempPath + "' not exists. Set to default java input/output temp directory");
            tempPath = new File(System.getProperty("java.io.tmpdir"));
        }
    }

}
