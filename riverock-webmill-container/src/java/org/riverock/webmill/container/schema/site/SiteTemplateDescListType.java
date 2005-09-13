/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.7</a>, using an XML
 * Schema.
 * $Id$
 */

package org.riverock.webmill.container.schema.site;

import java.util.ArrayList;

/**
 * Class SiteTemplateDescListType.
 *
 * @version $Revision$ $Date$
 */
public class SiteTemplateDescListType {


    /**
     * Field _templateDescriptionList
     */
    private java.util.ArrayList<SiteTemplateDescriptionType> _templateDescriptionList;






    public SiteTemplateDescListType() {
        super();
        _templateDescriptionList = new ArrayList<SiteTemplateDescriptionType>();
    }






    /**
     * Method addTemplateDescription
     *
     * @param vTemplateDescription
     */
    public void addTemplateDescription(org.riverock.webmill.container.schema.site.SiteTemplateDescriptionType vTemplateDescription)
        throws java.lang.IndexOutOfBoundsException {
        _templateDescriptionList.add(vTemplateDescription);
    }

    /**
     * Method addTemplateDescription
     *
     * @param index
     * @param vTemplateDescription
     */
    public void addTemplateDescription(int index, org.riverock.webmill.container.schema.site.SiteTemplateDescriptionType vTemplateDescription)
        throws java.lang.IndexOutOfBoundsException {
        _templateDescriptionList.add(index, vTemplateDescription);
    }

    /**
     * Method clearTemplateDescription
     */
    public void clearTemplateDescription() {
        _templateDescriptionList.clear();
    }

    /**
     * Method getTemplateDescription
     *
     * @param index
     * @return SiteTemplateDescriptionType
     */
    public org.riverock.webmill.container.schema.site.SiteTemplateDescriptionType getTemplateDescription(int index)
        throws java.lang.IndexOutOfBoundsException {
        //-- check bounds for index
        if ((index < 0) || (index > _templateDescriptionList.size())) {
            throw new IndexOutOfBoundsException();
        }

        return (org.riverock.webmill.container.schema.site.SiteTemplateDescriptionType) _templateDescriptionList.get(index);
    }

    /**
     * Method getTemplateDescription
     *
     * @return SiteTemplateDescriptionType
     */
    public org.riverock.webmill.container.schema.site.SiteTemplateDescriptionType[] getTemplateDescription() {
        int size = _templateDescriptionList.size();
        org.riverock.webmill.container.schema.site.SiteTemplateDescriptionType[] mArray = new org.riverock.webmill.container.schema.site.SiteTemplateDescriptionType[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (org.riverock.webmill.container.schema.site.SiteTemplateDescriptionType) _templateDescriptionList.get(index);
        }
        return mArray;
    }

    /**
     * Method getTemplateDescriptionAsReference
     * <p/>
     * Returns a reference to 'templateDescription'. No type
     * checking is performed on any modications to the Collection.
     *
     * @return returns a reference to the Collection.
     */
    public java.util.ArrayList<SiteTemplateDescriptionType> getTemplateDescriptionAsReference() {
        return _templateDescriptionList;
    }

    /**
     * Method getTemplateDescriptionCount
     *
     * @return int
     */
    public int getTemplateDescriptionCount() {
        return _templateDescriptionList.size();
    }

    /**
     * Method removeTemplateDescription
     *
     * @param vTemplateDescription
     * @return boolean
     */
    public boolean removeTemplateDescription(org.riverock.webmill.container.schema.site.SiteTemplateDescriptionType vTemplateDescription) {
        boolean removed = _templateDescriptionList.remove(vTemplateDescription);
        return removed;
    }

    /**
     * Method setTemplateDescription
     *
     * @param index
     * @param vTemplateDescription
     */
    public void setTemplateDescription(int index, org.riverock.webmill.container.schema.site.SiteTemplateDescriptionType vTemplateDescription)
        throws java.lang.IndexOutOfBoundsException {
        //-- check bounds for index
        if ((index < 0) || (index > _templateDescriptionList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _templateDescriptionList.set(index, vTemplateDescription);
    }

    /**
     * Method setTemplateDescription
     *
     * @param templateDescriptionArray
     */
    public void setTemplateDescription(org.riverock.webmill.container.schema.site.SiteTemplateDescriptionType[] templateDescriptionArray) {
        //-- copy array
        _templateDescriptionList.clear();
        for (final SiteTemplateDescriptionType newVar : templateDescriptionArray) {
            _templateDescriptionList.add(newVar);
        }
    }

    /**
     * Method setTemplateDescription
     * <p/>
     * Sets the value of 'templateDescription' by copying the given
     * ArrayList.
     *
     * @param templateDescriptionCollection the ArrayList to copy.
     */
    public void setTemplateDescription(java.util.ArrayList templateDescriptionCollection) {
        //-- copy collection
        _templateDescriptionList.clear();
        for (int i = 0; i < templateDescriptionCollection.size(); i++) {
            _templateDescriptionList.add((org.riverock.webmill.container.schema.site.SiteTemplateDescriptionType) templateDescriptionCollection.get(i));
        }
    }

    /**
     * Method setTemplateDescriptionAsReference
     * <p/>
     * Sets the value of 'templateDescription' by setting it to the
     * given ArrayList. No type checking is performed.
     *
     * @param templateDescriptionCollection the ArrayList to copy.
     */
    public void setTemplateDescriptionAsReference(java.util.ArrayList<SiteTemplateDescriptionType> templateDescriptionCollection) {
        _templateDescriptionList = templateDescriptionCollection;
    }
}
