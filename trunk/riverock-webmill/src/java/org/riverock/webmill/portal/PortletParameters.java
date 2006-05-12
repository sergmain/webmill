package org.riverock.webmill.portal;

import java.util.Map;
import java.util.List;
import java.io.File;

import org.riverock.webmill.portal.context.RequestState;

/**
 * @author Sergei Maslyukov
 *         Date: 20.04.2006
 *         Time: 17:20:09
 */
public final class PortletParameters {
    private String namespace = null;
    private Map<String, List<String>> parameters = null;

    /** File with request data, if request is multipart */
    private File requestBodyFile = null;

    private boolean isMultiPart = false;
    private RequestState requestState = null;

    public PortletParameters( final String namespace, RequestState requestState, final Map<String, List<String>> parameters) {
        this.namespace = namespace;
        this.parameters = parameters;
        this.requestState = requestState;
    }

    public PortletParameters( final String namespace, RequestState requestState, File requestBodyFile ) {
        this.namespace = namespace;
        this.requestBodyFile = requestBodyFile;
        this.isMultiPart = true;
        this.requestState = requestState;
    }

    public RequestState getRequestState() {
        return requestState;
    }

    public boolean isMultiPart() {
        return isMultiPart;
    }

    public File getRequestBodyFile() {
        return requestBodyFile;
    }

    public String getNamespace() {
        return namespace;
    }

    public Map<String, List<String>> getParameters() {
        return parameters;
    }
}
