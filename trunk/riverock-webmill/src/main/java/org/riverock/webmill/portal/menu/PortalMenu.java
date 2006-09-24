/*
 * org.riverock.webmill - Portal framework implementation
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
package org.riverock.webmill.portal.menu;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import org.riverock.common.collections.TreeUtils;
import org.riverock.interfaces.common.TreeItem;
import org.riverock.interfaces.portal.bean.CatalogItem;
import org.riverock.interfaces.portal.bean.CatalogLanguageItem;
import org.riverock.interfaces.portlet.menu.Menu;
import org.riverock.interfaces.portlet.menu.MenuItem;
import org.riverock.webmill.container.ContainerConstants;
import org.riverock.webmill.portal.dao.InternalDaoFactory;

/**
 * $Id$
 */
public final class PortalMenu implements Menu {
    private final static Logger log = Logger.getLogger( PortalMenu.class );

    private List<MenuItem> menuItem = new ArrayList<MenuItem>();
    private boolean isDefault = false;
    private String catalogCode = null;

    public boolean getIsDefault(){
        return isDefault;
    }

    public String getCatalogCode(){
        return catalogCode;
    }

    public MenuItem searchMenuItem(Long id_) {
        if (id_==null || menuItem==null)
            return null;

        for (MenuItem ci : menuItem) {
            if (ci.getId().equals(id_))
                return ci;

            ci = searchMenuItemInternal( ci.getCatalogItems(), id_);
            if (ci!=null) {
                return ci;
            }
        }

        return null;
    }

    private MenuItem searchMenuItemInternal(List<MenuItem> v, Long id_) {
        if (v==null || id_==null)
            return null;

        for (MenuItem ci : v) {
            if (ci.getId().equals(id_))
                return ci;

            ci = searchMenuItemInternal( ci.getCatalogItems(), id_);
            if (ci!=null) {
                return ci;
            }
        }

        return null;
    }

    public List<MenuItem> getMenuItem() {
        return menuItem;
    }

    protected void finalize() throws Throwable {
        if (menuItem != null) {
            menuItem.clear();
            menuItem = null;
        }
        super.finalize();
    }

    public PortalMenu() {
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

        for (Object o : v) {
            MenuItem ctxItem = (MenuItem)o;

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

    public static boolean checkCurrentThread(List items, MenuItem find) {
        if (find == null || items == null)
            return false;

        for (Object o : items) {
            MenuItem ctxItem = (MenuItem)o;

            if (ctxItem == find)
                return true;

            if (checkCurrentThread(ctxItem.getCatalogItems(), find))
                return true;
        }
        return false;
    }

    public PortalMenu(CatalogLanguageItem bean) {

        if (bean==null){
            throw new IllegalStateException("Item of menu is null");
        }

        isDefault = bean.getDefault();
        catalogCode = bean.getCatalogCode();

        if (log.isDebugEnabled()) log.debug("#33.70.00 ");

        List<CatalogItem> list = InternalDaoFactory.getInternalCatalogDao().getCatalogItemList( bean.getCatalogLanguageId() );
        for (CatalogItem catalogBean : list) {
            // Dont include menuitem with id_template==null to menu
            if (catalogBean.getTemplateId()==null) {
                continue;
            }
            menuItem.add(new PortalMenuItem(catalogBean));
        }

        if (log.isDebugEnabled()) log.debug("menuItem size - " + menuItem.size());

        menuItem = (List<MenuItem>)(List)TreeUtils.rebuildTree((List<TreeItem>)((List)menuItem));

        if (log.isDebugEnabled()) log.debug("menuItem size - " + menuItem.size());
    }

    public void reinit(){}

}