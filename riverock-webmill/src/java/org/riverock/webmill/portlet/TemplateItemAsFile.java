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

import java.io.StringWriter;

import org.riverock.webmill.schema.site.SitePortletDataType;
import org.riverock.webmill.schema.site.TemplateItemType;
import org.riverock.webmill.schema.site.types.TemplateItemTypeTypeType;
import org.riverock.webmill.utils.ServletUtils;

import org.apache.log4j.Logger;

/**
 * User: SergeMaslyukov
 * Date: 25.11.2004
 * Time: 1:48:01
 * $Id$
 */
public final class TemplateItemAsFile extends TemplateItemBaseClass {
    private static Logger log = Logger.getLogger(TemplateItemAsFile.class);

    SitePortletDataType getData( TemplateItemType item, PortalRequestInstance portalRequestInstance )
        throws Exception {

        if ( log.isDebugEnabled() )
            log.debug( "include file - "+item.getValue());

        // check of existing file not work with files from others context
/*
        String nameFile = PropertiesProvider.getApplicationPath()+item.getValue();
        if ( nameFile.indexOf( '?' )!=-1 )
            nameFile = nameFile.substring( 0, nameFile.indexOf( '?' ) );

        File testFile = new File( nameFile );
        if ( !testFile.exists() ) {
            testFile = null;

            String errorString = "Portlet "+portalRequestInstance.getDefaultPortletType()+" refered to file "+testFile+" is broken";
            log.error( errorString );
            portalRequestInstance.byteArrayOutputStream.write( errorString.getBytes() );
            return PortalRequestProcessor.setData( errorString.getBytes(), true, false);
        }
        testFile = null;
*/
        StringWriter writer = new StringWriter();
        ServletUtils.include(
            portalRequestInstance.getHttpRequest(), portalRequestInstance.getHttpResponse(),
            null,
            item.getValue(), writer
        );
        return PortalRequestProcessor.setData( writer.toString().getBytes(), false, false);
    }

    TemplateItemTypeTypeType getType() {
        return TemplateItemTypeTypeType.FILE;
    }
}
