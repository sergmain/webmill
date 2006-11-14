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
package org.riverock.portlet.manager.menu;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import org.apache.log4j.Logger;

import org.riverock.interfaces.portal.bean.PortletName;
import org.riverock.interfaces.portal.bean.SiteLanguage;
import org.riverock.interfaces.portal.bean.Template;
import org.riverock.interfaces.portlet.member.ClassQueryItem;
import org.riverock.interfaces.portlet.member.PortletGetList;
import org.riverock.portlet.manager.menu.bean.MenuCatalogBean;
import org.riverock.portlet.manager.menu.bean.MenuItemBean;
import org.riverock.portlet.manager.menu.bean.MenuItemExtended;
import org.riverock.portlet.manager.menu.bean.SiteExtended;
import org.riverock.portlet.tools.FacesTools;
import org.riverock.webmill.container.ContainerConstants;
import org.riverock.webmill.container.bean.PortletWebApplication;
import org.riverock.webmill.container.portlet.PortletContainer;
import org.riverock.webmill.container.tools.PortletService;

/**
 * @author Sergei Maslyukov
 *         Date: 14.06.2006
 *         Time: 21:43:41
 */
public class MenuDataProvider implements Serializable {
    private final static Logger log = Logger.getLogger(MenuDataProvider.class);
    private static final long serialVersionUID = 2057005500L;

    private MenuService menuService =null;
    private MenuSessionBean menuSessionBean = null;

    private SiteExtended siteExtended = null;
    private SiteLanguage siteLanguage = null;
    private MenuItemExtended menuItem = null;
    private MenuCatalogBean menuCatalog = null;
    private List<SelectItem> contextList=null;

    public MenuDataProvider() {
    }

    public MenuSessionBean getMenuSessionBean() {
        return menuSessionBean;
    }

    public void setMenuSessionBean(MenuSessionBean menuSessionBean) {
        this.menuSessionBean = menuSessionBean;
    }

    public void setMenuService(MenuService menuService) {
        this.menuService = menuService;
    }

    public List<SelectItem> getContextList() {
        log.debug("Start getContextList()");

        if (contextList!=null) {
            return contextList;
        }

        List<SelectItem> list = new ArrayList<SelectItem>();

        MenuItemBean menuItem = menuSessionBean.getMenuItem().getMenuItem();
        Long portletId = menuItem.getPortletId();
        PortletName bean = FacesTools.getPortalDaoProvider().getPortalPortletNameDao().getPortletName(portletId);
        if (log.isDebugEnabled()) {
            log.debug("portletId: " + portletId);
            log.debug("bean: " + bean);
        }

        if (bean==null) {
            return list;
        }

        if (log.isDebugEnabled()) {
            log.debug("namePortlet: "+bean.getPortletName());
        }

        PortletContainer portletContainer = (PortletContainer)FacesTools.getAttribute( ContainerConstants.PORTAL_CURRENT_CONTAINER );
        PortletWebApplication portletWebApplication = portletContainer.searchPortletItem( bean.getPortletName() );

        if (log.isDebugEnabled()) {
            log.debug("portletWebApplication "+portletWebApplication);
        }

        if ( portletWebApplication ==null ) {
            return list;
        }

        String classNameTemp =
            PortletService.getStringParam(
                portletWebApplication.getPortletDefinition(), ContainerConstants.class_name_get_list
            );

        if (classNameTemp==null) {
            return list;
        }

        List<ClassQueryItem> v=null;
        ClassLoader oldLoader = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader( portletWebApplication.getClassLoader() );

            Constructor constructor;
            try {
                constructor = Class.forName(classNameTemp, false, portletWebApplication.getClassLoader()).getConstructor();
            }
            catch (Exception e) {
                String es = "Error getConstructor()";
                log.error(es, e);
                throw new IllegalStateException(es, e);
            }

            if (log.isDebugEnabled()) {
                log.debug("#12.12.005  constructor is " + constructor);
            }

            if (constructor != null) {
                PortletGetList obj;
                Object o = null;
                try {
                    o = constructor.newInstance();
                    obj = (PortletGetList)o;
                }
                catch (ClassCastException e) {
                    if (o!=null) {
                        log.error("ClassCastException to PortletGetList.class  from "+o.getClass().getName(), e);
                    }
                    else {
                        log.error("ClassCastException to PortletGetList.class  from null", e);
                    }
                    throw e;
                }
                catch (Throwable e) {
                    String es = "Error invoke constructor ";
                    log.error(es, e);
                    throw new IllegalStateException(es, e);
                }

                if (log.isDebugEnabled())
                {
                    log.debug("#12.12.008 object " + obj);
                    log.debug("#12.12.009 localePack  " +
                        PortletService.getStringParam(
                            portletWebApplication.getPortletDefinition(), ContainerConstants.locale_name_package
                        )
                    );
                }

                v = obj.getList( menuItem.getCatalogLanguageId(), menuItem.getContextId());

                if (v==null) {
                    return list;
                }

            }

        }
        finally {
            Thread.currentThread().setContextClassLoader( oldLoader );
        }

        if (v!=null) {
            for (ClassQueryItem classQueryItem : v) {
                list.add( new SelectItem(classQueryItem.getIndex(), classQueryItem.getValue()));
            }
            contextList=list;
        }

        return list;
    }


    public List<SelectItem> getTemplateList() {
        List<SelectItem> list = new ArrayList<SelectItem>();
        List<Template> portletNames = menuSessionBean.getTemplates();

        for (Template portletName : portletNames) {
            if (portletName.getTemplateId() == null) {
                throw new IllegalStateException("id is null, name: " + portletName.getTemplateName());
            }

            list.add(new SelectItem(portletName.getTemplateId(), portletName.getTemplateName()));
        }
        return list;
    }

    public SiteExtended getSiteExtended() {
        if (menuSessionBean.getObjectType()!=menuSessionBean.getSiteType()) {
            throw new IllegalStateException("Query site info with not site type, current type: " + menuSessionBean.getObjectType());
        }
        Long siteId = menuSessionBean.getId();
        if (siteExtended==null) {
            siteExtended= menuService.getSiteExtended(siteId);
        }
        if (!siteExtended.getSite().getSiteId().equals(siteId)) {
            log.warn("Mismatch siteId");
            siteExtended= menuService.getSiteExtended(siteId);
        }

        return siteExtended;
    }

    public void clearSite() {
        this.siteExtended=null;
    }

    public SiteLanguage getSiteLanguage() {
        if (menuSessionBean.getObjectType()!=menuSessionBean.getSiteLanguageType()) {
            throw new IllegalStateException("Query site language info with not site language type, current type: " + menuSessionBean.getObjectType());
        }
        Long siteLangaugeId = menuSessionBean.getId();
        if (siteLanguage==null) {
            siteLanguage = menuService.getSiteLanguage(siteLangaugeId);
        }
    if (siteLanguage.getSiteLanguageId()==null) {
        return siteLanguage;
    }
        if (!siteLanguage.getSiteLanguageId().equals(siteLangaugeId)) {
            log.warn("Mismatch siteLangaugeId");
            siteLanguage = menuService.getSiteLanguage(siteLangaugeId);
        }

        return siteLanguage;
    }

    public void clearSiteLanguage() {
        this.siteLanguage=null;
    }

    public void clearMenuCatalog() {
        this.menuCatalog=null;
    }

    public MenuCatalogBean getMenuCatalog() {
        if (menuSessionBean.getObjectType()!=menuSessionBean.getMenuCatalogType()) {
            throw new IllegalStateException("Query menu catalog info with not menu catalog type, current type: " + menuSessionBean.getObjectType());
        }
        Long menuCatalogId = menuSessionBean.getId();
        if (menuCatalog==null) {
            menuCatalog = menuService.getMenuCatalog(menuCatalogId);
        }

        if (menuCatalog.getCatalogLanguageId()==null) {
            return menuCatalog;
        }

        if (!menuCatalog.getCatalogLanguageId().equals(menuCatalogId)) {
            log.warn("Mismatch menuCatalogId");
            menuCatalog = menuService.getMenuCatalog(menuCatalogId);
        }

        return menuCatalog;
    }

    public void clearMenuItem() {
        this.menuItem=null;
    }

    public MenuItemExtended getMenuItem() {
        if (menuSessionBean.getObjectType()!=menuSessionBean.getMenuItemType()) {
            throw new IllegalStateException("Query menu item info with not menu item type, current type: " + menuSessionBean.getObjectType());
        }
        Long menuItemId = menuSessionBean.getId();
        if (log.isDebugEnabled()) {
            log.debug("menuItemId: " + menuSessionBean.getId());
        }
        if (menuItem==null) {
            menuItem = menuService.getMenuItem(menuItemId);
        }

        if (menuItem.getMenuItem().getCatalogId()==null) {
            return menuItem;
        }

        if (!menuItem.getMenuItem().getCatalogId().equals(menuItemId)) {
            log.warn("Mismatch menuItemId");
            menuItem = menuService.getMenuItem(menuItemId);
        }

        return menuItem;
    }
}
