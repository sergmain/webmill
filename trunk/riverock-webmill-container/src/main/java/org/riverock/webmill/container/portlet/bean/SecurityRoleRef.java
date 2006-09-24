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
 * The security-role-ref element contains the declaration of a
 * security role reference in the code of the web application. The
 * <p/>
 * declaration consists of an optional description, the security
 * role name used in the code, and an optional link to a security
 * role. If the security role is not specified, the Deployer must
 * choose an appropriate security role.
 * The value of the role name element must be the String used
 * as the parameter to the
 * EJBContext.isCallerInRole(String roleName) method
 * or the HttpServletRequest.isUserInRole(String role) method.
 * Used in: portlet
 *
 * @version $Revision$ $Date$
 */
public class SecurityRoleRef implements Serializable {
    private static final long serialVersionUID = 30434672384237167L;


    /**
     * Field id
     */
    private java.lang.String id;

    /**
     * Field description
     */
    private List<Description> description;

    /**
     * Field roleName
     */
    private java.lang.String roleName;

    /**
     * Field roleLink
     */
    private String roleLink;


    public SecurityRoleRef() {
        super();
        description = new ArrayList<Description>();
    }


    /**
     * Method addDescription
     *
     * @param vDescription
     */
    public void addDescription(Description vDescription) {
        description.add(vDescription);
    }

    /**
     * Method getDescription
     */
    public List<Description> getDescription() {
        return description;
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
     * Returns the value of field 'roleLink'.
     *
     * @return the value of field 'roleLink'.
     */
    public String getRoleLink() {
        return this.roleLink;
    }

    /**
     * Returns the value of field 'roleName'.
     *
     * @return the value of field 'roleName'.
     */
    public java.lang.String getRoleName() {
        return this.roleName;
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
     * Sets the value of field 'id'.
     *
     * @param id the value of field 'id'.
     */
    public void setId(java.lang.String id) {
        this.id = id;
    }

    /**
     * Sets the value of field 'roleLink'.
     *
     * @param roleLink the value of field 'roleLink'.
     */
    public void setRoleLink(String roleLink) {
        this.roleLink = roleLink;
    }

    /**
     * Sets the value of field 'roleName'.
     *
     * @param roleName the value of field 'roleName'.
     */
    public void setRoleName(java.lang.String roleName) {
        this.roleName = roleName;
    }
}
