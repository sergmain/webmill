/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.7</a>, using an XML
 * Schema.
 * $Id$
 */

package org.riverock.webmill.container.schema.site;







/**
 * Class SitePortletDataType.
 * 
 * @version $Revision$ $Date$
 */
public class SitePortletDataType {



     //- Class/Member Variables -/


    /**
     * Field _data
     */
    private byte[] _data;

    /**
     * Field _isError
     */
    private java.lang.Boolean _isError = new java.lang.Boolean("false");

    /**
     * Field _isXml
     */
    private java.lang.Boolean _isXml = new java.lang.Boolean("false");






    public SitePortletDataType() 
     {
        super();
    } //-- org.riverock.webmill.container.schema.site.SitePortletDataType()






    /**
     * Returns the value of field 'data'.
     * 
     * @return byte
     * @return the value of field 'data'.
     */
    public byte[] getData()
    {
        return this._data;
    } //-- byte[] getData() 

    /**
     * Returns the value of field 'isError'.
     * 
     * @return Boolean
     * @return the value of field 'isError'.
     */
    public java.lang.Boolean getIsError()
    {
        return this._isError;
    } //-- java.lang.Boolean getIsError() 

    /**
     * Returns the value of field 'isXml'.
     * 
     * @return Boolean
     * @return the value of field 'isXml'.
     */
    public java.lang.Boolean getIsXml()
    {
        return this._isXml;
    } //-- java.lang.Boolean getIsXml() 

    /**
     * Sets the value of field 'data'.
     * 
     * @param data the value of field 'data'.
     */
    public void setData(byte[] data)
    {
        this._data = data;
    } //-- void setData(byte) 

    /**
     * Sets the value of field 'isError'.
     * 
     * @param isError the value of field 'isError'.
     */
    public void setIsError(java.lang.Boolean isError)
    {
        this._isError = isError;
    } //-- void setIsError(java.lang.Boolean) 

    /**
     * Sets the value of field 'isXml'.
     * 
     * @param isXml the value of field 'isXml'.
     */
    public void setIsXml(java.lang.Boolean isXml)
    {
        this._isXml = isXml;
    } //-- void setIsXml(java.lang.Boolean) 
}
