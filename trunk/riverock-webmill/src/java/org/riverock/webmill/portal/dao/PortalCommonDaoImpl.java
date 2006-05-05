package org.riverock.webmill.portal.dao;

import java.util.List;

import org.riverock.interfaces.sso.a3.AuthSession;
import org.riverock.interfaces.portal.bean.Template;
import org.riverock.interfaces.portal.dao.PortalCommonDao;

/**
 * @author SergeMaslyukov
 *         Date: 30.01.2006
 *         Time: 23:37:53
 *         $Id$
 */
public class PortalCommonDaoImpl implements PortalCommonDao {
    private AuthSession authSession = null;

    PortalCommonDaoImpl(AuthSession authSession) {
        this.authSession = authSession;
    }
    public Template getTemplate(Long templateId) {
        return InternalDaoFactory.getInternalTemplateDao().getTemplate(templateId);
    }

    public List<Template> getTemplateList( Long siteId ) {
        return InternalDaoFactory.getInternalTemplateDao().getTemplateList(siteId);
    }

    public List<Template> getTemplateLanguageList(Long siteLanguageId) {
        return InternalDaoFactory.getInternalTemplateDao().getTemplateLanguageList(siteLanguageId);
    }

}
