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

import java.util.List;



import org.riverock.generic.db.DatabaseAdapter;

import org.riverock.generic.db.DatabaseManager;

import org.riverock.portlet.schema.portlet.menu_member.MenuMemberApplicationType;

import org.riverock.portlet.schema.portlet.menu_member.MenuMemberModuleType;

import org.riverock.portlet.schema.portlet.menu_member.MenuMemberType;

import org.riverock.portlet.main.Constants;

import org.riverock.common.tools.RsetTools;

import org.riverock.generic.tools.XmlTools;

import org.riverock.webmill.portlet.Portlet;

import org.riverock.webmill.portlet.PortletResultObject;

import org.riverock.webmill.portlet.PortletGetList;

import org.riverock.webmill.portlet.PortletParameter;

import org.riverock.webmill.portlet.CtxURL;

import org.riverock.sso.a3.AuthInfo;

import org.riverock.sso.a3.AuthTools;



import org.apache.log4j.Logger;



public class MenuMember implements Portlet, PortletResultObject, PortletGetList

{

    private static Logger log = Logger.getLogger(MenuMember.class );



    private MenuMemberType menu = new MenuMemberType();



    private PortletParameter param = null;



    protected void finalize() throws Throwable

    {

        super.finalize();

    }



    public MenuMember(){}



    public void setParameter(PortletParameter param_)

    {

        this.param = param_;

    }



    public boolean isXml(){ return true; }

    public boolean isHtml(){ return false; }



    public PortletResultObject getInstance(DatabaseAdapter db_)

        throws Exception

    {

        PreparedStatement ps = null;

        ResultSet rset = null;



        try

        {

            AuthInfo authInfo = AuthTools.getAuthInfo( param.getRequest() );



            if (authInfo == null)

                return this;



            String sql_ =

                "select a.user_login, f.code_arm, f.order_field, f.id_arm, f.name_arm "+

                "from auth_user a, auth_relate_accgroup b, auth_relate_right_arm d, auth_object_arm e, auth_arm f "+

                "where a.id_auth_user=b.id_auth_user and b.id_access_group = d.id_access_group and " +

                "d.id_object_arm = e.id_object_arm and e.id_arm = f.id_arm and a.USER_LOGIN=? "+

                "union "+

                "select a1.user_login, f1.code_arm, f1.order_field, f1.id_arm, f1.name_arm "+

                "from auth_user a1, auth_arm f1 "+

                "where a1.is_root=1 and a1.USER_LOGIN=? ";



//            db_ = DatabaseAdapter.getInstance(false);

            ps = db_.prepareStatement(sql_);



            ps.setString(1, authInfo.userLogin);

            ps.setString(2, authInfo.userLogin);



            rset = ps.executeQuery();

            int recordNumber = 0;

            while (rset.next())

            {

                MenuMemberApplicationType appl =

                    getApplication(authInfo, rset, recordNumber++, db_);

                if (appl != null)

                {

                    menu.addMenuMemberApplication( appl );

                }

            }

        }

        catch (Exception e)

        {

            log.error("Error get list of member application", e);

            throw e;

        }

        finally

        {

            DatabaseManager.close(rset, ps);

            rset = null;

            ps = null;

        }

        return this;

    }



    private void setModuleUrl()

        throws Exception

    {

/*

        try

        {

        if (log.isDebugEnabled())

        {

            log.debug("PortletParam - " + param);

            if (param != null)

            {

                log.debug("PortletParam  response - " + param.response);

                log.debug("PortletParam  param.response.encodeURL( CtxURL.ctx()  ) -  " + param.response.encodeURL( CtxURL.ctx()  ));

                log.debug("PortletParam  getMemberTemplate - " + param.jspPage.p.getMemberTemplate() );

                log.debug("PortletParam  nameTemplate - " + param.jspPage.p.getMemberTemplate().getNameTemplate() );



                log.debug("Module url - "+

                    param.response.encodeURL( CtxURL.ctx()  ) + '?'+

                    Constants.NAME_TYPE_CONTEXT_PARAM    +'='+ Constants .CTX_TYPE_MEMBER   + '&'+

                    Constants.MEMBER_NAME_APPL_PARAM    +'='+ applicationCode + '&' +

                    Constants.MEMBER_NAME_MOD_PARAM+"="+ moduleCode + '&' +

                    Constants.NAME_TEMPLATE_CONTEXT_PARAM    + '=' + param.jspPage.p.getMemberTemplate().getNameTemplate()

                );

            }

        }



        return param.response.encodeURL( CtxURL.ctx()  ) + '?'+

            Constants.NAME_TYPE_CONTEXT_PARAM    +'='+ Constants .CTX_TYPE_MEMBER   + '&'+

//            Constants.NAME_TYPE_CONTEXT_PARAM    +'='+ Constants .CTX_TYPE_MEMBER_CONTROLLER   + '&'+

//            Constants.NAME_TYPE_CONTEXT_PARAM    +'='+ Constants .CTX_TYPE_MEMBER_VIEW    + '&'+



            Constants.MEMBER_NAME_APPL_PARAM    +'='+ applicationCode + '&' +

            Constants.MEMBER_NAME_MOD_PARAM+"="+ moduleCode + '&' +

            Constants.NAME_TEMPLATE_CONTEXT_PARAM    + '=' + param.jspPage.p.getMemberTemplate().getNameTemplate();



        }

        catch(Exception e)

        {

            log.error("Error getModuleUrl ", e);

            throw e;

        }

*/

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

/*

        String s = ("<br><FONT face=Arial size=-1>");





        for (int i=0; i< v.size(); i++)

        {

            MenuMemberApplication ap = (MenuMemberApplication)v.elementAt(i);

            s += ("<table width=\"100%\"><tr><th>"+ap.applicationName+"</th></tr></table>");

            s += ("<table border=\"0\" cellPadding=\"0\" cellSpacing=\"0\"><tr><td>&nbsp;&nbsp;</td><td>");



            for (int j=0; j<ap.subMenu.size(); j++)

            {

                MenuMemberModule mod = (MenuMemberModule)ap.subMenu.elementAt(j);

                if (mod.isNew==1)

                    s += ("<a class=\"linktoc\" "+

                            "href=\""+param.response.encodeURL( mod.url )+

                            "&"+param.jspPage.addURL+

                            "\">"+mod.moduleName+"</a><br>");

                else

                    s += ("<a class=\"linktoc\" "+

                            "href=\""+param.response.encodeURL( mod.url )+

                            "?"+param.jspPage.addURL+

                            "\">"+mod.moduleName+"</a><br>");



            }

            s += ("</td></tr></table>");

        }

        s += ("</font> ");



        return s;

*/

        return null;

    }



    public byte[] getXml(String rootElement) throws Exception

    {

        return XmlTools.getXml( menu, rootElement );

    }



    public byte[] getXml()

            throws Exception

    {

        return getXml( "MenuMember" );

    }



    public List getList(Long idSiteCtxLangCatalog, Long idContext)

    {

        return null;

    }



    private final static String sql_ =

        "select distinct z.CODE_ARM,a.CODE_OBJECT_ARM,  a.NAME_OBJECT_ARM, "+

        "a.url, a.order_field, a.is_new  " +

        "from   AUTH_OBJECT_ARM a," +

        "(" +

        "select " +

        "    a.user_login, " +

        "    f.code_arm, " +

        "    d.id_object_arm, " +

        "	 e.id_arm, " +

        "	 e.is_new  " +

        "from    auth_user a, " +

        "    auth_relate_accgroup b, " +

        "    auth_relate_right_arm d, " +

        "    auth_object_arm e, " +

        "    auth_arm f " +

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



    private MenuMemberApplicationType getApplication(AuthInfo authInfo, ResultSet rs, DatabaseAdapter db_)

        throws PortletException

    {

        return getApplication(authInfo, rs, 0, db_);

    }



    private MenuMemberApplicationType getApplication(AuthInfo authInfo, ResultSet rs, int recordNumber_, DatabaseAdapter db_)

            throws PortletException

    {

        if (authInfo == null)

            return null;



        PreparedStatement ps = null;

        ResultSet rset = null;



        MenuMemberApplicationType appl = new MenuMemberApplicationType();

        try

        {

            appl.setApplicationName( RsetTools.getString(rs, "NAME_ARM" ) );

            appl.setApplicationCode( RsetTools.getString(rs, "CODE_ARM" ) );



            appl.setOrderField( RsetTools.getInt(rs, "ORDER_FIELD" ));



            appl.setApplicationId( RsetTools.getLong(rs, "ID_ARM") );

            appl.setApplicationRecordNumber( new Integer(recordNumber_) );



            ps = db_.prepareStatement(sql_);



            ps.setString(1, authInfo.userLogin);

            RsetTools.setLong(ps, 2, appl.getApplicationId() );



            rset = ps.executeQuery();

            int moduleRecordNumber = 0;

            while (rset.next())

            {

                MenuMemberModuleType mod = getModule(rset, appl.getApplicationCode() );

                if (mod!=null)

                {

                    mod.setModuleRecordNumber( new Integer(moduleRecordNumber++) );

                    mod.setApplRecordNumber( appl.getApplicationRecordNumber() );

                    appl.addMenuMemberModule( mod );

                }

            }

            return appl;

        }

        catch (Exception e1)

        {

            log.error("Error get member application", e1);

            throw new PortletException(e1.toString());

        }

        finally

        {

            DatabaseManager.close(rset, ps);

            rset = null;

            ps = null;

        }



    }



    private MenuMemberModuleType getModule(ResultSet rs, String applicationCode_)

            throws Exception

    {

        if (rs==null)

            return null;



        MenuMemberModuleType mod = new MenuMemberModuleType();

        mod.setApplicationCode( applicationCode_ );

        try

        {

            mod.setModuleName( RsetTools.getString(rs, "NAME_OBJECT_ARM") );

            mod.setModuleCode( RsetTools.getString(rs, "CODE_OBJECT_ARM") );

            mod.setOrderField( RsetTools.getInt(rs, "ORDER_FIELD") );

            mod.setIsNew( new Boolean(new Integer(1).equals(RsetTools.getInt(rs, "IS_NEW"))) );



            try

            {

                if (log.isDebugEnabled())

                {

                    log.debug("PortletParam - " + param);

                    if (param != null)

                    {

                        log.debug("PortletParam  response - " + param.getResponse());

                        log.debug("PortletParam  param.response.encodeURL( CtxURL.ctx()  ) -  " + param.getResponse().encodeURL( CtxURL.ctx()  ));

                        log.debug("PortletParam  getMemberTemplate - " + param.getJspPage().p.getMemberTemplate() );

                        log.debug("PortletParam  nameTemplate - " + param.getJspPage().p.getMemberTemplate().getNameTemplate() );



                        log.debug("Module url - "+

                            param.getResponse().encodeURL( CtxURL.ctx()  ) + '?'+

                                  param.getJspPage().getAsURL()+

                            Constants.NAME_TYPE_CONTEXT_PARAM + '=' + Constants .CTX_TYPE_MEMBER   + '&'+

                            Constants.MEMBER_NAME_APPL_PARAM + '=' + applicationCode_ + '&' +

                            Constants.MEMBER_NAME_MOD_PARAM + '=' + mod.getModuleCode() + '&' +

                            Constants.NAME_TEMPLATE_CONTEXT_PARAM + '=' + param.getJspPage().p.getMemberTemplate().getNameTemplate()

                        );

                    }

                }



                mod.setModuleUrl(

                    param.getResponse().encodeURL( CtxURL.ctx()  ) + '?'+

                    param.getJspPage().getAsURL()+

                    Constants.NAME_TYPE_CONTEXT_PARAM    +'='+ Constants .CTX_TYPE_MEMBER   + '&'+

                    Constants.MEMBER_NAME_APPL_PARAM  + '=' + applicationCode_ + '&' +

                    Constants.MEMBER_NAME_MOD_PARAM + '=' + mod.getModuleCode() + '&' +

                    Constants.NAME_TEMPLATE_CONTEXT_PARAM    + '=' + param.getJspPage().p.getMemberTemplate().getNameTemplate()

                );



            }

            catch(Exception e)

            {

                log.error("Error getModuleUrl ", e);

                throw e;

            }



            return mod;

        }

        catch (Exception e)

        {

            log.error("Error get member module", e);

            throw e;

        }

    }

}