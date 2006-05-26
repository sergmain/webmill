package org.riverock.webmill.portal.dao;

import java.util.Date;

import org.riverock.interfaces.portal.user.UserMetadataItem;
import org.riverock.generic.db.DatabaseAdapter;

/**
 * @author Sergei Maslyukov
 *         Date: 26.05.2006
 *         Time: 20:52:25
 */
public interface InternalUserMetadataDao {
    public UserMetadataItem getMetadata(String userLogin, Long siteId, String metadataName);

    public UserMetadataItem getMetadata(DatabaseAdapter adapter, String userLogin, Long siteId, String metadataName);

    public void setMetadataIntValue(String userLogin, Long siteId, String metadataName, Long intValue);

    public void setMetadataStringValue(String userLogin, Long siteId, String metadataName, String stringValue);

    public void setMetadataDateValue(String userLogin, Long siteId, String metadataName, Date dateValue);
}
