package org.riverock.forum.bean;

/**
 * @author SMaslyukov
 *         Date: 07.04.2005
 *         Time: 18:28:58
 *         $Id$
 */
public class ForumSmallBean {
    private String forumName = null;
    private String forumUrl = null;
    private long forumId;

    public String getForumName() {
        return forumName;
    }

    public void setForumName(String forumName) {
        this.forumName = forumName;
    }

    public String getForumUrl() {
        return forumUrl;
    }

    public void setForumUrl(String forumUrl) {
        this.forumUrl = forumUrl;
    }

    public long getForumId() {
        return forumId;
    }

    public void setForumId(long forumId) {
        this.forumId = forumId;
    }
}
