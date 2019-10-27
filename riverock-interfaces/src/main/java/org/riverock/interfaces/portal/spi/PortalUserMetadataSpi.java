package org.riverock.interfaces.portal.spi;

import java.util.Date;

import org.riverock.interfaces.portal.dao.PortalUserMetadataDao;
import org.riverock.interfaces.portal.user.UserMetadataItem;

/**
 * User: SergeMaslyukov
 * Date: 02.09.2007
 * Time: 12:54:10
 * $Id$
 */
public interface PortalUserMetadataSpi extends PortalUserMetadataDao {
    public UserMetadataItem getMetadata(String userLogin, Long siteId, String metadataName);

    public void setMetadataIntValue(String userLogin, Long siteId, String metadataName, Long intValue);

    public void setMetadataStringValue(String userLogin, Long siteId, String metadataName, String stringValue);

    public void setMetadataDateValue(String userLogin, Long siteId, String metadataName, Date dateValue);
}
