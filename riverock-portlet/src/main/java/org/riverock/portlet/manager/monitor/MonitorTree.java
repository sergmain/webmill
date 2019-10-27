package org.riverock.portlet.manager.monitor;

import java.io.File;
import java.io.FileInputStream;
import java.io.Serializable;
import java.util.List;
import java.util.Locale;
import java.text.NumberFormat;

import org.apache.log4j.Logger;
import org.apache.myfaces.custom.tree2.HtmlTree;
import org.apache.myfaces.custom.tree2.TreeModel;
import org.apache.myfaces.custom.tree2.TreeModelBase;
import org.apache.myfaces.custom.tree2.TreeNode;
import org.apache.myfaces.custom.tree2.TreeNodeBase;
import org.apache.myfaces.custom.tree2.TreeState;
import org.apache.myfaces.custom.tree2.TreeStateBase;

import org.riverock.common.config.PropertiesProvider;
import org.riverock.common.tools.XmlTools;
import org.riverock.portlet.manager.monitor.schema.Directory;
import org.riverock.portlet.tools.FacesTools;
import org.riverock.interfaces.ContainerConstants;

/**
 * User: SMaslyukov
 * Date: 15.05.2007
 * Time: 19:20:17
 */
@SuppressWarnings("unchecked")
public class MonitorTree implements Serializable {
    private final static Logger log = Logger.getLogger(MonitorTree.class);

    private final static NumberFormat nf = NumberFormat.getIntegerInstance(Locale.ENGLISH);

    private TreeState treeState=null;
    private HtmlTree tree;
    private String result;
    private static final int KILOBYTE = 1024;

    public MonitorTree() {
        treeState = new TreeStateBase();
        treeState.setTransient(true);
    }

    public String expandAll() {
        tree.expandAll();
        return null;
    }

    public String collapseAll() {
        tree.collapseAll();
        return null;
    }

    public void setTree(HtmlTree tree) {
        this.tree = tree;
    }

    public HtmlTree getTree() {
        return tree;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public TreeModel getServerTree() {
        log.info("Invoke getServerTree()");

        TreeNode rootNode = getPrepareServerTree();
        TreeModel treeModel = new TreeModelBase(rootNode);
        treeModel.setTreeState(treeState);

        return treeModel;
    }

    public TreeNode getPrepareServerTree() {
        log.info("Invoke getPrepareServerTree()");

        Long siteId = Long.decode(FacesTools.getPortletRequest().getPortalContext().getProperty(ContainerConstants.PORTAL_PROP_SITE_ID));
        String serverPath = PropertiesProvider.getApplicationPath() + ServerMonitorConstants.SERVER_MONITOR_DIR + siteId;

        TreeNode treeRoot = new TreeNodeBase("tree-root", "tree-root", false);

        File path = new File(serverPath);
        File serverSize = new File(path, ServerMonitorConstants.SERVER_SIZE_XML);
        if (!serverSize.exists()) {
            log.debug("file "+serverSize.getName()+" not exist");
            return treeRoot;
        }

        try {
            Directory d = XmlTools.getObjectFromXml(Directory.class, new FileInputStream(serverSize));
            if (log.isDebugEnabled()) {
                log.debug("directory bean: " + d);
                if (d!=null) {
                    log.debug("count of subDirs: " + d.getSubDirectory().size());
                }
            }

            TreeNodeBase directoryNode = prepareDirectory(d);
            treeRoot.getChildren().add(directoryNode);

            processDirectory(directoryNode, d.getSubDirectory());
        }
        catch (Exception e) {
            String es = "Error create tree";
            log.error(es, e);
            result = es + ' ' + e.toString();
        }

        return treeRoot;
    }

    private void processDirectory(TreeNodeBase node, List<Directory> directories) {
        if (directories==null || directories.isEmpty()) {
            return;
        }
        for (Directory directory : directories) {
            TreeNodeBase menuItemNode = prepareDirectory(directory);
            node.getChildren().add(menuItemNode);
            processDirectory(menuItemNode, directory.getSubDirectory());
        }
    }

    private TreeNodeBase prepareDirectory(Directory directory) {
        return new TreeNodeBase(
            "directory",
            directory.getName()+", ["+
                nf.format(directory.getSize()/ KILOBYTE) +"Kb,  " +
                nf.format(directory.getSizeSubDirectory()/ KILOBYTE) +"Kb]",
            directory.getName(),
            false);
    }
}
