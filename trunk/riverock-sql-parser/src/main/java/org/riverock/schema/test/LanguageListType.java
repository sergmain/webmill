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
