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

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.portlet.Portlet;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.riverock.common.tools.ExceptionTools;
import org.riverock.common.tools.MainTools;
import org.riverock.generic.exception.FileManagerException;
import org.riverock.interfaces.schema.javax.portlet.PortletType;
import org.riverock.webmill.config.WebmillConfig;
import org.riverock.webmill.portal.PortalConstants;
import org.riverock.webmill.portlet.wrapper.RenderRequestImpl;
import org.riverock.webmill.portlet.wrapper.RenderResponseImpl;
import org.riverock.webmill.schema.site.SitePortletDataType;
import org.riverock.webmill.schema.site.TemplateItemType;
import org.riverock.webmill.schema.site.types.TemplateItemTypeTypeType;

import org.apache.log4j.Logger;

/**
 * User: SergeMaslyukov
 * Date: 25.11.2004
 * Time: 1:52:37
 * $Id$
 */
abstract class TemplateItemBaseClass {
    private final static Logger log = Logger.getLogger(TemplateItemBaseClass.class);

    private final static int NUM_LINES = 100;
    private final static int BUFFER_INITIAL_SIZE = 10000;

    abstract SitePortletDataType getData( TemplateItemType item, PortalRequestInstance portalRequestInstance )
        throws Exception;

    abstract TemplateItemTypeTypeType getType();

    static SitePortletDataType processTemplateItem( TemplateItemType item, PortalRequestInstance portalRequestInstance )
      throws Exception {

        TemplateItemBaseClass templateItem = null;
        switch (item.getType().getType()) {
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
                return null;
        }

        return templateItem.getData( item, portalRequestInstance);
    }

    protected final static SitePortletDataType processPortlet( TemplateItemType templateItem, String portletType, PortalRequestInstance portalRequestInstance ) throws FileManagerException {

        PortletType portletDefinition = PortletManager.getPortletDescription( portletType );
        if ( portletDefinition==null ) {
            return PortalRequestProcessor.setData(
                ("Error getPortlet(), type: "+portletType).getBytes(), true, false);
        }

        try {
            if (log.isDebugEnabled())
                log.debug( "Start create instance of portlet '"+portletDefinition.getPortletName().getContent()+"'");

            Portlet portlet = PortletContainer.getPortletInstance(
                portletDefinition.getPortletName().getContent()
            );

            ContextFactory.PortletParameters params = portalRequestInstance.getParameters( templateItem.getNamespace(), templateItem.getType() );

            if (log.isDebugEnabled()) {
                log.debug( "portlet: "+portletType );
                log.debug( "portlet type: "+ templateItem.getType().toString() );
                log.debug( "Instance of portlet is created; "+portlet);
                log.debug( "namespace: "+templateItem.getNamespace() );
                log.debug( "PortletParameter: "+params );
                if (params!=null)
                    log.debug( "Map with params: "+params.getParameters() );
            }
            Map map = null;
            if ( templateItem.getType().getType()==TemplateItemTypeTypeType.DYNAMIC_TYPE ) {
                map = preparePortletParameters( portalRequestInstance, portletDefinition );
            }
            else {
                map = new HashMap();
            }

            if ( params!=null && params.getParameters()!=null) {
                map.putAll( params.getParameters() );
            }
            RenderRequest renderRequest = new RenderRequestImpl( map, portalRequestInstance );

            // set portlet specific attribute
            renderRequest.setAttribute(
                PortalConstants.PORTAL_TEMPLATE_PARAMETERS_ATTRIBUTE, templateItem.getParameterAsReference());
            renderRequest.setAttribute(
                PortalConstants.PORTAL_PORTLET_CODE_ATTRIBUTE, templateItem.getCode() );
            renderRequest.setAttribute(
                PortalConstants.PORTAL_PORTLET_XML_ROOT_ATTRIBUTE, templateItem.getXmlRoot() );

            ByteArrayOutputStream stream = new ByteArrayOutputStream( BUFFER_INITIAL_SIZE );
            RenderResponse renderResponse = new RenderResponseImpl( portalRequestInstance, portalRequestInstance.getHttpResponse(), stream, templateItem.getNamespace() );

            log.debug( "Start render portlet");

            portlet.render( renderRequest, renderResponse );

            Boolean isXmlValue = PortletTools.getBooleanParam( portletDefinition, PortletTools.is_xml );
            boolean isXml = (isXmlValue==null?false:isXmlValue.booleanValue());

            if ( log.isDebugEnabled() ){
                log.debug( "Portlet object successfull created" );
                log.debug( "isXml() - "+isXml );

                if ( isXml )
                    log.debug( "XmlRoot - "+templateItem.getXmlRoot() );
            }

            byte portletBytes[] = stream.toByteArray();

            if (log.isDebugEnabled()) {
                String fileName =
                    WebmillConfig.getWebmillTempDir() +
                    renderResponse.getNamespace() +
                    "-1-portlet-data."+(isXml?"xml":"bin");
                log.debug( "write portlet result to file "+fileName );
                MainTools.writeToFile( fileName, portletBytes );
            }

            if ( isXml ) {
                // write all without XML header - <? ..... ?>
                int idx = MainTools.indexOf( portletBytes, (byte)'>' );
                if ( idx==-1 ){
                    final String es = "Array of bytes with xml'ized data is wrong - not start with <?xml ...?> ";
                    log.error(es);
                    return PortalRequestProcessor.setData(
                        ( "Error process Portlet '"+portletDefinition.getPortletName().getContent()+"'\n"+
                        es+"\n"
                        ).getBytes(),
                        true, false
                    );
                }
                else
                    return PortalRequestProcessor.setData(
                        MainTools.getBytes( portletBytes, idx+1 ), false, true);
            }
            else {
                return PortalRequestProcessor.setData( portletBytes, false, false);
            }
        }
        catch (Exception e) {
            log.error( "Error process Portlet '"+portletDefinition.getPortletName().getContent()+"'", e );
            return PortalRequestProcessor.setData(
                "Error process Portlet '"+portletDefinition.getPortletName().getContent()+"'\n"+
                e.toString()+"\n"+
                "<!-- "+ExceptionTools.getStackTrace( e, NUM_LINES )+"\n-->\n",
                true, false
            );
        }
    }

    private static Map preparePortletParameters( final PortalRequestInstance portalRequestInstance, final PortletType portletDefinition ) {
        Map map = new HashMap();

        String nameId = PortletTools.getStringParam( portletDefinition, PortletTools.name_portlet_id );
        if ( log.isDebugEnabled() ) {
            log.debug( "nameId: "+nameId );
            log.debug( "Id: "+portalRequestInstance.getDefaultPortletId() );
        }
        if (nameId!=null)
            map.put( nameId, portalRequestInstance.getDefaultPortletId() );

        return map;
    }
}
