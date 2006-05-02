package org.riverock.webmill.portal.bean;

import java.io.Serializable;

import org.riverock.interfaces.portal.bean.Xslt;

/**
 * @author Sergei Maslyukov
 *         Date: 02.05.2006
 *         Time: 17:21:17
 */
public class PortalXsltBean implements Serializable, Xslt {
    private static final long serialVersionUID = 3255005501L;

    private Long id = null;
    private String name = null;
    private String xsltData = null;

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
