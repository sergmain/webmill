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

package org.riverock.portlet.portlets;



import java.sql.PreparedStatement;

import java.sql.ResultSet;

import java.util.Vector;

import java.util.List;

import java.util.ArrayList;

import java.io.FileWriter;



import javax.servlet.http.HttpSession;



import org.apache.log4j.Logger;

import org.exolab.castor.xml.Marshaller;



import org.riverock.generic.db.DatabaseAdapter;

import org.riverock.generic.db.DatabaseManager;

import org.riverock.portlet.price.PriceCurrency;

import org.riverock.portlet.price.PriceGroup;

import org.riverock.portlet.price.PriceListItemList;

import org.riverock.portlet.price.PriceListPosition;

import org.riverock.portlet.price.Shop;

import org.riverock.portlet.price.ShopBasket;

import org.riverock.portlet.schema.portlet.shop.ShopPageType;

import org.riverock.portlet.schema.price.OrderType;

import org.riverock.portlet.schema.price.CurrencyPrecisionType;

import org.riverock.portlet.main.Constants;

import org.riverock.portlet.member.ClassQueryItem;

import org.riverock.common.tools.DateTools;

import org.riverock.common.tools.RsetTools;

import org.riverock.common.tools.ServletTools;

import org.riverock.common.tools.StringTools;

import org.riverock.generic.tools.XmlTools;

import org.riverock.webmill.portlet.Portlet;

import org.riverock.webmill.portlet.PortletResultObject;

import org.riverock.webmill.portlet.PortletGetList;

import org.riverock.webmill.portlet.PortletParameter;

import org.riverock.webmill.portlet.CtxURL;

import org.riverock.webmill.utils.ServletUtils;

import org.riverock.webmill.schema.site.SitePortletDataListType;

import org.riverock.webmill.config.WebmillConfig;



public class ShopPagePortlet implements Portlet, PortletResultObject, PortletGetList

{

    private static Logger cat = Logger.getLogger(ShopPagePortlet.class);



    private ShopPageType shopPage = new ShopPageType();;



    private Shop shop = null;

    private PortletParameter param = null;

    private String sortItemUrl = "";

    private String itemDirect = "ASC";

    private String sortPriceUrl = "";

    private String priceDirect = "ASC";



    protected void finalize() throws Throwable

    {

        shopPage = null;



        super.finalize();

    }



    public byte[] getXml()

        throws Exception

    {

        return getXml( "ShopPage" );

    }



    private static Object syncDebug = new Object();

    public byte[] getXml( String rootElement )

        throws Exception

    {

        if ( cat.isDebugEnabled() )

        {

            synchronized(syncDebug)

            {

                cat.debug( "Unmarshal ShopPagePortlet object" );

                FileWriter w = new FileWriter( WebmillConfig.getWebmillDebugDir()+"portlet-shop.xml" );

                Marshaller marsh = new Marshaller( w );

                marsh.setMarshalAsDocument( true );

                marsh.setEncoding( "utf-8" );

                marsh.marshal( shopPage );

                marsh = null;

                w.flush();

                w.close();

                w = null;

            }

        }

        return XmlTools.getXml( shopPage, rootElement );

    }



    public void setParameter(PortletParameter param_)

    {

        this.param = param_;

    }



    public boolean isXml()

    {

        return true;

    }



    public boolean isHtml()

    {

        return false;

    }



    public ShopPagePortlet()

    {

    }



    public PortletResultObject getInstance( DatabaseAdapter db_ )

        throws Exception

    {



        HttpSession session = param.getRequest().getSession();

        OrderType order = (OrderType) session.getAttribute( Constants.ORDER_SESSION );



        shop = (Shop) session.getAttribute( Constants.CURRENT_SHOP );



        if (shop == null)

        {

            if (cat.isDebugEnabled())

                cat.debug("Shop session not initialized");



            throw new Exception("shop session not iitialized. Add mill.order_logic portlet to template");

        }



        if (shop.currencyID == null)

        {

            throw new Exception("Default currency not defined.<br>Login and configure shop.");

        }



        shopPage.setPriceHeader( shop.header );

        shopPage.setPriceFooter( shop.footer );



        shopPage.setDateUploadPrice( DateTools.getStringDate(shop.dateUpload, "dd MMM yyyy", param.getJspPage().currentLocale) );

        shopPage.setTimeUploadPrice( DateTools.getStringDate(shop.dateUpload, "HH:mm", param.getJspPage().currentLocale) );





        ShopPageParam shopParam = new ShopPageParam();



        shopParam.id_shop = shop.id_shop;

        shopParam.isProcessInvoice = shop.isProcessInvoice;



        shopPage.setIsProcessInvoice( shop.isProcessInvoice ? "true" : "false" );





        shopParam.id_group = ServletTools.getLong( param.getRequest(), Constants.NAME_ID_GROUP_SHOP, new Long(0) );

        shopParam.nameTemplate = param.getRequest().getParameter(Constants.NAME_TEMPLATE_CONTEXT_PARAM);

        shopParam.setServerName( param.getRequest().getServerName());

        shopParam.id_currency = ServletTools.getLong( param.getRequest(), Constants.NAME_ID_CURRENCY_SHOP);



        // Если текущая валюта не определена(страница вызвана без указания конкретной валюты),

        // используем валюту по умолчанию

        if (shopParam.id_currency == null)

            shopParam.id_currency = shop.currencyID;



        if (shop.precisionList != null)

        {

            CurrencyPrecisionType item = shop.precisionList.getCurrencyPrecision(shopParam.id_currency);

            if (item != null)

                shopParam.precision = item;

        }

        else

        {

                cat.warn("Precision not defined. Use default - 2 digit.");

        }





// установка параметров для сортировки

// sort_direct == 0 означает сортировки по возрастанию, иначе сортировка по убыванию

        shopParam.sortBy = ServletUtils.getString( param.getRequest(), Constants.NAME_SHOP_SORT_BY, "item");

        shopParam.sortDirect = ServletTools.getInt( param.getRequest(), Constants.NAME_SHOP_SORT_DIRECT, new Integer(1)).intValue();



        sortItemUrl = param.getResponse().encodeURL(CtxURL.ctx()) + '?' +

            param.getJspPage().addURL + Constants.NAME_ID_GROUP_SHOP + '=' + shopParam.id_group + '&' +

            shopParam.currencyURL + '&' +

            Constants.NAME_ID_SHOP_PARAM + '=' + shopParam.id_shop + '&' +

            Constants.NAME_TYPE_CONTEXT_PARAM + '=' + Constants.CTX_TYPE_SHOP + '&' +

            Constants.NAME_TEMPLATE_CONTEXT_PARAM + '=' + shopParam.nameTemplate + '&' +

            Constants.NAME_ID_CURRENCY_SHOP + '=' + shopParam.id_currency + '&';



        sortPriceUrl = sortItemUrl;



        if ("item".equals(shopParam.sortBy))

        {

            itemDirect = (shopParam.sortDirect == 0 ? "DESC" : "ASC");

            shopPage.setItemDirect( itemDirect );

        }

        else if ("price".equals(shopParam.sortBy))

        {

            priceDirect = (shopParam.sortDirect == 0 ? "DESC" : "ASC");

            shopPage.setPriceDirect( priceDirect );

        }



        sortItemUrl +=

            Constants.NAME_SHOP_SORT_BY + "=item&" +

            Constants.NAME_SHOP_SORT_DIRECT + '=' + ("DESC".equals(itemDirect) ? 1 : 0);

        sortPriceUrl +=

            Constants.NAME_SHOP_SORT_BY + "=price&" +

            Constants.NAME_SHOP_SORT_DIRECT + '=' + ("DESC".equals(priceDirect) ? 1 : 0);



        shopPage.setSortItemUrl( sortItemUrl );

        shopPage.setSortPriceUrl( sortPriceUrl );



        if (shopParam.nameTemplate == null)

        {

            cat.error("shop template not defined");

            throw new Exception("shop template not defined");

        }



        shopParam.currencyURL = Constants.NAME_ID_CURRENCY_SHOP + "=" + shopParam.id_currency;



        if ((shopParam.id_currency == null) && (shop.is_default_currency == 1))

        {

            shopParam.currencyURL = "";

            shopParam.id_currency = shop.currencyID;

        }



/*

        if (cat.isDebugEnabled())

            cat.debug("LocalePackage - " + param.localePackage);



        if (localePackage != null)

            shopParam.sm = StringManager.getManager(localePackage, jspPage.currentLocale);

        else

            shopParam.sm = jspPage.sCustom;

*/

        shopParam.sm = param.getSm();



        if (shop.isNeedRecalc)

        {

            shopPage.setCurrencyList(

                PriceCurrency.getCurrencyList(

                    shopParam,

                    param.getJspPage(),

                    param.getResponse().encodeURL(CtxURL.ctx())

                )

            );

        }



        shopPage.setPricePosition( PriceListPosition.getInstance(db_, param.getResponse(), param.getJspPage(), shopParam) );

        shopPage.setCurrentBasket( ShopBasket.getInstance(order, shopParam.sm, param) );

        shopPage.setGroupList( PriceGroup.getInstance(db_, shopParam, param) );

        shopPage.setItemList( PriceListItemList.getInstance(db_, shopParam, param) );



        return this;

    }



    public PortletResultObject getInstance(DatabaseAdapter db__, Long id) throws Exception

    {

        return getInstance( db__ );

    }



    public PortletResultObject getInstanceByCode(DatabaseAdapter db__, String portletCode_) throws Exception

    {

        return getInstance( db__ );

    }



    public byte[] getPlainHTML()

    {

        return null;

    }



    public List getList(Long idSiteCtxLangCatalog, Long idContext)

    {

        if (cat.isDebugEnabled())

            cat.debug("Get list of Shop. idSiteCtxLangCatalog - " + idSiteCtxLangCatalog);





        PreparedStatement ps = null;

        ResultSet rs = null;

        DatabaseAdapter db_ = null;



        List v = new ArrayList();

        try {

            db_ = DatabaseAdapter.getInstance( false );

            ps = db_.prepareStatement(

                    "SELECT b.ID_SHOP, b.CODE_SHOP, b.NAME_SHOP "+

                    "FROM site_ctx_lang_catalog a, PRICE_SHOP_TABLE b, site_support_language c "+

                    "where a.ID_SITE_CTX_LANG_CATALOG=? and "+

                    "a.ID_SITE_SUPPORT_LANGUAGE=c.ID_SITE_SUPPORT_LANGUAGE and "+

                    "c.ID_SITE=b.ID_SITE"

            );



            RsetTools.setLong(ps, 1, idSiteCtxLangCatalog );



            rs = ps.executeQuery();

            while (rs.next())

            {

                Long id = RsetTools.getLong(rs, "ID_SHOP");

                String name = ""+id + ", "+

                        RsetTools.getString(rs, "CODE_SHOP")+ ", "+

                        RsetTools.getString(rs, "NAME_SHOP");



                ClassQueryItem item =

                        new ClassQueryItem(id, StringTools.truncateString(name, 60) );



                if (idContext.equals(item.index) )

                    item.isSelected = true;



                v.add( item );

            }

            return v;



        }

        catch(Exception e)

        {

            cat.error("Get list of Shop. idSiteCtxLangCatalog - " + idSiteCtxLangCatalog, e);

            return null;

        }

        finally

        {

            DatabaseManager.close(db_, rs, ps);

            rs = null;

            ps = null;

            db_ = null;

        }

    }

}

