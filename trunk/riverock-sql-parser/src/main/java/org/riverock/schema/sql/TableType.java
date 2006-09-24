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
 * Class TableType.
 * 
 * @version $Revision$ $Date$
 */
public class TableType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _columnCount
     */
    private int _columnCount;

    /**
     * keeps track of state for field: _columnCount
     */
    private boolean _has_columnCount;

    /**
     * Field _visibleColumns
     */
    private int _visibleColumns;

    /**
     * keeps track of state for field: _visibleColumns
     */
    private boolean _has_visibleColumns;

    /**
     * Field _tableName
     */
    private org.riverock.schema.sql.SqlNameType _tableName;

    /**
     * Field _columns
     */
    private org.riverock.schema.sql.ColumnListType _columns;

    /**
     * Field _subQuery
     */
    private org.riverock.schema.sql.ExpressionType _subQuery;


      //----------------/
     //- Constructors -/
    //----------------/

    public TableType() {
        super();
    } //-- org.riverock.schema.sql.TableType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method deleteColumnCount
     */
    public void deleteColumnCount()
    {
        this._has_columnCount= false;
    } //-- void deleteColumnCount() 

    /**
     * Method deleteVisibleColumns
     */
    public void deleteVisibleColumns()
    {
        this._has_visibleColumns= false;
    } //-- void deleteVisibleColumns() 

    /**
     * Method getColumnCountReturns the value of field
     * 'columnCount'.
     * 
     * @return the value of field 'columnCount'.
     */
    public int getColumnCount()
    {
        return this._columnCount;
    } //-- int getColumnCount() 

    /**
     * Method getColumnsReturns the value of field 'columns'.
     * 
     * @return the value of field 'columns'.
     */
    public org.riverock.schema.sql.ColumnListType getColumns()
    {
        return this._columns;
    } //-- org.riverock.schema.sql.ColumnListType getColumns() 

    /**
     * Method getSubQueryReturns the value of field 'subQuery'.
     * 
     * @return the value of field 'subQuery'.
     */
    public org.riverock.schema.sql.ExpressionType getSubQuery()
    {
        return this._subQuery;
    } //-- org.riverock.schema.sql.ExpressionType getSubQuery() 

    /**
     * Method getTableNameReturns the value of field 'tableName'.
     * 
     * @return the value of field 'tableName'.
     */
    public org.riverock.schema.sql.SqlNameType getTableName()
    {
        return this._tableName;
    } //-- org.riverock.schema.sql.SqlNameType getTableName() 

    /**
     * Method getVisibleColumnsReturns the value of field
     * 'visibleColumns'.
     * 
     * @return the value of field 'visibleColumns'.
     */
    public int getVisibleColumns()
    {
        return this._visibleColumns;
    } //-- int getVisibleColumns() 

    /**
     * Method hasColumnCount
     */
    public boolean hasColumnCount()
    {
        return this._has_columnCount;
    } //-- boolean hasColumnCount() 

    /**
     * Method hasVisibleColumns
     */
    public boolean hasVisibleColumns()
    {
        return this._has_visibleColumns;
    } //-- boolean hasVisibleColumns() 

    /**
     * Method setColumnCountSets the value of field 'columnCount'.
     * 
     * @param columnCount the value of field 'columnCount'.
     */
    public void setColumnCount(int columnCount)
    {
        this._columnCount = columnCount;
        this._has_columnCount = true;
    } //-- void setColumnCount(int) 

    /**
     * Method setColumnsSets the value of field 'columns'.
     * 
     * @param columns the value of field 'columns'.
     */
    public void setColumns(org.riverock.schema.sql.ColumnListType columns)
    {
        this._columns = columns;
    } //-- void setColumns(org.riverock.schema.sql.ColumnListType) 

    /**
     * Method setSubQuerySets the value of field 'subQuery'.
     * 
     * @param subQuery the value of field 'subQuery'.
     */
    public void setSubQuery(org.riverock.schema.sql.ExpressionType subQuery)
    {
        this._subQuery = subQuery;
    } //-- void setSubQuery(org.riverock.schema.sql.ExpressionType) 

    /**
     * Method setTableNameSets the value of field 'tableName'.
     * 
     * @param tableName the value of field 'tableName'.
     */
    public void setTableName(org.riverock.schema.sql.SqlNameType tableName)
    {
        this._tableName = tableName;
    } //-- void setTableName(org.riverock.schema.sql.SqlNameType) 

    /**
     * Method setVisibleColumnsSets the value of field
     * 'visibleColumns'.
     * 
     * @param visibleColumns the value of field 'visibleColumns'.
     */
    public void setVisibleColumns(int visibleColumns)
    {
        this._visibleColumns = visibleColumns;
        this._has_visibleColumns = true;
    } //-- void setVisibleColumns(int) 

}
