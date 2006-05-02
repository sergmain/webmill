package org.riverock.webmill.main;

import java.util.Date;

import org.riverock.interfaces.portal.bean.Css;

/**
 * @author SergeMaslyukov
 *         Date: 27.01.2006
 *         Time: 3:11:20
 *         $Id$
 */
public class CssBean implements Css {
    private String css = "";
    private Date date = null;

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
