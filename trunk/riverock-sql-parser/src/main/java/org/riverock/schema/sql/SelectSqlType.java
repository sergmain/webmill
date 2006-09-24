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
 * Class SelectSqlType.
 * 
 * @version $Revision$ $Date$
 */
public class SelectSqlType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _isDistinct
     */
    private boolean _isDistinct = false;

    /**
     * keeps track of state for field: _isDistinct
     */
    private boolean _has_isDistinct;

    /**
     * Field _text
     */
    private java.lang.String _text;

    /**
     * Field _select
     */
    private org.riverock.schema.sql.SelectListType _select;

    /**
     * Field _limit
     */
    private org.riverock.schema.sql.LimitType _limit;

    /**
     * Field _from
     */
    private org.riverock.schema.sql.FromListType _from;

    /**
     * Field _where
     */
    private org.riverock.schema.sql.WhereType _where;

    /**
     * Field _having
     */
    private org.riverock.schema.sql.HavingType _having;

    /**
     * Field _group
     */
    private org.riverock.schema.sql.GroupType _group;

    /**
     * Field _order
     */
    private org.riverock.schema.sql.OrderType _order;


      //----------------/
     //- Constructors -/
    //----------------/

    public SelectSqlType() {
        super();
    } //-- org.riverock.schema.sql.SelectSqlType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method deleteIsDistinct
     */
    public void deleteIsDistinct()
    {
        this._has_isDistinct= false;
    } //-- void deleteIsDistinct() 

    /**
     * Method getFromReturns the value of field 'from'.
     * 
     * @return the value of field 'from'.
     */
    public org.riverock.schema.sql.FromListType getFrom()
    {
        return this._from;
    } //-- org.riverock.schema.sql.FromListType getFrom() 

    /**
     * Method getGroupReturns the value of field 'group'.
     * 
     * @return the value of field 'group'.
     */
    public org.riverock.schema.sql.GroupType getGroup()
    {
        return this._group;
    } //-- org.riverock.schema.sql.GroupType getGroup() 

    /**
     * Method getHavingReturns the value of field 'having'.
     * 
     * @return the value of field 'having'.
     */
    public org.riverock.schema.sql.HavingType getHaving()
    {
        return this._having;
    } //-- org.riverock.schema.sql.HavingType getHaving() 

    /**
     * Method getIsDistinctReturns the value of field 'isDistinct'.
     * 
     * @return the value of field 'isDistinct'.
     */
    public boolean getIsDistinct()
    {
        return this._isDistinct;
    } //-- boolean getIsDistinct() 

    /**
     * Method getLimitReturns the value of field 'limit'.
     * 
     * @return the value of field 'limit'.
     */
    public org.riverock.schema.sql.LimitType getLimit()
    {
        return this._limit;
    } //-- org.riverock.schema.sql.LimitType getLimit() 

    /**
     * Method getOrderReturns the value of field 'order'.
     * 
     * @return the value of field 'order'.
     */
    public org.riverock.schema.sql.OrderType getOrder()
    {
        return this._order;
    } //-- org.riverock.schema.sql.OrderType getOrder() 

    /**
     * Method getSelectReturns the value of field 'select'.
     * 
     * @return the value of field 'select'.
     */
    public org.riverock.schema.sql.SelectListType getSelect()
    {
        return this._select;
    } //-- org.riverock.schema.sql.SelectListType getSelect() 

    /**
     * Method getTextReturns the value of field 'text'.
     * 
     * @return the value of field 'text'.
     */
    public java.lang.String getText()
    {
        return this._text;
    } //-- java.lang.String getText() 

    /**
     * Method getWhereReturns the value of field 'where'.
     * 
     * @return the value of field 'where'.
     */
    public org.riverock.schema.sql.WhereType getWhere()
    {
        return this._where;
    } //-- org.riverock.schema.sql.WhereType getWhere() 

    /**
     * Method hasIsDistinct
     */
    public boolean hasIsDistinct()
    {
        return this._has_isDistinct;
    } //-- boolean hasIsDistinct() 

    /**
     * Method setFromSets the value of field 'from'.
     * 
     * @param from the value of field 'from'.
     */
    public void setFrom(org.riverock.schema.sql.FromListType from)
    {
        this._from = from;
    } //-- void setFrom(org.riverock.schema.sql.FromListType) 

    /**
     * Method setGroupSets the value of field 'group'.
     * 
     * @param group the value of field 'group'.
     */
    public void setGroup(org.riverock.schema.sql.GroupType group)
    {
        this._group = group;
    } //-- void setGroup(org.riverock.schema.sql.GroupType) 

    /**
     * Method setHavingSets the value of field 'having'.
     * 
     * @param having the value of field 'having'.
     */
    public void setHaving(org.riverock.schema.sql.HavingType having)
    {
        this._having = having;
    } //-- void setHaving(org.riverock.schema.sql.HavingType) 

    /**
     * Method setIsDistinctSets the value of field 'isDistinct'.
     * 
     * @param isDistinct the value of field 'isDistinct'.
     */
    public void setIsDistinct(boolean isDistinct)
    {
        this._isDistinct = isDistinct;
        this._has_isDistinct = true;
    } //-- void setIsDistinct(boolean) 

    /**
     * Method setLimitSets the value of field 'limit'.
     * 
     * @param limit the value of field 'limit'.
     */
    public void setLimit(org.riverock.schema.sql.LimitType limit)
    {
        this._limit = limit;
    } //-- void setLimit(org.riverock.schema.sql.LimitType) 

    /**
     * Method setOrderSets the value of field 'order'.
     * 
     * @param order the value of field 'order'.
     */
    public void setOrder(org.riverock.schema.sql.OrderType order)
    {
        this._order = order;
    } //-- void setOrder(org.riverock.schema.sql.OrderType) 

    /**
     * Method setSelectSets the value of field 'select'.
     * 
     * @param select the value of field 'select'.
     */
    public void setSelect(org.riverock.schema.sql.SelectListType select)
    {
        this._select = select;
    } //-- void setSelect(org.riverock.schema.sql.SelectListType) 

    /**
     * Method setTextSets the value of field 'text'.
     * 
     * @param text the value of field 'text'.
     */
    public void setText(java.lang.String text)
    {
        this._text = text;
    } //-- void setText(java.lang.String) 

    /**
     * Method setWhereSets the value of field 'where'.
     * 
     * @param where the value of field 'where'.
     */
    public void setWhere(org.riverock.schema.sql.WhereType where)
    {
        this._where = where;
    } //-- void setWhere(org.riverock.schema.sql.WhereType) 

}
