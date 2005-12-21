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
import java.util.ArrayList;
import java.util.Vector;

import org.riverock.schema.sql.ColumnType;
import org.riverock.schema.sql.ExpressionType;
import org.riverock.schema.sql.SelectType;
import org.riverock.schema.sql.TableFilterType;
import org.riverock.schema.sql.TableType;
import org.riverock.schema.sql.ValueItemType;
import org.riverock.schema.sql.types.AggregateSpecType;

/**
 * User: Admin
 * Date: Apr 27, 2003
 * Time: 3:05:14 PM
 *
 * $Id$
 */
public class ExpressionService
{
    static final Integer INTEGER_0 = 0;
    static final Integer INTEGER_1 = 1;
    // leaf types
    static final int VALUE = 1;
    static final int COLUMN = 2;
    static final int QUERY = 3;
    static final int TRUE = 4;
    static final int VALUELIST = 5;
    static final int ASTERIX = 6;
    static final int FUNCTION = 7;
    // operations
    static final int NEGATE = 9;
    static final int ADD = 10;
    static final int SUBTRACT = 11;
    static final int MULTIPLY = 12;
    static final int DIVIDE = 14;
    static final int CONCAT = 15;
    // logical operations
    static final int NOT = 20;
    static final int EQUAL = 21;
    static final int BIGGER_EQUAL = 22;
    static final int BIGGER = 23;
    static final int SMALLER = 24;
    static final int SMALLER_EQUAL = 25;
    static final int NOT_EQUAL = 26;
    static final int LIKE = 27;
    static final int AND = 28;
    static final int OR = 29;
    static final int IN = 30;
    static final int EXISTS = 31;
    // aggregate functions
    static final int COUNT = 40;
    static final int SUM = 41;
    static final int MIN = 42;
    static final int MAX = 43;
    static final int AVG = 44;
    // system functions
    static final int IFNULL = 60;
    static final int CONVERT = 61;
    static final int CASEWHEN = 62;
    // temporary used during paring
    static final int PLUS = 100;
    static final int OPEN = 101;
    static final int CLOSE = 102;
    static final int SELECT = 103;
    static final int COMMA = 104;
    static final int STRINGCONCAT = 105;
    static final int BETWEEN = 106;
    static final int CAST = 107;
    static final int END = 108;
    static final int IS = 109;

    static final int AGGREGATE_SELF = -1;
    static final int AGGREGATE_NONE = 0;
    static final int AGGREGATE_LEFT = 1;
    static final int AGGREGATE_RIGHT = 2;
    static final int AGGREGATE_BOTH = 3;

    public static void checkAggregate( ExpressionType expression )
    {

        if ( isAggregate( expression.getType() ) )
        {
            expression.setAggregateSpec( AggregateSpecType.AGGREGATE_SELF );
        }
        else
        {
            expression.setAggregateSpec( AggregateSpecType.AGGREGATE_NONE );

            if ( ( expression.getExpArg1()!=null ) && isAggregate( expression.getExpArg1() ) )
            {
                expression.setAggregateSpec( AggregateSpecType.AGGREGATE_LEFT );
            }

            if ( ( expression.getExpArg2()!=null ) && isAggregate( expression.getExpArg2() ) )
            {
                expression.setAggregateSpec( AggregateSpecType.AGGREGATE_RIGHT );
            }

            if ( ( expression.getExpArg2()!=null ) && isAggregate( expression.getExpArg2() ) &&
                 ( expression.getExpArg1()!=null ) && isAggregate( expression.getExpArg1() ) )
            {
                expression.setAggregateSpec( AggregateSpecType.AGGREGATE_BOTH );
            }



        }
    }

    /**
     *  Method declaration
     *
     *
     *  @return
     */
    static boolean isAggregate( ExpressionType expression )
    {
        return expression.getAggregateSpec().getType()!=AggregateSpecType.AGGREGATE_NONE.getType();
    }

    static boolean isAggregate( int type )
    {

        switch (type)
        {

            case COUNT:
            case MAX:
            case MIN:
            case SUM:
            case AVG:
                return true;
        }

        return false;
    }

    /**
     * Method declaration
     *
     *
     * @return
     */
    static String getAlias( ExpressionType expression )
    {

        if ( expression.getAlias()!=null )
        {
            return expression.getAlias();
        }

        if ( expression.getType()==VALUE )
        {
            return "";
        }

        if ( expression.getType()==COLUMN )
        {
            return expression.getColumn();
        }

// fredt@users 20020130 - patch 497872 by Nitin Chauhan - modified
// return column name for aggregates without alias
        if ( expression.getExpArg1()!=null )
        {
            String name = getColumnName( expression.getExpArg1() );

            if ( name.length()>0 )
            {
                return name;
            }
        }

        return expression.getExpArg2()==null ? ""
            : getAlias(expression.getExpArg2());
    }

    /**
     * Method declaration
     *
     *
     * @throws Exception
     */
    static void checkResolved( ExpressionType expression )
        throws Exception
    {

        if ( ( expression.getType()==COLUMN ) && ( expression.getFilter()==null ))
            throw new Exception( "COLUMN_NOT_FOUND "+ expression.getColumn() );

        if ( expression.getExpArg1()!=null )
        {
            checkResolved(expression.getExpArg1());
        }

        if ( expression.getExpArg2()!=null )
        {
            checkResolved(expression.getExpArg2());
        }

        if ( expression.getSelect()!=null )
        {
            ServiceClass.checkResolved( expression.getSelect() );
        }

    }

    /**
     * Method declaration
     *
     *
     * @param f
     *
     * @throws Exception
     */
    static void resolve( ExpressionType expression, TableFilterType f )
        throws Exception
    {
        if (expression==null)
            return;

        if ( ( f!=null ) && ( expression.getType()==COLUMN ) )
        {
            String tableName = f.getAlias();

            if ( ( expression.getTable()==null ) || tableName.equals( expression.getTable() ) )
            {
                TableType table = f.getTable();
                int i = ServiceClass.searchColumn( table, expression.getColumn() );

                if ( i!=-1 )
                {

// fredt@users 20011110 - fix for 471711 - subselects
                    // todo: other error message: multiple tables are possible
                    if ( expression.getFilter()!=null && !expression.getFilter().getAlias().equals(tableName ))
                        throw new Exception("COLUMN_NOT_FOUND "+ expression.getColumn() );

                    ColumnType col = table.getColumns().getItem( i );

                    expression.setFilter( f );
                    expression.setColumnNumber( i );
                    expression.setTable( tableName );
                    expression.setDataType( col.getColumnType() );
                    expression.setColumnSize( col.getColumnSize() );
                    expression.setColumnScale( col.getColumnScale() );
                }
            }
        }

        // currently sets only data type
        // todo: calculate fixed expressions if possible
        if ( expression.getExpArg1()!=null )
        {
            resolve(expression.getExpArg1(), f );
        }

        if ( expression.getExpArg2()!=null )
        {
            resolve(expression.getExpArg2(), f );
        }

        if ( expression.getSelect()!=null )
        {
            ServiceClass.resolve( expression.getSelect(), f, false );
            ServiceClass.resolve( expression.getSelect() );
        }

        if ( expression.getDataType()!=0 )
        {
            return;
        }

        switch (expression.getType())
        {
            case FUNCTION :
//                iDataType = fFunction.getReturnType();
                break;


            case QUERY:
//                expression.setDataType( expression.getSelect().getExpColumn()[0].getDataType() );
                break;

            case NEGATE:
//                expression.setDataType( expression.getExpArg1().getDataType() );
                break;

            case ADD:
            case SUBTRACT:
            case MULTIPLY:
            case DIVIDE:
//                expression.setDataType( expression.getExpArg1().getDataType() );
                break;

            case CONCAT:
//                expression.setDataType( Types.VARCHAR );
                break;

            case NOT:
            case EQUAL:
            case BIGGER_EQUAL:
            case BIGGER:
            case SMALLER:
            case SMALLER_EQUAL:
            case NOT_EQUAL:
            case LIKE:
            case AND:
            case OR:
            case IN:
            case EXISTS:
//                expression.setDataType( Types.BIT );
                break;

            case COUNT:
//                expression.setDataType( Types.INTEGER );
                break;

            case MAX:
            case MIN:
            case SUM:
            case AVG:
//                expression.setDataType( expression.getExpArg1().getDataType() );
                break;

            case CONVERT:

                // it is already set
                break;

            case IFNULL:
            case CASEWHEN:
//                expression.setDataType( expression.getExpArg2().getDataType() );
                break;
        }
    }

    /**
     * Method declaration
     *
     *
     * @return
     */
    static boolean isResolved( ExpressionType expression )
    {

        switch (expression.getType())
        {

            case VALUE:
            case NEGATE:
                return true;

            case COLUMN:
                return expression.getFilter()!=null;
        }

        // todo: could recurse here, but never miss a 'false'!
        return false;
    }

    /**
     * Method declaration
     *
     *
     * @param i
     *
     * @return
     */
    static boolean isCompare( int i )
    {

        switch (i)
        {

            case EQUAL:
            case BIGGER_EQUAL:
            case BIGGER:
            case SMALLER:
            case SMALLER_EQUAL:
            case NOT_EQUAL:
                return true;
        }

        return false;
    }

    /**
     * Method declaration
     *
     *
     * @return
     */
    static String getTableName( ExpressionType expression )
    {

        if ( expression.getType()==ASTERIX )
        {
            return expression.getTable();
        }

        if ( expression.getType()==COLUMN )
        {
            if ( expression.getFilter()==null )
            {
                return expression.getTable();
            }
            else
            {
                return expression.getFilter().getTable().getTableName().getOriginName();
            }
        }

        // todo
        return "";
    }

    /**
     * Method declaration
     *
     *
     * @return
     */
    static String getColumnName( ExpressionType expression )
    {

        if ( expression.getType()==COLUMN )
        {
            if ( expression.getFilter()==null )
            {
                return expression.getColumn();
            }
            else
            {
                return ( expression.getFilter().getTable().getColumns().getItem( expression.getColumnNumber() ) ).getColumnName().getOriginName();
            }
        }

        return getAlias( expression );
    }

    /**
     * Method declaration
     *
     * @param type
     */
    static void setDistinctAggregate( ExpressionType expression, boolean type )
    {

        expression.setIsDistinctAggregate( type && ( expression.getExpArg1().getType()!=ASTERIX ) );

        if ( expression.getType()==COUNT )
        {
            expression.setDataType( type ? expression.getDataType()
                : Types.INTEGER );
        }
    }

    /**
     * Method declaration
     *
     *
     * @throws Exception
     */
    static void swapCondition( ExpressionType expression ) throws Exception
    {

        int i = EQUAL;

        switch (expression.getType())
        {

            case BIGGER_EQUAL:
                i = SMALLER_EQUAL;
                break;

            case SMALLER_EQUAL:
                i = BIGGER_EQUAL;
                break;

            case SMALLER:
                i = BIGGER;
                break;

            case BIGGER:
                i = SMALLER;
                break;

            case EQUAL:
                break;

            default :
        }

        expression.setType( i );

        ExpressionType e = expression.getExpArg1();

        expression.setExpArg1( expression.getExpArg2() );
        expression.setExpArg2( e );
    }

    static ExpressionType getInstance( ExpressionType e )
    {
        ExpressionType exp = new ExpressionType();
        exp.setType( e.getType() );
        exp.setDataType( e.getDataType() );
        exp.setExpArg1( e.getExpArg1() );
        exp.setExpArg2( e.getExpArg2() );
        exp.setCharLikeEscape( e.getCharLikeEscape() );
        exp.setSelect( e.getSelect() );

        checkAggregate( exp );

        return exp;
    }

    static ExpressionType getInstance( SelectType s )
    {
        ExpressionType exp = new ExpressionType();
        exp.setType( QUERY );
        exp.setSelect( s );
        return exp;
    }

    static ExpressionType getInstance( ArrayList v )
    {

        ExpressionType exp = new ExpressionType();
        exp.setType( VALUELIST );
        exp.setDataType( Types.VARCHAR );

        int size = v.size();

        exp.setValueList( new Vector( size ) );
//        exp.setValueList( new ArrayList( size ) );

        for ( int i = 0; i<size; i++ )
        {
            Object o = v.get( i );

            if ( o!=null )
            {
                ValueItemType item = new ValueItemType();
                item.setItem( ""+o );
                exp.addValueList( item );
            }
            else
            {
                exp.setIsValueListHasNull( true );
            }
        }
        return exp;
    }

    static ExpressionType getInstance( int type, ExpressionType e, ExpressionType e2 )
    {

        ExpressionType exp = new ExpressionType();
        exp.setType( type );
        exp.setExpArg1( e );
        exp.setExpArg2( e2 );

        checkAggregate( exp );
        return exp;
    }

    static ExpressionType getInstance( String table, String column )
    {
        ExpressionType exp = new ExpressionType();

        exp.setTable( table );

        if ( column==null )
        {
            exp.setType( ASTERIX );
        }
        else
        {
            exp.setType( COLUMN );
            exp.setColumn( column );
        }
        return exp;
   }

    static ExpressionType getInstance( String table, String column, boolean isquoted )
    {

        ExpressionType exp = new ExpressionType();
        exp.setTable( table );

        if ( column==null )
        {
            exp.setType( ASTERIX );
        }
        else
        {
            exp.setType( COLUMN );
            exp.setColumn( column );
        }
        return exp;
    }

    static ExpressionType getInstance( int datatype, Object o )
    {
        ExpressionType exp = new ExpressionType();

        exp.setType( VALUE );
        exp.setDataType( datatype );
        exp.setObjectData( ""+o );

        return exp;
    }
}
