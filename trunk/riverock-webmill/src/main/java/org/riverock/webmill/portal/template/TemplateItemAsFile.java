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

import org.apache.log4j.Logger;

import org.riverock.interfaces.portal.template.PortalTemplateItemType;
import org.riverock.webmill.portal.template.bean.types.PortalTemplateItemTypeImpl;
import org.riverock.webmill.portal.PageElement;

/**
 * User: SergeMaslyukov
 * Date: 25.11.2004
 * Time: 1:48:01
 * $Id$
 */
public final class TemplateItemAsFile extends TemplateItemBaseClass {
    private final static Logger log = Logger.getLogger(TemplateItemAsFile.class);

    void getData( PageElement pageElement ) {

        if ( log.isDebugEnabled() )
            log.debug( "include file - "+pageElement.getPortalTemplateItem().getValue());

        throw new IllegalArgumentException( "not implemented" );
    }

    void processAction( PageElement item ) {
    }

    PortalTemplateItemType getType() {
        return PortalTemplateItemTypeImpl.FILE;
    }
}
