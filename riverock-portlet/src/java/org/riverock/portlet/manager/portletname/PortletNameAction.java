package org.riverock.portlet.manager.portletname;

import java.io.Serializable;
import org.riverock.portlet.main.AuthSessionBean;

/**
 * @author SergeMaslyukov
 *         Date: 26.02.2006
 *         Time: 15:55:44
 *         $Id$
 */
public class PortletNameAction implements Serializable {
    private static final long serialVersionUID = 2057005501L;

	private PortletNameSessionBean portletNameSessionBean = null;
	private AuthSessionBean authSessionBean = null;

	public PortletNameAction() {
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
		portletNameSessionBean.setPortletName( new PortletNameBeanImpl() );
	
		return "portlet-name-add";
	}

	public String processAddPortletName() {
        Long id = PortletNameDaoFactory.getPortletNameDao().addPortletName( portletNameSessionBean.getPortletName() );
        portletNameSessionBean.setCurrentPortletNameId( id );
		return "portlet-name";
	}

	public String cancelAddPortletName() {
		loadCurrentPortletName();
		return "portlet-name";
	}

	public String processEditPortletName() {
        PortletNameDaoFactory.getPortletNameDao().updatePortletName( portletNameSessionBean.getPortletName() );
		return "portlet-name";
	}

	public String cancelEditPortletName() {
		loadCurrentPortletName();
		return "portlet-name";
	}

	public String processDeletePortletName() {
        PortletNameDaoFactory.getPortletNameDao().deletePortletName( portletNameSessionBean.getPortletName() );
		portletNameSessionBean.setPortletName( null );
		return "portlet-name";
	}
	
	public String selectPortletName() {
		loadCurrentPortletName();
		return "portlet-name";
	}

	private void loadCurrentPortletName() {
        PortletNameBean bean = PortletNameDaoFactory.getPortletNameDao().getPortletName( portletNameSessionBean.getCurrentPortletNameId() );
		portletNameSessionBean.setPortletName( bean );
	}
}
