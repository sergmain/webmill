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



package org.riverock.sql.parser;



import java.sql.Types;

import java.util.HashMap;



import org.riverock.schema.sql.SelectType;

import org.riverock.schema.sql.SqlNameType;

import org.riverock.schema.sql.TableFilterType;

import org.riverock.schema.sql.TableType;



public class ServiceClass

{

    // lookup for types

    private static HashMap hTypes;



    static

    {

        hTypes = new HashMap( 67, 1 );



        hTypes.put( "INTEGER", new Integer( Types.INTEGER ) );

        hTypes.put( "INT", new Integer( Types.INTEGER ) );

        hTypes.put( "int", new Integer( Types.INTEGER ) );

        hTypes.put( "java.lang.Integer", new Integer( Types.INTEGER ) );

        hTypes.put( "IDENTITY", new Integer( Types.INTEGER ) );

        hTypes.put( "DOUBLE", new Integer( Types.DOUBLE ) );

        hTypes.put( "double", new Integer( Types.DOUBLE ) );

        hTypes.put( "java.lang.Double", new Integer( Types.DOUBLE ) );

        hTypes.put( "FLOAT", new Integer( Types.FLOAT ) );

        hTypes.put( "REAL", new Integer( Types.REAL ) );

        hTypes.put( "VARCHAR", new Integer( Types.VARCHAR ) );

        hTypes.put( "java.lang.String", new Integer( Types.VARCHAR ) );

        hTypes.put( "CHAR", new Integer( Types.CHAR ) );

        hTypes.put( "CHARACTER", new Integer( Types.CHAR ) );

        hTypes.put( "LONGVARCHAR", new Integer( Types.LONGVARCHAR ) );

        hTypes.put( "VARCHAR_IGNORECASE", new Integer( ServiceClass.VARCHAR_IGNORECASE ) );

        hTypes.put( "DATE", new Integer( Types.DATE ) );

        hTypes.put( "java.sql.Date", new Integer( Types.DATE ) );

        hTypes.put( "TIME", new Integer( Types.TIME ) );

        hTypes.put( "java.sql.Time", new Integer( Types.TIME ) );

        hTypes.put( "TIMESTAMP", new Integer( Types.TIMESTAMP ) );

        hTypes.put( "java.sql.Timestamp", new Integer( Types.TIMESTAMP ) );

        hTypes.put( "DATETIME", new Integer( Types.TIMESTAMP ) );

        hTypes.put( "DECIMAL", new Integer( Types.DECIMAL ) );

        hTypes.put( "java.math.BigDecimal", new Integer( Types.DECIMAL ) );

        hTypes.put( "NUMERIC", new Integer( Types.NUMERIC ) );

        hTypes.put( "BIT", new Integer( Types.BIT ) );

        hTypes.put( "boolean", new Integer( Types.BIT ) );

        hTypes.put( "java.lang.Boolean", new Integer( Types.BIT ) );

        hTypes.put( "TINYINT", new Integer( Types.TINYINT ) );

        hTypes.put( "byte", new Integer( Types.TINYINT ) );

        hTypes.put( "java.lang.Byte", new Integer( Types.TINYINT ) );

        hTypes.put( "SMALLINT", new Integer( Types.SMALLINT ) );

        hTypes.put( "short", new Integer( Types.SMALLINT ) );

        hTypes.put( "java.lang.Short", new Integer( Types.SMALLINT ) );

        hTypes.put( "BIGINT", new Integer( Types.BIGINT ) );

        hTypes.put( "long", new Integer( Types.BIGINT ) );

        hTypes.put( "java.lang.Long", new Integer( Types.BIGINT ) );

        hTypes.put( "BINARY", new Integer( Types.BINARY ) );

        hTypes.put( "[B", new Integer( Types.BINARY ) );

        hTypes.put( "VARBINARY", new Integer( Types.VARBINARY ) );

        hTypes.put( "LONGVARBINARY", new Integer( Types.LONGVARBINARY ) );

        hTypes.put( "OTHER", new Integer( Types.OTHER ) );

        hTypes.put( "OBJECT", new Integer( Types.OTHER ) );

        hTypes.put( "java.lang.Object", new Integer( Types.OTHER ) );

        hTypes.put( "NULL", new Integer( Types.NULL ) );

        hTypes.put( "void", new Integer( Types.NULL ) );

        hTypes.put( "java.lang.Void", new Integer( Types.NULL ) );

    }



    // non-standard type not in JDBC

    static final int VARCHAR_IGNORECASE = 100;

    // supported JDBC types - exclude NULL and VARCHAR_IGNORECASE

    static final int[] numericTypes = {

        Types.TINYINT, Types.SMALLINT, Types.INTEGER, Types.BIGINT,

        Types.NUMERIC, Types.DECIMAL, Types.FLOAT, Types.REAL, Types.DOUBLE

    };

    static final int[] otherTypes = {

        Types.BIT, Types.LONGVARBINARY, Types.VARBINARY, Types.BINARY,

        Types.LONGVARCHAR, Types.CHAR, Types.VARCHAR, Types.DATE, Types.TIME,

        Types.TIMESTAMP, Types.OTHER

    };

    static final int[][] typesArray = {

        numericTypes, otherTypes

    };



    static final int UNION = 1;

    static final int UNIONALL = 2;

    static final int INTERSECT = 3;

    static final int MINUS = 4;



    public static String toQuotedString( String s, char quotechar,

        boolean doublequote )

    {



        if ( s==null )

        {

            return "NULL";

        }



        int l = s.length();

        StringBuffer b = new StringBuffer( l+16 ).append( quotechar );



        for ( int i = 0; i<l; i++ )

        {

            char c = s.charAt( i );



            if ( doublequote && c==quotechar )

            {

                b.append( c );

            }



            b.append( c );

        }



        return b.append( quotechar ).toString();

    }



    /**

     *

     * @return java.sql.Types int value

     * @throws  Exception

     */

    static int getTypeNr( String type ) throws Exception

    {



        Integer i = (Integer)hTypes.get( type );



        if ( i==null )

            throw new Exception( "WRONG_DATA_TYPE "+ type );



        return i.intValue();

    }



    static SqlNameType getInstance( String name, boolean isquoted )

        throws Exception

    {

        SqlNameType sqlName = new SqlNameType();

        if ( name==null || name.length()==0 )

        {

            throw new Exception( "INVALID_IDENTIFIER" );

        }



        sqlName.setOriginName( name );

        sqlName.setIsNameQuoted( isquoted );



        if ( sqlName.getIsNameQuoted() )

            sqlName.setStatementName( toQuotedString( name, '"', true ) );

        else

            sqlName.setStatementName( name );



        return sqlName;

    }



    static SqlNameType getOriginInstance( String name )

        throws Exception

    {

        SqlNameType sqlName = new SqlNameType();

        if ( name==null || name.length()==0 )

        {

            throw new Exception( "INVALID_IDENTIFIER" );

        }



        if ((name.startsWith("\"") && !name.endsWith("\"")) ||

            (!name.startsWith("\"") && name.endsWith("\""))

        )

        {

            throw new Exception( "INVALID_IDENTIFIER "+name );

        }



        if (name.startsWith("\"") &&

            name.endsWith("\"")

        )

        {

            sqlName.setIsNameQuoted( true );

            sqlName.setOriginName( name.substring(1, name.length()-1) );

        }

        else

        {

            sqlName.setIsNameQuoted( false );

            sqlName.setOriginName( name.toUpperCase() );

        }



        return sqlName;

    }



    /**

     *  Method declaration

     *

     * @param  c

     * @return

     * @throws  Exception

     */

    static int getColumnNr( TableType table, String c )

        throws Exception

    {



        int i = searchColumn( table, c );



        if ( i==-1 )

        {

            throw new Exception( "COLUMN_NOT_FOUND " + c );

        }



        return i;

    }



    /**

     *  Method declaration

     *

     * @param  c

     * @return

     */

    static int searchColumn( TableType table, String c )

    {



        for ( int i = 0; i<table.getVisibleColumns(); i++ )

        {

            if ( c.equals( table.getColumns().getItem( i ).getColumnName().getOriginName() ) )

            {

                return i;

            }

        }



        return -1;

    }



    static TableType getInstance( SqlNameType name )

    {

        TableType table = new TableType();

        table.setTableName( name );

        return table;

    }



    /**

     * Method declaration

     *

     *

     * @throws Exception

     */

    static void checkResolved( SelectType select )

        throws Exception

    {



        if ( select.getExpCondition()!=null )

        {

            ExpressionService.checkResolved( select.getExpCondition() );

        }



        int len = select.getExpColumn().length;



        for ( int i = 0; i<len; i++ )

        {

            ExpressionService.checkResolved( select.getExpColumn()[i] );

        }

    }



    /**

     * Method declaration

     *

     *

     * @throws Exception

     */

    static void resolve( SelectType select )

        throws Exception

    {



        int len = select.getTableFilter().length;



        for ( int i = 0; i<len; i++ )

        {

            resolve( select, select.getTableFilter()[i], true );

        }

    }



    /**

     * Method declaration

     *

     *

     * @param f

     * @param ownfilter

     *

     * @throws Exception

     */

    static void resolve( SelectType select, TableFilterType f, boolean ownfilter )

        throws Exception

    {



        if ( select.getExpCondition()!=null )

        {



            // first set the table filter in the condition

            ExpressionService.resolve( select.getExpCondition(), f );



            if ( f!=null && ownfilter )

            {



                // the table filter tries to get as many conditions as

                // possible but only if it belongs to this query

                TableService.setCondition( f, select.getExpCondition() );

            }

        }



        int len = select.getExpColumn().length;



        for ( int i = 0; i<len; i++ )

        {

            ExpressionService.resolve( select.getExpColumn()[i], f );

        }

    }

}

