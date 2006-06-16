package org.riverock.portlet.manager.menu.action;

import java.io.Serializable;

import javax.faces.event.ActionEvent;

import org.apache.log4j.Logger;

import org.riverock.portlet.main.AuthSessionBean;
import org.riverock.portlet.manager.menu.MenuDataProvider;
import org.riverock.portlet.manager.menu.MenuSessionBean;

/**
 * @author Sergei Maslyukov
 * 16.05.2006
 * 20:14:59
 *
 *
 */
public class MenuSiteAction implements Serializable {
    private final static Logger log = Logger.getLogger( MenuSiteAction.class );
    private static final long serialVersionUID = 2057005511L;

    private MenuSessionBean siteSessionBean = null;
    private AuthSessionBean authSessionBean = null;
    private MenuDataProvider dataProvider = null;

    public MenuSiteAction() {
    }

    public void setDataProvider(MenuDataProvider dataProvider) {
        this.dataProvider = dataProvider;
    }

    public void setMenuSessionBean( MenuSessionBean siteSessionBean) {
        this.siteSessionBean = siteSessionBean;
    }

    public AuthSessionBean getAuthSessionBean() {
        return authSessionBean;
    }

    public void setAuthSessionBean( AuthSessionBean authSessionBean) {
        this.authSessionBean = authSessionBean;
    }

    public String selectSite(ActionEvent event) {
        log.debug( "Select site action." );
        loadCurrentSite();

        return "menu";
    }

// private methods
    private void loadCurrentSite() {
//        siteSessionBean.setSite( dataProvider.getSite() );
    }
}
