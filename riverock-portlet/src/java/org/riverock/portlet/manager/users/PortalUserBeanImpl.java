package org.riverock.portlet.manager.users;

import java.io.Serializable;
import java.util.Date;

import org.riverock.common.tools.StringTools;

/**
 * @author SergeMaslyukov
 *         Date: 26.02.2006
 *         Time: 15:55:44
 *         $Id$
 */
public class PortalUserBeanImpl implements Serializable, PortalUserBean {
    private static final long serialVersionUID = 2057005507L;

    private Long id = null;

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

    public PortalUserBeanImpl() {
    }

    public PortalUserBeanImpl(PortalUserBean beanPortal) {
        this.id = beanPortal.getId();
        this.companyId = beanPortal.getCompanyId();
        this.firstName = beanPortal.getFirstName();
        this.middleName = beanPortal.getMiddleName();
        this.lastName = beanPortal.getLastName();
        this.createdDate = beanPortal.getCreatedDate();
        this.closedDate = beanPortal.getClosedDate();
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

    public Date getClosedDate() {
        return closedDate;
    }

    public void setClosedDate( Date closedDate ) {
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

    public Long getId() {
        return id;
    }

    public void setId( Long id ) {
        this.id = id;
    }

    public boolean equals( PortalUserBean portalUserBean ) {
        if( portalUserBean == null || portalUserBean.getId()==null || id==null ) {
            return false;
        }
        return portalUserBean.getId().equals( id );
    }

    public String toString() {
        return "[name:" + getName() + ",id:" + id + "]";
    }
}
