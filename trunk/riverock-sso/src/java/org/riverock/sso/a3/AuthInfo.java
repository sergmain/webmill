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

package org.riverock.sso.a3;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.db.DatabaseManager;
import org.riverock.common.tools.RsetTools;
import org.riverock.interfaces.sso.a3.AuthException;

import org.apache.log4j.Logger;

/**
 *
 *  $Id$
 */
public class AuthInfo {
    private static Logger log = Logger.getLogger(AuthInfo.class);

    private Long authUserID;
    private Long userID;
    private Long firmID;
    private Long serviceID;
    private Long roadID;

    private String userLogin = "";
    private String userPassword = "";

    private int isUseCurrentFirm = 0;
    private int isService = 0;
    private int isRoad = 0;
    private int isRoot = 0;

    protected void finalize() throws Throwable
    {
        userLogin = null;
        userPassword = null;

        super.finalize();
    }

    public AuthInfo(){}

    private void set(ResultSet rs) throws SQLException {
        authUserID = RsetTools.getLong(rs, "ID_AUTH_USER");
        userID = RsetTools.getLong(rs, "ID_USER");
        firmID = RsetTools.getLong(rs, "ID_FIRM");
        serviceID = RsetTools.getLong(rs, "ID_SERVICE");
        roadID = RsetTools.getLong(rs, "ID_ROAD");
        userLogin = RsetTools.getString(rs, "USER_LOGIN");
        userPassword = RsetTools.getString(rs, "USER_PASSWORD");

        int isUseCurrentFirmTemp = RsetTools.getInt(rs, "IS_USE_CURRENT_FIRM", 0);
        int isServiceTemp = RsetTools.getInt(rs, "IS_SERVICE", 0);
        int isRoadTemp = RsetTools.getInt(rs, "IS_ROAD", 0);
        int isRootTemp = RsetTools.getInt(rs, "IS_ROOT", 0);

        isUseCurrentFirm = ((isUseCurrentFirmTemp+isServiceTemp+isRoadTemp+isRootTemp)>0?1:0);
        isService = ((isServiceTemp+isRoadTemp+isRootTemp)>0?1:0);
        isRoad = ((isRoadTemp+isRootTemp)>0?1:0);
        isRoot = ((isRootTemp)>0?1:0);
    }

    public static AuthInfo getInstance(DatabaseAdapter db_, String login_, String pass_)
            throws AuthException
    {
        PreparedStatement ps = null;
        ResultSet rs = null;
        AuthInfo auth_ = null;
        try
        {
            ps = db_.prepareStatement(
                "select * FROM AUTH_USER where USER_LOGIN=? and USER_PASSWORD=?"
            );

            ps.setString(1, login_);
            ps.setString(2, pass_);

            rs = ps.executeQuery();
            if (rs.next())
            {
                auth_ = new AuthInfo();
                auth_.set(rs);
            }

        }
        catch (Throwable e)
        {
            String es = "Error in getInstance(DatabaseAdapter db_, String login_, String pass_)";
            log.error(es, e);
            throw new AuthException(es, e);
        }
        finally
        {
            DatabaseManager.close(rs, ps);
            rs = null;
            ps = null;
        }
        return auth_;
    }

    public static AuthInfo getInstance(DatabaseAdapter db_, Long id_auth_user)
            throws AuthException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        AuthInfo auth_ = null;
        try{
            ps = db_.prepareStatement( "select * FROM AUTH_USER where ID_AUTH_USER=?" );
            RsetTools.setLong(ps, 1, id_auth_user);
            rs = ps.executeQuery();
            if (rs.next()){
                auth_ = new AuthInfo();
                auth_.set(rs);
            }
        }
        catch (Throwable e){
            String es = "AuthInfo.getInstance() exception";
            log.error(es, e);
            throw new AuthException(es, e);
        }
        finally{
            DatabaseManager.close(rs, ps);
            rs = null;
            ps = null;
        }
        return auth_;
    }

    public Long getAuthUserID() {
        return authUserID;
    }

    public Long getUserID() {
        return userID;
    }

    public Long getFirmID() {
        return firmID;
    }

    public Long getServiceID() {
        return serviceID;
    }

    public Long getRoadID() {
        return roadID;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public int getUseCurrentFirm() {
        return isUseCurrentFirm;
    }

    public int getService() {
        return isService;
    }

    public int getRoad() {
        return isRoad;
    }

    public int getRoot() {
        return isRoot;
    }
}

