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
 * Class TableFilterType.
 * 
 * @version $Revision$ $Date$
 */
public class TableFilterType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _table
     */
    private org.riverock.schema.sql.TableType _table;

    /**
     * Field _alias
     */
    private java.lang.String _alias;

    /**
     * Field _expStart
     */
    private org.riverock.schema.sql.ExpressionType _expStart;

    /**
     * Field _expEnd
     */
    private org.riverock.schema.sql.ExpressionType _expEnd;

    /**
     * Field _expAnd
     */
    private org.riverock.schema.sql.ExpressionType _expAnd;

    /**
     * Field _isOuterJoin
     */
    private boolean _isOuterJoin;

    /**
     * keeps track of state for field: _isOuterJoin
     */
    private boolean _has_isOuterJoin;


      //----------------/
     //- Constructors -/
    //----------------/

    public TableFilterType() {
        super();
    } //-- org.riverock.schema.sql.TableFilterType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method deleteIsOuterJoin
     */
    public void deleteIsOuterJoin()
    {
        this._has_isOuterJoin= false;
    } //-- void deleteIsOuterJoin() 

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
     * Method getExpAndReturns the value of field 'expAnd'.
     * 
     * @return the value of field 'expAnd'.
     */
    public org.riverock.schema.sql.ExpressionType getExpAnd()
    {
        return this._expAnd;
    } //-- org.riverock.schema.sql.ExpressionType getExpAnd() 

    /**
     * Method getExpEndReturns the value of field 'expEnd'.
     * 
     * @return the value of field 'expEnd'.
     */
    public org.riverock.schema.sql.ExpressionType getExpEnd()
    {
        return this._expEnd;
    } //-- org.riverock.schema.sql.ExpressionType getExpEnd() 

    /**
     * Method getExpStartReturns the value of field 'expStart'.
     * 
     * @return the value of field 'expStart'.
     */
    public org.riverock.schema.sql.ExpressionType getExpStart()
    {
        return this._expStart;
    } //-- org.riverock.schema.sql.ExpressionType getExpStart() 

    /**
     * Method getIsOuterJoinReturns the value of field
     * 'isOuterJoin'.
     * 
     * @return the value of field 'isOuterJoin'.
     */
    public boolean getIsOuterJoin()
    {
        return this._isOuterJoin;
    } //-- boolean getIsOuterJoin() 

    /**
     * Method getTableReturns the value of field 'table'.
     * 
     * @return the value of field 'table'.
     */
    public org.riverock.schema.sql.TableType getTable()
    {
        return this._table;
    } //-- org.riverock.schema.sql.TableType getTable() 

    /**
     * Method hasIsOuterJoin
     */
    public boolean hasIsOuterJoin()
    {
        return this._has_isOuterJoin;
    } //-- boolean hasIsOuterJoin() 

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
     * Method setExpAndSets the value of field 'expAnd'.
     * 
     * @param expAnd the value of field 'expAnd'.
     */
    public void setExpAnd(org.riverock.schema.sql.ExpressionType expAnd)
    {
        this._expAnd = expAnd;
    } //-- void setExpAnd(org.riverock.schema.sql.ExpressionType) 

    /**
     * Method setExpEndSets the value of field 'expEnd'.
     * 
     * @param expEnd the value of field 'expEnd'.
     */
    public void setExpEnd(org.riverock.schema.sql.ExpressionType expEnd)
    {
        this._expEnd = expEnd;
    } //-- void setExpEnd(org.riverock.schema.sql.ExpressionType) 

    /**
     * Method setExpStartSets the value of field 'expStart'.
     * 
     * @param expStart the value of field 'expStart'.
     */
    public void setExpStart(org.riverock.schema.sql.ExpressionType expStart)
    {
        this._expStart = expStart;
    } //-- void setExpStart(org.riverock.schema.sql.ExpressionType) 

    /**
     * Method setIsOuterJoinSets the value of field 'isOuterJoin'.
     * 
     * @param isOuterJoin the value of field 'isOuterJoin'.
     */
    public void setIsOuterJoin(boolean isOuterJoin)
    {
        this._isOuterJoin = isOuterJoin;
        this._has_isOuterJoin = true;
    } //-- void setIsOuterJoin(boolean) 

    /**
     * Method setTableSets the value of field 'table'.
     * 
     * @param table the value of field 'table'.
     */
    public void setTable(org.riverock.schema.sql.TableType table)
    {
        this._table = table;
    } //-- void setTable(org.riverock.schema.sql.TableType) 

}
