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

/**
 *
 * $Author$
 *
 * $Id$
 *
 */
package org.riverock.portlet.portlets.direct;

import java.util.List;
import java.util.ResourceBundle;

import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.PortletConfig;

import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.webmill.portlet.PortletGetList;
import org.riverock.webmill.portlet.PortletResultObject;
import org.riverock.webmill.portlet.PortletResultContent;

import org.apache.log4j.Logger;

/**
 * User: mill
 * Date: 07.10.2004
 * Time: 17:03:49
 * $Id$
 */
public final class DirectPlainPortlet implements PortletResultObject, PortletGetList, PortletResultContent {
    private final static Logger log = Logger.getLogger( DirectPlainPortlet.class );

    public Long id = null;
    public Long idSupportLanguage = null;

    private RenderRequest renderRequest = null;
    private RenderResponse renderResponse = null;
    private ResourceBundle bundle = null;

    public void setParameters( RenderRequest renderRequest, RenderResponse renderResponse, PortletConfig portletConfig ) {
        this.renderRequest = renderRequest;
        this.renderResponse = renderResponse;
        this.bundle = bundle;
    }

    protected void finalize() throws Throwable {
        super.finalize();
    }

    public PortletResultContent getInstance(DatabaseAdapter db__) throws PortletException {
        return null;
    }

    public byte[] getXml(String name) throws PortletException{
        return null;
    }

    public byte[] getXml() throws PortletException {
        return null;
    }

    public DirectPlainPortlet(){}

    public PortletResultContent getInstance(DatabaseAdapter db__, Long id__)
        throws PortletException{
        return null;
    }

    public PortletResultContent getInstanceByCode(DatabaseAdapter db__, String articleCode_)
        throws PortletException{
        return null;
    }

    public DirectPlainPortlet(DatabaseAdapter db_, Long id_) {
    }

    public byte[] getPlainHTML(){
        return null;
    }

    public List getList( Long idSiteCtxLangCatalog, Long idContext){
        return null;
    }
}
