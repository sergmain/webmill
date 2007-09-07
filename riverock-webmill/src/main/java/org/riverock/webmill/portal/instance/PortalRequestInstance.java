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
package org.riverock.webmill.portal.instance;

import java.io.File;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import org.riverock.common.html.Header;
import org.riverock.common.tools.MainTools;
import org.riverock.interfaces.portal.CookieManager;
import org.riverock.interfaces.portal.PortalInfo;
import org.riverock.interfaces.portal.bean.CatalogItem;
import org.riverock.interfaces.portal.spi.PortalSpiProvider;
import org.riverock.interfaces.portal.xslt.XsltTransformer;
import org.riverock.interfaces.sso.a3.AuthSession;
import org.riverock.sso.a3.AuthTools;
import org.riverock.webmill.exception.PortalException;
import org.riverock.webmill.portal.CookieManagerImpl;
import org.riverock.webmill.portal.PortalConstants;
import org.riverock.webmill.portal.PortalInstance;
import org.riverock.webmill.portal.PortalRequest;
import org.riverock.webmill.portal.PortalTransformationParameters;
import org.riverock.webmill.portal.dao.InternalDaoFactory;
import org.riverock.webmill.portal.info.PortalInfoImpl;
import org.riverock.webmill.portal.spi.PortalSpiProviderImpl;
import org.riverock.webmill.portal.template.PortalTemplate;
import org.riverock.webmill.portal.template.parser.ParsedTemplateElement;
import org.riverock.webmill.portal.url.UrlInterpreterIterator;
import org.riverock.webmill.portal.url.definition_provider.PortletDefinitionProviderImpl;
import org.riverock.webmill.portal.url.interpreter.ExtendedCatalogItemBean;
import org.riverock.webmill.portal.url.interpreter.UrlInterpreterParameter;
import org.riverock.webmill.portal.url.interpreter.UrlInterpreterResult;
import org.riverock.webmill.portal.utils.PortalUtils;
import org.riverock.webmill.utils.PortletUtils;

/**
 * User: Admin
 * Date: Aug 26, 2003
 * Time: 4:40:19 PM
 * <p/>
 * $Id$
 */
public final class PortalRequestInstance implements PortalRequest {
    private final static Logger log = Logger.getLogger(PortalRequestInstance.class);

    private static final int MAX_REQUEST_BODY_SIZE = 25 * 1024 * 1024; // 25Mb

    private XsltTransformer xslt = null;
    private PortalTemplate template = null;

    private long startMills;

    private UrlInterpreterResult urlInterpreterResult = null;

    /**
     * array of preffered locales from http request
     */
    private Locale[] preferredLocales = null;

    private HttpServletRequest httpRequest = null;
    private HttpServletResponse httpResponse = null;
    private AuthSession auth = null;
    private CookieManager cookieManager = new CookieManagerImpl();

    private String errorString = null;

    /**
     * File with request data, if request is multipart
     */
    private File requestBodyFile = null;
    private boolean isMultiPartRequest = false;
    private PortalSpiProvider portalDaoProvider = null;
    private File tempPath = null;

    private PortalTransformationParameters portalTransformationParameters = new PortalTransformationParameters();

    public void destroy() {
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
        MainTools.deleteFile(requestBodyFile);
        requestBodyFile = null;
        portalDaoProvider = null;
        portalTransformationParameters = null;
    }

    public PortalRequestInstance() {
        startMills = System.currentTimeMillis();
    }

    public PortalSpiProvider getPortalSpiProvider() {
        return portalDaoProvider;
    }

    public UrlInterpreterResult getUrlInterpreterResult() {
        return urlInterpreterResult;
    }

    public PortalRequestInstance(
        HttpServletRequest request_,
        HttpServletResponse response_,
        PortalInstance portalInstance
    ) throws PortalException {

        this.startMills = System.currentTimeMillis();

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
            this.portalDaoProvider = new PortalSpiProviderImpl(auth, classLoader, portalInstance.getSiteId());
            if (log.isDebugEnabled()) {
                log.debug("auth: " + this.auth);
                log.debug("portal requet instance class loader:\n" + classLoader + "\nhash: " + classLoader.hashCode());
                ClassLoader cl = portalInstance.getPortletContainer().getClass().getClassLoader();
                log.debug("portlet container class loader:\n" + classLoader + "\nhash: " + cl.hashCode());
            }
            this.preferredLocales = Header.getAcceptLanguageAsLocaleListSorted(httpRequest);

            PortalInfo portalInfo = PortalInfoImpl.getInstance(portalInstance.getPortalClassLoader(), portalInstance.getSiteId());
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

            initTemplate(portalInstance);
            initXslt(portalInstance);
        }
        finally {
            if (log.isInfoEnabled()) {
                log.info("init PortalRequestInstance for " + (System.currentTimeMillis() - getStartMills()) + " milliseconds");
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

    public String getErrorString() {
        return errorString;
    }

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

    public PortalTemplate getTemplate() {
        return template;
    }

    private void initTemplate(PortalInstance portalInstance) throws PortalException {

        template = portalInstance.getPortalTemplateManager().getTemplate(urlInterpreterResult.getTemplateName(), getLocale().toString());

        if (getTemplate() == null) {
            String errorString = "Template '" + urlInterpreterResult.getTemplateName() + "', locale " + getLocale().toString() + ", not found";
            log.warn(errorString);
            throw new PortalException(errorString);
        }
        log.debug("template:\n" + getTemplate().toString());
    }

    private void initXslt(PortalInstance portalInstance) throws PortalException {
        xslt = portalInstance.getXsltTransformerManager().getXslt(getLocale().toString());
        if (getXslt() == null) {
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

    public XsltTransformer getXslt() {
        return xslt;
    }

    public long getStartMills() {
        return startMills;
    }
}
