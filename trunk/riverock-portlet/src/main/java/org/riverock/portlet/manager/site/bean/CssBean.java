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
import java.util.Date;

import org.riverock.interfaces.portal.bean.Css;

/**
 * @author Sergei Maslyukov
 *         Date: 18.05.2006
 *         Time: 13:28:41
 */
public class CssBean implements Serializable, Css {
    private static final long serialVersionUID = 2057004503L;

    private Long cssId = null;
    private Long siteId = null;
    private String css = "";
    private String cssComment = "";
    private Date date = null;
    private boolean isCurrent = false;

    public CssBean(){
    }

    public CssBean(Css css) {
        this.cssId=css.getCssId();
        this.siteId=css.getSiteId();
        this.css=css.getCss();
        this.cssComment=css.getCssComment();
        this.date=css.getDate();
        this.isCurrent=css.isCurrent();
    }

    public String getCssComment() {
        return cssComment;
    }

    public void setCssComment(String cssComment) {
        this.cssComment = cssComment;
    }

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

    public boolean isCurrent() {
        return isCurrent;
    }

    public void setCurrent(boolean current) {
        isCurrent = current;
    }

    public String getCss() {
        return css;
    }

    public void setCss(String css) {
        this.css = css;
    }

    public Date getDate() {
        if (date==null) {
            return null;
        }
        return new Date(date.getTime());
    }

    public void setDate(Date date) {
        if (date==null) {
            this.date=null;
            return;
        }
        this.date = new Date(date.getTime());
    }
}
