package org.riverock.webmill.container.portlet.bean;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.io.Serializable;

/**
 * Persistent preference values that may be used for customization
 * and personalization by the portlet.
 * Used in: portlet-preferences
 *
 * @version $Revision$ $Date$
 */
public class Preference implements Serializable {
    private static final long serialVersionUID = 30434672384237162L;


    /**
     * Field _id
     */
    private java.lang.String _id;

    /**
     * Field _name
     */
    private String _name;

    /**
     * Field _valueList
     */
    private Collection<String> _valueList;

    /**
     * Field _readOnly
     */
    private Boolean _readOnly = null;


    public Preference() {
        super();
        _valueList = new ArrayList<String>();
    }


    public void setValue(Collection<String> values) {
        this._valueList = values;
    }

    public Iterator<String> getValue() {
        if (_valueList == null) {
            setValue(new ArrayList<String>());
        }

        return _valueList.iterator();
    }

    public Collection<String> getValueAsRef() {
        if (_valueList == null) {
            setValue(new ArrayList<String>());
        }

        return _valueList;
    }

    /**
     * Method addValue
     *
     * @param vValue
     */
    public void addValue(String vValue) {
        _valueList.add(vValue);
    }

    /**
     * Method addValue
     *
     * @param index
     * @param vValue
     */
//    public void addValue(int index, String vValue) {
//        _valueList.add(index, vValue);
//    }

    /**
     * Method clearValue
     */
    public void clearValue() {
        _valueList.clear();
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
     * Returns the value of field 'readOnly'.
     *
     * @return the value of field 'readOnly'.
     */
    public Boolean getReadOnly() {
        return this._readOnly;
    }

    /**
     * Method getValue
     *
     * @param index
     */
/*
    public String getValue(int index) {
        //-- check bounds for index
        if ((index < 0) || (index > _valueList.size())) {
            throw new IndexOutOfBoundsException();
        }

        return _valueList.get(index);
    }
*/

    /**
     * Method getValue
     */
/*
    public String[] getValue() {
        int size = _valueList.size();
        String[] mArray = new String[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = _valueList.get(index);
        }
        return mArray;
    }
*/

    /**
     * Method getValueCount
     */
    public int getValueCount() {
        return _valueList.size();
    }

    /**
     * Method removeValue
     *
     * @param vValue
     */
    public boolean removeValue(String vValue) {
        boolean removed = _valueList.remove(vValue);
        return removed;
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

    /**
     * Sets the value of field 'readOnly'.
     *
     * @param readOnly the value of field 'readOnly'.
     */
    public void setReadOnly(Boolean readOnly) {
        this._readOnly = readOnly;
    }

    /**
     * Method setValue
     *
     * @param index
     * @param vValue
     */
/*
    public void setValue(int index, String vValue) {
        //-- check bounds for index
        if ((index < 0) || (index > _valueList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _valueList.set(index, vValue);
    }
*/

    /**
     * Method setValue
     *
     * @param valueArray
     */
    public void setValue(String[] valueArray) {
        //-- copy array
        _valueList.clear();
        for (final String newVar : valueArray) {
            _valueList.add(newVar);
        }
    }
}
