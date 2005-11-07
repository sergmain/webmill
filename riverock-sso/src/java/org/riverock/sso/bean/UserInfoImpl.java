package org.riverock.sso.bean;

import org.riverock.interfaces.sso.a3.UserInfo;

/**
 * @author SergeMaslyukov
 *         Date: 05.11.2005
 *         Time: 15:54:55
 *         $Id$
 */
public class UserInfoImpl implements UserInfo {


    //--------------------------/
    //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field userId
     */
    private java.lang.Long userId;

    /**
     * Field idFirm
     */
    private java.lang.Long companyId;

    /**
     * Field _firstName
     */
    private java.lang.String _firstName;

    /**
     * Field _middleName
     */
    private java.lang.String _middleName;

    /**
     * Field _lastName
     */
    private java.lang.String _lastName;

    /**
     * Field _dateStartWork
     */
    private java.util.Date _dateStartWork;

    /**
     * Field _dateFire
     */
    private java.util.Date _dateFire;

    /**
     * Field _address
     */
    private java.lang.String _address;

    /**
     * Field _telephone
     */
    private java.lang.String _telephone;

    /**
     * Field _dateBindProff
     */
    private java.util.Date _dateBindProff;

    /**
     * Field _homeTelephone
     */
    private java.lang.String _homeTelephone;

    /**
     * Field _email
     */
    private java.lang.String _email;

    /**
     * Field _discount
     */
    private java.lang.Double _discount;


    //-----------/
    //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'address'.
     *
     * @return the value of field 'address'.
     */
    public java.lang.String getAddress() {
        return this._address;
    }

    /**
     * Returns the value of field 'dateBindProff'.
     *
     * @return the value of field 'dateBindProff'.
     */
    public java.util.Date getDateBindProff() {
        return this._dateBindProff;
    } //-- java.util.Date getDateBindProff()

    /**
     * Returns the value of field 'dateFire'.
     *
     * @return the value of field 'dateFire'.
     */
    public java.util.Date getDateFire() {
        return this._dateFire;
    } //-- java.util.Date getDateFire()

    /**
     * Returns the value of field 'dateStartWork'.
     *
     * @return the value of field 'dateStartWork'.
     */
    public java.util.Date getDateStartWork() {
        return this._dateStartWork;
    } //-- java.util.Date getDateStartWork()

    /**
     * Returns the value of field 'discount'.
     *
     * @return the value of field 'discount'.
     */
    public java.lang.Double getDiscount() {
        return this._discount;
    } //-- java.lang.Double getDiscount()

    /**
     * Returns the value of field 'email'.
     *
     * @return the value of field 'email'.
     */
    public java.lang.String getEmail() {
        return this._email;
    } //-- java.lang.String getEmail()

    /**
     * Returns the value of field 'firstName'.
     *
     * @return the value of field 'firstName'.
     */
    public java.lang.String getFirstName() {
        return this._firstName;
    } //-- java.lang.String getFirstName()

    /**
     * Returns the value of field 'homeTelephone'.
     *
     * @return the value of field 'homeTelephone'.
     */
    public java.lang.String getHomeTelephone() {
        return this._homeTelephone;
    } //-- java.lang.String getHomeTelephone()

    /**
     * Returns the value of field 'idFirm'.
     *
     * @return the value of field 'idFirm'.
     */
    public java.lang.Long getCompanyId() {
        return this.companyId;
    } //-- java.lang.Long getIdFirm()

    public java.lang.Long getUserId() {
        return this.userId;
    }

    /**
     * Returns the value of field 'lastName'.
     *
     * @return the value of field 'lastName'.
     */
    public java.lang.String getLastName() {
        return this._lastName;
    } //-- java.lang.String getLastName()

    /**
     * Returns the value of field 'middleName'.
     *
     * @return the value of field 'middleName'.
     */
    public java.lang.String getMiddleName() {
        return this._middleName;
    } //-- java.lang.String getMiddleName()

    /**
     * Returns the value of field 'telephone'.
     *
     * @return the value of field 'telephone'.
     */
    public java.lang.String getTelephone() {
        return this._telephone;
    } //-- java.lang.String getTelephone()

    /**
     * Sets the value of field 'address'.
     *
     * @param address the value of field 'address'.
     */
    public void setAddress(java.lang.String address) {
        this._address = address;
    } //-- void setAddress(java.lang.String)

    /**
     * Sets the value of field 'dateBindProff'.
     *
     * @param dateBindProff the value of field 'dateBindProff'.
     */
    public void setDateBindProff(java.util.Date dateBindProff) {
        this._dateBindProff = dateBindProff;
    } //-- void setDateBindProff(java.util.Date)

    /**
     * Sets the value of field 'dateFire'.
     *
     * @param dateFire the value of field 'dateFire'.
     */
    public void setDateFire(java.util.Date dateFire) {
        this._dateFire = dateFire;
    } //-- void setDateFire(java.util.Date)

    /**
     * Sets the value of field 'dateStartWork'.
     *
     * @param dateStartWork the value of field 'dateStartWork'.
     */
    public void setDateStartWork(java.util.Date dateStartWork) {
        this._dateStartWork = dateStartWork;
    } //-- void setDateStartWork(java.util.Date)

    /**
     * Sets the value of field 'discount'.
     *
     * @param discount the value of field 'discount'.
     */
    public void setDiscount(java.lang.Double discount) {
        this._discount = discount;
    } //-- void setDiscount(java.lang.Double)

    /**
     * Sets the value of field 'email'.
     *
     * @param email the value of field 'email'.
     */
    public void setEmail(java.lang.String email) {
        this._email = email;
    } //-- void setEmail(java.lang.String)

    /**
     * Sets the value of field 'firstName'.
     *
     * @param firstName the value of field 'firstName'.
     */
    public void setFirstName(java.lang.String firstName) {
        this._firstName = firstName;
    } //-- void setFirstName(java.lang.String)

    /**
     * Sets the value of field 'homeTelephone'.
     *
     * @param homeTelephone the value of field 'homeTelephone'.
     */
    public void setHomeTelephone(java.lang.String homeTelephone) {
        this._homeTelephone = homeTelephone;
    } //-- void setHomeTelephone(java.lang.String)

    /**
     * Sets the value of field 'idFirm'.
     *
     * @param companyId the value of field 'idFirm'.
     */
    public void setCompanyId(java.lang.Long companyId) {
        this.companyId = companyId;
    } //-- void setIdFirm(java.lang.Long)

    public void setUserId(java.lang.Long userId) {
        this.userId = userId;
    }

    /**
     * Sets the value of field 'lastName'.
     *
     * @param lastName the value of field 'lastName'.
     */
    public void setLastName(java.lang.String lastName) {
        this._lastName = lastName;
    } //-- void setLastName(java.lang.String)

    /**
     * Sets the value of field 'middleName'.
     *
     * @param middleName the value of field 'middleName'.
     */
    public void setMiddleName(java.lang.String middleName) {
        this._middleName = middleName;
    }

    /**
     * Sets the value of field 'telephone'.
     *
     * @param telephone the value of field 'telephone'.
     */
    public void setTelephone(java.lang.String telephone) {
        this._telephone = telephone;
    }
}
