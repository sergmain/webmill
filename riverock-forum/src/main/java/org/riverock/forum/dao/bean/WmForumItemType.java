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
 * Class WmForumItemType.
 * 
 * @version $Revision$ $Date$
 */
public class WmForumItemType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _forumId
     */
    private java.lang.Long _forumId;

    /**
     * Field _siteId
     */
    private java.lang.Long _siteId;

    /**
     * Field _isUseLocale
     */
    private java.lang.Boolean _isUseLocale = new java.lang.Boolean("false");

    /**
     * Field _isDeleted
     */
    private java.lang.Boolean _isDeleted = new java.lang.Boolean("false");

    /**
     * Field _forumName
     */
    private java.lang.String _forumName;


      //----------------/
     //- Constructors -/
    //----------------/

    public WmForumItemType() 
     {
        super();
    } //-- org.riverock.forum.schema.core.WmForumItemType()


      //-----------/
     //- Methods -/
    //-----------/

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
     * Returns the value of field 'forumName'.
     * 
     * @return String
     * @return the value of field 'forumName'.
     */
    public java.lang.String getForumName()
    {
        return this._forumName;
    } //-- java.lang.String getForumName() 

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
     * Returns the value of field 'siteId'.
     * 
     * @return Long
     * @return the value of field 'siteId'.
     */
    public java.lang.Long getSiteId()
    {
        return this._siteId;
    } //-- java.lang.Long getSiteId() 

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
     * Sets the value of field 'forumName'.
     * 
     * @param forumName the value of field 'forumName'.
     */
    public void setForumName(java.lang.String forumName)
    {
        this._forumName = forumName;
    } //-- void setForumName(java.lang.String) 

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

    /**
     * Sets the value of field 'siteId'.
     * 
     * @param siteId the value of field 'siteId'.
     */
    public void setSiteId(java.lang.Long siteId)
    {
        this._siteId = siteId;
    } //-- void setSiteId(java.lang.Long) 

}
