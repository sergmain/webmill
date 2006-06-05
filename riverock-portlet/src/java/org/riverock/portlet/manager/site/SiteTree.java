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
package org.riverock.portlet.manager.site;

import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.validator.ValidatorException;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.myfaces.custom.tree2.HtmlTree;
import org.apache.myfaces.custom.tree2.TreeModel;
import org.apache.myfaces.custom.tree2.TreeModelBase;
import org.apache.myfaces.custom.tree2.TreeNode;
import org.apache.myfaces.custom.tree2.TreeNodeBase;

import org.riverock.interfaces.portal.bean.Css;
import org.riverock.interfaces.portal.bean.Site;
import org.riverock.interfaces.portal.bean.SiteLanguage;
import org.riverock.interfaces.portal.bean.Template;
import org.riverock.interfaces.portal.bean.Xslt;

/**
 * @author SergeMaslyukov
 *         Date: 16.05.2006
 *         Time: 20:06:35
 *         $Id$
 */
public class SiteTree implements Serializable {
    private final static Logger log = Logger.getLogger(SiteTree.class);
    private static final long serialVersionUID = 2057005500L;

    private HtmlTree _tree;
    private String _nodePath;

    private TreeNode treeNode = null;
    private SiteService siteService=null;

    public SiteTree() {
    }

    public void setSiteTree(TreeNode treeNode) {
        this.treeNode = treeNode;
    }

    @SuppressWarnings("unchecked")
	public TreeNode getSiteTree() {

        log.info("Invoke getSiteTree()");

        TreeNode treeRoot = new TreeNodeBase("tree-root", "tree-root", false);
        TreeNode treeData = new TreeNodeBase("site-list", "Webmill portal. Site list.", false);
        treeRoot.getChildren().add(treeData);
        for (Site site : siteService.getSites()) {
            TreeNodeBase siteNode = new TreeNodeBase("site", site.getSiteName(), site.getSiteId().toString(), false);

            TreeNodeBase siteLanguageListNode = new TreeNodeBase("site-language-list", "Site languages", site.getSiteId().toString(), false);
            siteNode.getChildren().add( siteLanguageListNode );

            TreeNodeBase cssListNode = new TreeNodeBase("css-list", "Css list", site.getSiteId().toString(), false);
            siteNode.getChildren().add( cssListNode );

            for (Css css : siteService.getCssList(site.getSiteId())) {
                TreeNodeBase cssNode =
                    new TreeNodeBase("css", StringUtils.isNotBlank(css.getCssComment()) ? css.getCssComment() : "CSS",
                        css.getCssId().toString(), true);

                cssListNode.getChildren().add(cssNode);
            }

            for (SiteLanguage siteLanguage : siteService.getSiteLanguageList(site.getSiteId())) {
                TreeNodeBase siteLanguageNode = new TreeNodeBase(
                    "site-language",
                    siteLanguage.getNameCustomLanguage() + " (" + siteLanguage.getCustomLanguage() + ")",
                    siteLanguage.getSiteLanguageId().toString(),
                    false);
                siteLanguageListNode.getChildren().add(siteLanguageNode);

                TreeNodeBase templateListNode = new TreeNodeBase("template-list", "Templates", siteLanguage.getSiteLanguageId().toString(), false);
                for (Template template : siteService.getTemplateList(siteLanguage.getSiteLanguageId())) {
                    templateListNode.getChildren().add(
                        new TreeNodeBase("template",template.getTemplateName(),template.getTemplateId().toString(),true)
                    );
                }
                siteLanguageNode.getChildren().add(templateListNode);

                TreeNodeBase xsltListNode = new TreeNodeBase("xslt-list", "Xslt list", siteLanguage.getSiteLanguageId().toString(), false);
                for (Xslt xslt : siteService.getXsltList(siteLanguage.getSiteLanguageId())) {
                    xsltListNode.getChildren().add(
                        new TreeNodeBase("xslt",xslt.getName(),xslt.getId().toString(),true)
                    );
                }
                siteLanguageNode.getChildren().add(xsltListNode);
            }

            treeData.getChildren().add(siteNode);
        }
        treeNode = treeRoot;
        return treeNode;
    }

    public TreeModel getExpandedTreeData() {
        log.info("Invoke getExpandedTreeData()");

        return new TreeModelBase(getSiteTree());
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

    public SiteService getSiteService() {
        return siteService;
    }

    public void setSiteService(SiteService siteService) {
        this.siteService = siteService;
    }

}
