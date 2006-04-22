package org.riverock.webmill.portal.context;

import java.util.Locale;
import java.io.File;

import javax.servlet.http.HttpServletRequest;

import org.riverock.interfaces.portal.PortalInfo;
import org.riverock.webmill.container.portlet.PortletContainer;

/**
 * @author Sergei Maslyukov
 *         Date: 20.04.2006
 *         Time: 12:46:07
 */
public final class RequestContextParameter {
    private HttpServletRequest request = null;
    private PortalInfo portalInfo = null;
    private PortletContainer portletContainer = null;
    private Locale predictedLocale = null;
    private boolean isMultiPartRequest = false;
    private File requestBodyFile = null;

    public RequestContextParameter(HttpServletRequest request, PortalInfo portalInfo, PortletContainer portletContainer, boolean isMultiPartRequest, File requestBodyFile) {
        this.request = request;
        this.portalInfo = portalInfo;
        this.portletContainer = portletContainer;
        this.predictedLocale = RequestContextUtils.prepareLocale(this);
        this.isMultiPartRequest = isMultiPartRequest;
        this.requestBodyFile = requestBodyFile;
    }

    public File getRequestBodyFile() {
        return requestBodyFile;
    }

    public boolean isMultiPartRequest() {
        return isMultiPartRequest;
    }

    public Locale getPredictedLocale() {
        return predictedLocale;
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    public PortalInfo getPortalInfo() {
        return portalInfo;
    }

    public PortletContainer getPortletContainer() {
        return portletContainer;
    }

}
