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