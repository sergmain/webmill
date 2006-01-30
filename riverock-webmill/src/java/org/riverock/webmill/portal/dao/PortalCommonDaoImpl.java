package org.riverock.webmill.portal.dao;

import java.util.List;

import org.riverock.interfaces.sso.a3.AuthSession;
import org.riverock.interfaces.portal.bean.TemplateBean;
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
    public TemplateBean getTemplateBean(Long templateId) {
        return InternalDaoFactory.getInternalDao().getTemplateBean(templateId);
    };

    public List<TemplateBean> getTemplateList( Long siteId ) {
        return InternalDaoFactory.getInternalDao().getTemplateList(siteId);
    }

    public List<TemplateBean> getTemplateLanguageList(Long siteLanguageId) {
        return InternalDaoFactory.getInternalDao().getTemplateLanguageList(siteLanguageId);
    }

}
