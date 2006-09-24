/*
 * org.riverock.webmill.container - Webmill portlet container implementation
 *
 * Copyright (C) 2006, Riverock Software, All Rights Reserved.
 *
 * Riverock - The Open-source Java Development Community
 * http://www.riverock.org
 *
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.riverock.webmill.container.bean;

import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;

/**
 * @author smaslyukov
 *         Date: 29.07.2005
 *         Time: 15:41:40
 *         $Id$
 */
public class SitePortletDataList implements Serializable {
    private static final long serialVersionUID = 30434672384237709L;

    private java.util.List<SitePortletData> _portletList = new ArrayList<SitePortletData>();


    public SitePortletDataList() {
    }

    public void addPortlet(SitePortletData vPortlet) {
        _portletList.add(vPortlet);
    }

    /**
     * Method addPortlet
     *
     * @param index
     * @param vPortlet
     */
    public void addPortlet(int index, SitePortletData vPortlet) {
        _portletList.add(index, vPortlet);
    }

    /**
     * Method clearPortlet
     */
    public void clearPortlet() {
        _portletList.clear();
    }

    /**
     * Method getPortlet
     *
     * @param index
     * @return SitePortletData
     */
    public SitePortletData getPortlet(int index) {
        //-- check bounds for index
        if ((index < 0) || (index > _portletList.size())) {
            throw new IndexOutOfBoundsException();
        }

        return (SitePortletData) _portletList.get(index);
    }

    /**
     * Method getPortlet
     *
     * @return SitePortletData
     */
    public SitePortletData[] getPortlet() {
        int size = _portletList.size();
        SitePortletData[] mArray = new SitePortletData[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (SitePortletData) _portletList.get(index);
        }
        return mArray;
    }

    /**
     * Method getPortletAsReference
     * <p/>
     * Returns a reference to 'portlet'. No type checking is
     * performed on any modications to the Collection.
     *
     * @return returns a reference to the Collection.
     */
    public List<SitePortletData> getPortletAsReference() {
        return _portletList;
    }

    /**
     * Method getPortletCount
     *
     * @return int
     */
    public int getPortletCount() {
        return _portletList.size();
    }

    /**
     * Method removePortlet
     *
     * @param vPortlet
     * @return boolean
     */
    public boolean removePortlet(SitePortletData vPortlet) {
        boolean removed = _portletList.remove(vPortlet);
        return removed;
    }

    /**
     * Method setPortlet
     *
     * @param index
     * @param vPortlet
     */
    public void setPortlet(int index, SitePortletData vPortlet) {
        //-- check bounds for index
        if ((index < 0) || (index > _portletList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _portletList.set(index, vPortlet);
    }

    /**
     * Method setPortlet
     *
     * @param portletArray
     */
    public void setPortlet(SitePortletData[] portletArray) {
        _portletList.clear();
        for (final SitePortletData newVar : portletArray) {
            _portletList.add(newVar);
        }
    }

    /**
     * Method setPortlet
     * <p/>
     * Sets the value of 'portlet' by copying the given ArrayList.
     *
     * @param portletCollection the ArrayList to copy.
     */
    public void setPortlet(List<SitePortletData> portletCollection) {
        _portletList.clear();
        for (int i = 0; i < portletCollection.size(); i++) {
            _portletList.add((SitePortletData) portletCollection.get(i));
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
    public void setPortletAsReference(List<SitePortletData> portletCollection) {
        _portletList = portletCollection;
    }
}
