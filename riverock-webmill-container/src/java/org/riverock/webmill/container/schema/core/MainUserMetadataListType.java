/*
 * org.riverock.webmill.container -- Webmill portlet container implementation
 *
 * Copyright (C) 2004, Riverock Software, All Rights Reserved.
 *
 * Riverock -- The Open-source Java Development Community
 * http://www.riverock.org
 *
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 *
 */
package org.riverock.webmill.container.schema.core;

import java.util.ArrayList;
import java.util.List;

/**
 * Class MainUserMetadataListType.
 *
 * @version $Revision$ $Date$
 */
public class MainUserMetadataListType {

    /**
     * Field _mainUserMetadataList
     */
    private List<MainUserMetadataItemType> _mainUserMetadataList;

    public MainUserMetadataListType() {
        super();
        _mainUserMetadataList = new ArrayList<MainUserMetadataItemType>();
    }

    /**
     * Method addMainUserMetadata
     *
     * @param vMainUserMetadata
     */
    public void addMainUserMetadata(org.riverock.webmill.container.schema.core.MainUserMetadataItemType vMainUserMetadata)
        throws java.lang.IndexOutOfBoundsException {
        _mainUserMetadataList.add(vMainUserMetadata);
    }

    /**
     * Method addMainUserMetadata
     *
     * @param index
     * @param vMainUserMetadata
     */
    public void addMainUserMetadata(int index, org.riverock.webmill.container.schema.core.MainUserMetadataItemType vMainUserMetadata)
        throws java.lang.IndexOutOfBoundsException {
        _mainUserMetadataList.add(index, vMainUserMetadata);
    }

    /**
     * Method clearMainUserMetadata
     */
    public void clearMainUserMetadata() {
        _mainUserMetadataList.clear();
    }

    /**
     * Method getMainUserMetadata
     *
     * @param index
     * @return MainUserMetadataItemType
     */
    public org.riverock.webmill.container.schema.core.MainUserMetadataItemType getMainUserMetadata(int index)
        throws java.lang.IndexOutOfBoundsException {
        //-- check bounds for index
        if ((index < 0) || (index > _mainUserMetadataList.size())) {
            throw new IndexOutOfBoundsException();
        }

        return (org.riverock.webmill.container.schema.core.MainUserMetadataItemType) _mainUserMetadataList.get(index);
    }

    /**
     * Method getMainUserMetadata
     *
     * @return MainUserMetadataItemType
     */
    public org.riverock.webmill.container.schema.core.MainUserMetadataItemType[] getMainUserMetadata() {
        int size = _mainUserMetadataList.size();
        org.riverock.webmill.container.schema.core.MainUserMetadataItemType[] mArray = new org.riverock.webmill.container.schema.core.MainUserMetadataItemType[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (org.riverock.webmill.container.schema.core.MainUserMetadataItemType) _mainUserMetadataList.get(index);
        }
        return mArray;
    }

    /**
     * Method getMainUserMetadataAsReference
     * <p/>
     * Returns a reference to 'mainUserMetadata'. No type checking
     * is performed on any modications to the Collection.
     *
     * @return returns a reference to the Collection.
     */
    public List<MainUserMetadataItemType> getMainUserMetadataAsReference() {
        return _mainUserMetadataList;
    }

    /**
     * Method getMainUserMetadataCount
     *
     * @return int
     */
    public int getMainUserMetadataCount() {
        return _mainUserMetadataList.size();
    }

    /**
     * Method removeMainUserMetadata
     *
     * @param vMainUserMetadata
     * @return boolean
     */
    public boolean removeMainUserMetadata(org.riverock.webmill.container.schema.core.MainUserMetadataItemType vMainUserMetadata) {
        boolean removed = _mainUserMetadataList.remove(vMainUserMetadata);
        return removed;
    }

    /**
     * Method setMainUserMetadata
     *
     * @param index
     * @param vMainUserMetadata
     */
    public void setMainUserMetadata(int index, org.riverock.webmill.container.schema.core.MainUserMetadataItemType vMainUserMetadata)
        throws java.lang.IndexOutOfBoundsException {
        //-- check bounds for index
        if ((index < 0) || (index > _mainUserMetadataList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _mainUserMetadataList.set(index, vMainUserMetadata);
    }

    /**
     * Method setMainUserMetadata
     *
     * @param mainUserMetadataArray
     */
    public void setMainUserMetadata(org.riverock.webmill.container.schema.core.MainUserMetadataItemType[] mainUserMetadataArray) {
        //-- copy array
        _mainUserMetadataList.clear();
        for (final MainUserMetadataItemType newVar : mainUserMetadataArray) {
            _mainUserMetadataList.add(newVar);
        }
    }

    /**
     * Method setMainUserMetadata
     * <p/>
     * Sets the value of 'mainUserMetadata' by copying the given
     * ArrayList.
     *
     * @param mainUserMetadataCollection the ArrayList to copy.
     */
    public void setMainUserMetadata(java.util.ArrayList mainUserMetadataCollection) {
        //-- copy collection
        _mainUserMetadataList.clear();
        for (int i = 0; i < mainUserMetadataCollection.size(); i++) {
            _mainUserMetadataList.add((org.riverock.webmill.container.schema.core.MainUserMetadataItemType) mainUserMetadataCollection.get(i));
        }
    }

    /**
     * Method setMainUserMetadataAsReference
     * <p/>
     * Sets the value of 'mainUserMetadata' by setting it to the
     * given ArrayList. No type checking is performed.
     *
     * @param mainUserMetadataCollection the ArrayList to copy.
     */
    public void setMainUserMetadataAsReference(List<MainUserMetadataItemType> mainUserMetadataCollection) {
        _mainUserMetadataList = mainUserMetadataCollection;
    }
}
