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

import java.util.Collection;
import java.util.LinkedList;

/**
 * @author SMaslyukov
 *         Date: 19.04.2005
 *         Time: 15:35:35
 *         $Id: ForumCategoryBean.java 1119 2006-12-02 22:35:13Z serg_main $
 */
public class ForumCategoryBean {
    private boolean isUseLocale = false;
    private String categoryName = null;
    private Collection forums = new LinkedList();
    private Long forumCategoryId = null;
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

    public Long getForumCategoryId() {
        return forumCategoryId;
    }

    public void setForumCategoryId(Long forumCategoryId) {
        this.forumCategoryId = forumCategoryId;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }
}
