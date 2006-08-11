package org.riverock.portlet.manager.menu;

import java.io.Serializable;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.myfaces.custom.tree2.TreeModel;
import org.apache.myfaces.custom.tree2.TreeModelBase;
import org.apache.myfaces.custom.tree2.TreeNode;
import org.apache.myfaces.custom.tree2.TreeNodeBase;
import org.apache.myfaces.custom.tree2.TreeState;
import org.apache.myfaces.custom.tree2.TreeStateBase;

import org.riverock.interfaces.portal.bean.CatalogItem;
import org.riverock.interfaces.portal.bean.CatalogLanguageItem;
import org.riverock.interfaces.portal.bean.Site;
import org.riverock.interfaces.portal.bean.SiteLanguage;

/**
 * @author Sergei Maslyukov
 *         Date: 14.06.2006
 *         Time: 21:44:21
 */
@SuppressWarnings({"unchecked"})
public class MenuTree implements Serializable {
    private final static Logger log = Logger.getLogger(MenuTree.class);
    private static final long serialVersionUID = 2057005500L;

    private MenuService menuService = null;
    private TreeState treeState=null;

    private MenuSessionBean menuSessionBean = null;

    public MenuTree() {
        treeState = new TreeStateBase();
        treeState.setTransient(true);
    }

    public MenuService getMenuService() {
        return menuService;
    }

    public void setMenuService(MenuService menuService) {
        this.menuService = menuService;
    }

    public MenuSessionBean getMenuSessionBean() {
        return menuSessionBean;
    }

    public void setMenuSessionBean(MenuSessionBean menuSessionBean) {
        this.menuSessionBean = menuSessionBean;
    }

    public TreeModel getMenuTree() {
        log.info("Invoke getMenuTree()");

        TreeNode rootNode = getPrepareMenuTree();
        TreeModel treeModel = new TreeModelBase(rootNode);
        treeModel.setTreeState(treeState);

        return treeModel;
    }

    private TreeNode getPrepareMenuTree() {

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
        }
        return treeRoot;
    }

    private void processMenuItem(TreeNodeBase node, List<CatalogItem> menuItemList) {
        for (CatalogItem catalogItem : menuItemList) {
            TreeNodeBase menuItemNode = new TreeNodeBase(
                "menu-item",
                catalogItem.getKeyMessage()+", ["+(catalogItem.getUrl()!=null?catalogItem.getUrl():"")+']',
                catalogItem.getCatalogId().toString(),
                false);
            node.getChildren().add(menuItemNode);
            if (catalogItem.getSubCatalogItemList()!=null) {
                processMenuItem(menuItemNode, catalogItem.getSubCatalogItemList());
            }
        }
    }
}
