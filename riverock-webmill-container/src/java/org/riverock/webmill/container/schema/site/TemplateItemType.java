/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.7</a>, using an XML
 * Schema.
 * $Id$
 */

package org.riverock.webmill.container.schema.site;

import java.util.ArrayList;

import org.riverock.webmill.container.schema.site.types.TemplateItemTypeTypeType;

/**
 * Class TemplateItemType.
 *
 * @version $Revision$ $Date$
 */
public class TemplateItemType {


    /**
     * Field _type
     */
    private org.riverock.webmill.container.schema.site.types.TemplateItemTypeTypeType _type;

    /**
     * Field _value
     */
    private java.lang.String _value;

    /**
     * Field _code
     */
    private java.lang.String _code;

    /**
     * Field _xmlRoot
     */
    private java.lang.String _xmlRoot;

    /**
     * Field _isDisabled
     */
    private java.lang.Boolean _isDisabled = new java.lang.Boolean("false");

    /**
     * Field _namespace
     */
    private java.lang.String _namespace;

    /**
     * Field _parameterList
     */
    private java.util.ArrayList<SiteTemplateParameterType> _parameterList;






    public TemplateItemType() {
        super();
        _parameterList = new ArrayList<SiteTemplateParameterType>();
    }


    /**
     * Method addParameter
     *
     * @param vParameter
     */
    public void addParameter(org.riverock.webmill.container.schema.site.SiteTemplateParameterType vParameter)
        throws java.lang.IndexOutOfBoundsException {
        _parameterList.add(vParameter);
    }

    /**
     * Method addParameter
     *
     * @param index
     * @param vParameter
     */
    public void addParameter(int index, org.riverock.webmill.container.schema.site.SiteTemplateParameterType vParameter)
        throws java.lang.IndexOutOfBoundsException {
        _parameterList.add(index, vParameter);
    }

    /**
     * Method clearParameter
     */
    public void clearParameter() {
        _parameterList.clear();
    }

    /**
     * Returns the value of field 'code'.
     *
     * @return the value of field 'code'.
     */
    public java.lang.String getCode() {
        return this._code;
    }

    /**
     * Returns the value of field 'isDisabled'.
     *
     * @return the value of field 'isDisabled'.
     */
    public java.lang.Boolean getIsDisabled() {
        return this._isDisabled;
    }

    /**
     * Returns the value of field 'namespace'.
     *
     * @return the value of field 'namespace'.
     */
    public java.lang.String getNamespace() {
        return this._namespace;
    }

    /**
     * Method getParameter
     *
     * @param index
     * @return SiteTemplateParameterType
     */
    public org.riverock.webmill.container.schema.site.SiteTemplateParameterType getParameter(int index)
        throws java.lang.IndexOutOfBoundsException {
        //-- check bounds for index
        if ((index < 0) || (index > _parameterList.size())) {
            throw new IndexOutOfBoundsException();
        }

        return (org.riverock.webmill.container.schema.site.SiteTemplateParameterType) _parameterList.get(index);
    }

    /**
     * Method getParameter
     *
     * @return SiteTemplateParameterType
     */
    public org.riverock.webmill.container.schema.site.SiteTemplateParameterType[] getParameter() {
        int size = _parameterList.size();
        org.riverock.webmill.container.schema.site.SiteTemplateParameterType[] mArray = new org.riverock.webmill.container.schema.site.SiteTemplateParameterType[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (org.riverock.webmill.container.schema.site.SiteTemplateParameterType) _parameterList.get(index);
        }
        return mArray;
    }

    /**
     * Method getParameterAsReference
     * <p/>
     * Returns a reference to 'parameter'. No type checking is
     * performed on any modications to the Collection.
     *
     * @return returns a reference to the Collection.
     */
    public java.util.ArrayList<SiteTemplateParameterType> getParameterAsReference() {
        return _parameterList;
    }

    /**
     * Method getParameterCount
     *
     * @return int
     */
    public int getParameterCount() {
        return _parameterList.size();
    }

    /**
     * Returns the value of field 'type'.
     *
     * @return the value of field 'type'.
     */
    public String getType() {
        if (_type==null) {
            return null;
        }

        return this._type.toString();
    }

    /**
     * Returns the value of field 'type'.
     *
     * @return the value of field 'type'.
     */
    public org.riverock.webmill.container.schema.site.types.TemplateItemTypeTypeType getTypeObject() {
        return this._type;
    }

    /**
     * Returns the value of field 'value'.
     *
     * @return the value of field 'value'.
     */
    public java.lang.String getValue() {
        return this._value;
    }

    /**
     * Returns the value of field 'xmlRoot'.
     *
     * @return the value of field 'xmlRoot'.
     */
    public java.lang.String getXmlRoot() {
        return this._xmlRoot;
    }

    /**
     * Method removeParameter
     *
     * @param vParameter
     * @return boolean
     */
    public boolean removeParameter(org.riverock.webmill.container.schema.site.SiteTemplateParameterType vParameter) {
        boolean removed = _parameterList.remove(vParameter);
        return removed;
    }

    /**
     * Sets the value of field 'code'.
     *
     * @param code the value of field 'code'.
     */
    public void setCode(java.lang.String code) {
        this._code = code;
    }

    /**
     * Sets the value of field 'isDisabled'.
     *
     * @param isDisabled the value of field 'isDisabled'.
     */
    public void setIsDisabled(java.lang.Boolean isDisabled) {
        this._isDisabled = isDisabled;
    }

    /**
     * Sets the value of field 'namespace'.
     *
     * @param namespace the value of field 'namespace'.
     */
    public void setNamespace(java.lang.String namespace) {
        this._namespace = namespace;
    }

    /**
     * Method setParameter
     *
     * @param index
     * @param vParameter
     */
    public void setParameter(int index, org.riverock.webmill.container.schema.site.SiteTemplateParameterType vParameter)
        throws java.lang.IndexOutOfBoundsException {
        //-- check bounds for index
        if ((index < 0) || (index > _parameterList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _parameterList.set(index, vParameter);
    }

    /**
     * Method setParameter
     *
     * @param parameterArray
     */
    public void setParameter(org.riverock.webmill.container.schema.site.SiteTemplateParameterType[] parameterArray) {
        //-- copy array
        _parameterList.clear();
        for (final SiteTemplateParameterType newVar : parameterArray) {
            _parameterList.add(newVar);
        }
    }

    /**
     * Method setParameter
     * <p/>
     * Sets the value of 'parameter' by copying the given
     * ArrayList.
     *
     * @param parameterCollection the ArrayList to copy.
     */
    public void setParameter(java.util.ArrayList parameterCollection) {
        //-- copy collection
        _parameterList.clear();
        for (int i = 0; i < parameterCollection.size(); i++) {
            _parameterList.add((org.riverock.webmill.container.schema.site.SiteTemplateParameterType) parameterCollection.get(i));
        }
    }

    /**
     * Method setParameterAsReference
     * <p/>
     * Sets the value of 'parameter' by setting it to the given
     * ArrayList. No type checking is performed.
     *
     * @param parameterCollection the ArrayList to copy.
     */
    public void setParameterAsReference(java.util.ArrayList<SiteTemplateParameterType> parameterCollection) {
        _parameterList = parameterCollection;
    }

    /**
     * Sets the value of field 'type'.
     *
     * @param type the value of field 'type'.
     */
    public void setTypeObject(TemplateItemTypeTypeType type) {
        this._type = type;
    }

    /**
     * Sets the value of field 'type'.
     *
     * @param type the value of field 'type'.
     */
    public void setType(String type) {
        this._type = TemplateItemTypeTypeType.valueOf( type );
    }

    /**
     * Sets the value of field 'value'.
     *
     * @param value the value of field 'value'.
     */
    public void setValue(java.lang.String value) {
        this._value = value;
    }

    /**
     * Sets the value of field 'xmlRoot'.
     *
     * @param xmlRoot the value of field 'xmlRoot'.
     */
    public void setXmlRoot(java.lang.String xmlRoot) {
        this._xmlRoot = xmlRoot;
    }
}
