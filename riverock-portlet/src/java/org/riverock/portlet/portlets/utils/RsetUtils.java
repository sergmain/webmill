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

 * User: serg_main

 * Date: 27.11.2003

 * Time: 16:13:43

 * @author Serge Maslyukov

 * $Id$

 */



package org.riverock.portlet.portlets.utils;



import java.sql.PreparedStatement;

import java.sql.ResultSet;

import java.sql.SQLException;



import org.riverock.generic.db.DatabaseAdapter;

import org.riverock.generic.db.DatabaseManager;

import org.riverock.common.tools.RsetTools;



import org.apache.log4j.Logger;



public class RsetUtils

{

    private static Logger log = Logger.getLogger( RsetUtils.class );



    public static String getBigTextField( DatabaseAdapter db_, Long id_,

        String field_,

        String table_,

        String idx_field_,

        String order_field_

        )

        throws SQLException

    {

        if ( id_==null )

            return "";



        PreparedStatement ps = null;

        ResultSet rset = null;

        String sql_ = "select "+field_+" from "+table_+" where "+idx_field_+"= ? order by "+order_field_+" ASC";



        if ( log.isDebugEnabled() )

        {

            log.debug( "ID: "+id_ );

            log.debug( "SQL: "+sql_ );

        }



        String text = "";

        try

        {

            ps = db_.prepareStatement( sql_ );



            if ( log.isDebugEnabled() )

                log.debug( "11.03.01" );



            RsetTools.setLong( ps, 1, id_ );

            rset = ps.executeQuery();



            if ( log.isDebugEnabled() )

                log.debug( "11.03.01" );



            while ( rset.next() )

            {

                if ( log.isDebugEnabled() )

                    log.debug( "11.03.01 "+text );



                text += RsetTools.getString( rset, field_ );



                if ( log.isDebugEnabled() )

                    log.debug( "11.03.01" );

            }

        } finally

        {

            DatabaseManager.close(rset, ps);

            rset = null;

            ps = null;

        }

        return text;

    }





}

