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

import java.io.IOException;
import java.io.OutputStream;

import javax.portlet.Portlet;
import javax.portlet.PortletConfig;
import javax.portlet.PortletException;
import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.db.DatabaseManager;
import org.riverock.webmill.portal.PortalConstants;

import org.apache.log4j.Logger;

/**
 * User: SergeMaslyukov
 * Date: 07.12.2004
 * Time: 15:54:17
 * $Id$
 */
public abstract class GenericWebmillPortlet implements Portlet {
    private final static Logger log = Logger.getLogger( GenericWebmillPortlet.class );

    public GenericWebmillPortlet(){}

    protected PortletConfig portletConfig = null;
    public void init(PortletConfig portletConfig) throws PortletException {
        this.portletConfig = portletConfig;
    }

    public void destroy() {
        portletConfig = null;
    }

    public final void doProcessAction(ActionRequest actionRequest, ActionResponse actionResponse) {
    }

    public boolean isXml() {
        String s = portletConfig.getInitParameter( PortletTools.is_xml );
        if (s==null)
            return false;
        return new Boolean(s).booleanValue();
    }

    public void doRender(RenderRequest renderRequest, RenderResponse renderResponse, PortletResultObject beanObject) throws PortletException, IOException
    {
        OutputStream out = null;
        DatabaseAdapter db_ = null;
        try
        {
            db_ = DatabaseAdapter.getInstance();
            out = renderResponse.getPortletOutputStream();

            String code = (String)renderRequest.getAttribute(
                PortalConstants.PORTAL_PORTLET_CODE_ATTRIBUTE );

            String xmlRoot = (String)renderRequest.getAttribute(
                PortalConstants.PORTAL_PORTLET_XML_ROOT_ATTRIBUTE );

            if (log.isDebugEnabled()) {
                log.debug( "Portlet code: "+code+", xmlRoot: "+xmlRoot );
                log.debug( "renderRequest: "+renderRequest );
                log.debug( "portlet name: "+portletConfig.getPortletName() );
                log.debug( "portlet isXml(): " + isXml() );
            }

            beanObject.setParameters( renderRequest, renderResponse, portletConfig );
            PortletResultContent result = null;
            if ( code==null || code.length()==0 ){
                String portletId = portletConfig.getInitParameter( PortletTools.name_portlet_id );

                log.debug( "get portlet id, nameId: " + portletId + ", renderRequest: " + renderRequest );

                Long id = PortletTools.getLong( renderRequest, portletId );
                if ( log.isDebugEnabled() ) {
                    log.debug( "nameId: " + portletId );
                    log.debug( "Id: " + id );
                }
                result = beanObject.getInstance( db_, id );
            }
            else {
                result = beanObject.getInstanceByCode( db_, code );
            }

            if (result!=null) {
                result.setParameters( renderRequest, renderResponse, portletConfig );
            } else {
                out.write( ("Error create portlet "+portletConfig.getPortletName()).getBytes() );
                return;
            }

            byte[] bytes = null;
            if ( isXml() ) {
                if (xmlRoot==null) {
                    bytes = result.getXml();
                }
                else {
                    bytes = result.getXml( xmlRoot );
                }
            }
            else {
                bytes = result.getPlainHTML();
            }
/*
            if (log.isDebugEnabled()) {
                String fileName =
                    WebmillConfig.getWebmillTempDir() +
                    renderResponse.getNamespace() +
                    "-portlet-data."+(isXml()?"xml":"bin");
                log.debug( "write portlet result to file "+fileName );
                MainTools.writeToFile( fileName, bytes );
                fileName =
                    WebmillConfig.getWebmillTempDir() +
                    renderResponse.getNamespace() +
                    "-0-portlet-data."+(isXml()?"xml":"bin");
                log.debug( "write portlet result to file "+fileName );
                MainTools.writeToFile( fileName, new String(bytes, "utf8" ).getBytes( "utf8" ) );
            }
*/
            if (bytes!=null)
                out.write( bytes );
        }
        catch (Exception e){
            final String es = "Error get " + portletConfig.getPortletName();
            log.error(es, e);
            throw new PortletException(es, e);
        }
        finally{
            DatabaseManager.close( db_ );
            db_ = null;
            out.flush();
            out.close();
            out = null;
        }
    }
}
