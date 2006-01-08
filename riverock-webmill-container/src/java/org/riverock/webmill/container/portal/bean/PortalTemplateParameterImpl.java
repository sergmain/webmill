package org.riverock.webmill.container.portal.bean;

import org.riverock.interfaces.portal.template.PortalTemplateParameter;

/**
 * Class SiteTemplateParameterType.
 *
 * @version $Revision$ $Date$
 */
public class PortalTemplateParameterImpl implements PortalTemplateParameter {

    /**
     * Field _name
     */
    private java.lang.String _name;

    /**
     * Field _value
     */
    private java.lang.String _value;

    public PortalTemplateParameterImpl() {
        super();
    }

    /**
     * Returns the value of field 'name'.
     *
     * @return the value of field 'name'.
     */
    public String getName() {
        return this._name;
    }

    /**
     * Returns the value of field 'value'.
     *
     * @return the value of field 'value'.
     */
    public String getValue() {
        return this._value;
    }

    /**
     * Sets the value of field 'name'.
     *
     * @param name the value of field 'name'.
     */
    public void setName(java.lang.String name) {
        this._name = name;
    } //-- void setName(java.lang.String) 

    /**
     * Sets the value of field 'value'.
     *
     * @param value the value of field 'value'.
     */
    public void setValue(java.lang.String value) {
        this._value = value;
    } //-- void setValue(java.lang.String) 
}
