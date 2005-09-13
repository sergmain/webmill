package org.riverock.webmill.container.portlet.bean;

import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;

/**
 * Supports indicates the portlet modes a
 * portlet supports for a specific content type. All portlets must
 * <p/>
 * support the view mode.
 * Used in: portlet
 *
 * @version $Revision$ $Date$
 */
public class Supports implements Serializable {
    private static final long serialVersionUID = 30434672384237169L;

    /**
     * Field _id
     */
    private java.lang.String _id;

    /**
     * Field mimeType
     */
    private String mimeType;

    /**
     * Field _portletModeList
     */
    private List<String> _portletModeList;


    public Supports() {
        super();
        _portletModeList = new ArrayList<String>();
    }


    /**
     * Method addPortletMode
     *
     * @param vPortletMode
     */
    public void addPortletMode(String vPortletMode) {
        _portletModeList.add(vPortletMode);
    }

    /**
     * Method addPortletMode
     *
     * @param index
     * @param vPortletMode
     */
    public void addPortletMode(int index, String vPortletMode) {
        _portletModeList.add(index, vPortletMode);
    }

    /**
     * Method clearPortletMode
     */
    public void clearPortletMode() {
        _portletModeList.clear();
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
     * Returns the value of field 'mimeType'.
     *
     * @return the value of field 'mimeType'.
     */
    public String getMimeType() {
        return this.mimeType;
    }

    /**
     * Method getPortletMode
     *
     * @param index
     */
    public String getPortletMode(int index) {
        //-- check bounds for index
        if ((index < 0) || (index > _portletModeList.size())) {
            throw new IndexOutOfBoundsException();
        }

        return _portletModeList.get(index);
    }

    /**
     * Method getPortletMode
     */
    public String[] getPortletMode() {
        int size = _portletModeList.size();
        String[] mArray = new String[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = _portletModeList.get(index);
        }
        return mArray;
    }

    /**
     * Method getPortletModeCount
     */
    public int getPortletModeCount() {
        return _portletModeList.size();
    }

    /**
     * Method removePortletMode
     *
     * @param vPortletMode
     */
    public boolean removePortletMode(String vPortletMode) {
        boolean removed = _portletModeList.remove(vPortletMode);
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
     * Sets the value of field 'mimeType'.
     *
     * @param mimeType the value of field 'mimeType'.
     */
    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    /**
     * Method setPortletMode
     *
     * @param index
     * @param vPortletMode
     */
    public void setPortletMode(int index, String vPortletMode) {
        //-- check bounds for index
        if ((index < 0) || (index > _portletModeList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _portletModeList.set(index, vPortletMode);
    }

    /**
     * Method setPortletMode
     *
     * @param portletModeArray
     */
    public void setPortletMode(String[] portletModeArray) {
        //-- copy array
        _portletModeList.clear();
        for (final String newVar : portletModeArray) {
            _portletModeList.add(newVar);
        }
    }
}
