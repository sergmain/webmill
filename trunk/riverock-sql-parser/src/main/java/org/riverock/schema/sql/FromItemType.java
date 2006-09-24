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
