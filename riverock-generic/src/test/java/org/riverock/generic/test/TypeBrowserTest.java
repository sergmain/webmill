/*
 * org.riverock.generic - Database connectivity classes, part of Webmill portal
 * For more information about Webmill portal, please visit project site
 * http://webmill.askmore.info
 *
 * Copyright (C) 2000-2006, Riverock Software, All Rights Reserved.
 *
 * Riverock - The Open-source Java Development Community
 * http://www.riverock.org
 *
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.riverock.generic.test;

import org.riverock.generic.startup.StartupApplication;
import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.db.DatabaseManager;
import org.riverock.generic.exception.DatabaseException;
import org.riverock.common.html.TypeBrowser;

import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;

/**
 * User: SergeMaslyukov
 * Date: 08.09.2006
 * Time: 23:29:47
 * <p/>
 * $Id$
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

        DatabaseAdapter db=null;
        db=DatabaseAdapter.getInstance();

        Long count = DatabaseManager.getLongValue(db, "select count(*) from test.a_useragent", null, null);

        PreparedStatement ps = db.prepareStatement("select * from test.a_useragent");
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
        DatabaseManager.close(db, rs, ps);

    }
}
