package org.riverock.portlet.auth.bean;

import java.util.List;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * @author SMaslyukov
 *         Date: 22.05.2005
 *         Time: 16:53:44
 *         $Id$
 */
public class BindUserListBean {
    private List users = new ArrayList(); // list of BindUserBean

    private boolean isCompany;
    private boolean isGroupCompany;
    private boolean isHolding;

    private String addButton = null;
    private String changeButton = null;
    private String deleteButton = null;

    private ResourceBundle bundle = null;

    public List getUsers() {
        return users;
    }

    public void setUsers(List users) {
        this.users = users;
    }

    public boolean isCompany() {
        return isCompany;
    }

    public void setCompany(boolean company) {
        isCompany = company;
    }

    public boolean isGroupCompany() {
        return isGroupCompany;
    }

    public void setGroupCompany(boolean groupCompany) {
        isGroupCompany = groupCompany;
    }

    public boolean isHolding() {
        return isHolding;
    }

    public void setHolding(boolean holding) {
        isHolding = holding;
    }

    public String getAddButton() {
        return addButton;
    }

    public void setAddButton(String addButton) {
        this.addButton = addButton;
    }

    public String getChangeButton() {
        return changeButton;
    }

    public void setChangeButton(String changeButton) {
        this.changeButton = changeButton;
    }

    public String getDeleteButton() {
        return deleteButton;
    }

    public void setDeleteButton(String deleteButton) {
        this.deleteButton = deleteButton;
    }

    public ResourceBundle getBundle() {
        return bundle;
    }

    public void setBundle( ResourceBundle bundle ) {
        this.bundle = bundle;
    }
}
