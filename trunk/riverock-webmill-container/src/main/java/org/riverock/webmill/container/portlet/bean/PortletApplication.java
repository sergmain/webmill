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
package org.riverock.webmill.container.portlet.bean;

import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;

/**
 * @author Serge Maslyukov
 *         Created 06.08.2005
 *         Class PortletAppType.
 * @version $Revision$ $Date$
 */
public class PortletApplication implements Serializable {
    private static final long serialVersionUID = 30434672384237148L;

    /**
     * Field version
     */
    protected java.lang.String version;

    /**
     * Field _id
     */
    protected java.lang.String id;

    /**
     * Field portlet
     */
    protected List<PortletDefinition> portlet;

    /**
     * Field customPortletMode
     */
    protected List<CustomPortletMode> customPortletMode;

    /**
     * Field customWindowState
     */
    private List<CustomWindowState> customWindowState;

    /**
     * Field userAttribute
     */
    private List<UserAttribute> userAttribute;

    /**
     * Field securityConstraint
     */
    private List<SecurityConstraint> securityConstraint;


    public PortletApplication() {
        super();
        portlet = new ArrayList<PortletDefinition>();
        customPortletMode = new ArrayList<CustomPortletMode>();
        customWindowState = new ArrayList<CustomWindowState>();
        userAttribute = new ArrayList<UserAttribute>();
        securityConstraint = new ArrayList<SecurityConstraint>();
    }


    /**
     * Method addCustomPortletMode
     *
     * @param vCustomPortletMode
     */
    public void addCustomPortletMode(CustomPortletMode vCustomPortletMode) {
        customPortletMode.add(vCustomPortletMode);
    }

    /**
     * Method addCustomWindowState
     *
     * @param vCustomWindowState
     */
    public void addCustomWindowState(CustomWindowState vCustomWindowState) {
        customWindowState.add(vCustomWindowState);
    }

    /**
     * Method addPortlet
     *
     * @param vPortlet
     */
    public void addPortlet(PortletDefinition vPortlet) {
        portlet.add(vPortlet);
    }

    /**
     * Method addSecurityConstraint
     *
     * @param vSecurityConstraint
     */
    public void addSecurityConstraint(SecurityConstraint vSecurityConstraint) {
        securityConstraint.add(vSecurityConstraint);
    }

    /**
     * Method addUserAttribute
     *
     * @param vUserAttribute
     */
    public void addUserAttribute(UserAttribute vUserAttribute) {
        userAttribute.add(vUserAttribute);
    }

    /**
     * Method getCustomPortletMode
     */
    public List<CustomPortletMode> getCustomPortletMode() {
        return customPortletMode;
    }

    /**
     * Method getCustomWindowState
     */
    public List<CustomWindowState> getCustomWindowState() {
        return customWindowState;
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
     * Method getPortlet
     * @return List<PortletDefinition>
     */
    public List<PortletDefinition> getPortlet() {
        if (portlet==null) {
            portlet=new ArrayList<PortletDefinition>();
        }
        return portlet;
    }

    /**
     * Method getSecurityConstraint
     * @return List<SecurityConstraint>
     */
    public List<SecurityConstraint> getSecurityConstraint() {
        if (securityConstraint==null) {
            securityConstraint=new ArrayList<SecurityConstraint>();
        }
        return securityConstraint;
    }

    /**
     * Method getUserAttribute
     * @return List<UserAttribute>
     */
    public List<UserAttribute> getUserAttribute() {
        if (userAttribute==null) {
            userAttribute=new ArrayList<UserAttribute>();
        }
        return userAttribute;
    }

    /**
     * Returns the value of field 'version'.
     *
     * @return the value of field 'version'.
     */
    public java.lang.String getVersion() {
        return this.version;
    }

    /**
     * Method setCustomPortletMode
     *
     * @param customPortletMode
     */
    public void setCustomPortletMode(List<CustomPortletMode> customPortletMode) {
        this.customPortletMode=customPortletMode;
    }

    /**
     * Method setCustomWindowState
     *
     * @param customWindowState
     */
    public void setCustomWindowState(List<CustomWindowState> customWindowState) {
        this.customWindowState=customWindowState;
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
     * Method setPortlet
     *
     * @param portlet
     */
    public void setPortlet(List<PortletDefinition> portlet) {
        this.portlet=portlet;
    }

    /**
     * Method setSecurityConstraint
     *
     * @param securityConstraint
     */
    public void setSecurityConstraint(List<SecurityConstraint> securityConstraint) {
        this.securityConstraint=securityConstraint;
    }

    /**
     * Method setUserAttribute
     *
     * @param userAttribute
     */
    public void setUserAttribute(List<UserAttribute> userAttribute) {
        this.userAttribute=userAttribute;
    }

    /**
     * Sets the value of field 'version'.
     *
     * @param version the value of field 'version'.
     */
    public void setVersion(java.lang.String version) {
        this.version = version;
    }
}
