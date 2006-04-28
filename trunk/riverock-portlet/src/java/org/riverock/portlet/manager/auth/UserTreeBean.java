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