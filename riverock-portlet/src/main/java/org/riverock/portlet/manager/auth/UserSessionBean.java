/*
 * org.riverock.portlet - Portlet Library
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
package org.riverock.portlet.manager.auth;

import java.io.Serializable;

import org.apache.myfaces.custom.tree2.TreeState;
import org.apache.myfaces.custom.tree2.TreeStateBase;

/**
 * @author SergeMaslyukov
 *         Date: 06.01.2006
 *         Time: 11:26:35
 *         $Id: UserSessionBean.java 1049 2006-11-14 15:56:05Z serg_main $
 */
public class UserSessionBean implements Serializable {

    private AuthUserExtendedInfoImpl userBean = null;
    private Long currentAuthUserId = null;
    private Long currentRoleId = null;

    private boolean isAdd = false;
    private boolean isEdit = false;
    private boolean isDelete = false;
    private TreeState treeState = null;

    public UserSessionBean() {
        treeState = new TreeStateBase();
        treeState.setTransient(true);
    }

    public TreeState getTreeState() {
        return treeState;
    }

    public void resetStatus() {
        isAdd = false;
        isEdit = false;
        isDelete = false;
    }

    public void setAdd(boolean isAdd) {
        this.isAdd = isAdd;
        if (isAdd) {
            isEdit = false;
            isDelete = false;
        }
    }

    public boolean getAdd() {
        return isAdd;
    }

    public void setEdit(boolean isEdit) {
        this.isEdit = isEdit;
        if (isEdit) {
            isAdd = false;
            isDelete = false;
        }
    }

    public boolean getEdit() {
        return isEdit;
    }

    public void setDelete(boolean isDelete) {
        this.isDelete = isDelete;
        if (isDelete) {
            isEdit = false;
            isAdd = false;
        }
    }

    public boolean getDelete() {
        return isDelete;
    }

    public void setUserBean(AuthUserExtendedInfoImpl userBean) {
        this.userBean = userBean;
    }

    public AuthUserExtendedInfoImpl getUserBean() {
        return userBean;
    }

    public Long getCurrentAuthUserId() {
        return currentAuthUserId;
    }

    public void setCurrentAuthUserId(Long id) {
        this.currentAuthUserId = id;
    }

    public Long getCurrentRoleId() {
        return currentRoleId;
    }

    public void setCurrentRoleId(Long id) {
        this.currentRoleId = id;
    }
}
