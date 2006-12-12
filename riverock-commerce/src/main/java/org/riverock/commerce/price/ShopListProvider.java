/*
 * org.riverock.commerce - Commerce application
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
package org.riverock.commerce.price;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import org.riverock.commerce.bean.ClassQueryItemImpl;
import org.riverock.commerce.bean.Shop;
import org.riverock.commerce.dao.CommerceDaoFactory;
import org.riverock.common.tools.StringTools;
import org.riverock.interfaces.portal.dao.PortalDaoProvider;
import org.riverock.interfaces.portlet.member.ClassQueryItem;
import org.riverock.interfaces.portlet.member.PortletGetList;

/**
 * User: SergeMaslyukov
 * Date: 14.12.2004
 * Time: 13:27:22
 * $Id$
 * <p/>
 * This class used for select shop and bind it to menu item
 */
@SuppressWarnings({"UnusedAssignment"})
public class ShopListProvider implements PortletGetList {

    private static Logger log = Logger.getLogger( ShopPage.class );

    private PortalDaoProvider provider=null;

    public void setPortalDaoProvider(PortalDaoProvider provider) {
        this.provider=provider;
    }

    public List<ClassQueryItem> getList( Long idSiteCtxLangCatalog, Long idContext ) {

        if (log.isDebugEnabled()) {
            log.debug( "Get list of Shop. idSiteCtxLangCatalog - " + idSiteCtxLangCatalog );
        }
        Long siteId = provider.getPortalCatalogDao().getSiteId(idSiteCtxLangCatalog);
        List<Shop> shops = CommerceDaoFactory.getShopDao().getShopList(siteId);
        List<ClassQueryItem> v = new ArrayList<ClassQueryItem>();
        for (Shop shop : shops) {
            Long id = shop.getShopId();
            String name = "" + id + ", " + shop.getShopCode() + ", " + shop.getShopName();

            ClassQueryItem item = new ClassQueryItemImpl( id, StringTools.truncateString( name, 60 ) );
            if (idContext!=null && idContext.equals( item.getIndex() )) {
                item.setSelected(true);
            }

            v.add( item );
        }
        return v;
    }

}
