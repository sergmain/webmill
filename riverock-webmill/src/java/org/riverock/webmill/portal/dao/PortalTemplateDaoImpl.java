package org.riverock.webmill.portal.dao;

import java.util.List;

import org.riverock.interfaces.portal.dao.PortalTemplateDao;
import org.riverock.interfaces.portal.bean.Template;
import org.riverock.interfaces.sso.a3.AuthSession;

/**
 * @author Sergei Maslyukov
 *         Date: 17.05.2006
 *         Time: 14:12:05
 */
public class PortalTemplateDaoImpl implements PortalTemplateDao {
    private AuthSession authSession = null;

    PortalTemplateDaoImpl(AuthSession authSession) {
        this.authSession = authSession;
    }

    public Template getTemplate(Long templateId) {
        return InternalDaoFactory.getInternalTemplateDao().getTemplate(templateId);
    }

    public Template getTemplate(String templateName, Long siteLanguageId) {
        return InternalDaoFactory.getInternalTemplateDao().getTemplate(siteLanguageId);
    }

    public List<Template> getTemplateLanguageList(Long siteLanguageId) {
        return InternalDaoFactory.getInternalTemplateDao().getTemplateLanguageList(siteLanguageId);
    }

    public List<Template> getTemplateList(Long siteId) {
        return InternalDaoFactory.getInternalTemplateDao().getTemplateList(siteId);
    }

    public Long createTemplate(Template template) {
        return InternalDaoFactory.getInternalTemplateDao().createTemplate(template);
    }
}
