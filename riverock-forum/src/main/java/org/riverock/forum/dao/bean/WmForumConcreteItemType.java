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

import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import java.util.Date;

/**
 * Class WmForumConcreteItemType.
 * 
 * @version $Revision$ $Date$
 */
public class WmForumConcreteItemType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _fId
     */
    private java.lang.Long _fId;

    /**
     * Field _isDeleted
     */
    private java.lang.Boolean _isDeleted = new java.lang.Boolean("false");

    /**
     * Field _FOrder
     */
    private java.lang.Long _FOrder = new java.lang.Long("0");

    /**
     * Field _FName
     */
    private java.lang.String _FName;

    /**
     * Field _FInfo
     */
    private java.lang.String _FInfo;

    /**
     * Field _FUId
     */
    private java.lang.Long _FUId;

    /**
     * Field _FTopics
     */
    private java.lang.Long _FTopics = new java.lang.Long("0");

    /**
     * Field _FMessages
     */
    private java.lang.Long _FMessages = new java.lang.Long("0");

    /**
     * Field _FUId2
     */
    private java.lang.Long _FUId2;

    /**
     * Field _FLasttime
     */
    private java.util.Date _FLasttime;

    /**
     * Field _forumCategoryId
     */
    private java.lang.Long _forumCategoryId;


      //----------------/
     //- Constructors -/
    //----------------/

    public WmForumConcreteItemType() 
     {
        super();
    } //-- org.riverock.forum.schema.core.WmForumConcreteItemType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'fId'.
     * 
     * @return Long
     * @return the value of field 'fId'.
     */
    public java.lang.Long getFId()
    {
        return this._fId;
    } //-- java.lang.Long getFId() 

    /**
     * Returns the value of field 'FInfo'.
     * 
     * @return String
     * @return the value of field 'FInfo'.
     */
    public java.lang.String getFInfo()
    {
        return this._FInfo;
    } //-- java.lang.String getFInfo() 

    /**
     * Returns the value of field 'FLasttime'.
     * 
     * @return Date
     * @return the value of field 'FLasttime'.
     */
    public java.util.Date getFLasttime()
    {
        return this._FLasttime;
    } //-- java.util.Date getFLasttime() 

    /**
     * Returns the value of field 'FMessages'.
     * 
     * @return Long
     * @return the value of field 'FMessages'.
     */
    public java.lang.Long getFMessages()
    {
        return this._FMessages;
    } //-- java.lang.Long getFMessages() 

    /**
     * Returns the value of field 'FName'.
     * 
     * @return String
     * @return the value of field 'FName'.
     */
    public java.lang.String getFName()
    {
        return this._FName;
    } //-- java.lang.String getFName() 

    /**
     * Returns the value of field 'FOrder'.
     * 
     * @return Long
     * @return the value of field 'FOrder'.
     */
    public java.lang.Long getFOrder()
    {
        return this._FOrder;
    } //-- java.lang.Long getFOrder() 

    /**
     * Returns the value of field 'FTopics'.
     * 
     * @return Long
     * @return the value of field 'FTopics'.
     */
    public java.lang.Long getFTopics()
    {
        return this._FTopics;
    } //-- java.lang.Long getFTopics() 

    /**
     * Returns the value of field 'FUId'.
     * 
     * @return Long
     * @return the value of field 'FUId'.
     */
    public java.lang.Long getFUId()
    {
        return this._FUId;
    } //-- java.lang.Long getFUId() 

    /**
     * Returns the value of field 'FUId2'.
     * 
     * @return Long
     * @return the value of field 'FUId2'.
     */
    public java.lang.Long getFUId2()
    {
        return this._FUId2;
    } //-- java.lang.Long getFUId2() 

    /**
     * Returns the value of field 'forumCategoryId'.
     * 
     * @return Long
     * @return the value of field 'forumCategoryId'.
     */
    public java.lang.Long getForumCategoryId()
    {
        return this._forumCategoryId;
    } //-- java.lang.Long getForumCategoryId() 

    /**
     * Returns the value of field 'isDeleted'.
     * 
     * @return Boolean
     * @return the value of field 'isDeleted'.
     */
    public java.lang.Boolean getIsDeleted()
    {
        return this._isDeleted;
    } //-- java.lang.Boolean getIsDeleted() 

    /**
     * Sets the value of field 'fId'.
     * 
     * @param fId the value of field 'fId'.
     */
    public void setFId(java.lang.Long fId)
    {
        this._fId = fId;
    } //-- void setFId(java.lang.Long) 

    /**
     * Sets the value of field 'FInfo'.
     * 
     * @param FInfo the value of field 'FInfo'.
     */
    public void setFInfo(java.lang.String FInfo)
    {
        this._FInfo = FInfo;
    } //-- void setFInfo(java.lang.String) 

    /**
     * Sets the value of field 'FLasttime'.
     * 
     * @param FLasttime the value of field 'FLasttime'.
     */
    public void setFLasttime(java.util.Date FLasttime)
    {
        this._FLasttime = FLasttime;
    } //-- void setFLasttime(java.util.Date) 

    /**
     * Sets the value of field 'FMessages'.
     * 
     * @param FMessages the value of field 'FMessages'.
     */
    public void setFMessages(java.lang.Long FMessages)
    {
        this._FMessages = FMessages;
    } //-- void setFMessages(java.lang.Long) 

    /**
     * Sets the value of field 'FName'.
     * 
     * @param FName the value of field 'FName'.
     */
    public void setFName(java.lang.String FName)
    {
        this._FName = FName;
    } //-- void setFName(java.lang.String) 

    /**
     * Sets the value of field 'FOrder'.
     * 
     * @param FOrder the value of field 'FOrder'.
     */
    public void setFOrder(java.lang.Long FOrder)
    {
        this._FOrder = FOrder;
    } //-- void setFOrder(java.lang.Long) 

    /**
     * Sets the value of field 'FTopics'.
     * 
     * @param FTopics the value of field 'FTopics'.
     */
    public void setFTopics(java.lang.Long FTopics)
    {
        this._FTopics = FTopics;
    } //-- void setFTopics(java.lang.Long) 

    /**
     * Sets the value of field 'FUId'.
     * 
     * @param FUId the value of field 'FUId'.
     */
    public void setFUId(java.lang.Long FUId)
    {
        this._FUId = FUId;
    } //-- void setFUId(java.lang.Long) 

    /**
     * Sets the value of field 'FUId2'.
     * 
     * @param FUId2 the value of field 'FUId2'.
     */
    public void setFUId2(java.lang.Long FUId2)
    {
        this._FUId2 = FUId2;
    } //-- void setFUId2(java.lang.Long) 

    /**
     * Sets the value of field 'forumCategoryId'.
     * 
     * @param forumCategoryId the value of field 'forumCategoryId'.
     */
    public void setForumCategoryId(java.lang.Long forumCategoryId)
    {
        this._forumCategoryId = forumCategoryId;
    } //-- void setForumCategoryId(java.lang.Long) 

    /**
     * Sets the value of field 'isDeleted'.
     * 
     * @param isDeleted the value of field 'isDeleted'.
     */
    public void setIsDeleted(java.lang.Boolean isDeleted)
    {
        this._isDeleted = isDeleted;
    } //-- void setIsDeleted(java.lang.Boolean) 

}
