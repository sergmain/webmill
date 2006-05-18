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
