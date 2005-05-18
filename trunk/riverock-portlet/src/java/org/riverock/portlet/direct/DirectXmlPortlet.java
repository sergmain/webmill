/*
 * org.riverock.portlet -- Portlet Library
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
package org.riverock.portlet.direct;

import java.util.List;
import java.util.ResourceBundle;
import java.io.IOException;

import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.PortletConfig;
import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletRequestDispatcher;
import javax.portlet.Portlet;

import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.webmill.portlet.PortletResultObject;
import org.riverock.webmill.portlet.PortletResultContent;
import org.riverock.webmill.portlet.GenericWebmillPortlet;
import org.riverock.webmill.portal.PortalConstants;
import org.riverock.portlet.article.ArticleXmlPortlet;
import org.riverock.portlet.article.ArticleXml;

import org.apache.log4j.Logger;

/**
 * User: mill
 * Date: 07.12.2004
 * Time: 17:04:03
 * $Id$
 */
public final class DirectXmlPortlet implements Portlet {

    private final static Logger log = Logger.getLogger( DirectXmlPortlet.class );

    public DirectXmlPortlet(){}

    private PortletConfig portletConfig = null;
    public void init( final PortletConfig portletConfig ) {
        this.portletConfig = portletConfig;
    }

    public void processAction( final ActionRequest actionRequest, final ActionResponse actionResponse ) {
    }

    public void render( final RenderRequest renderRequest, final RenderResponse renderResponse) throws PortletException, IOException {

        String path = (String)renderRequest.getAttribute( PortalConstants.PORTAL_URL_RESOURCE_ATTRIBUTE );

        if ( path==null )
            return;

        if ( log.isDebugEnabled() ) {
            log.debug( "process url: " + path );
            log.debug( "renderRequest: " + renderRequest );
            log.debug( "renderResponse: " + renderResponse );
        }

        DirectProcessor.process( renderRequest, renderResponse );
    }

    public void destroy() {
        this.portletConfig = null;
    }
}