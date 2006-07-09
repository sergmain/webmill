/*
 * org.riverock.generic -- Database connectivity classes
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
