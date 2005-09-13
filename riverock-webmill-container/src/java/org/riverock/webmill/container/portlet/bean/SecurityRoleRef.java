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
     * Field _id
     */
    private java.lang.String _id;

    /**
     * Field _descriptionList
     */
    private List<Description> _descriptionList;

    /**
     * Field _roleName
     */
    private java.lang.String _roleName;

    /**
     * Field _roleLink
     */
    private String _roleLink;


    public SecurityRoleRef() {
        super();
        _descriptionList = new ArrayList<Description>();
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
     * Method clearDescription
     */
    public void clearDescription() {
        _descriptionList.clear();
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
     * Returns the value of field 'id'.
     *
     * @return the value of field 'id'.
     */
    public java.lang.String getId() {
        return this._id;
    }

    /**
     * Returns the value of field 'roleLink'.
     *
     * @return the value of field 'roleLink'.
     */
    public String getRoleLink() {
        return this._roleLink;
    }

    /**
     * Returns the value of field 'roleName'.
     *
     * @return the value of field 'roleName'.
     */
    public java.lang.String getRoleName() {
        return this._roleName;
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
     * Sets the value of field 'id'.
     *
     * @param id the value of field 'id'.
     */
    public void setId(java.lang.String id) {
        this._id = id;
    }

    /**
     * Sets the value of field 'roleLink'.
     *
     * @param roleLink the value of field 'roleLink'.
     */
    public void setRoleLink(String roleLink) {
        this._roleLink = roleLink;
    }

    /**
     * Sets the value of field 'roleName'.
     *
     * @param roleName the value of field 'roleName'.
     */
    public void setRoleName(java.lang.String roleName) {
        this._roleName = roleName;
    }
}
