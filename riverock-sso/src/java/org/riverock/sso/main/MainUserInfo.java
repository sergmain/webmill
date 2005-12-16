/*
 * org.riverock.sso -- Single Sign On implementation
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
package org.riverock.sso.main;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.apache.log4j.Logger;

import org.riverock.common.tools.RsetTools;
import org.riverock.common.tools.StringTools;
import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.db.DatabaseManager;
import org.riverock.sso.bean.UserInfoImpl;

/**
 * $Id$
 */
public class MainUserInfo extends UserInfoImpl {
    private static Logger log = Logger.getLogger( MainUserInfo.class );

    private static final long serialVersionUID = 3043770876L;

    protected void finalize() throws Throwable {
        setFirstName(null);
        setMiddleName(null);
        setLastName(null);
        setDateStartWork(null);
        setDateFire(null);
        setAddress(null);
        setTelephone(null);
        setDateBindProff(null);
        setHomeTelephone(null);
        setEmail(null);

        super.finalize();
    }

    public MainUserInfo() {
    }


    private MainUserInfo(String userLogin) throws Exception {
        String sql_ = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        sql_ =
            "select a.* " +
            "from   MAIN_USER_INFO a, WM_AUTH_USER b " +
            "where  a.id_user = b.id_user and b.user_login = ? ";

        DatabaseAdapter db_ = null;
        try {
            db_ = DatabaseAdapter.getInstance();
            ps = db_.prepareStatement(sql_);
            ps.setString(1, userLogin);

            rs = ps.executeQuery();

            if (rs.next()) {
                set(rs);
            }
        }
        catch (Exception e) {
            log.error("Error get user info", e);
            throw e;
        }
        finally {
            DatabaseManager.close(db_, rs, ps);
            rs = null;
            ps = null;
            db_ = null;
        }
    }


    private void set(ResultSet rs) throws Exception {
        try {
            setUserId(RsetTools.getLong(rs, "ID_USER"));
            setCompanyId(RsetTools.getLong(rs, "ID_FIRM"));
            setFirstName(RsetTools.getString(rs, "FIRST_NAME"));
            setMiddleName(RsetTools.getString(rs, "MIDDLE_NAME"));
            setLastName(RsetTools.getString(rs, "LAST_NAME"));

            setDateStartWork(RsetTools.getTimestamp(rs, "DATE_START_WORK"));

            setDateFire(RsetTools.getTimestamp(rs, "DATE_FIRE"));

            setAddress(RsetTools.getString(rs, "ADDRESS"));
            setTelephone(RsetTools.getString(rs, "TELEPHONE"));

            setDateBindProff(RsetTools.getTimestamp(rs, "DATE_BIND_PROFF"));

            setHomeTelephone(RsetTools.getString(rs, "HOME_TELEPHONE"));
            setEmail(RsetTools.getString(rs, "EMAIL"));
            setDiscount(RsetTools.getDouble(rs, "DISCOUNT"));

        }
        catch (Exception e) {
            log.error("Error set user info from ResultSet", e);
            throw e;
        }
    }

    public static MainUserInfo getInstance(String userLogin) throws Exception {
        if ( StringTools.isEmpty(userLogin) ) {
            return null;
        }

        return new MainUserInfo(userLogin);
    }

}
