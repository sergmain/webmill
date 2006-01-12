package org.riverock.portlet.auth.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.faces.validator.ValidatorException;

import org.apache.log4j.Logger;

import org.apache.myfaces.custom.tree2.HtmlTree;
import org.apache.myfaces.custom.tree2.TreeModel;
import org.apache.myfaces.custom.tree2.TreeModelBase;
import org.apache.myfaces.custom.tree2.TreeNode;
import org.apache.myfaces.custom.tree2.TreeNodeBase;

import org.riverock.portlet.tools.FacesTools;

/**
 * @author SergeMaslyukov
 *         Date: 02.01.2006
 *         Time: 9:26:35
 *         $Id$
 */
public class UserListBean implements Serializable {
    private final static Logger log = Logger.getLogger( UserListBean.class );
    private static final long serialVersionUID = 2043005500L;


    private UserBeanHolder userBeanHolder = null;
    private UserBeanManager userBeanManager = null;

    public UserListBean() {
    }

    public void selectUserAction( ActionEvent event ) {
        log.info( "Select user action." );

        Long authUserId = FacesTools.getLong( event.getComponent(), "authUserId" );
        UserBean userBean = lookupUserBean( authUserId );
        userBeanHolder.setUserBean( userBean );
        log.info( "Current user bean: " + userBean );

    }

// Role actions
    public void deleteRoleActionListener( ActionEvent event ) {
        log.info( "Delete role action." );

        Long roleId = FacesTools.getLong( event.getComponent(), "roleId" );
	log.info( "delete role with id: " + roleId );


	Iterator<RoleBean> iterator = null;

        iterator = userBeanHolder.getUserBean().getAllRoles().iterator();
        while( iterator.hasNext() ) {
            RoleBean roleBean = iterator.next();

            if( roleBean.getRoleId().equals( roleId ) ) {
		log.info( "Role is found. set isDelete to true" );
                roleBean.setDelete( true );
                break;
            }
        }
    }

    public void addRoleAction() {
        log.info( "Add role action." );

	Long roleId = userBeanHolder.getUserBean().getNewRoleId();
	;log.info("New id of role: " + roleId);
	if ( roleId == null ) {
		return;
	}

	userBeanHolder.getUserBean().getAllRoles().add( userBeanManager.getRole( roleId ) );
    }

// Add actions
    public void addUserAction() {
        log.info( "Add user action." );
        userBeanHolder.setUserBean( new UserBean() );
        if( userBeanHolder.getUserBean() != null ) {
            userBeanHolder.getUserBean().setAdd( true );
            userBeanHolder.getUserBean().setEdit( false );
            userBeanHolder.getUserBean().setDelete( false );
        }

    }

    public void processAddUserAction() {
        log.info( "Procss add user action." );
        if( userBeanHolder.getUserBean() != null ) {
            userBeanManager.processAddUser( userBeanHolder.getUserBean() );
            userBeanHolder.setUserBean( null );
            userBeanManager.reinitCompanyBeans();
        }
    }

    public void cancelAddUserAction() {
        log.info( "Cancel add action." );
        userBeanHolder.setUserBean( null );
    }

// Edit actions
    public void editUserAction() {
        log.info( "Edit user action." );
        if( userBeanHolder.getUserBean() != null ) {
            userBeanHolder.getUserBean().setAdd( false );
            userBeanHolder.getUserBean().setEdit( true );
            userBeanHolder.getUserBean().setDelete( false );
        }

    }

    public void saveUserAction() {
        log.info( "Save user action." );
        if( userBeanHolder.getUserBean() != null ) {
            userBeanHolder.getUserBean().setAdd( false );
            userBeanHolder.getUserBean().setEdit( false );
            userBeanHolder.getUserBean().setDelete( false );

            userBeanManager.processSaveUser( userBeanHolder.getUserBean() );
            userBeanHolder.setUserBean( null );
            userBeanManager.reinitCompanyBeans();
        }
    }

    public void cancelEditUserAction() {
        log.info( "Cancel edit action." );
        if( userBeanHolder.getUserBean() != null ) {
            userBeanHolder.getUserBean().setAdd( false );
            userBeanHolder.getUserBean().setEdit( false );
            userBeanHolder.getUserBean().setDelete( false );
            userBeanHolder.setUserBean( null );
            userBeanManager.reinitCompanyBeans();
        }
    }

// Delete actions
    public void deleteUserAction() {
        log.info( "Edit user action." );
        if( userBeanHolder.getUserBean() != null ) {
            userBeanHolder.getUserBean().setEdit( false );
            userBeanHolder.getUserBean().setDelete( true );
        }

    }

    public void cancelDeleteUserAction() {
        log.info( "Cancel delete user action." );
        if( userBeanHolder.getUserBean() != null ) {
            userBeanHolder.getUserBean().setEdit( false );
            userBeanHolder.getUserBean().setDelete( false );
        }
    }

    public void processDeleteUserAction() {
        log.info( "Process delete user action." );
        if( userBeanHolder.getUserBean() != null ) {
            userBeanManager.processDeleteUser( userBeanHolder.getUserBean() );
            userBeanHolder.setUserBean( null );
            userBeanManager.reinitCompanyBeans();
        }
    }

/*
    public void changeCompanyValueListener( ValueChangeEvent event ) {
        String selectedValues[] = (String[])evt.getNewValue();
        if(selectedValues.length == 0)
        {
            selectedInfo = "No selected values";
        } else
        {
            StringBuffer sb = new StringBuffer("Selected values: ");
            for(int i = 0; i < selectedValues.length; i++)
            {
                if(i > 0)
                    sb.append(", ");
                sb.append(selectedValues[i]);
            }

            selectedInfo = sb.toString();
        }
        log.info( "new value: " + event.getNewValue() + ", old value: " + event.getOldValue() );
    }
*/

    public void setUserBeanHolder( UserBeanHolder userBeanHolder ) {
        this.userBeanHolder = userBeanHolder;
    }

    public UserBean getCurrentUser() {
        return userBeanHolder.getUserBean();
    }

    private UserBean lookupUserBean( Long authUserId ) {
        log.info( "start search user bean for authUserId: " + authUserId );

        UserBean resultUserBean = null;
        Iterator<CompanyBean> iterator = userBeanManager.getCompanyBeans().iterator();
        while( iterator.hasNext() ) {
            CompanyBean companyBean = iterator.next();

            Iterator<UserBean> it = companyBean.getUserBeans().iterator();
            while( it.hasNext() ) {
                UserBean userBean = it.next();

                if( userBean.getAuthUserId().equals( authUserId ) )
                    resultUserBean = userBean;

                userBean.setEdit( false );
            }
        }
        return resultUserBean;
    }

    public List<SelectItem> getUserList() {
        List<SelectItem> list = new ArrayList<SelectItem>();
        List<UserItemBean> companies = userBeanManager.getUserList();

        Iterator<UserItemBean> iterator = companies.iterator();
        while( iterator.hasNext() ) {
            UserItemBean user = iterator.next();

            list.add( new SelectItem( user.getUserId(), user.getUserName() ) );
        }
        return list;
    }

    private boolean isAlreadyBinded( RoleBean roleBean ) {
        Iterator<RoleBean> iterator = userBeanHolder.getUserBean().getRoles().iterator();
        while( iterator.hasNext() ) {
            RoleBean role = iterator.next();
		if (role.equals( roleBean ) ) {
			return true;
		}
        }
	return false;
    }

    public List<SelectItem> getRoleList() {
        List<SelectItem> list = new ArrayList<SelectItem>();
        List<RoleBean> companies = userBeanManager.getRoleList();

        Iterator<RoleBean> iterator = companies.iterator();
        while( iterator.hasNext() ) {
            RoleBean role = iterator.next();

	     if (!isAlreadyBinded( role ) )  {
            	list.add( new SelectItem( role.getRoleId(), role.getName() ) );
	     }
        }
        return list;
    }

    public List<SelectItem> getCompanyList() {
        List<SelectItem> list = new ArrayList<SelectItem>();
        List<CompanyItemBean> companies = userBeanManager.getCompanyList();

        Iterator<CompanyItemBean> iterator = companies.iterator();
        while( iterator.hasNext() ) {
            CompanyItemBean company = iterator.next();

            list.add( new SelectItem( company.getId(), company.getName() ) );
        }
        return list;
    }

    public List<SelectItem> getGroupCompanyList() {
        List<SelectItem> list = new ArrayList<SelectItem>();
        List<GroupCompanyItemBean> groupCompanies = userBeanManager.getGroupCompanyList();

        Iterator<GroupCompanyItemBean> iterator = groupCompanies.iterator();
        while( iterator.hasNext() ) {
            GroupCompanyItemBean groupCompany = iterator.next();

            list.add( new SelectItem( groupCompany.getId(), groupCompany.getName() ) );
        }
        return list;
    }

    public List<SelectItem> getHoldingList() {
        List<SelectItem> list = new ArrayList<SelectItem>();
        List<HoldingItemBean> holdings = userBeanManager.getHoldingList();

        Iterator<HoldingItemBean> iterator = holdings.iterator();
        while( iterator.hasNext() ) {
            HoldingItemBean holding = iterator.next();

            list.add( new SelectItem( holding.getId(), holding.getName() ) );
        }
        return list;
    }

    public UserBeanManager getUserBeanManager() {
        return userBeanManager;
    }

    public void setUserBeanManager( UserBeanManager userBeanManager ) {
        if( log.isDebugEnabled() ) {
            log.debug( "set user bean manager to " + userBeanManager );
        }
        this.userBeanManager = userBeanManager;
    }

    public TreeNode getUserTree() {
        TreeNode treeData = new TreeNodeBase( "foo-folder", "Company list", false );
        Iterator<CompanyBean> iterator = userBeanManager.getCompanyBeans().iterator();
        while( iterator.hasNext() ) {
            CompanyBean companyBean = iterator.next();

            TreeNodeBase companyNode = new TreeNodeBase( "company", companyBean.getCompanyName(), companyBean.getCompanyId().toString(), false );
            Iterator<UserBean> it = companyBean.getUserBeans().iterator();
            while( it.hasNext() ) {
                UserBean userBean = it.next();

                companyNode.getChildren().add( new TreeNodeBase( "user", userBean.getUserName() + " ( " + userBean.getUserLogin() + " )", userBean.getAuthUserId().toString(), true ) );
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

    private HtmlTree _tree;
    private String _nodePath;

/*
    private void logInfo( ActionEvent ev ) {

        FacesContext facesContext = FacesContext.getCurrentInstance();
        UIComponent c = ev.getComponent();
        log.info( "c: " + c );
        if( c != null ) {
            log.info( "c.getFamily(): " + c.getFamily() );
            log.info( "c.getId(): " + c.getId() );
            log.info( "c.getRendererType(): " + c.getRendererType() );
            Map map = c.getAttributes();
            Iterator iterator = map.entrySet().iterator();
            while( iterator.hasNext() ) {
                Map.Entry entry = ( Map.Entry ) iterator.next();
                log.info( "key: " + ( ( String ) entry.getKey() ) + ", value: " + entry.getValue() );
            }
            List list = c.getChildren();
            if( list != null ) {
                Iterator it = list.iterator();
                while( it.hasNext() ) {
                    UIComponent c1 = ( UIComponent ) it.next();
                    log.info( "c1: " + c1 );
                    if( c1 instanceof javax.faces.component.UIParameter ) {
                        javax.faces.component.UIParameter p = ( javax.faces.component.UIParameter ) c1;
                        log.info( "p.name: " + p.getName() );
                        log.info( "p.value: " + p.getValue() );
                    }
                    log.info( "c1.getFamily(): " + c1.getFamily() );
                    log.info( "c1.getId(): " + c1.getId() );
                    log.info( "c1.getRendererType(): " + c1.getRendererType() );
                    Map map1 = c1.getAttributes();
                    Iterator iterator1 = map1.entrySet().iterator();
                    while( iterator1.hasNext() ) {
                        Map.Entry entry1 = ( Map.Entry ) iterator1.next();
                        log.info( "key: " + ( ( String ) entry1.getKey() ) + ", value: " + entry1.getValue() );
                    }
                }
            }
        }
        UIComponent c1 = c.findComponent( "authUserId" );
        log.info( "find component: " + c1 );
        javax.faces.el.ValueBinding b = c.getValueBinding( "authUserId" );
        log.info( "ValueBinding: " + b );
        if( b != null ) {
            Class clazz = b.getType( facesContext );
            log.info( "b.type: " + clazz + ", b.value: " + b.getValue( facesContext ) );
        }

        log.info( "_nodePath: " + _nodePath );
        if( _tree != null )
            log.info( "_tree.getPathInformation( _nodePath ): " + _tree.getPathInformation( _nodePath ) );
        else
            log.info( "_tree is null" );

        UIComponent component = ev.getComponent();
        VariableResolver vr = facesContext.getApplication().getVariableResolver();

        Object obj = vr.resolveVariable( facesContext, "treeBacker" );
        log.info( "obj: " + obj );
        log.info( "component: " + component );
        if( component != null )
            log.info( "component.getId()" + component.getId() );
        else
            log.info( "component is null" );


    }
*/
}