/*

 * org.riverock.portlet -- Portlet Library

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



package org.riverock.portlet.port.menu;



import java.sql.PreparedStatement;

import java.sql.ResultSet;

import java.util.ArrayList;

import java.util.List;



import org.riverock.common.tools.RsetTools;

import org.riverock.generic.db.DatabaseAdapter;

import org.riverock.generic.db.DatabaseManager;

import org.riverock.generic.port.LocalizedString;

import org.riverock.webmill.portal.menu.MenuInterface;

import org.riverock.webmill.portal.menu.MenuItem;

import org.riverock.webmill.portal.menu.MenuItemInterface;

import org.riverock.portlet.schema.core.SiteCtxLangCatalogItemType;



import org.apache.log4j.Logger;



/**

 * $Id$

 */

public class Menu implements MenuInterface

{

    private static Logger log = Logger.getLogger( Menu.class );



    private SiteCtxLangCatalogItemType item;

//    public final static String DEFAULT_MENU_CODE = "DEFAULT";

//    private boolean isDefault = false;



//    public MenuItem[] ctxArray = new MenuItem[0];    // это плоская модель меню - как в базе

    private List menuItem = new ArrayList(2);           // здесь находится дерево меню



    public MenuItemInterface searchMenuItem(Long id_)

    {

        if (id_==null)

            return null;



        if (log.isDebugEnabled())

            log.debug("Menu size - " + getMenuItemCount());



        for (int j = 0; j < getMenuItemCount(); j++)

        {

            MenuItemInterface ci = getMenuItem(j);

            if (ci.getId() == id_)

                return ci;



           ci = searchMenuItemInternal( ci.getCatalogItems(), id_);

        }



        return null;

    }



    private MenuItemInterface searchMenuItemInternal(List v, Long id_)

    {

        if (log.isDebugEnabled())

            log.debug("Menu size - " + getMenuItemCount());



        for (int j = 0; j<v.size(); j++)

        {

            MenuItemInterface ci = (MenuItemInterface)v.get(j);

            if (ci.getId() == id_)

                return ci;



           ci = searchMenuItemInternal( ci.getCatalogItems(), id_);

        }



        return null;

    }



    public int getMenuItemCount()

    {

        if (menuItem==null)

            return 0;



        return menuItem.size();

    }



    public List getMenuItem()

    {

        return menuItem;

    }



    public MenuItemInterface getMenuItem(int index)

            throws java.lang.IndexOutOfBoundsException

    {

        if ((index < 0) || (index > menuItem.size())) {

            throw new IndexOutOfBoundsException();

        }



         if (menuItem==null)

            return null;



        return (MenuItemInterface)menuItem.get(index);

    }



    protected void finalize() throws Throwable

    {

        if (menuItem != null)

        {

            menuItem.clear();

            menuItem = null;

        }

//        ctxArray = null;



        super.finalize();

    }



    public Menu()

    {

    }



    public boolean getIsDefault()

    {

        if (item==null)

            return false;



        return Boolean.TRUE.equals( item.getIsDefault() );

    }



    public String getCatalogCode()

    {

        if (item==null)

            return null;



        return item.getCatalogCode();

    }



// Возвращает объект MenuItem с индексом id_ctx

//    public MenuItem getCatalogItem(long id_ctx)

//    {

//        if (log.isDebugEnabled())

//            log.debug("Menu size - " + ctxArray.length);

//

//        for (int j = 0; j < ctxArray.length; j++)

//        {

//            MenuItem ci = ctxArray[j];

//            if (ci.id == id_ctx)

//                return ci;

//        }

//

//        return null;

//    }



// Возвращает ссылку на MenuItem указывающий на index page

    private String getIndexTemplate(List v)

    {

        if (v == null)

            return null;



        for (int j = 0; j < v.size(); j++)

        {

            MenuItemInterface ctxItem =

                (MenuItemInterface) v.get(j);



            if (org.riverock.webmill.main.Constants.CTX_TYPE_INDEX.equals(ctxItem.getType()))

                return ctxItem.getNameTemplate();



            String s = getIndexTemplate(ctxItem.getCatalogItems());

            if (s != null)

                return s;

        }

        return null;

    }



    public String getIndexTemplate()

    {

        return getIndexTemplate(menuItem);

    }



//    private boolean checkShopContext(Vector v)

//    {

//        if (v == null)

//            return false;

//

//        for (int j = 0; j < v.size(); j++)

//        {

//            MenuItem ctxItem =

//                (MenuItem) v.elementAt(j);

//

//            if (Constants.CTX_TYPE_SHOP.equals(ctxItem.type))

//                return true;

//

//            if (checkShopContext(ctxItem.catalogItems))

//                return true;

//        }

//        return false;

//    }



//    public boolean checkShopContext()

//    {

//        return checkShopContext(menuItem);

//    }



    public static boolean checkCurrentTread(List items, MenuItemInterface find)

    {

        if (find == null || items == null)

            return false;



        for (int j = 0; j < items.size(); j++)

        {

            MenuItemInterface ctxItem =

                (MenuItemInterface) items.get(j);



            if (ctxItem == find)

                return true;



            if (checkCurrentTread(ctxItem.getCatalogItems(), find))

                return true;

        }

        return false;

    }



//    public boolean isCheckThread(MenuItem find)

//    {

//        return checkCurrentTread(menuItem, find);

//    }



    static String sql_ = null;

    static

    {

        sql_ =

            "select a.ID_SITE_CTX_CATALOG, a.ID_TOP_CTX_CATALOG, c.TYPE, a.ID_CONTEXT, " +

            "       a.IS_USE_PROPERTIES, a.KEY_MESSAGE, a.STORAGE , " +

            "       d.NAME_SITE_TEMPLATE, a.CTX_PAGE_URL " +

            "from   SITE_CTX_CATALOG  a, SITE_CTX_TYPE c , SITE_TEMPLATE d  " +

            "where  a.ID_SITE_CTX_LANG_CATALOG=? and  " +

            "       a.ID_SITE_CTX_TYPE=c.ID_SITE_CTX_TYPE and " +

            "       a.ID_SITE_TEMPLATE=d.ID_SITE_TEMPLATE " +

            "order by a.ID_TOP_CTX_CATALOG ASC, a.ORDER_FIELD ASC ";



        try

        {

            MenuInterface obj = new Menu();

            org.riverock.sql.cache.SqlStatement.registerSql( sql_, obj.getClass() );

        }

        catch(Exception e)

        {

            log.error("Exception in registerSql, sql\n"+sql_, e);

        }

        catch(Error e)

        {

            log.error("Error in registerSql, sql\n"+sql_, e);

        }

    }



    public Menu(DatabaseAdapter db_, SiteCtxLangCatalogItemType item_)

        throws Exception

    {

        this.item = item_;



        if (log.isDebugEnabled())

            log.debug("#33.70.00 ");



        PreparedStatement ps = null;

        ResultSet rs = null;

        try

        {

            ps = db_.prepareStatement(sql_);

            ps.setObject(1, this.item.getIdSiteCtxLangCatalog() );



            rs = ps.executeQuery();



            while (rs.next())

            {

                menuItem.add(

                    new MenuItem(

                        RsetTools.getLong(rs, "ID_SITE_CTX_CATALOG"),

                        RsetTools.getLong(rs, "ID_TOP_CTX_CATALOG"),

                        RsetTools.getLong(rs, "ID_CONTEXT"),

                        RsetTools.getString(rs, "TYPE"),

                        new LocalizedString(rs),

                        RsetTools.getString(rs, "NAME_SITE_TEMPLATE"),

                        RsetTools.getString(rs, "CTX_PAGE_URL") )

                );

            }

        }

        catch(Exception e)

        {

            log.error("Exception in Menu(....", e);

            throw e;

        }

        catch(Error e)

        {

            log.error("Error in Menu(....", e);

            throw e;

        }

        finally

        {

            DatabaseManager.close(rs, ps);

            rs = null;

            ps = null;

        }



        if (log.isDebugEnabled())

            log.debug("menuItem size - " + menuItem.size());



        rebuildCatalog();



        if (log.isDebugEnabled())

            log.debug("menuItem size - " + menuItem.size());



    }



    private void rebuildCatalog()

    {

//        Object obj[] = menuItem.toArray();

//        ctxArray = new MenuItem[obj.length];

//        for (int i = 0; i < obj.length; i++)

//        {

//            ctxArray[i] = (MenuItem) obj[i];

//        }

//        obj = null;



        ArrayList result = null;

        ArrayList v = new ArrayList(menuItem.size());



        Long id = new Long(0);



        if (log.isDebugEnabled())

            log.debug("Before rebuid. Size of menuItem " + menuItem.size());



        while (menuItem.size() > 0)

        {

            if (log.isDebugEnabled())

                log.debug("#1.01.01 menuItem.size() - " + menuItem.size() + "; ID -  " + id);



            for (int i = 0; i < menuItem.size(); i++)

            {

                MenuItemInterface item = (MenuItemInterface) menuItem.get(i);

                if ( id.equals( item.getIdTop() ) )

                    v.add(item);

            }



            if (log.isDebugEnabled())

            {

                log.debug("#1.01.02 v.size() - " + v.size());



                if (result != null)

                {

                    log.debug("#1.01.05 result.size() -  " + result.size());

                }

            }



            if (result == null)

            {

                if (log.isDebugEnabled())

                    log.debug("Init result Vector");



                result = (ArrayList) v.clone();



                if (log.isDebugEnabled())

                    log.debug("Finish init result Vector. result.size() - " + result.size());

            }

            else

            {

                putList(result, v, id);

            }



            if (log.isDebugEnabled())

                log.debug("#1.01.07 result.size() - " + result.size());



            menuItem.removeAll(v);



            if (log.isDebugEnabled())

                log.debug("#1.01.09 menuItem.size() - " + menuItem.size());



            v.clear();

            if (log.isDebugEnabled())

            {

                log.debug("#1.01.11 ctx.List.size() - " + menuItem.size());

                log.debug("#1.01.13 result.size() - " + result.size());

            }



            if (menuItem.size() > 0)

                id = ((MenuItemInterface) menuItem.get(0)).getIdTop();

        }

        menuItem = null;

        if (result != null)

            menuItem = result;

        else

            menuItem = new ArrayList(0);



        if (log.isDebugEnabled())

            log.debug("After rebuid. Size of menuItem " + menuItem.size());



    }



    private boolean putList(List target, List data, Long id)

    {

        if (log.isDebugEnabled())

            log.debug("Start putList(). target.size() - " + target.size() + " data.size() " + data.size());



        try

        {

            for (int i = 0; i < target.size(); i++)

            {

                MenuItemInterface item = (MenuItemInterface) target.get(i);

                if (item.getId() == id)

                {

                    item.setCatalogItems((ArrayList) ((ArrayList)data).clone() );

                    return true;

                }

                else

                {

                    if (putList(item.getCatalogItems(), data, id))

                        return true;

                }



            }

            return false;

        } finally

        {

            if (log.isDebugEnabled())

                log.debug("End putList(). target.size() - " + target.size() + " data.size() " + data.size());

        }

    }



    public void reinit(){}



}