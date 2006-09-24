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
 * Class ColumnType.
 * 
 * @version $Revision$ $Date$
 */
public class ColumnType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _columnName
     */
    private org.riverock.schema.sql.SqlNameType _columnName;

    /**
     * Field _columnType
     */
    private int _columnType;

    /**
     * keeps track of state for field: _columnType
     */
    private boolean _has_columnType;

    /**
     * Field _columnSize
     */
    private int _columnSize;

    /**
     * keeps track of state for field: _columnSize
     */
    private boolean _has_columnSize;

    /**
     * Field _columnScale
     */
    private int _columnScale;

    /**
     * keeps track of state for field: _columnScale
     */
    private boolean _has_columnScale;


      //----------------/
     //- Constructors -/
    //----------------/

    public ColumnType() {
        super();
    } //-- org.riverock.schema.sql.ColumnType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method deleteColumnScale
     */
    public void deleteColumnScale()
    {
        this._has_columnScale= false;
    } //-- void deleteColumnScale() 

    /**
     * Method deleteColumnSize
     */
    public void deleteColumnSize()
    {
        this._has_columnSize= false;
    } //-- void deleteColumnSize() 

    /**
     * Method deleteColumnType
     */
    public void deleteColumnType()
    {
        this._has_columnType= false;
    } //-- void deleteColumnType() 

    /**
     * Method getColumnNameReturns the value of field 'columnName'.
     * 
     * @return the value of field 'columnName'.
     */
    public org.riverock.schema.sql.SqlNameType getColumnName()
    {
        return this._columnName;
    } //-- org.riverock.schema.sql.SqlNameType getColumnName() 

    /**
     * Method getColumnScaleReturns the value of field
     * 'columnScale'.
     * 
     * @return the value of field 'columnScale'.
     */
    public int getColumnScale()
    {
        return this._columnScale;
    } //-- int getColumnScale() 

    /**
     * Method getColumnSizeReturns the value of field 'columnSize'.
     * 
     * @return the value of field 'columnSize'.
     */
    public int getColumnSize()
    {
        return this._columnSize;
    } //-- int getColumnSize() 

    /**
     * Method getColumnTypeReturns the value of field 'columnType'.
     * 
     * @return the value of field 'columnType'.
     */
    public int getColumnType()
    {
        return this._columnType;
    } //-- int getColumnType() 

    /**
     * Method hasColumnScale
     */
    public boolean hasColumnScale()
    {
        return this._has_columnScale;
    } //-- boolean hasColumnScale() 

    /**
     * Method hasColumnSize
     */
    public boolean hasColumnSize()
    {
        return this._has_columnSize;
    } //-- boolean hasColumnSize() 

    /**
     * Method hasColumnType
     */
    public boolean hasColumnType()
    {
        return this._has_columnType;
    } //-- boolean hasColumnType() 

    /**
     * Method setColumnNameSets the value of field 'columnName'.
     * 
     * @param columnName the value of field 'columnName'.
     */
    public void setColumnName(org.riverock.schema.sql.SqlNameType columnName)
    {
        this._columnName = columnName;
    } //-- void setColumnName(org.riverock.schema.sql.SqlNameType) 

    /**
     * Method setColumnScaleSets the value of field 'columnScale'.
     * 
     * @param columnScale the value of field 'columnScale'.
     */
    public void setColumnScale(int columnScale)
    {
        this._columnScale = columnScale;
        this._has_columnScale = true;
    } //-- void setColumnScale(int) 

    /**
     * Method setColumnSizeSets the value of field 'columnSize'.
     * 
     * @param columnSize the value of field 'columnSize'.
     */
    public void setColumnSize(int columnSize)
    {
        this._columnSize = columnSize;
        this._has_columnSize = true;
    } //-- void setColumnSize(int) 

    /**
     * Method setColumnTypeSets the value of field 'columnType'.
     * 
     * @param columnType the value of field 'columnType'.
     */
    public void setColumnType(int columnType)
    {
        this._columnType = columnType;
        this._has_columnType = true;
    } //-- void setColumnType(int) 

}
