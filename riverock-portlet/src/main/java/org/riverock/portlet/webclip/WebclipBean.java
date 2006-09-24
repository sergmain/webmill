/*
 * org.riverock.portlet - Portlet Library
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
package org.riverock.portlet.webclip;

import java.util.Date;

/**
 * User: SergeMaslyukov
 * Date: 10.09.2006
 * Time: 17:49:00
 * <p/>
 * $Id$
 */
public class WebclipBean {
    private Long webclipId;
    private Long siteId;
    private String webclipData;
    private Date datePost;

    public WebclipBean() {
    }

    public WebclipBean(Long siteId, Long webclipId, String webclipData, Date datePost) {
        this.webclipId = webclipId;
        this.siteId = siteId;
        this.webclipData = webclipData;
        this.datePost = datePost;
    }

    public Long getWebclipId() {
        return webclipId;
    }

    public void setWebclipId(Long webclipId) {
        this.webclipId = webclipId;
    }

    public Long getSiteId() {
        return siteId;
    }

    public void setSiteId(Long siteId) {
        this.siteId = siteId;
    }

    public String getWebclipData() {
        return webclipData;
    }

    public void setWebclipData(String webclipData) {
        this.webclipData = webclipData;
    }

    public Date getDatePost() {
        return datePost;
    }

    public void setDatePost(Date datePost) {
        this.datePost = datePost;
    }

    public String toString() {
        return "webclipId: "+webclipId + ", siteId: "+siteId;
    }
}
