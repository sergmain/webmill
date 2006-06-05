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

public class ForumConcreteBean implements Serializable {

    private java.util.Collection topics;
    private String f_name;
    private String f_info;
    private String moderatorName;   // u_name
    private String lastPosterName;   // u_name2
    private List forums;
    private int count;
    private int f_id;
    private int f_messages;
    private int f_topics;
    private int moderatorId;   // f_u_id
    private int lastPorterId;  // f_u_id2
    private int topicsPerPage; // range
    private int start;
    private String keyword;
    private java.util.Date f_lasttime;
    private boolean isDeleted = false;

    private String forumUrl = null;

    private String urlToModeratorInfo = null;
    private boolean isExists = true;
    private String urlToPostThread = null;

    private String urlToPrevPage = null;
    private String urlToNextPage = null;

    private int messagesPerPage;
    private int countPages;

    public void reset() {
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getStart() {
        return start;
    }

    public void setTopicsPerPage(int topicsPerPage) {
        this.topicsPerPage = topicsPerPage;
    }

    public int getTopicsPerPage() {
        return topicsPerPage;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getCount() {
        return count;
    }

    public void setTopics(java.util.Collection topics) {
        this.topics = topics;
    }

    public java.util.Collection getTopics() {
        return topics;
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

    public void setModeratorId(int moderatorId) {
        this.moderatorId = moderatorId;
    }

    public int getModeratorId() {
        return moderatorId;
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

    public void setF_messages(int f_messages) {
        this.f_messages = f_messages;
    }

    public int getF_messages() {
        return f_messages;
    }

    public void setForums(List forums) {
        this.forums = forums;
    }

    public List getForums() {
        return forums;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getKeyword() {
        return keyword;
    }

    public String getUrlToModeratorInfo() {
        return urlToModeratorInfo;
    }

    public void setUrlToModeratorInfo(String urlToModeratorInfo) {
        this.urlToModeratorInfo = urlToModeratorInfo;
    }

    public boolean isExists() {
        return isExists;
    }

    public void setExists(boolean exists) {
        isExists = exists;
    }

    public String getUrlToPostThread() {
        return urlToPostThread;
    }

    public void setUrlToPostThread(String urlToPostThread) {
        this.urlToPostThread = urlToPostThread;
    }

    public String getUrlToPrevPage() {
        return urlToPrevPage;
    }

    public void setUrlToPrevPage(String urlToPrevPage) {
        // forum.do?f_id=<%=forumBean.getF_id()%>&start=<%=start-topicsPerPage%>&keyword=<%=keyword%>
        this.urlToPrevPage = urlToPrevPage;
    }

    public String getUrlToNextPage() {
        return urlToNextPage;
    }

    public void setUrlToNextPage(String urlToNextPage) {
//        <%if(start+topicsPerPage<=count){%><A HREF="forum.do?f_id=<%=forumBean.getF_id()%>&start=<%=start+topicsPerPage%>&keyword=<%=keyword%>">NEXT</A><%}else{%>NEXT<%}%>
        this.urlToNextPage = urlToNextPage;
    }

    public String getLastPosterName() {
        return lastPosterName;
    }

    public void setLastPosterName(String lastPosterName) {
        this.lastPosterName = lastPosterName;
    }

    public int getLastPorterId() {
        return lastPorterId;
    }

    public void setLastPorterId(int lastPorterId) {
        this.lastPorterId = lastPorterId;
    }

    public java.util.Date getF_lasttime() {
        return f_lasttime;
    }

    public void setF_lasttime(java.util.Date f_lasttime) {
        this.f_lasttime = f_lasttime;
    }

    public String getForumUrl() {
        return forumUrl;
    }

    public void setForumUrl(String forumUrl) {
        this.forumUrl = forumUrl;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public int getMessagesPerPage() {
        return messagesPerPage;
    }

    public void setMessagesPerPage(int messagesPerPage) {
        this.messagesPerPage = messagesPerPage;
    }

    public int getCountPages() {
        return countPages;
    }

    public void setCountPages(int countPages) {
        this.countPages = countPages;
    }
}