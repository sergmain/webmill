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



import org.riverock.sso.a3.AuthSession;



import java.util.Vector;



/**

 * $Id$

 */

public class BasketShopSession

{



//    private static Logger cat = Logger.getLogger("org.riverock.portlet.price.BasketShopSession");



    public long id_shop;

    public Shop shop = null;



    public long id_order;

    public AuthSession authSession;

    public Vector v; // Vector of Item



    public String currentBasketUrl = "";

    public String currentBasketName = "";



    protected void finalize() throws Throwable

    {

/*

        if (cat.isDebugEnabled())

            cat.debug("Begin terminate shop session");



        clearObject();



        if (cat.isDebugEnabled())

            cat.debug("End terminate shop session");



        shop = null;

        authSession = null;

        currentBasketUrl = null;

        currentBasketName = null;



        if (v != null)

        {

            v.clear();

            v = null;

        }

*/

        super.finalize();

    }



/*

    public void clearObject()

    {

        if (cat.isDebugEnabled())

            cat.debug("Items in basket: " + v.size());



        if (v.size() == 0)

        {

            if (cat.isDebugEnabled())

                cat.debug("Basket is empty, delete order now");



            try

            {

                removeOrder();



                if (cat.isDebugEnabled())

                    cat.debug("Order removed");

            }

            catch (Exception e01)

            {

                if (cat.isDebugEnabled())

                    cat.debug("Error delete order: " + e01.toString());



                ExceptionTools.getStackTrace(e01, 15);

            }



        }

        else

        {

            if (cat.isDebugEnabled())

                cat.debug("Basket is not empty, leave order");

        }

    }

*/



    public BasketShopSession()

    {

    }



/*

    public int getCountItems()

    {

        if (v == null)

            return 0;

        return v.size();

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

                null, null,

                Constants.NAME_LANG_PARAM, null, null);







//	String url_redir = PortletTools.getString(ctxInstance.getPortletRequest(), "url", "/");

//	String url 	= tools.replaceString(url_redir, "?", "%3F");

//	url_redir 	= tools.replaceString(url_redir, "%3F", "?");

//---



            String url_redir = PortletTools.getString(ctxInstance.getPortletRequest(), "url", CtxURL.ctx());



//	String url 	= tools.replaceString(url_redir, "?", "%3F");

//	url		= tools.replaceString(url, "=", "%3D");



            String url = StringTools.rewriteURL(url_redir);



            url_redir = response.encodeURL(url_redir);



            url_redir = StringTools.replaceString(url_redir, "%3F", "?");



            String action = PortletTools.getString(ctxInstance.getPortletRequest(), "action");



            String username = PortletTools.getString(ctxInstance.getPortletRequest(), "username").trim();

            String password1 = PortletTools.getString(ctxInstance.getPortletRequest(), "password1").trim();

            String password2 = PortletTools.getString(ctxInstance.getPortletRequest(), "password2").trim();



            String first_name = PortletTools.getString(ctxInstance.getPortletRequest(), "first_name");

            String last_name = PortletTools.getString(ctxInstance.getPortletRequest(), "last_name");



            String email = PortletTools.getString(ctxInstance.getPortletRequest(), "email");

            String address = PortletTools.getString(ctxInstance.getPortletRequest(), "address");

            String phone = PortletTools.getString(ctxInstance.getPortletRequest(), "phone");





            PortletSession sess = ctxInstance.getPortletRequest().getPortletSession(true);

            BasketShopSession b = (BasketShopSession) sess.getAttribute(Constants.BASKET_SHOP_SESSION);



            if (b == null)

            {

                response.sendRedirect(response.encodeURL(CtxURL.ctx()));

                return true;

            }



//out.println(p.idFirm);

//if(true) return;



            AuthSession auth_ = (AuthSession) sess.getAttribute(Constants.AUTH_SESSION);

            if ((auth_ != null) && (auth_.checkAccess(db_, ctxInstance.getPortletRequest().getServerName())))

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

                    long id_user = AuthTools.addNewUser(db_,

                        first_name, last_name, "", ctxInstance.page.p.idFirm,

                        email, address, phone);



                    long id_auth_user;

                    try

                    {

                        id_auth_user = AuthTools.addUserAuth(db_, id_user,

                            ctxInstance.page.p.idFirm, 0, 0, username, password1,

                            true, false, false);



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



                    AuthTools.bindUserRole(db_, id_auth_user, "SHOP_BUYER");



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

                        ctxInstance.page.p.adminEmail,

                        "Confirm registration",

                        ctxInstance.getPortletRequest().getLocale());



                    if (auth_.checkAccess(db_, ctxInstance.getPortletRequest().getServerName()))

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

                    (!auth_.checkAccess(db_, ctxInstance.getPortletRequest().getServerName()))

                )



                {

                    if ((username != null) && (password1 != null))

                    {

                        auth_ = new AuthSession(username, password1);

                        if (auth_.checkAccess(db_, ctxInstance.getPortletRequest().getServerName()))

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



// public static void sendMessage(

// String message,

// String email_to,

// String subj,

// Locale loc)



// return true;



            }



        }

        catch (Exception e1)

        {

            try

            {

                cat.error("", e1);

                out.println(e1.toString() + "<br>");

            }

            catch (Exception e11)

            {

            }

        }

        return false;

    }



    public BasketShopSession(DatabaseAdapter db_, long id_shop_, AuthSession auth_session)

        throws Exception

    {

        v = new Vector();

        id_shop = id_shop_;

        id_order = db_.getSequenceNextValue("seq_order");



        initAuthSession(auth_session);



    }



    public void initAuthSession(AuthSession auth_session)

        throws PriceException

    {

        authSession = auth_session;

        String sql_ = "";

        PreparedStatement ps = null;

        DatabaseAdapter dbDyn = null;

        try

        {

            dbDyn = DatabaseAdapter.getInstance(true);



            if (authSession != null)

            {

                sql_ =

                    "insert into price_relate_user_order " +

                    "(id_order, id_user, date_create, id_shop)" +

                    "(select ?, id_user, sysdate, ? from auth_user where user_login = ?)";



                ps = dbDyn.prepareStatement(sql_);



                RsetTools.setLong(ps, 1, id_order);

                RsetTools.setLong(ps, 2, id_shop);

                ps.setString(3, dbDyn.toDB(authSession.getUserLogin()));

            }

            else

            {

                sql_ =

                    "insert into price_relate_user_order " +

                    "(id_order, id_user, date_create, id_shop) " +

                    "(select ?, null, sysdate, ? from dual) ";



                ps = dbDyn.prepareStatement(sql_);

                RsetTools.setLong(ps, 1, id_order);

                RsetTools.setLong(ps, 2, id_shop);

            }

            int i = ps.executeUpdate();



            if (cat.isDebugEnabled())

                cat.debug("count of inserted record - " + i);



            dbDyn.commit();

        }

        catch (Exception e1)

        {

            try

            {

                dbDyn.rollback();

            }

            catch (Exception e11)

            {

            }

            throw new PriceException(e1.toString());

        }

        finally

        {

            RsetTools.closePs(ps);

            ps = null;



            DatabaseAdapter.close(dbDyn);

            dbDyn = null;

        }



    }



    public void updateAuthSession(AuthSession auth_session)

        throws PriceException

    {

        authSession = auth_session;

        String sql_ = "";

        PreparedStatement ps = null;

        DatabaseAdapter dbDyn = null;

        try

        {

            if (authSession != null)

            {

                dbDyn = DatabaseAdapter.getInstance(true);



                sql_ =

                    "update price_relate_user_order " +

                    "set id_user = " +

                    "(select id_user from auth_user	where user_login = ?) " +

                    "where id_order = ? ";



                ps = dbDyn.prepareStatement(sql_);



                ps.setString(1, dbDyn.toDB(authSession.getUserLogin()));

                RsetTools.setLong(ps, 2, id_order);

                int i = ps.executeUpdate();



                if (cat.isDebugEnabled())

                    cat.debug("count of updated record - " + i);



                dbDyn.commit();

            }

        }

        catch (Exception e1)

        {

            try

            {

                dbDyn.rollback();

            }

            catch (Exception e11)

            {

            }

            throw new PriceException(e1.toString());

        }

        finally

        {

            RsetTools.closePs(ps);

            ps = null;



            DatabaseAdapter.close(dbDyn);

            dbDyn = null;

        }

    }



    public void removeOrder()

        throws PriceException

    {

        String sql_ =

            "delete from price_relate_user_order " +

            "where id_order = ? and id_shop = ? ";



        PreparedStatement ps = null;

        DatabaseAdapter dbDyn = null;

        try

        {

            dbDyn = DatabaseAdapter.getInstance(true);

            ps = dbDyn.prepareStatement(sql_);

            RsetTools.setLong(ps, 1, id_order);

            RsetTools.setLong(ps, 2, id_shop);

            int i = ps.executeUpdate();



            if (cat.isDebugEnabled())

                cat.debug("count of deleted record - " + i);



            dbDyn.commit();

        }

        catch (Exception e)

        {

            try

            {

                dbDyn.rollback();

            }

            catch (Exception e11)

            {

            }

            throw new PriceException(e.toString());

        }

        finally

        {

            RsetTools.closePs(ps);

            ps = null;



            DatabaseAdapter.close(dbDyn);

            dbDyn = null;

        }

    }



    public boolean isItemInBasket(long id_)

    {

        for (int i = 0; i < v.size(); i++)

        {

            ShopBasketItems item = (ShopBasketItems) v.elementAt(i);

            if (item.id_item == id_)

                return true;

        }

        return false;

    }



    public void addItem(long id_item_, int count, long id_shop_, String serverName)

        throws PriceException

    {



        DatabaseAdapter dbDyn = null;

        PreparedStatement ps = null;



        try

        {

            dbDyn = DatabaseAdapter.getInstance(true);



            PortalInfo p = PortalInfo.getInstance(dbDyn, serverName );



            if (id_shop_ != id_shop)

            {

                clear(id_shop_);

            }

//cat.debug("#1.0002");



            int size_vector = v.size();

            int i;

            for (i = 0; i < size_vector; i++)

            {

                ShopBasketItems it = (ShopBasketItems) v.elementAt(i);

                if (it.id_item == id_item_)

                {

                    it.count += count;

                    v.setElementAt(it, i);

                    break;

                }

            }



            if (cat.isDebugEnabled())

                cat.debug("#1.0003 i - " +i+" size_vector "+size_vector);



            ShopBasketItems itemBasket = null;

            if (i == size_vector)

            {

                itemBasket = new ShopBasketItems(id_item_, count);

                itemBasket.initItem( dbDyn );



                v.addElement( itemBasket );

            }



            String sql_ =

                "update PRICE_ORDER " +

                "set COUNT=? " +

                "where ID_ITEM=? and ID_ORDER=? ";



            ps = dbDyn.prepareStatement(sql_);



//cat.debug("#1.0004");



            ps.setInt(1, count);

            RsetTools.setLong(ps, 2, id_item_);

            RsetTools.setLong(ps, 3, id_order);



            int update = ps.executeUpdate();



            if (update == 0)

            {

                long seqValue = dbDyn.getSequenceNextValue("seq_price_order");

                sql_ =

                    "insert into PRICE_ORDER " +

                    "(ID_PRICE_ORDER, ID_ORDER, ID_ITEM, COUNT, ITEM, PRICE, CURRENCY )" +

                    "(select ?, ?, ID_ITEM, ?, ITEM, ?, CURRENCY " +

                    "from PRICE_LIST " +

                    "where ID_ITEM = ? and ID_SHOP = ?) ";





                ps = dbDyn.prepareStatement(sql_);

                RsetTools.setLong(ps, 1, seqValue);

                RsetTools.setLong(ps, 2, id_order);

                ps.setInt(3, count);

                ps.setDouble(4, itemBasket.price);

                RsetTools.setLong(ps, 5, id_item_);

                RsetTools.setLong(ps, 6, id_shop);



                update = ps.executeUpdate();

            } // if update

            dbDyn.commit();

//cat.debug("#1.0006");

        }

        catch (Exception e)

        {

            cat.error("Error add id_item_ to shop basket", e);

            try

            {

                dbDyn.rollback();

            }

            catch (Exception e11)

            {

            }

            throw new PriceException(e.toString());

        }

        finally

        {

            RsetTools.closePs(ps);

            ps = null;



            DatabaseAdapter.close(dbDyn);

            dbDyn = null;

        }

    }



    public void setItem(long item, int count, long id_shop_)

        throws PriceException

    {



        if (id_shop_ != id_shop)

        {

            clear(id_shop_);

        }



        int size_vector = v.size();

        int i;

        for (i = 0; i < size_vector; i++)

        {

            ShopBasketItems it = (ShopBasketItems) v.elementAt(i);

            if (it.id_item == item)

            {

                it.count = count;

                v.setElementAt(it, i);

                break;

            }

        }

        if (i == size_vector)

            v.addElement(new ShopBasketItems(item, count));



        String sql_ =

            "update price_order " +

            "set count = ? " +

            "where id_order = ? and id_item = ? 	";



        PreparedStatement ps = null;

        DatabaseAdapter dbDyn = null;

        try

        {

            dbDyn = DatabaseAdapter.getInstance(true);

            ps = dbDyn.prepareStatement(sql_);

            ps.setInt(1, count);

            RsetTools.setLong(ps, 2, id_order);

            RsetTools.setLong(ps, 3, item);



            int update = ps.executeUpdate();



            if (cat.isDebugEnabled())

                cat.debug("count of updated record - " + update);



            dbDyn.commit();

        }

        catch (Exception e)

        {

            try

            {

                dbDyn.rollback();

            }

            catch (Exception e11)

            {

            }

            throw new PriceException(e.toString());

        }

        finally

        {

            RsetTools.closePs(ps);

            ps = null;



            DatabaseAdapter.close(dbDyn);

            dbDyn = null;

        }

    }



    public void delItem(long item, long id_shop_)

        throws PriceException

    {



        if (id_shop_ != id_shop)

        {

            clear(id_shop_);

            return;

        }



        int size_vector = v.size();

        int i;

        for (i = 0; i < size_vector; i++)

        {

            ShopBasketItems it = (ShopBasketItems) v.elementAt(i);

            if (it.id_item == item)

            {

                v.removeElementAt(i);

                break;

            }

        }



        String sql_ =

            "delete from price_order where id_order = ? and id_item = ? ";



        PreparedStatement ps = null;

        DatabaseAdapter dbDyn = null;

        try

        {

            dbDyn = DatabaseAdapter.getInstance(true);

            ps = dbDyn.prepareStatement(sql_);



            RsetTools.setLong(ps, 1, id_order);

            RsetTools.setLong(ps, 2, item);



            int update = ps.executeUpdate();



            if (cat.isDebugEnabled())

                cat.debug("count of deleted record - " + update);



            dbDyn.commit();

        }

        catch (Exception e)

        {

            try

            {

                dbDyn.rollback();

            }

            catch (Exception e11)

            {

            }

            throw new PriceException(e.toString());

        }

        finally

        {

            RsetTools.closePs(ps);

            ps = null;



            DatabaseAdapter.close(dbDyn);

            dbDyn = null;

        }

    }



    public void clear(long id_shop_)

        throws PriceException

    {

        v.clear();

        id_shop = id_shop_;



        String sql_ =

            "delete from price_order where id_order = ? ";



        PreparedStatement ps = null;

        DatabaseAdapter dbDyn = null;

        try

        {

            dbDyn = DatabaseAdapter.getInstance(true);

            ps = dbDyn.prepareStatement(sql_);

            RsetTools.setLong(ps, 1, id_order);



            int update = ps.executeUpdate();



            if (cat.isDebugEnabled())

                cat.debug("count of delted record - " + update);



            sql_ =

                "update price_relate_user_order set id_shop = ? " +

                "where id_order = ? ";



            ps = dbDyn.prepareStatement(sql_);

            RsetTools.setLong(ps, 1, id_shop);

            RsetTools.setLong(ps, 2, id_order);

            update = ps.executeUpdate();



            if (cat.isDebugEnabled())

                cat.debug("count of updated record - " + update);



            dbDyn.commit();

        }

        catch (Exception e)

        {

            try

            {

                dbDyn.rollback();

            }

            catch (Exception e11)

            {

            }

            throw new PriceException(e.toString());

        }

        finally

        {

            RsetTools.closePs(ps);

            ps = null;



            DatabaseAdapter.close(dbDyn);

            dbDyn = null;

        }

    }

*/

}