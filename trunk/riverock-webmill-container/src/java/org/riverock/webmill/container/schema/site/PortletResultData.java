package org.riverock.webmill.container.schema.site;

/**
 * Class SitePortletDataType.
 *
 * @version $Revision$ $Date$
 */
public class PortletResultData {

    /**
     * Field _data
     */
    private byte[] _data;

    /**
     * Field _isError
     */
    private java.lang.Boolean _isError = false;

    /**
     * Field _isXml
     */
    private java.lang.Boolean _isXml = false;

    public PortletResultData() {
        super();
    }

    /**
     * Returns the value of field 'data'.
     *
     * @return the value of field 'data'.
     */
    public byte[] getData() {
        return this._data;
    }

    /**
     * Returns the value of field 'isError'.
     *
     * @return the value of field 'isError'.
     */
    public java.lang.Boolean getIsError() {
        return this._isError;
    }

    /**
     * Returns the value of field 'isXml'.
     *
     * @return the value of field 'isXml'.
     */
    public java.lang.Boolean getIsXml() {
        return this._isXml;
    }

    /**
     * Sets the value of field 'data'.
     *
     * @param data the value of field 'data'.
     */
    public void setData(byte[] data) {
        this._data = data;
    }

    /**
     * Sets the value of field 'isError'.
     *
     * @param isError the value of field 'isError'.
     */
    public void setIsError(java.lang.Boolean isError) {
        this._isError = isError;
    }

    /**
     * Sets the value of field 'isXml'.
     *
     * @param isXml the value of field 'isXml'.
     */
    public void setIsXml(java.lang.Boolean isXml) {
        this._isXml = isXml;
    }  
}
