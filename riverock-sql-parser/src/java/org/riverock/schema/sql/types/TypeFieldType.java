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
 * Class TypeFieldType.
 * 
 * @version $Revision$ $Date$
 */
public class TypeFieldType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * The SUBQUERY type
     */
    public static final int SUBQUERY_TYPE = 0;

    /**
     * The instance of the SUBQUERY type
     */
    public static final TypeFieldType SUBQUERY = new TypeFieldType(SUBQUERY_TYPE, "SUBQUERY");

    /**
     * The FORMULA type
     */
    public static final int FORMULA_TYPE = 1;

    /**
     * The instance of the FORMULA type
     */
    public static final TypeFieldType FORMULA = new TypeFieldType(FORMULA_TYPE, "FORMULA");

    /**
     * The FIELD type
     */
    public static final int FIELD_TYPE = 2;

    /**
     * The instance of the FIELD type
     */
    public static final TypeFieldType FIELD = new TypeFieldType(FIELD_TYPE, "FIELD");

    /**
     * The VALUE type
     */
    public static final int VALUE_TYPE = 3;

    /**
     * The instance of the VALUE type
     */
    public static final TypeFieldType VALUE = new TypeFieldType(VALUE_TYPE, "VALUE");

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

    private TypeFieldType(int type, java.lang.String value) {
        super();
        this.type = type;
        this.stringValue = value;
    } //-- org.riverock.schema.sql.types.TypeFieldType(int, java.lang.String)


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method enumerateReturns an enumeration of all possible
     * instances of TypeFieldType
     */
    public static java.util.Enumeration enumerate()
    {
        return _memberTable.elements();
    } //-- java.util.Enumeration enumerate() 

    /**
     * Method getTypeReturns the type of this TypeFieldType
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
        members.put("SUBQUERY", SUBQUERY);
        members.put("FORMULA", FORMULA);
        members.put("FIELD", FIELD);
        members.put("VALUE", VALUE);
        return members;
    } //-- java.util.Hashtable init() 

    /**
     * Method toStringReturns the String representation of this
     * TypeFieldType
     */
    public java.lang.String toString()
    {
        return this.stringValue;
    } //-- java.lang.String toString() 

    /**
     * Method valueOfReturns a new TypeFieldType based on the given
     * String value.
     * 
     * @param string
     */
    public static org.riverock.schema.sql.types.TypeFieldType valueOf(java.lang.String string)
    {
        java.lang.Object obj = null;
        if (string != null) obj = _memberTable.get(string);
        if (obj == null) {
            String err = "'" + string + "' is not a valid TypeFieldType";
            throw new IllegalArgumentException(err);
        }
        return (TypeFieldType) obj;
    } //-- org.riverock.schema.sql.types.TypeFieldType valueOf(java.lang.String) 

}
