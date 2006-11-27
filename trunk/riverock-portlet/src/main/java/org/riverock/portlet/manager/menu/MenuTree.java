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
package org.riverock.portlet.manager.menu;

import java.io.Serializable;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.myfaces.custom.tree2.HtmlTree;
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
    private HtmlTree _tree;

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

    public String expandAll() {
        _tree.expandAll();
        return null;
    }

    public void setTree(HtmlTree tree)
    {
        _tree = tree;
    }

    public HtmlTree getTree()
    {
        return _tree;
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

                processCatalogLanguage(siteLanguage, menuCatalogListNode);
            }
        }
        return treeRoot;
    }

    private void processCatalogLanguage(SiteLanguage siteLanguage, TreeNodeBase menuCatalogListNode) {
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
