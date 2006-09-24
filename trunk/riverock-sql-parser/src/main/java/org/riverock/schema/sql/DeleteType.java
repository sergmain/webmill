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
 * Class DeleteType.
 * 
 * @version $Revision$ $Date$
 */
public class DeleteType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _table
     */
    private org.riverock.schema.sql.TableType _table;

    /**
     * Field _expression
     */
    private org.riverock.schema.sql.ExpressionType _expression;


      //----------------/
     //- Constructors -/
    //----------------/

    public DeleteType() {
        super();
    } //-- org.riverock.schema.sql.DeleteType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method getExpressionReturns the value of field 'expression'.
     * 
     * @return the value of field 'expression'.
     */
    public org.riverock.schema.sql.ExpressionType getExpression()
    {
        return this._expression;
    } //-- org.riverock.schema.sql.ExpressionType getExpression() 

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
     * Method setExpressionSets the value of field 'expression'.
     * 
     * @param expression the value of field 'expression'.
     */
    public void setExpression(org.riverock.schema.sql.ExpressionType expression)
    {
        this._expression = expression;
    } //-- void setExpression(org.riverock.schema.sql.ExpressionType) 

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
