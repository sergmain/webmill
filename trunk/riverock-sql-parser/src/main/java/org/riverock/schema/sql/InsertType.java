/*
 * org.riverock.sql - Classes for tracking database changes
 *
 * Copyright (C) 2006, Riverock Software, All Rights Reserved.
 *
 * Riverock - The Open-source Java Development Community
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
