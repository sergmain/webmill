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
import org.riverock.webmill.portal.template.bean.types.PortalTemplateItemTypeImpl;
import org.riverock.webmill.portal.PageElement;

/**
 * User: SergeMaslyukov
 * Date: 25.11.2004
 * Time: 1:47:25
 * $Id$
 */
public final class TemplateItemAsPortlet extends TemplateItemBaseClass {

    void getData( PageElement pageElement ) {
        pageElement.renderPortlet();
    }

    void processAction( PageElement pageElement ) {
        pageElement.processActionPortlet();
    }

    PortalTemplateItemType getType() {
        return PortalTemplateItemTypeImpl.PORTLET;
    }
}
