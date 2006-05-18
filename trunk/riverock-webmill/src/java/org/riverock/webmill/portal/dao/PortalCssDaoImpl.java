package org.riverock.webmill.portal.dao;

import java.util.List;

import org.riverock.interfaces.portal.dao.PortalCssDao;
import org.riverock.interfaces.portal.bean.Css;
import org.riverock.interfaces.sso.a3.AuthSession;

/**
 * @author Sergei Maslyukov
 *         Date: 18.05.2006
 *         Time: 13:32:44
 */
public class PortalCssDaoImpl implements PortalCssDao {
    private AuthSession authSession = null;

    PortalCssDaoImpl(AuthSession authSession) {
        this.authSession = authSession;
    }

    public Css getCss(Long cssId) {
        return InternalDaoFactory.getInternalCssDao().getCss(cssId);
    }

    public Long createCss(Css css) {
        return InternalDaoFactory.getInternalCssDao().createCss(css);
    }

    public List<Css> getCssList(Long siteId) {
        return InternalDaoFactory.getInternalCssDao().getCssList(siteId);
    }
}
