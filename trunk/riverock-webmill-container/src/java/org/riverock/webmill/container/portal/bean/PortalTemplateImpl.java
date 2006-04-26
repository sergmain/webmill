package org.riverock.webmill.container.portal.bean;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.riverock.interfaces.portal.template.PortalTemplateItem;
import org.riverock.interfaces.portal.template.PortalTemplateParameter;
import org.riverock.interfaces.portal.template.PortalTemplate;

/**
 * Both attributes, 'nameTemplate' and 'name' are deprecated. Must
 * not used
 *
 * @version $Revision$ $Date$
 */
public class PortalTemplateImpl implements PortalTemplate {

    /**
     * Field role
     */
    private java.lang.String role;

    private java.lang.String templateName = null;

    private Long templateId = null;

    public Long getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Long templateId) {
        this.templateId = templateId;
    }

    /**
     * Field portalTemplateItems
     */
    private List<PortalTemplateItem> portalTemplateItems;

    public PortalTemplateImpl() {
        super();
        portalTemplateItems = new ArrayList<PortalTemplateItem>();
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    /**
     * Method addSiteTemplateItem
     *
     * @param vSitePortalTemplateItem
     */
    public void addSiteTemplateItem(PortalTemplateItemImpl vSitePortalTemplateItem) {
        portalTemplateItems.add(vSitePortalTemplateItem);
    }

    /**
     * Returns the value of field 'role'.
     *
     * @return the value of field 'role'.
     */
    public String getRole() {
        return this.role;
    }

    /**
     * Method getSiteTemplateItemAsReference
     * <p/>
     * Returns a reference to 'siteTemplateItem'. No type checking
     * is performed on any modications to the Collection.
     *
     * @return returns a reference to the Collection.
     */
    public List<PortalTemplateItem> getPortalTemplateItems() {
        return portalTemplateItems;
    }

    /**
     * Sets the value of field 'role'.
     *
     * @param role the value of field 'role'.
     */
    public void setRole(java.lang.String role) {
        this.role = role;
    }

    /**
     * Method setSiteTemplateItemAsReference
     * <p/>
     * Sets the value of 'siteTemplateItem' by setting it to the
     * given ArrayList. No type checking is performed.
     *
     * @param siteTemplateItemCollection the ArrayList to copy.
     */
    public void setPortalTemplateItems(List<PortalTemplateItem> siteTemplateItemCollection) {
        portalTemplateItems = siteTemplateItemCollection;
    }

    public String toString() {
        String s = "";

        s +=
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<SiteTemplate";
        if (getRole() != null) {
            s += " role=\"" + getRole() + "\"";
        }
        s += ">\n";

        Iterator<PortalTemplateItem> items = portalTemplateItems.iterator();
        while (items.hasNext()) {
            PortalTemplateItem portalTemplateItem = items.next();
            s += "    <SiteTemplateItem  type=\"" + portalTemplateItem.getType() + "\" value=\"" + portalTemplateItem.getValue() + "\"";
            if (portalTemplateItem.getCode() != null) {
                s += " code=\"" + portalTemplateItem.getCode() + "\"";
            }
            if (portalTemplateItem.getXmlRoot() != null) {
                s += " xmlRoot=\"" + portalTemplateItem.getXmlRoot() + "\"";
            }

            s += ">\n";

            Iterator<PortalTemplateParameter> iterator = portalTemplateItem.getParameters().iterator();
            while (iterator.hasNext()) {
                PortalTemplateParameter portalTemplateParameterImpl = iterator.next();
                s += "        <Parameter name=\"" + portalTemplateParameterImpl.getName() + "\" value=\"" + portalTemplateParameterImpl.getValue() + "\"/>\n";
            }
            s += "    </SiteTemplateItem>\n";

        }

        s += "</SiteTemplate>";

        return s;
    }
}
