/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.7</a>, using an XML
 * Schema.
 * $Id$
 */

package org.riverock.webmill.container.schema.core;





import java.util.ArrayList;

/**
 * Class MainUserMetadataListType.
 * 
 * @version $Revision$ $Date$
 */
public class MainUserMetadataListType {



     //- Class/Member Variables -/


    /**
     * Field _mainUserMetadataList
     */
    private java.util.ArrayList _mainUserMetadataList;






    public MainUserMetadataListType() 
     {
        super();
        _mainUserMetadataList = new ArrayList();
    } //-- org.riverock.webmill.container.schema.core.MainUserMetadataListType()






    /**
     * Method addMainUserMetadata
     * 
     * 
     * 
     * @param vMainUserMetadata
     */
    public void addMainUserMetadata(org.riverock.webmill.container.schema.core.MainUserMetadataItemType vMainUserMetadata)
        throws java.lang.IndexOutOfBoundsException
    {
        _mainUserMetadataList.add(vMainUserMetadata);
    } //-- void addMainUserMetadata(org.riverock.webmill.container.schema.core.MainUserMetadataItemType) 

    /**
     * Method addMainUserMetadata
     * 
     * 
     * 
     * @param index
     * @param vMainUserMetadata
     */
    public void addMainUserMetadata(int index, org.riverock.webmill.container.schema.core.MainUserMetadataItemType vMainUserMetadata)
        throws java.lang.IndexOutOfBoundsException
    {
        _mainUserMetadataList.add(index, vMainUserMetadata);
    } //-- void addMainUserMetadata(int, org.riverock.webmill.container.schema.core.MainUserMetadataItemType) 

    /**
     * Method clearMainUserMetadata
     * 
     */
    public void clearMainUserMetadata()
    {
        _mainUserMetadataList.clear();
    } //-- void clearMainUserMetadata() 

    /**
     * Method getMainUserMetadata
     * 
     * 
     * 
     * @param index
     * @return MainUserMetadataItemType
     */
    public org.riverock.webmill.container.schema.core.MainUserMetadataItemType getMainUserMetadata(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _mainUserMetadataList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (org.riverock.webmill.container.schema.core.MainUserMetadataItemType) _mainUserMetadataList.get(index);
    } //-- org.riverock.webmill.container.schema.core.MainUserMetadataItemType getMainUserMetadata(int) 

    /**
     * Method getMainUserMetadata
     * 
     * 
     * 
     * @return MainUserMetadataItemType
     */
    public org.riverock.webmill.container.schema.core.MainUserMetadataItemType[] getMainUserMetadata()
    {
        int size = _mainUserMetadataList.size();
        org.riverock.webmill.container.schema.core.MainUserMetadataItemType[] mArray = new org.riverock.webmill.container.schema.core.MainUserMetadataItemType[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (org.riverock.webmill.container.schema.core.MainUserMetadataItemType) _mainUserMetadataList.get(index);
        }
        return mArray;
    } //-- org.riverock.webmill.container.schema.core.MainUserMetadataItemType[] getMainUserMetadata() 

    /**
     * Method getMainUserMetadataAsReference
     * 
     * Returns a reference to 'mainUserMetadata'. No type checking
     * is performed on any modications to the Collection.
     * 
     * @return ArrayList
     * @return returns a reference to the Collection.
     */
    public java.util.ArrayList getMainUserMetadataAsReference()
    {
        return _mainUserMetadataList;
    } //-- java.util.ArrayList getMainUserMetadataAsReference() 

    /**
     * Method getMainUserMetadataCount
     * 
     * 
     * 
     * @return int
     */
    public int getMainUserMetadataCount()
    {
        return _mainUserMetadataList.size();
    } //-- int getMainUserMetadataCount() 

    /**
     * Method removeMainUserMetadata
     * 
     * 
     * 
     * @param vMainUserMetadata
     * @return boolean
     */
    public boolean removeMainUserMetadata(org.riverock.webmill.container.schema.core.MainUserMetadataItemType vMainUserMetadata)
    {
        boolean removed = _mainUserMetadataList.remove(vMainUserMetadata);
        return removed;
    } //-- boolean removeMainUserMetadata(org.riverock.webmill.container.schema.core.MainUserMetadataItemType) 

    /**
     * Method setMainUserMetadata
     * 
     * 
     * 
     * @param index
     * @param vMainUserMetadata
     */
    public void setMainUserMetadata(int index, org.riverock.webmill.container.schema.core.MainUserMetadataItemType vMainUserMetadata)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _mainUserMetadataList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _mainUserMetadataList.set(index, vMainUserMetadata);
    } //-- void setMainUserMetadata(int, org.riverock.webmill.container.schema.core.MainUserMetadataItemType) 

    /**
     * Method setMainUserMetadata
     * 
     * 
     * 
     * @param mainUserMetadataArray
     */
    public void setMainUserMetadata(org.riverock.webmill.container.schema.core.MainUserMetadataItemType[] mainUserMetadataArray)
    {
        //-- copy array
        _mainUserMetadataList.clear();
        for (final MainUserMetadataItemType newVar : mainUserMetadataArray) {
            _mainUserMetadataList.add(newVar);
        }
    } //-- void setMainUserMetadata(org.riverock.webmill.container.schema.core.MainUserMetadataItemType) 

    /**
     * Method setMainUserMetadata
     * 
     * Sets the value of 'mainUserMetadata' by copying the given
     * ArrayList.
     * 
     * @param mainUserMetadataCollection the ArrayList to copy.
     */
    public void setMainUserMetadata(java.util.ArrayList mainUserMetadataCollection)
    {
        //-- copy collection
        _mainUserMetadataList.clear();
        for (int i = 0; i < mainUserMetadataCollection.size(); i++) {
            _mainUserMetadataList.add((org.riverock.webmill.container.schema.core.MainUserMetadataItemType)mainUserMetadataCollection.get(i));
        }
    } //-- void setMainUserMetadata(java.util.ArrayList) 

    /**
     * Method setMainUserMetadataAsReference
     * 
     * Sets the value of 'mainUserMetadata' by setting it to the
     * given ArrayList. No type checking is performed.
     * 
     * @param mainUserMetadataCollection the ArrayList to copy.
     */
    public void setMainUserMetadataAsReference(java.util.ArrayList mainUserMetadataCollection)
    {
        _mainUserMetadataList = mainUserMetadataCollection;
    } //-- void setMainUserMetadataAsReference(java.util.ArrayList) 
}
