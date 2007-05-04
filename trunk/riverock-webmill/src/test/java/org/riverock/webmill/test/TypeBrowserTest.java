/*
 * org.riverock.webmill - Webmill portal with support jsr-168, xml/xslt and others things.
 * For more information, please visit project site http://webmill.riverock.org
 *
 * Copyright (C) 2000-2006, Riverock Software, All Rights Reserved.
 *
 * Riverock - The Open-source Java Development Community
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
 */
package org.riverock.webmill.test;

import java.util.Comparator;
import java.util.Set;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.Collections;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.riverock.common.startup.StartupApplication;
import org.riverock.common.html.TypeBrowser;

/**
 * @author Sergei Maslyukov
 *         Date: 14.12.2006
 *         Time: 15:43:11
 *         <p/>
 *         $Id$
 */
public class TypeBrowserTest {

    static class StringComparator implements Comparator<String> {
        // Compare two Strings. Callback for sort.
        // effectively returns a-b;
        // e.g. +1 (or any +ve number) if a > b
        // 0 if a == b
        // -1 (or any -ve number) if a < b
        public final int compare ( String a, String b ) {
            return a.toLowerCase().compareTo(b.toLowerCase());
        } // end compare
    } // end class StringComparator

    public static void main(String[] args) throws Exception {
        StartupApplication.init();

//        DatabaseAdapter db=null;
//        db=DatabaseAdapter.getInstance();
//
        Long count=null;
//        count = DatabaseManager.getLongValue(db, "select count(*) from test.a_useragent", null, null);
//
        PreparedStatement ps=null;
//        ps = db.prepareStatement("select * from test.a_useragent");
        ResultSet rs = ps.executeQuery();

        int unknown=0;
        TypeBrowser typeBrowser;
        Set<String> set = new HashSet<String>();
        int i=0;
        while(rs.next()) {
            String ua = rs.getString("USER_AGENT");
            System.out.println("ua #"+(i++)+"= " + ua);
            typeBrowser = new TypeBrowser(ua);
            if (typeBrowser.type==TypeBrowser.UNKNOWN) {
                set.add(ua);
            }
        }
        System.out.println("count = " + count);
        System.out.println("unknown = " + unknown);
        System.out.println("real count unknown = " + set.size());

        ArrayList<String> list = new ArrayList<String>(set);

        Collections.sort(list, new StringComparator());
        for (String s : list) {
            System.out.println("s = " + s);
        }
//        DatabaseManager.close(db, rs, ps);

    }
}