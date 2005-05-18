package org.riverock.forum.bean;

import java.io.Serializable;

public class UserBean implements Serializable{
  private String u_name;
  private String u_email;
  private String u_address;
  private String u_sign;
  private String u_lastip;
  private int u_avatar_id;
  private String u_password;
  private int u_id;
  private java.util.Date u_lasttime;
  private int u_post;
  private java.util.Date u_regtime;

  public String getU_name() {
    return u_name;
  }
  public void setU_name(String u_name) {
    this.u_name = u_name;
  }
  public void setU_id(int u_id) {
    this.u_id = u_id;
  }
  public int getU_id() {
    return u_id;
  }
  public void setU_email(String u_email) {
    this.u_email = u_email;
  }
  public String getU_email() {
    return u_email;
  }
  public void setU_regtime(java.util.Date u_regtime) {
    this.u_regtime = u_regtime;
  }
  public java.util.Date getU_regtime() {
    return u_regtime;
  }
  public void setU_address(String u_address) {
    this.u_address = u_address;
  }
  public String getU_address() {
    return u_address;
  }
  public void setU_sign(String u_sign) {
    this.u_sign = u_sign;
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
  public void setU_lasttime(java.util.Date u_lasttime) {
    this.u_lasttime = u_lasttime;
  }
  public java.util.Date getU_lasttime() {
    return u_lasttime;
  }
  public void setU_lastip(String u_lastip) {
    this.u_lastip = u_lastip;
  }
  public String getU_lastip() {
    return u_lastip;
  }
  public void setU_avatar_id(int u_avatar_id) {
    this.u_avatar_id = u_avatar_id;
  }
  public int getU_avatar_id() {
    return u_avatar_id;
  }
  public void setU_password(String u_password) {
    this.u_password = u_password;
  }
  public String getU_password() {
    return u_password;
  }
}