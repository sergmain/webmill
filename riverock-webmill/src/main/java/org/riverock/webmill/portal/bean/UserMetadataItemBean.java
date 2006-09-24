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
package org.riverock.webmill.portal.bean;

import java.util.Date;

import org.riverock.interfaces.portal.user.UserMetadataItem;

/**
 * @author Sergei Maslyukov
 *         Date: 26.05.2006
 *         Time: 20:21:04
 */
public class UserMetadataItemBean implements UserMetadataItem {
    private Long metadataItemId = null;
    private Long siteId = null;
    private Long userId = null;
    private String metadataName = null;
    private Long intValue = null;
    private Date dateValue = null;
    private String stringValue = null;

    public Long getMetadataItemId() {
        return metadataItemId;
    }

    public void setMetadataItemId(Long metadataItemId) {
        this.metadataItemId = metadataItemId;
    }

    public Long getSiteId() {
        return siteId;
    }

    public void setSiteId(Long siteId) {
        this.siteId = siteId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getMetadataName() {
        return metadataName;
    }

    public void setMetadataName(String metadataName) {
        this.metadataName = metadataName;
    }

    public Long getIntValue() {
        return intValue;
    }

    public void setIntValue(Long intValue) {
        this.intValue = intValue;
    }

    public Date getDateValue() {
        if (dateValue==null) {
            return null;
        }
        return new Date(dateValue.getTime());
    }

    public void setDateValue(Date dateValue) {
        if (dateValue==null) {
            this.dateValue=null;
            return;
        }
        this.dateValue = new Date(dateValue.getTime());
    }

    public String getStringValue() {
        return stringValue;
    }

    public void setStringValue(String stringValue) {
        this.stringValue = stringValue;
    }
}
