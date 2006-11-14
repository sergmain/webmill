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
package org.riverock.webmill.portal.dao;

import org.apache.log4j.Logger;
import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.db.DatabaseManager;

import java.io.StringWriter;
import java.sql.Types;
import java.util.List;
import java.util.Map;

/**
 * User: SergeMaslyukov
 * Date: 16.08.2006
 * Time: 17:27:39
 * <p/>
 * $Id$
 */
@SuppressWarnings({"UnusedAssignment"})
public class InternalPreferencesDaoImpl implements InternalPreferencesDao {
    private final static Logger log = Logger.getLogger(InternalPreferencesDaoImpl.class);
    
    private ClassLoader classLoader=null;

    public InternalPreferencesDaoImpl() {
        classLoader=Thread.currentThread().getContextClassLoader();
    }

    public void store(Map<String, List<String>> preferences, Long contextId) {
        ClassLoader oldLoader = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader( classLoader );
            StringWriter out = new StringWriter();

            for (Map.Entry<String, List<String>> entry : preferences.entrySet()) {
                for (String s : entry.getValue()) {
                    out.write(entry.getKey()+'='+s+'\n');
                }
            }
            String s = out.toString();

            DatabaseAdapter adapter = null;

            if (log.isDebugEnabled()) {
                log.debug(" result metadata: " + s);
                log.debug(" contextId: " + contextId);
            }

            try {
                adapter = DatabaseAdapter.getInstance();

                DatabaseManager.runSQL(
                adapter,
                    "update WM_PORTAL_CATALOG "+
                    "set METADATA=? "+
                    "where ID_SITE_CTX_CATALOG=?",
                    new Object[]{s, contextId},
                    new int[]{Types.VARCHAR,  Types.DECIMAL}
                );
                adapter.commit();
            } catch (Throwable e) {
                try {
                    if (adapter!=null)
                        adapter.rollback();
                }
                catch(Throwable th) {
                    // catch rollback error
                }
                String es = "Error store portlet preferences";
                log.error(es, e);
                throw new IllegalStateException( es, e);
            } finally {
                DatabaseManager.close(adapter);
                adapter = null;
            }
        }
        finally {
            Thread.currentThread().setContextClassLoader( oldLoader );
        }
    }
}
