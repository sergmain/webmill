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

 * Author: mill

 * Date: Dec 11, 2002

 * Time: 8:28:58 AM

 *

 * $Id$

 */



package org.riverock.portlet.portlets.model;



import java.io.FileWriter;

import java.io.IOException;

import java.sql.PreparedStatement;

import java.sql.Types;

import java.util.ArrayList;



import javax.servlet.ServletException;

import javax.servlet.http.HttpServlet;

import javax.servlet.http.HttpServletRequest;

import javax.servlet.http.HttpServletResponse;

import javax.servlet.http.HttpSession;

import javax.servlet.jsp.JspWriter;



import org.riverock.common.tools.DateTools;

import org.riverock.common.tools.ServletTools;

import org.riverock.common.tools.StringTools;

import org.riverock.common.tools.RsetTools;

import org.riverock.common.tools.NumberTools;

import org.riverock.generic.db.DatabaseAdapter;

import org.riverock.generic.db.DatabaseManager;

import org.riverock.generic.mail.MailMessage;

import org.riverock.generic.schema.db.CustomSequenceType;

import org.riverock.portlet.core.GetPriceListItem;

import org.riverock.portlet.main.Constants;

import org.riverock.portlet.price.CurrencyItem;

import org.riverock.portlet.price.CurrencyPrecisionList;

import org.riverock.portlet.price.CurrencyService;

import org.riverock.portlet.price.PriceException;

import org.riverock.portlet.price.Shop;

import org.riverock.portlet.price.CurrencyManager;

import org.riverock.portlet.schema.price.CurrencyPrecisionType;

import org.riverock.portlet.schema.price.OrderItemType;

import org.riverock.portlet.schema.price.OrderType;

import org.riverock.portlet.schema.price.ShopOrderType;

import org.riverock.portlet.schema.price.CustomCurrencyItemType;

import org.riverock.sso.a3.AuthException;

import org.riverock.sso.a3.AuthSession;

import org.riverock.sso.a3.AuthTools;

import org.riverock.sso.a3.InternalAuthProviderTools;

import org.riverock.webmill.port.InitPage;

import org.riverock.webmill.port.PortalInfo;

import org.riverock.webmill.portlet.CtxURL;

import org.riverock.webmill.portlet.PortletTools;

import org.riverock.webmill.utils.ServletUtils;

import org.riverock.webmill.config.WebmillConfig;



import org.apache.log4j.Logger;

import org.exolab.castor.xml.Marshaller;



public class OrderLogic extends HttpServlet

{

    private static Logger log = Logger.getLogger(OrderLogic.class);



    public OrderLogic()

    {

    }



    protected void finalize() throws Throwable

    {

        super.finalize();

    }



    public void doPost(HttpServletRequest request, HttpServletResponse response)

        throws IOException, ServletException

    {

        if (log.isDebugEnabled())

            log.debug("method is POST");



        doGet(request, response);

    }



    public void doGet(HttpServletRequest request_, HttpServletResponse response_)

        throws IOException, ServletException

    {

        DatabaseAdapter dbDyn = null;

        try

        {

            dbDyn = DatabaseAdapter.getInstance(true);



            HttpSession session = request_.getSession(true);



            Long idShop = PortletTools.getIdPortlet(Constants.NAME_ID_SHOP_PARAM, request_);





            if (log.isDebugEnabled())

            {

                if (idShop != null)

                    log.debug("idShop " + idShop.longValue());

                else

                    log.debug("idShop is null");

            }



// получаем из сессии текущий магазин

            Shop tempShop = (Shop) session.getAttribute(Constants.CURRENT_SHOP);



            if (log.isDebugEnabled())

            {

                log.debug("tempShop " + tempShop);

                if (tempShop != null)

                    log.debug("tempShop.idShop - " + tempShop.id_shop);

            }



            Shop shop = null;

// если в сессии текущего магазина нет, но вызван какой-то конкретный магазин

// создаем новый магазин и помещаем в сессию

            if (tempShop == null && idShop != null)

            {

                if (log.isDebugEnabled())

                    log.debug("tempShop is null and idShop is not null ");



                shop = Shop.getInstance(dbDyn, idShop);

                session.setAttribute(Constants.CURRENT_SHOP, shop);

            }

// если в сессии есть текущий магазин и

// код вызванного магазина совпадает с кодом мкгаза в сессии, юзаем его (тот, который в сессии)

            else if (tempShop != null &&

                (idShop == null || tempShop.id_shop.equals(idShop)))

            {

                if (log.isDebugEnabled())

                    log.debug("tempShop is not null and tempShop.idShop == idShop ");



                shop = tempShop;

            }

// если в сессии есть текущий магазин и

// код вызванного магазина не совпадает с кодом магаза в сессии,

// заменяем магаз в сессии на магаз с вызываемым кодом

            else if (tempShop != null && idShop != null && !tempShop.id_shop.equals(idShop))

            {

                if (log.isDebugEnabled())

                    log.debug("#11.22.09 create shop instance with idShop - " + idShop.longValue());



                shop = Shop.getInstance(dbDyn, idShop);



                if (log.isDebugEnabled())

                    log.debug("idShop of created shop - " + shop.id_shop);



                session.removeAttribute(Constants.CURRENT_SHOP);

                session.setAttribute(Constants.CURRENT_SHOP, shop);

            }

// теперь в shop находится текущий магаз ( тот который в сессии )

// если его создание прошло успешно - магаз с вызываемым кодом действительно есть,

// иначе shop == null



            if (log.isDebugEnabled())

            {

                log.debug("shop object " + shop);

                if (shop != null)

                    log.debug("shop.id_shop " + shop.id_shop);

            }



            OrderType order = null;

// если текущий магаз определен, то ищем в сессии заказ, связанный с этим магазом.

// если заказа в сессии нет, то создаем

            if (shop != null && shop.id_shop != null)

            {

                order = (OrderType) session.getAttribute(Constants.ORDER_SESSION);



                if (log.isDebugEnabled())

                    log.debug("order object - " + order);



                if (order == null)

                {

                    if (log.isDebugEnabled())

                        log.debug("Create new order");



                    order = new OrderType();

                    order.setServerName(request_.getServerName());



                    ShopOrderType shopOrder = new ShopOrderType();

                    shopOrder.setIdShop(shop.id_shop);



                    order.addShopOrdertList(shopOrder);

                    initAuthSession(dbDyn, order, AuthTools.getAuthSession(request_));

                }



// если заказ создан ранее и юзер прошел авторизацию,

// помещаем авторизационные данные в заказ

                if ((order != null) && (order.getAuthSession() == null))

                {

                    AuthSession authSession = (AuthSession) session.getAttribute(Constants.AUTH_SESSION);

                    if ((authSession != null) && (authSession.checkAccess(request_.getServerName())))

                    {

                        if (log.isDebugEnabled())

                            log.debug("updateAuthSession");



                        updateAuthSession(dbDyn, order, authSession);

                    }

                }



                Long id_item = ServletTools.getLong(request_, Constants.NAME_ADD_ID_ITEM);

                int count = ServletTools.getInt(request_, Constants.NAME_COUNT_ADD_ITEM_SHOP, new Integer(0)).intValue();



// если при вызове было указано какое либо количество определенного наименования,

// то помещаем это наименование в заказ

                if (log.isDebugEnabled())

                    log.debug("set new count of item. id_item - " + id_item + " count - " + count);



                if ((id_item != null) && (count > 0))

                {

                    if (log.isDebugEnabled())

                    {

                        log.debug("add item to order");

                        log.debug("id_order " + order.getIdOrder());

                        log.debug("id_item " + id_item);

                        log.debug("count " + count);

                    }



                    addItem(dbDyn, order, id_item, count);

                }

                session.removeAttribute(Constants.ORDER_SESSION);

                session.setAttribute(Constants.ORDER_SESSION, order);

            }



        }

        catch (Exception e)

        {

            try

            {

                dbDyn.rollback();

            }

            catch (Exception e1)

            {

            }



            log.error("Error processing ShopLogic", e);

            throw new ServletException(e);

        }

        finally

        {

            try

            {

                dbDyn.commit();

            }

            catch (Exception e2)

            {

            }



            DatabaseManager.close(dbDyn);

            dbDyn = null;

        }

    }



    public static boolean registerSession(

        HttpServletRequest request, HttpServletResponse response,

        JspWriter out)

    {

        DatabaseAdapter db_ = null;

        try

        {



            db_ = DatabaseAdapter.getInstance(true);

            InitPage jspPage = new InitPage(db_, request, response,

                                            null,

                                            Constants.NAME_LANG_PARAM, null, null);







//	String url_redir = ServletUtils.getString(request, "url", "/");

//	String url 	= tools.replaceString(url_redir, "?", "%3F");

//	url_redir 	= tools.replaceString(url_redir, "%3F", "?");

//---



            String url_redir = ServletUtils.getString(request, "url", CtxURL.ctx());



//	String url 	= tools.replaceString(url_redir, "?", "%3F");

//	url		= tools.replaceString(url, "=", "%3D");



            String url = StringTools.rewriteURL(url_redir);



            url_redir = response.encodeURL(url_redir);



            url_redir = StringTools.replaceString(url_redir, "%3F", "?");



            String action = ServletUtils.getString(request, "action");



            String username = ServletUtils.getString(request, "username").trim();

            String password1 = ServletUtils.getString(request, "password1").trim();

            String password2 = ServletUtils.getString(request, "password2").trim();



            String first_name = ServletUtils.getString(request, "first_name");

            String last_name = ServletUtils.getString(request, "last_name");



            String email = ServletUtils.getString(request, "email");

            String address = ServletUtils.getString(request, "address");

            String phone = ServletUtils.getString(request, "phone");





            HttpSession sess = request.getSession(true);

            OrderType order = (OrderType) sess.getAttribute(Constants.ORDER_SESSION);



            if (order == null)

            {

                response.sendRedirect(response.encodeURL(CtxURL.ctx()));

                return true;

            }



//out.println(p.idFirm);

//if(true) return;



            AuthSession auth_ = (AuthSession) sess.getAttribute(Constants.AUTH_SESSION);

            if ((auth_ != null) && (auth_.checkAccess(request.getServerName())))

            {

                response.sendRedirect(url_redir);

                return true;

            }



            if (action.equals("reg_new"))

            {



                if ((username != null) && (password1 != null) &&

                    (password1.equals(password2)) &&

                    (!username.equals("")) &&

                    (!password1.equals(""))

                )

                {

                    boolean commit_status = db_.conn.getAutoCommit();

                    db_.conn.setAutoCommit(false);

                    Long id_user = InternalAuthProviderTools.addNewUser(db_,

                                                                        first_name, last_name, "", jspPage.p.sites.getIdFirm(),

                                                                        email, address, phone);



                    Long id_auth_user;

                    try

                    {

                        id_auth_user = InternalAuthProviderTools.addUserAuth(

                            db_, id_user,

                            jspPage.p.sites.getIdFirm(), null, null, username, password1,

                            true, false, false

                        );



                    }

                    catch (AuthException e)

                    {

                        db_.rollback();

                        if (db_.testExceptionIndexUniqueKey(e, "USER_LOGIN_AU_UK"))

                        {

                            db_.conn.setAutoCommit(commit_status);

                            out.print("Login '" + username + "' already exist<br>");

                            out.print("Continue <a href=\"" + response.encodeURL("reg.jsp") + "?" + jspPage.addURL + "url=" + url + "\">here</a>");

                            return true;

                        }

                        else

                        {

                            out.print(e.toString());

                            return true;

                        }

                    }



                    InternalAuthProviderTools.bindUserRole(db_, id_auth_user, "SHOP_BUYER");



                    db_.commit();

                    db_.conn.setAutoCommit(commit_status);



                    auth_ = new AuthSession(username, password1);



                    MailMessage.sendMessage(

                        "You succesfully registered.\n" +

                        "Use your username and password for process invoice:\n" +

                        "\n" +

                        "\n" +

                        "Username: " + username + "\n" +

                        "Password: " + password1 + "\n",

                        email,

                        jspPage.p.sites.getAdminEmail(),

                        "Confirm registration"

                    );



                    if (auth_.checkAccess(request.getServerName()))

                    {

                        sess.setAttribute(Constants.AUTH_SESSION, auth_);

                        response.sendRedirect(url_redir);

//out.print("url: "+url_redir);

                        return true;

                    }

                }



            }

            else if (action.equals("reg_exists"))

            {

                if ((auth_ == null) ||

                    (!auth_.checkAccess(request.getServerName()))

                )



                {

                    if ((username != null) && (password1 != null))

                    {

                        auth_ = new AuthSession(username, password1);

                        if (auth_.checkAccess(request.getServerName()))

                        {

                            sess.setAttribute(Constants.AUTH_SESSION, auth_);

                            response.sendRedirect(url_redir);

                            return true;

                        }

                    }

                }

            }

            else if (action.equals("send_pass"))

            {

/*

public static void sendMessage(

String message,

String email_to,

String subj,

Locale loc)



return true;

*/

            }



        }

        catch (Exception e1)

        {

            try

            {

                log.error("", e1);

                out.println(e1.toString() + "<br>");

            }

            catch (Exception e11)

            {

            }

        }

        finally

        {

            DatabaseManager.close(db_);

            db_ = null;

        }

        return false;

    }



/*

    public BasketShopSession(DatabaseAdapter db_, long id_shop_, AuthSession auth_session)

        throws Exception

    {

        v = new Vector();

        id_shop = id_shop_;

        id_order = db_.getSequenceNextValue("seq_order");



        initAuthSession(auth_session);



    }

*/

    public static void initAuthSession(DatabaseAdapter dbDyn, OrderType order, AuthSession authSession)

        throws Exception

    {

        String sql_ = "";

        PreparedStatement ps = null;

        try

        {

            CustomSequenceType seq = new CustomSequenceType();

            seq.setSequenceName("SEQ_ORDER");

            seq.setTableName("PRICE_RELATE_USER_ORDER_V2");

            seq.setColumnName("ID_ORDER_V2");



            order.setIdOrder(new Long(dbDyn.getSequenceNextValue(seq)));



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

//                AuthInfo authInfo = InternalAuthProvider.getAuthInfo( authSession );

//                RsetTools.setLong(ps, 3, authInfo.userID );

                RsetTools.setLong(ps, 3, authSession.getUserInfo().getIdUser());

            }

            else

                ps.setNull(3, Types.NUMERIC);



            int i = ps.executeUpdate();



            if (log.isDebugEnabled())

                log.debug("count of inserted record - " + i);

        }

        catch (Exception e1)

        {

            log.error("order.getIdOrder() " + order.getIdOrder());

            log.error("authSession " + authSession);

            if (authSession != null && authSession.getUserInfo() != null)

            {

                log.error("authSession.getUserInfo().getIdUser() " + authSession.getUserInfo().getIdUser());

//                AuthInfo authInfo = InternalAuthProvider.getAuthInfo( authSession );

//                log.error("authInfo "+authInfo);

//                if (authInfo!=null)

//                    log.error("authInfo.authUserID "+authInfo.authUserID);

            }

            log.error("Error init AuthSession", e1);

            throw e1;

        }

        finally

        {

            DatabaseManager.close(ps);

            ps = null;

        }



    }



    public static void updateAuthSession(DatabaseAdapter dbDyn, OrderType order, AuthSession authSession)

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

                sql_ =

                    "update PRICE_RELATE_USER_ORDER_V2 " +

                    "set ID_USER = " +

                    "(select ID_USER from AUTH_USER	where USER_LOGIN = ?) " +

                    "where ID_ORDER_V2 = ? ";



                ps = dbDyn.prepareStatement(sql_);



                ps.setString(1, authSession.getUserLogin());

                RsetTools.setLong(ps, 2, order.getIdOrder());

                int i = ps.executeUpdate();



                if (log.isDebugEnabled())

                    log.debug("count of updated record - " + i);



            }

        }

        catch (Exception e1)

        {

            log.error("Error update authSession", e1);

            throw new PriceException(e1.toString());

        }

        finally

        {

            DatabaseManager.close(ps);

            ps = null;

        }

    }



    public static void removeOrder(DatabaseAdapter dbDyn, OrderType order)

        throws Exception

    {

        if (!dbDyn.isDynamic())

            throw new Exception("Error remove order. DB connection is not dynamic");



        String sql_ =

            "delete from PRICE_RELATE_USER_ORDER_V2 " +

            "where ID_ORDER_V2 = ? ";



        PreparedStatement ps = null;

        try

        {

            ps = dbDyn.prepareStatement(sql_);

            RsetTools.setLong(ps, 1, order.getIdOrder());



            int i = ps.executeUpdate();



            if (log.isDebugEnabled())

                log.debug("count of deleted record - " + i);



        }

        catch (Exception e)

        {

            log.error("Error remove id_item_ to shop basket", e);

            throw new PriceException(e.toString());

        }

        finally

        {

            DatabaseManager.close(ps);

            ps = null;

        }

    }



    public static boolean isItemInBasket(Long idItem, OrderType order)

    {

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



    public static void addItem(DatabaseAdapter dbDyn, OrderType order, Long idItem, int count)

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



// в методе initItem выводится дополнительная информация в DEBUG

            OrderItemType item = initItem(dbDyn, idItem, order.getServerName());

            if (log.isDebugEnabled())

                log.debug("idShop of created item - " + item.getIdShop());



            item.setCountItem(new Integer(count));

            boolean isNotInOrder = true;

            // если в заказе есть магазин и наименование, то изменяем количество

            for (int i = 0; i < order.getShopOrdertListCount(); i++)

            {

                ShopOrderType shopOrderTemp = order.getShopOrdertList(i);

                if (log.isDebugEnabled())

                    log.debug("shopOrder.idShop - " + shopOrderTemp.getIdShop());



                if (shopOrderTemp.getIdShop().equals( item.getIdShop()) )

                {

                    if (log.isDebugEnabled())

                        log.debug("Нужный магазин найден. Ищем нужное наименование, idItem - " + idItem);



                    shopOrder = shopOrderTemp;

                    for (int k = 0; k < shopOrderTemp.getOrderItemListCount(); k++)

                    {

                        OrderItemType orderItem = shopOrderTemp.getOrderItemList(k);

                        if (orderItem.getIdItem().equals( idItem) )

                        {

                            if (log.isDebugEnabled())

                                log.debug("Нужное наименвание найдено, old count "+orderItem.getCountItem()+". Устанавливаем новое количество "+count);



                            orderItem.setCountItem(new Integer(count));

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

                    log.debug("Нужный магазин найден, но наименования в нем нет.");

                }

            }



            // если в заказе нет магазина, то создаем магазин помещаем туда нименование

            if (shopOrder == null)

            {

                if (log.isDebugEnabled())

                    log.debug("Нужного магазина не найдено. Создаем новый с кодом - " + item.getIdShop());



                shopOrder = new ShopOrderType();

                shopOrder.setIdShop(item.getIdShop());

                shopOrder.addOrderItemList(item);



                order.addShopOrdertList(shopOrder);



                isNotInOrder = false;

            }

            // если в заказе есть магазина, но нет наименования - помещаем наименование в заказ

            if (isNotInOrder)

            {

                if (log.isDebugEnabled())

                    log.debug("Магазин есть но наименование не помещено в него. Помещаем");



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

            Long seqValue = new Long(dbDyn.getSequenceNextValue(seq));



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



    public static void setItem(DatabaseAdapter dbDyn, OrderType order, Long idItem, int count)

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

                    item.setCountItem(new Integer(count));

                    isNotInOrder = false;

                    break;

                }

            }

        }



        if (isNotInOrder)

            addItem(dbDyn, order, idItem, count);



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

            log.error("Error set new count", e);

            throw new PriceException(e.toString());

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

                    // в данной версии подразумевается, что в заказе не может быть

                    // двух магазинов с одинаковым кодом товар

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

            log.error("Error delete item from order", e);

            throw new PriceException(e.toString());

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

            log.error("Error clear order", e);

            throw new PriceException(e.toString());

        }

        finally

        {

            DatabaseManager.close(ps);

            ps = null;

        }

    }

/*

    private static Object syncObj = new Object();

    public static OrderItemType initItemV2(DatabaseAdapter db_, long idItem, String serverName)

        throws Exception

    {

        OrderItemType orderItem = new OrderItemType();

        GetPriceListItem priceItem = GetPriceListItem.getInstance(db_, idItem);



        try

        {

            if (priceItem!=null && priceItem.isFound)

            {

                PriceListItemType item = GetPriceListItem.getInstance(db_, idItem).item;



                PortalInfo p = PortalInfo.getInstance(db_, serverName);

                orderItem.setIdItem(idItem);

                orderItem.setIdOrigin( item.getId() );

                orderItem.setIdShop( item.getIdShop() );

                orderItem.setItem( item.getItem());

                orderItem.setPrice( item.getPrice() );

                String currencyCode = item.getCurrency();



                CurrencyItem currencyItem =

                    (CurrencyItem) CurrencyService.getCurrencyItemByCode(

                        CurrencyManager.getInstance(db_, p.sites.getIdSite()).getCurrencyList(), currencyCode

                    );

                currencyItem.fillRealCurrencyData(CurrencyManager.getInstance(db_, p.sites.getIdSite()).getCurrencyList().getStandardCurrencyList());



                orderItem.setCurrencyItem(currencyItem);



                Shop shop = Shop.getInstance(db_, orderItem.getIdShop());



                if (log.isDebugEnabled())

                {

                    log.debug("currencyCode " + currencyCode);

                    log.debug("currencyItem " + currencyItem);

                    log.debug("item.price " + orderItem.getItem());

                    if (currencyItem != null)

                    {

                        log.debug("currencyItem.isRealInit " + currencyItem.getIsRealInit());

                        log.debug("currencyItem.getRealCurs " + currencyItem.getRealCurs());

                    }

                }



                if (log.isDebugEnabled())

                    log.debug("new price will be calculated - " + (currencyItem != null && currencyItem.getIsRealInit()));



                if (currencyItem != null && currencyItem.getIsRealInit())

                {

                    CurrencyPrecisionType prec = null;

                    double resultPrice = 0;



                    if (log.isDebugEnabled())

                    {

                        log.debug("item idShop - "+orderItem.getIdShop());

                        log.debug("shop idShop - "+shop.id_shop);

                        log.debug("item idCurrency - "+orderItem.getCurrencyItem().getIdCurrency());

                        log.debug("shop idOrderCurrency - "+shop.idOrderCurrency);

                        log.debug("код валюты наименования совпадает с валютой в которой выводить заказ - " +

                            (orderItem.getCurrencyItem().getIdCurrency() == shop.idOrderCurrency));

                    }



                    // если код валюты наименования совпадает с валютой в которой выводить заказ

                    if (orderItem.getCurrencyItem().getIdCurrency() == shop.idOrderCurrency)

                    {

                        orderItem.setResultCurrency(orderItem.getCurrencyItem());



                        prec = shop.precisionList.getCurrencyPrecision(

                            currencyItem.getIdCurrency()

                        );

                        if (prec != null)

                        {

                            resultPrice =

                                NumberTools.truncate(orderItem.getPrice(), prec.getPrecision());





                        }

                        else

                            throw new Exception("Precision is null");

                    }

                    else

                    {

                        prec = shop.precisionList.getCurrencyPrecision(

                            shop.idOrderCurrency

                        );

                        if (prec != null)

                        {

                            CustomCurrencyItemType defaultCurrency =

                                CurrencyService.getCurrencyItem(CurrencyManager.getInstance(db_, p.sites.getIdSite()).getCurrencyList(), shop.idOrderCurrency);



                            orderItem.setResultCurrency(defaultCurrency);

                            double crossCurs =

                                orderItem.getCurrencyItem().getRealCurs() / defaultCurrency.getRealCurs();



                            resultPrice =

                                NumberTools.truncate(orderItem.getPrice(), prec.getPrecision()) *

                                crossCurs;



                            if (log.isDebugEnabled())

                            {

                                FileWriter w = new FileWriter( WebmillConfig.getWebmillDebugDir()+"schema-currency-item.xml" );

                                FileWriter w1 = new FileWriter( WebmillConfig.getWebmillDebugDir()+"schema-currency-default.xml");



                                Marshaller.marshal(orderItem.getCurrencyItem(), w);

                                Marshaller.marshal(defaultCurrency, w1);



                                w.flush();

                                w.close();

                                w = null;

                                w1.flush();

                                w1.close();

                                w1 = null;



                                log.debug("item curs - " + orderItem.getCurrencyItem().getRealCurs());

                                log.debug("default curs - " + defaultCurrency.getRealCurs());

                                log.debug("crossCurs - " + crossCurs + " price - " +

                                    NumberTools.truncate(orderItem.getPrice(), prec.getPrecision()) +

                                    " result price - " + resultPrice);

                            }



                        }

                        else

                            throw new Exception("Precision is null");

                    }

                    orderItem.setPriceItemResult(

                        NumberTools.truncate(resultPrice, prec.getPrecision())

                    );

                    orderItem.setPrecisionResult( prec.getPrecision() );

                }

                else

                {

                    boolean isReal = false;

                    if (currencyItem != null)

                        isReal = currencyItem.getIsRealInit();



                    throw new Exception("Price for item can not calculated. CurrencyItem is " +

                        (currencyItem == null ? "" : "not ") + "null, is real curs init - " + isReal

                    );

                }



                synchronized(syncObj)

                {

                    XmlTools.writeToFile(orderItem, WebmillConfig.getWebmillDebugDir()+"order-orderitem.xml");

                    XmlTools.writeToFile(item, WebmillConfig.getWebmillDebugDir()+"order-item.xml");

                }

                return orderItem;

            }



        }

        catch (Exception e)

        {

            log.error("error init item", e);

            throw e;

        }

        return orderItem;

    }

*/



    private static Object syncInitItemObj = new Object();



    public static OrderItemType initItem(DatabaseAdapter db_, Long idItem, String serverName)

        throws Exception

    {

        OrderItemType item = new OrderItemType();



//        PreparedStatement ps = null;

//        ResultSet rs = null;

//

//        String sql_ = "select * from PRICE_LIST where ID_ITEM = ?";



        try

        {

            GetPriceListItem itemTemp = GetPriceListItem.getInstance(db_, idItem);



            if (itemTemp.isFound)

            {

                GetPriceListItem.copyItem(itemTemp.item, item);



//            ps = db_.prepareStatement(sql_);

//

//            RsetTools.setLong(ps, 1, idItem);

//

//            rs = ps.executeQuery();





//                item.setIdItem(idItem);

//

//                item.setIdShop(RsetTools.getLong(rs, "ID_SHOP"));

//                item.setIdOrigin(RsetTools.getLong(rs, "ID"));

//                item.setNameItem(RsetTools.getString(rs, "ITEM"));

//                item.setPriceItem(RsetTools.getDouble(rs, "PRICE"));



//                String currencyCode = RsetTools.getString(rs, "CURRENCY");



                PortalInfo p = PortalInfo.getInstance(db_, serverName);

                CurrencyItem currencyItem =

                    (CurrencyItem) CurrencyService.getCurrencyItemByCode(

                        CurrencyManager.getInstance(db_, p.sites.getIdSite()).getCurrencyList(), item.getCurrency()

                    );

                currencyItem.fillRealCurrencyData(CurrencyManager.getInstance(db_, p.sites.getIdSite()).getCurrencyList().getStandardCurrencyList());



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

                        log.debug("код валюты наименования совпадает с валютой в которой выводить заказ - " +

                                  (item.getCurrencyItem().getIdCurrency() == shop.idOrderCurrency));

                    }



                    // если код валюты наименования совпадает с валютой в которой выводить заказ

                    int precisionValue = 2;

                    CurrencyPrecisionType precision = null;

                    if (item.getCurrencyItem().getIdCurrency().equals(shop.idOrderCurrency) )

                    {

                        item.setResultCurrency(item.getCurrencyItem());



                        precision = getPrecisionValue(shop.precisionList, currencyItem.getIdCurrency());

                        if (precision != null && precision.getPrecision() != null)

                            precisionValue = precision.getPrecision().intValue();



                        if (item.getPrice() != null)

                            resultPrice = NumberTools.truncate(

                                item.getPrice().doubleValue(), precisionValue

                            );

                        else

                            log.warn("price is null");

                    }

                    else

                    {

                        precision = getPrecisionValue(shop.precisionList, shop.idOrderCurrency);

                        if (precision != null && precision.getPrecision() != null)

                            precisionValue = precision.getPrecision().intValue();



                        CustomCurrencyItemType defaultCurrency =

                            CurrencyService.getCurrencyItem(CurrencyManager.getInstance(db_, p.sites.getIdSite()).getCurrencyList(), shop.idOrderCurrency);



                        item.setResultCurrency(defaultCurrency);



                        if (log.isDebugEnabled())

                        {

                            synchronized (syncInitItemObj)

                            {

                                FileWriter w = new FileWriter(WebmillConfig.getWebmillDebugDir() + "schema-currency-item.xml");

                                FileWriter w1 = new FileWriter(WebmillConfig.getWebmillDebugDir() + "schema-currency-default.xml");



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



                        crossCurs = item.getCurrencyItem().getRealCurs().doubleValue() / defaultCurrency.getRealCurs().doubleValue();



                        if (item.getPrice()!=null)

                        {

                            resultPrice =

                                NumberTools.truncate(item.getPrice().doubleValue(), precisionValue) *

                                crossCurs;



                            if (log.isDebugEnabled())

                        {

                            log.debug("crossCurs - " + crossCurs + " price - " +

                                      NumberTools.truncate(item.getPrice().doubleValue(), precisionValue) +

                                      " result price - " + resultPrice);

                        }

                        }

                        else

                            log.warn("price is null");

                    }

                    item.setPriceItemResult(

                        new Double(NumberTools.truncate(resultPrice, precisionValue))

                    );

                    item.setPrecisionResult(new Integer(precisionValue));

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

        if (prec == null)

        {

            log.warn("Precison not found for currencyId " + idCurrency);

            return null;

        }

        return prec;

    }



    public static int getCountItem(OrderType order)

    {

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