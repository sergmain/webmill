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

import java.io.Serializable;

public class PostBean implements Serializable {
    private String f_name;
    private String t_name;
    private boolean reply;
    private int t_locked;
    private Long f_id;
    private Long t_id;
    private Long forumId = null;

    public void reset() {
    }

    public void setF_id(Long f_id) {
        this.f_id = f_id;
    }

    public Long getF_id() {
        return f_id;
    }

    public void setF_name(String f_name) {
        this.f_name = f_name;
    }

    public String getF_name() {
        return f_name;
    }

    public void setT_id(Long t_id) {
        this.t_id = t_id;
    }

    public Long getT_id() {
        return t_id;
    }

    public void setT_name(String t_name) {
        this.t_name = t_name;
    }

    public String getT_name() {
        return t_name;
    }

    public void setReply(boolean reply) {
        this.reply = reply;
    }

    public boolean isReply() {
        return reply;
    }

    public void setT_locked(int t_locked) {
        this.t_locked = t_locked;
    }

    public int getT_locked() {
        return t_locked;
    }

    public Long getForumId() {
        return forumId;
    }

    public void setForumId(Long forumId) {
        this.forumId = forumId;
    }
}