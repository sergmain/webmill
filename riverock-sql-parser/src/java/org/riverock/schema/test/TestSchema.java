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



package org.riverock.schema.test;



  //---------------------------------/

 //- Imported classes and packages -/

//---------------------------------/



import java.io.Reader;

import java.io.Serializable;

import java.io.Writer;



/**

 * Class TestSchema.

 * 

 * @version $Revision$ $Date$

 */

public class TestSchema implements java.io.Serializable {





      //--------------------------/

     //- Class/Member Variables -/

    //--------------------------/



    /**

     * Field _name

     */

    private java.lang.String _name;



    /**

     * Field _bytes

     */

    private byte[] _bytes;





      //----------------/

     //- Constructors -/

    //----------------/



    public TestSchema() {

        super();

    } //-- org.riverock.schema.test.TestSchema()





      //-----------/

     //- Methods -/

    //-----------/



    /**

     * Method getBytesReturns the value of field 'bytes'.

     * 

     * @return the value of field 'bytes'.

     */

    public byte[] getBytes()

    {

        return this._bytes;

    } //-- byte[] getBytes() 



    /**

     * Method getNameReturns the value of field 'name'.

     * 

     * @return the value of field 'name'.

     */

    public java.lang.String getName()

    {

        return this._name;

    } //-- java.lang.String getName() 



    /**

     * Method setBytesSets the value of field 'bytes'.

     * 

     * @param bytes the value of field 'bytes'.

     */

    public void setBytes(byte[] bytes)

    {

        this._bytes = bytes;

    } //-- void setBytes(byte) 



    /**

     * Method setNameSets the value of field 'name'.

     * 

     * @param name the value of field 'name'.

     */

    public void setName(java.lang.String name)

    {

        this._name = name;

    } //-- void setName(java.lang.String) 



}

