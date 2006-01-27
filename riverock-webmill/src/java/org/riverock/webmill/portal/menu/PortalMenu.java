/*
 * org.riverock.webmill -- Portal framework implementation
 * 
 * Copyright (C) 2004, Riverock Software, All Rights Reserved.
 * 
 * Riverock -- The Open-source Java Development Community
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
 *
 */
package org.riverock.webmill.portal.menu;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import org.apache.log4j.Logger;

import org.riverock.common.collections.TreeUtils;
import org.riverock.interfaces.portlet.menu.Menu;
import org.riverock.interfaces.portlet.menu.MenuItem;
import org.riverock.webmill.container.ContainerConstants;
import org.riverock.webmill.portal.bean.CatalogBean;
import org.riverock.webmill.portal.bean.CatalogLanguageBean;
import org.riverock.webmill.portal.dao.PortalDaoFactory;

/**
 * $Id$
 */
public final class PortalMenu implements Menu {
    private final static Logger log = Logger.getLogger( PortalMenu.class );

    private LinkedList<MenuItem> menuItem = new LinkedList<MenuItem>();
    private boolean isDefault = false;
    private String catalogCode = null;

    public boolean getIsDefault(){
        return isDefault;
    }

    public String getCatalogCode(){
        return catalogCode;
    }

    public MenuItem searchMenuItem(Long id_)
    {
        if (id_==null || menuItem==null)
            return null;

        ListIterator it = menuItem.listIterator();
        while (it.hasNext()) {
            MenuItem ci = (MenuItem)it.next();
            if (ci.getId().equals(id_))
                return ci;

           ci = searchMenuItemInternal( ci.getCatalogItems(), id_);
        }

        return null;
    }

    private MenuItem searchMenuItemInternal(List v, Long id_)
    {
        if (v==null || id_==null)
            return null;

        ListIterator it = v.listIterator();
        while (it.hasNext()) {
            MenuItem ci = (MenuItem)it.next();
            if (ci.getId().equals(id_))
                return ci;

           ci = searchMenuItemInternal( ci.getCatalogItems(), id_);
        }

        return null;
    }

    public List<MenuItem> getMenuItem() {
        return menuItem;
    }

    protected void finalize() throws Throwable
    {
        if (menuItem != null)
        {
            menuItem.clear();
            menuItem = null;
        }
        super.finalize();
    }

    public PortalMenu()
    {
    }

    /**
     *
     * @param v
     * @return return name of template for 'index' page
     */
    private MenuItem getIndexTemplate(List v){
        if (log.isDebugEnabled()) {
            log.debug("list for search index: " + v);
        }
        if (v == null)
            return null;

        Iterator it = v.iterator();
        while (it.hasNext()) {
            MenuItem ctxItem = (MenuItem)it.next();

            if (log.isDebugEnabled()) {
                log.debug("Menu item type: " + ctxItem.getType());
            }
            if (ContainerConstants.CTX_TYPE_INDEX.equals(ctxItem.getType())) {
                return ctxItem;
            }

            ctxItem = getIndexTemplate(ctxItem.getCatalogItems());
            if (ctxItem != null) {
                return ctxItem;
            }
        }
        return null;
    }

    public MenuItem getIndexMenuItem() {
        return getIndexTemplate(menuItem);
    }

    public static boolean checkCurrentThread(List items, MenuItem find)
    {
        if (find == null || items == null)
            return false;

        ListIterator it = items.listIterator();
        while (it.hasNext()) {
            MenuItem ctxItem = (MenuItem)it.next();

            if (ctxItem == find)
                return true;

            if (checkCurrentThread(ctxItem.getCatalogItems(), find))
                return true;
        }
        return false;
    }

    public PortalMenu(CatalogLanguageBean bean) {

        if (bean==null){
            throw new IllegalStateException("Item of menu is null");
        }

        isDefault = bean.getDefault();
        catalogCode = bean.getCatalogCode();

        if (log.isDebugEnabled()) log.debug("#33.70.00 ");

        List<CatalogBean> list = PortalDaoFactory.getPortalDao().getCatalogList( bean.getCatalogLanguageId() );
        Iterator<CatalogBean> iterator = list.iterator();
        while (iterator.hasNext()) {
            CatalogBean catalogBean = iterator.next();
            menuItem.add(new PortalMenuItem( catalogBean ));
        }

        if (log.isDebugEnabled()) log.debug("menuItem size - " + menuItem.size());

        menuItem = TreeUtils.rebuildTree(menuItem);

        if (log.isDebugEnabled()) log.debug("menuItem size - " + menuItem.size());
    }

    public void reinit(){}

}