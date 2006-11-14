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