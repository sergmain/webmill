package org.riverock.interfaces.portal.dao;

import java.util.List;

import org.riverock.interfaces.portal.bean.Template;

/**
 * @author SergeMaslyukov
 *         Date: 30.01.2006
 *         Time: 23:31:06
 *         $Id$
 */
public interface PortalCommonDao {
    public Template getTemplate(Long templateId);
    public List<Template> getTemplateList( Long siteId );
    public List<Template> getTemplateLanguageList( Long siteLanguageId );
}
