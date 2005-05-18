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
