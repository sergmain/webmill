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

import java.util.Vector;

/*
 * This class was automatically generated with
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id$
 */
/**
 * Class MainDbDefinitionListType.
 * 
 * @version $Revision$ $Date$
 */
public class MainDbDefinitionListType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _mainDbDefinitionList
     */
    private java.util.Vector _mainDbDefinitionList;


      //----------------/
     //- Constructors -/
    //----------------/

    public MainDbDefinitionListType() {
        super();
        _mainDbDefinitionList = new Vector();
    } //-- MainDbDefinitionListType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addMainDbDefinition
     * 
     * @param index
     * @param vMainDbDefinition
     */
    public void addMainDbDefinition(int index, MainDbDefinitionItemType vMainDbDefinition)
        throws java.lang.IndexOutOfBoundsException
    {
        _mainDbDefinitionList.insertElementAt(vMainDbDefinition, index);
    } //-- void addMainDbDefinition(int, MainDbDefinitionItemType)

    /**
     * Method addMainDbDefinition
     *
     * @param vMainDbDefinition
     */
    public void addMainDbDefinition(MainDbDefinitionItemType vMainDbDefinition)
        throws java.lang.IndexOutOfBoundsException
    {
        _mainDbDefinitionList.add(vMainDbDefinition);
    } //-- void addMainDbDefinition(int, MainDbDefinitionItemType)

    /**
     * Method enumerateMainDbDefinition
     */
    public java.util.Enumeration enumerateMainDbDefinition()
    {
        return _mainDbDefinitionList.elements();
    } //-- java.util.Enumeration enumerateMainDbDefinition() 

    /**
     * Method getMainDbDefinition
     * 
     * @param index
     */
    public MainDbDefinitionItemType getMainDbDefinition(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _mainDbDefinitionList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (MainDbDefinitionItemType) _mainDbDefinitionList.elementAt(index);
    } //-- MainDbDefinitionItemType getMainDbDefinition(int)

    /**
     * Method getMainDbDefinition
     */
    public MainDbDefinitionItemType[] getMainDbDefinition()
    {
        int size = _mainDbDefinitionList.size();
        MainDbDefinitionItemType[] mArray = new MainDbDefinitionItemType[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (MainDbDefinitionItemType) _mainDbDefinitionList.elementAt(index);
        }
        return mArray;
    } //-- MainDbDefinitionItemType[] getMainDbDefinition()

    /**
     * Method getMainDbDefinitionAsReferenceReturns a reference to
     * 'mainDbDefinition'. No type checking is performed on any
     * modications to the Vector.
     * 
     * @return returns a reference to the Vector.
     */
    public java.util.Vector getMainDbDefinitionAsReference()
    {
        return _mainDbDefinitionList;
    } //-- java.util.Vector getMainDbDefinitionAsReference() 

    /**
     * Method getMainDbDefinitionCount
     */
    public int getMainDbDefinitionCount()
    {
        return _mainDbDefinitionList.size();
    } //-- int getMainDbDefinitionCount() 

    /**
     * Method removeAllMainDbDefinition
     */
    public void removeAllMainDbDefinition()
    {
        _mainDbDefinitionList.removeAllElements();
    } //-- void removeAllMainDbDefinition() 

    /**
     * Method removeMainDbDefinition
     * 
     * @param index
     */
    public MainDbDefinitionItemType removeMainDbDefinition(int index)
    {
        java.lang.Object obj = _mainDbDefinitionList.elementAt(index);
        _mainDbDefinitionList.removeElementAt(index);
        return (MainDbDefinitionItemType) obj;
    } //-- MainDbDefinitionItemType removeMainDbDefinition(int)

    /**
     * Method setMainDbDefinition
     * 
     * @param index
     * @param vMainDbDefinition
     */
    public void setMainDbDefinition(int index, MainDbDefinitionItemType vMainDbDefinition)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _mainDbDefinitionList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _mainDbDefinitionList.setElementAt(vMainDbDefinition, index);
    } //-- void setMainDbDefinition(int, MainDbDefinitionItemType)

    /**
     * Method setMainDbDefinition
     * 
     * @param mainDbDefinitionArray
     */
    public void setMainDbDefinition(MainDbDefinitionItemType[] mainDbDefinitionArray)
    {
        //-- copy array
        _mainDbDefinitionList.removeAllElements();
        for (int i = 0; i < mainDbDefinitionArray.length; i++) {
            _mainDbDefinitionList.addElement(mainDbDefinitionArray[i]);
        }
    } //-- void setMainDbDefinition(MainDbDefinitionItemType)

    /**
     * Method setMainDbDefinitionSets the value of
     * 'mainDbDefinition' by copying the given Vector.
     * 
     * @param mainDbDefinitionVector the Vector to copy.
     */
    public void setMainDbDefinition(java.util.Vector mainDbDefinitionVector)
    {
        //-- copy vector
        _mainDbDefinitionList.removeAllElements();
        for (int i = 0; i < mainDbDefinitionVector.size(); i++) {
            _mainDbDefinitionList.addElement(mainDbDefinitionVector.elementAt(i));
        }
    } //-- void setMainDbDefinition(java.util.Vector) 

    /**
     * Method setMainDbDefinitionAsReferenceSets the value of
     * 'mainDbDefinition' by setting it to the given Vector. No
     * type checking is performed.
     * 
     * @param mainDbDefinitionVector the Vector to copy.
     */
    public void setMainDbDefinitionAsReference(java.util.Vector mainDbDefinitionVector)
    {
        _mainDbDefinitionList = mainDbDefinitionVector;
    } //-- void setMainDbDefinitionAsReference(java.util.Vector) 

}
