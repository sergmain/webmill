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

import java.util.Collection;

/**
 * @author SMaslyukov
 *         Date: 19.04.2005
 *         Time: 15:37:48
 *         $Id$
 */
public class ForumBean {
    private String forumName = null;
    private boolean isUseLocale = false;
    private Collection forumCategories = null;
    private String userSum;
    private int messageSum;
    private int topicSum;
    private Long forumId;
    private boolean isDeleted = false;

    public String getForumName() {
        return forumName;
    }

    public void setForumName(String forumName) {
        this.forumName = forumName;
    }

    public Collection getForumCategories() {
        return forumCategories;
    }

    public void setForumCategories(Collection forumCategories) {
        this.forumCategories = forumCategories;
    }

    public boolean isUseLocale() {
        return isUseLocale;
    }

    public void setUseLocale(boolean useLocale) {
        isUseLocale = useLocale;
    }

    public void setTopicSum(int topicSum) {
        this.topicSum = topicSum;
    }

    public int getTopicSum() {
        return topicSum;
    }

    public void setMessageSum(int messageSum) {
        this.messageSum = messageSum;
    }

    public int getMessageSum() {
        return messageSum;
    }

    public void setUserSum(String userSum) {
        this.userSum = userSum;
    }

    public String getUserSum() {
        return userSum;
    }

    public void setForumId(Long forumId) {
        this.forumId = forumId;
    }

    public Long getForumId() {
        return forumId;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }
}
