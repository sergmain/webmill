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
package org.riverock.webmill.a3.audit;

import java.util.Date;

/**
 * @author SergeMaslyukov
 *         Date: 27.01.2006
 *         Time: 1:59:12
 *         $Id$
 */
public class RequestStatisticBean {
    private String userAgent = null;
    private String url = null;
    private String serverName = null;
    private String refer = null;
    private String parameters = null;
    private boolean isReferTooBig;
    private boolean isParamTooBig;
    private String remoteAddr = null;
    private Date accessDate = null;

    public String getParameters() {
        return parameters;
    }

    public void setParameters(String parameters) {
        this.parameters = parameters;
    }

    public boolean isParamTooBig() {
        return isParamTooBig;
    }

    public void setParamTooBig(boolean paramTooBig) {
        isParamTooBig = paramTooBig;
    }

    public Date getAccessDate() {
        return accessDate;
    }

    public void setAccessDate(Date accessDate) {
        this.accessDate = accessDate;
    }

    public String getRefer() {
        return refer;
    }

    public void setRefer(String refer) {
        this.refer = refer;
    }

    public boolean isReferTooBig() {
        return isReferTooBig;
    }

    public void setReferTooBig(boolean referTooBig) {
        isReferTooBig = referTooBig;
    }

    public String getRemoteAddr() {
        return remoteAddr;
    }

    public void setRemoteAddr(String remoteAddr) {
        this.remoteAddr = remoteAddr;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

}
