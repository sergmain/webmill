/*

 * org.riverock.sql -- Classes for tracking database changes

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



/**

 * User: Admin

 * Date: May 10, 2003

 * Time: 6:16:50 PM

 *

 * $Id$

 */

package org.riverock.sql.cache;



import java.util.ArrayList;

import java.util.HashMap;

import java.util.Hashtable;



import org.riverock.sql.parser.Parser;



import org.apache.log4j.Logger;



public class SqlStatement

{

    private static Logger log = Logger.getLogger( "org.riverock.sql.cache.SqlStatement" );



    private static HashMap sqlParserHash = new HashMap( 100 );

    public static Hashtable classHash = new Hashtable();

    public static Hashtable classRelateHash = new Hashtable();



    public synchronized static Parser parseSql( String sql )

        throws Exception

    {

        if ( log.isDebugEnabled() )

            log.debug( "sql\n"+sql );



        if ( sql==null || sql.length()==0 )

            return null;



        try

        {

            Object obj = sqlParserHash.get( sql );



            if ( obj==null )

            {

                String clone = new String( sql.toCharArray() );

                Parser parserTemp = (Parser)sqlParserHash.get( clone );



                if ( log.isDebugEnabled() )

                    log.debug( "sql\n"+clone+"\nresult search parsed result in hash"+parserTemp );



                if ( parserTemp==null )

                {

                    if ( log.isDebugEnabled() )

                        log.debug( "put new result of parsing" );



                    parserTemp = Parser.getInstance( clone );

                    sqlParserHash.put( clone, parserTemp );

                }

                return parserTemp;

            }

            else if (obj instanceof Parser)

            {

                Parser tempParserObj = (Parser)obj;

                if (sql.equals(tempParserObj.sql))

                    return tempParserObj;



                String clone = new String( sql.toCharArray() );

                Parser newParser = Parser.getInstance(clone);



                ArrayList v = new ArrayList(4);

                v.add(obj);

                v.add(newParser);



                sqlParserHash.put( clone, v );

                return tempParserObj;

            }

            else if (obj instanceof ArrayList)

            {

                ArrayList arrayList = (ArrayList)obj;

                for ( int i=0; i<arrayList.size(); i++)

                {

                    Parser tempParser = (Parser)arrayList.get(i);

                    if (tempParser==null)

                        continue;



                    if (tempParser.sql.equals(sql))

                        return tempParser;

                }



                String clone = new String( sql.toCharArray() );

                Parser newParser = Parser.getInstance(clone);

                arrayList.add(newParser);



                return newParser;

            }

            else

            {

                String errorString = "Object in hash is "+obj.getClass().getName();

                log.error(errorString);

                throw new Exception(errorString);

            }

        }

        catch (Exception e)

        {

            log.error( "Error parse SQL "+sql );

            throw e;

        }

    }



    public synchronized static void registerRelateClass( Class classTarget, Class classMain )

    {

        if (classTarget==null || classMain==null)

              throw new IllegalArgumentException( "Both classes in relation must be not null" );



        if ( classTarget==classMain)

              throw new IllegalArgumentException( "Can not set relation to self" );



        Object obj = classRelateHash.get( classMain.getName() );

        if ( obj==null )

        {

            classRelateHash.put( classMain.getName(), classTarget.getName() );

        }

        else if ( obj instanceof ArrayList )

        {

            ( (ArrayList)obj ).add( classTarget.getName() );

        }

        else

        {

            ArrayList v = new ArrayList();

            v.add( obj );

            v.add( classTarget.getName() );

            classRelateHash.put( classMain.getName(), v );

        }

    }



    public synchronized static void registerSql( String sql_, Class class_ )

        throws Exception

    {

        if ( sql_==null || class_==null )

            return;



        try

        {

            Parser parser = parseSql( sql_ );



            Object obj = classHash.get( class_.getName() );

            if ( obj==null )

            {

                classHash.put( class_.getName(), parser );

            }

            else if ( obj instanceof ArrayList )

            {

                ( (ArrayList)obj ).add( parser );

            }

            else

            {

                ArrayList v = new ArrayList();

                v.add( obj );

                v.add( parser );

                classHash.put( class_.getName(), v );

            }

        }

        catch (Exception e)

        {

            log.error( "Error parser sql:\n"+sql_, e );

            throw e;

        }

    }

}

