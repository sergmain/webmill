/*
 * org.riverock.webmill.container -- Webmill portlet container implementation
 *
 * Copyright (C) 2004, Riverock Software, All Rights Reserved.
 *
 * Riverock -- The Open-source Java Development Community
 * http://www.riverock.org
 *
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 *
 */
package org.riverock.webmill.container.portal.bean;

import java.util.ArrayList;
import java.util.List;

import org.riverock.interfaces.portal.template.PortalTemplate;
import org.riverock.interfaces.portal.template.PortalTemplateItem;
import org.riverock.interfaces.portal.template.PortalTemplateParameter;

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

        for (PortalTemplateItem portalTemplateItem : portalTemplateItems) {
            s += "    <SiteTemplateItem  type=\"" + portalTemplateItem.getType() + "\" value=\"" + portalTemplateItem.getValue() + "\"";
            if (portalTemplateItem.getCode() != null) {
                s += " code=\"" + portalTemplateItem.getCode() + "\"";
            }
            if (portalTemplateItem.getXmlRoot() != null) {
                s += " xmlRoot=\"" + portalTemplateItem.getXmlRoot() + "\"";
            }
            if (portalTemplateItem.getRole() != null) {
                s += " role=\"" + portalTemplateItem.getRole() + "\"";
            }

            s += ">\n";

            for (PortalTemplateParameter portalTemplateParameterImpl : portalTemplateItem.getParameters()) {
                s += "        <Parameter name=\"" + portalTemplateParameterImpl.getName() + "\" value=\"" + portalTemplateParameterImpl.getValue() + "\"/>\n";
            }
            s += "    </SiteTemplateItem>\n";

        }

        s += "</SiteTemplate>";

        return s;
    }
}
