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

package org.riverock.webmill.port;



import java.sql.PreparedStatement;

import java.sql.ResultSet;

import java.util.HashMap;



import org.riverock.generic.db.DatabaseAdapter;

import org.riverock.generic.db.DatabaseManager;

import org.riverock.common.tools.RsetTools;



import org.apache.log4j.Logger;



public class PortalXsltList

{

    private static Logger log = Logger.getLogger( "org.riverock.webmill.port.PortalXsltList" );



    public HashMap hash = new HashMap (4);



    static

    {

        try

        {

            org.riverock.sql.cache.SqlStatement.registerRelateClass( new PortalXsltList().getClass(), new PortalXslt().getClass());

        }

        catch (Exception exception)

        {

            log.error("Exception in ", exception);

        }

    }



    public void reinit(){}



    protected void finalize() throws Throwable

    {

        if (hash != null)

        {

            hash.clear();

            hash = null;

        }



        super.finalize();

    }



    public PortalXsltList()

    {

    }



    public PortalXslt getXslt(String lang)

    {

        if (log.isDebugEnabled())

            log.debug("XsltList.size - " + hash.size());



        return (PortalXslt) hash.get( lang );

    }



    public static PortalXsltList getInstance(DatabaseAdapter db_, Long idSite)

            throws Exception

    {



        if (log.isDebugEnabled())

            log.debug("XsltList. serverName  ID - " + idSite);



        PortalXsltList list = new PortalXsltList();



        String sql_ =

                "select a.CUSTOM_LANGUAGE, d.ID_SITE_XSLT " +

                "from SITE_SUPPORT_LANGUAGE  a, SITE_XSLT d " +

                "where a.ID_SITE=? and " +

                "a.ID_SITE_SUPPORT_LANGUAGE=d.ID_SITE_SUPPORT_LANGUAGE and " +

                "d.IS_CURRENT=1";



        PreparedStatement ps = null;

        ResultSet rset = null;



        HashMap tempHash = new HashMap();

        try

        {

            ps = db_.prepareStatement(sql_);



            ps.setObject(1, idSite);

            rset = ps.executeQuery();

            while (rset.next())

            {

                String lang = RsetTools.getString(rset, "CUSTOM_LANGUAGE");

                Long id = RsetTools.getLong(rset, "ID_SITE_XSLT");



                if (log.isDebugEnabled())

                {

                    log.debug("XsltList. lang - " + lang);

                    log.debug("XsltList. id - " + id);

                }



                PortalXslt item = PortalXslt.getInstance( db_, id );

                item.xsltLang = lang;



                tempHash.put(lang, item );

            }

        }

        catch (Exception e)

        {

            log.error("Error create PortalXsltList", e);

            throw new Exception(e.toString());

        }

        finally

        {

            DatabaseManager.close(rset, ps);

            rset = null;

            ps = null;

        }



        if (log.isDebugEnabled())

            log.debug( "XsltList. count of templates - " + tempHash.size() );



        if(tempHash.size() == 0)

            return null;



        list.hash = tempHash;



        return list;

    }

}