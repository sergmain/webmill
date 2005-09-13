package org.riverock.webmill.container.portlet.bean;

import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;

/**
 * @author Serge Maslyukov
 *         Created 06.08.2005
 *         <p/>
 *         Class PortletAppType.
 * @version $Revision$ $Date$
 */
public class PortletApplication implements Serializable {
    private static final long serialVersionUID = 30434672384237148L;


    /**
     * Field _version
     */
    private java.lang.String _version;

    /**
     * Field _id
     */
    private java.lang.String id;

    /**
     * Field _portletList
     */
    private List<PortletDefinition> _portletList;

    /**
     * Field _customPortletModeList
     */
    private List<CustomPortletModeType> _customPortletModeList;

    /**
     * Field _customWindowStateList
     */
    private List<CustomWindowStateType> _customWindowStateList;

    /**
     * Field _userAttributeList
     */
    private List<UserAttribute> _userAttributeList;

    /**
     * Field _securityConstraintList
     */
    private List<SecurityConstraint> _securityConstraintList;


    public PortletApplication() {
        super();
        _portletList = new ArrayList<PortletDefinition>();
        _customPortletModeList = new ArrayList<CustomPortletModeType>();
        _customWindowStateList = new ArrayList<CustomWindowStateType>();
        _userAttributeList = new ArrayList<UserAttribute>();
        _securityConstraintList = new ArrayList<SecurityConstraint>();
    }


    /**
     * Method addCustomPortletMode
     *
     * @param vCustomPortletMode
     */
    public void addCustomPortletMode(CustomPortletModeType vCustomPortletMode) {
        _customPortletModeList.add(vCustomPortletMode);
    }

    /**
     * Method addCustomPortletMode
     *
     * @param index
     * @param vCustomPortletMode
     */
    public void addCustomPortletMode(int index, CustomPortletModeType vCustomPortletMode) {
        _customPortletModeList.add(index, vCustomPortletMode);
    }

    /**
     * Method addCustomWindowState
     *
     * @param vCustomWindowState
     */
    public void addCustomWindowState(CustomWindowStateType vCustomWindowState) {
        _customWindowStateList.add(vCustomWindowState);
    }

    /**
     * Method addCustomWindowState
     *
     * @param index
     * @param vCustomWindowState
     */
    public void addCustomWindowState(int index, CustomWindowStateType vCustomWindowState) {
        _customWindowStateList.add(index, vCustomWindowState);
    }

    /**
     * Method addPortlet
     *
     * @param vPortlet
     */
    public void addPortlet(PortletDefinition vPortlet) {
        _portletList.add(vPortlet);
    }

    /**
     * Method addPortlet
     *
     * @param index
     * @param vPortlet
     */
    public void addPortlet(int index, PortletDefinition vPortlet) {
        _portletList.add(index, vPortlet);
    }

    /**
     * Method addSecurityConstraint
     *
     * @param vSecurityConstraint
     */
    public void addSecurityConstraint(SecurityConstraint vSecurityConstraint) {
        _securityConstraintList.add(vSecurityConstraint);
    }

    /**
     * Method addSecurityConstraint
     *
     * @param index
     * @param vSecurityConstraint
     */
    public void addSecurityConstraint(int index, SecurityConstraint vSecurityConstraint) {
        _securityConstraintList.add(index, vSecurityConstraint);
    }

    /**
     * Method addUserAttribute
     *
     * @param vUserAttribute
     */
    public void addUserAttribute(UserAttribute vUserAttribute) {
        _userAttributeList.add(vUserAttribute);
    }

    /**
     * Method addUserAttribute
     *
     * @param index
     * @param vUserAttribute
     */
    public void addUserAttribute(int index, UserAttribute vUserAttribute) {
        _userAttributeList.add(index, vUserAttribute);
    }

    /**
     * Method clearCustomPortletMode
     */
    public void clearCustomPortletMode() {
        _customPortletModeList.clear();
    }

    /**
     * Method clearCustomWindowState
     */
    public void clearCustomWindowState() {
        _customWindowStateList.clear();
    }

    /**
     * Method clearPortlet
     */
    public void clearPortlet() {
        _portletList.clear();
    }

    /**
     * Method clearSecurityConstraint
     */
    public void clearSecurityConstraint() {
        _securityConstraintList.clear();
    }

    /**
     * Method clearUserAttribute
     */
    public void clearUserAttribute() {
        _userAttributeList.clear();
    }

    /**
     * Method getCustomPortletMode
     *
     * @param index
     */
    public CustomPortletModeType getCustomPortletMode(int index) {
        //-- check bounds for index
        if ((index < 0) || (index > _customPortletModeList.size())) {
            throw new IndexOutOfBoundsException();
        }

        return (CustomPortletModeType) _customPortletModeList.get(index);
    }

    /**
     * Method getCustomPortletMode
     */
    public CustomPortletModeType[] getCustomPortletMode() {
        int size = _customPortletModeList.size();
        CustomPortletModeType[] mArray = new CustomPortletModeType[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (CustomPortletModeType) _customPortletModeList.get(index);
        }
        return mArray;
    }

    /**
     * Method getCustomPortletModeCount
     */
    public int getCustomPortletModeCount() {
        return _customPortletModeList.size();
    }

    /**
     * Method getCustomWindowState
     *
     * @param index
     */
    public CustomWindowStateType getCustomWindowState(int index) {
        //-- check bounds for index
        if ((index < 0) || (index > _customWindowStateList.size())) {
            throw new IndexOutOfBoundsException();
        }

        return (CustomWindowStateType) _customWindowStateList.get(index);
    }

    /**
     * Method getCustomWindowState
     */
    public CustomWindowStateType[] getCustomWindowState() {
        int size = _customWindowStateList.size();
        CustomWindowStateType[] mArray = new CustomWindowStateType[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (CustomWindowStateType) _customWindowStateList.get(index);
        }
        return mArray;
    }

    /**
     * Method getCustomWindowStateCount
     */
    public int getCustomWindowStateCount() {
        return _customWindowStateList.size();
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
     *
     * @param index
     */
    public PortletDefinition getPortlet(int index) {
        //-- check bounds for index
        if ((index < 0) || (index > _portletList.size())) {
            throw new IndexOutOfBoundsException();
        }

        return (PortletDefinition) _portletList.get(index);
    }

    /**
     * Method getPortlet
     */
    public PortletDefinition[] getPortlet() {
        int size = _portletList.size();
        PortletDefinition[] mArray = new PortletDefinition[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (PortletDefinition) _portletList.get(index);
        }
        return mArray;
    }

    /**
     * Method getPortletCount
     */
    public int getPortletCount() {
        return _portletList.size();
    }

    /**
     * Method getSecurityConstraint
     *
     * @param index
     */
    public SecurityConstraint getSecurityConstraint(int index) {
        //-- check bounds for index
        if ((index < 0) || (index > _securityConstraintList.size())) {
            throw new IndexOutOfBoundsException();
        }

        return (SecurityConstraint) _securityConstraintList.get(index);
    }

    /**
     * Method getSecurityConstraint
     */
    public SecurityConstraint[] getSecurityConstraint() {
        int size = _securityConstraintList.size();
        SecurityConstraint[] mArray = new SecurityConstraint[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (SecurityConstraint) _securityConstraintList.get(index);
        }
        return mArray;
    }

    /**
     * Method getSecurityConstraintCount
     */
    public int getSecurityConstraintCount() {
        return _securityConstraintList.size();
    }

    /**
     * Method getUserAttribute
     *
     * @param index
     */
    public UserAttribute getUserAttribute(int index) {
        //-- check bounds for index
        if ((index < 0) || (index > _userAttributeList.size())) {
            throw new IndexOutOfBoundsException();
        }

        return (UserAttribute) _userAttributeList.get(index);
    }

    /**
     * Method getUserAttribute
     */
    public UserAttribute[] getUserAttribute() {
        int size = _userAttributeList.size();
        UserAttribute[] mArray = new UserAttribute[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (UserAttribute) _userAttributeList.get(index);
        }
        return mArray;
    }

    /**
     * Method getUserAttributeCount
     */
    public int getUserAttributeCount() {
        return _userAttributeList.size();
    }

    /**
     * Returns the value of field 'version'.
     *
     * @return the value of field 'version'.
     */
    public java.lang.String getVersion() {
        return this._version;
    }

    /**
     * Method removeCustomPortletMode
     *
     * @param vCustomPortletMode
     */
    public boolean removeCustomPortletMode(CustomPortletModeType vCustomPortletMode) {
        boolean removed = _customPortletModeList.remove(vCustomPortletMode);
        return removed;
    }

    /**
     * Method removeCustomWindowState
     *
     * @param vCustomWindowState
     */
    public boolean removeCustomWindowState(CustomWindowStateType vCustomWindowState) {
        boolean removed = _customWindowStateList.remove(vCustomWindowState);
        return removed;
    }

    /**
     * Method removePortlet
     *
     * @param vPortlet
     */
    public boolean removePortlet(PortletDefinition vPortlet) {
        boolean removed = _portletList.remove(vPortlet);
        return removed;
    }

    /**
     * Method removeSecurityConstraint
     *
     * @param vSecurityConstraint
     */
    public boolean removeSecurityConstraint(SecurityConstraint vSecurityConstraint) {
        boolean removed = _securityConstraintList.remove(vSecurityConstraint);
        return removed;
    }

    /**
     * Method removeUserAttribute
     *
     * @param vUserAttribute
     */
    public boolean removeUserAttribute(UserAttribute vUserAttribute) {
        boolean removed = _userAttributeList.remove(vUserAttribute);
        return removed;
    }

    /**
     * Method setCustomPortletMode
     *
     * @param index
     * @param vCustomPortletMode
     */
    public void setCustomPortletMode(int index, CustomPortletModeType vCustomPortletMode) {
        //-- check bounds for index
        if ((index < 0) || (index > _customPortletModeList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _customPortletModeList.set(index, vCustomPortletMode);
    }

    /**
     * Method setCustomPortletMode
     *
     * @param customPortletModeArray
     */
    public void setCustomPortletMode(CustomPortletModeType[] customPortletModeArray) {
        //-- copy array
        _customPortletModeList.clear();
        for (final CustomPortletModeType newVar : customPortletModeArray) {
            _customPortletModeList.add(newVar);
        }
    }

    /**
     * Method setCustomWindowState
     *
     * @param index
     * @param vCustomWindowState
     */
    public void setCustomWindowState(int index, CustomWindowStateType vCustomWindowState) {
        //-- check bounds for index
        if ((index < 0) || (index > _customWindowStateList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _customWindowStateList.set(index, vCustomWindowState);
    }

    /**
     * Method setCustomWindowState
     *
     * @param customWindowStateArray
     */
    public void setCustomWindowState(CustomWindowStateType[] customWindowStateArray) {
        //-- copy array
        _customWindowStateList.clear();
        for (final CustomWindowStateType newVar : customWindowStateArray) {
            _customWindowStateList.add(newVar);
        }
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
     * @param index
     * @param vPortlet
     */
    public void setPortlet(int index, PortletDefinition vPortlet) {
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
    public void setPortlet(PortletDefinition[] portletArray) {
        //-- copy array
        _portletList.clear();
        for (final PortletDefinition newVar : portletArray) {
            _portletList.add(newVar);
        }
    }

    /**
     * Method setSecurityConstraint
     *
     * @param index
     * @param vSecurityConstraint
     */
    public void setSecurityConstraint(int index, SecurityConstraint vSecurityConstraint) {
        //-- check bounds for index
        if ((index < 0) || (index > _securityConstraintList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _securityConstraintList.set(index, vSecurityConstraint);
    }

    /**
     * Method setSecurityConstraint
     *
     * @param securityConstraintArray
     */
    public void setSecurityConstraint(SecurityConstraint[] securityConstraintArray) {
        //-- copy array
        _securityConstraintList.clear();
        for (final SecurityConstraint newVar : securityConstraintArray) {
            _securityConstraintList.add(newVar);
        }
    }

    /**
     * Method setUserAttribute
     *
     * @param index
     * @param vUserAttribute
     */
    public void setUserAttribute(int index, UserAttribute vUserAttribute) {
        //-- check bounds for index
        if ((index < 0) || (index > _userAttributeList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _userAttributeList.set(index, vUserAttribute);
    }

    /**
     * Method setUserAttribute
     *
     * @param userAttributeArray
     */
    public void setUserAttribute(UserAttribute[] userAttributeArray) {
        //-- copy array
        _userAttributeList.clear();
        for (final UserAttribute newVar : userAttributeArray) {
            _userAttributeList.add(newVar);
        }
    }

    /**
     * Sets the value of field 'version'.
     *
     * @param version the value of field 'version'.
     */
    public void setVersion(java.lang.String version) {
        this._version = version;
    }
}
