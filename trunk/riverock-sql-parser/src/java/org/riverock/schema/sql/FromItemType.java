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

/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id$
 */

package org.riverock.schema.sql;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;

/**
 * Class FromItemType.
 * 
 * @version $Revision$ $Date$
 */
public class FromItemType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _table
     */
    private java.lang.String _table;

    /**
     * Field _subQuery
     */
    private org.riverock.schema.sql.SelectSqlType _subQuery;

    /**
     * Field _alias
     */
    private java.lang.String _alias;


      //----------------/
     //- Constructors -/
    //----------------/

    public FromItemType() {
        super();
    } //-- org.riverock.schema.sql.FromItemType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method getAliasReturns the value of field 'alias'.
     * 
     * @return the value of field 'alias'.
     */
    public java.lang.String getAlias()
    {
        return this._alias;
    } //-- java.lang.String getAlias() 

    /**
     * Method getSubQueryReturns the value of field 'subQuery'.
     * 
     * @return the value of field 'subQuery'.
     */
    public org.riverock.schema.sql.SelectSqlType getSubQuery()
    {
        return this._subQuery;
    } //-- org.riverock.schema.sql.SelectSqlType getSubQuery() 

    /**
     * Method getTableReturns the value of field 'table'.
     * 
     * @return the value of field 'table'.
     */
    public java.lang.String getTable()
    {
        return this._table;
    } //-- java.lang.String getTable() 

    /**
     * Method setAliasSets the value of field 'alias'.
     * 
     * @param alias the value of field 'alias'.
     */
    public void setAlias(java.lang.String alias)
    {
        this._alias = alias;
    } //-- void setAlias(java.lang.String) 

    /**
     * Method setSubQuerySets the value of field 'subQuery'.
     * 
     * @param subQuery the value of field 'subQuery'.
     */
    public void setSubQuery(org.riverock.schema.sql.SelectSqlType subQuery)
    {
        this._subQuery = subQuery;
    } //-- void setSubQuery(org.riverock.schema.sql.SelectSqlType) 

    /**
     * Method setTableSets the value of field 'table'.
     * 
     * @param table the value of field 'table'.
     */
    public void setTable(java.lang.String table)
    {
        this._table = table;
    } //-- void setTable(java.lang.String) 

}
