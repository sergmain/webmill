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
package org.riverock.generic.site;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.db.DatabaseManager;
import org.riverock.generic.exception.GenericException;
import org.riverock.common.tools.RsetTools;
import org.riverock.sql.cache.SqlStatement;
import org.riverock.sql.cache.SqlStatementRegisterException;

import org.apache.log4j.Logger;

/**
 * $Id$
 */
public final class SiteListSite{

    private final static Logger log = Logger.getLogger(SiteListSite.class);

    private Map hashListSite = new HashMap();

    public Long getLongIdSite( final String serverName) {
        if (serverName==null) return null;
        Long siteId = (Long)hashListSite.get(serverName.toLowerCase());
        if (siteId==null){
            log.warn("site with serverName '"+serverName+"' not found");
            log.warn( "Dump map with current serverNames" );
            for (Object s : hashListSite.keySet()) {
                log.warn("Value in map - " + s.toString() + ", value - " + hashListSite.get(s));
            }
        }
        return siteId;
    }

    public static Long getIdSite(String serverName) throws GenericException {
        return SiteListSite.getInstance().getLongIdSite(serverName);
    }

    public SiteListSite(){}

    // Кусок кода для принудительной переинициализации класса
    public void reinit()
    {
        lastReadData = 0;
    }

    public void terminate(java.lang.Long id_)
    {
        lastReadData = 0;
    }

    public void finalize() {
        if (hashListSite!=null) hashListSite.clear();
        hashListSite = null;
    }
    private static long lastReadData = 0;
    private final static long LENGTH_TIME_PERIOD = 10000;
    private static SiteListSite backupObject = null;
    private static Object syncObject = new Object();

    public static SiteListSite getInstance() throws GenericException {
        if (log.isDebugEnabled())
        {
            log.debug("#15.01.01 lastReadData: " + lastReadData + ", current " + System.currentTimeMillis());
            log.debug("#15.01.02 LENGTH_TIME_PERIOD " + LENGTH_TIME_PERIOD + ", status " +
                (((System.currentTimeMillis() - lastReadData) > LENGTH_TIME_PERIOD)
                || (backupObject == null))
            );
        }

        if (((System.currentTimeMillis() - lastReadData) > LENGTH_TIME_PERIOD)
            || (backupObject == null))
        {
            synchronized(syncObject)
            {
                if (((System.currentTimeMillis() - lastReadData) > LENGTH_TIME_PERIOD)
                        || (backupObject == null))
                {
                    if (log.isDebugEnabled())
                    {
                        log.debug("#15.01.03 reinit cached value ");
                        log.debug("#15.01.04 old value " + backupObject);
                    }

                    backupObject = null;

                    SiteListSite site = new SiteListSite();
                    site.initHashList();

                    backupObject = site;

                    if (log.isDebugEnabled()) log.debug("#15.01.05 new value " + backupObject);
                }
                else
                    if (log.isDebugEnabled()) log.debug("Get from cache");

                if (log.isDebugEnabled()) log.debug("#15.01.09 ret value " + backupObject);

                lastReadData = System.currentTimeMillis();
            }
        }
        return backupObject;
    }

    static String sql_ = null;
    static {
        sql_ =
            "select a.ID_SITE, a.NAME_VIRTUAL_HOST from WM_PORTAL_VIRTUAL_HOST a";

        try {
            SqlStatement.registerSql( sql_, new SiteListSite().getClass() );
        }
        catch( Throwable exception ) {
            final String es = "Exception in SqlStatement.registerRelateClass()";
            log.error( es, exception );
            throw new SqlStatementRegisterException( es, exception );
        }
    }

    private void initHashList() throws GenericException {
        PreparedStatement ps = null;
        ResultSet rs = null;

        DatabaseAdapter db_ = null;
        try
        {
            db_ = DatabaseAdapter.getInstance();
            ps = db_.prepareStatement( sql_ );

            rs = ps.executeQuery();
            while (rs.next())
            {
                hashListSite.put(
                    RsetTools.getString(rs, "NAME_VIRTUAL_HOST").toLowerCase(),
                    RsetTools.getLong(rs, "ID_SITE")
                );
            }
        }
        catch(Throwable e){
            final String es = "Error get list of virtual host ";
            log.error(es, e);
            throw new GenericException(es, e);
        }
        finally {
            DatabaseManager.close(db_, rs, ps);
            rs = null;
            ps = null;
        }
    }
}
