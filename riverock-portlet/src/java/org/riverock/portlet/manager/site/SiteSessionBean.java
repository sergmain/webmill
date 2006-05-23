/**
 * License
 * 
 */
package org.riverock.portlet.manager.site;

import java.io.Serializable;

import org.riverock.portlet.manager.site.bean.CssBean;
import org.riverock.portlet.manager.site.bean.SiteExtended;
import org.riverock.portlet.manager.site.bean.SiteLanguageBean;
import org.riverock.portlet.manager.site.bean.TemplateBean;
import org.riverock.portlet.manager.site.bean.XsltBean;

/**
 * @author Sergei Maslyukov
 *         16.05.2006
 *         20:24:52
 */
public class SiteSessionBean implements Serializable {
    private static final long serialVersionUID = 2058005508L;

    public static final int SITE_TYPE = 1;
    public static final int SITE_LANGUAGE_TYPE = 2;
    public static final int TEMPLATE_TYPE = 3;
    public static final int XSLT_TYPE = 4;
    public static final int CSS_TYPE = 5;

    private boolean isEdit;
    private Long id = null;
    private int objectType=0;

    private SiteExtended siteExtended = null;
    private SiteLanguageBean siteLanguage = null;
    private TemplateBean template = null;
    private CssBean css = null;
    private XsltBean xslt = null;

    public SiteExtended getSiteExtended() {
        return siteExtended;
    }

    public void setSiteExtended(SiteExtended siteExtended) {
        this.siteExtended = siteExtended;
    }

    public SiteLanguageBean getSiteLanguage() {
        return siteLanguage;
    }

    public void setSiteLanguage(SiteLanguageBean siteLanguage) {
        this.siteLanguage = siteLanguage;
    }

    public TemplateBean getTemplate() {
        return template;
    }

    public void setTemplate(TemplateBean template) {
        this.template = template;
    }

    public CssBean getCss() {
        return css;
    }

    public void setCss(CssBean css) {
        this.css = css;
    }

    public XsltBean getXslt() {
        return xslt;
    }

    public void setXslt(XsltBean xslt) {
        this.xslt = xslt;
    }

    public int getSiteType() {
        return SITE_TYPE;
    }

    public int getSiteLanguageType() {
        return SITE_LANGUAGE_TYPE;
    }

    public int getTemplateType() {
        return TEMPLATE_TYPE;
    }

    public int getXsltType() {
        return XSLT_TYPE;
    }

    public int getCssType() {
        return CSS_TYPE;
    }

    public boolean isEdit() {
        return isEdit;
    }

    public void setEdit(boolean isEdit) {
        this.isEdit = isEdit;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getObjectType() {
        return objectType;
    }

    public void setObjectType(int objectType) {
        this.objectType = objectType;
    }
}
