package org.riverock.webmill.container.portlet.bean;

import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;

/**
 * User attribute defines a user specific attribute that the
 * portlet application needs. The portlet within this application
 * can access this attribute via the request parameter USER_INFO
 * map.
 * Used in: portlet-app
 *
 * @version $Revision$ $Date$
 */
public class UserAttribute implements Serializable {
    private static final long serialVersionUID = 30434672384237171L;

    /**
     * Field _id
     */
    private java.lang.String _id;

    /**
     * Field _descriptionList
     */
    private List<Description> _descriptionList;

    /**
     * Field _name
     */
    private String _name;


    public UserAttribute() {
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
            mArray[index] = _descriptionList.get(index);
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
     * Returns the value of field 'name'.
     *
     * @return the value of field 'name'.
     */
    public String getName() {
        return this._name;
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
     * Sets the value of field 'name'.
     *
     * @param name the value of field 'name'.
     */
    public void setName(String name) {
        this._name = name;
    }
}
