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

public class UserListBean implements Serializable{

  private java.util.Collection users;
  private int count;
  private int range;
  private int start;
  private String keyword;
  public void reset(){}
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
  public void setUsers(java.util.Collection users) {
    this.users = users;
  }
  public java.util.Collection getUsers() {
    return users;
  }
  public void setKeyword(String keyword) {
    this.keyword = keyword;
  }
  public String getKeyword() {
    return keyword;
  }
}