/*
 * org.riverock.webmill.container - Webmill portlet container implementation
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
package org.riverock.webmill.container.portlet.bean.types;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Class TransportGuaranteeType.
 *
 * @version $Revision: 1055 $ $Date: 2006-11-14 17:56:15 +0000 (Tue, 14 Nov 2006) $
 */
public class TransportGuaranteeType implements Serializable {
    private static final long serialVersionUID = 30434672384237120L;


    /**
     * The NONE type
     */
    public static final int NONE_TYPE = 0;

    /**
     * The instance of the NONE type
     */
    public static final TransportGuaranteeType NONE = new TransportGuaranteeType(NONE_TYPE, "NONE");

    /**
     * The INTEGRAL type
     */
    public static final int INTEGRAL_TYPE = 1;

    /**
     * The instance of the INTEGRAL type
     */
    public static final TransportGuaranteeType INTEGRAL = new TransportGuaranteeType(INTEGRAL_TYPE, "INTEGRAL");

    /**
     * The CONFIDENTIAL type
     */
    public static final int CONFIDENTIAL_TYPE = 2;

    /**
     * The instance of the CONFIDENTIAL type
     */
    public static final TransportGuaranteeType CONFIDENTIAL = new TransportGuaranteeType(CONFIDENTIAL_TYPE, "CONFIDENTIAL");

    /**
     * Field _memberTable
     */
    private static Map<String, TransportGuaranteeType> _memberTable = init();

    /**
     * Field type
     */
    private int type = -1;

    /**
     * Field stringValue
     */
    private java.lang.String stringValue = null;


    private TransportGuaranteeType(int type, java.lang.String value) {
        super();
        this.type = type;
        this.stringValue = value;
    }

    /**
     * Method getTypeReturns the type of this TransportGuaranteeType
     */
    public int getType() {
        return this.type;
    }

    /**
     * Method init
     */
    private static Map<String, TransportGuaranteeType> init() {
        Map<String, TransportGuaranteeType> members = new HashMap<String, TransportGuaranteeType>();
        members.put("NONE", NONE);
        members.put("INTEGRAL", INTEGRAL);
        members.put("CONFIDENTIAL", CONFIDENTIAL);
        return members;
    }

    /**
     * Method toStringReturns the String representation of this
     * TransportGuaranteeType
     */
    public java.lang.String toString() {
        return this.stringValue;
    }

    /**
     * Method valueOfReturns a new TransportGuaranteeType based on
     * the given String value.
     *
     * @param string
     */
    public static TransportGuaranteeType valueOf(java.lang.String string) {
        java.lang.Object obj = null;
        if (string != null) obj = _memberTable.get(string);
        if (obj == null) {
            String err = "'" + string + "' is not a valid TransportGuaranteeType";
            throw new IllegalArgumentException(err);
        }
        return (TransportGuaranteeType) obj;
    } 
}
