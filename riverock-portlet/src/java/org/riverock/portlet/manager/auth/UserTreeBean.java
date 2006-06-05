/*
 * org.riverock.portlet -- Portlet Library
 *
 * Copyright (C) 2004, Riverock Software, All Rights Reserved.
 *
 * Riverock -- The Open-source Java Development Community
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
package org.riverock.portlet.manager.auth;

import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.validator.ValidatorException;

import org.apache.log4j.Logger;
import org.apache.myfaces.custom.tree2.HtmlTree;
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
public class UserTreeBean implements Serializable {
    private final static Logger log = Logger.getLogger(UserTreeBean.class);
    private static final long serialVersionUID = 2043005500L;

    private DataProvider dataProvider = null;

    private HtmlTree _tree;
    private String _nodePath;

    public UserTreeBean() {
    }

    public DataProvider getDataProvider() {
        return dataProvider;
    }

    public void setDataProvider(DataProvider dataProvider) {
        this.dataProvider = dataProvider;
    }

    private TreeNode treeNode = null;

    public TreeNode getUserTree() {


        log.info("Invoke getUserTree()");

        TreeNode treeData = new TreeNodeBase("foo-folder", "Company list", false);
        for (CompanyBean companyBean : dataProvider.getCompanyBeans()) {
            TreeNodeBase companyNode = new TreeNodeBase("company", companyBean.getCompanyName(), companyBean.getCompanyId().toString(), false);
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
        treeNode = treeData;

//      }
        return treeNode;
    }

    public TreeModel getExpandedTreeData() {
        log.info("Invoke getExpandedTreeData()");

        return new TreeModelBase(getUserTree());
    }

    public void setTree(HtmlTree tree) {
        log.info("Invoke setTree( tree )");

        _tree = tree;
    }

    public HtmlTree getTree() {
        log.info("Invoke getTree()");

        return _tree;
    }

    public String expandAll() {
        log.info("Invoke expandAll()");

        _tree.expandAll();
        return null;
    }

    public void setNodePath(String nodePath) {
        log.info("Invoke setNodePath( nodePath )");

        _nodePath = nodePath;
    }

    public String getNodePath() {
        log.info("Invoke getNodePath()");

        return _nodePath;
    }

    public void checkPath(FacesContext context, UIComponent component, Object value) {
        log.info("Invoke checkPath()");

        FacesMessage message = null;
        String path[] = _tree.getPathInformation(value.toString());
        for (String nodeId : path) {
            try {
                _tree.setNodeId(nodeId);
            }
            catch (Exception e) {
                throw new ValidatorException(message, e);
            }
            if (_tree.getNode().isLeaf()) {
                message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid node path (cannot expand a leaf): " + nodeId, "Invalid node path (cannot expand a leaf): " + nodeId);
                throw new ValidatorException(message);
            }
        }

    }

    public void expandPath(ActionEvent event) {
        log.info("Invoke expandPath( event )");

        _tree.expandPath(_tree.getPathInformation(_nodePath));
    }
}