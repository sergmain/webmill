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
 * $Id$
 */
package org.riverock.portlet.tools;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.db.DatabaseManager;
import org.riverock.generic.exception.DatabaseException;
import org.riverock.common.tools.RsetTools;

public final class Client
{

    public static String make_list_prn(
            ResultSet rs,
            String field,
            DatabaseAdapter ora_,
            String t,
            String i,
            String f
            )
        throws SQLException, DatabaseException
    {
        return make_list_prn(rs, field, ora_, t, i, f, "", null, "0");
    }

    public static String make_list_prn(
            ResultSet rs,
            String field,
            DatabaseAdapter ora_,
            String t,
            String i,
            String f,
            String w,
            String o
            )
        throws SQLException, DatabaseException
    {
        return make_list_prn(rs, field, ora_, t, i, f, w, o, "0");
    }


    public static String make_list_prn(
            ResultSet rs,
            String field,
            DatabaseAdapter ora_,
            String t,
            String i,
            String f,
            String w,
            String o,
            String d
            )
        throws SQLException, DatabaseException
    {

        long v_id = 0;
        if (rs != null)
        {
            v_id = RsetTools.getLong(rs, field, new Long(0) ).intValue();
        }

        return make_list_prn(v_id, ora_, t, i, f, w, o, d);
    }

    public static String make_list_prn(
            long v_id,
            DatabaseAdapter ora_,
            String t,
            String i,
            String f,
            String w,
            String o,
            String d
            )
        throws SQLException, DatabaseException
    {

        String v_s = "";
        v_s = "select ";
        if ((d != null) && d.equals("1"))
            v_s += " distinct ";

        v_s += (i + ", " + f + " from " + t + ((w == null)?" ":w));

        if (o != null)
            v_s += (" order by " + o);

        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = ora_.prepareStatement(v_s);

            rs = ps.executeQuery();

            long v_num;
            String v_str;
            String v_select;
            String r = "";

            while (rs.next()) {

                v_num = rs.getLong(1);
                v_str = rs.getString(2);

                if (v_str == null) v_str = "";

                if (v_num == v_id)
                    v_select = " SELECTED";
                else
                    v_select = "";

                r += ("<option" + v_select + " value=\"" + v_num + "\">" +
                        v_str.replace('\"', '\'') +
                        "</option>\n");
            }
            ;
            return r;
        }
        finally {
            DatabaseManager.close( rs, ps );
            rs = null;
            ps = null;
        }
    }

    public static String make_list_prn_str(
            String v_id,
            DatabaseAdapter ora_,
            String t,
            String i,
            String f,
            String w,
            String o,
            String d
            )
        throws SQLException, DatabaseException
    {

        String v_s = "select ";

        if ((d != null) && d.equals("1"))
            v_s += " distinct ";

        v_s += (i + ", " + f + " from " + t + ((w == null)?" ":w));

        if (o != null)
            v_s += (" order by " + o);

        String r = "";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = ora_.prepareStatement(v_s);

            rs = ps.executeQuery();

            String v_val;
            String v_str;
            String v_select;
            while (rs.next())
            {

                v_val = rs.getString(1);
                v_str = rs.getString(2);

                if (v_val.equals(v_id))
                    v_select = " SELECTED";
                else
                    v_select = "";

                r += ("<option" + v_select + " value=\"" +
                        v_val + "\">" + v_str.replace('\"', '\'') +
                        "</option>\n");

            }
            ;
        }
        finally {
            DatabaseManager.close( rs, ps );
            rs = null;
            ps = null;
        }
        return r;
    }
}
