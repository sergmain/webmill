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

import org.riverock.webmill.container.portlet.PortletContainer;

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlType;

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
     * Field description
     */
    private List<Description> description;

    /**
     * Field _portletName
     */
    private String portletName;

    /**
     * Field displayName
     */
    private List<DisplayName> displayName;

    /**
     * Field _portletClass
     */
    private java.lang.String portletClass;

    /**
     * Field initParam
     */
    private List<InitParam> initParam;

    /**
     * Field _expirationCache
     */
    private Integer expirationCache=null;

    /**
     * Field supports
     */
    private List<Supports> supports;

    /**
     * Field _supportedLocaleList
     */
    private List<String> supportedLocale;

    private String resourceBundle = null;
    private PortletInfo portletInfo = null;

    /**
     * Field portletPreferences
     */
    private Preferences preferences;

    /**
     * Field securityRoleRefList
     */
    private List<SecurityRoleRef> securityRoleRefList;

    private String applicationName = null;

    private String fullPortletName = null;

    public PortletDefinition() {
        super();
        description = new ArrayList<Description>();
        displayName = new ArrayList<DisplayName>();
        initParam = new ArrayList<InitParam>();
        supports = new ArrayList<Supports>();
        supportedLocale = new ArrayList<String>();
        securityRoleRefList = new ArrayList<SecurityRoleRef>();
    }

    public void destroy() {
        if (description !=null) {
            description.clear();
        }
        if (displayName !=null) {
            displayName.clear();
        }
        if (initParam !=null) {
            initParam.clear();
        }
        if (supports !=null) {
            supports.clear();
        }
        if (supportedLocale !=null) {
            supportedLocale.clear();
        }
        if (securityRoleRefList !=null) {
            securityRoleRefList.clear();
        }
        applicationName = null;
    }

    public Preferences getPreferences() {
        return preferences;
    }

    public void setPreferences(Preferences preferences) {
        this.preferences = preferences;
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
        description.add(vDescription);
    }

    public void addDisplayName(DisplayName vDisplayName) {
        displayName.add(vDisplayName);
    }

    /**
     * Method addInitParam
     *
     * @param vInitParam
     */
    public void addInitParam(InitParam vInitParam) {
        initParam.add(vInitParam);
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
     * Method addSupportedLocale
     *
     * @param vSupportedLocale
     */
    public void addSupportedLocale(String vSupportedLocale) {
        supportedLocale.add(vSupportedLocale);
    }

    /**
     * Method addSupports
     *
     * @param vSupports
     */
    public void addSupports(Supports vSupports) {
        supports.add(vSupports);
    }

    /**
     * Method getDescription
     */
    public List<Description> getDescription() {
        return description;
    }

    /**
     * Method getDisplayName
     */
    public List<DisplayName> getDisplayName() {
        return displayName;
    }

    /**
     * Returns the value of field 'expirationCache'.
     *
     * @return the value of field 'expirationCache'.
     */
    public Integer getExpirationCache() {
        return this.expirationCache;
    }

    public java.lang.String getId() {
        return this.id;
    }

    /**
     * Method getInitParam
     */
    public List<InitParam> getInitParam() {
        if (initParam ==null) {
             initParam =new ArrayList<InitParam>();
        }
        return initParam;
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

    public List<SecurityRoleRef> getSecurityRoleRef() {
        if (securityRoleRefList==null)
            return new ArrayList<SecurityRoleRef>();

        return securityRoleRefList;
    }

    /**
     * Method getSupportedLocaleCount
     */
    public List<String> getSupportedLocale() {
        if (supportedLocale ==null)
            return new ArrayList<String>();

        return supportedLocale;
    }

    public List<Supports> getSupports() {
        if (supports ==null)
            return new ArrayList<Supports>();

        return supports;
    }

    /**
     * Method setDescription
     *
     * @param description
     */
    public void setDescription(List<Description> description) {
        this.description=description;
    }

    /**
     * Method setDisplayName
     *
     * @param displayName
     */
    public void setDisplayName(List<DisplayName> displayName) {
        this.displayName=displayName;
    }

    /**
     * Sets the value of field 'expirationCache'.
     *
     * @param expirationCache the value of field 'expirationCache'.
     */
    public void setExpirationCache(Integer expirationCache) {
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
     * @param initParam
     */
    public void setInitParam(List<InitParam> initParam) {
        this.initParam=initParam;
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
     * Method setSecurityRoleRef
     *
     * @param securityRoleRef
     */
    public void setSecurityRoleRef(List<SecurityRoleRef> securityRoleRef) {
        this.securityRoleRefList=securityRoleRef;
    }

    public void setSupportedLocale(List<String> supportedLocale) {
        this.supportedLocale=supportedLocale;
    }

    /**
     * Method setSupports
     *
     * @param supports
     */
    public void setSupports(List<Supports> supports) {
        this.supports=supports;    }

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
