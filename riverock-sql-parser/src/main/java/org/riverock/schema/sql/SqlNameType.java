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
 * Class SqlNameType.
 * 
 * @version $Revision$ $Date$
 */
public class SqlNameType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _isNameQuoted
     */
    private boolean _isNameQuoted = false;

    /**
     * keeps track of state for field: _isNameQuoted
     */
    private boolean _has_isNameQuoted;

    /**
     * Field _originName
     */
    private java.lang.String _originName;

    /**
     * Field _statementName
     */
    private java.lang.String _statementName;


      //----------------/
     //- Constructors -/
    //----------------/

    public SqlNameType() {
        super();
    } //-- org.riverock.schema.sql.SqlNameType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method deleteIsNameQuoted
     */
    public void deleteIsNameQuoted()
    {
        this._has_isNameQuoted= false;
    } //-- void deleteIsNameQuoted() 

    /**
     * Method getIsNameQuotedReturns the value of field
     * 'isNameQuoted'.
     * 
     * @return the value of field 'isNameQuoted'.
     */
    public boolean getIsNameQuoted()
    {
        return this._isNameQuoted;
    } //-- boolean getIsNameQuoted() 

    /**
     * Method getOriginNameReturns the value of field 'originName'.
     * 
     * @return the value of field 'originName'.
     */
    public java.lang.String getOriginName()
    {
        return this._originName;
    } //-- java.lang.String getOriginName() 

    /**
     * Method getStatementNameReturns the value of field
     * 'statementName'.
     * 
     * @return the value of field 'statementName'.
     */
    public java.lang.String getStatementName()
    {
        return this._statementName;
    } //-- java.lang.String getStatementName() 

    /**
     * Method hasIsNameQuoted
     */
    public boolean hasIsNameQuoted()
    {
        return this._has_isNameQuoted;
    } //-- boolean hasIsNameQuoted() 

    /**
     * Method setIsNameQuotedSets the value of field
     * 'isNameQuoted'.
     * 
     * @param isNameQuoted the value of field 'isNameQuoted'.
     */
    public void setIsNameQuoted(boolean isNameQuoted)
    {
        this._isNameQuoted = isNameQuoted;
        this._has_isNameQuoted = true;
    } //-- void setIsNameQuoted(boolean) 

    /**
     * Method setOriginNameSets the value of field 'originName'.
     * 
     * @param originName the value of field 'originName'.
     */
    public void setOriginName(java.lang.String originName)
    {
        this._originName = originName;
    } //-- void setOriginName(java.lang.String) 

    /**
     * Method setStatementNameSets the value of field
     * 'statementName'.
     * 
     * @param statementName the value of field 'statementName'.
     */
    public void setStatementName(java.lang.String statementName)
    {
        this._statementName = statementName;
    } //-- void setStatementName(java.lang.String) 

}
