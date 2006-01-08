package org.riverock.webmill.container.portal.bean;

import java.util.ArrayList;
import java.util.List;

import org.riverock.webmill.container.portal.bean.types.PortalTemplateItemTypeImpl;
import org.riverock.interfaces.portal.template.PortalTemplateParameter;
import org.riverock.interfaces.portal.template.PortalTemplateItemType;
import org.riverock.interfaces.portal.template.PortalTemplateItem;

/**
 * Class TemplateItemType.
 *
 * @version $Revision$ $Date$
 */
public class PortalTemplateItemImpl implements PortalTemplateItem {

    /**
     * Field type
     */
    private PortalTemplateItemType portalTemplateItemType;

    /**
     * Field value
     */
    private java.lang.String value;

    /**
     * Field code
     */
    private java.lang.String code;

    /**
     * Field xmlRoot
     */
    private java.lang.String xmlRoot;

    /**
     * Field isDisabled
     */
    private java.lang.Boolean isDisabled = false;

    /**
     * Field namespace
     */
    private java.lang.String namespace;

    /**
     * Field parameters
     */
    private List<PortalTemplateParameter> parameters;

    public PortalTemplateItemImpl() {
        super();
        parameters = new ArrayList<PortalTemplateParameter>();
    }

    /**
     * Method addParameter
     *
     * @param vParameter
     */
    public void addParameter(PortalTemplateParameter vParameter) {
        parameters.add(vParameter);
    }

    /**
     * Method clearParameter
     */
    public void clearParameter() {
        parameters.clear();
    }

    /**
     * Returns the value of field 'code'.
     *
     * @return the value of field 'code'.
     */
    public java.lang.String getCode() {
        return this.code;
    }

    /**
     * Returns the value of field 'isDisabled'.
     *
     * @return the value of field 'isDisabled'.
     */
    public java.lang.Boolean getIsDisabled() {
        return this.isDisabled;
    }

    /**
     * Returns the value of field 'namespace'.
     *
     * @return the value of field 'namespace'.
     */
    public String getNamespace() {
        return this.namespace;
    }

    /**
     * Method getParameterAsReference
     * <p/>
     * Returns a reference to 'parameter'. No type checking is
     * performed on any modications to the Collection.
     *
     * @return returns a reference to the Collection.
     */
    public List<PortalTemplateParameter> getParameters() {
        return parameters;
    }

    /**
     * Returns the value of field 'type'.
     *
     * @return the value of field 'type'.
     */
    public String getType() {
        if (portalTemplateItemType==null) {
            return null;
        }

        return this.portalTemplateItemType.toString();
    }

    /**
     * Returns the value of field 'type'.
     *
     * @return the value of field 'type'.
     */
    public PortalTemplateItemType getTypeObject() {
        return this.portalTemplateItemType;
    }

    /**
     * Returns the value of field 'value'.
     *
     * @return the value of field 'value'.
     */
    public java.lang.String getValue() {
        return this.value;
    }

    /**
     * Returns the value of field 'xmlRoot'.
     *
     * @return the value of field 'xmlRoot'.
     */
    public java.lang.String getXmlRoot() {
        return this.xmlRoot;
    }

    /**
     * Sets the value of field 'code'.
     *
     * @param code the value of field 'code'.
     */
    public void setCode(java.lang.String code) {
        this.code = code;
    }

    /**
     * Sets the value of field 'isDisabled'.
     *
     * @param isDisabled the value of field 'isDisabled'.
     */
    public void setIsDisabled(java.lang.Boolean isDisabled) {
        this.isDisabled = isDisabled;
    }

    /**
     * Sets the value of field 'namespace'.
     *
     * @param namespace the value of field 'namespace'.
     */
    public void setNamespace(java.lang.String namespace) {
        this.namespace = namespace;
    }

    /**
     * Method setParameter
     * <p/>
     * Sets the value of 'parameter' by copying the given
     * ArrayList.
     *
     * @param parameterCollection the ArrayList to copy.
     */
    public void setParameters(List<PortalTemplateParameter> parameterCollection) {
        parameters.clear();
        parameters.addAll( parameterCollection );
    }

    /**
     * Sets the value of field 'type'.
     *
     * @param typePortalImpl the value of field 'type'.
     */
    public void setTypeObject(PortalTemplateItemTypeImpl typePortalImpl) {
        this.portalTemplateItemType = typePortalImpl;
    }

    /**
     * Sets the value of field 'type'.
     *
     * @param type the value of field 'type'.
     */
    public void setType(String type) {
        this.portalTemplateItemType = PortalTemplateItemTypeImpl.valueOf( type );
    }

    /**
     * Sets the value of field 'value'.
     *
     * @param value the value of field 'value'.
     */
    public void setValue(java.lang.String value) {
        this.value = value;
    }

    /**
     * Sets the value of field 'xmlRoot'.
     *
     * @param xmlRoot the value of field 'xmlRoot'.
     */
    public void setXmlRoot(java.lang.String xmlRoot) {
        this.xmlRoot = xmlRoot;
    }
}
