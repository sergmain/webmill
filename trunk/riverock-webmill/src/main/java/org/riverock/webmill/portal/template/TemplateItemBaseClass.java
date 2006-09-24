/*
 * org.riverock.webmill - Portal framework implementation
 *
 * Copyright (C) 2006, Riverock Software, All Rights Reserved.
 *
 * Riverock - The Open-source Java Development Community
 * http://www.riverock.org
 *
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
