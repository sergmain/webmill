package org.riverock.portlet.manager.menu.action;

import java.io.Serializable;

import javax.faces.event.ActionEvent;

import org.apache.log4j.Logger;

import org.riverock.portlet.main.AuthSessionBean;
import org.riverock.portlet.manager.menu.MenuDataProvider;
import org.riverock.portlet.manager.menu.MenuSessionBean;
import org.riverock.portlet.manager.menu.bean.MenuItemBean;

/**
 * @author Sergei Maslyukov
 * 16.05.2006
 * 20:14:59
 *
 *
 */
public class MenuAction implements Serializable {
    private final static Logger log = Logger.getLogger( MenuAction.class );
    private static final long serialVersionUID = 2057005511L;

    private MenuSessionBean siteSessionBean = null;
    private AuthSessionBean authSessionBean = null;
    private MenuDataProvider dataProvider = null;

    public MenuAction() {
    }

    public void setDataProvider(MenuDataProvider dataProvider) {
        this.dataProvider = dataProvider;
    }

    // getter/setter methods
    public void setMenuSessionBean( MenuSessionBean siteSessionBean) {
        this.siteSessionBean = siteSessionBean;
    }

    public AuthSessionBean getAuthSessionBean() {
        return authSessionBean;
    }

    public void setAuthSessionBean( AuthSessionBean authSessionBean) {
        this.authSessionBean = authSessionBean;
    }

// main select action
    public String selectMenuItem(ActionEvent event) {
        MenuAction.log.info( "Select menu item action." );
        loadCurrentObject();

        return "menu";
    }
/*
// Add actions
    public String addCssAction() {
        MenuAction.log.info( "Add menu item action." );

        MenuItemBean menuItemBean = new MenuItemBean();
        menuItemBean.setCatalogId(siteSessionBean.getId());
        setSessionObject(menuItemBean);

        return "css-add";
    }

    public String processAddCssAction() {
        MenuAction.log.info( "Procss add CSS action." );

        if( getSessionObject() !=null ) {
            Long cssId = FacesTools.getPortalDaoProvider().getPortalCssDao().createCss(
                getSessionObject()
            );
            setSessionObject(null);
            siteSessionBean.setId(cssId);
            cleadDataProviderObject();
            loadCurrentObject();
        }

        return "site";
    }

    public String cancelAddCssAction() {
        MenuAction.log.info( "Cancel add CSS action." );

        setSessionObject(null);
        cleadDataProviderObject();

        return "site";
    }

// Edit actions
    public String editCssAction() {
        MenuAction.log.info( "Edit CSS action." );

        return "css-edit";
    }

    public String processEditCssAction() {
        MenuAction.log.info( "Save changes CSS action." );

        if( getSessionObject() !=null ) {
            FacesTools.getPortalDaoProvider().getPortalCssDao().updateCss(getSessionObject());
            cleadDataProviderObject();
            loadCurrentObject();
        }

        return "site";
    }

    public String cancelEditCssAction() {
        MenuAction.log.info( "Cancel edit CSS action." );

        return "site";
    }

// Delete actions
    public String deleteCssAction() {
        MenuAction.log.info( "delete CSS action." );

        setSessionObject( new CssBean(dataProvider.getCss()) );

        return "css-delete";
    }

    public String cancelDeleteCssAction() {
        MenuAction.log.info( "Cancel delete CSS action." );

        return "site";
    }

    public String processDeleteCssAction() {
        MenuAction.log.info( "Process delete CSS action." );

        if( getSessionObject() != null ) {
            FacesTools.getPortalDaoProvider().getPortalCssDao().deleteCss(getSessionObject().getCssId());
            setSessionObject(null);
            siteSessionBean.setId(null);
            siteSessionBean.setObjectType(MenuSessionBean.UNKNOWN_TYPE);
            cleadDataProviderObject();
        }

        return "site";
    }

    private void setSessionObject(MenuItemBean bean) {
        siteSessionBean.setMenuItem( bean );
    }

*/
    private void loadCurrentObject() {
//        siteSessionBean.setMenuItem( new MenuItemBean(dataProvider.getMenuItem()) );
    }

    private void cleadDataProviderObject() {
        dataProvider.clearMenuItem();
    }

    private MenuItemBean getSessionObject() {
        return siteSessionBean.getMenuItem();
    }

}
