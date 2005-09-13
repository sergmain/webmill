package org.riverock.webmill.container.portlet.bean;

import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;

/**
 * The portlet-collectionType is used to identify a subset
 * of portlets within a portlet application to which a
 * security constraint applies.
 * Used in: security-constraint
 *
 * @version $Revision$ $Date$
 */
public class PortletCollection implements Serializable {
    private static final long serialVersionUID = 30434672384237151L;


    /**
     * Field _portletNameList
     */
    private List<String> _portletNameList;


    public PortletCollection() {
        super();
        _portletNameList = new ArrayList<String>();
    }


    /**
     * Method addPortletName
     *
     * @param vPortletName
     */
    public void addPortletName(String vPortletName) {
        _portletNameList.add(vPortletName);
    }

    /**
     * Method addPortletName
     *
     * @param index
     * @param vPortletName
     */
    public void addPortletName(int index, String vPortletName) {
        _portletNameList.add(index, vPortletName);
    }

    /**
     * Method clearPortletName
     */
    public void clearPortletName() {
        _portletNameList.clear();
    }

    /**
     * Method getPortletName
     *
     * @param index
     */
    public String getPortletName(int index) {
        //-- check bounds for index
        if ((index < 0) || (index > _portletNameList.size())) {
            throw new IndexOutOfBoundsException();
        }

        return (String) _portletNameList.get(index);
    }

    /**
     * Method getPortletName
     */
    public String[] getPortletName() {
        int size = _portletNameList.size();
        String[] mArray = new String[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (String) _portletNameList.get(index);
        }
        return mArray;
    }

    /**
     * Method getPortletNameCount
     */
    public int getPortletNameCount() {
        return _portletNameList.size();
    }

    /**
     * Method removePortletName
     *
     * @param vPortletName
     */
    public boolean removePortletName(String vPortletName) {
        boolean removed = _portletNameList.remove(vPortletName);
        return removed;
    }

    /**
     * Method setPortletName
     *
     * @param index
     * @param vPortletName
     */
    public void setPortletName(int index, String vPortletName) {
        //-- check bounds for index
        if ((index < 0) || (index > _portletNameList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _portletNameList.set(index, vPortletName);
    }

    /**
     * Method setPortletName
     *
     * @param portletNameArray
     */
    public void setPortletName(String[] portletNameArray) {
        //-- copy array
        _portletNameList.clear();
        for (final String newVar : portletNameArray) {
            _portletNameList.add(newVar);
        }
    }
}
