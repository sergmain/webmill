package org.riverock.portlet.manager.portletname;

import java.io.Serializable;

/**
 * @author SergeMaslyukov
 *         Date: 26.02.2006
 *         Time: 15:55:44
 *         $Id$
 */
public class PortletNameSessionBean implements Serializable {
    private static final long serialVersionUID = 2057005504L;

	private PortletNameBean portletName = null;
	private Long currentPortletNameId = null;

	public PortletNameSessionBean() {
	}

	public PortletNameBean getPortletName() {
		return portletName;
	}

	public void setPortletName(PortletNameBean portletName) {
		this.portletName = portletName;
	}

	public Long getCurrentPortletNameId() {
		return currentPortletNameId;
	}

	public void setCurrentPortletNameId(Long currentPortletNameId) {
		this.currentPortletNameId = currentPortletNameId;
	}
}
