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