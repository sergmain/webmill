/*
 * org.riverock.sql - Classes for tracking database changes
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
package org.riverock.sql.parser;

import org.riverock.schema.sql.TableType;
import org.riverock.schema.sql.TableFilterType;
import org.riverock.schema.sql.ExpressionType;

/**
 * User: Admin
 * Date: Apr 27, 2003
 * Time: 9:41:16 PM
 *
 * $Id$
 */
public class TableService
{
    /**
     * Method declaration
     *
     *
     * @param e
     *
     * @throws Exception
     */
    public static void setCondition( TableFilterType tableFilter, ExpressionType e ) throws Exception
    {

        int type = e.getType();
        ExpressionType e1 = e.getExpArg1();
        ExpressionType e2 = e.getExpArg2();

        if ( type==ExpressionService.AND )
        {
            setCondition( tableFilter, e1 );
            setCondition( tableFilter, e2 );

            return;
        }

        int candidate;

        switch (type)
        {

            case ExpressionService.NOT_EQUAL:
            case ExpressionService.LIKE:    // todo: maybe use index
            case ExpressionService.IN:
                candidate = 0;
                break;

            case ExpressionService.EQUAL:
                candidate = 1;
                break;

            case ExpressionService.BIGGER:
            case ExpressionService.BIGGER_EQUAL:
                candidate = 2;
                break;

            case ExpressionService.SMALLER:
            case ExpressionService.SMALLER_EQUAL:
                candidate = 3;
                break;

            default :

                // not a condition so forget it
                return;
        }

        if ( e1.getFilter()==tableFilter )
        {    // ok include this
        }
        else
            if ( ( e2.getFilter()==tableFilter ) && ( candidate!=0 ) )
            {

                // swap and try again to allow index usage
                ExpressionService.swapCondition( e );
                setCondition( tableFilter, e );

                return;
            }
            else
            {

                // unrelated: don't include
                return;
            }

        if ( !ExpressionService.isResolved( e2 ) )
        {
            return;
        }

        if ( candidate==0 )
        {
            addAndCondition( tableFilter, e );

            return;
        }               

        int i = e1.getColumnNumber();

// fredt@users 20020221 - patch 513005 by sqlbob@users (RMP)
// fredt - comment - this is for text tables only
/*
        if (table.isText()) {
            Index primary = table.getPrimaryIndex();


            if (index != primary) {
                Node readAll = primary.getRoot();

                while (readAll != null) {
                    readAll = readAll.getRight();
                }
            }
        }
*/

        if ( candidate==1 )
        {

            // candidate for both start & end
            if ( ( tableFilter.getExpStart()!=null ) || ( tableFilter.getExpEnd()!=null ) )
            {
                addAndCondition( tableFilter, e );

                return;
            }

            tableFilter.setExpStart( ExpressionService.getInstance( e ) );
            tableFilter.setExpEnd( tableFilter.getExpStart() );
        }
        else if ( candidate==2 )
        {

            // candidate for start
            if ( tableFilter.getExpStart()!=null )
            {
                addAndCondition( tableFilter, e );

                return;
            }

            tableFilter.setExpStart( ExpressionService.getInstance( e ) );
        }
        else if ( candidate==3 )
        {

            // candidate for end
            if ( tableFilter.getExpEnd()!=null )
            {
                addAndCondition( tableFilter, e );

                return;
            }

            tableFilter.setExpEnd( ExpressionService.getInstance( e ) );
        }

        e.setType( ExpressionService.TRUE );
    }

    /**
     * Method declaration
     *
     *
     * @param e
     */
    public static void addAndCondition( TableFilterType tableFilter, ExpressionType e )
    {

        ExpressionType e2 = ExpressionService.getInstance( e );

        if ( tableFilter.getExpAnd()==null )
        {
            tableFilter.setExpAnd( e2 );
        }
        else
        {
            ExpressionType and = ExpressionService.getInstance( ExpressionService.AND, tableFilter.getExpAnd(), e2 );

            tableFilter.setExpAnd( and );
        }

        e.setType( ExpressionService.TRUE );
    }

    public static TableFilterType getInstance( TableType t, String alias, boolean outerjoin )
    {
        TableFilterType tableFilter = new TableFilterType();
        tableFilter.setTable( t );
        if (t.getSubQuery()!=null)
        {
            if (alias!=null)
                tableFilter.setAlias(alias);
        }
        else
            tableFilter.setAlias( ( alias!=null )? alias: t.getTableName().getOriginName() );

        tableFilter.setIsOuterJoin( outerjoin );
        return tableFilter;
    }
}
