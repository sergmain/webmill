/*
 * org.riverock.generic - Database connectivity classes, part of Webmill portal
 * For more information about Webmill portal, please visit project site
 * http://webmill.askmore.info
 *
 * Copyright (C) 2000-2006, Riverock Software, All Rights Reserved.
 *
 * Riverock - The Open-source Java Development Community
 * http://www.riverock.org
 *
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
