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
package org.riverock.portlet.manager.site.bean;

import java.io.Serializable;

import org.riverock.interfaces.portal.bean.Xslt;

/**
 * @author Sergei Maslyukov
 *         Date: 18.05.2006
 *         Time: 13:24:13
 */
public class XsltBean implements Serializable, Xslt {
    private static final long serialVersionUID = 2058005504L;

    private Long id = null;
    private Long siteLanguageId = null;
    private String name = null;
    private String xsltData = null;
    private boolean isCurrent = false;

    public XsltBean(){
    }

    public XsltBean(Xslt xslt){
        this.id=xslt.getId();
        this.name=xslt.getName();
        this.siteLanguageId=xslt.getSiteLanguageId();
        this.xsltData=xslt.getXsltData();
        this.isCurrent=xslt.isCurrent();
    }

    public boolean isCurrent() {
        return isCurrent;
    }

    public void setCurrent(boolean current) {
        isCurrent = current;
    }

    public Long getSiteLanguageId() {
        return siteLanguageId;
    }

    public void setSiteLanguageId(Long siteLanguageId) {
        this.siteLanguageId = siteLanguageId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getXsltData() {
        return xsltData;
    }

    public void setXsltData(String xsltData) {
        this.xsltData = xsltData;
    }
}
