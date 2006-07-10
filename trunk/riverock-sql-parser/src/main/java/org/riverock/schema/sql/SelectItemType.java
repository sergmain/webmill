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

import org.riverock.schema.sql.types.TypeFieldType;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;

/**
 * Class SelectItemType.
 * 
 * @version $Revision$ $Date$
 */
public class SelectItemType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _column
     */
    private java.lang.String _column;

    /**
     * Field _alias
     */
    private java.lang.String _alias;

    /**
     * Field _typeField
     */
    private org.riverock.schema.sql.types.TypeFieldType _typeField;

    /**
     * Field _subQuery
     */
    private org.riverock.schema.sql.SelectSqlType _subQuery;


      //----------------/
     //- Constructors -/
    //----------------/

    public SelectItemType() {
        super();
    } //-- org.riverock.schema.sql.SelectItemType()


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
     * Method getColumnReturns the value of field 'column'.
     * 
     * @return the value of field 'column'.
     */
    public java.lang.String getColumn()
    {
        return this._column;
    } //-- java.lang.String getColumn() 

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
     * Method getTypeFieldReturns the value of field 'typeField'.
     * 
     * @return the value of field 'typeField'.
     */
    public org.riverock.schema.sql.types.TypeFieldType getTypeField()
    {
        return this._typeField;
    } //-- org.riverock.schema.sql.types.TypeFieldType getTypeField() 

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
     * Method setColumnSets the value of field 'column'.
     * 
     * @param column the value of field 'column'.
     */
    public void setColumn(java.lang.String column)
    {
        this._column = column;
    } //-- void setColumn(java.lang.String) 

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
     * Method setTypeFieldSets the value of field 'typeField'.
     * 
     * @param typeField the value of field 'typeField'.
     */
    public void setTypeField(org.riverock.schema.sql.types.TypeFieldType typeField)
    {
        this._typeField = typeField;
    } //-- void setTypeField(org.riverock.schema.sql.types.TypeFieldType) 

}
