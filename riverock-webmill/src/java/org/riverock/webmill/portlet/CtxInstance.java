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



/**

 * User: Admin

 * Date: Aug 26, 2003

 * Time: 4:40:19 PM

 *

 * $Id$

 */

package org.riverock.webmill.portlet;



import java.io.ByteArrayOutputStream;

import java.util.List;

import java.util.Map;



import javax.servlet.http.HttpServletRequest;

import javax.servlet.http.HttpServletResponse;

import javax.servlet.http.HttpSession;

import javax.servlet.http.Cookie;

import javax.portlet.PortletRequest;



import org.riverock.webmill.port.InitPage;

import org.riverock.webmill.port.PortalXslt;

import org.riverock.webmill.schema.site.SiteTemplate;

import org.riverock.webmill.portlet.wrapper.RenderRequestWrapper;

import org.riverock.webmill.portlet.wrapper.PortletSessionWrapper;

import org.riverock.generic.db.DatabaseAdapter;

import org.riverock.generic.tools.StringManager;



public class CtxInstance {



    public HttpServletRequest req = null;

    public HttpServletResponse response = null;

    public HttpSession session = null;

    public InitPage page = null;

    public StringManager sCustom = null;

    private String nameLocaleBundle = null;



    public ByteArrayOutputStream byteArrayOutputStream = null;



    public PortalXslt xslt = null;

    public SiteTemplate template = null;

    public List portlets = null;



    public int counter;

    public long startMills;

    public DatabaseAdapter db = null;



    private PortletRequest portletRequest = null;

    private Map parameters = null;



    public String getRemoteAddr()

    {

        return req.getRemoteAddr();

    }



    public Cookie[] getCookies()

    {

        return req.getCookies();

    }



    public CtxInstance()

    {

        startMills = System.currentTimeMillis();

    }



    public String getType()

    {

        return page.getType();

    }



    public String getNameTemplate()

    {

        return page.getNameTemplate();

    }



    public void setParameters(Map parameters, String nameLocaleBunble)

    {

        this.parameters = null;

        this.portletRequest = null;



        this.parameters = parameters;

        this.portletRequest = new RenderRequestWrapper(

            parameters,

            new PortletSessionWrapper(req.getSession(true))

        );

        this.nameLocaleBundle = nameLocaleBunble;

        if ((nameLocaleBunble != null) && (nameLocaleBunble.trim().length() != 0))

            sCustom = StringManager.getManager(nameLocaleBunble, page.currentLocale);

    }



    public Map getParameters()

    {

        return parameters;

    }



    public PortletRequest getPortletRequest()

    {

        return portletRequest;

    }



    public String getNameLocaleBundle()

    {

        return nameLocaleBundle;

    }



}

