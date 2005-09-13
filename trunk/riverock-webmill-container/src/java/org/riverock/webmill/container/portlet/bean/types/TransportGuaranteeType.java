package org.riverock.webmill.container.portlet.bean.types;

import java.util.Hashtable;
import java.io.Serializable;

/**
 * Class TransportGuaranteeType.
 *
 * @version $Revision$ $Date$
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
    private static java.util.Hashtable _memberTable = init();

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
     * Method enumerateReturns an enumeration of all possible
     * instances of TransportGuaranteeType
     */
    public static java.util.Enumeration enumerate() {
        return _memberTable.elements();
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
    private static java.util.Hashtable init() {
        Hashtable members = new Hashtable();
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
