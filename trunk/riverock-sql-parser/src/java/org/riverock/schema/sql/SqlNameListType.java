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

import java.util.Enumeration;

import java.util.Vector;



/**

 * Class SqlNameListType.

 * 

 * @version $Revision$ $Date$

 */

public class SqlNameListType implements java.io.Serializable {





      //--------------------------/

     //- Class/Member Variables -/

    //--------------------------/



    /**

     * Field _itemList

     */

    private java.util.Vector _itemList;





      //----------------/

     //- Constructors -/

    //----------------/



    public SqlNameListType() {

        super();

        _itemList = new Vector();

    } //-- org.riverock.schema.sql.SqlNameListType()





      //-----------/

     //- Methods -/

    //-----------/



    /**

     * Method addItem

     * 

     * @param vItem

     */

    public void addItem(org.riverock.schema.sql.SqlNameType vItem)

        throws java.lang.IndexOutOfBoundsException

    {

        _itemList.addElement(vItem);

    } //-- void addItem(org.riverock.schema.sql.SqlNameType) 



    /**

     * Method addItem

     * 

     * @param index

     * @param vItem

     */

    public void addItem(int index, org.riverock.schema.sql.SqlNameType vItem)

        throws java.lang.IndexOutOfBoundsException

    {

        _itemList.insertElementAt(vItem, index);

    } //-- void addItem(int, org.riverock.schema.sql.SqlNameType) 



    /**

     * Method enumerateItem

     */

    public java.util.Enumeration enumerateItem()

    {

        return _itemList.elements();

    } //-- java.util.Enumeration enumerateItem() 



    /**

     * Method getItem

     * 

     * @param index

     */

    public org.riverock.schema.sql.SqlNameType getItem(int index)

        throws java.lang.IndexOutOfBoundsException

    {

        //-- check bounds for index

        if ((index < 0) || (index > _itemList.size())) {

            throw new IndexOutOfBoundsException();

        }

        

        return (org.riverock.schema.sql.SqlNameType) _itemList.elementAt(index);

    } //-- org.riverock.schema.sql.SqlNameType getItem(int) 



    /**

     * Method getItem

     */

    public org.riverock.schema.sql.SqlNameType[] getItem()

    {

        int size = _itemList.size();

        org.riverock.schema.sql.SqlNameType[] mArray = new org.riverock.schema.sql.SqlNameType[size];

        for (int index = 0; index < size; index++) {

            mArray[index] = (org.riverock.schema.sql.SqlNameType) _itemList.elementAt(index);

        }

        return mArray;

    } //-- org.riverock.schema.sql.SqlNameType[] getItem() 



    /**

     * Method getItemAsReferenceReturns a reference to 'item'. No

     * type checking is performed on any modications to the Vector.

     * 

     * @return returns a reference to the Vector.

     */

    public java.util.Vector getItemAsReference()

    {

        return _itemList;

    } //-- java.util.Vector getItemAsReference() 



    /**

     * Method getItemCount

     */

    public int getItemCount()

    {

        return _itemList.size();

    } //-- int getItemCount() 



    /**

     * Method removeAllItem

     */

    public void removeAllItem()

    {

        _itemList.removeAllElements();

    } //-- void removeAllItem() 



    /**

     * Method removeItem

     * 

     * @param index

     */

    public org.riverock.schema.sql.SqlNameType removeItem(int index)

    {

        java.lang.Object obj = _itemList.elementAt(index);

        _itemList.removeElementAt(index);

        return (org.riverock.schema.sql.SqlNameType) obj;

    } //-- org.riverock.schema.sql.SqlNameType removeItem(int) 



    /**

     * Method setItem

     * 

     * @param index

     * @param vItem

     */

    public void setItem(int index, org.riverock.schema.sql.SqlNameType vItem)

        throws java.lang.IndexOutOfBoundsException

    {

        //-- check bounds for index

        if ((index < 0) || (index > _itemList.size())) {

            throw new IndexOutOfBoundsException();

        }

        _itemList.setElementAt(vItem, index);

    } //-- void setItem(int, org.riverock.schema.sql.SqlNameType) 



    /**

     * Method setItem

     * 

     * @param itemArray

     */

    public void setItem(org.riverock.schema.sql.SqlNameType[] itemArray)

    {

        //-- copy array

        _itemList.removeAllElements();

        for (int i = 0; i < itemArray.length; i++) {

            _itemList.addElement(itemArray[i]);

        }

    } //-- void setItem(org.riverock.schema.sql.SqlNameType) 



    /**

     * Method setItemSets the value of 'item' by copying the given

     * Vector.

     * 

     * @param itemVector the Vector to copy.

     */

    public void setItem(java.util.Vector itemVector)

    {

        //-- copy vector

        _itemList.removeAllElements();

        for (int i = 0; i < itemVector.size(); i++) {

            _itemList.addElement((org.riverock.schema.sql.SqlNameType)itemVector.elementAt(i));

        }

    } //-- void setItem(java.util.Vector) 



    /**

     * Method setItemAsReferenceSets the value of 'item' by setting

     * it to the given Vector. No type checking is performed.

     * 

     * @param itemVector the Vector to copy.

     */

    public void setItemAsReference(java.util.Vector itemVector)

    {

        _itemList = itemVector;

    } //-- void setItemAsReference(java.util.Vector) 



}

