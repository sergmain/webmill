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
import java.util.Enumeration;
import java.util.Vector;

/**
 * Class LanguageListType.
 * 
 * @version $Revision$ $Date$
 */
public class LanguageListType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _dataList
     */
    private java.util.Vector _dataList;


      //----------------/
     //- Constructors -/
    //----------------/

    public LanguageListType() {
        super();
        _dataList = new Vector();
    } //-- org.riverock.schema.test.LanguageListType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addData
     * 
     * @param vData
     */
    public void addData(org.riverock.schema.test.LanguageItemType vData)
        throws java.lang.IndexOutOfBoundsException
    {
        _dataList.addElement(vData);
    } //-- void addData(org.riverock.schema.test.LanguageItemType) 

    /**
     * Method addData
     * 
     * @param index
     * @param vData
     */
    public void addData(int index, org.riverock.schema.test.LanguageItemType vData)
        throws java.lang.IndexOutOfBoundsException
    {
        _dataList.insertElementAt(vData, index);
    } //-- void addData(int, org.riverock.schema.test.LanguageItemType) 

    /**
     * Method enumerateData
     */
    public java.util.Enumeration enumerateData()
    {
        return _dataList.elements();
    } //-- java.util.Enumeration enumerateData() 

    /**
     * Method getData
     * 
     * @param index
     */
    public org.riverock.schema.test.LanguageItemType getData(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _dataList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (org.riverock.schema.test.LanguageItemType) _dataList.elementAt(index);
    } //-- org.riverock.schema.test.LanguageItemType getData(int) 

    /**
     * Method getData
     */
    public org.riverock.schema.test.LanguageItemType[] getData()
    {
        int size = _dataList.size();
        org.riverock.schema.test.LanguageItemType[] mArray = new org.riverock.schema.test.LanguageItemType[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (org.riverock.schema.test.LanguageItemType) _dataList.elementAt(index);
        }
        return mArray;
    } //-- org.riverock.schema.test.LanguageItemType[] getData() 

    /**
     * Method getDataAsReferenceReturns a reference to 'data'. No
     * type checking is performed on any modications to the Vector.
     * 
     * @return returns a reference to the Vector.
     */
    public java.util.Vector getDataAsReference()
    {
        return _dataList;
    } //-- java.util.Vector getDataAsReference() 

    /**
     * Method getDataCount
     */
    public int getDataCount()
    {
        return _dataList.size();
    } //-- int getDataCount() 

    /**
     * Method removeAllData
     */
    public void removeAllData()
    {
        _dataList.removeAllElements();
    } //-- void removeAllData() 

    /**
     * Method removeData
     * 
     * @param index
     */
    public org.riverock.schema.test.LanguageItemType removeData(int index)
    {
        java.lang.Object obj = _dataList.elementAt(index);
        _dataList.removeElementAt(index);
        return (org.riverock.schema.test.LanguageItemType) obj;
    } //-- org.riverock.schema.test.LanguageItemType removeData(int) 

    /**
     * Method setData
     * 
     * @param index
     * @param vData
     */
    public void setData(int index, org.riverock.schema.test.LanguageItemType vData)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _dataList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _dataList.setElementAt(vData, index);
    } //-- void setData(int, org.riverock.schema.test.LanguageItemType) 

    /**
     * Method setData
     * 
     * @param dataArray
     */
    public void setData(org.riverock.schema.test.LanguageItemType[] dataArray)
    {
        //-- copy array
        _dataList.removeAllElements();
        for (int i = 0; i < dataArray.length; i++) {
            _dataList.addElement(dataArray[i]);
        }
    } //-- void setData(org.riverock.schema.test.LanguageItemType) 

    /**
     * Method setDataSets the value of 'data' by copying the given
     * Vector.
     * 
     * @param dataVector the Vector to copy.
     */
    public void setData(java.util.Vector dataVector)
    {
        //-- copy vector
        _dataList.removeAllElements();
        for (int i = 0; i < dataVector.size(); i++) {
            _dataList.addElement((org.riverock.schema.test.LanguageItemType)dataVector.elementAt(i));
        }
    } //-- void setData(java.util.Vector) 

    /**
     * Method setDataAsReferenceSets the value of 'data' by setting
     * it to the given Vector. No type checking is performed.
     * 
     * @param dataVector the Vector to copy.
     */
    public void setDataAsReference(java.util.Vector dataVector)
    {
        _dataList = dataVector;
    } //-- void setDataAsReference(java.util.Vector) 

}
