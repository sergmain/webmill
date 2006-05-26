package org.riverock.webmill.portal.bean;

import java.util.Date;

import org.riverock.interfaces.portal.user.UserMetadataItem;

/**
 * @author Sergei Maslyukov
 *         Date: 26.05.2006
 *         Time: 20:21:04
 */
public class UserMetadataItemBean implements UserMetadataItem {
    private Long metadataItemId = null;
    private Long siteId = null;
    private Long userId = null;
    private String metadataName = null;
    private Long intValue = null;
    private Date dateValue = null;
    private String stringValue = null;

    public Long getMetadataItemId() {
        return metadataItemId;
    }

    public void setMetadataItemId(Long metadataItemId) {
        this.metadataItemId = metadataItemId;
    }

    public Long getSiteId() {
        return siteId;
    }

    public void setSiteId(Long siteId) {
        this.siteId = siteId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getMetadataName() {
        return metadataName;
    }

    public void setMetadataName(String metadataName) {
        this.metadataName = metadataName;
    }

    public Long getIntValue() {
        return intValue;
    }

    public void setIntValue(Long intValue) {
        this.intValue = intValue;
    }

    public Date getDateValue() {
        return dateValue;
    }

    public void setDateValue(Date dateValue) {
        this.dateValue = dateValue;
    }

    public String getStringValue() {
        return stringValue;
    }

    public void setStringValue(String stringValue) {
        this.stringValue = stringValue;
    }
}
