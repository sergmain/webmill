/*
 * org.riverock.forum - Forum portlet
 *
 * Copyright (C) 2004, Riverock Software, All Rights Reserved.
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
 *
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