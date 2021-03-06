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

import java.io.Serializable;

import org.riverock.forum.util.ForumStringUtils;

public class Message implements Serializable {
    private String m_content;
    private String u_name;
    private String u_address;
    private String u_sign;
    private int u_avatar_id;
    private int m_id;
    private java.util.Date m_time;
    private int m_u_id;
    private int u_post;
    private java.util.Date u_regtime;
    private String r_name;
    private int m_iconid;
    private boolean isEdited = false;

    public void setM_id(int m_id) {
        this.m_id = m_id;
    }

    public int getM_id() {
        return m_id;
    }

    public void setM_content(String m_content) {
        this.m_content = ForumStringUtils.displayHtml(m_content);
    }

    public String getM_content() {
        return m_content;
    }

    public void setM_time(java.util.Date m_time) {
        this.m_time = m_time;
    }

    public java.util.Date getM_time() {
        return m_time;
    }

    public void setU_address(String u_address) {
        this.u_address = u_address;
    }

    public String getU_address() {
        return u_address;
    }

    public void setU_sign(String u_sign) {
        this.u_sign = ForumStringUtils.displayHtml(u_sign);
    }

    public String getU_sign() {
        return u_sign;
    }

    public void setU_post(int u_post) {
        this.u_post = u_post;
    }

    public int getU_post() {
        return u_post;
    }

    public void setU_name(String u_name) {
        this.u_name = u_name;
    }

    public String getU_name() {
        return u_name;
    }

    public void setM_u_id(int m_u_id) {
        this.m_u_id = m_u_id;
    }

    public int getM_u_id() {
        return m_u_id;
    }

    public void setU_regtime(java.util.Date u_regtime) {
        this.u_regtime = u_regtime;
    }

    public java.util.Date getU_regtime() {
        return u_regtime;
    }

    public void setU_avatar_id(int u_avatar_id) {
        this.u_avatar_id = u_avatar_id;
    }

    public int getU_avatar_id() {
        return u_avatar_id;
    }

    public void setR_name(String r_name) {
        this.r_name = r_name;
    }

    public String getR_name() {
        return r_name;
    }

    public void setM_iconid(int m_iconid) {
        this.m_iconid = m_iconid;
    }

    public int getM_iconid() {
        return m_iconid;
    }

    public boolean isEdited() {
        return isEdited;
    }

    public void setEdited(boolean edited) {
        isEdited = edited;
    }
}