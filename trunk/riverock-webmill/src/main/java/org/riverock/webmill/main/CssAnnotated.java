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
package org.riverock.webmill.main;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author SergeMaslyukov
 *         Date: 27.01.2006
 *         Time: 3:11:20
 *         $Id: CssBean.java 1019 2006-09-24 21:46:54Z serg_main $
 */
@Entity
@Table(name="wm_portal_css")
public class CssAnnotated {
    @Id
    @Column(name="ID_SITE_CONTENT_CSS")
    private Long cssId = null;

    @Column(name="ID_SITE")
    private Long siteId = null;

    public Long getCssId() {
        return cssId;
    }

    public void setCssId(Long cssId) {
        this.cssId = cssId;
    }

    public Long getSiteId() {
        return siteId;
    }

    public void setSiteId(Long siteId) {
        this.siteId = siteId;
    }

    public String toString() {
        return "[cssId:"+cssId+";siteId:"+siteId+"]";
    }
}
