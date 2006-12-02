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
 * Class WmForumMessageItemType.
 * 
 * @version $Revision$ $Date$
 */
public class WmForumMessageItemType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _mId
     */
    private java.lang.Long _mId;

    /**
     * Field _MIconid
     */
    private java.lang.Long _MIconid;

    /**
     * Field _MTId
     */
    private java.lang.Long _MTId;

    /**
     * Field _MContent
     */
    private java.lang.String _MContent;

    /**
     * Field _MUId
     */
    private java.lang.Long _MUId;

    /**
     * Field _MTime
     */
    private java.util.Date _MTime;


      //----------------/
     //- Constructors -/
    //----------------/

    public WmForumMessageItemType() 
     {
        super();
    } //-- org.riverock.forum.schema.core.WmForumMessageItemType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'MContent'.
     * 
     * @return String
     * @return the value of field 'MContent'.
     */
    public java.lang.String getMContent()
    {
        return this._MContent;
    } //-- java.lang.String getMContent() 

    /**
     * Returns the value of field 'MIconid'.
     * 
     * @return Long
     * @return the value of field 'MIconid'.
     */
    public java.lang.Long getMIconid()
    {
        return this._MIconid;
    } //-- java.lang.Long getMIconid() 

    /**
     * Returns the value of field 'mId'.
     * 
     * @return Long
     * @return the value of field 'mId'.
     */
    public java.lang.Long getMId()
    {
        return this._mId;
    } //-- java.lang.Long getMId() 

    /**
     * Returns the value of field 'MTId'.
     * 
     * @return Long
     * @return the value of field 'MTId'.
     */
    public java.lang.Long getMTId()
    {
        return this._MTId;
    } //-- java.lang.Long getMTId() 

    /**
     * Returns the value of field 'MTime'.
     * 
     * @return Date
     * @return the value of field 'MTime'.
     */
    public java.util.Date getMTime()
    {
        return this._MTime;
    } //-- java.util.Date getMTime() 

    /**
     * Returns the value of field 'MUId'.
     * 
     * @return Long
     * @return the value of field 'MUId'.
     */
    public java.lang.Long getMUId()
    {
        return this._MUId;
    } //-- java.lang.Long getMUId() 

    /**
     * Sets the value of field 'MContent'.
     * 
     * @param MContent the value of field 'MContent'.
     */
    public void setMContent(java.lang.String MContent)
    {
        this._MContent = MContent;
    } //-- void setMContent(java.lang.String) 

    /**
     * Sets the value of field 'MIconid'.
     * 
     * @param MIconid the value of field 'MIconid'.
     */
    public void setMIconid(java.lang.Long MIconid)
    {
        this._MIconid = MIconid;
    } //-- void setMIconid(java.lang.Long) 

    /**
     * Sets the value of field 'mId'.
     * 
     * @param mId the value of field 'mId'.
     */
    public void setMId(java.lang.Long mId)
    {
        this._mId = mId;
    } //-- void setMId(java.lang.Long) 

    /**
     * Sets the value of field 'MTId'.
     * 
     * @param MTId the value of field 'MTId'.
     */
    public void setMTId(java.lang.Long MTId)
    {
        this._MTId = MTId;
    } //-- void setMTId(java.lang.Long) 

    /**
     * Sets the value of field 'MTime'.
     * 
     * @param MTime the value of field 'MTime'.
     */
    public void setMTime(java.util.Date MTime)
    {
        this._MTime = MTime;
    } //-- void setMTime(java.util.Date) 

    /**
     * Sets the value of field 'MUId'.
     * 
     * @param MUId the value of field 'MUId'.
     */
    public void setMUId(java.lang.Long MUId)
    {
        this._MUId = MUId;
    } //-- void setMUId(java.lang.Long) 

}
