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



import java.util.HashMap;

import java.util.Locale;

import java.util.Map;

import java.util.List;

import java.util.ArrayList;



import javax.portlet.PortletRequest;

import javax.portlet.PortletResponse;

import javax.servlet.http.Cookie;

import javax.servlet.http.HttpServletRequest;

import javax.servlet.http.HttpServletResponse;



import org.apache.log4j.Logger;

import org.riverock.common.tools.StringTools;

import org.riverock.common.tools.ServletTools;

import org.riverock.common.config.ConfigException;

import org.riverock.generic.db.DatabaseAdapter;

import org.riverock.generic.db.DatabaseManager;

import org.riverock.generic.tools.StringManager;

import org.riverock.sso.a3.AuthSession;

import org.riverock.sso.a3.AuthTools;

import org.riverock.webmill.core.*;

import org.riverock.webmill.main.Constants;

import org.riverock.webmill.port.InitPage;

import org.riverock.webmill.portal.PortalConstants;

import org.riverock.webmill.portal.menu.MenuInterface;

import org.riverock.webmill.portal.menu.MenuItemInterface;

import org.riverock.webmill.portlet.wrapper.RenderRequestWrapper;

import org.riverock.webmill.portlet.wrapper.RenderResponseWrapper;

import org.riverock.webmill.schema.core.SiteCtxCatalogItemType;

import org.riverock.webmill.schema.core.SiteCtxLangCatalogItemType;

import org.riverock.webmill.schema.core.SiteSupportLanguageItemType;

import org.riverock.webmill.schema.types.HiddenParamType;



public class CtxInstance {



    private static Logger log = Logger.getLogger(CtxInstance.class);



    public CtxInstance(

        HttpServletRequest request_,

        HttpServletResponse response_,

        InitPage page,

        DatabaseAdapter db)

        throws Exception

    {

        this.request = request_;

        this.response = response_;

        this.auth = AuthTools.getAuthSession(request);

        this.page = page;



        // init real locale. Can be rewrited below, based on current context

        this.realLocale = page.getLocale();



        initTypeContext(

            page.p.getIdSupportLanguage(realLocale), db, request_

        );

        setDefaultPortletDescription( PortletDescription.getInstance( defaultPortletType ) );



        this.portletRequest = new RenderRequestWrapper(

            new HashMap(),

            request,

            this.auth,

            realLocale,

            page.getPreferredLocale()

        );

        portletRequest.setAttribute(PortalConstants.PORTAL_AUTH_ATTRIBUTE, auth);

    }





    private HttpServletRequest request = null;

    private HttpServletResponse response = null;

    private AuthSession auth = null;



    public InitPage page = null;

    public StringManager sCustom = null;





    private String nameLocaleBundle = null;



    private PortletRequest portletRequest = null;

    private PortletResponse portletResponse = null;

    private Map parameters = null;



    // info about default portlet

    private PortletDescription portletDescription = null;

    private String defaultPortletType = null;

    private String nameTemplate = null;

    private SiteCtxCatalogItemType ctx = null;

    private Locale realLocale = null;



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

        return request.getRemoteAddr();

    }



    public Cookie[] getCookies()

    {

        return request.getCookies();

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

            request,

            this.auth,

            this.realLocale,

            this.page.getPreferredLocale()

        );

        this.portletResponse = new RenderResponseWrapper(



        );



        this.nameLocaleBundle = nameLocaleBunble;

        if ((nameLocaleBunble != null) && (nameLocaleBunble.trim().length() != 0))

            sCustom = StringManager.getManager(nameLocaleBunble, realLocale);

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

    void initTypeContext(Long idSiteSupportLanguage, DatabaseAdapter db, HttpServletRequest request)

    throws Exception

    {

        try

        {

            if (request.getServletPath().startsWith(Constants.PAGEID_SERVLET_NAME) )

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



                initFromContext(db, ctxId, request);

            }

            else if (request.getServletPath().startsWith(Constants.PAGE_SERVLET_NAME) )

            {

                String path = request.getPathInfo();

                if (path==null || path.equals("/"))

                {

                    // init as 'index' page

                    internalInitTypeContext(request);

                    return;

                }

                int idx = path.indexOf('/', 1);

                String pageName = null;

                if (idx==-1)

                    pageName = path.substring(1);

                else

                    pageName = path.substring(1, idx);



                Long ctxId = DatabaseManager.getLongValue(

                    db,

                    "select a.ID_SITE_CTX_CATALOG " +

                    "from SITE_CTX_CATALOG a, SITE_CTX_LANG_CATALOG b " +

                    "where a.ID_SITE_CTX_LANG_CATALOG=b.ID_SITE_CTX_LANG_CATALOG and " +

                    "b.ID_SITE_SUPPORT_LANGUAGE=? and a.CTX_PAGE_URL=?",

                    new Object[]{idSiteSupportLanguage, pageName}

                );



                if (ctxId==null)

                {

                    log.error("Context with pageName "+pageName+" not found. process as 'index' page");

                    internalInitTypeContext(request);

                    return;

                }

                else

                    initFromContext(db, ctxId, request);

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

            log.error("Dump all menu for "+realLocale+" locale");

            try

            {

                for (int i=0; i<page.menuLanguage.getMenuCount(); i++)

                {

                    MenuInterface catalog = page.menuLanguage.getMenu(i);

                    for (int k=0; k<catalog.getMenuItemCount(); k++)

                    {

                        MenuItemInterface menuItem = catalog.getMenuItem(k);

                        log.error("Menu item: "+menuItem.toString());

                    }

                }

            }

            catch (Exception e)

            {

                log.error("Exception in dumping menu", e);

            }

            throw new Exception(errorString);

        }

    }



    private void initFromContext(DatabaseAdapter db, Long ctxId, HttpServletRequest request)

        throws Exception

    {

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

            log.error("Requested context with id "+ctx.getIdSiteCtxCatalog()+" is from others site. Process as 'index' page");

            internalInitTypeContext(request);

            return;

        }

        internalInitPageCtx(db, ctx, siteLang);

    }





    private void internalInitPageCtx(DatabaseAdapter db_, SiteCtxCatalogItemType ctx, SiteSupportLanguageItemType siteLang)

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



        realLocale = StringTools.getLocale(siteLang.getCustomLanguage());

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



    public HttpServletRequest getRequest()

    {

        return request;

    }



    public HttpServletResponse getResponse()

    {

        return response;

    }



    public String url(String portlet)

        throws ConfigException

    {

        return url(portlet, nameTemplate);

    }

    public String url(String portlet, String templateParam)

        throws ConfigException

    {

        return response.encodeURL( CtxURL.ctx()  ) + '?' +

            getAsURL()+

            Constants.NAME_TEMPLATE_CONTEXT_PARAM + '=' +templateParam+ '&' +

            Constants.NAME_TYPE_CONTEXT_PARAM + '=' + portlet;

    }

    private static HiddenParamType getHidden(String name, String value)

    {

        HiddenParamType hidden = new HiddenParamType();

        hidden.setHiddenParamName(name);

        hidden.setHiddenParamValue(value);

        return hidden;

    }



    public List getAsList()

    {

        List v = new ArrayList(1);



        if (realLocale != null)

            v.add( getHidden( Constants.NAME_LANG_PARAM, realLocale.toString()));



        return v;

    }



    public String getAsURL()

    {

        return Constants.NAME_LANG_PARAM + "=" + realLocale.toString() + "&";

    }



    public String getAsForm()

    {

        return "<input type=\"hidden\" name=\"" + Constants.NAME_LANG_PARAM + "\" value=\"" + realLocale.toString() + "\">";

    }



    public String getAsUrlXML()

    {

        return Constants.NAME_LANG_PARAM + "=" + realLocale.toString() + "&amp;";

    }



    public String getAsFormXML()

    {

        return "<HiddenParam><HiddenParamName>" + Constants.NAME_LANG_PARAM + "</HiddenParamName><HiddenParamValue>" + realLocale.toString() + "</HiddenParamValue></HiddenParam>";

    }



    public String urlAsForm(String nameTemplate, String portlet)

    {

        return

            getAsForm()+

            ServletTools.getHiddenItem(Constants.NAME_TEMPLATE_CONTEXT_PARAM, nameTemplate)+

            ServletTools.getHiddenItem(Constants.NAME_TYPE_CONTEXT_PARAM, portlet);

    }



}

