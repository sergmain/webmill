/*
 * org.riverock.webmill - Webmill portal with support jsr-168, xml/xslt and others things.
 * For more information, please visit project site http://webmill.riverock.org
 *
 * Copyright (C) 2000-2006, Riverock Software, All Rights Reserved.
 *
 * Riverock - The Open-source Java Development Community
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
 */
package org.riverock.webmill.portal.template;

import java.io.Serializable;

import org.riverock.webmill.portal.template.parser.ParsedTemplateElement;

/**
 * Both attributes, 'nameTemplate' and 'name' are deprecated. Must
 * not used
 *
 * @version $Revision: 1375 $ $Date: 2007-08-28 19:35:44 +0000 (Tue, 28 Aug 2007) $
 */
public class PortalTemplateImpl implements PortalTemplate, Serializable {

    /**
     * Template role
     */
    private java.lang.String role;

    private Long templateId = null;

    private int version;

    private ParsedTemplateElement[] template=null;

    private String templateName=null;

    public PortalTemplateImpl() {
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public ParsedTemplateElement[] getTemplateElements() {
        return template;
    }

    public void setTemplate(ParsedTemplateElement[] template) {
        this.template = template;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public Long getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Long templateId) {
        this.templateId = templateId;
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
     * Sets the value of field 'role'.
     *
     * @param role the value of field 'role'.
     */
    public void setRole(java.lang.String role) {
        this.role = role;
    }

    public String toString() {
        return "[templateName:"+templateName+"]";
    }
}
