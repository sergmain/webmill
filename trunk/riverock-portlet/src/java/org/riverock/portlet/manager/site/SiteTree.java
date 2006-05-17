/**
 * 
 */
package org.riverock.portlet.manager.site;

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

import org.riverock.interfaces.portal.bean.Site;
import org.riverock.interfaces.portal.bean.SiteLanguage;

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

    public TreeNode getSiteTree() {

        log.info("Invoke getSiteTree()");

        TreeNode treeData = new TreeNodeBase("site-list", "Webmill portal. Site list.", false);
        for (Site site : siteService.getSites()) {
            TreeNodeBase siteNode = new TreeNodeBase("site", site.getSiteName(), site.getSiteId().toString(), false);
            for (SiteLanguage siteLanguage : siteService.getSiteLanguage(site.getSiteId())) {
                siteNode.getChildren().add(
                    new TreeNodeBase(
                        "site-language",
                        siteLanguage.getNameCustomLanguage() + " ( " + siteLanguage.getCustomLanguage() + " )",
                        siteLanguage.getSiteLanguageId().toString(),
                        true)
                );

            }
            treeData.getChildren().add(siteNode);
        }
        treeNode = treeData;

//      }
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
