package org.riverock.portlet.register.bean;

/**
 * @author SMaslyukov
 *         Date: 22.05.2005
 *         Time: 15:21:38
 *         $Id$
 */
public class GenericBean {
    private String remoteAddr = null;
    private String userAgent = null;
    private String whereWeAre = null;
    private String loginUrl;
    private String logoutUrl;
    private String registerUrl;
    private String membersUrl;
    private String helpUrl;
    private StringBuilder baseModuleUrl;

    public String getRemoteAddr() {
        return remoteAddr;
    }

    public void setRemoteAddr(String remoteAddr) {
        this.remoteAddr = remoteAddr;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getWhereWeAre() {
        return whereWeAre;
    }

    public void setWhereWeAre(String whereWeAre) {
        this.whereWeAre = whereWeAre;
    }

    public void setLoginUrl(String loginUrl) {
        this.loginUrl = loginUrl;
    }

    public void setLogoutUrl(String logoutUrl) {
        this.logoutUrl = logoutUrl;
    }

    public void setRegisterUrl(String registerUrl) {
        this.registerUrl = registerUrl;
    }

    public void setMembersUrl(String membersUrl) {
        this.membersUrl = membersUrl;
    }

    public String getLoginUrl() {
        return loginUrl;
    }

    public String getLogoutUrl() {
        return logoutUrl;
    }

    public String getRegisterUrl() {
        return registerUrl;
    }

    public String getMembersUrl() {
        return membersUrl;
    }

    public String getHelpUrl() {
        return helpUrl;
    }

    public void setHelpUrl(String helpUrl) {
        this.helpUrl = helpUrl;
    }

    public void setBaseModuleUrl(StringBuilder baseModuleUrl) {
        this.baseModuleUrl = baseModuleUrl;
    }

    public StringBuilder getBaseModuleUrl() {
        return baseModuleUrl;
    }
}
