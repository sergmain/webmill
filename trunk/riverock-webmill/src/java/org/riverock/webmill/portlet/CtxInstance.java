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

import java.util.*;

import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.portlet.PortletURL;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.riverock.common.config.ConfigException;
import org.riverock.common.html.AcceptLanguageWithLevel;
import org.riverock.common.html.Header;
import org.riverock.common.tools.ServletTools;
import org.riverock.common.tools.StringTools;
import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.db.DatabaseManager;
import org.riverock.generic.tools.StringManager;
import org.riverock.generic.config.GenericConfig;
import org.riverock.sso.a3.AuthSession;
import org.riverock.sso.a3.AuthTools;
import org.riverock.webmill.core.*;
import org.riverock.webmill.exception.PortalException;
import org.riverock.webmill.main.Constants;
import org.riverock.webmill.port.PortalInfo;
import org.riverock.webmill.portal.PortalConstants;
import org.riverock.webmill.portal.menu.MenuInterface;
import org.riverock.webmill.portal.menu.MenuItemInterface;
import org.riverock.webmill.portal.menu.MenuLanguageInterface;
import org.riverock.webmill.portlet.wrapper.RenderRequestImpl;
import org.riverock.webmill.portlet.wrapper.RenderResponseImpl;
import org.riverock.webmill.portlet.wrapper.PortletURLImpl;
import org.riverock.webmill.schema.core.SiteCtxCatalogItemType;
import org.riverock.webmill.schema.core.SiteCtxLangCatalogItemType;
import org.riverock.webmill.schema.core.SiteSupportLanguageItemType;
import org.riverock.webmill.schema.core.SiteSupportLanguageListType;
import org.riverock.webmill.schema.types.HiddenParamType;
import org.riverock.webmill.utils.ServletUtils;
import org.riverock.interfaces.schema.javax.portlet.PortletType;

public class CtxInstance {

    private static Logger log = Logger.getLogger(CtxInstance.class);

    public CtxInstance(
        HttpServletRequest request_,
        HttpServletResponse response_,
        DatabaseAdapter db)
        throws Exception
    {
        this.request = request_;
        this.response = response_;
        this.auth = AuthTools.getAuthSession(request);

        initPortalInfo(db);
        // Todo - filter preferredLocale with locale defined for this site
        tempLocale = getPreferredLocale(db);
        if (tempLocale==null)
            tempLocale = portalInfo.getDefaultLocale();

        if (tempLocale==null)
            tempLocale = StringTools.getLocale( ServletUtils.getString(request, Constants.NAME_LANG_PARAM) );

        if (tempLocale==null)
        {
            String es = "Locales for this site not defined";
            log.error(es);
            throw new PortalException(es);
        }


        initTypeContext(db);
        setDefaultPortletDescription( PortletDescription.getInstance( defaultPortletType ) );

        preferredLocale = Header.getAcceptLanguageAsLocaleListSorted( request );
        if (this.realLocale==null)
        {

            realLocale = (tempLocale!=null?tempLocale:Locale.ENGLISH);
            log.warn("Locale of request is null, process as "+realLocale.toString());
        }

        // init string manager with real locale
        stringManager = StringManager.getManager("mill.locale.main", realLocale);




        this.portletRequest = new RenderRequestImpl(
            new HashMap(),
            request,
            this.auth,
            realLocale,
            preferredLocale
        );
        this.portletResponse = new RenderResponseImpl(response);
        portletRequest.setAttribute(PortalConstants.PORTAL_AUTH_ATTRIBUTE, auth);
    }

    private Locale[] preferredLocale = null;

    private HttpServletRequest request = null;
    private HttpServletResponse response = null;
    private AuthSession auth = null;

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
    private Locale tempLocale = null; // used for process request with ref page alias - '../page/home'
    private PortalInfo portalInfo = null;
    private StringManager stringManager = null;

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
        addGlobalParameters(this.parameters);

        this.portletRequest = new RenderRequestImpl(
            parameters,
            request,
            this.auth,
            this.realLocale,
            this.preferredLocale
        );
        this.portletResponse = new RenderResponseImpl(

        );

        if (log.isDebugEnabled())
            log.debug("nameLocaleBunble: "+nameLocaleBunble);
        
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

        if (namePortletId!=null && portletId!=null)
        {
            if (map.get(namePortletId)==null)
                map.put(namePortletId, portletId);
        }
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
    void initTypeContext(DatabaseAdapter db)
    throws Exception
    {
        try
        {
            if (request.getServletPath().startsWith(Constants.PAGEID_SERVLET_NAME) )
            {
                log.debug("Start process as /pageid");

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
                log.debug("Start process as page, format request: /<CONTEXT>/page/<PAGE_NAME>/<LOCALE>/...");
                // format request: /<CONTEXT>/page/<PAGE_NAME>/<LOCALE>/...
                String path = request.getPathInfo();
                if (path==null || path.equals("/"))
                {
                    // init as 'index' page
                    internalInitTypeContext(request);
                    return;
                }
                int idx = path.indexOf('/', 1);
                String pageName = null;
                String localeFromUrl = null;
                if (idx==-1)
                {
                    pageName = path.substring(1);
                    localeFromUrl = tempLocale.toString();
                }
                else
                {
                    pageName = path.substring(1, idx);
                    int idxLocale = path.indexOf('/', idx+1);
                    if (idxLocale==-1)
                        localeFromUrl = path.substring(idx+1);
                    else
                        localeFromUrl = path.substring(idx+1, idxLocale);
                }

                Long ctxId = DatabaseManager.getLongValue(
                    db,
                    "select a.ID_SITE_CTX_CATALOG " +
                    "from   SITE_CTX_CATALOG a, SITE_CTX_LANG_CATALOG b, SITE_SUPPORT_LANGUAGE c " +
                    "where  a.ID_SITE_CTX_LANG_CATALOG=b.ID_SITE_CTX_LANG_CATALOG and " +
                    "       b.ID_SITE_SUPPORT_LANGUAGE=c.ID_SITE_SUPPORT_LANGUAGE and " +
                    "       c.ID_SITE=? and c.CUSTOM_LANGUAGE=? and " +
                    "       a.CTX_PAGE_URL=?",
                    new Object[]{portalInfo.getSiteId(), localeFromUrl, pageName}
                );

                if (ctxId==null)
                {
                    log.error("Context with pageName "+pageName+" not found. process as 'index' page");
                    internalInitTypeContext(request);
                    return;
                }
                else
                {
                    log.debug("Start process as /ctx");
                    initFromContext(db, ctxId, request);
                }
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
            nameTemplate = portalInfo.getMenu(realLocale.toString()).getIndexTemplate();

        if ( nameTemplate==null )
        {
            String errorString = "Template for page with type 'mill.index' not found";
            log.error( errorString );
            log.error("Dump all menu for "+realLocale+" locale");
            try
            {
                MenuLanguageInterface ml = portalInfo.getMenu(realLocale.toString());
                for (int i=0; i<ml.getMenuCount(); i++)
                {
                    MenuInterface catalog = ml.getMenu(i);
                    ListIterator it = catalog.getMenuItem().listIterator();
                    while (it.hasNext()) {
                        MenuItemInterface menuItem = (MenuItemInterface)it.next();
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
        if (!getPortalInfo().getSiteId().equals(siteLang.getIdSite()))
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

        PortletType portlet = PortletManager.getPortletDescription( defaultPortletType );
        if (portlet==null)
            return;

        namePortletId =
            PortletTools.getStringParam(
                portlet, PortletTools.name_portlet_id
            );

        if (namePortletId==null)
            return;

        portletId = ctx.getIdContext();
    }
    private String namePortletId = null;
    private Long portletId = null;



    private void internalInitTypeContext(HttpServletRequest request)
    throws Exception
    {
        String ctxType = request.getParameter( Constants.NAME_TYPE_CONTEXT_PARAM );

        String ctxTemplate =
            request.getParameter( Constants.NAME_TEMPLATE_CONTEXT_PARAM );

        if ( log.isDebugEnabled() )
        {
            log.debug( "internalInitTypeContext(). TEMPLATE: "+ctxTemplate );
            log.debug( "internalInitTypeContext(). type context: "+ctxType );
            log.debug( "internalInitTypeContext(). tempLocale.toString(): "+tempLocale.toString());
        }

        this.realLocale = StringTools.getLocale( ServletUtils.getString(request, Constants.NAME_LANG_PARAM, tempLocale.toString()) );
        // If not found name of template or type of context, processing as index_page

        if ( log.isDebugEnabled() )
            log.debug( "internalInitTypeContext(). realLocale: "+realLocale.toString() );

        if ( ctxType==null || ctxTemplate==null )
        {
            defaultPortletType = Constants.CTX_TYPE_INDEX;
            if ( log.isDebugEnabled() )
                log.debug( "realLocale: "+realLocale );

            MenuLanguageInterface menu = portalInfo.getMenu(realLocale.toString());
            if (menu==null)
                log.error( "menu is null, locale: "+realLocale.toString() );
            else
                log.debug( "menu: "+menu );

            nameTemplate = menu.getIndexTemplate();
            return;
        }

        this.defaultPortletType = ctxType;
        this.nameTemplate = ctxTemplate;
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
        return response.encodeURL( ctx()  ) + '?' +
            getAsURL()+
            Constants.NAME_TYPE_CONTEXT_PARAM + '=' + portlet +
            (templateParam!=null?("&" + Constants.NAME_TEMPLATE_CONTEXT_PARAM + '=' +templateParam):"")
            ;
    }

    public PortletURL getURL()
    {
        return new PortletURLImpl(this, request, response);
    }

    public static String ctx()
    {
        if (GenericConfig.contextName  == null)
            return Constants.URI_CTX_MANAGER ;

        return GenericConfig.contextName + Constants.URI_CTX_MANAGER ;
    }

    public static String pageid()
    {
        if (GenericConfig.contextName  == null)
            return Constants.PAGEID_SERVLET_NAME ;

        return GenericConfig.contextName + Constants.PAGEID_SERVLET_NAME ;
    }

    public static String page()
    {
        if (GenericConfig.contextName  == null)
            return Constants.PAGE_SERVLET_NAME ;

        return GenericConfig.contextName + Constants.PAGE_SERVLET_NAME ;
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

    private void initPortalInfo(DatabaseAdapter db_)
        throws PortalException
    {
        long jspPagePortletInfo = 0;
        try
        {
            if (log.isInfoEnabled())
            {
                if (log.isInfoEnabled())
                    log.info("start get instance of PortalInfo ");

                jspPagePortletInfo = System.currentTimeMillis();
            }
            portalInfo = PortalInfo.getInstance(db_, request.getServerName());
        }
        catch(Exception e)
        {
            String es = "Error PortalInfo.getInstance(db_, request.getServerName())";
            log.error(es, e);
            throw new PortalException(es, e);
        }
        finally
        {
            if (log.isInfoEnabled())
            {
                log.info("Get instance of PortalInfo  for  "
                        + (System.currentTimeMillis()-jspPagePortletInfo)+" milliseconds");
            }
        }
    }

    public PortalInfo getPortalInfo()
    {
        return portalInfo;
    }


    private Locale getPreferredLocale(DatabaseAdapter db_)
        throws Exception
    {
        // determinate preffered locale
        // AcceptLanguageWithLevel[] accept =
        List acceptVector = Header.getAcceptLanguageAsList( request );

        SiteSupportLanguageListType supportLanguageList =
              GetSiteSupportLanguageWithIdSiteList.getInstance(db_, portalInfo.sites.getIdSite()).item;

        if (log.isDebugEnabled())
            log.debug("Start looking for preffered locale");

        // Locale not queried in URL
        Locale tempLocale = null;
        AcceptLanguageWithLevel bestAccept = null;
        SiteSupportLanguageItemType includedFromCookie = null;

        Cookie[] cookies_req = request.getCookies();
        if (cookies_req!=null)
        {
            for (int i = 0; i<cookies_req.length; i++)
            {
                Cookie c = cookies_req[i];
                String name_cookie = c.getName();
                if (name_cookie.equals( Constants.NAME_LOCALE_COOKIE))
                {
                    includedFromCookie =
                        includedAccept(StringTools.getLocale( c.getValue()) , supportLanguageList );
                    break;
                }
            }
        }

        if (includedFromCookie!=null)
        {
            tempLocale = StringTools.getLocale(includedFromCookie.getCustomLanguage());
        }
        else
        {
            if (log.isDebugEnabled())
            {
                log.debug("Total accepted locale vector - "+ acceptVector);
                if (acceptVector!=null)
                    log.debug("Total accepted locale elements - "+ acceptVector.size());
            }

            if (acceptVector!=null)
            {
                for (int i=0; i<acceptVector.size(); i++)
                {
                    AcceptLanguageWithLevel accept = (AcceptLanguageWithLevel)acceptVector.get(i);

                    if (log.isDebugEnabled())
                        log.debug("Accepted locale item, locale - "+ accept.locale+", level - "+ accept.level);

                    SiteSupportLanguageItemType included = includedAccept(accept.locale, supportLanguageList );

                    if (log.isDebugEnabled())
                        log.debug("included - "+included);

                    if  (bestAccept==null)
                    {
                        if (included!=null)
                        {
                            bestAccept = accept;
                            tempLocale = StringTools.getLocale( included.getCustomLanguage() );
                        }
                    }
                    else
                    {
                        if (included!=null && bestAccept.level<accept.level)
                        {
                            bestAccept = accept;
                            tempLocale = StringTools.getLocale( included.getCustomLanguage() );
                        }
                    }

                    if (log.isDebugEnabled())
                        log.debug("tempLocale - "+tempLocale);

                }
            }
        }
        return tempLocale;
    }

    private SiteSupportLanguageItemType includedAccept( Locale accept, SiteSupportLanguageListType supportLanguageList )
    {
        boolean hasVariant = (accept.getVariant()!=null && accept.getVariant().trim().length()>0);
        boolean hasCountry = (accept.getCountry()!=null && accept.getCountry().trim().length()>0);

        if (log.isDebugEnabled())
            log.debug("accept locale: "+accept.toString()+", hasVariant - "+hasVariant + ", hasCountry - "+hasCountry);

        SiteSupportLanguageItemType result = null;
        Locale rl = null;
        SiteSupportLanguageItemType resultTemp = null;
        Locale rlTemp = null;
        for (int i=0; i<supportLanguageList.getSiteSupportLanguageCount(); i++)
        {
            SiteSupportLanguageItemType sl = supportLanguageList.getSiteSupportLanguage(i);

            Locale cl = StringTools.getLocale( sl.getCustomLanguage() );

            if (log.isDebugEnabled())
                log.debug("SiteSupportLanguageItemType.getNameCustomLanguage locale - "+cl.toString());

            if (accept.toString().equalsIgnoreCase(cl.toString()))
                return sl;

            boolean hasCountryTemp = (cl.getCountry()!=null && cl.getCountry().trim().length()>0);

            if (hasCountry && hasCountryTemp)
            {
                if (accept.getCountry().equalsIgnoreCase(cl.getCountry()) &&
                    accept.getLanguage().equalsIgnoreCase(cl.getLanguage()) )
                {
                    if (log.isDebugEnabled()) log.debug("new resultTemp locale is: "+cl.toString());
                    resultTemp = sl;
                    rlTemp = cl;
                }
            }
            else if (accept.getLanguage().equalsIgnoreCase(cl.getLanguage()) )
            {
                if (log.isDebugEnabled()) log.debug("new resultTemp locale is: "+cl.toString());
                resultTemp = sl;
                rlTemp = cl;
            }

            if (result==null)
            {
                result = resultTemp;
                rl = cl;
                if (log.isDebugEnabled()) log.debug("#1, new result locale is: "+cl.toString());
            }
            else
            {
                boolean hasVariantResult = (rl.getVariant()!=null && rl.getVariant().trim().length()>0);
                boolean hasCountryResult = (rl.getCountry()!=null && rl.getCountry().trim().length()>0);
                boolean hasVariantResultTemp = (rlTemp.getVariant()!=null && rlTemp.getVariant().trim().length()>0);
                boolean hasCountryResultTemp = (rlTemp.getCountry()!=null && rlTemp.getCountry().trim().length()>0);
                if (hasVariantResult && !hasVariantResultTemp) {
                    result = resultTemp;
                    rl = cl;
                    if (log.isDebugEnabled()) log.debug("#2, new result locale is: "+cl.toString());
                }
                else if (hasCountryResult && !hasCountryResultTemp) {
                    result = resultTemp;
                    rl = cl;
                    if (log.isDebugEnabled()) log.debug("#3, new result locale is: "+cl.toString());
                }
            }
        }

        if (log.isDebugEnabled()) log.debug("Result SiteSupportLanguageItemType.getCustomLanguage is: "+result.getCustomLanguage());
        return result;
    }

    public StringManager getStringManager()
    {
        return stringManager;
    }

    public String getNamePortletId()
    {
        return namePortletId;
    }

    public Long getPortletId()
    {
        return portletId;
    }

}
