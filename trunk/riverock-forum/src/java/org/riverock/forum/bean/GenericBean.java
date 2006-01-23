package org.riverock.forum.bean;

/**
 * @author SMaslyukov
 *         Date: 07.04.2005
 *         Time: 17:39:39
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
    private StringBuilder forumHomeUrl;
    private String forumName = null;
    private Integer forumId;

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

    public void setForumHomeUrl(StringBuilder forumHomeUrl) {
        this.forumHomeUrl = forumHomeUrl;
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

    public StringBuilder getForumHomeUrl() {
        return forumHomeUrl;
    }

    public String getForumName() {
        return forumName;
    }

    public void setForumName(String forumName) {
        this.forumName = forumName;
    }

    public String getHelpUrl() {
        return helpUrl;
    }

    public void setHelpUrl(String helpUrl) {
        this.helpUrl = helpUrl;
    }

    public void setForumId(Integer forumId) {
        this.forumId = forumId;
    }

    public Integer getForumId() {
        return this.forumId;
    }
}
