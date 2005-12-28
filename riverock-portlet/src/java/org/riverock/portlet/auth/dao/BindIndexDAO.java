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
package org.riverock.portlet.auth.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.riverock.common.tools.RsetTools;
import org.riverock.common.tools.StringTools;
import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.db.DatabaseManager;
import org.riverock.module.action.ModuleActionRequest;
import org.riverock.module.exception.ActionException;
import org.riverock.module.web.user.ModuleUser;
import org.riverock.portlet.auth.bean.BindUserBean;
import org.riverock.portlet.auth.bean.BindUserListBean;
import org.riverock.sso.utils.AuthHelper;

/**
 * @author SMaslyukov
 *         Date: 22.05.2005
 *         Time: 16:51:28
 *         $Id$
 */
public class BindIndexDAO {
    static private final Log log = LogFactory.getLog( BindIndexDAO.class );

    public BindUserListBean execute( ModuleActionRequest moduleActionRequest, ModuleUser moduleUser ) throws ActionException {

        DatabaseAdapter db_ = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            db_ = DatabaseAdapter.getInstance();

            BindUserListBean userListBean = new BindUserListBean();
            userListBean.setCompany( moduleUser.isCompany() );
            userListBean.setGroupCompany( moduleUser.isGroupCompany() );
            userListBean.setHolding( moduleUser.isHolding() );
            userListBean.setAddButton(moduleActionRequest.getResourceBundle().getString("button.add"));
            userListBean.setChangeButton( moduleActionRequest.getResourceBundle().getString( "button.change" ) );
            userListBean.setDeleteButton( moduleActionRequest.getResourceBundle().getString( "button.delete" ) );
            userListBean.setBundle( moduleActionRequest.getResourceBundle() );

            String sql_ = null;
            switch( db_.getFamaly() ) {
                case DatabaseManager.MYSQL_FAMALY:
                    sql_ = 
                        "select a.id_auth_user, a.id_user, a.user_login, " +
                        "       a.is_service, a.is_road, a.is_use_current_firm, a.is_root, a.id_road, " +
                        "       a.id_service, " +
                        "       b.LAST_NAME, b.FIRST_NAME, b.MIDDLE_NAME, " +
                        "       c.id_firm, c.short_name " +
                        " " +
                        "from   WM_AUTH_USER a, WM_LIST_USER b, WM_LIST_COMPANY c " +
                        "where  a.id_user = b.id_user and " +
                        "       a.id_firm = c.id_firm and " +
                        "       b.ID_FIRM in ( " + AuthHelper.getGrantedCompanyId( db_, moduleUser.getUserLogin() ) + " ) " +
                        "order by c.id_firm asc, c.short_name asc, b.LAST_NAME asc, b.FIRST_NAME asc, b.MIDDLE_NAME asc ";

                    ps = db_.prepareStatement( sql_ );
                    break;
                default:
                    sql_ = 
                        "select a.id_auth_user, a.id_user, a.user_login, " +
                        "                a.is_service, a.is_road, a.is_use_current_firm, a.is_root, a.id_road, " +
                        "                a.id_service, " +
                        "             b.LAST_NAME, b.FIRST_NAME, b.MIDDLE_NAME, " +
                        "             c.id_firm, c.short_name " +
                        " " +
                        "            from    WM_AUTH_USER a, WM_LIST_USER b, WM_LIST_COMPANY c " +
                        "            where   a.id_user = b.id_user and " +
                        "                    a.id_firm = c.id_firm and " +
                        "                    b.ID_FIRM in " +
                        "                    ( " +
                        "            select  a01.id_firm " +
                        "            from    WM_AUTH_USER a01 " +
                        "            where   a01.is_use_current_firm = 1 " +
                        "                    and a01.user_login = ? " +
                        "            union " +
                        "            select  d02.id_firm " +
                        "            from    WM_AUTH_USER a02, WM_LIST_R_GR_COMP_COMP d02 " +
                        "            where   a02.is_service = 1 and a02.id_service = d02.id_service " +
                        "                    and a02.user_login = ? " +
                        "            union " +
                        "            select  e03.id_firm " +
                        "            from    WM_AUTH_USER a03, WM_LIST_R_HOLDING_GR_COMPANY d03, WM_LIST_R_GR_COMP_COMP e03 " +
                        "            where   a03.is_road = 1 and a03.id_road = d03.id_road and d03.id_service = e03.id_service " +
                        "                    and a03.user_login = ? " +
                        "            union " +
                        "            select  b04.id_firm " +
                        "            from    WM_AUTH_USER a04, WM_LIST_COMPANY b04 " +
                        "            where   a04.is_root = 1 and a04.user_login = ? " +
                        "            ) " +
                        "            order by c.id_firm asc, c.short_name asc, b.LAST_NAME asc, b.FIRST_NAME asc, b.MIDDLE_NAME asc ";

                    ps = db_.prepareStatement( sql_ );
                    ps.setString( 1, moduleUser.getUserLogin() );
                    ps.setString( 2, moduleUser.getUserLogin() );
                    ps.setString( 3, moduleUser.getUserLogin() );
                    ps.setString( 4, moduleUser.getUserLogin() );

                    break;
            }
            rs = ps.executeQuery();

            List<BindUserBean> users = new ArrayList<BindUserBean>();
            while( rs.next() ) {

                BindUserBean userBean = new BindUserBean();
                userBean.setUserName( StringTools.getUserName( RsetTools.getString( rs, "LAST_NAME" ),
                    RsetTools.getString( rs, "FIRST_NAME" ),
                    RsetTools.getString( rs, "MIDDLE_NAME" ) ) );
                userBean.setCompanyName( RsetTools.getLong( rs, "ID_FIRM" ) + ", " +
                    RsetTools.getString( rs, "short_name", "&lt;not defined&gt;" ) );
                userBean.setUserLogin( RsetTools.getString( rs, "user_login", "&nbsp;" ) );
                userBean.setCompany( RsetTools.getInt( rs, "is_use_current_firm", 0 ) == 1 );
                userBean.setGroupCompany( RsetTools.getInt( rs, "is_service", 0 ) == 1 );
                userBean.setHolding( RsetTools.getInt( rs, "is_road", 0 ) == 1 );
                userBean.setCompanyLevel( moduleActionRequest.getResourceBundle().getString( userBean.isCompany() ? "yesno.yes" : "yesno.no" ) );
                userBean.setGroupCompanyLevel( moduleActionRequest.getResourceBundle().getString( userBean.isGroupCompany() ? "yesno.yes" : "yesno.no" ) );
                userBean.setHoldingLevel( moduleActionRequest.getResourceBundle().getString( userBean.isHolding() ? "yesno.yes" : "yesno.no" ) );
                userBean.setAuthUserId( RsetTools.getLong( rs, "id_auth_user" ) );
                users.add( userBean );
            }
            userListBean.setUsers( users );
            return userListBean;
        }
        catch( Exception e ) {
            String es = "Exception in BindIndex";
            log.error( es, e );
            throw new ActionException( es, e );
        }
        finally {
            DatabaseManager.close( db_, rs, ps );
            rs = null;
            ps = null;
            db_ = null;
        }
    }
}
