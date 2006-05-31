package org.riverock.webmill.main;

import java.util.Date;
import java.io.Serializable;

import org.riverock.interfaces.portal.bean.Css;

/**
 * @author SergeMaslyukov
 *         Date: 27.01.2006
 *         Time: 3:11:20
 *         $Id$
 */
public class CssBean implements Serializable, Css {
    private static final long serialVersionUID = 3037005502L;

    private Long cssId = null;
    private Long siteId = null;
    private String css = "";
    private String cssComment = "";
    private Date date = null;
    private boolean isCurrent = false;

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
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}