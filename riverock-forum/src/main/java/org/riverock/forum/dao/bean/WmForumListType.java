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
 * Class WmForumListType.
 * 
 * @version $Revision$ $Date$
 */
public class WmForumListType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _wmForumList
     */
    private java.util.ArrayList _wmForumList;


      //----------------/
     //- Constructors -/
    //----------------/

    public WmForumListType() 
     {
        super();
        _wmForumList = new ArrayList();
    } //-- org.riverock.forum.schema.core.WmForumListType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addWmForum
     * 
     * 
     * 
     * @param vWmForum
     */
    public void addWmForum(WmForumItemType vWmForum)
        throws java.lang.IndexOutOfBoundsException
    {
        _wmForumList.add(vWmForum);
    } //-- void addWmForum(org.riverock.forum.schema.core.WmForumItemType) 

    /**
     * Method addWmForum
     * 
     * 
     * 
     * @param index
     * @param vWmForum
     */
    public void addWmForum(int index, WmForumItemType vWmForum)
        throws java.lang.IndexOutOfBoundsException
    {
        _wmForumList.add(index, vWmForum);
    } //-- void addWmForum(int, org.riverock.forum.schema.core.WmForumItemType) 

    /**
     * Method clearWmForum
     * 
     */
    public void clearWmForum()
    {
        _wmForumList.clear();
    } //-- void clearWmForum() 

    /**
     * Method getWmForum
     * 
     * 
     * 
     * @param index
     * @return WmForumItemType
     */
    public WmForumItemType getWmForum(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _wmForumList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (WmForumItemType) _wmForumList.get(index);
    } //-- org.riverock.forum.schema.core.WmForumItemType getWmForum(int) 

    /**
     * Method getWmForum
     * 
     * 
     * 
     * @return WmForumItemType
     */
    public WmForumItemType[] getWmForum()
    {
        int size = _wmForumList.size();
        WmForumItemType[] mArray = new WmForumItemType[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (WmForumItemType) _wmForumList.get(index);
        }
        return mArray;
    } //-- org.riverock.forum.schema.core.WmForumItemType[] getWmForum() 

    /**
     * Method getWmForumCount
     * 
     * 
     * 
     * @return int
     */
    public int getWmForumCount()
    {
        return _wmForumList.size();
    } //-- int getWmForumCount() 

    /**
     * Method removeWmForum
     * 
     * 
     * 
     * @param vWmForum
     * @return boolean
     */
    public boolean removeWmForum(WmForumItemType vWmForum)
    {
        boolean removed = _wmForumList.remove(vWmForum);
        return removed;
    } //-- boolean removeWmForum(org.riverock.forum.schema.core.WmForumItemType) 

    /**
     * Method setWmForum
     * 
     * 
     * 
     * @param index
     * @param vWmForum
     */
    public void setWmForum(int index, WmForumItemType vWmForum)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _wmForumList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _wmForumList.set(index, vWmForum);
    } //-- void setWmForum(int, org.riverock.forum.schema.core.WmForumItemType) 

    /**
     * Method setWmForum
     * 
     * 
     * 
     * @param wmForumArray
     */
    public void setWmForum(WmForumItemType[] wmForumArray)
    {
        //-- copy array
        _wmForumList.clear();
        for (int i = 0; i < wmForumArray.length; i++) {
            _wmForumList.add(wmForumArray[i]);
        }
    } //-- void setWmForum(org.riverock.forum.schema.core.WmForumItemType) 

}
