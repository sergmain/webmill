package org.riverock.webmill.container.schema.site;

import org.riverock.interfaces.portal.template.PortalTemplate;

/**
 * Class SiteTemplateDescriptionType.
 *
 * @version $Revision$ $Date$
 */
public class SiteTemplateDescriptionType {


    //- Class/Member Variables -/


    /**
     * Field _idTemplate
     */
    private java.lang.Long _idTemplate;

    /**
     * Field _nameLanguage
     */
    private java.lang.String _nameLanguage;

    /**
     * Field _idTemplateLanguage
     */
    private java.lang.Long _idTemplateLanguage;

    /**
     * Field _template
     */
    private PortalTemplate _template;


    public SiteTemplateDescriptionType() {
        super();
    } //-- org.riverock.webmill.container.schema.site.SiteTemplateDescriptionType()


    /**
     * Returns the value of field 'idTemplate'.
     *
     * @return the value of field 'idTemplate'.
     */
    public java.lang.Long getIdTemplate() {
        return this._idTemplate;
    } //-- java.lang.Long getIdTemplate() 

    /**
     * Returns the value of field 'idTemplateLanguage'.
     *
     * @return the value of field 'idTemplateLanguage'.
     */
    public java.lang.Long getIdTemplateLanguage() {
        return this._idTemplateLanguage;
    } //-- java.lang.Long getIdTemplateLanguage() 

    /**
     * Returns the value of field 'nameLanguage'.
     *
     * @return the value of field 'nameLanguage'.
     */
    public java.lang.String getNameLanguage() {
        return this._nameLanguage;
    } //-- java.lang.String getNameLanguage() 

    /**
     * Returns the value of field 'template'.
     *
     * @return the value of field 'template'.
     */
    public PortalTemplate getTemplate() {
        return this._template;
    }

    /**
     * Sets the value of field 'idTemplate'.
     *
     * @param idTemplate the value of field 'idTemplate'.
     */
    public void setIdTemplate(java.lang.Long idTemplate) {
        this._idTemplate = idTemplate;
    } //-- void setIdTemplate(java.lang.Long) 

    /**
     * Sets the value of field 'idTemplateLanguage'.
     *
     * @param idTemplateLanguage the value of field
     *                           'idTemplateLanguage'.
     */
    public void setIdTemplateLanguage(java.lang.Long idTemplateLanguage) {
        this._idTemplateLanguage = idTemplateLanguage;
    } //-- void setIdTemplateLanguage(java.lang.Long) 

    /**
     * Sets the value of field 'nameLanguage'.
     *
     * @param nameLanguage the value of field 'nameLanguage'.
     */
    public void setNameLanguage(java.lang.String nameLanguage) {
        this._nameLanguage = nameLanguage;
    } //-- void setNameLanguage(java.lang.String) 

    /**
     * Sets the value of field 'template'.
     *
     * @param template the value of field 'template'.
     */
    public void setTemplate(PortalTemplate template) {
        this._template = template;
    }
}
