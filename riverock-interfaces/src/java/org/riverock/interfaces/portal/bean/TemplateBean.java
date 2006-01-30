package org.riverock.interfaces.portal.bean;

/**
 * @author SergeMaslyukov
 *         Date: 30.01.2006
 *         Time: 23:41:35
 *         $Id$
 */
public interface TemplateBean {
    public String getTemplateLanguage();
    public Long getTemplateId();
    public Long getSiteLanguageId();
    public String getTemplateName();
    public String getTemplateData();
}
