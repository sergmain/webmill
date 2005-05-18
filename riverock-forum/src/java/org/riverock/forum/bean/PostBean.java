package org.riverock.forum.bean;

import java.io.Serializable;

public class PostBean implements Serializable {
    private String f_name;
    private String t_name;
    private boolean reply;
    private int t_locked;
    private int f_id;
    private int t_id;
    private Long forumId = null;

    public void reset() {
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

    public void setT_id(int t_id) {
        this.t_id = t_id;
    }

    public int getT_id() {
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