package org.riverock.portlet.manager.portletname;

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

import org.riverock.interfaces.portal.bean.PortletName;
import org.riverock.portlet.tools.FacesTools;

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

    public List<PortletName> getPortletNameList() {
        List<PortletName> list = FacesTools.getPortalDaoProvider().getPortalPortletNameDao().getPortletNameList();
        if (list==null) {
            return null;
        }

        Iterator<PortletName> iterator = list.iterator();
        List<PortletName> portletNames = new ArrayList<PortletName>();
        while(iterator.hasNext()) {
            PortletName company = iterator.next();
            portletNames.add( new PortletNameBean(company) );
        }
        return portletNames;
    }
}
