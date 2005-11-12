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
package org.riverock.webmill.container.portlet.bean;

import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;

import javax.portlet.PortletPreferences;

/**
 * @author Serge Maslyukov
 *         Created 06.08.2005
 *         The portlet element contains the declarative data of a portlet.
 * @version $Revision$ $Date$
 */
public class PortletDefinition implements Serializable {
    private static final long serialVersionUID = 30434672384237154L;

    /**
     * Field _id
     */
    private java.lang.String id;

    /**
     * Field _descriptionList
     */
    private List<Description> _descriptionList;

    /**
     * Field _portletName
     */
    private String portletName;

    /**
     * Field _displayNameList
     */
    private List<DisplayName> _displayNameList;

    /**
     * Field _portletClass
     */
    private java.lang.String portletClass;

    /**
     * Field _initParamList
     */
    private List<InitParam> _initParamList;

    /**
     * Field _expirationCache
     */
    private String expirationCache;

    /**
     * Field _supportsList
     */
    private List<Supports> _supportsList;

    /**
     * Field _supportedLocaleList
     */
    private List<String> supportedLocaleList;

    private String resourceBundle = null;
    private PortletInfo portletInfo = null;

    /**
     * Field _portletPreferences
     */
    private PortletPreferences _portletPreferences;

    /**
     * Field _securityRoleRefList
     */
    private List<SecurityRoleRef> _securityRoleRefList;

    public PortletDefinition() {
        super();
        _descriptionList = new ArrayList<Description>();
        _displayNameList = new ArrayList<DisplayName>();
        _initParamList = new ArrayList<InitParam>();
        _supportsList = new ArrayList<Supports>();
        supportedLocaleList = new ArrayList<String>();
        _securityRoleRefList = new ArrayList<SecurityRoleRef>();
    }

    public void destroy() {
        if (_descriptionList!=null) {
            _descriptionList.clear();
        }
        if (_displayNameList!=null) {
            _displayNameList.clear();
        }
        if (_initParamList!=null) {
            _initParamList.clear();
        }
        if (_supportsList!=null) {
            _supportsList.clear();
        }
        if (supportedLocaleList!=null) {
            supportedLocaleList.clear();
        }
        if (_securityRoleRefList!=null) {
            _securityRoleRefList.clear();
        }
    }

    public String getResourceBundle() {
        return resourceBundle;
    }

    public void setResourceBundle(String resourceBundle) {
        this.resourceBundle = resourceBundle;
    }

    public PortletInfo getPortletInfo() {
        return portletInfo;
    }

    public void setPortletInfo(PortletInfo portletInfo) {
        this.portletInfo = portletInfo;
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
     * Method addDisplayName
     *
     * @param vDisplayName
     */
    public void addDisplayName(DisplayName vDisplayName) {
        _displayNameList.add(vDisplayName);
    }

    /**
     * Method addDisplayName
     *
     * @param index
     * @param vDisplayName
     */
    public void addDisplayName(int index, DisplayName vDisplayName) {
        _displayNameList.add(index, vDisplayName);
    }

    /**
     * Method addInitParam
     *
     * @param vInitParam
     */
    public void addInitParam(InitParam vInitParam) {
        _initParamList.add(vInitParam);
    }

    /**
     * Method addInitParam
     *
     * @param index
     * @param vInitParam
     */
    public void addInitParam(int index, InitParam vInitParam) {
        _initParamList.add(index, vInitParam);
    }

    /**
     * Method addSecurityRoleRef
     *
     * @param vSecurityRoleRef
     */
    public void addSecurityRoleRef(SecurityRoleRef vSecurityRoleRef) {
        _securityRoleRefList.add(vSecurityRoleRef);
    }

    /**
     * Method addSecurityRoleRef
     *
     * @param index
     * @param vSecurityRoleRef
     */
    public void addSecurityRoleRef(int index, SecurityRoleRef vSecurityRoleRef) {
        _securityRoleRefList.add(index, vSecurityRoleRef);
    }

    /**
     * Method addSupportedLocale
     *
     * @param vSupportedLocale
     */
    public void addSupportedLocale(String vSupportedLocale) {
        supportedLocaleList.add(vSupportedLocale);
    }

    /**
     * Method addSupportedLocale
     *
     * @param index
     * @param vSupportedLocale
     */
    public void addSupportedLocale(int index, String vSupportedLocale) {
        supportedLocaleList.add(index, vSupportedLocale);
    }

    /**
     * Method addSupports
     *
     * @param vSupports
     */
    public void addSupports(Supports vSupports) {
        _supportsList.add(vSupports);
    }

    /**
     * Method addSupports
     *
     * @param index
     * @param vSupports
     */
    public void addSupports(int index, Supports vSupports) {
        _supportsList.add(index, vSupports);
    }

    /**
     * Method clearDescription
     */
    public void clearDescription() {
        _descriptionList.clear();
    }

    /**
     * Method clearDisplayName
     */
    public void clearDisplayName() {
        _displayNameList.clear();
    }

    /**
     * Method clearInitParam
     */
    public void clearInitParam() {
        _initParamList.clear();
    }

    /**
     * Method clearSecurityRoleRef
     */
    public void clearSecurityRoleRef() {
        _securityRoleRefList.clear();
    }

    /**
     * Method clearSupportedLocale
     */
    public void clearSupportedLocale() {
        supportedLocaleList.clear();
    }

    /**
     * Method clearSupports
     */
    public void clearSupports() {
        _supportsList.clear();
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
            mArray[index] = (Description) _descriptionList.get(index);
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
     * Method getDisplayName
     *
     * @param index
     */
    public DisplayName getDisplayName(int index) {
        //-- check bounds for index
        if ((index < 0) || (index > _displayNameList.size())) {
            throw new IndexOutOfBoundsException();
        }

        return (DisplayName) _displayNameList.get(index);
    }

    /**
     * Method getDisplayName
     */
    public DisplayName[] getDisplayName() {
        int size = _displayNameList.size();
        DisplayName[] mArray = new DisplayName[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (DisplayName) _displayNameList.get(index);
        }
        return mArray;
    }

    /**
     * Method getDisplayNameCount
     */
    public int getDisplayNameCount() {
        return _displayNameList.size();
    }

    /**
     * Returns the value of field 'expirationCache'.
     *
     * @return the value of field 'expirationCache'.
     */
    public String getExpirationCache() {
        return this.expirationCache;
    }

    /**
     * Returns the value of field 'id'.
     *
     * @return the value of field 'id'.
     */
    public java.lang.String getId() {
        return this.id;
    }

    /**
     * Method getInitParam
     *
     * @param index
     */
    public InitParam getInitParam(int index) {
        //-- check bounds for index
        if ((index < 0) || (index > _initParamList.size())) {
            throw new IndexOutOfBoundsException();
        }

        return (InitParam) _initParamList.get(index);
    }

    /**
     * Method getInitParam
     */
    public InitParam[] getInitParam() {
        int size = _initParamList.size();
        InitParam[] mArray = new InitParam[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (InitParam) _initParamList.get(index);
        }
        return mArray;
    }

    /**
     * Method getInitParamCount
     */
    public int getInitParamCount() {
        return _initParamList.size();
    }

    /**
     * Returns the value of field 'portletClass'.
     *
     * @return the value of field 'portletClass'.
     */
    public java.lang.String getPortletClass() {
        return this.portletClass;
    }

    /**
     * Returns the value of field 'portletName'.
     *
     * @return the value of field 'portletName'.
     */
    public String getPortletName() {
        return this.portletName;
    }

    /**
     * Returns the value of field 'portletPreferences'.
     *
     * @return the value of field 'portletPreferences'.
     */
    public PortletPreferences getPortletPreferences() {
        return this._portletPreferences;
    }

    /**
     * Method getSecurityRoleRef
     *
     * @param index
     */
    public SecurityRoleRef getSecurityRoleRef(int index) {
        //-- check bounds for index
        if ((index < 0) || (index > _securityRoleRefList.size())) {
            throw new IndexOutOfBoundsException();
        }

        return (SecurityRoleRef) _securityRoleRefList.get(index);
    }

    /**
     * Method getSecurityRoleRef
     */
    public SecurityRoleRef[] getSecurityRoleRef() {
        int size = _securityRoleRefList.size();
        SecurityRoleRef[] mArray = new SecurityRoleRef[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (SecurityRoleRef) _securityRoleRefList.get(index);
        }
        return mArray;
    }

    /**
     * Method getSecurityRoleRefCount
     */
    public int getSecurityRoleRefCount() {
        return _securityRoleRefList.size();
    }

    /**
     * Method getSupportedLocale
     *
     * @param index
     */
    public String getSupportedLocale(int index) {
        //-- check bounds for index
        if ((index < 0) || (index > supportedLocaleList.size())) {
            throw new IndexOutOfBoundsException();
        }

        return supportedLocaleList.get(index);
    }

    /**
     * Method getSupportedLocale
     */
    public String[] getSupportedLocale() {
        int size = supportedLocaleList.size();
        String[] mArray = new String[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = supportedLocaleList.get(index);
        }
        return mArray;
    }

    /**
     * Method getSupportedLocaleCount
     */
    public int getSupportedLocaleCount() {
        return supportedLocaleList.size();
    }

    /**
     * Method getSupports
     *
     * @param index
     */
    public Supports getSupports(int index) {
        //-- check bounds for index
        if ((index < 0) || (index > _supportsList.size())) {
            throw new IndexOutOfBoundsException();
        }

        return (Supports) _supportsList.get(index);
    }

    /**
     * Method getSupports
     */
    public Supports[] getSupports() {
        int size = _supportsList.size();
        Supports[] mArray = new Supports[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (Supports) _supportsList.get(index);
        }
        return mArray;
    }

    /**
     * Method getSupportsCount
     */
    public int getSupportsCount() {
        return _supportsList.size();
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
     * Method removeDisplayName
     *
     * @param vDisplayName
     */
    public boolean removeDisplayName(DisplayName vDisplayName) {
        boolean removed = _displayNameList.remove(vDisplayName);
        return removed;
    }

    /**
     * Method removeInitParam
     *
     * @param vInitParam
     */
    public boolean removeInitParam(InitParam vInitParam) {
        boolean removed = _initParamList.remove(vInitParam);
        return removed;
    }

    /**
     * Method removeSecurityRoleRef
     *
     * @param vSecurityRoleRef
     */
    public boolean removeSecurityRoleRef(SecurityRoleRef vSecurityRoleRef) {
        boolean removed = _securityRoleRefList.remove(vSecurityRoleRef);
        return removed;
    }

    /**
     * Method removeSupportedLocale
     *
     * @param vSupportedLocale
     */
    public boolean removeSupportedLocale(String vSupportedLocale) {
        boolean removed = supportedLocaleList.remove(vSupportedLocale);
        return removed;
    }

    /**
     * Method removeSupports
     *
     * @param vSupports
     */
    public boolean removeSupports(Supports vSupports) {
        boolean removed = _supportsList.remove(vSupports);
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
     * Method setDisplayName
     *
     * @param index
     * @param vDisplayName
     */
    public void setDisplayName(int index, DisplayName vDisplayName) {
        //-- check bounds for index
        if ((index < 0) || (index > _displayNameList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _displayNameList.set(index, vDisplayName);
    }

    /**
     * Method setDisplayName
     *
     * @param displayNameArray
     */
    public void setDisplayName(DisplayName[] displayNameArray) {
        //-- copy array
        _displayNameList.clear();
        for (final DisplayName newVar : displayNameArray) {
            _displayNameList.add(newVar);
        }
    }

    /**
     * Sets the value of field 'expirationCache'.
     *
     * @param expirationCache the value of field 'expirationCache'.
     */
    public void setExpirationCache(String expirationCache) {
        this.expirationCache = expirationCache;
    }

    /**
     * Sets the value of field 'id'.
     *
     * @param id the value of field 'id'.
     */
    public void setId(java.lang.String id) {
        this.id = id;
    }

    /**
     * Method setInitParam
     *
     * @param index
     * @param vInitParam
     */
    public void setInitParam(int index, InitParam vInitParam) {
        //-- check bounds for index
        if ((index < 0) || (index > _initParamList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _initParamList.set(index, vInitParam);
    }

    /**
     * Method setInitParam
     *
     * @param initParamArray
     */
    public void setInitParam(InitParam[] initParamArray) {
        //-- copy array
        _initParamList.clear();
        for (final InitParam newVar : initParamArray) {
            _initParamList.add(newVar);
        }
    }

    /**
     * Sets the value of field 'portletClass'.
     *
     * @param portletClass the value of field 'portletClass'.
     */
    public void setPortletClass(java.lang.String portletClass) {
        this.portletClass = portletClass;
    }

    /**
     * Sets the value of field 'portletName'.
     *
     * @param portletName the value of field 'portletName'.
     */
    public void setPortletName(String portletName) {
        this.portletName = portletName;
    }

    /**
     * Sets the value of field 'portletPreferences'.
     *
     * @param portletPreferences the value of field
     *                           'portletPreferences'.
     */
    public void setPortletPreferences(PortletPreferences portletPreferences) {
        this._portletPreferences = portletPreferences;
    }

    /**
     * Method setSecurityRoleRef
     *
     * @param index
     * @param vSecurityRoleRef
     */
    public void setSecurityRoleRef(int index, SecurityRoleRef vSecurityRoleRef) {
        //-- check bounds for index
        if ((index < 0) || (index > _securityRoleRefList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _securityRoleRefList.set(index, vSecurityRoleRef);
    }

    /**
     * Method setSecurityRoleRef
     *
     * @param securityRoleRefArray
     */
    public void setSecurityRoleRef(SecurityRoleRef[] securityRoleRefArray) {
        //-- copy array
        _securityRoleRefList.clear();
        for (final SecurityRoleRef newVar : securityRoleRefArray) {
            _securityRoleRefList.add(newVar);
        }
    }

    /**
     * Method setSupportedLocale
     *
     * @param index
     * @param vSupportedLocale
     */
    public void setSupportedLocale(int index, String vSupportedLocale) {
        //-- check bounds for index
        if ((index < 0) || (index > supportedLocaleList.size())) {
            throw new IndexOutOfBoundsException();
        }
        supportedLocaleList.set(index, vSupportedLocale);
    }

    /**
     * Method setSupportedLocale
     *
     * @param supportedLocaleArray
     */
    public void setSupportedLocale(String[] supportedLocaleArray) {
        //-- copy array
        supportedLocaleList.clear();
        for (final String newVar : supportedLocaleArray) {
            supportedLocaleList.add(newVar);
        }
    }

    /**
     * Method setSupports
     *
     * @param index
     * @param vSupports
     */
    public void setSupports(int index, Supports vSupports) {
        //-- check bounds for index
        if ((index < 0) || (index > _supportsList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _supportsList.set(index, vSupports);
    }

    /**
     * Method setSupports
     *
     * @param supportsArray
     */
    public void setSupports(Supports[] supportsArray) {
        //-- copy array
        _supportsList.clear();
        for (final Supports newVar : supportsArray) {
            _supportsList.add(newVar);
        }
    }
}
