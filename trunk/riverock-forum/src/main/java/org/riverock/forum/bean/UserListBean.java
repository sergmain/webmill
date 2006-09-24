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