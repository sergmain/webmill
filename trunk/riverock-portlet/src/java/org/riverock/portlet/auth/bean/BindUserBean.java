package org.riverock.portlet.auth.bean;

/**
 * @author SMaslyukov
 *         Date: 22.05.2005
 *         Time: 16:35:58
 *         $Id$
 */
public class BindUserBean {
    private Long authUserId;
    private String companyName = null;
    private String userName = null;

    private boolean isCompany;
    private boolean isGroupCompany;
    private boolean isHolding;

    private String companyLevel = null;
    private String groupCompanyLevel = null;
    private String holdingLevel = null;


    private String userLogin;


    public Long getAuthUserId() {
        return authUserId;
    }

    public void setAuthUserId(Long authUserId) {
        this.authUserId = authUserId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public boolean isCompany() {
        return isCompany;
    }

    public void setCompany(boolean company) {
        isCompany = company;
    }

    public boolean isGroupCompany() {
        return isGroupCompany;
    }

    public void setGroupCompany(boolean groupCompany) {
        isGroupCompany = groupCompany;
    }

    public boolean isHolding() {
        return isHolding;
    }

    public void setHolding(boolean holding) {
        isHolding = holding;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public String getCompanyLevel() {
        return companyLevel;
    }

    public void setCompanyLevel(String companyLevel) {
        this.companyLevel = companyLevel;
    }

    public String getGroupCompanyLevel() {
        return groupCompanyLevel;
    }

    public void setGroupCompanyLevel(String groupCompanyLevel) {
        this.groupCompanyLevel = groupCompanyLevel;
    }

    public String getHoldingLevel() {
        return holdingLevel;
    }

    public void setHoldingLevel(String holdingLevel) {
        this.holdingLevel = holdingLevel;
    }
}
