package org.riverock.forum.bean;

import java.util.Collection;
import java.util.LinkedList;

/**
 * @author SMaslyukov
 *         Date: 19.04.2005
 *         Time: 15:35:35
 *         $Id$
 */
public class ForumCategoryBean {
    private boolean isUseLocale = false;
    private String categoryName = null;
    private Collection forums = new LinkedList();
    private Integer forumCategoryId = null;
    private boolean isDeleted = false;

    public boolean isUseLocale() {
        return isUseLocale;
    }

    public void setUseLocale(boolean useLocale) {
        isUseLocale = useLocale;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Collection getForums() {
        return forums;
    }

    public void setForums(Collection forums) {
        this.forums = forums;
    }

    public Integer getForumCategoryId() {
        return forumCategoryId;
    }

    public void setForumCategoryId(Integer forumCategoryId) {
        this.forumCategoryId = forumCategoryId;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }
}
