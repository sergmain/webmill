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
package org.riverock.portlet.member;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.portlet.PortletException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.riverock.common.tools.RsetTools;
import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.db.DatabaseManager;
import org.riverock.sso.a3.AuthInfo;

/**
 * Project millengine
 * Copyright Serg Maslyukov, 1999-2002
 * <p/>
 * $Id$
 */
public class MenuMemberApplication {
    private static Log cat = LogFactory.getLog( MenuMemberApplication.class );

    /**
     * название приложения
     */
    public String applicationName = "";

    public String applicationCode = null;

    /**
     * значение для порядка вывода приложений
     */
    public int order = 0;

    public int applRecordNumber = 0;

    public List<MenuMemberModule> subMenu = new ArrayList<MenuMemberModule>();
    public Long applicationID = null;

    protected void finalize() throws Throwable {
        applicationName = null;
        applicationCode = null;

        if( subMenu != null ) {
            subMenu.clear();
            subMenu = null;
        }

        super.finalize();
    }

    public boolean isXml() {
        return false;
    }

    public boolean isHtml() {
        return false;
    }

    public MenuMemberApplication() {
    }

    public List getModuleList() {
        return subMenu;
    }

    private final static String sql_ =
        "select distinct z.CODE_ARM,a.CODE_OBJECT_ARM,  a.NAME_OBJECT_ARM, " +
        "a.url, a.order_field, a.is_new  " +
        "from   WM_AUTH_MODULEa," +
        "(" +
        "select " +
        "        a.user_login, " +
        "        f.code_arm, " +
        "        d.id_object_arm, " +
        "	 e.id_arm, " +
        "	 e.is_new  " +
        "from    WM_AUTH_USER a, " +
        "        WM_AUTH_RELATE_ACCGROUP b, " +
        "        WM_AUTH_RELATE_RIGHT_ARM d, " +
        "        WM_AUTH_MODULE e, " +
        "        WM_AUTH_APPLICATION f " +
        "where   a.id_auth_user=b.id_auth_user and " +
        "        b.id_access_group = d.id_access_group and " +
        "        d.id_object_arm = e.id_object_arm and " +
        "        e.id_arm = f.id_arm " +
        "union " +
        "select  a1.user_login, f1.code_arm, d1.id_object_arm, f1.id_arm, d1.is_new   " +
        "from    WM_AUTH_USER a1, WM_AUTH_MODULE d1,  WM_AUTH_APPLICATION f1 " +
        "where   a1.is_root=1 and d1.id_arm = f1.id_arm " +
        ") z " +
        "where  z.user_login=? and " +
        "       a.id_object_arm = z.id_object_arm and " +
        "       z.id_arm=? and a.url is not null " +
        "order by ORDER_FIELD ASC";

    public MenuMemberApplication( AuthInfo authInfo, ResultSet rs )
        throws PortletException {
        this( authInfo, rs, 0 );
    }

    public MenuMemberApplication( AuthInfo authInfo, ResultSet rs, int recordNumber_ )
        throws PortletException {
        if( authInfo == null )
            return;

        PreparedStatement ps = null;
        ResultSet rset = null;
        DatabaseAdapter db_ = null;
        List<MenuMemberModule> vv = new ArrayList<MenuMemberModule>();

        try {
            applicationName = RsetTools.getString( rs, "NAME_ARM" );
            applicationCode = RsetTools.getString( rs, "CODE_ARM" );

            order = RsetTools.getInt( rs, "ORDER_FIELD", 0 );

            applicationID = RsetTools.getLong( rs, "ID_ARM" );
            applRecordNumber = recordNumber_;

            db_ = DatabaseAdapter.getInstance();

            ps = db_.prepareStatement( sql_ );

            ps.setString( 1, authInfo.getUserLogin() );
            ps.setObject( 2, applicationID );

            rset = ps.executeQuery();
            int moduleRecordNumber = 0;
            while( rset.next() ) {
                MenuMemberModule mod = new MenuMemberModule( rset, applicationCode );
                mod.modRecordNumber = moduleRecordNumber++;
                mod.applRecordNumber = applRecordNumber;
                vv.add( mod );
            }
        }
        catch( Exception e1 ) {
            cat.error( "Error get member application", e1 );
            throw new PortletException( e1.toString() );
        }
        finally {
            DatabaseManager.close( db_, rset, ps );
            rset = null;
            ps = null;
            db_ = null;
        }

        if( vv.size() > 0 )
            subMenu = vv;
    }
}
