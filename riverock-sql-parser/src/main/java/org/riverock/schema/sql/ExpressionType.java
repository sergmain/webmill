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

import org.riverock.schema.sql.types.AggregateSpecType;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import java.util.Enumeration;
import java.util.Vector;

/**
 * Class ExpressionType.
 * 
 * @version $Revision$ $Date$
 */
public class ExpressionType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _objectData
     */
    private java.lang.String _objectData;

    /**
     * Field _type
     */
    private int _type;

    /**
     * keeps track of state for field: _type
     */
    private boolean _has_type;

    /**
     * Field _expArg1
     */
    private ExpressionType _expArg1;

    /**
     * Field _expArg2
     */
    private ExpressionType _expArg2;

    /**
     * Field _aggregateSpec
     */
    private org.riverock.schema.sql.types.AggregateSpecType _aggregateSpec = org.riverock.schema.sql.types.AggregateSpecType.valueOf("AGGREGATE_NONE");

    /**
     * Field _valueListList
     */
    private java.util.Vector _valueListList;

    /**
     * Field _isValueListHasNull
     */
    private boolean _isValueListHasNull;

    /**
     * keeps track of state for field: _isValueListHasNull
     */
    private boolean _has_isValueListHasNull;

    /**
     * Field _dataType
     */
    private int _dataType;

    /**
     * keeps track of state for field: _dataType
     */
    private boolean _has_dataType;

    /**
     * Field _select
     */
    private org.riverock.schema.sql.SelectType _select;

    /**
     * Field _charLikeEscape
     */
    private java.lang.String _charLikeEscape;

    /**
     * Field _table
     */
    private java.lang.String _table;

    /**
     * Field _column
     */
    private java.lang.String _column;

    /**
     * Field _filter
     */
    private org.riverock.schema.sql.TableFilterType _filter;

    /**
     * Field _columnNumber
     */
    private int _columnNumber;

    /**
     * keeps track of state for field: _columnNumber
     */
    private boolean _has_columnNumber;

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

    /**
     * Field _alias
     */
    private java.lang.String _alias;

    /**
     * Field _isDescending
     */
    private boolean _isDescending;

    /**
     * keeps track of state for field: _isDescending
     */
    private boolean _has_isDescending;

    /**
     * Field _isDistinctAggregate
     */
    private boolean _isDistinctAggregate;

    /**
     * keeps track of state for field: _isDistinctAggregate
     */
    private boolean _has_isDistinctAggregate;


      //----------------/
     //- Constructors -/
    //----------------/

    public ExpressionType() {
        super();
        setAggregateSpec(org.riverock.schema.sql.types.AggregateSpecType.valueOf("AGGREGATE_NONE"));
        _valueListList = new Vector();
    } //-- org.riverock.schema.sql.ExpressionType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addValueList
     * 
     * @param vValueList
     */
    public void addValueList(org.riverock.schema.sql.ValueItemType vValueList)
        throws java.lang.IndexOutOfBoundsException
    {
        _valueListList.addElement(vValueList);
    } //-- void addValueList(org.riverock.schema.sql.ValueItemType) 

    /**
     * Method addValueList
     * 
     * @param index
     * @param vValueList
     */
    public void addValueList(int index, org.riverock.schema.sql.ValueItemType vValueList)
        throws java.lang.IndexOutOfBoundsException
    {
        _valueListList.insertElementAt(vValueList, index);
    } //-- void addValueList(int, org.riverock.schema.sql.ValueItemType) 

    /**
     * Method deleteColumnNumber
     */
    public void deleteColumnNumber()
    {
        this._has_columnNumber= false;
    } //-- void deleteColumnNumber() 

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
     * Method deleteDataType
     */
    public void deleteDataType()
    {
        this._has_dataType= false;
    } //-- void deleteDataType() 

    /**
     * Method deleteIsDescending
     */
    public void deleteIsDescending()
    {
        this._has_isDescending= false;
    } //-- void deleteIsDescending() 

    /**
     * Method deleteIsDistinctAggregate
     */
    public void deleteIsDistinctAggregate()
    {
        this._has_isDistinctAggregate= false;
    } //-- void deleteIsDistinctAggregate() 

    /**
     * Method deleteIsValueListHasNull
     */
    public void deleteIsValueListHasNull()
    {
        this._has_isValueListHasNull= false;
    } //-- void deleteIsValueListHasNull() 

    /**
     * Method deleteType
     */
    public void deleteType()
    {
        this._has_type= false;
    } //-- void deleteType() 

    /**
     * Method enumerateValueList
     */
    public java.util.Enumeration enumerateValueList()
    {
        return _valueListList.elements();
    } //-- java.util.Enumeration enumerateValueList() 

    /**
     * Method getAggregateSpecReturns the value of field
     * 'aggregateSpec'.
     * 
     * @return the value of field 'aggregateSpec'.
     */
    public org.riverock.schema.sql.types.AggregateSpecType getAggregateSpec()
    {
        return this._aggregateSpec;
    } //-- org.riverock.schema.sql.types.AggregateSpecType getAggregateSpec() 

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
     * Method getCharLikeEscapeReturns the value of field
     * 'charLikeEscape'.
     * 
     * @return the value of field 'charLikeEscape'.
     */
    public java.lang.String getCharLikeEscape()
    {
        return this._charLikeEscape;
    } //-- java.lang.String getCharLikeEscape() 

    /**
     * Method getColumnReturns the value of field 'column'.
     * 
     * @return the value of field 'column'.
     */
    public java.lang.String getColumn()
    {
        return this._column;
    } //-- java.lang.String getColumn() 

    /**
     * Method getColumnNumberReturns the value of field
     * 'columnNumber'.
     * 
     * @return the value of field 'columnNumber'.
     */
    public int getColumnNumber()
    {
        return this._columnNumber;
    } //-- int getColumnNumber() 

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
     * Method getDataTypeReturns the value of field 'dataType'.
     * 
     * @return the value of field 'dataType'.
     */
    public int getDataType()
    {
        return this._dataType;
    } //-- int getDataType() 

    /**
     * Method getExpArg1Returns the value of field 'expArg1'.
     * 
     * @return the value of field 'expArg1'.
     */
    public org.riverock.schema.sql.ExpressionType getExpArg1()
    {
        return this._expArg1;
    } //-- org.riverock.schema.sql.ExpressionType getExpArg1() 

    /**
     * Method getExpArg2Returns the value of field 'expArg2'.
     * 
     * @return the value of field 'expArg2'.
     */
    public org.riverock.schema.sql.ExpressionType getExpArg2()
    {
        return this._expArg2;
    } //-- org.riverock.schema.sql.ExpressionType getExpArg2() 

    /**
     * Method getFilterReturns the value of field 'filter'.
     * 
     * @return the value of field 'filter'.
     */
    public org.riverock.schema.sql.TableFilterType getFilter()
    {
        return this._filter;
    } //-- org.riverock.schema.sql.TableFilterType getFilter() 

    /**
     * Method getIsDescendingReturns the value of field
     * 'isDescending'.
     * 
     * @return the value of field 'isDescending'.
     */
    public boolean getIsDescending()
    {
        return this._isDescending;
    } //-- boolean getIsDescending() 

    /**
     * Method getIsDistinctAggregateReturns the value of field
     * 'isDistinctAggregate'.
     * 
     * @return the value of field 'isDistinctAggregate'.
     */
    public boolean getIsDistinctAggregate()
    {
        return this._isDistinctAggregate;
    } //-- boolean getIsDistinctAggregate() 

    /**
     * Method getIsValueListHasNullReturns the value of field
     * 'isValueListHasNull'.
     * 
     * @return the value of field 'isValueListHasNull'.
     */
    public boolean getIsValueListHasNull()
    {
        return this._isValueListHasNull;
    } //-- boolean getIsValueListHasNull() 

    /**
     * Method getObjectDataReturns the value of field 'objectData'.
     * 
     * @return the value of field 'objectData'.
     */
    public java.lang.String getObjectData()
    {
        return this._objectData;
    } //-- java.lang.String getObjectData() 

    /**
     * Method getSelectReturns the value of field 'select'.
     * 
     * @return the value of field 'select'.
     */
    public org.riverock.schema.sql.SelectType getSelect()
    {
        return this._select;
    } //-- org.riverock.schema.sql.SelectType getSelect() 

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
     * Method getTypeReturns the value of field 'type'.
     * 
     * @return the value of field 'type'.
     */
    public int getType()
    {
        return this._type;
    } //-- int getType() 

    /**
     * Method getValueList
     * 
     * @param index
     */
    public org.riverock.schema.sql.ValueItemType getValueList(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _valueListList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (org.riverock.schema.sql.ValueItemType) _valueListList.elementAt(index);
    } //-- org.riverock.schema.sql.ValueItemType getValueList(int) 

    /**
     * Method getValueList
     */
    public org.riverock.schema.sql.ValueItemType[] getValueList()
    {
        int size = _valueListList.size();
        org.riverock.schema.sql.ValueItemType[] mArray = new org.riverock.schema.sql.ValueItemType[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (org.riverock.schema.sql.ValueItemType) _valueListList.elementAt(index);
        }
        return mArray;
    } //-- org.riverock.schema.sql.ValueItemType[] getValueList() 

    /**
     * Method getValueListAsReferenceReturns a reference to
     * 'valueList'. No type checking is performed on any
     * modications to the Vector.
     * 
     * @return returns a reference to the Vector.
     */
    public java.util.Vector getValueListAsReference()
    {
        return _valueListList;
    } //-- java.util.Vector getValueListAsReference() 

    /**
     * Method getValueListCount
     */
    public int getValueListCount()
    {
        return _valueListList.size();
    } //-- int getValueListCount() 

    /**
     * Method hasColumnNumber
     */
    public boolean hasColumnNumber()
    {
        return this._has_columnNumber;
    } //-- boolean hasColumnNumber() 

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
     * Method hasDataType
     */
    public boolean hasDataType()
    {
        return this._has_dataType;
    } //-- boolean hasDataType() 

    /**
     * Method hasIsDescending
     */
    public boolean hasIsDescending()
    {
        return this._has_isDescending;
    } //-- boolean hasIsDescending() 

    /**
     * Method hasIsDistinctAggregate
     */
    public boolean hasIsDistinctAggregate()
    {
        return this._has_isDistinctAggregate;
    } //-- boolean hasIsDistinctAggregate() 

    /**
     * Method hasIsValueListHasNull
     */
    public boolean hasIsValueListHasNull()
    {
        return this._has_isValueListHasNull;
    } //-- boolean hasIsValueListHasNull() 

    /**
     * Method hasType
     */
    public boolean hasType()
    {
        return this._has_type;
    } //-- boolean hasType() 

    /**
     * Method removeAllValueList
     */
    public void removeAllValueList()
    {
        _valueListList.removeAllElements();
    } //-- void removeAllValueList() 

    /**
     * Method removeValueList
     * 
     * @param index
     */
    public org.riverock.schema.sql.ValueItemType removeValueList(int index)
    {
        java.lang.Object obj = _valueListList.elementAt(index);
        _valueListList.removeElementAt(index);
        return (org.riverock.schema.sql.ValueItemType) obj;
    } //-- org.riverock.schema.sql.ValueItemType removeValueList(int) 

    /**
     * Method setAggregateSpecSets the value of field
     * 'aggregateSpec'.
     * 
     * @param aggregateSpec the value of field 'aggregateSpec'.
     */
    public void setAggregateSpec(org.riverock.schema.sql.types.AggregateSpecType aggregateSpec)
    {
        this._aggregateSpec = aggregateSpec;
    } //-- void setAggregateSpec(org.riverock.schema.sql.types.AggregateSpecType) 

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
     * Method setCharLikeEscapeSets the value of field
     * 'charLikeEscape'.
     * 
     * @param charLikeEscape the value of field 'charLikeEscape'.
     */
    public void setCharLikeEscape(java.lang.String charLikeEscape)
    {
        this._charLikeEscape = charLikeEscape;
    } //-- void setCharLikeEscape(java.lang.String) 

    /**
     * Method setColumnSets the value of field 'column'.
     * 
     * @param column the value of field 'column'.
     */
    public void setColumn(java.lang.String column)
    {
        this._column = column;
    } //-- void setColumn(java.lang.String) 

    /**
     * Method setColumnNumberSets the value of field
     * 'columnNumber'.
     * 
     * @param columnNumber the value of field 'columnNumber'.
     */
    public void setColumnNumber(int columnNumber)
    {
        this._columnNumber = columnNumber;
        this._has_columnNumber = true;
    } //-- void setColumnNumber(int) 

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
     * Method setDataTypeSets the value of field 'dataType'.
     * 
     * @param dataType the value of field 'dataType'.
     */
    public void setDataType(int dataType)
    {
        this._dataType = dataType;
        this._has_dataType = true;
    } //-- void setDataType(int) 

    /**
     * Method setExpArg1Sets the value of field 'expArg1'.
     * 
     * @param expArg1 the value of field 'expArg1'.
     */
    public void setExpArg1(org.riverock.schema.sql.ExpressionType expArg1)
    {
        this._expArg1 = expArg1;
    } //-- void setExpArg1(org.riverock.schema.sql.ExpressionType) 

    /**
     * Method setExpArg2Sets the value of field 'expArg2'.
     * 
     * @param expArg2 the value of field 'expArg2'.
     */
    public void setExpArg2(org.riverock.schema.sql.ExpressionType expArg2)
    {
        this._expArg2 = expArg2;
    } //-- void setExpArg2(org.riverock.schema.sql.ExpressionType) 

    /**
     * Method setFilterSets the value of field 'filter'.
     * 
     * @param filter the value of field 'filter'.
     */
    public void setFilter(org.riverock.schema.sql.TableFilterType filter)
    {
        this._filter = filter;
    } //-- void setFilter(org.riverock.schema.sql.TableFilterType) 

    /**
     * Method setIsDescendingSets the value of field
     * 'isDescending'.
     * 
     * @param isDescending the value of field 'isDescending'.
     */
    public void setIsDescending(boolean isDescending)
    {
        this._isDescending = isDescending;
        this._has_isDescending = true;
    } //-- void setIsDescending(boolean) 

    /**
     * Method setIsDistinctAggregateSets the value of field
     * 'isDistinctAggregate'.
     * 
     * @param isDistinctAggregate the value of field
     * 'isDistinctAggregate'.
     */
    public void setIsDistinctAggregate(boolean isDistinctAggregate)
    {
        this._isDistinctAggregate = isDistinctAggregate;
        this._has_isDistinctAggregate = true;
    } //-- void setIsDistinctAggregate(boolean) 

    /**
     * Method setIsValueListHasNullSets the value of field
     * 'isValueListHasNull'.
     * 
     * @param isValueListHasNull the value of field
     * 'isValueListHasNull'.
     */
    public void setIsValueListHasNull(boolean isValueListHasNull)
    {
        this._isValueListHasNull = isValueListHasNull;
        this._has_isValueListHasNull = true;
    } //-- void setIsValueListHasNull(boolean) 

    /**
     * Method setObjectDataSets the value of field 'objectData'.
     * 
     * @param objectData the value of field 'objectData'.
     */
    public void setObjectData(java.lang.String objectData)
    {
        this._objectData = objectData;
    } //-- void setObjectData(java.lang.String) 

    /**
     * Method setSelectSets the value of field 'select'.
     * 
     * @param select the value of field 'select'.
     */
    public void setSelect(org.riverock.schema.sql.SelectType select)
    {
        this._select = select;
    } //-- void setSelect(org.riverock.schema.sql.SelectType) 

    /**
     * Method setTableSets the value of field 'table'.
     * 
     * @param table the value of field 'table'.
     */
    public void setTable(java.lang.String table)
    {
        this._table = table;
    } //-- void setTable(java.lang.String) 

    /**
     * Method setTypeSets the value of field 'type'.
     * 
     * @param type the value of field 'type'.
     */
    public void setType(int type)
    {
        this._type = type;
        this._has_type = true;
    } //-- void setType(int) 

    /**
     * Method setValueList
     * 
     * @param index
     * @param vValueList
     */
    public void setValueList(int index, org.riverock.schema.sql.ValueItemType vValueList)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _valueListList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _valueListList.setElementAt(vValueList, index);
    } //-- void setValueList(int, org.riverock.schema.sql.ValueItemType) 

    /**
     * Method setValueList
     * 
     * @param valueListArray
     */
    public void setValueList(org.riverock.schema.sql.ValueItemType[] valueListArray)
    {
        //-- copy array
        _valueListList.removeAllElements();
        for (int i = 0; i < valueListArray.length; i++) {
            _valueListList.addElement(valueListArray[i]);
        }
    } //-- void setValueList(org.riverock.schema.sql.ValueItemType) 

    /**
     * Method setValueListSets the value of 'valueList' by copying
     * the given Vector.
     * 
     * @param valueListVector the Vector to copy.
     */
    public void setValueList(java.util.Vector valueListVector)
    {
        //-- copy vector
        _valueListList.removeAllElements();
        for (int i = 0; i < valueListVector.size(); i++) {
            _valueListList.addElement((org.riverock.schema.sql.ValueItemType)valueListVector.elementAt(i));
        }
    } //-- void setValueList(java.util.Vector) 

    /**
     * Method setValueListAsReferenceSets the value of 'valueList'
     * by setting it to the given Vector. No type checking is
     * performed.
     * 
     * @param valueListVector the Vector to copy.
     */
    public void setValueListAsReference(java.util.Vector valueListVector)
    {
        _valueListList = valueListVector;
    } //-- void setValueListAsReference(java.util.Vector) 

}
