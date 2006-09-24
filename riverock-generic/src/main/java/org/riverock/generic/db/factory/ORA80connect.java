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
package org.riverock.generic.db.factory;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Locale;

import org.apache.log4j.Logger;

import org.riverock.common.config.ConfigException;
import org.riverock.common.tools.DateTools;
import org.riverock.generic.tools.CurrentTimeZone;

/**
 * Класс ORAconnect прденазначен для коннекта к оракловской базе данных.
 *
 * $Id$
 */
public class ORA80connect extends ORAconnect {
    private static Logger log = Logger.getLogger("org.riverock.generic.db.ORA80connect");

    public int getSubVersion() {
        return 0;
    }

    /**
     * in some DB (Oracle8.0) setTimestamp not work and we need work around
     *
     * @return String
     */
    public String getNameDateBind() {
        return "to_date(?, 'dd.mm.yyyy hh24:mi:ss')";
    }

    /**
     * bind Timestamp value
     *
     * @param ps
     * @param stamp @see java.sql.Timestamp
     * @throws SQLException
     */
    public void bindDate(PreparedStatement ps, int idx, Timestamp stamp) throws SQLException {
        try {
            String stringTimestamp =
                DateTools.getStringDate(stamp, "yyyy-MM-dd HH:mm:ss.SSS", Locale.ENGLISH, CurrentTimeZone.getTZ());
            ps.setString(idx, stringTimestamp);
        }
        catch (ConfigException exc) {
            String es = "Exception in CurrentTimeZone.getTZ()";
            log.error(es, exc);
            throw new SQLException(es);
        }
        ;

    }

    public String getDriverClass() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public ORA80connect() {
        super();
    }
}
