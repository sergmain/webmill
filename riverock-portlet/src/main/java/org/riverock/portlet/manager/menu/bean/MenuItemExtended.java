/*
 * org.riverock.portlet - Portlet Library
 *
 * Copyright (C) 2006, Riverock Software, All Rights Reserved.
 *
 * Riverock - The Open-source Java Development Community
 * http://www.riverock.org
 *
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.riverock.portlet.manager.menu.bean;

import java.io.Serializable;

import org.riverock.interfaces.portal.bean.CatalogItem;
import org.riverock.interfaces.portal.bean.PortletName;
import org.riverock.interfaces.portal.bean.Template;

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
