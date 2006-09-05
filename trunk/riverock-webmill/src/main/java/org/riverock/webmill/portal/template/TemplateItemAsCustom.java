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
package org.riverock.webmill.portal.template;

import org.riverock.interfaces.portal.template.PortalTemplateItemType;
import org.riverock.webmill.portal.template.bean.types.PortalTemplateItemTypeImpl;
import org.riverock.webmill.portal.PageElement;
import org.riverock.webmill.portal.PortalRequestProcessor;

import org.apache.log4j.Logger;

/**
 * User: SergeMaslyukov
 * Date: 25.11.2004
 * Time: 1:48:01
 * $Id$
 */
public final class TemplateItemAsCustom extends TemplateItemBaseClass {
    private final static Logger log = Logger.getLogger(TemplateItemAsCustom.class);

    void getData( PageElement pageElement ) {
        if ( log.isDebugEnabled() )
            log.debug( "Template item  type - "+pageElement.getPortalTemplateItem().getType()+"  value "+pageElement.getPortalTemplateItem().getValue() );

        pageElement.setData(
            PortalRequestProcessor.setData(
                new StringBuilder("<").append( pageElement.getPortalTemplateItem().getValue() ).append( "/>" ).toString().getBytes(),
                false, true
            )
        );
    }

    void processAction( PageElement item ) {
    }

    PortalTemplateItemType getType() {
        return PortalTemplateItemTypeImpl.CUSTOM;
    }
}