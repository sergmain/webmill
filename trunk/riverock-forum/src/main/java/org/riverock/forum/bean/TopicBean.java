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
import java.util.List;

public class TopicBean implements Serializable {
    private java.util.Collection messages;
    private String f_name;
    private String f_info;
    private String moderatorName;  // moderatorName
    private String t_name;
    private String t_views;
    private List forums;
    private int t_locked;
    private int t_order;
    private int t_replies;
    private int f_id;
    private int f_messages;
    private int f_topics;
    private int f_u_id;
    private Long t_id;
    private int start;
    private int range;
    private int count;
    private String postTopicUrl = null;
    private String replyTopicUrl = null;

    private int messagesPerPage;
    private int countPages;

    public void reset() {
    }

    public void setMessages(java.util.Collection messages) {
        this.messages = messages;
    }

    public java.util.Collection getMessages() {
        return messages;
    }

    public void setF_id(int f_id) {
        this.f_id = f_id;
    }

    public int getF_id() {
        return f_id;
    }

    public void setF_name(String f_name) {
        this.f_name = f_name;
    }

    public String getF_name() {
        return f_name;
    }

    public void setF_info(String f_info) {
        this.f_info = f_info;
    }

    public String getF_info() {
        return f_info;
    }

    public void setF_u_id(int f_u_id) {
        this.f_u_id = f_u_id;
    }

    public int getF_u_id() {
        return f_u_id;
    }

    public void setF_topics(int f_topics) {
        this.f_topics = f_topics;
    }

    public int getF_topics() {
        return f_topics;
    }

    public void setModeratorName(String moderatorName) {
        this.moderatorName = moderatorName;
    }

    public String getModeratorName() {
        return moderatorName;
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

    public void setT_replies(int t_replies) {
        this.t_replies = t_replies;
    }

    public int getT_replies() {
        return t_replies;
    }

    public void setF_messages(int f_messages) {
        this.f_messages = f_messages;
    }

    public int getF_messages() {
        return f_messages;
    }

    public void setT_views(String t_views) {
        this.t_views = t_views;
    }

    public String getT_views() {
        return t_views;
    }

    public void setForums(List forums) {
        this.forums = forums;
    }

    public List getForums() {
        return forums;
    }

    public void setT_order(int t_order) {
        this.t_order = t_order;
    }

    public int getT_order() {
        return t_order;
    }

    public void setT_locked(int t_locked) {
        this.t_locked = t_locked;
    }

    public int getT_locked() {
        return t_locked;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getStart() {
        return start;
    }

    public void setRange(int range) {
        this.range = range;
    }

    public int getRange() {
        return range;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getCount() {
        return count;
    }

    public String getPostTopicUrl() {
        return postTopicUrl;
    }

    public void setPostTopicUrl(String postTopicUrl) {
        this.postTopicUrl = postTopicUrl;
    }

    public String getReplyTopicUrl() {
        return replyTopicUrl;
    }

    public void setReplyTopicUrl(String replyTopicUrl) {
        this.replyTopicUrl = replyTopicUrl;
    }

    public int getCountPages() {
        return countPages;
    }

    public void setCountPages(int countPages) {
        this.countPages = countPages;
    }

    public int getMessagesPerPage() {
        return messagesPerPage;
    }

    public void setMessagesPerPage(int messagesPerPage) {
        this.messagesPerPage = messagesPerPage;
    }
}