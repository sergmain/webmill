/*
 * org.riverock.commerce - Commerce application
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
package org.riverock.commerce.price;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.portlet.RenderResponse;
import javax.portlet.PortletURL;




import org.apache.log4j.Logger;

import org.riverock.common.tools.RsetTools;
import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.db.DatabaseManager;

import org.riverock.portlet.schema.portlet.shop.PositionItemType;
import org.riverock.portlet.schema.portlet.shop.PricePositionType;
import org.riverock.webmill.container.ContainerConstants;

/**
 * Построение иерархии категорий товаров начиная от текущей и до корня вверх
 *
 * $Id$
 *
 */
public class PriceListPosition {
    private static Logger log = Logger.getLogger( PriceListPosition.class );

    public static PricePositionType getInstance(DatabaseAdapter db_, ShopPageParam shopParam_, RenderResponse renderResponse)
        throws PriceException
    {

        if (log.isDebugEnabled())
            log.debug("idGroup "+shopParam_.id_group);

        PricePositionType position = new PricePositionType();
        if (shopParam_.id_group==null)
            return null;

        String sql_ = null;
        PreparedStatement st = null;
        ResultSet rs = null;

        Long id_top = shopParam_.id_group;
        Long id_curr = null;

        try {

            sql_ =
                "select ID, ID_MAIN, ITEM " +
                "from   WM_PRICE_LIST " +
                "where  ID=? and ID_SHOP=? and ABSOLETE=0 and IS_GROUP=1";

            while (true)
            {
                st = db_.prepareStatement(sql_);
                RsetTools.setLong(st, 1, id_top);
                RsetTools.setLong(st, 2, shopParam_.id_shop);
                rs = st.executeQuery();

                if (rs.next())
                {
                    id_curr = RsetTools.getLong(rs, "ID");
                    id_top = RsetTools.getLong(rs, "ID_MAIN");

                    PortletURL portletURL = renderResponse.createRenderURL();

                    portletURL.setParameter(ContainerConstants.NAME_TYPE_CONTEXT_PARAM, ShopPortlet.CTX_TYPE_SHOP);
                    portletURL.setParameter(ShopPortlet.NAME_ID_GROUP_SHOP, id_curr.toString());
                    portletURL.setParameters(shopParam_.currencyURL);
                    portletURL.setParameter(ShopPortlet.NAME_ID_SHOP_PARAM, shopParam_.id_shop.toString() );

                    if (shopParam_.sortBy != null){
                        portletURL.setParameter(ShopPortlet.NAME_SHOP_SORT_BY, shopParam_.sortBy);
                        portletURL.setParameter(ShopPortlet.NAME_SHOP_SORT_DIRECT, ""+shopParam_.sortDirect);
                    }
                    PositionItemType positionItem = new PositionItemType();

                    positionItem.setPositionName( RsetTools.getString(rs, "ITEM") );
                    positionItem.setPositionUrl( portletURL.toString() );
                    positionItem.setIdGroupCurrent(id_curr);
                    positionItem.setIdGroupTop(id_top);

                    position.addPositionItem(positionItem);
                }
                else
                    break;

                DatabaseManager.close(rs, st);
                rs = null;
                st = null;
            }
        }
        catch (Exception e) {
            final String es = "Error build pricelist position";
            log.error(es, e);
            throw new PriceException(es, e);
        }
        finally {
            DatabaseManager.close(rs, st);
            rs = null;
            st = null;
        }


        if (position.getPositionItemCount() == 0)
            return null;

        List<PositionItemType> v = new ArrayList<PositionItemType>( position.getPositionItemCount() );

        if (log.isDebugEnabled())
            log.debug("Count of position  - " + position.getPositionItemCount());

        Long id = 0l;
        int maxLoop = 100;
        while (true || --maxLoop>0) {
            for (int i = 0; i < position.getPositionItemCount(); i++) {
                PositionItemType item = position.getPositionItem(i);

                if (log.isDebugEnabled())
                    log.debug("Position id_curr - " + item.getIdGroupCurrent() + " id_top " + item.getIdGroupTop());

                if (item.getIdGroupTop().equals( id )) {
                    v.add(item);
                    id = item.getIdGroupCurrent();
                }

                item = null;
            }
            if (shopParam_.id_group.equals(id) )
                break;
        }
        if (maxLoop==0)
            throw new PriceException("Error build price position, max loop count processed");

        position.setPositionItem( (ArrayList)v );
        v = null;

        PortletURL portletURL = renderResponse.createRenderURL();

        portletURL.setParameter(ContainerConstants.NAME_TYPE_CONTEXT_PARAM, ShopPortlet.CTX_TYPE_SHOP);
        portletURL.setParameter(ShopPortlet.NAME_ID_GROUP_SHOP, "0");
        portletURL.setParameters(shopParam_.currencyURL);
        portletURL.setParameter(ShopPortlet.NAME_ID_SHOP_PARAM, shopParam_.id_shop.toString());

        if (shopParam_.sortBy != null) {
            portletURL.setParameter(ShopPortlet.NAME_SHOP_SORT_BY, shopParam_.sortBy);
            portletURL.setParameter(ShopPortlet.NAME_SHOP_SORT_DIRECT, ""+shopParam_.sortDirect);
        }
        position.setTopLevelUrl( portletURL.toString() );

        return position;
    }
}