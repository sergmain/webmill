/*
 * org.riverock.webmill - Portal framework implementation
 *
 * Copyright (C) 2006, Riverock Software, All Rights Reserved.
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
