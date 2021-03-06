/*
 * org.riverock.forum - Forum portlet
 *
 * Copyright (C) 2006, Riverock Software, All Rights Reserved.
 *
 * Riverock - The Open-source Java Development Community
 * http://www.riverock.org
 *
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package org.riverock.forum.bean;

/**
 * @author SMaslyukov
 *         Date: 07.04.2005
 *         Time: 17:39:39
 *         $Id: GenericBean.java 1119 2006-12-02 22:35:13Z serg_main $
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
    private Long forumId;
    private String actionUrl;

    public String getActionUrl() {
        return actionUrl;
    }

    public void setActionUrl(String actionUrl) {
        this.actionUrl = actionUrl;
    }

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

    public void setForumId(Long forumId) {
        this.forumId = forumId;
    }

    public Long getForumId() {
        return this.forumId;
    }
}
