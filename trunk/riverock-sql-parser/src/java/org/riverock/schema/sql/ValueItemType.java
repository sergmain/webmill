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

