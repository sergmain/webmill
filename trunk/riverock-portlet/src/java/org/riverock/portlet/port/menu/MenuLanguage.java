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



import org.riverock.generic.db.DatabaseAdapter;

import org.riverock.sql.cache.SqlStatement;

import org.riverock.portlet.schema.core.SiteSupportLanguageItemType;

import org.riverock.portlet.schema.core.SiteCtxLangCatalogItemType;

import org.riverock.portlet.schema.core.SiteCtxLangCatalogListType;

import org.riverock.portlet.core.GetSiteCtxLangCatalogWithIdSiteSupportLanguageList;

import org.riverock.webmill.portal.menu.MenuItemInterface;

import org.riverock.webmill.portal.menu.MenuInterface;

import org.riverock.webmill.portal.menu.MenuLanguageInterface;



import org.apache.log4j.Logger;



import java.util.Vector;



/**

 * $Id$

 */

public class MenuLanguage implements MenuLanguageInterface

{

    private static Logger log = Logger.getLogger( "org.riverock.port.menu.MenuLanguage" );



    static

    {

        Menu objMain = new Menu();

        MenuLanguage objTarget = new MenuLanguage();

        try

        {

            SqlStatement.registerRelateClass( objTarget.getClass(), objMain.getClass() );

        }

        catch(Exception exc)

        {

            log.error("Exception in SqlStatement.registerRelateClass()", exc);

        }

        objMain = null;

        objTarget = null;

    }



    private Vector menu = new Vector();  // vector of Menu

    private SiteSupportLanguageItemType item = null;



    public MenuLanguage(){}



    public int getMenuCount()

    {

        if (menu==null)

            return 0;



        return menu.size();

    }



    public MenuInterface getMenu(int index)

            throws java.lang.IndexOutOfBoundsException

    {

        if ((index < 0) || (index > menu.size())) {

            throw new IndexOutOfBoundsException();

        }



         if (menu==null)

            return null;



        return (Menu)menu.elementAt(index);

    }



// ¬озвращает ссылку на MenuItem указывающий на index page

    public String getIndexTemplate()

    {

        if (log.isDebugEnabled())

        {

            log.debug("menu - "+menu);

            log.debug( "getMenuCounte() - "+getMenuCount() );

        }



        for (int i = 0; i < menu.size(); i++)

        {

            Menu catalog = (Menu) menu.elementAt(i);

            String indexTemplate = catalog.getIndexTemplate();

            if (indexTemplate != null)

                return indexTemplate;

        }

        return null;

    }



    public MenuInterface getDefault()

    {

        for (int i = 0; i < menu.size(); i++)

        {

            Menu catalog = (Menu) menu.elementAt(i);

            if (catalog.getIsDefault())

                return catalog;

        }

        return null;

    }



    public MenuInterface getCatalogByCode( String code )

    {

        if (code==null)

            return null;



        for (int i = 0; i < menu.size(); i++)

        {

            Menu catalog = (Menu) menu.elementAt(i);

            if (code.equals( catalog.getCatalogCode()) )

                return catalog;

        }

        return null;

    }



    public MenuItemInterface searchMenuItem(Long id)

    {

        for (int i = 0; i < getMenuCount(); i++)

        {

            MenuInterface menuTemp = getMenu(i);

            MenuItemInterface ci = menuTemp.searchMenuItem(id);

            if (ci!=null)

                return ci;

        }

        return null;

    }



/*

    static String sql_ = null;

    static

    {

        sql_ =

            "select a.ID_SITE_CTX_LANG_CATALOG , a.CATALOG_CODE , a.IS_DEFAULT " +

            "from SITE_CTX_LANG_CATALOG  a  " +

            "where a.ID_SITE_SUPPORT_LANGUAGE=?   "+

            "order by a.IS_DEFAULT desc";



        try

        {

            MenuLanguage objTarget = new MenuLanguage();

            org.riverock.sql.cache.SqlStatement.registerSql( sql_, objTarget.getClass() );

            objTarget = null;

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

*/



    public MenuLanguage(DatabaseAdapter db_, SiteSupportLanguageItemType item_)

//        long idSiteSupportLanguage_,

//        String locale_,            CUSTOM_LANGUAGE

//            String lang_)          NAME_CUSTOM_LANGUAGE

        throws Exception

    {

        if (item_==null)

            return;



        this.item = item_;



        if (log.isDebugEnabled())

        {

            log.debug("#33.07.00 ");

            log.debug("ID_SITE_SUPPORT_LANGUAGE -  " + item_.getIdSiteSupportLanguage());

            log.debug("locale - " + getLocaleStr());

            log.debug("language - " + getLang());

        }



//        PreparedStatement ps = null;

//        ResultSet rs = null;

        try

        {

            SiteCtxLangCatalogListType list =

                    GetSiteCtxLangCatalogWithIdSiteSupportLanguageList.

                    getInstance(db_, item_.getIdSiteSupportLanguage())

                    .item;



//            ps = db_.prepareStatement(sql_);

//            RsetTools.setLong(ps, 1, item_.getIdSiteSupportLanguage());

//

//            rs = ps.executeQuery();



//            while (rs.next())

            for (int i=0; i<list.getSiteCtxLangCatalogCount(); i++)

            {

                SiteCtxLangCatalogItemType ic = list.getSiteCtxLangCatalog(i);



//                long id = RsetTools.getLong(rs, "ID_SITE_CTX_LANG_CATALOG");

//                String code = RsetTools.getString(rs, "CATALOG_CODE");

                MenuInterface catalog = new Menu(db_, ic );



//                catalog.isDefault = (RsetTools.getInt(rs, "IS_DEFAULT")==1?true:false);

                menu.add(catalog);

            }

        }

        catch(Exception e)

        {

            log.error("Exception in MenuLanguage(....", e);

            throw e;

        }

        catch(Error e)

        {

            log.error("Error in MenuLanguage(....", e);

            throw e;

        }

//        finally

//        {

//            DatabaseManager.close(rs, ps);

//            rs = null;

//            ps = null;

//        }

    }



    public String getLocaleStr()

    {

        if (item==null)

            return null;



        return item.getCustomLanguage();

    }



    public String getLang()

    {

        if (item==null)

            return null;



        return item.getNameCustomLanguage();

    }



    public void reinit(){}



}

