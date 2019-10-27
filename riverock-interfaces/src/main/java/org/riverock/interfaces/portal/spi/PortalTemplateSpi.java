package org.riverock.interfaces.portal.spi;

import java.util.List;

import org.riverock.interfaces.portal.dao.PortalTemplateDao;
import org.riverock.interfaces.portal.bean.Template;

/**
 * User: SergeMaslyukov
 * Date: 02.09.2007
 * Time: 12:52:14
 * $Id$
 */
public interface PortalTemplateSpi extends PortalTemplateDao {
    public Template getTemplate(Long templateId);
    public Template getTemplate(String templateName, Long siteLanguageId);
    public List<Template> getTemplateLanguageList( Long siteLanguageId );
    public List<Template> getTemplateList( Long siteId );

    public Long createTemplate(Template template);
    public void updateTemplate(Template template);
    public void deleteTemplate(Long templateId);

    public void setDefaultDynamic(Long templateId);
    public Template getDefaultDynamicTemplate(Long siteLanguageId);
    public void setMaximizedTemplate(Long templateId);
    public Template getMaximizedTemplate(Long siteLanguageId);
    public void setPopupTemplate(Long templateId);
    public Template getPopupTemplate(Long siteLanguageId);
}
