package org.riverock.interfaces.portal.user;

/**
 * @author Sergei Maslyukov
 *         Date: 26.05.2006
 *         Time: 20:09:47
 */
public interface UserMetadataItem {
    public Long getMetadataItemId();
    public Long getSiteId();
    public Long getUserId();
    public String getMetadataName();

    public Long getIntValue();
    public java.util.Date getDateValue();
    public String getStringValue();
}
