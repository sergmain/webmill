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



/**

 *

 * $Author$

 *

 * $Id$

 *

 */

package org.riverock.portlet.price;



import java.io.UnsupportedEncodingException;



import org.riverock.portlet.schema.portlet.shop.CurrentBasketType;

import org.riverock.portlet.schema.price.OrderType;

import org.riverock.portlet.portlets.model.OrderLogic;

import org.riverock.portlet.main.Constants;

import org.riverock.generic.tools.StringManager;

import org.riverock.webmill.portlet.PortletParameter;

import org.riverock.webmill.portlet.CtxURL;

import org.riverock.common.tools.ServletTools;

import org.riverock.common.config.ConfigException;



import org.apache.log4j.Logger;



public class ShopBasket

{

    private static Logger log = Logger.getLogger("org.riverock.portlet.price.ShopBasket" );



    public static CurrentBasketType getInstance( OrderType order , StringManager sm, PortletParameter param)

        throws PriceException

    {

        if (order == null || OrderLogic.getCountItem(order) == 0)

            return null;



        try

        {

            CurrentBasketType basket = new CurrentBasketType();

            basket.setCurrentBasketName( sm.getStr("price.invoice") );



            basket.setItemInBasket( new Integer(OrderLogic.getCountItem(order)) );



            basket.setCurrentBasketUrl(

                CtxURL.url(

                    param.getRequest(),

                    param.getResponse(),

                    param.getJspPage(),

                    "mill.invoice"

                ) + '&' +

                Constants.NAME_ID_CURRENCY_SHOP + '=' +

                ServletTools.getInt( param.getRequest(), Constants.NAME_ID_CURRENCY_SHOP ) + '&' +

                Constants.NAME_ID_GROUP_SHOP + '=' +

                ServletTools.getInt( param.getRequest(), Constants.NAME_ID_GROUP_SHOP )

            );

            return basket;

        }

        catch (ConfigException e)

        {

            log.error("Error get configuration", e);

            throw new PriceException(e.getMessage());

        }

        catch (UnsupportedEncodingException e)

        {

            log.error("Error get localized string", e);

            throw new PriceException(e.getMessage());

        }

    }



}