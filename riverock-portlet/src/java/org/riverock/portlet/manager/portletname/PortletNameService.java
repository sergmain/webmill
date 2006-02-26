package org.riverock.portlet.manager.portletname;

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * @author SergeMaslyukov
 *         Date: 26.02.2006
 *         Time: 15:55:44
 *         $Id$
 */
public class PortletNameService implements Serializable {
    private static final long serialVersionUID = 2055005515L;

	public PortletNameService() {
	}

	public List<PortletNameBean> getPortletNameList() {
		List<PortletNameBean> list = PortletNameDaoFactory.getPortletNameDao().getPortletNameList();
		if (list==null) {
			return null;
		}
		
		Iterator<PortletNameBean> iterator = list.iterator();
		List<PortletNameBean> portletNames = new ArrayList<PortletNameBean>();
		while(iterator.hasNext()) {
			PortletNameBean company = iterator.next();
			portletNames.add( new PortletNameBeanImpl(company) );
		}
		return portletNames;
	}
}
