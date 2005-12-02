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
package org.riverock.portlet.register.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.apache.log4j.Logger;

import org.riverock.generic.db.DatabaseManager;
import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.module.exception.ActionException;
import org.riverock.portlet.register.bean.RegisterPasswordInfoBean;

/**
 * @author SergeMaslyukov
 *         Date: 30.11.2005
 *         Time: 22:23:04
 *         $Id$
 */
public class RegisterPasswordInfoDAO {
    private final static Logger log = Logger.getLogger(RegisterPasswordInfoDAO.class);

    private final static String sql = "select EMAIL, USERNAME, PASSWORD from  ";

    public RegisterPasswordInfoBean execute(Long serverNameId, String email) throws ActionException {
        DatabaseAdapter adapter = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        RegisterPasswordInfoBean bean = new RegisterPasswordInfoBean();
        try {
            adapter = DatabaseAdapter.getInstance();
            ps = adapter.prepareStatement( sql );
            ps.setLong(1, serverNameId );
            ps.setString(2, email );
            rs = ps.executeQuery();;

            bean.setUsername( rs.getString("USERNAME") );
            bean.setEmail( rs.getString("EMAIL") );
            bean.setPassword( rs.getString("PASSWORD") );

            return bean;
        }
        catch (Exception e) {
            String es = "Error execute RegisterPasswordInfoDAO";
            log.error(es, e);
            throw new ActionException(es, e);
        }
        finally {
            DatabaseManager.close(adapter, rs, ps);
            adapter = null;
            rs = null;
            ps = null;
        }
    }
}
