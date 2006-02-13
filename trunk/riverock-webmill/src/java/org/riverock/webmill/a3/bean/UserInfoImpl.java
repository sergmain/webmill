package org.riverock.webmill.a3.bean;

import java.io.Serializable;
import java.util.Date;

import org.riverock.interfaces.sso.a3.UserInfo;

/**
 * @author SergeMaslyukov
 *         Date: 31.01.2006
 *         Time: 16:32:21
 *         $Id$
 */
public class UserInfoImpl implements UserInfo, Serializable {
    private static final long serialVersionUID = 3043770851L;

    private java.lang.Long userId;
    private java.lang.Long companyId;
    private java.lang.String firstName;
    private java.lang.String middleName;
    private java.lang.String lastName;
    private java.util.Date dateStartWork;
    private java.util.Date dateFire;
    private java.lang.String address;
    private java.lang.String telephone;
    private java.util.Date dateBindProff;
    private java.lang.String homeTelephone;
    private java.lang.String email;
    private java.lang.Double discount;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Date getDateStartWork() {
        return dateStartWork;
    }

    public void setDateStartWork(Date dateStartWork) {
        this.dateStartWork = dateStartWork;
    }

    public Date getDateFire() {
        return dateFire;
    }

    public void setDateFire(Date dateFire) {
        this.dateFire = dateFire;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public Date getDateBindProff() {
        return dateBindProff;
    }

    public void setDateBindProff(Date dateBindProff) {
        this.dateBindProff = dateBindProff;
    }

    public String getHomeTelephone() {
        return homeTelephone;
    }

    public void setHomeTelephone(String homeTelephone) {
        this.homeTelephone = homeTelephone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }
}
