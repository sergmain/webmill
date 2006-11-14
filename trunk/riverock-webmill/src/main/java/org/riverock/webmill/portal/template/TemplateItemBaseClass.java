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

import org.riverock.interfaces.portal.template.PortalTemplateItemType;
import org.riverock.webmill.portal.PageElement;

/**
 * User: SergeMaslyukov
 * Date: 25.11.2004
 * Time: 1:52:37
 * $Id$
 */
public abstract class TemplateItemBaseClass {

    abstract void getData( PageElement pageElement );
    abstract void processAction( PageElement item );
    abstract PortalTemplateItemType getType();

    public static void renderTemplateItem( PageElement pageElement ) {

        TemplateItemBaseClass templateItem;
        switch (pageElement.getPortalTemplateItem().getTypeObject().getType()) {
            case PortalTemplateItemType.PORTLET_TYPE:
                templateItem = new TemplateItemAsPortlet();
                break;
            case PortalTemplateItemType.DYNAMIC_TYPE:
                templateItem =  new TemplateItemAsDynamic();
                break;
            case PortalTemplateItemType.FILE_TYPE:
                templateItem = new TemplateItemAsFile();
                break;
            case PortalTemplateItemType.CUSTOM_TYPE:
                templateItem = new TemplateItemAsCustom();
                break;
            default:
                return;
        }
        templateItem.getData( pageElement );
    }

    public static void processActionTemplateItem( PageElement pageElement ) {

        TemplateItemBaseClass templateItem;
        switch (pageElement.getPortalTemplateItem().getTypeObject().getType()) {
            case PortalTemplateItemType.PORTLET_TYPE:
                templateItem = new TemplateItemAsPortlet();
                break;
            case PortalTemplateItemType.DYNAMIC_TYPE:
                templateItem =  new TemplateItemAsDynamic();
                break;
            default:
                return;
        }
        templateItem.processAction( pageElement );
    }
}
