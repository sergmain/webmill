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
package org.riverock.portlet.member;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Locale;

import javax.portlet.PortletSession;
import javax.portlet.PortletException;

import org.apache.log4j.Logger;
import org.riverock.common.tools.RsetTools;
import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.db.DatabaseManager;
import org.riverock.generic.tools.XmlTools;
import org.riverock.portlet.main.Constants;
import org.riverock.portlet.schema.portlet.menu_member.MenuMemberApplicationType;
import org.riverock.portlet.schema.portlet.menu_member.MenuMemberModuleType;
import org.riverock.portlet.schema.portlet.menu_member.MenuMemberType;
import org.riverock.sso.a3.AuthInfo;
import org.riverock.sso.a3.AuthTools;

import org.riverock.webmill.portlet.Portlet;
import org.riverock.webmill.portlet.PortletGetList;
import org.riverock.webmill.portlet.PortletParameter;
import org.riverock.webmill.portlet.PortletResultObject;
import org.riverock.webmill.portlet.CtxInstance;
import org.riverock.webmill.schema.site.SiteTemplate;

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

    SiteTemplateMember memberTemplates = null;
    CtxInstance ctxInstance = null;

    public PortletResultObject getInstance(DatabaseAdapter db_)
        throws PortletException
    {
        PreparedStatement ps = null;
        ResultSet rset = null;

        try
        {
            AuthInfo authInfo = AuthTools.getAuthInfo( param.getPortletRequest() );

            if (authInfo == null)
                return this;

            PortletSession session = param.getPortletRequest().getPortletSession();
            ctxInstance = (CtxInstance)session.getAttribute( org.riverock.webmill.main.Constants.PORTLET_REQUEST_SESSION );
            memberTemplates = SiteTemplateMember.getInstance(db_, ctxInstance.getPortalInfo().getSiteId());


            String sql_ =
                "select a.user_login, f.code_arm, f.order_field, f.id_arm, f.name_arm "+
                "from auth_user a, auth_relate_accgroup b, auth_relate_right_arm d, auth_object_arm e, auth_arm f "+
                "where a.id_auth_user=b.id_auth_user and b.id_access_group = d.id_access_group and " +
                "d.id_object_arm = e.id_object_arm and e.id_arm = f.id_arm and a.USER_LOGIN=? "+
                "union "+
                "select a1.user_login, f1.code_arm, f1.order_field, f1.id_arm, f1.name_arm "+
                "from auth_user a1, auth_arm f1 "+
                "where a1.is_root=1 and a1.USER_LOGIN=? ";

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
        catch (Throwable e)
        {
            String es = "Error get list of member application";
            log.error(es, e);
            throw new PortletException(es, e);
        }
        finally
        {
            DatabaseManager.close(rset, ps);
            rset = null;
            ps = null;
        }
        return this;
    }

    public PortletResultObject getInstance(DatabaseAdapter db__, Long id) throws PortletException
    {
        return getInstance( db__ );
    }

    public PortletResultObject getInstanceByCode(DatabaseAdapter db__, String portletCode_) throws PortletException
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

    String sql_mysql =
        "select distinct f.CODE_ARM, a1.CODE_OBJECT_ARM,  a1.NAME_OBJECT_ARM, a1.url, a1.order_field, a1.is_new "+
        "from    AUTH_OBJECT_ARM a1, auth_user a, auth_relate_accgroup b, auth_relate_right_arm d, auth_object_arm e, auth_arm f "+
        "where   a.id_auth_user=b.id_auth_user and b.id_access_group = d.id_access_group and d.id_object_arm = e.id_object_arm and "+
        "        e.id_arm = f.id_arm and a.user_login=? and a1.id_object_arm = d.id_object_arm and e.id_arm=? and a1.url is not null "+
        "union "+
        "select distinct f01.CODE_ARM, a1.CODE_OBJECT_ARM,  a1.NAME_OBJECT_ARM, a1.url, a1.order_field, a1.is_new "+
        "from    AUTH_OBJECT_ARM a1, auth_user a01, auth_object_arm d01,  auth_arm f01 "+
        "where   a01.is_root=1 and d01.id_arm = f01.id_arm and a01.user_login=? and a1.id_object_arm = d01.id_object_arm and "+
        "       f01.id_arm=? and a1.url is not null "+
        "order by ORDER_FIELD ASC ";


//    private MenuMemberApplicationType getApplication(AuthInfo authInfo, ResultSet rs, DatabaseAdapter db_)
//        throws PortletException
//    {
//        return getApplication(authInfo, rs, 0, db_);
//    }

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

            ps = makeStatement( db_, authInfo.userLogin, appl.getApplicationId() );

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

    private PreparedStatement makeStatement(DatabaseAdapter db_, String userLogin, Long applicationId)
        throws SQLException
    {

        PreparedStatement ps = null;
        switch (db_.getFamaly())
        {
            case DatabaseManager.MYSQL_FAMALY:
                ps = db_.prepareStatement(sql_mysql);

                ps.setString(1, userLogin);
                RsetTools.setLong(ps, 2, applicationId );
                ps.setString(3, userLogin);
                RsetTools.setLong(ps, 4, applicationId );
                break;
            default:
                ps = db_.prepareStatement(sql_);

                ps.setString(1, userLogin);
                RsetTools.setLong(ps, 2, applicationId );
                break;
        }

        return ps;
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
                        log.debug("PortletParam  param.response.encodeURL( ctxInstance.ctx()  ) -  " + param.getResponse().encodeURL( CtxInstance.ctx()  ));
                        log.debug("PortletParam  getMemberTemplate - " + getMemberTemplate() );
                        log.debug("PortletParam  nameTemplate - " + getMemberTemplate().getNameTemplate() );

                        log.debug(
                            "Module url - "+ ctxInstance.url( Constants.CTX_TYPE_MEMBER, getMemberTemplate().getNameTemplate() ) + '&' +
                            Constants.MEMBER_NAME_APPL_PARAM + '=' + applicationCode_ + '&' +
                            Constants.MEMBER_NAME_MOD_PARAM + '=' + mod.getModuleCode()
                        );
                    }
                }

                mod.setModuleUrl(
                    ctxInstance.url( Constants.CTX_TYPE_MEMBER, getMemberTemplate().getNameTemplate() ) + '&' +
                    Constants.MEMBER_NAME_APPL_PARAM  + '=' + applicationCode_ + '&' +
                    Constants.MEMBER_NAME_MOD_PARAM + '=' + mod.getModuleCode()
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

    private SiteTemplate getMemberTemplate()
    {
        SiteTemplate st = null;

        if (log.isDebugEnabled())
            log.debug("Looking template for locale " + ctxInstance.getPortletRequest().getLocale().toString());

        st = getMemberTemplate(ctxInstance.getPortletRequest().getLocale());
        if (st != null)
            return st;

        if (log.isInfoEnabled())
            log.info("memberTemplate for Locale " + ctxInstance.getPortletRequest().getLocale().toString() + " not initialized");

        return new SiteTemplate();
    }

    public SiteTemplate getMemberTemplate(Locale locale)
    {
        if (memberTemplates==null || memberTemplates.memberTemplate == null)
            return null;

        return (SiteTemplate) memberTemplates.memberTemplate.get(locale.toString());
    }
}