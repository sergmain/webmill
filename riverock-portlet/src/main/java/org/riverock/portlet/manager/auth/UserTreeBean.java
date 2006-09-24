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

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.myfaces.custom.tree2.TreeModel;
import org.apache.myfaces.custom.tree2.TreeModelBase;
import org.apache.myfaces.custom.tree2.TreeNode;
import org.apache.myfaces.custom.tree2.TreeNodeBase;

/**
 * @author SergeMaslyukov
 *         Date: 02.01.2006
 *         Time: 9:26:35
 *         $Id$
 */
@SuppressWarnings({"unchecked"})
public class UserTreeBean implements Serializable {
    private final static Logger log = Logger.getLogger(UserTreeBean.class);
    private static final long serialVersionUID = 2043005500L;

    private DataProvider dataProvider = null;
    private UserSessionBean userSessionBean = null;

    public UserTreeBean() {
    }

    public UserSessionBean getUserSessionBean() {
        return userSessionBean;
    }

    public void setUserSessionBean(UserSessionBean userSessionBean) {
        this.userSessionBean = userSessionBean;
    }

    public DataProvider getDataProvider() {
        return dataProvider;
    }

    public void setDataProvider(DataProvider dataProvider) {
        this.dataProvider = dataProvider;
    }

    public TreeModel getUserTree() {
        log.info("Invoke getUserTree()");

        TreeNode rootNode = getPrepareUserTree();
        TreeModel treeModel = new TreeModelBase(rootNode);
        treeModel.setTreeState(userSessionBean.getTreeState());

        return treeModel;
    }

    private TreeNode getPrepareUserTree() {

        log.info("Invoke getPrepareUserTree()");

        TreeNode treeData = new TreeNodeBase("foo-folder", "Company list", false);
        for (CompanyBean companyBean : dataProvider.getCompanyBeans()) {
            String companyName = companyBean.getCompanyName();
            if (StringUtils.isBlank(companyName)) {
                companyName = "<empty company name>";
            }
            TreeNodeBase companyNode = new TreeNodeBase("company", companyName, companyBean.getCompanyId().toString(), false);
            for (AuthUserExtendedInfoImpl authUserExtendedInfoImpl : companyBean.getUserBeans()) {
                companyNode.getChildren().add(
                    new TreeNodeBase(
                        "user",
                        authUserExtendedInfoImpl.getUserName() + " ( " +
                            authUserExtendedInfoImpl.getAuthInfo().getUserLogin() + " )",
                        authUserExtendedInfoImpl.getAuthInfo().getAuthUserId().toString(),
                        true)
                );
            }

            treeData.getChildren().add(companyNode);
        }
        return treeData;
    }
}