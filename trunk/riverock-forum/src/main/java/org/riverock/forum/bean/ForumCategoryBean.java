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
