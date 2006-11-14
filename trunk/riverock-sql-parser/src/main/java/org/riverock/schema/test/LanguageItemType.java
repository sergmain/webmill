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
