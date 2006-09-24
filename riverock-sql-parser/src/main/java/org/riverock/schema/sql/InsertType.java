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
 * Class InsertType.
 * 
 * @version $Revision$ $Date$
 */
public class InsertType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _table
     */
    private org.riverock.schema.sql.TableType _table;

    /**
     * Field _select
     */
    private org.riverock.schema.sql.SelectType _select;


      //----------------/
     //- Constructors -/
    //----------------/

    public InsertType() {
        super();
    } //-- org.riverock.schema.sql.InsertType()


      //-----------/
     //- Methods -/
    //-----------/

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
    public org.riverock.schema.sql.TableType getTable()
    {
        return this._table;
    } //-- org.riverock.schema.sql.TableType getTable() 

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
    public void setTable(org.riverock.schema.sql.TableType table)
    {
        this._table = table;
    } //-- void setTable(org.riverock.schema.sql.TableType) 

}
