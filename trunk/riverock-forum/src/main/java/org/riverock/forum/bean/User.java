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

public class User implements Serializable{
  private String u_name;
  private String u_email;
  private String u_address;
  private String u_sign;
  private int u_avatar_id;
  private int u_id;
  private java.util.Date u_lasttime;
  private int u_post;
  private java.util.Date u_regtime;
  private String r_name;

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
}