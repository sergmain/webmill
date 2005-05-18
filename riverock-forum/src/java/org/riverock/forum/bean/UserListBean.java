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