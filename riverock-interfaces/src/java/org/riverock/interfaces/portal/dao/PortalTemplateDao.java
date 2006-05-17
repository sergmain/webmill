package org.riverock.interfaces.portal.dao;

import java.util.List;

import org.riverock.interfaces.portal.bean.Template;

/**
 * @author Sergei Maslyukov
 *         Date: 17.05.2006
 *         Time: 13:55:30
 */
public interface PortalTemplateDao {
    public Template getTemplate(Long templateId);
    public Template getTemplate(String templateName, Long siteLanguageId);
    public List<Template> getTemplateLanguageList( Long siteLanguageId );
    public List<Template> getTemplateList( Long siteId );
    public Long createTemplate(Template template);
}
