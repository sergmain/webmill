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

/**
 * Class WmForumCategoryItemType.
 * 
 * @version $Revision$ $Date$
 */
public class WmForumCategoryItemType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _forumCategoryId
     */
    private java.lang.Long _forumCategoryId;

    /**
     * Field _forumId
     */
    private java.lang.Long _forumId;

    /**
     * Field _isUseLocale
     */
    private java.lang.Boolean _isUseLocale = new java.lang.Boolean("false");

    /**
     * Field _isDeleted
     */
    private java.lang.Boolean _isDeleted = new java.lang.Boolean("false");

    /**
     * Field _forumCategoryName
     */
    private java.lang.String _forumCategoryName;


      //----------------/
     //- Constructors -/
    //----------------/

    public WmForumCategoryItemType() 
     {
        super();
    } //-- org.riverock.forum.schema.core.WmForumCategoryItemType()


      //-----------/
     //- Methods -/
    //-----------/

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
     * Returns the value of field 'forumCategoryName'.
     * 
     * @return String
     * @return the value of field 'forumCategoryName'.
     */
    public java.lang.String getForumCategoryName()
    {
        return this._forumCategoryName;
    } //-- java.lang.String getForumCategoryName() 

    /**
     * Returns the value of field 'forumId'.
     * 
     * @return Long
     * @return the value of field 'forumId'.
     */
    public java.lang.Long getForumId()
    {
        return this._forumId;
    } //-- java.lang.Long getForumId() 

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
     * Returns the value of field 'isUseLocale'.
     * 
     * @return Boolean
     * @return the value of field 'isUseLocale'.
     */
    public java.lang.Boolean getIsUseLocale()
    {
        return this._isUseLocale;
    } //-- java.lang.Boolean getIsUseLocale() 

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
     * Sets the value of field 'forumCategoryName'.
     * 
     * @param forumCategoryName the value of field
     * 'forumCategoryName'.
     */
    public void setForumCategoryName(java.lang.String forumCategoryName)
    {
        this._forumCategoryName = forumCategoryName;
    } //-- void setForumCategoryName(java.lang.String) 

    /**
     * Sets the value of field 'forumId'.
     * 
     * @param forumId the value of field 'forumId'.
     */
    public void setForumId(java.lang.Long forumId)
    {
        this._forumId = forumId;
    } //-- void setForumId(java.lang.Long) 

    /**
     * Sets the value of field 'isDeleted'.
     * 
     * @param isDeleted the value of field 'isDeleted'.
     */
    public void setIsDeleted(java.lang.Boolean isDeleted)
    {
        this._isDeleted = isDeleted;
    } //-- void setIsDeleted(java.lang.Boolean) 

    /**
     * Sets the value of field 'isUseLocale'.
     * 
     * @param isUseLocale the value of field 'isUseLocale'.
     */
    public void setIsUseLocale(java.lang.Boolean isUseLocale)
    {
        this._isUseLocale = isUseLocale;
    } //-- void setIsUseLocale(java.lang.Boolean) 

}
