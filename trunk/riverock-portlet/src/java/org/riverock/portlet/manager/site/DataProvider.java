/**
 * License
 * 
 */
package org.riverock.portlet.manager.site;

import java.io.Serializable;

import org.apache.log4j.Logger;

import org.riverock.portlet.manager.site.bean.SiteExtended;
import org.riverock.interfaces.portal.bean.SiteLanguage;
import org.riverock.interfaces.portal.bean.Template;
import org.riverock.interfaces.portal.bean.Xslt;
import org.riverock.interfaces.portal.bean.Css;

/**
 * @author Sergei Maslyukov
 * 16.05.2006
 * 20:27:53
 *
 *
 */
public class DataProvider implements Serializable {
    private final static Logger log = Logger.getLogger(DataProvider.class);
    private static final long serialVersionUID = 2057005500L;

    private SiteService siteService=null;
    private SiteSessionBean siteSessionBean = null;

    private SiteExtended siteExtended = null;
    private SiteLanguage siteLanguage = null;
    private Template template = null;
    private Xslt xslt = null;
    private Css css = null;

    public DataProvider() {
    }

    public SiteSessionBean getSiteSessionBean() {
        return siteSessionBean;
    }

    public void setSiteSessionBean(SiteSessionBean siteSessionBean) {
        this.siteSessionBean = siteSessionBean;
    }

    public void setSiteService(SiteService siteService) {
        this.siteService = siteService;
    }

    public SiteExtended getSiteExtended() {
        if (siteSessionBean.getObjectType()!=siteSessionBean.getSiteType()) {
            throw new IllegalStateException("Query site info with not site type, current type: " + siteSessionBean.getObjectType());
        }
        Long siteId = siteSessionBean.getId();
        if (siteExtended==null) {
            siteExtended = siteService.getSiteExtended(siteId);
        }
        if (!siteExtended.getSite().equals(siteId)) {
            log.warn("Mismatch siteId");
            siteExtended = siteService.getSiteExtended(siteId);
        }

        return siteExtended;
    }

    public void clearSite() {
        this.siteExtended=null;
    }

    public SiteLanguage getSiteLanguage() {
        if (siteSessionBean.getObjectType()!=siteSessionBean.getSiteLanguageType()) {
            throw new IllegalStateException("Query site language info with not site language type, current type: " + siteSessionBean.getObjectType());
        }
        Long siteLangaugeId = siteSessionBean.getId();
        if (siteLanguage==null) {
            siteLanguage = siteService.getSiteLanguage(siteLangaugeId);
        }
        if (!siteLanguage.getSiteLanguageId().equals(siteLangaugeId)) {
            log.warn("Mismatch siteLangaugeId");
            siteLanguage = siteService.getSiteLanguage(siteLangaugeId);
        }

        return siteLanguage;
    }

    public void clearSiteLanguage() {
        this.siteLanguage=null;
    }

    public Template getTemplate() {
        if (siteSessionBean.getObjectType()!=siteSessionBean.getTemplateType()) {
            throw new IllegalStateException("Query template info with not template type, current type: " + siteSessionBean.getObjectType());
        }
        Long templateId = siteSessionBean.getId();
        if (template==null) {
            template = siteService.getTemplate(templateId);
        }
        if (!template.getTemplateId().equals(templateId)) {
            log.warn("Mismatch templateId");
            template = siteService.getTemplate(templateId);
        }

        return template;
    }

    public void clearTemplate() {
        this.template=null;
    }

    public Xslt getXslt() {
        if (siteSessionBean.getObjectType()!=siteSessionBean.getXsltType()) {
            throw new IllegalStateException("Query xslt info with not xslt type, current type: " + siteSessionBean.getObjectType());
        }
        Long xsltId = siteSessionBean.getId();
        if (xslt==null) {
            xslt = siteService.getXslt(xsltId);
        }
        if (!xslt.getId().equals(xsltId)) {
            log.warn("Mismatch xsltId");
            xslt = siteService.getXslt(xsltId);
        }

        return xslt;
    }

    public void clearXslt() {
        this.xslt=null;
    }

    public Css getCss() {
        if (siteSessionBean.getObjectType()!=siteSessionBean.getCssType()) {
            throw new IllegalStateException("Query CSS info with not CSS type, current type: " + siteSessionBean.getObjectType());
        }
        Long cssId = siteSessionBean.getId();
        if (css==null) {
            css = siteService.getCss(cssId);
        }
        if (!css.getCssId().equals(cssId)) {
            log.warn("Mismatch cssId");
            css = siteService.getCss(cssId);
        }

        return css;
    }

    public void clearCss() {
        this.css=null;
    }

}
