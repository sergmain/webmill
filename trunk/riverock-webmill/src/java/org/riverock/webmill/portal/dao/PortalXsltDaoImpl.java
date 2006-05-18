package org.riverock.webmill.portal.dao;

import java.util.Map;
import java.util.List;

import org.riverock.interfaces.portal.dao.PortalXsltDao;
import org.riverock.interfaces.portal.bean.Xslt;
import org.riverock.interfaces.sso.a3.AuthSession;

/**
 * @author Sergei Maslyukov
 *         Date: 17.05.2006
 *         Time: 14:17:22
 */
public class PortalXsltDaoImpl implements PortalXsltDao {
    private AuthSession authSession = null;

    PortalXsltDaoImpl(AuthSession authSession) {
        this.authSession = authSession;
    }

    public StringBuilder getXsltData(Long xsltId) {
        return InternalDaoFactory.getInternalXsltDao().getXsltData(xsltId);
    }

    /**
     * key is language of site
     */
    public Map<String, Xslt> getCurrentXsltForSiteAsMap(Long siteId) {
        return InternalDaoFactory.getInternalXsltDao().getCurrentXsltForSiteAsMap(siteId);
    }

    public Xslt getCurrentXslt(Long siteLanguageId) {
        return InternalDaoFactory.getInternalXsltDao().getCurrentXslt(siteLanguageId);
    }

    public Xslt getXslt(Long siteId) {
        return InternalDaoFactory.getInternalXsltDao().getXslt(siteId);
    }

    public Xslt getXslt(String xsltName, Long siteLanguageId) {
        return InternalDaoFactory.getInternalXsltDao().getXslt(xsltName, siteLanguageId);
    }

    public Long createXslt(Xslt xslt) {
        return InternalDaoFactory.getInternalXsltDao().createXslt(xslt);
    }

    public List<Xslt> getXsltList(Long siteLanguageId) {
        return InternalDaoFactory.getInternalXsltDao().getXsltList(siteLanguageId);
    }
}
