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
