/*
 * org.riverock.forum - Forum portlet
 *
 * Copyright (C) 2006, Riverock Software, All Rights Reserved.
 *
 * Riverock - The Open-source Java Development Community
 * http://www.riverock.org
 *
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */

/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.9.1</a>, using an XML
 * Schema.
 * $Id$
 */

package org.riverock.forum.dao.bean;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.util.ArrayList;

/**
 * Class WmForumTopicListType.
 * 
 * @version $Revision$ $Date$
 */
public class WmForumTopicListType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _wmForumTopicList
     */
    private java.util.ArrayList _wmForumTopicList;


      //----------------/
     //- Constructors -/
    //----------------/

    public WmForumTopicListType() 
     {
        super();
        _wmForumTopicList = new ArrayList();
    } //-- org.riverock.forum.schema.core.WmForumTopicListType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addWmForumTopic
     * 
     * 
     * 
     * @param vWmForumTopic
     */
    public void addWmForumTopic(WmForumTopicItemType vWmForumTopic)
        throws java.lang.IndexOutOfBoundsException
    {
        _wmForumTopicList.add(vWmForumTopic);
    } //-- void addWmForumTopic(org.riverock.forum.schema.core.WmForumTopicItemType) 

    /**
     * Method addWmForumTopic
     * 
     * 
     * 
     * @param index
     * @param vWmForumTopic
     */
    public void addWmForumTopic(int index, WmForumTopicItemType vWmForumTopic)
        throws java.lang.IndexOutOfBoundsException
    {
        _wmForumTopicList.add(index, vWmForumTopic);
    } //-- void addWmForumTopic(int, org.riverock.forum.schema.core.WmForumTopicItemType) 

    /**
     * Method clearWmForumTopic
     * 
     */
    public void clearWmForumTopic()
    {
        _wmForumTopicList.clear();
    } //-- void clearWmForumTopic() 

    /**
     * Method getWmForumTopic
     * 
     * 
     * 
     * @param index
     * @return WmForumTopicItemType
     */
    public WmForumTopicItemType getWmForumTopic(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _wmForumTopicList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (WmForumTopicItemType) _wmForumTopicList.get(index);
    } //-- org.riverock.forum.schema.core.WmForumTopicItemType getWmForumTopic(int) 

    /**
     * Method getWmForumTopic
     * 
     * 
     * 
     * @return WmForumTopicItemType
     */
    public WmForumTopicItemType[] getWmForumTopic()
    {
        int size = _wmForumTopicList.size();
        WmForumTopicItemType[] mArray = new WmForumTopicItemType[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (WmForumTopicItemType) _wmForumTopicList.get(index);
        }
        return mArray;
    } //-- org.riverock.forum.schema.core.WmForumTopicItemType[] getWmForumTopic() 

    /**
     * Method getWmForumTopicCount
     * 
     * 
     * 
     * @return int
     */
    public int getWmForumTopicCount()
    {
        return _wmForumTopicList.size();
    } //-- int getWmForumTopicCount() 

    /**
     * Method removeWmForumTopic
     * 
     * 
     * 
     * @param vWmForumTopic
     * @return boolean
     */
    public boolean removeWmForumTopic(WmForumTopicItemType vWmForumTopic)
    {
        boolean removed = _wmForumTopicList.remove(vWmForumTopic);
        return removed;
    } //-- boolean removeWmForumTopic(org.riverock.forum.schema.core.WmForumTopicItemType) 

    /**
     * Method setWmForumTopic
     * 
     * 
     * 
     * @param index
     * @param vWmForumTopic
     */
    public void setWmForumTopic(int index, WmForumTopicItemType vWmForumTopic)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _wmForumTopicList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _wmForumTopicList.set(index, vWmForumTopic);
    } //-- void setWmForumTopic(int, org.riverock.forum.schema.core.WmForumTopicItemType) 

    /**
     * Method setWmForumTopic
     * 
     * 
     * 
     * @param wmForumTopicArray
     */
    public void setWmForumTopic(WmForumTopicItemType[] wmForumTopicArray)
    {
        //-- copy array
        _wmForumTopicList.clear();
        for (int i = 0; i < wmForumTopicArray.length; i++) {
            _wmForumTopicList.add(wmForumTopicArray[i]);
        }
    } //-- void setWmForumTopic(org.riverock.forum.schema.core.WmForumTopicItemType) 

}
