package org.riverock.webmill.container.schema.site;

import java.util.ArrayList;
import java.util.List;

/**
 * Class SiteTemplateDescListType.
 *
 * @version $Revision$ $Date$
 */
public class SiteTemplateDescListType {

    /**
     * Field _templateDescriptionList
     */
    private List<SiteTemplateDescriptionType> _templateDescriptionList;

    public SiteTemplateDescListType() {
        super();
        _templateDescriptionList = new ArrayList<SiteTemplateDescriptionType>();
    }

    /**
     * Method addTemplateDescription
     *
     * @param vTemplateDescription
     */
    public void addTemplateDescription(SiteTemplateDescriptionType vTemplateDescription) {
        _templateDescriptionList.add(vTemplateDescription);
    }

    /**
     * Method clearTemplateDescription
     */
    public void clearTemplateDescriptions() {
        _templateDescriptionList.clear();
    }

    /**
     * Method getTemplateDescriptionAsReference
     * <p/>
     * Returns a reference to 'templateDescription'. No type
     * checking is performed on any modications to the Collection.
     *
     * @return returns a reference to the Collection.
     */
    public List<SiteTemplateDescriptionType> getTemplateDescriptions() {
        return _templateDescriptionList;
    }

    /**
     * Method removeTemplateDescription
     *
     * @param vTemplateDescription
     * @return boolean
     */
    public boolean removeTemplateDescription(org.riverock.webmill.container.schema.site.SiteTemplateDescriptionType vTemplateDescription) {
        boolean removed = _templateDescriptionList.remove(vTemplateDescription);
        return removed;
    }

    /**
     * Method setTemplateDescription
     *
     * @param templateDescriptionArray
     */
    public void setTemplateDescription(org.riverock.webmill.container.schema.site.SiteTemplateDescriptionType[] templateDescriptionArray) {
        //-- copy array
        _templateDescriptionList.clear();
        for (final SiteTemplateDescriptionType newVar : templateDescriptionArray) {
            _templateDescriptionList.add(newVar);
        }
    }

    /**
     * Method setTemplateDescription
     * <p/>
     * Sets the value of 'templateDescription' by copying the given
     * ArrayList.
     *
     * @param templateDescriptionCollection the ArrayList to copy.
     */
    public void setTemplateDescription(List<SiteTemplateDescriptionType> templateDescriptionCollection) {
        _templateDescriptionList.clear();
        _templateDescriptionList.addAll(templateDescriptionCollection);
    }

    /**
     * Method setTemplateDescriptionAsReference
     * <p/>
     * Sets the value of 'templateDescription' by setting it to the
     * given ArrayList. No type checking is performed.
     *
     * @param templateDescriptionCollection the ArrayList to copy.
     */
    public void setTemplateDescriptions(List<SiteTemplateDescriptionType> templateDescriptionCollection) {
        _templateDescriptionList = templateDescriptionCollection;
    }
}
