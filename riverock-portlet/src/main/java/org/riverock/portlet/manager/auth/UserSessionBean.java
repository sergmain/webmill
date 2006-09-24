/*
 * org.riverock.portlet - Portlet Library
 *
 * Copyright (C) 2006, Riverock Software, All Rights Reserved.
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
package org.riverock.portlet.manager.auth;

import java.io.Serializable;

import org.apache.myfaces.custom.tree2.TreeState;
import org.apache.myfaces.custom.tree2.TreeStateBase;

/**
 * @author SergeMaslyukov
 *         Date: 06.01.2006
 *         Time: 11:26:35
 *         $Id$
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
