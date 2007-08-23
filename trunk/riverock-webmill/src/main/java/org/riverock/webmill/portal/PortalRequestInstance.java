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
package org.riverock.webmill.portal;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import org.riverock.common.html.Header;
import org.riverock.common.tools.MainTools;
import org.riverock.interfaces.portal.CookieManager;
import org.riverock.interfaces.portal.PortalInfo;
import org.riverock.interfaces.portal.bean.CatalogItem;
import org.riverock.interfaces.portal.dao.PortalDaoProvider;
import org.riverock.interfaces.portal.xslt.XsltTransformer;
import org.riverock.interfaces.sso.a3.AuthSession;
import org.riverock.sso.a3.AuthTools;
import org.riverock.webmill.exception.PortalException;
import org.riverock.webmill.port.PortalInfoImpl;
import org.riverock.webmill.portal.bean.ExtendedCatalogItemBean;
import org.riverock.webmill.portal.dao.InternalDaoFactory;
import org.riverock.webmill.portal.dao.PortalDaoProviderImpl;
import org.riverock.webmill.portal.namespace.Namespace;
import org.riverock.webmill.portal.namespace.NamespaceFactory;
import org.riverock.webmill.portal.namespace.NamespaceMapper;
import org.riverock.webmill.portal.page_element.PageElement;
import org.riverock.webmill.portal.page_element.PageElementPortlet;
import org.riverock.webmill.portal.page_element.StringPageElement;
import org.riverock.webmill.portal.page_element.XsltPageElement;
import org.riverock.webmill.portal.preference.PreferenceFactory;
import org.riverock.webmill.portal.url.PortletDefinitionProviderImpl;
import org.riverock.webmill.portal.url.UrlInterpreterResult;
import org.riverock.webmill.portal.url.UrlInterpreterParameter;
import org.riverock.webmill.portal.url.RequestState;
import org.riverock.webmill.portal.url.UrlInterpreterIterator;
import org.riverock.webmill.portal.utils.PortalUtils;
import org.riverock.webmill.template.PortalTemplate;
import org.riverock.webmill.template.TemplateUtils;
import org.riverock.webmill.template.parser.ParsedTemplateElement;
import org.riverock.webmill.template.schema.Portlet;
import org.riverock.webmill.utils.PortletUtils;
import org.riverock.webmill.xslt.XsltTransformetManagerFactory;
import org.riverock.webmill.xslt.XsltTransformerManager;

/**
 * User: Admin
 * Date: Aug 26, 2003
 * Time: 4:40:19 PM
 * <p/>
 * $Id$
 */
public final class PortalRequestInstance {
    private final static Logger log = Logger.getLogger(PortalRequestInstance.class);

    private List<PageElement> pageElementList = new ArrayList<PageElement>();

    private static final int WEBPAGE_BUFFER_SIZE = 15000;
    private static final int MAX_REQUEST_BODY_SIZE = 25 * 1024 * 1024; // 25Mb

    ByteArrayOutputStream byteArrayOutputStream = null;
    XsltTransformer xslt = null;
    PortalTemplate template = null;

    long startMills;

    private UrlInterpreterResult urlInterpreterResult = null;

    /**
     * array of preffered locales from http request
     */
    private Locale[] preferredLocales = null;

    private HttpServletRequest httpRequest = null;
    private HttpServletResponse httpResponse = null;
    private PortalInstance portalInstance;
    private AuthSession auth = null;
//    private ActionRequestImpl actionRequest = null;
    private CookieManager cookieManager = new CookieManagerImpl();

    private String errorString = null;
    private String redirectUrl = null;

    /**
     * File with request data, if request is multipart
     */
    private File requestBodyFile = null;
    private boolean isMultiPartRequest = false;
    private PortalDaoProvider portalDaoProvider = null;
    private File tempPath = null;

    private PortalTransformationParameters portalTransformationParameters = new PortalTransformationParameters();

    public void destroy() {
        if (pageElementList!=null) {
            for (PageElement pageElement : pageElementList) {
                pageElement.destroy();
            }
            pageElementList.clear();
            pageElementList=null;
        }

        if (byteArrayOutputStream != null) {
            try {
                // TODO remove?
                byteArrayOutputStream.close();
            }
            catch (IOException e) {
                log.warn("Error close outputStream()");
            }
            byteArrayOutputStream = null;
        }
        xslt = null;
        template = null;
        urlInterpreterResult = null;
        preferredLocales = null;
        if (httpRequest!=null) {
            Enumeration en = httpRequest.getAttributeNames();
            while (en.hasMoreElements()) {
                Object o =  en.nextElement();
                if (o==null) {
                    continue;
                }
                if (o instanceof String) {
                    try {
                        httpRequest.removeAttribute((String)o);
                    }
                    catch (Throwable e) {
                        log.warn("Error remove attribute", e);
                    }
                }
                else {
                    log.warn("Class of oname of attribute is not String, real: " + o.getClass().getName());
                    try {
                        httpRequest.removeAttribute(o.toString());
                    }
                    catch (Throwable e) {
                        log.warn("Error remove attribute", e);
                    }
                }
            }
        }
        httpRequest = null;
        httpResponse = null;
        auth = null;
        cookieManager = null;
        errorString = null;
        redirectUrl = null;
        MainTools.deleteFile(requestBodyFile);
        requestBodyFile = null;
        portalDaoProvider = null;
        portalTransformationParameters = null;
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
        PortalInstance portalInstance
    ) throws PortalException {

        this.startMills = System.currentTimeMillis();
        this.portalInstance = portalInstance;
        this.byteArrayOutputStream = new ByteArrayOutputStream(WEBPAGE_BUFFER_SIZE);

        if (log.isInfoEnabled()) {
            log.info("start init PortalRequestInstance ");
        }

        this.httpRequest = request_;
        this.httpResponse = response_;
        try {
            initTempPath();

            this.isMultiPartRequest = PortletUtils.isMultiPart(httpRequest);
            if (isMultiPartRequest) {
                requestBodyFile = PortletUtils.storeBodyRequest(httpRequest, MAX_REQUEST_BODY_SIZE);
            }
            if (log.isDebugEnabled()) {
                log.debug("isMultiPartRequest: " + isMultiPartRequest);
                log.debug("requestBodyFile: " + requestBodyFile);
                if (isMultiPartRequest && requestBodyFile != null) {
                    log.debug("requestBodyFile: " + requestBodyFile.getAbsolutePath());
                    log.debug("requestBodyFile length: " + requestBodyFile.length());
                    log.debug("content length: " + httpRequest.getContentLength());
                }
            }


            this.auth = AuthTools.getAuthSession(httpRequest);
            ClassLoader classLoader = portalInstance.getPortalClassLoader();
            this.portalDaoProvider = new PortalDaoProviderImpl(auth, classLoader, portalInstance.getSiteId());
            if (log.isDebugEnabled()) {
                log.debug("auth: " + this.auth);
                log.debug("portal requet instance class loader:\n" + classLoader + "\nhash: " + classLoader.hashCode());
                ClassLoader cl = portalInstance.getPortletContainer().getClass().getClassLoader();
                log.debug("portlet container class loader:\n" + classLoader + "\nhash: " + cl.hashCode());
            }
            this.preferredLocales = Header.getAcceptLanguageAsLocaleListSorted(httpRequest);

            PortalInfo portalInfo = PortalInfoImpl.getInstance(portalInstance.getSiteId());
            if (portalInfo==null) {
               throw new IllegalArgumentException(
                  "PortalInfo object not created for host '"+httpRequest.getServerName()+"', siteId: " +portalInstance.getSiteId()
               );
            }
            if (portalInfo.getSiteId() == null) {
                throw new IllegalArgumentException("siteId is null");
            }
            UrlInterpreterParameter interpreterParameter =
                new UrlInterpreterParameter(
                    httpRequest.getPathInfo(),
                    new PortletDefinitionProviderImpl(portalInstance.getPortletContainer()), 
                    isMultiPartRequest, requestBodyFile,
                    portalInfo.getSiteId(),
                    PortalUtils.prepareLocale(httpRequest, portalInfo.getSiteId()),
                    (isMultiPartRequest
                        ?null
                        :PortalUtils.prepareParameters(httpRequest, portalInfo.getSite().getPortalCharset())
                    ),
                    portalInfo
                );

            this.urlInterpreterResult = UrlInterpreterIterator.interpretUrl(interpreterParameter);
            if (urlInterpreterResult == null) {
                throw new IllegalArgumentException("General error for access portal page");
            }

            if (urlInterpreterResult.getExtendedCatalogItem()!=null || urlInterpreterResult.getContextId()!=null) {
                CatalogItem catalogItem = InternalDaoFactory.getInternalCatalogDao().getCatalogItem(
                    urlInterpreterResult.getContextId()!=null
                        ? urlInterpreterResult.getContextId()
                        : urlInterpreterResult.getExtendedCatalogItem().getCatalogId()
                );
                initTransformationParameters(catalogItem);
            }

            if (log.isDebugEnabled()) {
                log.debug("#10.1 contextId: " + urlInterpreterResult.getContextId());
                if (urlInterpreterResult.getExtendedCatalogItem()!=null)
                    log.debug("#10.2 catalogId: " + urlInterpreterResult.getExtendedCatalogItem().getCatalogId());
                else
                    log.debug("#10.2 urlInterpreterResult.getExtendedCatalogItem() is null.");
                log.debug("#10.3, title: " + portalTransformationParameters.getTitle());
                log.debug("#10.4, keyword: " + portalTransformationParameters.getKeyword());
                log.debug("#10.5, author: " + portalTransformationParameters.getAuthor());
                log.debug("#10.6, portal context path: " + portalTransformationParameters.getPortalContextPath());
            }

            initTemplate();
            initXslt(portalInfo);

            // init page element list
            int i = 0;
            for (ParsedTemplateElement o : template.getTemplateElements()) {

                String portletName;
                Namespace namespace = null;
                PortletParameters portletParameters = null;
                String targetTemplateName;

                if (o.isPortlet() && StringUtils.isNotBlank(o.getPortlet().getTemplate())) {
                    targetTemplateName = o.getPortlet().getTemplate(); 
                }
                else {
                    targetTemplateName = urlInterpreterResult.getTemplateName();
                }

                if (o.isPortlet()) {
                    Portlet p = o.getPortlet();
                    portletName = TemplateUtils.getFullPortletName( p.getName() );

                    namespace = NamespaceFactory.getNamespace(portletName, targetTemplateName, NamespaceFactory.getTemplateUniqueIndex(o, i++));

                    portletParameters = urlInterpreterResult.getParameters().get(namespace.getNamespace());
                    if (portletParameters == null) {
                        portletParameters = new PortletParameters(namespace.getNamespace(), new RequestState(), new HashMap<String, List<String>>());
                    }
                }
                else if (o.isDynamic()) {
                    portletName = urlInterpreterResult.getDefaultPortletName();
                    namespace = NamespaceFactory.getNamespace(portletName, targetTemplateName, NamespaceFactory.getTemplateUniqueIndex(o, i++));

                    portletParameters = urlInterpreterResult.getParameters().get(namespace.getNamespace());

                    if (portletParameters == null) {
                        log.error(
                            "portletParameters object is null, " +
                                "namespace: " + namespace.getNamespace() + ", " +
                                "portletName: " + portletName
                        );
                        // skip this portlet
                        continue;
                    }
                }

                // chech for template item is not restricted after create namespace
                // restriction of template item must be processed after
/*
                if (!checkTemplateItemRole(o)) {
                    continue;
                }
*/

                if (httpRequest.isRequestedSessionIdValid()) {
                    checkDestroyedPortlet(PortalInstanceImpl.destroyedPortlet(), httpRequest.getSession(false));
                }

                PageElement element;
                switch (o.getType()) {
                    case PORTLET: {
                        Portlet p = o.getPortlet();
                        if (log.isDebugEnabled()) {
                            log.debug("PageElementPortlet, " +
                                "name: " + p.getName() + ", " +
                                ", code: " + p.getCode() + ", xmlRoot: " + p.getXmlRoot());
                            log.debug("namespace: " + namespace);
                            log.debug("portletParameters: " + portletParameters);
                            log.debug("element.getParameters(): " + p.getElementParameter());
                        }

                        element = new PageElementPortlet(
                            portalInstance, namespace, portletParameters, targetTemplateName, p.getXmlRoot(), p.getCode(), p.getElementParameter(),
                            this, TemplateUtils.getFullPortletName( p.getName() ),
                            new ArrayList<String>(),
                            PreferenceFactory.getStubPortletPreferencePersistencer()
                        );
                        break;
                    }
                    case DYNAMIC: {
                        if (log.isDebugEnabled()) {
                            log.debug("PageElementPortlet as dynamiic");
                            log.debug("getDefaultPortletDefinition(): " + urlInterpreterResult.getDefaultPortletName());
                            log.debug("namespace: " + namespace);
                            log.debug("portletParameters: " + portletParameters);
                        }
                        element = new PageElementPortlet(
                            portalInstance, namespace, portletParameters, targetTemplateName, null, null, null,
                            this, urlInterpreterResult.getDefaultPortletName(),
                            urlInterpreterResult.getExtendedCatalogItem().getRoleList(),
                            PreferenceFactory.getPortletPreferencePersistencer(urlInterpreterResult.getExtendedCatalogItem().getCatalogId())
                        );
                        break;
                    }
                    case XSLT:
                        element = new XsltPageElement(o.getXslt().getName());
                        break;
                    case STRING:
                        element = new StringPageElement(o.getString());
                        break;
                    case INCLUDE:
                        continue;
                    default:
                        throw new PortalException("Unknown type of template element: " + o.getType());
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

    private void initTransformationParameters(CatalogItem catalogItem) {
        // set up transformation parameters - title, keywords, author, portal context path.
        this.portalTransformationParameters.setPortalContextPath(httpRequest.getContextPath());
        if (catalogItem!=null) {
            if (StringUtils.isNotBlank(catalogItem.getTitle())) {
                this.portalTransformationParameters.setTitle(catalogItem.getTitle());
            }
            else {
                this.portalTransformationParameters.setTitle(catalogItem.getKeyMessage());
            }
            if (StringUtils.isNotBlank(catalogItem.getKeyword())) {
                this.portalTransformationParameters.setKeyword(catalogItem.getKeyword());
            }
            else {
                this.portalTransformationParameters.setKeyword(catalogItem.getKeyMessage());
            }
            this.portalTransformationParameters.setAuthor(catalogItem.getAuthor());
        }
    }

    /**
     * remove form session all attributes, which are corresponded to destroyed portlet
     *
     * @param destroyedPortletNames name of destroyed portlet
     * @param session               http session
     */
    private static void checkDestroyedPortlet(List<String> destroyedPortletNames, HttpSession session) {
        if (session == null) {
            return;
        }
        try {
            NamespaceMapper nm = NamespaceFactory.getNamespaceMapper();
            //noinspection unchecked
            List<String> attrs = Collections.list(session.getAttributeNames());
            for (String portletName : destroyedPortletNames) {
                List<Namespace> namespaces = NamespaceFactory.getNamespaces(portletName);
                for (Namespace namespace : namespaces) {
                    for (String attr : attrs) {
                        String realAttrName = nm.decode(namespace, attr);
                        if (realAttrName != null) {
                            session.removeAttribute(attr);
                        }
                    }
                }
            }
        }
        catch (Throwable e) {
            log.error("Error remove attributed", e);
        }
    }

    private boolean checkTemplateItemRole(ParsedTemplateElement element) {
/*
        if (element == null || !element.isPortlet() || StringUtils.isBlank(element.getPortlet().get)) {
            return true;
        }

        if (auth == null) {
            return false;
        }

        StringTokenizer st = new StringTokenizer(templateItem.getRole(), ", ", false);
        while (st.hasMoreTokens()) {
            String role = st.nextToken();
            if (isUserInRole(role)) {
                return true;
            }
        }
*/
        log.warn("Role checker not implemented always return false");
        return false;
    }

    public List<PageElement> getPageElementList() {
        return pageElementList;
    }

    public String getErrorString() {
        return errorString;
    }

//    ActionRequestImpl getActionRequest() {
//        return actionRequest;
//    }
//
    public Locale[] getPreferredLocales() {
        return preferredLocales;
    }

    public AuthSession getAuth() {
        return auth;
    }

    private Map<String, Boolean> userRoles = new HashMap<String, Boolean>();

    public boolean isUserInRole(String role) {
        if (log.isDebugEnabled()) {
            log.debug("PortalRequestInstance.isUserInRole()");
            log.debug("    role: " + role);
        }
        if (role == null) {
            return false;
        }

        Boolean access = userRoles.get(role);
        if (access != null) {
            return access;
        }

        if (role.equals(PortalConstants.WEBMILL_GUEST_ROLE)) {
            return true;
        }

        if (log.isDebugEnabled()) {
            log.debug("    serverName: " + httpRequest.getServerName());
            log.debug("    auth: " + auth);
        }

        if (httpRequest.getServerName() == null || auth == null) {
            return role.equals(PortalConstants.WEBMILL_ANONYMOUS_ROLE);
        }

        // here auth always not null. return false for webmill.anonymous role
        if (role.equals(PortalConstants.WEBMILL_ANONYMOUS_ROLE)) {
            return false;
        }

        boolean status = auth.checkAccess(httpRequest.getServerName());
        if (!status) {
            userRoles.put(role, false);
            return false;
        }

        if (role.equals(PortalConstants.WEBMILL_AUTHENTIC_ROLE)) {
            userRoles.put(PortalConstants.WEBMILL_AUTHENTIC_ROLE, true);
            return true;
        }

        boolean roleRefAccess = auth.isUserInRole(role);
        userRoles.put(role, roleRefAccess);

        return roleRefAccess;
    }

    public PortalInstance getPortalInstance() {
        return portalInstance;
    }

    public Locale getLocale() {
        if (urlInterpreterResult == null)
            return null;
        else
            return urlInterpreterResult.getLocale();
    }

    public HttpServletRequest getHttpRequest() {
        return httpRequest;
    }

    public HttpServletResponse getHttpResponse() {
        return httpResponse;
    }

    private void initTemplate() throws PortalException {

        template = portalInstance.getPortalTemplateManager().getTemplate(urlInterpreterResult.getTemplateName(), getLocale().toString());

        if (template == null) {
            String errorString = "Template '" + urlInterpreterResult.getTemplateName() + "', locale " + getLocale().toString() + ", not found";
            log.warn(errorString);
            throw new PortalException(errorString);
        }
        log.debug("template:\n" + template.toString());
    }

    private void initXslt(PortalInfo portalInfo) throws PortalException {
        XsltTransformerManager transformerManager = XsltTransformetManagerFactory.getInstanse(portalInfo.getSiteId());
        // prepare Xsl objects
        if (transformerManager == null) {
            String errorString = "XSL template not defined";
            log.error(errorString);
            throw new PortalException(errorString);
        }
        xslt = transformerManager.getXslt(getLocale().toString());
        if (xslt == null) {
            String errorString = "XSLT for locale " + getLocale().toString() + " not defined.";
            log.error(errorString);
            throw new PortalException(errorString);
        }
    }

    public ExtendedCatalogItemBean getDefaultCtx() {
        return urlInterpreterResult.getExtendedCatalogItem();
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

    public UrlInterpreterResult getRequestContext() {
        return urlInterpreterResult;
    }

    public File getTempPath() {
        return tempPath;
    }

    public PortalTransformationParameters getPortalTransformationParameters() {
        return portalTransformationParameters;
    }

    private void initTempPath() {
        // init temp path for current request
        try {
            tempPath = (File) httpRequest.getAttribute("javax.servlet.context.tempdir");
        }
        catch (Throwable e) {
            log.error("error get temp path from request attributes, set to java.io.temp", e);
        }
        if (tempPath == null) {
            tempPath = new File(System.getProperty("java.io.tmpdir"));
        }
        if (!tempPath.exists()) {
            log.error("Specified temp directory '" + tempPath + "' not exists. Set to default java input/output temp directory");
            // TODO. or set to other dir?
            tempPath = new File(System.getProperty("java.io.tmpdir"));
        }
    }
}
