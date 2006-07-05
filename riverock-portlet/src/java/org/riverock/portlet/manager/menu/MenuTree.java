package org.riverock.portlet.manager.menu;

import java.io.Serializable;
import java.util.List;

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

import org.riverock.interfaces.portal.bean.CatalogLanguageItem;
import org.riverock.interfaces.portal.bean.Site;
import org.riverock.interfaces.portal.bean.SiteLanguage;
import org.riverock.interfaces.portal.bean.CatalogItem;

/**
 * @author Sergei Maslyukov
 *         Date: 14.06.2006
 *         Time: 21:44:21
 */
@SuppressWarnings({"unchecked"})
public class MenuTree implements Serializable {
    private final static Logger log = Logger.getLogger(MenuTree.class);
    private static final long serialVersionUID = 2057005500L;

    private HtmlTree _tree;
    private String _nodePath;

    @SuppressWarnings({"FieldCanBeLocal"})
    private TreeNode treeNode = null;
    private MenuService menuService = null;

    private MenuSessionBean menuSessionBean = null;

    public MenuTree() {
    }

    public MenuSessionBean getMenuSessionBean() {
        return menuSessionBean;
    }

    public void setMenuSessionBean(MenuSessionBean menuSessionBean) {
        this.menuSessionBean = menuSessionBean;
    }

    public void setMenuTree(TreeNode treeNode) {
        this.treeNode = treeNode;
    }

    public TreeNode getMenuTree() {

        log.info("Invoke getMenuTree()");

        TreeNode treeRoot = new TreeNodeBase("tree-root", "tree-root", false);
        if (menuSessionBean.getCurrentSiteId()!=null) {

            Site site = menuService.getSite(menuSessionBean.getCurrentSiteId());

            TreeNodeBase siteNode = new TreeNodeBase("site", site.getSiteName(), site.getSiteId().toString(), false);
            treeRoot.getChildren().add(siteNode);
            
            for (SiteLanguage siteLanguage : menuService.getSiteLanguageList(site.getSiteId())) {
                TreeNodeBase siteLanguageNode = new TreeNodeBase(
                    "site-language",
                    siteLanguage.getNameCustomLanguage() + " (" + siteLanguage.getCustomLanguage() + ")",
                    siteLanguage.getSiteLanguageId().toString(),
                    false);
                treeRoot.getChildren().add(siteLanguageNode);

                TreeNodeBase menuCatalogListNode = new TreeNodeBase("menu-catalog-list", "Menu catalog list", siteLanguage.getSiteLanguageId().toString(), false);
                siteLanguageNode.getChildren().add(menuCatalogListNode);

                for (CatalogLanguageItem catalogLanguageItem : menuService.getMenuCatalogList(siteLanguage.getSiteLanguageId())) {
                    TreeNodeBase menuCatalogNode = new TreeNodeBase(
                        "menu-catalog",
                        catalogLanguageItem.getCatalogCode(),
                        catalogLanguageItem.getCatalogLanguageId().toString(),
                        false);
                    menuCatalogListNode.getChildren().add(menuCatalogNode);

                    processMenuItem(menuCatalogNode, menuService.getMenuItemList(catalogLanguageItem.getCatalogLanguageId()));
                }

            }
//            treeRoot.getChildren().add(siteNode);
        }
        treeNode = treeRoot;
        return treeNode;
    }

    private void processMenuItem(TreeNodeBase node, List<CatalogItem> menuItemList) {
        for (CatalogItem catalogItem : menuItemList) {
            TreeNodeBase menuItemNode = new TreeNodeBase(
                "menu-item",
                catalogItem.getKeyMessage()+", "+catalogItem.getUrl(),
                catalogItem.getCatalogId().toString(),
                false);
            node.getChildren().add(menuItemNode);
            if (catalogItem.getSubCatalogItemList()!=null) {
                processMenuItem(menuItemNode, catalogItem.getSubCatalogItemList());
            }
        }
    }

    public TreeModel getExpandedTreeData() {
        log.info("Invoke getExpandedTreeData()");

        return new TreeModelBase(getMenuTree());
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

    public MenuService getMenuService() {
        return menuService;
    }

    public void setMenuService(MenuService menuService) {
        this.menuService = menuService;
    }

}
