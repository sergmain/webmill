/*
 * org.riverock.sql -- Classes for tracking database changes
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

/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id$
 */

package org.riverock.schema.sql.types;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.Serializable;
import java.util.Enumeration;
import java.util.Hashtable;

/**
 * Class AggregateSpecType.
 * 
 * @version $Revision$ $Date$
 */
public class AggregateSpecType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * The AGGREGATE_SELF type
     */
    public static final int AGGREGATE_SELF_TYPE = 0;

    /**
     * The instance of the AGGREGATE_SELF type
     */
    public static final AggregateSpecType AGGREGATE_SELF = new AggregateSpecType(AGGREGATE_SELF_TYPE, "AGGREGATE_SELF");

    /**
     * The AGGREGATE_NONE type
     */
    public static final int AGGREGATE_NONE_TYPE = 1;

    /**
     * The instance of the AGGREGATE_NONE type
     */
    public static final AggregateSpecType AGGREGATE_NONE = new AggregateSpecType(AGGREGATE_NONE_TYPE, "AGGREGATE_NONE");

    /**
     * The AGGREGATE_LEFT type
     */
    public static final int AGGREGATE_LEFT_TYPE = 2;

    /**
     * The instance of the AGGREGATE_LEFT type
     */
    public static final AggregateSpecType AGGREGATE_LEFT = new AggregateSpecType(AGGREGATE_LEFT_TYPE, "AGGREGATE_LEFT");

    /**
     * The AGGREGATE_RIGHT type
     */
    public static final int AGGREGATE_RIGHT_TYPE = 3;

    /**
     * The instance of the AGGREGATE_RIGHT type
     */
    public static final AggregateSpecType AGGREGATE_RIGHT = new AggregateSpecType(AGGREGATE_RIGHT_TYPE, "AGGREGATE_RIGHT");

    /**
     * The AGGREGATE_BOTH type
     */
    public static final int AGGREGATE_BOTH_TYPE = 4;

    /**
     * The instance of the AGGREGATE_BOTH type
     */
    public static final AggregateSpecType AGGREGATE_BOTH = new AggregateSpecType(AGGREGATE_BOTH_TYPE, "AGGREGATE_BOTH");

    /**
     * Field _memberTable
     */
    private static java.util.Hashtable _memberTable = init();

    /**
     * Field type
     */
    private int type = -1;

    /**
     * Field stringValue
     */
    private java.lang.String stringValue = null;


      //----------------/
     //- Constructors -/
    //----------------/

    private AggregateSpecType(int type, java.lang.String value) {
        super();
        this.type = type;
        this.stringValue = value;
    } //-- org.riverock.schema.sql.types.AggregateSpecType(int, java.lang.String)


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method enumerateReturns an enumeration of all possible
     * instances of AggregateSpecType
     */
    public static java.util.Enumeration enumerate()
    {
        return _memberTable.elements();
    } //-- java.util.Enumeration enumerate() 

    /**
     * Method getTypeReturns the type of this AggregateSpecType
     */
    public int getType()
    {
        return this.type;
    } //-- int getType() 

    /**
     * Method init
     */
    private static java.util.Hashtable init()
    {
        Hashtable members = new Hashtable();
        members.put("AGGREGATE_SELF", AGGREGATE_SELF);
        members.put("AGGREGATE_NONE", AGGREGATE_NONE);
        members.put("AGGREGATE_LEFT", AGGREGATE_LEFT);
        members.put("AGGREGATE_RIGHT", AGGREGATE_RIGHT);
        members.put("AGGREGATE_BOTH", AGGREGATE_BOTH);
        return members;
    } //-- java.util.Hashtable init() 

    /**
     * Method toStringReturns the String representation of this
     * AggregateSpecType
     */
    public java.lang.String toString()
    {
        return this.stringValue;
    } //-- java.lang.String toString() 

    /**
     * Method valueOfReturns a new AggregateSpecType based on the
     * given String value.
     * 
     * @param string
     */
    public static org.riverock.schema.sql.types.AggregateSpecType valueOf(java.lang.String string)
    {
        java.lang.Object obj = null;
        if (string != null) obj = _memberTable.get(string);
        if (obj == null) {
            String err = "'" + string + "' is not a valid AggregateSpecType";
            throw new IllegalArgumentException(err);
        }
        return (AggregateSpecType) obj;
    } //-- org.riverock.schema.sql.types.AggregateSpecType valueOf(java.lang.String) 

}
