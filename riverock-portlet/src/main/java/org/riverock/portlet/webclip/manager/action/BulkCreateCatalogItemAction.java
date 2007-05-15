package org.riverock.portlet.webclip.manager.action;

import java.io.Serializable;

import org.apache.log4j.Logger;

import org.riverock.portlet.manager.portletname.PortletNameSessionBean;
import org.riverock.portlet.manager.portletname.PortletNameBean;
import org.riverock.portlet.main.AuthSessionBean;
import org.riverock.portlet.tools.FacesTools;
import org.riverock.interfaces.portal.bean.PortletName;

/**
 * User: SergeMaslyukov
 * Date: 12.05.2007
 * Time: 12:52:28
 */
public class BulkCreateCatalogItemAction implements Serializable {
    private final static Logger log = Logger.getLogger(BulkCreateCatalogItemAction.class);

    private static final long serialVersionUID = 2057005501L;

    private PortletNameSessionBean portletNameSessionBean = null;
    private AuthSessionBean authSessionBean = null;

    public BulkCreateCatalogItemAction() {
    }

    public AuthSessionBean getAuthSessionBean() {
        return authSessionBean;
    }

    public void setAuthSessionBean(AuthSessionBean authSessionBean) {
        this.authSessionBean = authSessionBean;
    }

    public PortletNameSessionBean getSessionBean() {
        return portletNameSessionBean;
    }

    public void setPortletNameSessionBean(PortletNameSessionBean portletNameSessionBean) {
        this.portletNameSessionBean = portletNameSessionBean;
    }

    public String addPortletName() {
        portletNameSessionBean.setPortletName( new PortletNameBean() );

        return "portlet-name-add";
    }

    public String processAddPortletName() {
        Long id = FacesTools.getPortalDaoProvider().getPortalPortletNameDao().createPortletName( portletNameSessionBean.getPortletName() );
        portletNameSessionBean.setCurrentPortletNameId( id );
        loadCurrentPortletName();
        return "portlet-name";
    }

    public String cancelAddPortletName() {
        loadCurrentPortletName();
        return "portlet-name";
    }

    public String processEditPortletName() {
        FacesTools.getPortalDaoProvider().getPortalPortletNameDao().updatePortletName( portletNameSessionBean.getPortletName() );
        return "portlet-name";
    }

    public String cancelEditPortletName() {
        loadCurrentPortletName();
        return "portlet-name";
    }

    public String processDeletePortletName() {
        FacesTools.getPortalDaoProvider().getPortalPortletNameDao().deletePortletName( portletNameSessionBean.getPortletName() );
        portletNameSessionBean.setPortletName( null );
        return "portlet-name";
    }

    public String selectPortletName() {
        loadCurrentPortletName();
        return "portlet-name";
    }

    private void loadCurrentPortletName() {
        PortletName bean = FacesTools.getPortalDaoProvider().getPortalPortletNameDao().getPortletName( portletNameSessionBean.getCurrentPortletNameId() );
        portletNameSessionBean.setPortletName( new PortletNameBean(bean) );
    }
}
