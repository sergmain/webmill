/*
 * org.riverock.generic -- Database connectivity classes
 * 
 * Copyright (C) 2004, Riverock Software, All Rights Reserved.
 * 
 * Riverock -- The Open-source Java Development Community
 * http://www.riverock.org
 * 
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 *
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
