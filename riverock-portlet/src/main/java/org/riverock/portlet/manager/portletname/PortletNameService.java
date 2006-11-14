/*
 * org.riverock.portlet - Portlet Library
 *
 * Copyright (C) 2006, Riverock Software, All Rights Reserved.
 *
 * Riverock - The Open-source Java Development Community
 * http://www.riverock.org
 *
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
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
