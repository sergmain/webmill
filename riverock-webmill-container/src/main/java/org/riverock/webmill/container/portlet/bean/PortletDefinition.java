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

import org.riverock.webmill.container.portlet.PortletContainer;

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
     * Field descriptionList
     */
    private List<Description> descriptionList;

    /**
     * Field _portletName
     */
    private String portletName;

    /**
     * Field displayNameList
     */
    private List<DisplayName> displayNameList;

    /**
     * Field _portletClass
     */
    private java.lang.String portletClass;

    /**
     * Field initParamList
     */
    private List<InitParam> initParamList;

    /**
     * Field _expirationCache
     */
    private String expirationCache;

    /**
     * Field supportsList
     */
    private List<Supports> supportsList;

    /**
     * Field _supportedLocaleList
     */
    private List<String> supportedLocaleList;

    private String resourceBundle = null;
    private PortletInfo portletInfo = null;

    /**
     * Field portletPreferences
     */
    private PortletPreferences portletPreferences;

    /**
     * Field securityRoleRefList
     */
    private List<SecurityRoleRef> securityRoleRefList;

    private String applicationName = null;

    private String fullPortletName = null;

    public PortletDefinition() {
        super();
        descriptionList = new ArrayList<Description>();
        displayNameList = new ArrayList<DisplayName>();
        initParamList = new ArrayList<InitParam>();
        supportsList = new ArrayList<Supports>();
        supportedLocaleList = new ArrayList<String>();
        securityRoleRefList = new ArrayList<SecurityRoleRef>();
    }

    public void destroy() {
        if (descriptionList !=null) {
            descriptionList.clear();
        }
        if (displayNameList !=null) {
            displayNameList.clear();
        }
        if (initParamList !=null) {
            initParamList.clear();
        }
        if (supportsList !=null) {
            supportsList.clear();
        }
        if (supportedLocaleList!=null) {
            supportedLocaleList.clear();
        }
        if (securityRoleRefList !=null) {
            securityRoleRefList.clear();
        }
        applicationName = null;
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

    public void addDescription(Description vDescription) {
        descriptionList.add(vDescription);
    }

    public void addDescription(int index, Description vDescription) {
        descriptionList.add(index, vDescription);
    }

    public void addDisplayName(DisplayName vDisplayName) {
        displayNameList.add(vDisplayName);
    }

    public void addDisplayName(int index, DisplayName vDisplayName) {
        displayNameList.add(index, vDisplayName);
    }

    /**
     * Method addInitParam
     *
     * @param vInitParam
     */
    public void addInitParam(InitParam vInitParam) {
        initParamList.add(vInitParam);
    }

    /**
     * Method addInitParam
     *
     * @param index
     * @param vInitParam
     */
    public void addInitParam(int index, InitParam vInitParam) {
        initParamList.add(index, vInitParam);
    }

    /**
     * Method addSecurityRoleRef
     *
     * @param vSecurityRoleRef
     */
    public void addSecurityRoleRef(SecurityRoleRef vSecurityRoleRef) {
        securityRoleRefList.add(vSecurityRoleRef);
    }

    /**
     * Method addSecurityRoleRef
     *
     * @param index
     * @param vSecurityRoleRef
     */
    public void addSecurityRoleRef(int index, SecurityRoleRef vSecurityRoleRef) {
        securityRoleRefList.add(index, vSecurityRoleRef);
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
        supportsList.add(vSupports);
    }

    /**
     * Method addSupports
     *
     * @param index
     * @param vSupports
     */
    public void addSupports(int index, Supports vSupports) {
        supportsList.add(index, vSupports);
    }

    /**
     * Method clearDescription
     */
    public void clearDescription() {
        descriptionList.clear();
    }

    /**
     * Method clearDisplayName
     */
    public void clearDisplayName() {
        displayNameList.clear();
    }

    /**
     * Method clearInitParam
     */
    public void clearInitParam() {
        initParamList.clear();
    }

    /**
     * Method clearSecurityRoleRef
     */
    public void clearSecurityRoleRef() {
        securityRoleRefList.clear();
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
        supportsList.clear();
    }

    /**
     * Method getDescription
     *
     * @param index
     */
    public Description getDescription(int index) {
        //-- check bounds for index
        if ((index < 0) || (index > descriptionList.size())) {
            throw new IndexOutOfBoundsException();
        }

        return (Description) descriptionList.get(index);
    }

    /**
     * Method getDescription
     */
    public Description[] getDescription() {
        int size = descriptionList.size();
        Description[] mArray = new Description[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (Description) descriptionList.get(index);
        }
        return mArray;
    }

    /**
     * Method getDescriptionCount
     */
    public int getDescriptionCount() {
        return descriptionList.size();
    }

    /**
     * Method getDisplayName
     *
     * @param index
     */
    public DisplayName getDisplayName(int index) {
        //-- check bounds for index
        if ((index < 0) || (index > displayNameList.size())) {
            throw new IndexOutOfBoundsException();
        }

        return (DisplayName) displayNameList.get(index);
    }

    /**
     * Method getDisplayName
     */
    public DisplayName[] getDisplayName() {
        int size = displayNameList.size();
        DisplayName[] mArray = new DisplayName[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (DisplayName) displayNameList.get(index);
        }
        return mArray;
    }

    /**
     * Method getDisplayNameCount
     */
    public int getDisplayNameCount() {
        return displayNameList.size();
    }

    /**
     * Returns the value of field 'expirationCache'.
     *
     * @return the value of field 'expirationCache'.
     */
    public String getExpirationCache() {
        return this.expirationCache;
    }

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
        if ((index < 0) || (index > initParamList.size())) {
            throw new IndexOutOfBoundsException();
        }

        return (InitParam) initParamList.get(index);
    }

    /**
     * Method getInitParam
     */
    public InitParam[] getInitParam() {
        int size = initParamList.size();
        InitParam[] mArray = new InitParam[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (InitParam) initParamList.get(index);
        }
        return mArray;
    }

    /**
     * Method getInitParam
     */
    public List<InitParam> getInitParamList() {
        if (initParamList==null) {
             initParamList=new ArrayList<InitParam>();
        }
        return initParamList;
    }

    /**
     * Method getInitParamCount
     */
    public int getInitParamCount() {
        return initParamList.size();
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
        return this.portletPreferences;
    }

    /**
     * Method getSecurityRoleRef
     *
     * @param index
     */
//    public SecurityRoleRef getSecurityRoleRef(int index) {
//        //-- check bounds for index
//        if ((index < 0) || (index > securityRoleRefList.size())) {
//            throw new IndexOutOfBoundsException();
//        }
//
//        return (SecurityRoleRef) securityRoleRefList.get(index);
//    }

    /**
     * Method getSecurityRoleRef
     */
//    public SecurityRoleRef[] getSecurityRoleRef() {
//        int size = securityRoleRefList.size();
//        SecurityRoleRef[] mArray = new SecurityRoleRef[size];
//        for (int index = 0; index < size; index++) {
//            mArray[index] = (SecurityRoleRef) securityRoleRefList.get(index);
//        }
//        return mArray;
//    }

    public List<SecurityRoleRef> getSecurityRoleRefList() {
        if (securityRoleRefList==null)
            return new ArrayList<SecurityRoleRef>();

        return securityRoleRefList;
    }

    /**
     * Method getSupportedLocale
     *
     * @param index
     */
//    public String getSupportedLocale(int index) {
//        //-- check bounds for index
//        if ((index < 0) || (index > supportedLocaleList.size())) {
//            throw new IndexOutOfBoundsException();
//        }
//
//        return supportedLocaleList.get(index);
//    }

    /**
     * Method getSupportedLocale
     */
//    public String[] getSupportedLocale() {
//        int size = supportedLocaleList.size();
//        String[] mArray = new String[size];
//        for (int index = 0; index < size; index++) {
//            mArray[index] = supportedLocaleList.get(index);
//        }
//        return mArray;
//    }

    /**
     * Method getSupportedLocaleCount
     */
    public List<String> getSupportedLocaleList() {
        if (supportedLocaleList==null)
            return new ArrayList<String>();

        return supportedLocaleList;
    }

    /**
     * Method getSupports
     *
     * @param index
     */
/*
    public Supports getSupports(int index) {
        //-- check bounds for index
        if ((index < 0) || (index > supportsList.size())) {
            throw new IndexOutOfBoundsException();
        }

        return (Supports) supportsList.get(index);
    }
*/

    /**
     * Method getSupports
     */
/*
    public Supports[] getSupports() {
        int size = supportsList.size();
        Supports[] mArray = new Supports[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (Supports) supportsList.get(index);
        }
        return mArray;
    }
*/
    public List<Supports> getSupports() {
        if (supportsList ==null)
            return new ArrayList<Supports>();

        return supportsList;
    }

    /**
     * Method getSupportsCount
     */
//    public int getSupportsCount() {
//        return supportsList.size();
//    }

    /**
     * Method removeDescription
     *
     * @param vDescription
     */
    public boolean removeDescription(Description vDescription) {
        boolean removed = descriptionList.remove(vDescription);
        return removed;
    }

    /**
     * Method removeDisplayName
     *
     * @param vDisplayName
     */
    public boolean removeDisplayName(DisplayName vDisplayName) {
        boolean removed = displayNameList.remove(vDisplayName);
        return removed;
    }

    /**
     * Method removeInitParam
     *
     * @param vInitParam
     */
    public boolean removeInitParam(InitParam vInitParam) {
        boolean removed = initParamList.remove(vInitParam);
        return removed;
    }

    /**
     * Method removeSecurityRoleRef
     *
     * @param vSecurityRoleRef
     */
    public boolean removeSecurityRoleRef(SecurityRoleRef vSecurityRoleRef) {
        boolean removed = securityRoleRefList.remove(vSecurityRoleRef);
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
        boolean removed = supportsList.remove(vSupports);
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
        if ((index < 0) || (index > descriptionList.size())) {
            throw new IndexOutOfBoundsException();
        }
        descriptionList.set(index, vDescription);
    }

    /**
     * Method setDescription
     *
     * @param descriptionArray
     */
    public void setDescription(Description[] descriptionArray) {
        //-- copy array
        descriptionList.clear();
        for (final Description newVar : descriptionArray) {
            descriptionList.add(newVar);
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
        if ((index < 0) || (index > displayNameList.size())) {
            throw new IndexOutOfBoundsException();
        }
        displayNameList.set(index, vDisplayName);
    }

    /**
     * Method setDisplayName
     *
     * @param displayNameArray
     */
    public void setDisplayName(DisplayName[] displayNameArray) {
        //-- copy array
        displayNameList.clear();
        for (final DisplayName newVar : displayNameArray) {
            displayNameList.add(newVar);
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
        initFullPortletName();
    }

    /**
     * Method setInitParam
     *
     * @param index
     * @param vInitParam
     */
    public void setInitParam(int index, InitParam vInitParam) {
        //-- check bounds for index
        if ((index < 0) || (index > initParamList.size())) {
            throw new IndexOutOfBoundsException();
        }
        initParamList.set(index, vInitParam);
    }

    /**
     * Method setInitParam
     *
     * @param initParamArray
     */
    public void setInitParam(InitParam[] initParamArray) {
        //-- copy array
        initParamList.clear();
        for (final InitParam newVar : initParamArray) {
            initParamList.add(newVar);
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
        initFullPortletName();
    }

    /**
     * Sets the value of field 'portletPreferences'.
     *
     * @param portletPreferences the value of field
     *                           'portletPreferences'.
     */
    public void setPortletPreferences(PortletPreferences portletPreferences) {
        this.portletPreferences = portletPreferences;
    }

    /**
     * Method setSecurityRoleRef
     *
     * @param index
     * @param vSecurityRoleRef
     */
    public void setSecurityRoleRef(int index, SecurityRoleRef vSecurityRoleRef) {
        //-- check bounds for index
        if ((index < 0) || (index > securityRoleRefList.size())) {
            throw new IndexOutOfBoundsException();
        }
        securityRoleRefList.set(index, vSecurityRoleRef);
    }

    /**
     * Method setSecurityRoleRef
     *
     * @param securityRoleRefArray
     */
    public void setSecurityRoleRef(SecurityRoleRef[] securityRoleRefArray) {
        //-- copy array
        securityRoleRefList.clear();
        for (final SecurityRoleRef newVar : securityRoleRefArray) {
            securityRoleRefList.add(newVar);
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

    public void setSupportedLocale(String[] supportedLocaleArray) {
        supportedLocaleList.clear();
        for (final String newVar : supportedLocaleArray) {
            supportedLocaleList.add(newVar);
        }
    }

    public void setSupports(int index, Supports vSupports) {
        //-- check bounds for index
        if ((index < 0) || (index > supportsList.size())) {
            throw new IndexOutOfBoundsException();
        }
        supportsList.set(index, vSupports);
    }

    /**
     * Method setSupports
     *
     * @param supportsArray
     */
    public void setSupports(Supports[] supportsArray) {
        //-- copy array
        supportsList.clear();
        for (final Supports newVar : supportsArray) {
            supportsList.add(newVar);
        }
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        if (applicationName==null) {
            throw new IllegalStateException( "portlet aplication name can not be null");
        }

        this.applicationName = applicationName;
        initFullPortletName();
    }

    public String getFullPortletName() {
        return fullPortletName;
    }

    public void setFullPortletName(String fullPortletName) {
        this.fullPortletName = fullPortletName;
    }

    private void initFullPortletName() {
        if (applicationName==null && id==null) {
            this.fullPortletName = PortletContainer.PORTLET_ID_NAME_SEPARATOR + portletName;
            return;
        }

        if (applicationName!=null && id==null) {
            this.fullPortletName = applicationName + PortletContainer.PORTLET_ID_NAME_SEPARATOR + portletName;
            return;
        }

        if (applicationName==null && id!=null) {
            this.fullPortletName =
                PortletContainer.PORTLET_ID_NAME_SEPARATOR + portletName +
                PortletContainer.PORTLET_ID_NAME_SEPARATOR + id;
            return;
        }

        this.fullPortletName =
            applicationName + PortletContainer.PORTLET_ID_NAME_SEPARATOR +
            portletName + PortletContainer.PORTLET_ID_NAME_SEPARATOR +
            id;
    }
}
