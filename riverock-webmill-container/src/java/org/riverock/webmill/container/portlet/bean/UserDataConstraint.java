package org.riverock.webmill.container.portlet.bean;

import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;

import org.riverock.webmill.container.portlet.bean.types.TransportGuaranteeType;

/**
 * The user-data-constraintType is used to indicate how
 * data communicated between the client and portlet should be
 * protected.
 * Used in: security-constraint
 *
 * @version $Revision$ $Date$
 */
public class UserDataConstraint implements Serializable {
    private static final long serialVersionUID = 30434672384237173L;

    /**
     * Field _id
     */
    private java.lang.String _id;

    /**
     * Field _descriptionList
     */
    private List<Description> _descriptionList;

    /**
     * Field _transportGuarantee
     */
    private TransportGuaranteeType _transportGuarantee;


    public UserDataConstraint() {
        super();
        _descriptionList = new ArrayList<Description>();
    }


    /**
     * Method addDescription
     *
     * @param vDescription
     */
    public void addDescription(Description vDescription) {
        _descriptionList.add(vDescription);
    }

    /**
     * Method addDescription
     *
     * @param index
     * @param vDescription
     */
    public void addDescription(int index, Description vDescription) {
        _descriptionList.add(index, vDescription);
    }

    /**
     * Method clearDescription
     */
    public void clearDescription() {
        _descriptionList.clear();
    }

    /**
     * Method getDescription
     *
     * @param index
     */
    public Description getDescription(int index) {
        //-- check bounds for index
        if ((index < 0) || (index > _descriptionList.size())) {
            throw new IndexOutOfBoundsException();
        }

        return (Description) _descriptionList.get(index);
    }

    /**
     * Method getDescription
     */
    public Description[] getDescription() {
        int size = _descriptionList.size();
        Description[] mArray = new Description[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (Description) _descriptionList.get(index);
        }
        return mArray;
    }

    /**
     * Method getDescriptionCount
     */
    public int getDescriptionCount() {
        return _descriptionList.size();
    }

    /**
     * Returns the value of field 'id'.
     *
     * @return the value of field 'id'.
     */
    public java.lang.String getId() {
        return this._id;
    }

    /**
     * Returns the value of field 'transportGuarantee'.
     *
     * @return the value of field 'transportGuarantee'.
     */
    public TransportGuaranteeType getTransportGuarantee() {
        return this._transportGuarantee;
    }

    /**
     * Method removeDescription
     *
     * @param vDescription
     */
    public boolean removeDescription(Description vDescription) {
        boolean removed = _descriptionList.remove(vDescription);
        return removed;
    }

    /**
     * Method setDescription
     *
     * @param index
     * @param vDescription
     */
    public void setDescription(int index, Description vDescription) {
        //-- check bounds for index
        if ((index < 0) || (index > _descriptionList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _descriptionList.set(index, vDescription);
    }

    /**
     * Method setDescription
     *
     * @param descriptionArray
     */
    public void setDescription(Description[] descriptionArray) {
        //-- copy array
        _descriptionList.clear();
        for (final Description newVar : descriptionArray) {
            _descriptionList.add(newVar);
        }
    }

    /**
     * Sets the value of field 'id'.
     *
     * @param id the value of field 'id'.
     */
    public void setId(java.lang.String id) {
        this._id = id;
    }

    /**
     * Sets the value of field 'transportGuarantee'.
     *
     * @param transportGuarantee the value of field
     *                           'transportGuarantee'.
     */
    public void setTransportGuarantee(TransportGuaranteeType transportGuarantee) {
        this._transportGuarantee = transportGuarantee;
    }
}
