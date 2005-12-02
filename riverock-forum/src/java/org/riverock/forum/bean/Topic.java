package org.riverock.forum.bean;

import java.io.Serializable;

import org.riverock.common.tools.StringTools;

public class Topic implements Serializable {
    private String t_name;
    private String u_name;
    private String u_name2;
    private String t_views;
    private int t_order;
    private int t_replies;
    private int t_locked;
    private int t_id;
    private String urlToPosterInfo = null;
    private String urlToLastPosterInfo = null;

    private java.util.Date t_lasttime;
    private int t_u_id;
    private int t_u_id2;
    private int t_iconid;

    private int countPages = 0;


    public void setT_id(int t_id) {
        this.t_id = t_id;
    }

    public int getT_id() {
        return t_id;
    }

    public void setT_name(String t_name) {
//        this.t_name = StringTools.encodeXml(t_name);
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

    public void setU_name(String u_name) {
//        this.u_name = StringTools.encodeXml(u_name);
        this.u_name = u_name;
    }

    public String getU_name() {
        return u_name;
    }

    public void setU_name2(String u_name2) {
//        this.u_name2 = StringTools.encodeXml(u_name2);
        this.u_name2 = u_name2;
    }

    public String getU_name2() {
        return u_name2;
    }

    public void setT_lasttime(java.util.Date t_lasttime) {
        this.t_lasttime = t_lasttime;
    }

    public java.util.Date getT_lasttime() {
        return t_lasttime;
    }

    public void setT_u_id(int t_u_id) {
        this.t_u_id = t_u_id;
    }

    public int getT_u_id() {
        return t_u_id;
    }

    public void setT_u_id2(int t_u_id2) {
        this.t_u_id2 = t_u_id2;
    }

    public int getT_u_id2() {
        return t_u_id2;
    }

    public void setT_views(String t_views) {
        this.t_views = t_views;
    }

    public String getT_views() {
        return t_views;
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

    public void setT_iconid(int t_iconid) {
        this.t_iconid = t_iconid;
    }

    public int getT_iconid() {
        return t_iconid;
    }

    public String getUrlToPosterInfo() {
        return urlToPosterInfo;
    }

    public void setUrlToPosterInfo(String urlToPosterInfo) {
        this.urlToPosterInfo = urlToPosterInfo;
    }

    public String getUrlToLastPosterInfo() {
        return urlToLastPosterInfo;
    }

    public void setUrlToLastPosterInfo(String urlToLastPosterInfo) {
        this.urlToLastPosterInfo = urlToLastPosterInfo;
    }

    public int getCountPages() {
        return countPages;
    }

    public void setCountPages(int countPages) {
        this.countPages = countPages;
    }
}