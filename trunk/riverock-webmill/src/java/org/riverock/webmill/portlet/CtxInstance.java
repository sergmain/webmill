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

import java.util.HashMap;



import javax.portlet.PortletRequest;

import javax.servlet.http.Cookie;

import javax.servlet.http.HttpServletRequest;

import javax.servlet.http.HttpServletResponse;

import javax.servlet.http.HttpSession;



import org.apache.log4j.Logger;

import org.riverock.generic.db.DatabaseAdapter;

import org.riverock.generic.tools.StringManager;

import org.riverock.webmill.core.GetSiteCtxCatalogItem;

import org.riverock.webmill.core.GetSiteCtxLangCatalogItem;

import org.riverock.webmill.core.GetSiteCtxTypeItem;

import org.riverock.webmill.core.GetSiteSupportLanguageItem;

import org.riverock.webmill.core.GetSiteTemplateItem;

import org.riverock.webmill.main.Constants;

import org.riverock.webmill.port.InitPage;

import org.riverock.webmill.port.PortalXslt;

import org.riverock.webmill.portlet.wrapper.PortletSessionWrapper;

import org.riverock.webmill.portlet.wrapper.RenderRequestWrapper;

import org.riverock.webmill.schema.core.SiteCtxCatalogItemType;

import org.riverock.webmill.schema.core.SiteCtxLangCatalogItemType;

import org.riverock.webmill.schema.core.SiteSupportLanguageItemType;

import org.riverock.webmill.schema.site.SiteTemplate;



public class CtxInstance {



    private static Logger log = Logger.getLogger(CtxInstance.class);



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

    DatabaseAdapter db = null;



    private PortletRequest portletRequest = null;

    private Map parameters = null;



    // info about default portlet

    private PortletDescription portletDescription = null;

    private String defaultPortletType = null;

    private String nameTemplate = null;

    private SiteCtxCatalogItemType ctx = null;



    public String getNameTemplate()

    {

        return nameTemplate;

    }



    public String getDefaultPortletType()

    {

        return defaultPortletType;

    }



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



    public PortletDescription getDefaultPortletDescription()

    {

        return portletDescription;

    }



    public void setParameters(Map parameters, String nameLocaleBunble)

    {

        this.parameters = null;

        this.portletRequest = null;



        this.parameters = parameters;

        addGlobalParameters(parameters);



        this.portletRequest = new RenderRequestWrapper(

            parameters,

            new PortletSessionWrapper(req.getSession(true))

        );

        this.nameLocaleBundle = nameLocaleBunble;

        if ((nameLocaleBunble != null) && (nameLocaleBunble.trim().length() != 0))

            sCustom = StringManager.getManager(nameLocaleBunble, page.currentLocale);

    }



    private void addGlobalParameters(Map map)

    {

        if (map.get(Constants.NAME_TYPE_CONTEXT_PARAM)==null && defaultPortletType!=null)

            map.put(Constants.NAME_TYPE_CONTEXT_PARAM, defaultPortletType);



        if (map.get(Constants.NAME_TEMPLATE_CONTEXT_PARAM)==null && nameTemplate!=null)

            map.put(Constants.NAME_TEMPLATE_CONTEXT_PARAM, nameTemplate);



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



    void setDefaultPortletDescription(PortletDescription portletDescription)

    {

        this.portletDescription = portletDescription;

    }



    /**

     * init context type and name of template,

     * if type of context is null, set it to 'index_page'

     */

    void initTypeContext(HttpServletRequest request)

    throws Exception

    {

        try

        {

            if (request.getServletPath().startsWith(Constants.PAGE_SERVLET_NAME) )

            {

                String path = request.getPathInfo();

                if (path==null || path.equals("/"))

                {

                    // init as 'index' page

                    internalInitTypeContext(request);

                    return;

                }

                int idx = path.indexOf('/', 1);

                Long ctxId = null;

                if (idx==-1)

                    ctxId = new Long(path.substring(1));

                else

                    ctxId = new Long(path.substring(1, idx));



                ctx = GetSiteCtxCatalogItem.getInstance(db, ctxId).item;

                if (ctx==null)

                {

                    log.error("Context with id "+ctxId+" not found. process as 'index' page");

                    internalInitTypeContext(request);

                    return;

                }



                SiteCtxLangCatalogItemType langMenu = GetSiteCtxLangCatalogItem.getInstance(db, ctx.getIdSiteCtxLangCatalog()).item;

                if (langMenu==null)

                {

                    log.error("Lang Catalog with id "+ctx.getIdSiteCtxLangCatalog()+" not found. process as 'index' page");

                    internalInitTypeContext(request);

                    return;

                }



                SiteSupportLanguageItemType siteLang = GetSiteSupportLanguageItem.getInstance(db, langMenu.getIdSiteSupportLanguage()).item;

                if (siteLang==null)

                {

                    log.error("Site language with id "+langMenu.getIdSiteSupportLanguage()+" not found. process as 'index' page");

                    internalInitTypeContext(request);

                    return;

                }

                if (!page.p.getSiteId().equals(siteLang.getIdSite()))

                {

                    log.error("Requested context with id "+ctxId+" is from others site. Process as 'index' page");

                    internalInitTypeContext(request);

                    return;

                }

                internalInitPageCtx(db, ctx);

            }

            else

                internalInitTypeContext(request);

        }

        finally

        {



            if ( log.isDebugEnabled() )

            {

                log.debug( "getTypeContext(). type: "+defaultPortletType );

                log.debug( "getTypeContext(). template: "+nameTemplate );

            }

        }



        if ( nameTemplate==null )

            nameTemplate = page.menuLanguage.getIndexTemplate();



        if ( nameTemplate==null )

        {

            String errorString = "Template for page with type 'mill.index' not found";

            log.error( errorString );

            throw new Exception(errorString);

        }

    }





    private void internalInitPageCtx(DatabaseAdapter db_, SiteCtxCatalogItemType ctx)

        throws Exception

    {

        String ctxType = GetSiteCtxTypeItem.getInstance(db_, ctx.getIdSiteCtxType()).item.getType();

        String ctxTemplate = GetSiteTemplateItem.getInstance(db_, ctx.getIdSiteTemplate()).item.getNameSiteTemplate();



        if ( log.isDebugEnabled() )

        {

            log.debug( "getTypeContext(). TEMPLATE: "+ctxTemplate );

            log.debug( "getTypeContext(). type context: "+ctxType );

        }



        // If not found name of template or type of context, processing as index_page

        if ( ctxType==null || ctxTemplate==null )

        {

            defaultPortletType = Constants.CTX_TYPE_INDEX;

            nameTemplate = null;

            return;

        }



        defaultPortletType = ctxType;

        nameTemplate = ctxTemplate;

    }



    private void internalInitTypeContext(HttpServletRequest request)

    throws Exception

    {

        String ctxType = request.getParameter( Constants.NAME_TYPE_CONTEXT_PARAM );



        String ctxTemplate =

            request.getParameter( Constants.NAME_TEMPLATE_CONTEXT_PARAM );



        if ( log.isDebugEnabled() )

        {

            log.debug( "getTypeContext(). TEMPLATE: "+ctxTemplate );

            log.debug( "getTypeContext(). type context: "+ctxType );

        }



        // If not found name of template or type of context, processing as index_page

        if ( ctxType==null || ctxTemplate==null )

        {

            defaultPortletType = Constants.CTX_TYPE_INDEX;

            nameTemplate = page.menuLanguage.getIndexTemplate();

            return;

        }



        defaultPortletType = ctxType;

        nameTemplate = ctxTemplate;

    }



    SiteCtxCatalogItemType getCtx()

    {

        return ctx;

    }



}

