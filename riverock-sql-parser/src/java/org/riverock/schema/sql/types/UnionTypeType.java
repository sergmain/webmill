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
 * Class UnionTypeType.
 * 
 * @version $Revision$ $Date$
 */
public class UnionTypeType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * The UNION type
     */
    public static final int UNION_TYPE = 0;

    /**
     * The instance of the UNION type
     */
    public static final UnionTypeType UNION = new UnionTypeType(UNION_TYPE, "UNION");

    /**
     * The UNIONALL type
     */
    public static final int UNIONALL_TYPE = 1;

    /**
     * The instance of the UNIONALL type
     */
    public static final UnionTypeType UNIONALL = new UnionTypeType(UNIONALL_TYPE, "UNIONALL");

    /**
     * The INTERSECT type
     */
    public static final int INTERSECT_TYPE = 2;

    /**
     * The instance of the INTERSECT type
     */
    public static final UnionTypeType INTERSECT = new UnionTypeType(INTERSECT_TYPE, "INTERSECT");

    /**
     * The MINUS type
     */
    public static final int MINUS_TYPE = 3;

    /**
     * The instance of the MINUS type
     */
    public static final UnionTypeType MINUS = new UnionTypeType(MINUS_TYPE, "MINUS");

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

    private UnionTypeType(int type, java.lang.String value) {
        super();
        this.type = type;
        this.stringValue = value;
    } //-- org.riverock.schema.sql.types.UnionTypeType(int, java.lang.String)


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method enumerateReturns an enumeration of all possible
     * instances of UnionTypeType
     */
    public static java.util.Enumeration enumerate()
    {
        return _memberTable.elements();
    } //-- java.util.Enumeration enumerate() 

    /**
     * Method getTypeReturns the type of this UnionTypeType
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
        members.put("UNION", UNION);
        members.put("UNIONALL", UNIONALL);
        members.put("INTERSECT", INTERSECT);
        members.put("MINUS", MINUS);
        return members;
    } //-- java.util.Hashtable init() 

    /**
     * Method toStringReturns the String representation of this
     * UnionTypeType
     */
    public java.lang.String toString()
    {
        return this.stringValue;
    } //-- java.lang.String toString() 

    /**
     * Method valueOfReturns a new UnionTypeType based on the given
     * String value.
     * 
     * @param string
     */
    public static org.riverock.schema.sql.types.UnionTypeType valueOf(java.lang.String string)
    {
        java.lang.Object obj = null;
        if (string != null) obj = _memberTable.get(string);
        if (obj == null) {
            String err = "'" + string + "' is not a valid UnionTypeType";
            throw new IllegalArgumentException(err);
        }
        return (UnionTypeType) obj;
    } //-- org.riverock.schema.sql.types.UnionTypeType valueOf(java.lang.String) 

}