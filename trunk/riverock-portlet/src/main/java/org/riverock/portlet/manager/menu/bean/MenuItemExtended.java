package org.riverock.portlet.manager.menu.bean;

import org.riverock.interfaces.portal.bean.CatalogItem;
import org.riverock.interfaces.portal.bean.PortletName;
import org.riverock.interfaces.portal.bean.Template;

/**
 * @author Sergei Maslyukov
 *         Date: 20.06.2006
 *         Time: 12:57:19
 */
public class MenuItemExtended {
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
