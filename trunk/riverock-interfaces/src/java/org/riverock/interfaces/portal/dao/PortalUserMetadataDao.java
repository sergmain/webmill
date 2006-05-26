package org.riverock.interfaces.portal.dao;

import java.util.Date;

import org.riverock.interfaces.portal.user.UserMetadataItem;

/**
 * @author Sergei Maslyukov
 *         Date: 26.05.2006
 *         Time: 21:22:26
 */
public interface PortalUserMetadataDao {
    public UserMetadataItem getMetadata(String userLogin, Long siteId, String metadataName);

    public void setMetadataIntValue(String userLogin, Long siteId, String metadataName, Long intValue);

    public void setMetadataStringValue(String userLogin, Long siteId, String metadataName, String stringValue);

    public void setMetadataDateValue(String userLogin, Long siteId, String metadataName, Date dateValue);
}
