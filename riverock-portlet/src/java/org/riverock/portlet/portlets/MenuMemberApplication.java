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



package org.riverock.portlet.portlets;



import java.sql.PreparedStatement;

import java.sql.ResultSet;

import java.util.Vector;



import org.apache.log4j.Logger;



import org.riverock.generic.db.DatabaseAdapter;

import org.riverock.generic.db.DatabaseManager;

import org.riverock.common.tools.RsetTools;

import org.riverock.sso.a3.AuthInfo;



/**

 * Класс Application прденазначен для хранения описания приложений.

 *

 * Project millengine

 * Copyright Serg Maslyukov, 1999-2002

 *

 * $Id$

 */

public class MenuMemberApplication

//    implements PortletGetList

{

    private static Logger cat = Logger.getLogger("org.riverock.portlet.MenuMemberApplication" );



    /**

     название приложения

     */

    public String applicationName = "";



    public String applicationCode = null;

    /**

     url доступа к приложениям

     */

//    public String url = "";

    /**

     значение для порядка вывода приложений

     */

    public int order = 0;



    public int applRecordNumber = 0;



    public Vector subMenu = new Vector(0);

    public Long applicationID;



//    public PortletParameter param = null;



    protected void finalize() throws Throwable

    {

        applicationName = null;

        applicationCode = null;

//        url = null;



        if (subMenu != null)

        {

            subMenu.clear();

            subMenu = null;

        }



//        param = null;



        super.finalize();

    }



/*

    public void setParameter(PortletParameter param_)

    {

        if(cat.isDebugEnabled())

            cat.debug("Portlet parametr is "+param_);



        this.param = param_;

        if (subMenu != null)

        {

            for (int i = 0; i < subMenu.size(); i++)

            {

                Portlet obj = (Portlet) subMenu.elementAt(i);

                obj.setParameter(param_);

            }

        }

    }

*/

    public boolean isXml(){ return false; }

    public boolean isHtml(){ return false; }



    public MenuMemberApplication()

    {

    }



    public Vector getModuleList()

    {

        return subMenu;

    }



    private final static String sql_ =

            "select distinct z.CODE_ARM,a.CODE_OBJECT_ARM,  a.NAME_OBJECT_ARM, "+

            "a.url, a.order_field, a.is_new  " +



            "from   AUTH_OBJECT_ARM a," +

            "(" +

            "select " +

            "        a.user_login, " +

            "        f.code_arm, " +

            "        d.id_object_arm, " +

            "	 e.id_arm, " +

            "	 e.is_new  " +

            "from    auth_user a, " +

            "        auth_relate_accgroup b, " +

            "        auth_relate_right_arm d, " +

            "        auth_object_arm e, " +

            "        auth_arm f " +

            "where   a.id_auth_user=b.id_auth_user and " +

            "        b.id_access_group = d.id_access_group and " +

            "        d.id_object_arm = e.id_object_arm and " +

            "        e.id_arm = f.id_arm " +

            "union " +

            "select  a1.user_login, f1.code_arm, d1.id_object_arm, f1.id_arm, d1.is_new   " +

            "from    auth_user a1, auth_object_arm d1,  auth_arm f1 " +

            "where   a1.is_root=1 and d1.id_arm = f1.id_arm " +

            ") z " +

            "where  z.user_login=? and " +

            "       a.id_object_arm = z.id_object_arm and " +

            "       z.id_arm=? and a.url is not null " +

            "order by ORDER_FIELD ASC";

/*

            "select distinct z.CODE_ARM,a.CODE_OBJECT_ARM,  a.NAME_OBJECT_ARM, "+

            "a.url, a.order_field, a.is_new , z.JSP_METHOD " +

            "from   AUTH_OBJECT_ARM a," +

            "(" +

            "select " +

            "        a.user_login, " +

            "        f.code_arm, " +

            "        d.id_object_arm, " +

            "	 e.id_arm, " +

            "	 e.is_new, f.JSP_METHOD  " +

            "from    auth_user a, " +

            "        auth_relate_accgroup b, " +

            "        auth_relate_right_arm d, " +

            "        auth_object_arm e, " +

            "        auth_arm f " +

            "where   a.id_auth_user=b.id_auth_user and " +

            "        b.id_access_group = d.id_access_group and " +

            "        d.id_object_arm = e.id_object_arm and " +

            "        e.id_arm = f.id_arm " +

            "union " +

            "select  a1.user_login, f1.code_arm, d1.id_object_arm, f1.id_arm, d1.is_new , f1.JSP_METHOD " +

            "from    auth_user a1, auth_object_arm d1,  auth_arm f1 " +

            "where   a1.is_root=1 and d1.id_arm = f1.id_arm " +

            ") z " +

            "where  z.user_login=? and " +

            "       a.id_object_arm = z.id_object_arm and " +

            "       z.id_arm=? and a.url is not null " +

            "order by ORDER_FIELD ASC";

*/



    public MenuMemberApplication(AuthInfo authInfo, ResultSet rs)

            throws PortletException

    {

        this(authInfo, rs, 0);

    }



    public MenuMemberApplication(AuthInfo authInfo, ResultSet rs, int recordNumber_)

            throws PortletException

    {

        if (authInfo == null)

            return;



        PreparedStatement ps = null;

        ResultSet rset = null;

        DatabaseAdapter db_ = null;

        Vector vv = new Vector();



        try

        {

            applicationName = RsetTools.getString(rs, "NAME_ARM" );

            applicationCode = RsetTools.getString(rs, "CODE_ARM" );



//            url = ""; //RsetTools.getString(rs, "JSP_METHOD" );

            order = RsetTools.getInt(rs, "ORDER_FIELD" , new Integer(0)).intValue();



            applicationID = RsetTools.getLong(rs, "ID_ARM");

            applRecordNumber = recordNumber_;



            db_ = DatabaseAdapter.getInstance(false);



//            if (cat.isDebugEnabled())

//                cat.debug("SQL: \n" + sql_);



            ps = db_.prepareStatement(sql_);



            ps.setString(1, authInfo.userLogin);

            ps.setObject(2, applicationID);



            rset = ps.executeQuery();

            int moduleRecordNumber = 0;

            while (rset.next())

            {

                MenuMemberModule mod = new MenuMemberModule(rset, applicationCode);

                mod.modRecordNumber = moduleRecordNumber++;

                mod.applRecordNumber = applRecordNumber;

                vv.addElement(mod);

            }

        }

        catch (Exception e1)

        {

            cat.error("Error get member application", e1);

            throw new PortletException(e1.toString());

        }

        finally

        {

            DatabaseManager.close(rset, ps);

            rset = null;

            ps = null;

        }



        if (vv.size() > 0)

            subMenu = vv;

    }



/*

    public byte[] getPlainHTML()

    {

        return null;

    }



    public byte[] getXml(String rootElement) throws Exception

    {

        return "".getBytes();

    }

    public byte[] getXml(){ return "".getBytes(); }



    public Vector getList(Long idSiteCtxLangCatalog, Long idContext)

    {

        return null;

    }

*/

}

