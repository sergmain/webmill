/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.7</a>, using an XML
 * Schema.
 * $Id$
 */

package org.riverock.webmill.container.schema.site;


import java.util.ArrayList;
import java.util.List;

/**
 * Class SitePortletDataListType.
 *
 * @version $Revision$ $Date$
 */
public class SitePortletDataListType {


    //- Class/Member Variables -/


    /**
     * Field _portletList
     */
    private List<SitePortletDataType> _portletList;

    public SitePortletDataListType() {
        super();
        _portletList = new ArrayList<SitePortletDataType>();
    }

    /**
     * Method addPortlet
     *
     * @param vPortlet
     */
    public void addPortlet(org.riverock.webmill.container.schema.site.SitePortletDataType vPortlet)
        throws java.lang.IndexOutOfBoundsException {
        _portletList.add(vPortlet);
    }

    /**
     * Method addPortlet
     *
     * @param index
     * @param vPortlet
     */
    public void addPortlet(int index, org.riverock.webmill.container.schema.site.SitePortletDataType vPortlet)
        throws java.lang.IndexOutOfBoundsException {
        _portletList.add(index, vPortlet);
    } //-- void addPortlet(int, org.riverock.webmill.container.schema.site.SitePortletDataType) 

    /**
     * Method clearPortlet
     */
    public void clearPortlet() {
        _portletList.clear();
    } //-- void clearPortlet() 

    /**
     * Method getPortlet
     *
     * @param index
     * @return SitePortletDataType
     */
    public org.riverock.webmill.container.schema.site.SitePortletDataType getPortlet(int index)
        throws java.lang.IndexOutOfBoundsException {
        //-- check bounds for index
        if ((index < 0) || (index > _portletList.size())) {
            throw new IndexOutOfBoundsException();
        }

        return (org.riverock.webmill.container.schema.site.SitePortletDataType) _portletList.get(index);
    } //-- org.riverock.webmill.container.schema.site.SitePortletDataType getPortlet(int) 

    /**
     * Method getPortlet
     *
     * @return SitePortletDataType
     */
    public org.riverock.webmill.container.schema.site.SitePortletDataType[] getPortlet() {
        int size = _portletList.size();
        org.riverock.webmill.container.schema.site.SitePortletDataType[] mArray = new org.riverock.webmill.container.schema.site.SitePortletDataType[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (org.riverock.webmill.container.schema.site.SitePortletDataType) _portletList.get(index);
        }
        return mArray;
    } //-- org.riverock.webmill.container.schema.site.SitePortletDataType[] getPortlet() 

    /**
     * Method getPortletAsReference
     * <p/>
     * Returns a reference to 'portlet'. No type checking is
     * performed on any modications to the Collection.
     *
     * @return returns a reference to the Collection.
     */
    public List<SitePortletDataType> getPortletAsReference() {
        return _portletList;
    }

    /**
     * Method getPortletCount
     *
     * @return int
     */
    public int getPortletCount() {
        return _portletList.size();
    } //-- int getPortletCount() 

    /**
     * Method removePortlet
     *
     * @param vPortlet
     * @return boolean
     */
    public boolean removePortlet(org.riverock.webmill.container.schema.site.SitePortletDataType vPortlet) {
        boolean removed = _portletList.remove(vPortlet);
        return removed;
    } //-- boolean removePortlet(org.riverock.webmill.container.schema.site.SitePortletDataType) 

    /**
     * Method setPortlet
     *
     * @param index
     * @param vPortlet
     */
    public void setPortlet(int index, org.riverock.webmill.container.schema.site.SitePortletDataType vPortlet)
        throws java.lang.IndexOutOfBoundsException {
        //-- check bounds for index
        if ((index < 0) || (index > _portletList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _portletList.set(index, vPortlet);
    } //-- void setPortlet(int, org.riverock.webmill.container.schema.site.SitePortletDataType) 

    /**
     * Method setPortlet
     *
     * @param portletArray
     */
    public void setPortlet(org.riverock.webmill.container.schema.site.SitePortletDataType[] portletArray) {
        //-- copy array
        _portletList.clear();
        for (final SitePortletDataType newVar : portletArray) {
            _portletList.add(newVar);
        }
    } //-- void setPortlet(org.riverock.webmill.container.schema.site.SitePortletDataType) 

    /**
     * Method setPortlet
     * <p/>
     * Sets the value of 'portlet' by copying the given ArrayList.
     *
     * @param portletCollection the ArrayList to copy.
     */
    public void setPortlet(java.util.ArrayList portletCollection) {
        _portletList.clear();
        for (int i = 0; i < portletCollection.size(); i++) {
            _portletList.add((org.riverock.webmill.container.schema.site.SitePortletDataType) portletCollection.get(i));
        }
    }

    /**
     * Method setPortletAsReference
     * <p/>
     * Sets the value of 'portlet' by setting it to the given
     * ArrayList. No type checking is performed.
     *
     * @param portletCollection the ArrayList to copy.
     */
    public void setPortletAsReference(List<SitePortletDataType> portletCollection) {
        _portletList = portletCollection;
    }
}
