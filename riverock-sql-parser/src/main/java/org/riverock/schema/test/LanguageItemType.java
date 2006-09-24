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

package org.riverock.schema.test;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;

/**
 * Class LanguageItemType.
 * 
 * @version $Revision$ $Date$
 */
public class LanguageItemType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _nameLanguage
     */
    private java.lang.String _nameLanguage;

    /**
     * Field _string
     */
    private java.lang.String _string;


      //----------------/
     //- Constructors -/
    //----------------/

    public LanguageItemType() {
        super();
    } //-- org.riverock.schema.test.LanguageItemType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method getNameLanguageReturns the value of field
     * 'nameLanguage'.
     * 
     * @return the value of field 'nameLanguage'.
     */
    public java.lang.String getNameLanguage()
    {
        return this._nameLanguage;
    } //-- java.lang.String getNameLanguage() 

    /**
     * Method getStringReturns the value of field 'string'.
     * 
     * @return the value of field 'string'.
     */
    public java.lang.String getString()
    {
        return this._string;
    } //-- java.lang.String getString() 

    /**
     * Method setNameLanguageSets the value of field
     * 'nameLanguage'.
     * 
     * @param nameLanguage the value of field 'nameLanguage'.
     */
    public void setNameLanguage(java.lang.String nameLanguage)
    {
        this._nameLanguage = nameLanguage;
    } //-- void setNameLanguage(java.lang.String) 

    /**
     * Method setStringSets the value of field 'string'.
     * 
     * @param string the value of field 'string'.
     */
    public void setString(java.lang.String string)
    {
        this._string = string;
    } //-- void setString(java.lang.String) 

}
