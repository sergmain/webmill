/*

 * org.riverock.webmill -- Portal framework implementation

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

package org.riverock.webmill.site;



import java.sql.PreparedStatement;

import java.sql.ResultSet;

import java.util.HashMap;



import org.riverock.generic.db.DatabaseAdapter;

import org.riverock.generic.db.DatabaseManager;

import org.riverock.common.tools.RsetTools;



import org.apache.log4j.Logger;



public class SiteListSite

{

    private static Logger log = Logger.getLogger(SiteListSite.class);



    private HashMap hashListSite = new HashMap();



    public Long getLongIdSite(String serverName)

    {

        return (Long) hashListSite.get(serverName);



//        if (l!=null)

//            return l.longValue();

//

//        return -1;

    }



    public static Long getIdSite(String serverName)

        throws Exception

    {

        return SiteListSite.getInstance().getLongIdSite(serverName);

    }



    public SiteListSite(){}



// ����� ���� ��� �������������� ����������������� ������

    public void reinit()

    {

        lastReadData = 0;

    }



    public void terminate(java.lang.Long id_)

    {

        lastReadData = 0;

    }



    private static long lastReadData = 0;

    private final static long LENGTH_TIME_PERIOD = 10000;

    private static SiteListSite backupObject = null;

    private static Object syncObject = new Object();



    public static SiteListSite getInstance()

        throws Exception

    {

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

                    backupObject = new SiteListSite();

                    backupObject.initHashList();



                    if (log.isDebugEnabled())

                        log.debug("#15.01.05 new value " + backupObject);

                }

                else

                {

                    if (log.isDebugEnabled())

                        log.debug("Get from cache");

                }



                if (log.isDebugEnabled())

                    log.debug("#15.01.09 ret value " + backupObject);



            }

        }

        lastReadData = System.currentTimeMillis();

        return backupObject;

    }



    static String sql_ = null;

    static

    {

        sql_ =

            "select a.ID_SITE, a.NAME_VIRTUAL_HOST from site_virtual_host a";



        SiteListSite tempObj = new SiteListSite();

        Class dependClass = tempObj.getClass();

        try

        {

            org.riverock.sql.cache.SqlStatement.registerSql( sql_, dependClass );

        }

        catch(Exception e)

        {

            log.error("Exception in registerSql, sql\n"+sql_, e);

        }

        catch(Error e)

        {

            log.error("Error in registerSql, sql\n"+sql_, e);

        }

    }



    private void initHashList()

        throws Exception

    {

        PreparedStatement ps = null;

        ResultSet rs = null;



        DatabaseAdapter db_ = null;

        try

        {

            db_ = DatabaseAdapter.getInstance(false);

            ps = db_.prepareStatement( sql_ );



            rs = ps.executeQuery();

            while (rs.next())

            {

                hashListSite.put(

                    RsetTools.getString(rs, "NAME_VIRTUAL_HOST"),

                    RsetTools.getLong(rs, "ID_SITE")

                );

            }

        }

        catch(Exception e)

        {

            log.error("Exception DBG, db_ - "+db_, e);

            if (db_!=null)

                log.error("DBG, db_.conn - "+db_.getConnection());



            throw e;

        }

        catch(Error e)

        {

            log.error("Error DBG, db_ - "+db_, e);

            if (db_!=null)

                log.error("DBG, db_.conn - "+db_.getConnection());



            throw e;

        }

        finally

        {

            DatabaseManager.close(rs, ps);

            rs = null;

            ps = null;



            DatabaseAdapter.close( db_);

            db_ = null;

        }

    }

}

