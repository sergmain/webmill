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
