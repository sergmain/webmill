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
import java.util.HashMap;

import org.riverock.interfaces.schema.javax.portlet.PortletType;
import org.riverock.webmill.config.WebmillConfig;
import org.riverock.webmill.schema.site.SitePortletDataType;
import org.riverock.webmill.schema.site.TemplateItemType;
import org.riverock.webmill.schema.site.types.TemplateItemTypeTypeType;
import org.riverock.webmill.utils.ServletUtils;

import org.apache.log4j.Logger;

/**
 * User: SergeMaslyukov
 * Date: 25.11.2004
 * Time: 1:47:43
 * $Id$
 */
public final class TemplateItemAsDynamic extends TemplateItemBaseClass{
    private final static Logger log = Logger.getLogger(TemplateItemAsDynamic.class);

    SitePortletDataType getData( TemplateItemType item, PortalRequestInstance portalRequestInstance )
        throws Exception {

//        if ( Constants.CTX_TYPE_INDEX.equals( portalRequestInstance.getDefaultPortletType() ) ) {
//            String errorString = "Context type is 'index_page'. Template must not containts 'index_page' portlet.";
//            log.error( errorString );
//            return PortalRequestProcessor.setData( errorString.getBytes(), true, false);
//        }

        PortletType portlet = null;
        portlet = PortletManager.getPortletDescription( portalRequestInstance.getDefaultPortletType() );
        if ( portlet==null ) {
            String errorString = "Description of portlet '"+portalRequestInstance.getDefaultPortletType()+"' not found. Fix portlet.xml file";
            log.error( errorString );
            return PortalRequestProcessor.setData( errorString.getBytes(), true, false);
        }

        Boolean isUrlTempBoolean = null;
        isUrlTempBoolean = PortletTools.getBooleanParam(portlet, PortletTools.is_url);

//        Map map = null;
//        map = PortletTools.getParameters(ctxInstance.getHttpRequest());
//        map.putAll( CtxInstance.getGlobalParameter(portlet, ctxInstance.getPortletId()) );
//        ctxInstance.setParameters(
//            map, PortletTools.getStringParam(portlet, PortletTools.locale_name_package)
//        );

        if ( Boolean.FALSE.equals(isUrlTempBoolean) ){
            if ( log.isDebugEnabled() )
                log.debug( "process dynamic content. portlet !=null && !portlet.isUrl, defaultPortletType: '"+portalRequestInstance.getDefaultPortletType()+"'" );

            return processPortlet( item, portalRequestInstance.getDefaultPortletType(), portalRequestInstance );
        }
        else {
            if ( log.isDebugEnabled() )
                log.debug( "process dynamic content. include "+portlet.getPortletClass()+", isUrl - "+isUrlTempBoolean );

            StringWriter writer = new StringWriter();
            ServletUtils.include(
                portalRequestInstance.getHttpRequest(), portalRequestInstance.getHttpResponse(),
                new HashMap(),
//                CtxInstance.getGlobalParameter(portlet, ctxInstance.getPortletId()),
                portlet.getPortletClass(),
                writer
            );
            String s = writer.toString();

            return PortalRequestProcessor.setData( s.getBytes( WebmillConfig.getHtmlCharset() ), false, false);
        }
    }

    TemplateItemTypeTypeType getType() {
        return TemplateItemTypeTypeType.DYNAMIC;
    }
}
