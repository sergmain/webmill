package org.riverock.webmill.portal.dao;

import java.util.Date;

import org.riverock.interfaces.portal.user.UserMetadataItem;
import org.riverock.interfaces.portal.dao.PortalUserMetadataDao;
import org.riverock.interfaces.sso.a3.AuthSession;

/**
 * @author Sergei Maslyukov
 *         Date: 26.05.2006
 *         Time: 21:18:50
 */
public class PortalUserMetadataDaoImpl implements PortalUserMetadataDao {
    private AuthSession authSession = null;

    PortalUserMetadataDaoImpl(AuthSession authSession) {
        this.authSession = authSession;
    }

    public UserMetadataItem getMetadata(String userLogin, Long siteId, String metadataName) {
        return InternalDaoFactory.getInternalUserMetadataDao().getMetadata(userLogin, siteId, metadataName);
    }

    public void setMetadataIntValue(String userLogin, Long siteId, String metadataName, Long intValue) {
        InternalDaoFactory.getInternalUserMetadataDao().setMetadataIntValue(userLogin, siteId, metadataName, intValue);
    }

    public void setMetadataStringValue(String userLogin, Long siteId, String metadataName, String stringValue) {
        InternalDaoFactory.getInternalUserMetadataDao().setMetadataStringValue(userLogin, siteId, metadataName, stringValue);
    }

    public void setMetadataDateValue(String userLogin, Long siteId, String metadataName, Date dateValue) {
        InternalDaoFactory.getInternalUserMetadataDao().setMetadataDateValue(userLogin, siteId, metadataName, dateValue);
    }
}
