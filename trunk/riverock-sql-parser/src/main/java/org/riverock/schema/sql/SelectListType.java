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
import java.util.Enumeration;
import java.util.Vector;

/**
 * Class SelectListType.
 * 
 * @version $Revision$ $Date$
 */
public class SelectListType implements java.io.Serializable {


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

    public SelectListType() {
        super();
        _itemList = new Vector();
    } //-- org.riverock.schema.sql.SelectListType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addItem
     * 
     * @param vItem
     */
    public void addItem(org.riverock.schema.sql.SelectItemType vItem)
        throws java.lang.IndexOutOfBoundsException
    {
        _itemList.addElement(vItem);
    } //-- void addItem(org.riverock.schema.sql.SelectItemType) 

    /**
     * Method addItem
     * 
     * @param index
     * @param vItem
     */
    public void addItem(int index, org.riverock.schema.sql.SelectItemType vItem)
        throws java.lang.IndexOutOfBoundsException
    {
        _itemList.insertElementAt(vItem, index);
    } //-- void addItem(int, org.riverock.schema.sql.SelectItemType) 

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
    public org.riverock.schema.sql.SelectItemType getItem(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _itemList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (org.riverock.schema.sql.SelectItemType) _itemList.elementAt(index);
    } //-- org.riverock.schema.sql.SelectItemType getItem(int) 

    /**
     * Method getItem
     */
    public org.riverock.schema.sql.SelectItemType[] getItem()
    {
        int size = _itemList.size();
        org.riverock.schema.sql.SelectItemType[] mArray = new org.riverock.schema.sql.SelectItemType[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (org.riverock.schema.sql.SelectItemType) _itemList.elementAt(index);
        }
        return mArray;
    } //-- org.riverock.schema.sql.SelectItemType[] getItem() 

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
    public org.riverock.schema.sql.SelectItemType removeItem(int index)
    {
        java.lang.Object obj = _itemList.elementAt(index);
        _itemList.removeElementAt(index);
        return (org.riverock.schema.sql.SelectItemType) obj;
    } //-- org.riverock.schema.sql.SelectItemType removeItem(int) 

    /**
     * Method setItem
     * 
     * @param index
     * @param vItem
     */
    public void setItem(int index, org.riverock.schema.sql.SelectItemType vItem)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _itemList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _itemList.setElementAt(vItem, index);
    } //-- void setItem(int, org.riverock.schema.sql.SelectItemType) 

    /**
     * Method setItem
     * 
     * @param itemArray
     */
    public void setItem(org.riverock.schema.sql.SelectItemType[] itemArray)
    {
        //-- copy array
        _itemList.removeAllElements();
        for (int i = 0; i < itemArray.length; i++) {
            _itemList.addElement(itemArray[i]);
        }
    } //-- void setItem(org.riverock.schema.sql.SelectItemType) 

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
            _itemList.addElement((org.riverock.schema.sql.SelectItemType)itemVector.elementAt(i));
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
