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

import org.riverock.schema.sql.types.UnionTypeType;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import java.util.Enumeration;
import java.util.Vector;

/**
 * Class SelectType.
 * 
 * @version $Revision$ $Date$
 */
public class SelectType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _isPreProcess
     */
    private boolean _isPreProcess;

    /**
     * keeps track of state for field: _isPreProcess
     */
    private boolean _has_isPreProcess;

    /**
     * Field _isDistinctSelect
     */
    private boolean _isDistinctSelect;

    /**
     * keeps track of state for field: _isDistinctSelect
     */
    private boolean _has_isDistinctSelect;

    /**
     * Field _isAggregated
     */
    private boolean _isAggregated;

    /**
     * keeps track of state for field: _isAggregated
     */
    private boolean _has_isAggregated;

    /**
     * Field _isGrouped
     */
    private boolean _isGrouped;

    /**
     * keeps track of state for field: _isGrouped
     */
    private boolean _has_isGrouped;

    /**
     * Field _groupColumnNamesList
     */
    private java.util.Vector _groupColumnNamesList;

    /**
     * Field _tableFilterList
     */
    private java.util.Vector _tableFilterList;

    /**
     * Field _expCondition
     */
    private org.riverock.schema.sql.ExpressionType _expCondition;

    /**
     * Field _expHavingCondition
     */
    private org.riverock.schema.sql.ExpressionType _expHavingCondition;

    /**
     * Field _expColumnList
     */
    private java.util.Vector _expColumnList;

    /**
     * Field _resultLength
     */
    private int _resultLength;

    /**
     * keeps track of state for field: _resultLength
     */
    private boolean _has_resultLength;

    /**
     * Field _groupLength
     */
    private int _groupLength;

    /**
     * keeps track of state for field: _groupLength
     */
    private boolean _has_groupLength;

    /**
     * Field _havingIndex
     */
    private int _havingIndex;

    /**
     * keeps track of state for field: _havingIndex
     */
    private boolean _has_havingIndex;

    /**
     * Field _orderLength
     */
    private int _orderLength;

    /**
     * keeps track of state for field: _orderLength
     */
    private boolean _has_orderLength;

    /**
     * Field _union
     */
    private SelectType _union;

    /**
     * Field _intoTableName
     */
    private org.riverock.schema.sql.SqlNameType _intoTableName;

    /**
     * Field _isIntoTableQuoted
     */
    private boolean _isIntoTableQuoted;

    /**
     * keeps track of state for field: _isIntoTableQuoted
     */
    private boolean _has_isIntoTableQuoted;

    /**
     * Field _unionType
     */
    private org.riverock.schema.sql.types.UnionTypeType _unionType;

    /**
     * Field _limitStart
     */
    private int _limitStart;

    /**
     * keeps track of state for field: _limitStart
     */
    private boolean _has_limitStart;

    /**
     * Field _limitCount
     */
    private int _limitCount;

    /**
     * keeps track of state for field: _limitCount
     */
    private boolean _has_limitCount;


      //----------------/
     //- Constructors -/
    //----------------/

    public SelectType() {
        super();
        _groupColumnNamesList = new Vector();
        _tableFilterList = new Vector();
        _expColumnList = new Vector();
    } //-- org.riverock.schema.sql.SelectType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addExpColumn
     * 
     * @param vExpColumn
     */
    public void addExpColumn(org.riverock.schema.sql.ExpressionType vExpColumn)
        throws java.lang.IndexOutOfBoundsException
    {
        _expColumnList.addElement(vExpColumn);
    } //-- void addExpColumn(org.riverock.schema.sql.ExpressionType) 

    /**
     * Method addExpColumn
     * 
     * @param index
     * @param vExpColumn
     */
    public void addExpColumn(int index, org.riverock.schema.sql.ExpressionType vExpColumn)
        throws java.lang.IndexOutOfBoundsException
    {
        _expColumnList.insertElementAt(vExpColumn, index);
    } //-- void addExpColumn(int, org.riverock.schema.sql.ExpressionType) 

    /**
     * Method addGroupColumnNames
     * 
     * @param vGroupColumnNames
     */
    public void addGroupColumnNames(org.riverock.schema.sql.SqlNameType vGroupColumnNames)
        throws java.lang.IndexOutOfBoundsException
    {
        _groupColumnNamesList.addElement(vGroupColumnNames);
    } //-- void addGroupColumnNames(org.riverock.schema.sql.SqlNameType) 

    /**
     * Method addGroupColumnNames
     * 
     * @param index
     * @param vGroupColumnNames
     */
    public void addGroupColumnNames(int index, org.riverock.schema.sql.SqlNameType vGroupColumnNames)
        throws java.lang.IndexOutOfBoundsException
    {
        _groupColumnNamesList.insertElementAt(vGroupColumnNames, index);
    } //-- void addGroupColumnNames(int, org.riverock.schema.sql.SqlNameType) 

    /**
     * Method addTableFilter
     * 
     * @param vTableFilter
     */
    public void addTableFilter(org.riverock.schema.sql.TableFilterType vTableFilter)
        throws java.lang.IndexOutOfBoundsException
    {
        _tableFilterList.addElement(vTableFilter);
    } //-- void addTableFilter(org.riverock.schema.sql.TableFilterType) 

    /**
     * Method addTableFilter
     * 
     * @param index
     * @param vTableFilter
     */
    public void addTableFilter(int index, org.riverock.schema.sql.TableFilterType vTableFilter)
        throws java.lang.IndexOutOfBoundsException
    {
        _tableFilterList.insertElementAt(vTableFilter, index);
    } //-- void addTableFilter(int, org.riverock.schema.sql.TableFilterType) 

    /**
     * Method deleteGroupLength
     */
    public void deleteGroupLength()
    {
        this._has_groupLength= false;
    } //-- void deleteGroupLength() 

    /**
     * Method deleteHavingIndex
     */
    public void deleteHavingIndex()
    {
        this._has_havingIndex= false;
    } //-- void deleteHavingIndex() 

    /**
     * Method deleteIsAggregated
     */
    public void deleteIsAggregated()
    {
        this._has_isAggregated= false;
    } //-- void deleteIsAggregated() 

    /**
     * Method deleteIsDistinctSelect
     */
    public void deleteIsDistinctSelect()
    {
        this._has_isDistinctSelect= false;
    } //-- void deleteIsDistinctSelect() 

    /**
     * Method deleteIsGrouped
     */
    public void deleteIsGrouped()
    {
        this._has_isGrouped= false;
    } //-- void deleteIsGrouped() 

    /**
     * Method deleteIsIntoTableQuoted
     */
    public void deleteIsIntoTableQuoted()
    {
        this._has_isIntoTableQuoted= false;
    } //-- void deleteIsIntoTableQuoted() 

    /**
     * Method deleteIsPreProcess
     */
    public void deleteIsPreProcess()
    {
        this._has_isPreProcess= false;
    } //-- void deleteIsPreProcess() 

    /**
     * Method deleteLimitCount
     */
    public void deleteLimitCount()
    {
        this._has_limitCount= false;
    } //-- void deleteLimitCount() 

    /**
     * Method deleteLimitStart
     */
    public void deleteLimitStart()
    {
        this._has_limitStart= false;
    } //-- void deleteLimitStart() 

    /**
     * Method deleteOrderLength
     */
    public void deleteOrderLength()
    {
        this._has_orderLength= false;
    } //-- void deleteOrderLength() 

    /**
     * Method deleteResultLength
     */
    public void deleteResultLength()
    {
        this._has_resultLength= false;
    } //-- void deleteResultLength() 

    /**
     * Method enumerateExpColumn
     */
    public java.util.Enumeration enumerateExpColumn()
    {
        return _expColumnList.elements();
    } //-- java.util.Enumeration enumerateExpColumn() 

    /**
     * Method enumerateGroupColumnNames
     */
    public java.util.Enumeration enumerateGroupColumnNames()
    {
        return _groupColumnNamesList.elements();
    } //-- java.util.Enumeration enumerateGroupColumnNames() 

    /**
     * Method enumerateTableFilter
     */
    public java.util.Enumeration enumerateTableFilter()
    {
        return _tableFilterList.elements();
    } //-- java.util.Enumeration enumerateTableFilter() 

    /**
     * Method getExpColumn
     * 
     * @param index
     */
    public org.riverock.schema.sql.ExpressionType getExpColumn(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _expColumnList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (org.riverock.schema.sql.ExpressionType) _expColumnList.elementAt(index);
    } //-- org.riverock.schema.sql.ExpressionType getExpColumn(int) 

    /**
     * Method getExpColumn
     */
    public org.riverock.schema.sql.ExpressionType[] getExpColumn()
    {
        int size = _expColumnList.size();
        org.riverock.schema.sql.ExpressionType[] mArray = new org.riverock.schema.sql.ExpressionType[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (org.riverock.schema.sql.ExpressionType) _expColumnList.elementAt(index);
        }
        return mArray;
    } //-- org.riverock.schema.sql.ExpressionType[] getExpColumn() 

    /**
     * Method getExpColumnAsReferenceReturns a reference to
     * 'expColumn'. No type checking is performed on any
     * modications to the Vector.
     * 
     * @return returns a reference to the Vector.
     */
    public java.util.Vector getExpColumnAsReference()
    {
        return _expColumnList;
    } //-- java.util.Vector getExpColumnAsReference() 

    /**
     * Method getExpColumnCount
     */
    public int getExpColumnCount()
    {
        return _expColumnList.size();
    } //-- int getExpColumnCount() 

    /**
     * Method getExpConditionReturns the value of field
     * 'expCondition'.
     * 
     * @return the value of field 'expCondition'.
     */
    public org.riverock.schema.sql.ExpressionType getExpCondition()
    {
        return this._expCondition;
    } //-- org.riverock.schema.sql.ExpressionType getExpCondition() 

    /**
     * Method getExpHavingConditionReturns the value of field
     * 'expHavingCondition'.
     * 
     * @return the value of field 'expHavingCondition'.
     */
    public org.riverock.schema.sql.ExpressionType getExpHavingCondition()
    {
        return this._expHavingCondition;
    } //-- org.riverock.schema.sql.ExpressionType getExpHavingCondition() 

    /**
     * Method getGroupColumnNames
     * 
     * @param index
     */
    public org.riverock.schema.sql.SqlNameType getGroupColumnNames(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _groupColumnNamesList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (org.riverock.schema.sql.SqlNameType) _groupColumnNamesList.elementAt(index);
    } //-- org.riverock.schema.sql.SqlNameType getGroupColumnNames(int) 

    /**
     * Method getGroupColumnNames
     */
    public org.riverock.schema.sql.SqlNameType[] getGroupColumnNames()
    {
        int size = _groupColumnNamesList.size();
        org.riverock.schema.sql.SqlNameType[] mArray = new org.riverock.schema.sql.SqlNameType[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (org.riverock.schema.sql.SqlNameType) _groupColumnNamesList.elementAt(index);
        }
        return mArray;
    } //-- org.riverock.schema.sql.SqlNameType[] getGroupColumnNames() 

    /**
     * Method getGroupColumnNamesAsReferenceReturns a reference to
     * 'groupColumnNames'. No type checking is performed on any
     * modications to the Vector.
     * 
     * @return returns a reference to the Vector.
     */
    public java.util.Vector getGroupColumnNamesAsReference()
    {
        return _groupColumnNamesList;
    } //-- java.util.Vector getGroupColumnNamesAsReference() 

    /**
     * Method getGroupColumnNamesCount
     */
    public int getGroupColumnNamesCount()
    {
        return _groupColumnNamesList.size();
    } //-- int getGroupColumnNamesCount() 

    /**
     * Method getGroupLengthReturns the value of field
     * 'groupLength'.
     * 
     * @return the value of field 'groupLength'.
     */
    public int getGroupLength()
    {
        return this._groupLength;
    } //-- int getGroupLength() 

    /**
     * Method getHavingIndexReturns the value of field
     * 'havingIndex'.
     * 
     * @return the value of field 'havingIndex'.
     */
    public int getHavingIndex()
    {
        return this._havingIndex;
    } //-- int getHavingIndex() 

    /**
     * Method getIntoTableNameReturns the value of field
     * 'intoTableName'.
     * 
     * @return the value of field 'intoTableName'.
     */
    public org.riverock.schema.sql.SqlNameType getIntoTableName()
    {
        return this._intoTableName;
    } //-- org.riverock.schema.sql.SqlNameType getIntoTableName() 

    /**
     * Method getIsAggregatedReturns the value of field
     * 'isAggregated'.
     * 
     * @return the value of field 'isAggregated'.
     */
    public boolean getIsAggregated()
    {
        return this._isAggregated;
    } //-- boolean getIsAggregated() 

    /**
     * Method getIsDistinctSelectReturns the value of field
     * 'isDistinctSelect'.
     * 
     * @return the value of field 'isDistinctSelect'.
     */
    public boolean getIsDistinctSelect()
    {
        return this._isDistinctSelect;
    } //-- boolean getIsDistinctSelect() 

    /**
     * Method getIsGroupedReturns the value of field 'isGrouped'.
     * 
     * @return the value of field 'isGrouped'.
     */
    public boolean getIsGrouped()
    {
        return this._isGrouped;
    } //-- boolean getIsGrouped() 

    /**
     * Method getIsIntoTableQuotedReturns the value of field
     * 'isIntoTableQuoted'.
     * 
     * @return the value of field 'isIntoTableQuoted'.
     */
    public boolean getIsIntoTableQuoted()
    {
        return this._isIntoTableQuoted;
    } //-- boolean getIsIntoTableQuoted() 

    /**
     * Method getIsPreProcessReturns the value of field
     * 'isPreProcess'.
     * 
     * @return the value of field 'isPreProcess'.
     */
    public boolean getIsPreProcess()
    {
        return this._isPreProcess;
    } //-- boolean getIsPreProcess() 

    /**
     * Method getLimitCountReturns the value of field 'limitCount'.
     * 
     * @return the value of field 'limitCount'.
     */
    public int getLimitCount()
    {
        return this._limitCount;
    } //-- int getLimitCount() 

    /**
     * Method getLimitStartReturns the value of field 'limitStart'.
     * 
     * @return the value of field 'limitStart'.
     */
    public int getLimitStart()
    {
        return this._limitStart;
    } //-- int getLimitStart() 

    /**
     * Method getOrderLengthReturns the value of field
     * 'orderLength'.
     * 
     * @return the value of field 'orderLength'.
     */
    public int getOrderLength()
    {
        return this._orderLength;
    } //-- int getOrderLength() 

    /**
     * Method getResultLengthReturns the value of field
     * 'resultLength'.
     * 
     * @return the value of field 'resultLength'.
     */
    public int getResultLength()
    {
        return this._resultLength;
    } //-- int getResultLength() 

    /**
     * Method getTableFilter
     * 
     * @param index
     */
    public org.riverock.schema.sql.TableFilterType getTableFilter(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _tableFilterList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (org.riverock.schema.sql.TableFilterType) _tableFilterList.elementAt(index);
    } //-- org.riverock.schema.sql.TableFilterType getTableFilter(int) 

    /**
     * Method getTableFilter
     */
    public org.riverock.schema.sql.TableFilterType[] getTableFilter()
    {
        int size = _tableFilterList.size();
        org.riverock.schema.sql.TableFilterType[] mArray = new org.riverock.schema.sql.TableFilterType[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (org.riverock.schema.sql.TableFilterType) _tableFilterList.elementAt(index);
        }
        return mArray;
    } //-- org.riverock.schema.sql.TableFilterType[] getTableFilter() 

    /**
     * Method getTableFilterAsReferenceReturns a reference to
     * 'tableFilter'. No type checking is performed on any
     * modications to the Vector.
     * 
     * @return returns a reference to the Vector.
     */
    public java.util.Vector getTableFilterAsReference()
    {
        return _tableFilterList;
    } //-- java.util.Vector getTableFilterAsReference() 

    /**
     * Method getTableFilterCount
     */
    public int getTableFilterCount()
    {
        return _tableFilterList.size();
    } //-- int getTableFilterCount() 

    /**
     * Method getUnionReturns the value of field 'union'.
     * 
     * @return the value of field 'union'.
     */
    public org.riverock.schema.sql.SelectType getUnion()
    {
        return this._union;
    } //-- org.riverock.schema.sql.SelectType getUnion() 

    /**
     * Method getUnionTypeReturns the value of field 'unionType'.
     * 
     * @return the value of field 'unionType'.
     */
    public org.riverock.schema.sql.types.UnionTypeType getUnionType()
    {
        return this._unionType;
    } //-- org.riverock.schema.sql.types.UnionTypeType getUnionType() 

    /**
     * Method hasGroupLength
     */
    public boolean hasGroupLength()
    {
        return this._has_groupLength;
    } //-- boolean hasGroupLength() 

    /**
     * Method hasHavingIndex
     */
    public boolean hasHavingIndex()
    {
        return this._has_havingIndex;
    } //-- boolean hasHavingIndex() 

    /**
     * Method hasIsAggregated
     */
    public boolean hasIsAggregated()
    {
        return this._has_isAggregated;
    } //-- boolean hasIsAggregated() 

    /**
     * Method hasIsDistinctSelect
     */
    public boolean hasIsDistinctSelect()
    {
        return this._has_isDistinctSelect;
    } //-- boolean hasIsDistinctSelect() 

    /**
     * Method hasIsGrouped
     */
    public boolean hasIsGrouped()
    {
        return this._has_isGrouped;
    } //-- boolean hasIsGrouped() 

    /**
     * Method hasIsIntoTableQuoted
     */
    public boolean hasIsIntoTableQuoted()
    {
        return this._has_isIntoTableQuoted;
    } //-- boolean hasIsIntoTableQuoted() 

    /**
     * Method hasIsPreProcess
     */
    public boolean hasIsPreProcess()
    {
        return this._has_isPreProcess;
    } //-- boolean hasIsPreProcess() 

    /**
     * Method hasLimitCount
     */
    public boolean hasLimitCount()
    {
        return this._has_limitCount;
    } //-- boolean hasLimitCount() 

    /**
     * Method hasLimitStart
     */
    public boolean hasLimitStart()
    {
        return this._has_limitStart;
    } //-- boolean hasLimitStart() 

    /**
     * Method hasOrderLength
     */
    public boolean hasOrderLength()
    {
        return this._has_orderLength;
    } //-- boolean hasOrderLength() 

    /**
     * Method hasResultLength
     */
    public boolean hasResultLength()
    {
        return this._has_resultLength;
    } //-- boolean hasResultLength() 

    /**
     * Method removeAllExpColumn
     */
    public void removeAllExpColumn()
    {
        _expColumnList.removeAllElements();
    } //-- void removeAllExpColumn() 

    /**
     * Method removeAllGroupColumnNames
     */
    public void removeAllGroupColumnNames()
    {
        _groupColumnNamesList.removeAllElements();
    } //-- void removeAllGroupColumnNames() 

    /**
     * Method removeAllTableFilter
     */
    public void removeAllTableFilter()
    {
        _tableFilterList.removeAllElements();
    } //-- void removeAllTableFilter() 

    /**
     * Method removeExpColumn
     * 
     * @param index
     */
    public org.riverock.schema.sql.ExpressionType removeExpColumn(int index)
    {
        java.lang.Object obj = _expColumnList.elementAt(index);
        _expColumnList.removeElementAt(index);
        return (org.riverock.schema.sql.ExpressionType) obj;
    } //-- org.riverock.schema.sql.ExpressionType removeExpColumn(int) 

    /**
     * Method removeGroupColumnNames
     * 
     * @param index
     */
    public org.riverock.schema.sql.SqlNameType removeGroupColumnNames(int index)
    {
        java.lang.Object obj = _groupColumnNamesList.elementAt(index);
        _groupColumnNamesList.removeElementAt(index);
        return (org.riverock.schema.sql.SqlNameType) obj;
    } //-- org.riverock.schema.sql.SqlNameType removeGroupColumnNames(int) 

    /**
     * Method removeTableFilter
     * 
     * @param index
     */
    public org.riverock.schema.sql.TableFilterType removeTableFilter(int index)
    {
        java.lang.Object obj = _tableFilterList.elementAt(index);
        _tableFilterList.removeElementAt(index);
        return (org.riverock.schema.sql.TableFilterType) obj;
    } //-- org.riverock.schema.sql.TableFilterType removeTableFilter(int) 

    /**
     * Method setExpColumn
     * 
     * @param index
     * @param vExpColumn
     */
    public void setExpColumn(int index, org.riverock.schema.sql.ExpressionType vExpColumn)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _expColumnList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _expColumnList.setElementAt(vExpColumn, index);
    } //-- void setExpColumn(int, org.riverock.schema.sql.ExpressionType) 

    /**
     * Method setExpColumn
     * 
     * @param expColumnArray
     */
    public void setExpColumn(org.riverock.schema.sql.ExpressionType[] expColumnArray)
    {
        //-- copy array
        _expColumnList.removeAllElements();
        for (int i = 0; i < expColumnArray.length; i++) {
            _expColumnList.addElement(expColumnArray[i]);
        }
    } //-- void setExpColumn(org.riverock.schema.sql.ExpressionType) 

    /**
     * Method setExpColumnSets the value of 'expColumn' by copying
     * the given Vector.
     * 
     * @param expColumnVector the Vector to copy.
     */
    public void setExpColumn(java.util.Vector expColumnVector)
    {
        //-- copy vector
        _expColumnList.removeAllElements();
        for (int i = 0; i < expColumnVector.size(); i++) {
            _expColumnList.addElement((org.riverock.schema.sql.ExpressionType)expColumnVector.elementAt(i));
        }
    } //-- void setExpColumn(java.util.Vector) 

    /**
     * Method setExpColumnAsReferenceSets the value of 'expColumn'
     * by setting it to the given Vector. No type checking is
     * performed.
     * 
     * @param expColumnVector the Vector to copy.
     */
    public void setExpColumnAsReference(java.util.Vector expColumnVector)
    {
        _expColumnList = expColumnVector;
    } //-- void setExpColumnAsReference(java.util.Vector) 

    /**
     * Method setExpConditionSets the value of field
     * 'expCondition'.
     * 
     * @param expCondition the value of field 'expCondition'.
     */
    public void setExpCondition(org.riverock.schema.sql.ExpressionType expCondition)
    {
        this._expCondition = expCondition;
    } //-- void setExpCondition(org.riverock.schema.sql.ExpressionType) 

    /**
     * Method setExpHavingConditionSets the value of field
     * 'expHavingCondition'.
     * 
     * @param expHavingCondition the value of field
     * 'expHavingCondition'.
     */
    public void setExpHavingCondition(org.riverock.schema.sql.ExpressionType expHavingCondition)
    {
        this._expHavingCondition = expHavingCondition;
    } //-- void setExpHavingCondition(org.riverock.schema.sql.ExpressionType) 

    /**
     * Method setGroupColumnNames
     * 
     * @param index
     * @param vGroupColumnNames
     */
    public void setGroupColumnNames(int index, org.riverock.schema.sql.SqlNameType vGroupColumnNames)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _groupColumnNamesList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _groupColumnNamesList.setElementAt(vGroupColumnNames, index);
    } //-- void setGroupColumnNames(int, org.riverock.schema.sql.SqlNameType) 

    /**
     * Method setGroupColumnNames
     * 
     * @param groupColumnNamesArray
     */
    public void setGroupColumnNames(org.riverock.schema.sql.SqlNameType[] groupColumnNamesArray)
    {
        //-- copy array
        _groupColumnNamesList.removeAllElements();
        for (int i = 0; i < groupColumnNamesArray.length; i++) {
            _groupColumnNamesList.addElement(groupColumnNamesArray[i]);
        }
    } //-- void setGroupColumnNames(org.riverock.schema.sql.SqlNameType) 

    /**
     * Method setGroupColumnNamesSets the value of
     * 'groupColumnNames' by copying the given Vector.
     * 
     * @param groupColumnNamesVector the Vector to copy.
     */
    public void setGroupColumnNames(java.util.Vector groupColumnNamesVector)
    {
        //-- copy vector
        _groupColumnNamesList.removeAllElements();
        for (int i = 0; i < groupColumnNamesVector.size(); i++) {
            _groupColumnNamesList.addElement((org.riverock.schema.sql.SqlNameType)groupColumnNamesVector.elementAt(i));
        }
    } //-- void setGroupColumnNames(java.util.Vector) 

    /**
     * Method setGroupColumnNamesAsReferenceSets the value of
     * 'groupColumnNames' by setting it to the given Vector. No
     * type checking is performed.
     * 
     * @param groupColumnNamesVector the Vector to copy.
     */
    public void setGroupColumnNamesAsReference(java.util.Vector groupColumnNamesVector)
    {
        _groupColumnNamesList = groupColumnNamesVector;
    } //-- void setGroupColumnNamesAsReference(java.util.Vector) 

    /**
     * Method setGroupLengthSets the value of field 'groupLength'.
     * 
     * @param groupLength the value of field 'groupLength'.
     */
    public void setGroupLength(int groupLength)
    {
        this._groupLength = groupLength;
        this._has_groupLength = true;
    } //-- void setGroupLength(int) 

    /**
     * Method setHavingIndexSets the value of field 'havingIndex'.
     * 
     * @param havingIndex the value of field 'havingIndex'.
     */
    public void setHavingIndex(int havingIndex)
    {
        this._havingIndex = havingIndex;
        this._has_havingIndex = true;
    } //-- void setHavingIndex(int) 

    /**
     * Method setIntoTableNameSets the value of field
     * 'intoTableName'.
     * 
     * @param intoTableName the value of field 'intoTableName'.
     */
    public void setIntoTableName(org.riverock.schema.sql.SqlNameType intoTableName)
    {
        this._intoTableName = intoTableName;
    } //-- void setIntoTableName(org.riverock.schema.sql.SqlNameType) 

    /**
     * Method setIsAggregatedSets the value of field
     * 'isAggregated'.
     * 
     * @param isAggregated the value of field 'isAggregated'.
     */
    public void setIsAggregated(boolean isAggregated)
    {
        this._isAggregated = isAggregated;
        this._has_isAggregated = true;
    } //-- void setIsAggregated(boolean) 

    /**
     * Method setIsDistinctSelectSets the value of field
     * 'isDistinctSelect'.
     * 
     * @param isDistinctSelect the value of field 'isDistinctSelect'
     */
    public void setIsDistinctSelect(boolean isDistinctSelect)
    {
        this._isDistinctSelect = isDistinctSelect;
        this._has_isDistinctSelect = true;
    } //-- void setIsDistinctSelect(boolean) 

    /**
     * Method setIsGroupedSets the value of field 'isGrouped'.
     * 
     * @param isGrouped the value of field 'isGrouped'.
     */
    public void setIsGrouped(boolean isGrouped)
    {
        this._isGrouped = isGrouped;
        this._has_isGrouped = true;
    } //-- void setIsGrouped(boolean) 

    /**
     * Method setIsIntoTableQuotedSets the value of field
     * 'isIntoTableQuoted'.
     * 
     * @param isIntoTableQuoted the value of field
     * 'isIntoTableQuoted'.
     */
    public void setIsIntoTableQuoted(boolean isIntoTableQuoted)
    {
        this._isIntoTableQuoted = isIntoTableQuoted;
        this._has_isIntoTableQuoted = true;
    } //-- void setIsIntoTableQuoted(boolean) 

    /**
     * Method setIsPreProcessSets the value of field
     * 'isPreProcess'.
     * 
     * @param isPreProcess the value of field 'isPreProcess'.
     */
    public void setIsPreProcess(boolean isPreProcess)
    {
        this._isPreProcess = isPreProcess;
        this._has_isPreProcess = true;
    } //-- void setIsPreProcess(boolean) 

    /**
     * Method setLimitCountSets the value of field 'limitCount'.
     * 
     * @param limitCount the value of field 'limitCount'.
     */
    public void setLimitCount(int limitCount)
    {
        this._limitCount = limitCount;
        this._has_limitCount = true;
    } //-- void setLimitCount(int) 

    /**
     * Method setLimitStartSets the value of field 'limitStart'.
     * 
     * @param limitStart the value of field 'limitStart'.
     */
    public void setLimitStart(int limitStart)
    {
        this._limitStart = limitStart;
        this._has_limitStart = true;
    } //-- void setLimitStart(int) 

    /**
     * Method setOrderLengthSets the value of field 'orderLength'.
     * 
     * @param orderLength the value of field 'orderLength'.
     */
    public void setOrderLength(int orderLength)
    {
        this._orderLength = orderLength;
        this._has_orderLength = true;
    } //-- void setOrderLength(int) 

    /**
     * Method setResultLengthSets the value of field
     * 'resultLength'.
     * 
     * @param resultLength the value of field 'resultLength'.
     */
    public void setResultLength(int resultLength)
    {
        this._resultLength = resultLength;
        this._has_resultLength = true;
    } //-- void setResultLength(int) 

    /**
     * Method setTableFilter
     * 
     * @param index
     * @param vTableFilter
     */
    public void setTableFilter(int index, org.riverock.schema.sql.TableFilterType vTableFilter)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _tableFilterList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _tableFilterList.setElementAt(vTableFilter, index);
    } //-- void setTableFilter(int, org.riverock.schema.sql.TableFilterType) 

    /**
     * Method setTableFilter
     * 
     * @param tableFilterArray
     */
    public void setTableFilter(org.riverock.schema.sql.TableFilterType[] tableFilterArray)
    {
        //-- copy array
        _tableFilterList.removeAllElements();
        for (int i = 0; i < tableFilterArray.length; i++) {
            _tableFilterList.addElement(tableFilterArray[i]);
        }
    } //-- void setTableFilter(org.riverock.schema.sql.TableFilterType) 

    /**
     * Method setTableFilterSets the value of 'tableFilter' by
     * copying the given Vector.
     * 
     * @param tableFilterVector the Vector to copy.
     */
    public void setTableFilter(java.util.Vector tableFilterVector)
    {
        //-- copy vector
        _tableFilterList.removeAllElements();
        for (int i = 0; i < tableFilterVector.size(); i++) {
            _tableFilterList.addElement((org.riverock.schema.sql.TableFilterType)tableFilterVector.elementAt(i));
        }
    } //-- void setTableFilter(java.util.Vector) 

    /**
     * Method setTableFilterAsReferenceSets the value of
     * 'tableFilter' by setting it to the given Vector. No type
     * checking is performed.
     * 
     * @param tableFilterVector the Vector to copy.
     */
    public void setTableFilterAsReference(java.util.Vector tableFilterVector)
    {
        _tableFilterList = tableFilterVector;
    } //-- void setTableFilterAsReference(java.util.Vector) 

    /**
     * Method setUnionSets the value of field 'union'.
     * 
     * @param union the value of field 'union'.
     */
    public void setUnion(org.riverock.schema.sql.SelectType union)
    {
        this._union = union;
    } //-- void setUnion(org.riverock.schema.sql.SelectType) 

    /**
     * Method setUnionTypeSets the value of field 'unionType'.
     * 
     * @param unionType the value of field 'unionType'.
     */
    public void setUnionType(org.riverock.schema.sql.types.UnionTypeType unionType)
    {
        this._unionType = unionType;
    } //-- void setUnionType(org.riverock.schema.sql.types.UnionTypeType) 

}
