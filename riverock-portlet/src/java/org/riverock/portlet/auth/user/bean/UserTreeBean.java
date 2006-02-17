package org.riverock.portlet.auth.user.bean;

import java.io.Serializable;
import java.util.Iterator;

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
    private final static Logger log = Logger.getLogger( UserTreeBean.class );
    private static final long serialVersionUID = 2043005500L;

    private HtmlTree _tree;
    private String _nodePath;

    private DataProvider dataProvider = null;

    public UserTreeBean() {
    }

    public DataProvider getDataProvider() {
        return dataProvider;
    }

    public void setDataProvider( DataProvider dataProvider ) {
        this.dataProvider = dataProvider;
    }

    public TreeNode getUserTree() {
        TreeNode treeData = new TreeNodeBase( "foo-folder", "Company list", false );
        Iterator<CompanyBean> iterator = dataProvider.getCompanyBeans().iterator();
        while( iterator.hasNext() ) {
            CompanyBean companyBean = iterator.next();

            TreeNodeBase companyNode = new TreeNodeBase( "company", companyBean.getCompanyName(), companyBean.getCompanyId().toString(), false );
            Iterator<AuthUserExtendedInfoImpl> it = companyBean.getUserBeans().iterator();
            while( it.hasNext() ) {
                AuthUserExtendedInfoImpl authUserExtendedInfoImpl = it.next();

                companyNode.getChildren().add(
                    new TreeNodeBase(
                        "user",
                        authUserExtendedInfoImpl.getUserName() + " ( " +
                        authUserExtendedInfoImpl.getAuthInfo().getUserLogin() + " )",
                        authUserExtendedInfoImpl.getAuthInfo().getAuthUserId().toString(),
                        true )
                );
            }

            treeData.getChildren().add( companyNode );
        }

        return treeData;
    }

    public TreeModel getExpandedTreeData() {
        return new TreeModelBase( getUserTree() );
    }

    public void setTree( HtmlTree tree ) {
        _tree = tree;
    }

    public HtmlTree getTree() {
        return _tree;
    }

    public String expandAll() {
        _tree.expandAll();
        return null;
    }

    public void setNodePath( String nodePath ) {
        _nodePath = nodePath;
    }

    public String getNodePath() {
        return _nodePath;
    }

    public void checkPath( FacesContext context, UIComponent component, Object value ) {
        FacesMessage message = null;
        String path[] = _tree.getPathInformation( value.toString() );
        for( String nodeId : path ) {
            try {
                _tree.setNodeId( nodeId );
            }
            catch( Exception e ) {
                throw new ValidatorException( message, e );
            }
            if( _tree.getNode().isLeaf() ) {
                message = new FacesMessage( FacesMessage.SEVERITY_ERROR, "Invalid node path (cannot expand a leaf): " + nodeId, "Invalid node path (cannot expand a leaf): " + nodeId );
                throw new ValidatorException( message );
            }
        }

    }

    public void expandPath( ActionEvent event ) {
        _tree.expandPath( _tree.getPathInformation( _nodePath ) );
    }
}