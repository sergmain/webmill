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
 * Class ValueItemType.
 * 
 * @version $Revision$ $Date$
 */
public class ValueItemType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _item
     */
    private java.lang.String _item;


      //----------------/
     //- Constructors -/
    //----------------/

    public ValueItemType() {
        super();
    } //-- org.riverock.schema.sql.ValueItemType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method getItemReturns the value of field 'item'.
     * 
     * @return the value of field 'item'.
     */
    public java.lang.String getItem()
    {
        return this._item;
    } //-- java.lang.String getItem() 

    /**
     * Method setItemSets the value of field 'item'.
     * 
     * @param item the value of field 'item'.
     */
    public void setItem(java.lang.String item)
    {
        this._item = item;
    } //-- void setItem(java.lang.String) 

}
