package org.riverock.forum.bean;

import java.io.Serializable;
import java.util.Date;

import org.riverock.common.tools.StringTools;
import org.riverock.interfaces.portal.bean.User;

/**
 * @author Sergei Maslyukov
 *         Date: 29.05.2006
 *         Time: 15:47:59
 */
public class PortalUserBean implements org.riverock.interfaces.portal.bean.User, Serializable {
    private static final long serialVersionUID = 2057005507L;

    private Long userId = null;

    private Long companyId = null;
    private String companyName = null;
    private String firstName = null;
    private String middleName = null;
    private String lastName = null;

    private Date createdDate = null;
    private Date closedDate = null;
    private String address = null;
    private String phone = null;
    private String email = null;

    public PortalUserBean() {
    }

    public PortalUserBean(User beanPortal) {
        this.userId = beanPortal.getUserId();
        this.companyId = beanPortal.getCompanyId();
        this.firstName = beanPortal.getFirstName();
        this.middleName = beanPortal.getMiddleName();
        this.lastName = beanPortal.getLastName();
        this.createdDate = beanPortal.getCreatedDate();
        this.closedDate = beanPortal.getDeletedDate();
        this.address = beanPortal.getAddress();
        this.phone = beanPortal.getPhone();
        this.email = beanPortal.getEmail();
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName( String companyName ) {
        this.companyName = companyName;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId( Long companyId ) {
        this.companyId = companyId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName( String firstName ) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName( String middleName ) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName( String lastName ) {
        this.lastName = lastName;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate( Date createdDate ) {
        this.createdDate = createdDate;
    }

    public Date getDeletedDate() {
        return closedDate;
    }

    public void setDeletedDate( Date closedDate ) {
        this.closedDate = closedDate;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress( String address ) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone( String phone ) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail( String email ) {
        this.email = email;
    }

    public String getName() {
        return StringTools.getUserName( firstName, middleName, lastName );
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId( Long id ) {
        this.userId = id;
    }

    public boolean equals( User portalUserBean ) {
        if( portalUserBean == null || portalUserBean.getUserId()==null || userId ==null ) {
            return false;
        }
        return portalUserBean.getUserId().equals( userId );
    }

    public String toString() {
        return "[name:" + getName() + ",userId:" + userId + "]";
    }
}