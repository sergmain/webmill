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
 * Class WmForumUserItemType.
 * 
 * @version $Revision$ $Date$
 */
public class WmForumUserItemType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _uId
     */
    private java.lang.Long _uId;

    /**
     * Field _UAvatarId
     */
    private java.lang.Long _UAvatarId;

    /**
     * Field _USign
     */
    private java.lang.String _USign;

    /**
     * Field _UPost
     */
    private java.lang.Long _UPost;

    /**
     * Field _ULasttime
     */
    private java.util.Date _ULasttime;

    /**
     * Field _ULastip
     */
    private java.lang.String _ULastip;


      //----------------/
     //- Constructors -/
    //----------------/

    public WmForumUserItemType() 
     {
        super();
    } //-- org.riverock.forum.schema.core.WmForumUserItemType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'UAvatarId'.
     * 
     * @return Long
     * @return the value of field 'UAvatarId'.
     */
    public java.lang.Long getUAvatarId()
    {
        return this._UAvatarId;
    } //-- java.lang.Long getUAvatarId() 

    /**
     * Returns the value of field 'uId'.
     * 
     * @return Long
     * @return the value of field 'uId'.
     */
    public java.lang.Long getUId()
    {
        return this._uId;
    } //-- java.lang.Long getUId() 

    /**
     * Returns the value of field 'ULastip'.
     * 
     * @return String
     * @return the value of field 'ULastip'.
     */
    public java.lang.String getULastip()
    {
        return this._ULastip;
    } //-- java.lang.String getULastip() 

    /**
     * Returns the value of field 'ULasttime'.
     * 
     * @return Date
     * @return the value of field 'ULasttime'.
     */
    public java.util.Date getULasttime()
    {
        return this._ULasttime;
    } //-- java.util.Date getULasttime() 

    /**
     * Returns the value of field 'UPost'.
     * 
     * @return Long
     * @return the value of field 'UPost'.
     */
    public java.lang.Long getUPost()
    {
        return this._UPost;
    } //-- java.lang.Long getUPost() 

    /**
     * Returns the value of field 'USign'.
     * 
     * @return String
     * @return the value of field 'USign'.
     */
    public java.lang.String getUSign()
    {
        return this._USign;
    } //-- java.lang.String getUSign() 

    /**
     * Sets the value of field 'UAvatarId'.
     * 
     * @param UAvatarId the value of field 'UAvatarId'.
     */
    public void setUAvatarId(java.lang.Long UAvatarId)
    {
        this._UAvatarId = UAvatarId;
    } //-- void setUAvatarId(java.lang.Long) 

    /**
     * Sets the value of field 'uId'.
     * 
     * @param uId the value of field 'uId'.
     */
    public void setUId(java.lang.Long uId)
    {
        this._uId = uId;
    } //-- void setUId(java.lang.Long) 

    /**
     * Sets the value of field 'ULastip'.
     * 
     * @param ULastip the value of field 'ULastip'.
     */
    public void setULastip(java.lang.String ULastip)
    {
        this._ULastip = ULastip;
    } //-- void setULastip(java.lang.String) 

    /**
     * Sets the value of field 'ULasttime'.
     * 
     * @param ULasttime the value of field 'ULasttime'.
     */
    public void setULasttime(java.util.Date ULasttime)
    {
        this._ULasttime = ULasttime;
    } //-- void setULasttime(java.util.Date) 

    /**
     * Sets the value of field 'UPost'.
     * 
     * @param UPost the value of field 'UPost'.
     */
    public void setUPost(java.lang.Long UPost)
    {
        this._UPost = UPost;
    } //-- void setUPost(java.lang.Long) 

    /**
     * Sets the value of field 'USign'.
     * 
     * @param USign the value of field 'USign'.
     */
    public void setUSign(java.lang.String USign)
    {
        this._USign = USign;
    } //-- void setUSign(java.lang.String) 

}
