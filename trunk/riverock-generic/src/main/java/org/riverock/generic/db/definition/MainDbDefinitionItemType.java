/*
 * org.riverock.generic - Database connectivity classes, part of Webmill portal
 * For more information about Webmill portal, please visit project site
 * http://webmill.askmore.info
 *
 * Copyright (C) 2000-2006, Riverock Software, All Rights Reserved.
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
package org.riverock.generic.db.definition;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/


/*
 * This class was automatically generated with
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id$
 */
/**
 * Class MainDbDefinitionItemType.
 * 
 * @version $Revision$ $Date$
 */
public class MainDbDefinitionItemType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _idDbDefinition
     */
    private long _idDbDefinition;

    /**
     * keeps track of state for field: _idDbDefinition
     */
    private boolean _has_idDbDefinition;

    /**
     * Field _nameDefinition
     */
    private java.lang.String _nameDefinition;

    /**
     * Field _aplayDate
     */
    private java.util.Date _aplayDate;


      //----------------/
     //- Constructors -/
    //----------------/

    public MainDbDefinitionItemType() {
        super();
    } //-- org.riverock.schema.core.MainDbDefinitionItemType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method deleteIdDbDefinition
     */
    public void deleteIdDbDefinition()
    {
        this._has_idDbDefinition= false;
    } //-- void deleteIdDbDefinition() 

    /**
     * Method getAplayDateReturns the value of field 'aplayDate'.
     * 
     * @return the value of field 'aplayDate'.
     */
    public java.util.Date getAplayDate()
    {
        return this._aplayDate;
    } //-- java.util.Date getAplayDate() 

    /**
     * Method getIdDbDefinitionReturns the value of field
     * 'idDbDefinition'.
     * 
     * @return the value of field 'idDbDefinition'.
     */
    public long getIdDbDefinition()
    {
        return this._idDbDefinition;
    } //-- long getIdDbDefinition() 

    /**
     * Method getNameDefinitionReturns the value of field
     * 'nameDefinition'.
     * 
     * @return the value of field 'nameDefinition'.
     */
    public java.lang.String getNameDefinition()
    {
        return this._nameDefinition;
    } //-- java.lang.String getNameDefinition() 

    /**
     * Method hasIdDbDefinition
     */
    public boolean hasIdDbDefinition()
    {
        return this._has_idDbDefinition;
    } //-- boolean hasIdDbDefinition() 

    /**
     * Method setAplayDateSets the value of field 'aplayDate'.
     * 
     * @param aplayDate the value of field 'aplayDate'.
     */
    public void setAplayDate(java.util.Date aplayDate)
    {
        this._aplayDate = aplayDate;
    } //-- void setAplayDate(java.util.Date) 

    /**
     * Method setIdDbDefinitionSets the value of field
     * 'idDbDefinition'.
     * 
     * @param idDbDefinition the value of field 'idDbDefinition'.
     */
    public void setIdDbDefinition(long idDbDefinition)
    {
        this._idDbDefinition = idDbDefinition;
        this._has_idDbDefinition = true;
    } //-- void setIdDbDefinition(long) 

    /**
     * Method setNameDefinitionSets the value of field
     * 'nameDefinition'.
     * 
     * @param nameDefinition the value of field 'nameDefinition'.
     */
    public void setNameDefinition(java.lang.String nameDefinition)
    {
        this._nameDefinition = nameDefinition;
    } //-- void setNameDefinition(java.lang.String) 

}
