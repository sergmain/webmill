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
package org.riverock.webmill.portal;

import org.riverock.webmill.container.schema.site.types.TemplateItemTypeTypeType;

import org.apache.log4j.Logger;

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
            log.debug( "include file - "+pageElement.getTemplateItemType().getValue());

        throw new IllegalArgumentException( "not implemented" );
/*
        StringWriter writer = new StringWriter();
        ServletUtils.include(
            portalRequestInstance.getHttpRequest(), portalRequestInstance.getHttpResponse(),
            null,
            pageElement.getValue(), writer
        );
        return PortalRequestProcessor.setData( writer.toString().getBytes(), false, false);
*/
    }

    void processAction( PageElement item ) {
    }

    TemplateItemTypeTypeType getType() {
        return TemplateItemTypeTypeType.FILE;
    }
}