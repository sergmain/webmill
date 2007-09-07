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
package org.riverock.portlet.manager.menu.bean;

import java.io.Serializable;

import org.riverock.interfaces.portal.bean.CatalogItem;
import org.riverock.interfaces.portal.bean.PortletName;
import org.riverock.interfaces.portal.bean.Template;
import org.riverock.portlet.manager.bean.TemplateBean;

/**
 * @author Sergei Maslyukov
 *         Date: 20.06.2006
 *         Time: 12:57:19
 */
public class MenuItemExtended implements Serializable {
    private MenuItemBean menuItem=null;
    private PortletNameBean portletName=null;
    private TemplateBean template =null;

    public MenuItemExtended(){}

    public MenuItemExtended(MenuItemBean menuItem, PortletNameBean portletName, TemplateBean templateBean){
        this.menuItem = menuItem;
        this.portletName = portletName;
        this.template = templateBean;
    }

    public MenuItemBean getMenuItem() {
        return menuItem;
    }

    public void setMenuItem(CatalogItem menuItem) {
        this.menuItem = new MenuItemBean(menuItem);
    }

    public PortletNameBean getPortletName() {
        return portletName;
    }

    public void setPortletName(PortletName portletName) {
        this.portletName = new PortletNameBean(portletName);
    }

    public TemplateBean getTemplate() {
        return template;
    }

    public void setTemplate(Template template) {
        this.template = new TemplateBean(template);
    }
}
