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
 * Class WmForumTopicItemType.
 * 
 * @version $Revision$ $Date$
 */
public class WmForumTopicItemType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _tFId
     */
    private java.lang.Long _tFId;

    /**
     * Field _tId
     */
    private java.lang.Long _tId;

    /**
     * Field _TOrder
     */
    private java.lang.Long _TOrder;

    /**
     * Field _TLocked
     */
    private java.lang.Long _TLocked;

    /**
     * Field _TIconid
     */
    private java.lang.Long _TIconid;

    /**
     * Field _TName
     */
    private java.lang.String _TName;

    /**
     * Field _TUId
     */
    private java.lang.Long _TUId;

    /**
     * Field _TReplies
     */
    private java.lang.Long _TReplies;

    /**
     * Field _TViews
     */
    private java.lang.Long _TViews;

    /**
     * Field _TUId2
     */
    private java.lang.Long _TUId2;

    /**
     * Field _TLasttime
     */
    private java.util.Date _TLasttime;


      //----------------/
     //- Constructors -/
    //----------------/

    public WmForumTopicItemType() 
     {
        super();
    } //-- org.riverock.forum.schema.core.WmForumTopicItemType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'tFId'.
     * 
     * @return Long
     * @return the value of field 'tFId'.
     */
    public java.lang.Long getTFId()
    {
        return this._tFId;
    } //-- java.lang.Long getTFId() 

    /**
     * Returns the value of field 'TIconid'.
     * 
     * @return Long
     * @return the value of field 'TIconid'.
     */
    public java.lang.Long getTIconid()
    {
        return this._TIconid;
    } //-- java.lang.Long getTIconid() 

    /**
     * Returns the value of field 'tId'.
     * 
     * @return Long
     * @return the value of field 'tId'.
     */
    public java.lang.Long getTId()
    {
        return this._tId;
    } //-- java.lang.Long getTId() 

    /**
     * Returns the value of field 'TLasttime'.
     * 
     * @return Date
     * @return the value of field 'TLasttime'.
     */
    public java.util.Date getTLasttime()
    {
        return this._TLasttime;
    } //-- java.util.Date getTLasttime() 

    /**
     * Returns the value of field 'TLocked'.
     * 
     * @return Long
     * @return the value of field 'TLocked'.
     */
    public java.lang.Long getTLocked()
    {
        return this._TLocked;
    } //-- java.lang.Long getTLocked() 

    /**
     * Returns the value of field 'TName'.
     * 
     * @return String
     * @return the value of field 'TName'.
     */
    public java.lang.String getTName()
    {
        return this._TName;
    } //-- java.lang.String getTName() 

    /**
     * Returns the value of field 'TOrder'.
     * 
     * @return Long
     * @return the value of field 'TOrder'.
     */
    public java.lang.Long getTOrder()
    {
        return this._TOrder;
    } //-- java.lang.Long getTOrder() 

    /**
     * Returns the value of field 'TReplies'.
     * 
     * @return Long
     * @return the value of field 'TReplies'.
     */
    public java.lang.Long getTReplies()
    {
        return this._TReplies;
    } //-- java.lang.Long getTReplies() 

    /**
     * Returns the value of field 'TUId'.
     * 
     * @return Long
     * @return the value of field 'TUId'.
     */
    public java.lang.Long getTUId()
    {
        return this._TUId;
    } //-- java.lang.Long getTUId() 

    /**
     * Returns the value of field 'TUId2'.
     * 
     * @return Long
     * @return the value of field 'TUId2'.
     */
    public java.lang.Long getTUId2()
    {
        return this._TUId2;
    } //-- java.lang.Long getTUId2() 

    /**
     * Returns the value of field 'TViews'.
     * 
     * @return Long
     * @return the value of field 'TViews'.
     */
    public java.lang.Long getTViews()
    {
        return this._TViews;
    } //-- java.lang.Long getTViews() 

    /**
     * Sets the value of field 'tFId'.
     * 
     * @param tFId the value of field 'tFId'.
     */
    public void setTFId(java.lang.Long tFId)
    {
        this._tFId = tFId;
    } //-- void setTFId(java.lang.Long) 

    /**
     * Sets the value of field 'TIconid'.
     * 
     * @param TIconid the value of field 'TIconid'.
     */
    public void setTIconid(java.lang.Long TIconid)
    {
        this._TIconid = TIconid;
    } //-- void setTIconid(java.lang.Long) 

    /**
     * Sets the value of field 'tId'.
     * 
     * @param tId the value of field 'tId'.
     */
    public void setTId(java.lang.Long tId)
    {
        this._tId = tId;
    } //-- void setTId(java.lang.Long) 

    /**
     * Sets the value of field 'TLasttime'.
     * 
     * @param TLasttime the value of field 'TLasttime'.
     */
    public void setTLasttime(java.util.Date TLasttime)
    {
        this._TLasttime = TLasttime;
    } //-- void setTLasttime(java.util.Date) 

    /**
     * Sets the value of field 'TLocked'.
     * 
     * @param TLocked the value of field 'TLocked'.
     */
    public void setTLocked(java.lang.Long TLocked)
    {
        this._TLocked = TLocked;
    } //-- void setTLocked(java.lang.Long) 

    /**
     * Sets the value of field 'TName'.
     * 
     * @param TName the value of field 'TName'.
     */
    public void setTName(java.lang.String TName)
    {
        this._TName = TName;
    } //-- void setTName(java.lang.String) 

    /**
     * Sets the value of field 'TOrder'.
     * 
     * @param TOrder the value of field 'TOrder'.
     */
    public void setTOrder(java.lang.Long TOrder)
    {
        this._TOrder = TOrder;
    } //-- void setTOrder(java.lang.Long) 

    /**
     * Sets the value of field 'TReplies'.
     * 
     * @param TReplies the value of field 'TReplies'.
     */
    public void setTReplies(java.lang.Long TReplies)
    {
        this._TReplies = TReplies;
    } //-- void setTReplies(java.lang.Long) 

    /**
     * Sets the value of field 'TUId'.
     * 
     * @param TUId the value of field 'TUId'.
     */
    public void setTUId(java.lang.Long TUId)
    {
        this._TUId = TUId;
    } //-- void setTUId(java.lang.Long) 

    /**
     * Sets the value of field 'TUId2'.
     * 
     * @param TUId2 the value of field 'TUId2'.
     */
    public void setTUId2(java.lang.Long TUId2)
    {
        this._TUId2 = TUId2;
    } //-- void setTUId2(java.lang.Long) 

    /**
     * Sets the value of field 'TViews'.
     * 
     * @param TViews the value of field 'TViews'.
     */
    public void setTViews(java.lang.Long TViews)
    {
        this._TViews = TViews;
    } //-- void setTViews(java.lang.Long) 

}
