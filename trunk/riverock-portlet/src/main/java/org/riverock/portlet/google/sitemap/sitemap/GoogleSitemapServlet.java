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
package org.riverock.portlet.google.sitemap;

import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;

import org.apache.myfaces.custom.tree2.TreeNodeBase;

import org.riverock.interfaces.portal.bean.SiteLanguage;
import org.riverock.interfaces.portal.bean.CatalogLanguageItem;
import org.riverock.portlet.tools.FacesTools;
import org.riverock.portlet.manager.menu.bean.SiteLanguageBean;
import org.riverock.webmill.container.ContainerConstants;

/**
 * @author Sergei Maslyukov
 *         Date: 19.09.2006
 *         Time: 15:31:15
 *         <p/>
 *         $Id$
 */
public class GoogleSitemapServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        Long siteId = new Long( request.getPortalContext().getProperty( ContainerConstants.PORTAL_PROP_SITE_ID ) );

        FacesTools.getPortalDaoProvider().getPortalSiteLanguageDao().getSiteLanguageList(siteId);
        for (SiteLanguage siteLanguage : menuService.getSiteLanguageList(site.getSiteId())) {
            TreeNodeBase siteLanguageNode = new TreeNodeBase(
                "site-language",
                siteLanguage.getNameCustomLanguage() + " (" + siteLanguage.getCustomLanguage() + ")",
                siteLanguage.getSiteLanguageId().toString(),
                false);
            treeRoot.getChildren().add(siteLanguageNode);

            TreeNodeBase menuCatalogListNode = new TreeNodeBase("menu-catalog-list", "Menu catalog list", siteLanguage.getSiteLanguageId().toString(), false);
            siteLanguageNode.getChildren().add(menuCatalogListNode);

            for (CatalogLanguageItem catalogLanguageItem : menuService.getMenuCatalogList(siteLanguage.getSiteLanguageId())) {
                TreeNodeBase menuCatalogNode = new TreeNodeBase(
                    "menu-catalog",
                    catalogLanguageItem.getCatalogCode(),
                    catalogLanguageItem.getCatalogLanguageId().toString(),
                    false);
                menuCatalogListNode.getChildren().add(menuCatalogNode);

                processMenuItem(menuCatalogNode, menuService.getMenuItemList(catalogLanguageItem.getCatalogLanguageId()));
            }

        }

    }

}
