package org.riverock.webmill.container.portlet.bean;

import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;

/**
 * Portlet persistent preference store.
 * Used in: portlet
 *
 * @version $Revision$ $Date$
 */
public class PortletPreferences implements Serializable {
    private static final long serialVersionUID = 30434672384237160L;


    /**
     * Field _id
     */
    private java.lang.String _id;

    /**
     * Field _preferenceList
     */
    private List<Preference> _preferenceList;

    /**
     * Field _preferencesValidator
     */
    private java.lang.String _preferencesValidator;


    public PortletPreferences() {
        super();
        _preferenceList = new ArrayList<Preference>();
    }


    /**
     * Method addPreference
     *
     * @param vPreference
     */
    public void addPreference(Preference vPreference) {
        _preferenceList.add(vPreference);
    }

    /**
     * Method addPreference
     *
     * @param index
     * @param vPreference
     */
    public void addPreference(int index, Preference vPreference) {
        _preferenceList.add(index, vPreference);
    }

    /**
     * Method clearPreference
     */
    public void clearPreference() {
        _preferenceList.clear();
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
     * Method getPreference
     *
     * @param index
     */
    public Preference getPreference(int index) {
        //-- check bounds for index
        if ((index < 0) || (index > _preferenceList.size())) {
            throw new IndexOutOfBoundsException();
        }

        return (Preference) _preferenceList.get(index);
    }

    /**
     * Method getPreference
     */
    public Preference[] getPreference() {
        int size = _preferenceList.size();
        Preference[] mArray = new Preference[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (Preference) _preferenceList.get(index);
        }
        return mArray;
    }

    /**
     * Method getPreferenceCount
     */
    public int getPreferenceCount() {
        return _preferenceList.size();
    }

    /**
     * Returns the value of field 'preferencesValidator'.
     *
     * @return the value of field 'preferencesValidator'.
     */
    public java.lang.String getPreferencesValidator() {
        return this._preferencesValidator;
    }

    /**
     * Method removePreference
     *
     * @param vPreference
     */
    public boolean removePreference(Preference vPreference) {
        boolean removed = _preferenceList.remove(vPreference);
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
     * Method setPreference
     *
     * @param index
     * @param vPreference
     */
    public void setPreference(int index, Preference vPreference) {
        //-- check bounds for index
        if ((index < 0) || (index > _preferenceList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _preferenceList.set(index, vPreference);
    }

    /**
     * Method setPreference
     *
     * @param preferenceArray
     */
    public void setPreference(Preference[] preferenceArray) {
        //-- copy array
        _preferenceList.clear();
        for (final Preference newVar : preferenceArray) {
            _preferenceList.add(newVar);
        }
    }

    /**
     * Sets the value of field 'preferencesValidator'.
     *
     * @param preferencesValidator the value of field
     *                             'preferencesValidator'.
     */
    public void setPreferencesValidator(java.lang.String preferencesValidator) {
        this._preferencesValidator = preferencesValidator;
    }
}
