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
 * Class WmForumCategoryListType.
 * 
 * @version $Revision$ $Date$
 */
public class WmForumCategoryListType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _wmForumCategoryList
     */
    private java.util.ArrayList _wmForumCategoryList;


      //----------------/
     //- Constructors -/
    //----------------/

    public WmForumCategoryListType() 
     {
        super();
        _wmForumCategoryList = new ArrayList();
    } //-- org.riverock.forum.schema.core.WmForumCategoryListType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addWmForumCategory
     * 
     * 
     * 
     * @param vWmForumCategory
     */
    public void addWmForumCategory(WmForumCategoryItemType vWmForumCategory)
        throws java.lang.IndexOutOfBoundsException
    {
        _wmForumCategoryList.add(vWmForumCategory);
    } //-- void addWmForumCategory(org.riverock.forum.schema.core.WmForumCategoryItemType) 

    /**
     * Method addWmForumCategory
     * 
     * 
     * 
     * @param index
     * @param vWmForumCategory
     */
    public void addWmForumCategory(int index, WmForumCategoryItemType vWmForumCategory)
        throws java.lang.IndexOutOfBoundsException
    {
        _wmForumCategoryList.add(index, vWmForumCategory);
    } //-- void addWmForumCategory(int, org.riverock.forum.schema.core.WmForumCategoryItemType) 

    /**
     * Method clearWmForumCategory
     * 
     */
    public void clearWmForumCategory()
    {
        _wmForumCategoryList.clear();
    } //-- void clearWmForumCategory() 

    /**
     * Method getWmForumCategory
     * 
     * 
     * 
     * @param index
     * @return WmForumCategoryItemType
     */
    public WmForumCategoryItemType getWmForumCategory(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _wmForumCategoryList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (WmForumCategoryItemType) _wmForumCategoryList.get(index);
    } //-- org.riverock.forum.schema.core.WmForumCategoryItemType getWmForumCategory(int) 

    /**
     * Method getWmForumCategory
     * 
     * 
     * 
     * @return WmForumCategoryItemType
     */
    public WmForumCategoryItemType[] getWmForumCategory()
    {
        int size = _wmForumCategoryList.size();
        WmForumCategoryItemType[] mArray = new WmForumCategoryItemType[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (WmForumCategoryItemType) _wmForumCategoryList.get(index);
        }
        return mArray;
    } //-- org.riverock.forum.schema.core.WmForumCategoryItemType[] getWmForumCategory() 

    /**
     * Method getWmForumCategoryCount
     * 
     * 
     * 
     * @return int
     */
    public int getWmForumCategoryCount()
    {
        return _wmForumCategoryList.size();
    } //-- int getWmForumCategoryCount() 

    /**
     * Method removeWmForumCategory
     * 
     * 
     * 
     * @param vWmForumCategory
     * @return boolean
     */
    public boolean removeWmForumCategory(WmForumCategoryItemType vWmForumCategory)
    {
        boolean removed = _wmForumCategoryList.remove(vWmForumCategory);
        return removed;
    } //-- boolean removeWmForumCategory(org.riverock.forum.schema.core.WmForumCategoryItemType) 

    /**
     * Method setWmForumCategory
     * 
     * 
     * 
     * @param index
     * @param vWmForumCategory
     */
    public void setWmForumCategory(int index, WmForumCategoryItemType vWmForumCategory)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _wmForumCategoryList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _wmForumCategoryList.set(index, vWmForumCategory);
    } //-- void setWmForumCategory(int, org.riverock.forum.schema.core.WmForumCategoryItemType) 

    /**
     * Method setWmForumCategory
     * 
     * 
     * 
     * @param wmForumCategoryArray
     */
    public void setWmForumCategory(WmForumCategoryItemType[] wmForumCategoryArray)
    {
        //-- copy array
        _wmForumCategoryList.clear();
        for (int i = 0; i < wmForumCategoryArray.length; i++) {
            _wmForumCategoryList.add(wmForumCategoryArray[i]);
        }
    } //-- void setWmForumCategory(org.riverock.forum.schema.core.WmForumCategoryItemType) 

}
