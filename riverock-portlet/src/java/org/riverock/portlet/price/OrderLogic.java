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
package org.riverock.portlet.price;

import java.io.File;
import java.io.FileWriter;
import java.sql.PreparedStatement;
import java.sql.Types;
import java.util.ArrayList;

import javax.portlet.PortletException;
import javax.portlet.PortletSession;
import javax.portlet.RenderRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.exolab.castor.xml.Marshaller;

import org.riverock.common.tools.DateTools;
import org.riverock.common.tools.NumberTools;
import org.riverock.common.tools.RsetTools;
import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.db.DatabaseManager;
import org.riverock.generic.schema.db.CustomSequenceType;
import org.riverock.portlet.core.GetPriceListItem;
import org.riverock.portlet.schema.price.CurrencyPrecisionType;
import org.riverock.portlet.schema.price.CustomCurrencyItemType;
import org.riverock.portlet.schema.price.OrderItemType;
import org.riverock.portlet.schema.price.OrderType;
import org.riverock.portlet.schema.price.ShopOrderType;
import org.riverock.portlet.tools.SiteUtils;
import org.riverock.portlet.shop.bean.ShopOrder;
import org.riverock.interfaces.sso.a3.AuthSession;
import org.riverock.webmill.container.tools.PortletService;
import org.riverock.webmill.container.portal.PortalInfo;
import org.riverock.webmill.container.ContainerConstants;

/**
 * Author: mill
 * Date: Dec 11, 2002
 * Time: 8:28:58 AM
 *
 * $Id$
 */
public final class OrderLogic {
    private final static Log log = LogFactory.getLog( OrderLogic.class );

    public OrderLogic() {
    }

    public static void process( final RenderRequest renderRequest ) throws PortletException {
        DatabaseAdapter dbDyn = null;
        try {
            dbDyn = DatabaseAdapter.getInstance();

            PortletSession session = renderRequest.getPortletSession(true);
            Long idShop = PortletService.getIdPortlet(ShopPortlet.NAME_ID_SHOP_PARAM, renderRequest);
            PortalInfo portalInfo = (PortalInfo)renderRequest.getAttribute( ContainerConstants.PORTAL_INFO_ATTRIBUTE );


            if (log.isDebugEnabled())
            {
                if (idShop != null)
                    log.debug("idShop " + idShop);
                else
                    log.debug("idShop is null");
            }

            // get current shop from session
            Shop tempShop = (Shop) session.getAttribute(ShopPortlet.CURRENT_SHOP);

            if (log.isDebugEnabled()) {
                log.debug("tempShop " + tempShop);
                if (tempShop != null)
                    log.debug("tempShop.idShop - " + tempShop.id_shop);
            }

            Shop shop = null;
            // ���� � ������ �������� �������� ���, �� ������ �����-�� ���������� �������
            // ������� ����� ������� � �������� � ������
            if (tempShop == null && idShop != null) {
                if (log.isDebugEnabled())
                    log.debug("tempShop is null and idShop is not null ");

                shop = Shop.getInstance(dbDyn, idShop);
                session.setAttribute(ShopPortlet.CURRENT_SHOP, shop);
            }
            // ���� � ������ ���� ������� ������� �
            // ��� ���������� �������� ��������� � ����� ������ � ������,
            // ����� ��� (���, ������� � ������)
            else if (tempShop != null &&
                (idShop == null || idShop.equals(tempShop.id_shop)))
            {
                if (log.isDebugEnabled())
                    log.debug("tempShop is not null and tempShop.idShop == idShop ");

                shop = tempShop;
            }
// ���� � ������ ���� ������� ������� �
// ��� ���������� �������� �� ��������� � ����� ������ � ������,
// �������� ����� � ������ �� ����� � ���������� �����
            else if (tempShop != null && idShop != null && !idShop.equals(tempShop.id_shop)) {
                if (log.isDebugEnabled()) {
                    log.debug("#11.22.09 create shop instance with idShop - " + idShop );
                }

                shop = Shop.getInstance(dbDyn, idShop);

                if (log.isDebugEnabled())
                    log.debug("idShop of created shop - " + shop.id_shop);

                session.removeAttribute(ShopPortlet.CURRENT_SHOP);
                session.setAttribute(ShopPortlet.CURRENT_SHOP, shop);
            }
// ������ � shop ��������� ������� ����� ( ��� ������� � ������ )
// ���� ��� �������� ������ ������� - ����� � ���������� ����� ������������� ����,
// ����� shop == null

            if (log.isDebugEnabled()) {
                log.debug("shop object " + shop);
                if (shop != null)
                    log.debug("shop.id_shop " + shop.id_shop);
            }

            ShopOrder order = null;
// ���� ������� ����� ���������, �� ���� � ������ �����, ��������� � ���� �������.
// ���� ������ � ������ ���, �� �������
            if (shop != null && shop.id_shop != null) {
                order = (ShopOrder) session.getAttribute(ShopPortlet.ORDER_SESSION);

                if (log.isDebugEnabled())
                    log.debug("order object - " + order);

                if (order == null) {
                    if (log.isDebugEnabled())
                        log.debug("Create new order");

                    order = new ShopOrder();
                    order.setServerName(renderRequest.getServerName());

                    ShopOrderType shopOrder = new ShopOrderType();
                    shopOrder.setIdShop(shop.id_shop);

                    order.addShopOrdertList(shopOrder);
                    initAuthSession(dbDyn, order, (AuthSession)renderRequest.getUserPrincipal());
                }

                // ���� ����� ������ ����� � ���� ������ �����������,
                // �������� ��������������� ������ � �����
                if ((order != null) && (order.getAuthSession() == null)) {
                    AuthSession authSession = (AuthSession)renderRequest.getUserPrincipal();
                    if ((authSession != null) && (authSession.checkAccess(renderRequest.getServerName()))) {
                        if (log.isDebugEnabled())
                            log.debug("updateAuthSession");

                        updateAuthSession(dbDyn, order, authSession);
                    }
                }

                Long id_item = PortletService.getLong(renderRequest, ShopPortlet.NAME_ADD_ID_ITEM);
                int count = PortletService.getInt(renderRequest, ShopPortlet.NAME_COUNT_ADD_ITEM_SHOP, 0);

// ���� ��� ������ ���� ������� ����� ���� ���������� ������������� ������������,
// �� �������� ��� ������������ � �����
                if (log.isDebugEnabled())
                    log.debug("set new count of item. id_item - " + id_item + " count - " + count);

                if ((id_item != null) && (count > 0)) {
                    if (log.isDebugEnabled()) {
                        log.debug("add item to order");
                        log.debug("id_order " + order.getIdOrder());
                        log.debug("id_item " + id_item);
                        log.debug("count " + count);
                    }

                    addItem(dbDyn, order, id_item, count, portalInfo.getSiteId());
                }
                session.removeAttribute(ShopPortlet.ORDER_SESSION);
                session.setAttribute(ShopPortlet.ORDER_SESSION, order);
            }

            dbDyn.commit();

        }
        catch (Exception e) {
            try {
                dbDyn.rollback();
            }
            catch (Exception e1) {
            }

            final String es = "Error processing OrderLogic";
            log.error( es, e );
            throw new PortletException( es, e );
        }
        finally {
            DatabaseManager.close(dbDyn);
            dbDyn = null;
        }
    }

    public static void initAuthSession(
        final DatabaseAdapter dbDyn, final OrderType order, final AuthSession authSession )
        throws Exception {
        String sql_ = "";
        PreparedStatement ps = null;
        try {
            CustomSequenceType seq = new CustomSequenceType();
            seq.setSequenceName("SEQ_ORDER");
            seq.setTableName("PRICE_RELATE_USER_ORDER_V2");
            seq.setColumnName("ID_ORDER_V2");

            order.setIdOrder( dbDyn.getSequenceNextValue(seq) );

            sql_ =
                "insert into PRICE_RELATE_USER_ORDER_V2 " +
                "(ID_ORDER_V2, DATE_CREATE, ID_USER)" +
                "values " +
                "(?,  " + dbDyn.getNameDateBind() + ", ? )";

            ps = dbDyn.prepareStatement(sql_);

            RsetTools.setLong(ps, 1, order.getIdOrder());
            dbDyn.bindDate(ps, 2, DateTools.getCurrentTime());

            if (authSession != null && authSession.getUserInfo() != null)
            {
                RsetTools.setLong(ps, 3, authSession.getUserInfo().getUserId());
            }
            else
                ps.setNull(3, Types.NUMERIC);

            int i = ps.executeUpdate();

            if (log.isDebugEnabled())
                log.debug("count of inserted record - " + i);
        }
        catch (Exception e1) {
            log.error("order.getIdOrder() " + order.getIdOrder());
            log.error("authSession " + authSession);
            if (authSession != null && authSession.getUserInfo() != null)
                log.error("authSession.getUserInfo().getIdUser() " + authSession.getUserInfo().getUserId());

            log.error("Error init AuthSession", e1);
            throw e1;
        }
        finally {
            DatabaseManager.close(ps);
            ps = null;
        }
    }

    public static void updateAuthSession( final DatabaseAdapter dbDyn, final OrderType order, final AuthSession authSession)
        throws Exception
    {
        if (!dbDyn.isDynamic())
            throw new Exception("Error update Auth Session. DB connection is not dynamic");

        String sql_ = "";
        PreparedStatement ps = null;
        try
        {
            if (authSession != null)
            {
                switch (dbDyn.getFamaly())
                {
                    case DatabaseManager.MYSQL_FAMALY:
                        Long userId = DatabaseManager.getLongValue(
                            dbDyn, "select ID_USER from AUTH_USER where USER_LOGIN=? ", new Object[]{authSession.getUserLogin()});
                        if (userId!=null)
                        {
                            sql_ =
                                "update PRICE_RELATE_USER_ORDER_V2 " +
                                "set ID_USER = ? " +
                                "where ID_ORDER_V2 = ? ";

                            ps = dbDyn.prepareStatement(sql_);

                            RsetTools.setLong(ps, 1, userId);
                            RsetTools.setLong(ps, 2, order.getIdOrder());
                        }
                        break;
                    default:
                        sql_ =
                            "update PRICE_RELATE_USER_ORDER_V2 " +
                            "set ID_USER = " +
                            "(select ID_USER from AUTH_USER	where USER_LOGIN = ?) " +
                            "where ID_ORDER_V2 = ? ";

                        ps = dbDyn.prepareStatement(sql_);

                        ps.setString(1, authSession.getUserLogin());
                        RsetTools.setLong(ps, 2, order.getIdOrder());
                        break;
                }

                int i = ps.executeUpdate();

                if (log.isDebugEnabled())
                    log.debug("count of updated record - " + i);

            }
        }
        catch (Exception e1) {
            final String es = "Error update authSession";
            log.error( es, e1 );
            throw new PriceException( es, e1 );
        }
        finally {
            DatabaseManager.close(ps);
            ps = null;
        }
    }

    public static void removeOrder( final DatabaseAdapter dbDyn, final OrderType order )
        throws Exception {

        if (!dbDyn.isDynamic())
            throw new Exception("Error remove order. DB connection is not dynamic");

        String sql_ =
            "delete from PRICE_RELATE_USER_ORDER_V2 " +
            "where ID_ORDER_V2 = ? ";

        PreparedStatement ps = null;
        try {
            ps = dbDyn.prepareStatement(sql_);
            RsetTools.setLong(ps, 1, order.getIdOrder());

            int i = ps.executeUpdate();

            if (log.isDebugEnabled())
                log.debug("count of deleted record - " + i);

        }
        catch (Exception e) {
            final String es = "Error remove id_item_ to shop basket";
            log.error( es, e );
            throw new PriceException( es, e );
        }
        finally {
            DatabaseManager.close(ps);
            ps = null;
        }
    }

    public static boolean isItemInBasket( final Long idItem, final OrderType order ) {
        if (order == null || idItem == null)
            return false;

        for (int i = 0; i < order.getShopOrdertListCount(); i++)
        {
            ShopOrderType shopOrder = order.getShopOrdertList(i);
            for (int k = 0; k < shopOrder.getOrderItemListCount(); k++)
            {
                OrderItemType item = shopOrder.getOrderItemList(k);
                if (idItem.equals(item.getIdItem()))
                    return true;
            }
        }
        return false;
    }

    public static void addItem( final DatabaseAdapter dbDyn, final OrderType order, final Long idItem, final int count, long siteId)
        throws Exception
    {
        if (!dbDyn.isDynamic())
            throw new Exception("Error add item in order. DB connection is not dynamic");

        if (log.isDebugEnabled())
            log.debug("Add new count of item. id_item - " + idItem + " count - " + count);

        PreparedStatement ps = null;

        try
        {
            ShopOrderType shopOrder = null;

// � ������ initItem ��������� �������������� ���������� � DEBUG
            OrderItemType item = initItem(dbDyn, idItem, order.getServerName(), siteId);
            if (log.isDebugEnabled())
                log.debug("idShop of created item - " + item.getIdShop());

            item.setCountItem( count );
            boolean isNotInOrder = true;
            // ���� � ������ ���� ������� � ������������, �� �������� ����������
            for (int i = 0; i < order.getShopOrdertListCount(); i++)
            {
                ShopOrderType shopOrderTemp = order.getShopOrdertList(i);
                if (log.isDebugEnabled())
                    log.debug("shopOrder.idShop - " + shopOrderTemp.getIdShop());

                if (shopOrderTemp.getIdShop().equals( item.getIdShop()) )
                {
                    if (log.isDebugEnabled())
                        log.debug("������ ������� ������. ���� ������ ������������, idItem - " + idItem);

                    shopOrder = shopOrderTemp;
                    for (int k = 0; k < shopOrderTemp.getOrderItemListCount(); k++)
                    {
                        OrderItemType orderItem = shopOrderTemp.getOrderItemList(k);
                        if (orderItem.getIdItem().equals( idItem) )
                        {
                            if (log.isDebugEnabled())
                                log.debug("������ ����������� �������, old count "+orderItem.getCountItem()+". ������������� ����� ���������� "+count);

                            orderItem.setCountItem( count );
                            isNotInOrder = false;
                            break;
                        }
                    }
                }
            }

            if (log.isDebugEnabled())
            {
                if (isNotInOrder && shopOrder != null)
                {
                    log.debug("������ ������� ������, �� ������������ � ��� ���.");
                }
            }

            // ���� � ������ ��� ��������, �� ������� ������� �������� ���� �����������
            if (shopOrder == null)
            {
                if (log.isDebugEnabled())
                    log.debug("������� �������� �� �������. ������� ����� � ����� - " + item.getIdShop());

                shopOrder = new ShopOrderType();
                shopOrder.setIdShop(item.getIdShop());
                shopOrder.addOrderItemList(item);

                order.addShopOrdertList(shopOrder);

                isNotInOrder = false;
            }
            // ���� � ������ ���� ��������, �� ��� ������������ - �������� ������������ � �����
            if (isNotInOrder)
            {
                if (log.isDebugEnabled())
                    log.debug("������� ���� �� ������������ �� �������� � ����. ��������");

                shopOrder.addOrderItemList(item);
            }

            if (log.isDebugEnabled())
            {
                log.debug("Try update count of existing item");
                log.debug("count - " + count);
                log.debug("idItem - " + idItem);
                log.debug("idOrder - " + order.getIdOrder());
            }

            String sql_ =
                "update PRICE_ORDER_V2 " +
                "set COUNT=? " +
                "where ID_ITEM=? and ID_ORDER_V2=? ";

            ps = dbDyn.prepareStatement(sql_);

            ps.setInt(1, count);
            RsetTools.setLong(ps, 2, idItem);
            RsetTools.setLong(ps, 3, order.getIdOrder());

            int update = ps.executeUpdate();

            if (log.isDebugEnabled())
                log.debug("count of updated record - " + update);

            if (update == 0)
                addItem(dbDyn, order.getIdOrder(), item);

        }
        catch (Exception e)
        {
            log.error("Error add id_item_ to shop basket", e);
            throw e;
        }
        finally
        {
            DatabaseManager.close(ps);
            ps = null;
        }
    }

    public static void addItem(DatabaseAdapter dbDyn, Long idOrder, OrderItemType item)
        throws Exception
    {
        if (!dbDyn.isDynamic())
            throw new Exception("Error add item in order. DB connection is not dynamic");

        if (item == null)
            throw new Exception("Error add item to order. Item is null");

        if (log.isDebugEnabled())
            log.debug("Add new count of item. id_item - " + item.getIdItem() + " count - " + item.getCountItem());

        PreparedStatement ps = null;

        try
        {
            String sql_ = null;

            CustomSequenceType seq = new CustomSequenceType();
            seq.setSequenceName("seq_price_order");
            seq.setTableName("PRICE_ORDER_V2");
            seq.setColumnName("ID_PRICE_ORDER_V2");
            Long seqValue = dbDyn.getSequenceNextValue(seq);

            sql_ =
                "insert into PRICE_ORDER_V2 " +
                "(ID_PRICE_ORDER_V2, ID_ORDER_V2, ID_ITEM, COUNT, ITEM, PRICE, " +
                "CURRENCY, PRICE_RESULT, CODE_CURRENCY_RESULT, NAME_CURRENCY_RESULT," +
                "PRECISION_CURRENCY_RESULT )" +
                "values " +
                "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )";

            if (log.isDebugEnabled())
            {
                log.debug("insert new item to order");
                log.debug("id sequnce - " + seqValue);
                log.debug("id_order - " + idOrder);
            }

            ps = dbDyn.prepareStatement(sql_);
            RsetTools.setLong(ps, 1, seqValue);
            RsetTools.setLong(ps, 2, idOrder);
            RsetTools.setLong(ps, 3, item.getIdItem());
            RsetTools.setInt(ps, 4, item.getCountItem());
            ps.setString(5, item.getItem());
            RsetTools.setDouble(ps, 6, item.getPrice());

            ps.setString(7, item.getCurrencyItem().getCurrencyCode());

            RsetTools.setDouble(ps, 8, item.getPriceItemResult());
            ps.setString(9, item.getResultCurrency().getCurrencyCode());
            ps.setString(10, item.getResultCurrency().getCurrencyName());
            RsetTools.setInt(ps, 11, item.getPrecisionResult());

// where
//            RsetTools.setLong(ps, 9, item.getIdItem());
//            RsetTools.setLong(ps, 10, item.getIdShop());

            int update = ps.executeUpdate();

            if (log.isDebugEnabled())
                log.debug("count of inserted record - " + update);

        }
        catch (Exception e)
        {
            log.error("Error add id_item to shop basket", e);
            throw e;
        }
        finally
        {
            DatabaseManager.close(ps);
            ps = null;
        }
    }

    public static void setItem(DatabaseAdapter dbDyn, OrderType order, Long idItem, int count, long siteId)
        throws Exception
    {
        if (!dbDyn.isDynamic())
            throw new Exception("Error set new count of item in order. DB connection is not dynamic");

        boolean isNotInOrder = true;
        if (idItem == null)
            return;

        for (int i = 0; i < order.getShopOrdertListCount(); i++)
        {
            ShopOrderType shopOrder = order.getShopOrdertList(i);
            for (int k = 0; k < shopOrder.getOrderItemListCount(); k++)
            {
                OrderItemType item = shopOrder.getOrderItemList(k);
                if (idItem.equals(item.getIdItem()))
                {
                    item.setCountItem( count );
                    isNotInOrder = false;
                    break;
                }
            }
        }

        if (isNotInOrder)
            addItem(dbDyn, order, idItem, count, siteId);

        String sql_ =
            "update PRICE_ORDER_V2 " +
            "set COUNT = ? " +
            "where ID_ORDER_V2 = ? and ID_ITEM = ? ";

        PreparedStatement ps = null;
        try
        {
            ps = dbDyn.prepareStatement(sql_);
            ps.setInt(1, count);
            RsetTools.setLong(ps, 2, order.getIdOrder());
            RsetTools.setLong(ps, 3, idItem);

            int update = ps.executeUpdate();

            if (log.isDebugEnabled())
                log.debug("count of updated record - " + update);

        }
        catch (Exception e)
        {
            final String es = "Error set new count";
            log.error( es, e );
            throw new PriceException( es, e );
        }
        finally
        {
            DatabaseManager.close(ps);
            ps = null;
        }
    }

    public static void delItem(DatabaseAdapter dbDyn, OrderType order, Long id_item)
        throws Exception
    {
        if (!dbDyn.isDynamic())
            throw new Exception("Error set new count of item in order. DB connection is not dynamic");

        if (id_item == null)
            return;

        boolean isDeleted = false;
        for (int i = 0; i < order.getShopOrdertListCount(); i++)
        {
            ShopOrderType shopOrder = order.getShopOrdertList(i);
            for (int k = 0; k < shopOrder.getOrderItemListCount(); k++)
            {
                OrderItemType item = shopOrder.getOrderItemList(k);
                if (id_item.equals(item.getIdItem()))
                {
                    shopOrder.removeOrderItemList(item);
                    // � ������ ������ ���������������, ��� � ������ �� ����� ����
                    // ���� ��������� � ���������� ����� �����
                    isDeleted = true;
                    break;
                }
            }
        }

        if (log.isDebugEnabled())
        {
            log.debug("isDeleted - " + isDeleted);
        }

        String sql_ = "delete from PRICE_ORDER_V2 where ID_ORDER_V2=? and ID_ITEM=? ";

        PreparedStatement ps = null;
        try
        {
            ps = dbDyn.prepareStatement(sql_);

            RsetTools.setLong(ps, 1, order.getIdOrder());
            RsetTools.setLong(ps, 2, id_item);

            int update = ps.executeUpdate();

            if (log.isDebugEnabled())
                log.debug("count of deleted record - " + update);

            dbDyn.commit();
        }
        catch (Exception e)
        {
            final String es = "Error delete item from order";
            log.error( es, e );
            throw new PriceException( es, e );
        }
        finally
        {
            DatabaseManager.close(ps);
            ps = null;
        }
    }

    public static void clear(DatabaseAdapter dbDyn, OrderType order)
        throws Exception
    {
        if (!dbDyn.isDynamic())
            throw new Exception("Error clear order. DB connection is not dynamic");

        order.setShopOrdertList(new ArrayList());

        String sql_ = "delete from PRICE_ORDER_V2 where ID_ORDER_V2 = ? ";

        PreparedStatement ps = null;
        try
        {
            ps = dbDyn.prepareStatement(sql_);
            RsetTools.setLong(ps, 1, order.getIdOrder());

            int update = ps.executeUpdate();

            if (log.isDebugEnabled())
                log.debug("count of deleted record - " + update);

        }
        catch (Exception e)
        {
            final String es = "Error clear order";
            log.error( es, e );
            throw new PriceException( es, e );
        }
        finally
        {
            DatabaseManager.close(ps);
            ps = null;
        }
    }

    private static Object syncInitItemObj = new Object();

    public static OrderItemType initItem(DatabaseAdapter db_, Long idItem, String serverName, long siteId)
        throws Exception
    {
        OrderItemType item = new OrderItemType();

        try {
            GetPriceListItem itemTemp = GetPriceListItem.getInstance(db_, idItem);

            if (itemTemp.isFound) {

                GetPriceListItem.copyItem(itemTemp.item, item);

                CurrencyItem currencyItem =
                    (CurrencyItem) CurrencyService.getCurrencyItemByCode(
                        CurrencyManager.getInstance(db_, siteId).getCurrencyList(), item.getCurrency()
                    );
                currencyItem.fillRealCurrencyData(CurrencyManager.getInstance(db_, siteId).getCurrencyList().getStandardCurrencyList());

                item.setCurrencyItem(currencyItem);

                Shop shop = Shop.getInstance(db_, item.getIdShop());

                if (log.isDebugEnabled())
                {
                    log.debug("currencyCode " + item.getCurrency());
                    log.debug("currencyItem " + currencyItem);
                    log.debug("item.price " + item.getPrice());
                    if (currencyItem != null)
                    {
                        log.debug("currencyItem.isRealInit " + currencyItem.getIsRealInit());
                        log.debug("currencyItem.getRealCurs " + currencyItem.getRealCurs());
                    }
                }

                if (log.isDebugEnabled())
                    log.debug("new price will be calculated - " + (currencyItem != null && Boolean.TRUE.equals(currencyItem.getIsRealInit())));

                if (currencyItem != null && Boolean.TRUE.equals(currencyItem.getIsRealInit()))
                {

                    double resultPrice = 0;

                    if (log.isDebugEnabled())
                    {
                        log.debug("item idShop - " + item.getIdShop());
                        log.debug("shop idShop - " + shop.id_shop);
                        log.debug("item idCurrency - " + item.getCurrencyItem().getIdCurrency());
                        log.debug("shop idOrderCurrency - " + shop.idOrderCurrency);
                        log.debug("��� ������ ������������ ��������� � ������� � ������� �������� ����� - " +
                                  (item.getCurrencyItem().getIdCurrency() == shop.idOrderCurrency));
                    }

                    // ���� ��� ������ ������������ ��������� � ������� � ������� �������� �����
                    int precisionValue = 2;
                    CurrencyPrecisionType precision = null;
                    if (item.getCurrencyItem().getIdCurrency().equals(shop.idOrderCurrency) )
                    {
                        item.setResultCurrency(item.getCurrencyItem());

                        precision = getPrecisionValue(shop.precisionList, currencyItem.getIdCurrency());
                        if (precision != null && precision.getPrecision() != null)
                            precisionValue = precision.getPrecision();

                        if (item.getPrice() != null)
                            resultPrice = NumberTools.truncate(
                                item.getPrice(), precisionValue
                            );
                        else {
                            if (log.isDebugEnabled()) {
                                log.info("price is null");
                            }
                        }
                    }
                    else
                    {
                        precision = getPrecisionValue(shop.precisionList, shop.idOrderCurrency);
                        if (precision != null && precision.getPrecision() != null)
                            precisionValue = precision.getPrecision();

                        CustomCurrencyItemType defaultCurrency =
                            CurrencyService.getCurrencyItem(CurrencyManager.getInstance(db_, siteId).getCurrencyList(), shop.idOrderCurrency);

                        item.setResultCurrency(defaultCurrency);

                        if (log.isDebugEnabled())
                        {
                            synchronized (syncInitItemObj)
                            {
                                FileWriter w = new FileWriter(SiteUtils.getTempDir() + File.separatorChar + "schema-currency-item.xml");
                                FileWriter w1 = new FileWriter(SiteUtils.getTempDir() + File.separatorChar + "schema-currency-default.xml");

                                Marshaller.marshal(item.getCurrencyItem(), w);
                                Marshaller.marshal(defaultCurrency, w1);

                                w.flush();
                                w.close();
                                w = null;
                                w1.flush();
                                w1.close();
                                w1 = null;

                                log.debug("item curs - " + item.getCurrencyItem().getRealCurs());
                                log.debug("default curs - " + defaultCurrency.getRealCurs());
                            }
                        }
                        double crossCurs = 0;

                        crossCurs = item.getCurrencyItem().getRealCurs() / defaultCurrency.getRealCurs();

                        if (item.getPrice()!=null) {
                            resultPrice =
                                NumberTools.truncate(item.getPrice(), precisionValue) *
                                crossCurs;

                            if (log.isDebugEnabled()) {
                                log.debug("crossCurs - " + crossCurs + " price - " +
                                    NumberTools.truncate(item.getPrice(), precisionValue) +
                                    " result price - " + resultPrice);
                            }
                        }
                        else {
                            if (log.isDebugEnabled()) {
                                log.info("price is null");
                            }
                        }
                    }
                    item.setPriceItemResult(
                        NumberTools.truncate( resultPrice, precisionValue )
                    );
                    item.setPrecisionResult(precisionValue);
                }
                else
                {
                    boolean isReal = false;
                    if (currencyItem != null)
                        isReal = Boolean.TRUE.equals(currencyItem.getIsRealInit());

                    throw new PriceException(
                        "Price for item can not calculated. CurrencyItem is " +
                        (currencyItem == null ? "" : "not ") + "null, is real curs init - " + isReal
                    );
                }
                return item;
            }

        }
        catch (Exception e)
        {
            log.error("error init item", e);
            throw e;
        }
        return item;
    }

    private static CurrencyPrecisionType getPrecisionValue(CurrencyPrecisionList precList, Long idCurrency)
    {
        CurrencyPrecisionType prec;
        prec = precList.getCurrencyPrecision(idCurrency);
        if (prec == null) {
            if (log.isDebugEnabled()) {
                log.info("Precison not found for currencyId " + idCurrency);
            }
            return null;
        }
        return prec;
    }

    public static int getCountItem(OrderType order) {

        if (order == null)
            return 0;
        int count = 0;
        for (int i = 0; i < order.getShopOrdertListCount(); i++)
        {
            ShopOrderType shopOrder = order.getShopOrdertList(i);
            count += shopOrder.getOrderItemListCount();
        }
        return count;
    }
}