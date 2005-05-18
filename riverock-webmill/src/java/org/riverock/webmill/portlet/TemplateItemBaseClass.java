/*
 * org.riverock.webmill -- Portal framework implementation
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
package org.riverock.webmill.portlet;

import org.riverock.webmill.schema.site.types.TemplateItemTypeTypeType;

/**
 * User: SergeMaslyukov
 * Date: 25.11.2004
 * Time: 1:52:37
 * $Id$
 */
abstract class TemplateItemBaseClass {

    abstract void getData( PageElement pageElement );
    abstract void processAction( PageElement item );
    abstract TemplateItemTypeTypeType getType();

    static void renderTemplateItem( PageElement pageElement ) {

        TemplateItemBaseClass templateItem = null;
        switch (pageElement.getTemplateItemType().getType().getType()) {
            case TemplateItemTypeTypeType.PORTLET_TYPE:
                templateItem = new TemplateItemAsPortlet();
                break;
            case TemplateItemTypeTypeType.DYNAMIC_TYPE:
                templateItem =  new TemplateItemAsDynamic();
                break;
            case TemplateItemTypeTypeType.FILE_TYPE:
                templateItem = new TemplateItemAsFile();
                break;
            case TemplateItemTypeTypeType.CUSTOM_TYPE:
                templateItem = new TemplateItemAsCustom();
                break;
            default:
                return;
        }
        templateItem.getData( pageElement );
    }

    static void processActionTemplateItem( PageElement pageElement ) {

        TemplateItemBaseClass templateItem = null;
        switch (pageElement.getTemplateItemType().getType().getType()) {
            case TemplateItemTypeTypeType.PORTLET_TYPE:
                templateItem = new TemplateItemAsPortlet();
                break;
            case TemplateItemTypeTypeType.DYNAMIC_TYPE:
                templateItem =  new TemplateItemAsDynamic();
                break;
            default:
                return;
        }
        templateItem.processAction( pageElement );
    }
}
