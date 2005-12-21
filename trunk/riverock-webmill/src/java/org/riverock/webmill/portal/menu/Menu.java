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

import org.apache.log4j.Logger;
import org.riverock.common.collections.TreeUtils;
import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.db.DatabaseManager;
import org.riverock.interfaces.portlet.menu.MenuInterface;
import org.riverock.interfaces.portlet.menu.MenuItemInterface;
import org.riverock.webmill.core.GetWmPortalCatalogWithIdSiteCtxLangCatalogList;
import org.riverock.webmill.exception.PortalException;

import org.riverock.webmill.schema.core.WmPortalCatalogItemType;
import org.riverock.webmill.schema.core.WmPortalCatalogListType;
import org.riverock.webmill.schema.core.WmPortalCatalogLanguageItemType;
import org.riverock.webmill.container.ContainerConstants;
import org.riverock.sql.cache.SqlStatement;
import org.riverock.sql.cache.SqlStatementRegisterException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;

/**
 * $Id$
 */
public final class Menu implements MenuInterface {
    private final static Logger log = Logger.getLogger( Menu.class );

    static{
        Class c = Menu.class;
        try{
            SqlStatement.registerRelateClass( c, GetWmPortalCatalogWithIdSiteCtxLangCatalogList.class );
        }
        catch( Exception exception ) {
            final String es = "Exception in SqlStatement.registerRelateClass()";
            log.error( es, exception );
            throw new SqlStatementRegisterException( es, exception );
        }
    }

    private List menuItem = new LinkedList();           // contains tree of menu
    private boolean isDefault = false;
    private String catalogCode = null;

    public boolean getIsDefault(){
        return isDefault;
    }

    public String getCatalogCode(){
        return catalogCode;
    }

    public MenuItemInterface searchMenuItem(Long id_)
    {
        if (id_==null || menuItem==null)
            return null;

        ListIterator it = menuItem.listIterator();
        while (it.hasNext()) {
            MenuItemInterface ci = (MenuItemInterface)it.next();
            if (ci.getId().equals(id_))
                return ci;

           ci = searchMenuItemInternal( ci.getCatalogItems(), id_);
        }

        return null;
    }

    private MenuItemInterface searchMenuItemInternal(List v, Long id_)
    {
        if (v==null || id_==null)
            return null;

        ListIterator it = v.listIterator();
        while (it.hasNext()) {
            MenuItemInterface ci = (MenuItemInterface)it.next();
            if (ci.getId().equals(id_))
                return ci;

           ci = searchMenuItemInternal( ci.getCatalogItems(), id_);
        }

        return null;
    }

    public List getMenuItem() {
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

    public Menu()
    {
    }

    /**
     *
     * @param v
     * @return return name of template for 'index' page
     */
    private MenuItemInterface getIndexTemplate(List v){
        if (log.isDebugEnabled()) {
            log.debug("list for search index: " + v);
        }
        if (v == null)
            return null;

        Iterator it = v.iterator();
        while (it.hasNext()) {
            MenuItemInterface ctxItem = (MenuItemInterface)it.next();

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

    public MenuItemInterface getIndexMenuItem() {
        return getIndexTemplate(menuItem);
    }


    public static boolean checkCurrentThread(List items, MenuItemInterface find)
    {
        if (find == null || items == null)
            return false;

        ListIterator it = items.listIterator();
        while (it.hasNext()) {
            MenuItemInterface ctxItem = (MenuItemInterface)it.next();

            if (ctxItem == find)
                return true;

            if (checkCurrentThread(ctxItem.getCatalogItems(), find))
                return true;
        }
        return false;
    }

    public Menu(DatabaseAdapter db_, WmPortalCatalogLanguageItemType item_) throws Exception {

        if (item_==null){
            throw new PortalException("Item of menu is null");
        }

        isDefault = item_.getIsDefault();
        catalogCode = item_.getCatalogCode();

        if (log.isDebugEnabled()) log.debug("#33.70.00 ");

        PreparedStatement ps = null;
        ResultSet rs = null;
        try{
            WmPortalCatalogListType catalogList =
                    GetWmPortalCatalogWithIdSiteCtxLangCatalogList.getInstance(db_, item_.getIdSiteCtxLangCatalog()).item;

            List list = catalogList.getWmPortalCatalogAsReference();
            Collections.sort(list, new MenuItemComparator());

            Iterator it = list.iterator();
            while (it.hasNext()){
                WmPortalCatalogItemType item = (WmPortalCatalogItemType)it.next();

                // Dont include menuitem with id_template==null to menu
                if (item.getIdSiteTemplate()!=null)
                    menuItem.add(new MenuItem(db_, item));
            }
        }
        catch(Throwable e) {
            final String es = "Exception in Menu(....";
            log.error(es, e);
            throw new PortalException( es, e );
        }
        finally {
            DatabaseManager.close(rs, ps);
            rs = null;
            ps = null;
        }

        if (log.isDebugEnabled()) log.debug("menuItem size - " + menuItem.size());

        menuItem = TreeUtils.rebuildTree((LinkedList)menuItem);

        if (log.isDebugEnabled()) log.debug("menuItem size - " + menuItem.size());
    }

    public void reinit(){}


    private class MenuItemComparator implements Comparator {
        public int compare(Object o1, Object o2) {

            if (o1==null && o2==null)
                return 0;
            if (o1==null)
                return 1;
            if (o2==null)
                return -1;

            // "order by a.ID_TOP_CTX_CATALOG ASC, a.ORDER_FIELD ASC ";
            if (((WmPortalCatalogItemType)o1).getIdTopCtxCatalog().equals(((WmPortalCatalogItemType)o2).getIdTopCtxCatalog()))
            {
                if (((WmPortalCatalogItemType)o1).getOrderField()==null &&
                        ((WmPortalCatalogItemType)o2).getOrderField()==null)
                    return 0;

                if (((WmPortalCatalogItemType)o1).getOrderField()!=null &&
                        ((WmPortalCatalogItemType)o2).getOrderField()==null)
                    return -1;

                if (((WmPortalCatalogItemType)o1).getOrderField()==null &&
                        ((WmPortalCatalogItemType)o2).getOrderField()!=null)
                    return 1;

                return ((WmPortalCatalogItemType)o1).getOrderField().compareTo(((WmPortalCatalogItemType)o2).getOrderField());
            }
            else
                return ((WmPortalCatalogItemType)o1).getIdTopCtxCatalog().compareTo(((WmPortalCatalogItemType)o2).getIdTopCtxCatalog());
        }
    }
}