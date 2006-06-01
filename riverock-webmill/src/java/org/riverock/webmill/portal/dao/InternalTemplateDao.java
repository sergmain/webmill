package org.riverock.webmill.portal.dao;

import java.util.List;

import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.interfaces.portal.bean.Template;

/**
 * @author Sergei Maslyukov
 *         Date: 03.05.2006
 *         Time: 17:51:14
 */
public interface InternalTemplateDao {
    public Template getTemplate(Long templateId);
    public Template getTemplate(String templateName, Long siteLanguageId);
    public List<Template> getTemplateLanguageList( Long siteLanguageId );
    public List<Template> getTemplateList( Long siteId );
    public Long createTemplate(Template template);

    public void deleteTemplateForSite(DatabaseAdapter adapter, Long siteId);

    public void deleteTemplateForSiteLanguage(DatabaseAdapter adapter, Long siteLanguageId);

    public void updateTemplate(Template template);

    public void deleteTemplate(Long templateId);
}
