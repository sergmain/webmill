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

 *

 * $Id$

 *

 */

package org.riverock.webmill.port;



import java.util.ArrayList;

import java.util.List;

import java.util.Locale;



import javax.servlet.http.Cookie;

import javax.servlet.http.HttpServletRequest;



import org.apache.log4j.Logger;

import org.riverock.common.config.ConfigException;

import org.riverock.common.html.AcceptLanguageWithLevel;

import org.riverock.common.html.Header;

import org.riverock.common.tools.StringTools;

import org.riverock.generic.db.DatabaseAdapter;

import org.riverock.generic.tools.StringManager;

import org.riverock.webmill.core.*;

import org.riverock.webmill.main.Constants;

import org.riverock.webmill.portal.menu.MenuLanguage;

import org.riverock.webmill.portal.menu.MenuLanguageInterface;

import org.riverock.webmill.portal.menu.SiteMenu;

import org.riverock.webmill.schema.core.*;

import org.riverock.webmill.schema.types.HiddenParamType;

import org.riverock.webmill.utils.ServletUtils;

import org.riverock.webmill.portlet.PortletDescription;



public class InitPage

{

    private static Logger log = Logger.getLogger("org.riverock.webmill.port.InitPage");



    public MenuLanguageInterface menuLanguage = null;

    public PortalInfo p = null;

    public Locale currentLocale = null;



    public StringManager sMain = null;

    public String title = "&nbsp;";



    private String type = null;

    private String nameTemplate = null;

    private Long portletId = null;

    private boolean isRequestedFromPage = false;



    private boolean isDynamic = false;



    public Long getPortletId()

    {

        return portletId;

    }



    // info about current portlet

    private PortletDescription portletDescription = null;





    public String getType()

    {

        return type;

    }



    public String getNameTemplate()

    {

        return nameTemplate;

    }



    protected void finalize() throws Throwable

    {

        menuLanguage = null;

        p = null;

        currentLocale = null;

        sMain = null;

        title = null;



        super.finalize();

    }



    public InitPage(DatabaseAdapter db_, HttpServletRequest request)

        throws Exception

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

            p = PortalInfo.getInstance(db_, request.getServerName());

        }

        catch(Exception e)

        {

            log.error("Error PortalInfo.getInstance(db_, request.getServerName())", e);

            throw e;

        }

        finally

        {

            if (log.isInfoEnabled())

            {

                log.info("Get instance of PortalInfo  for  "

                        + (System.currentTimeMillis()-jspPagePortletInfo)+" milliseconds");

            }

        }





        Locale tempLocale = getPrefferedLocale(db_, request);



        if (tempLocale==null)

            tempLocale = p.defaultLocale;



        if (log.isDebugEnabled())

            log.debug("tempLocale - "+tempLocale);



        initLocaleParameter(request, tempLocale);



        if (currentLocale == null)

            currentLocale = tempLocale;



        sMain = StringManager.getManager("mill.locale.main", currentLocale);



        String t = request.getParameter(Constants.PAGE_TITLE_SESSION);

        if (t != null)

            title = t;



        if (log.isDebugEnabled())

        {

            log.debug("start get menu for site "+p.sites.getIdSite()+", locale "+currentLocale.toString());

        }



        // Build menu

        SiteMenu sc = SiteMenu.getInstance(db_, p.sites.getIdSite());

        for (int i = 0; i < sc.getMenuLanguageCount(); i++)

        {

            MenuLanguageInterface tempCat = sc.getMenuLanguage(i);



            if (tempCat==null || tempCat.getLocaleStr()==null)

                continue;



            if (tempCat.getLocaleStr().equals( currentLocale.toString()) )

            {

                menuLanguage = tempCat;

                break;

            }

        }

        if (menuLanguage== null)

        {

            menuLanguage = new MenuLanguage();

            log.warn("Menu for locale "+currentLocale.toString()+" not found");

        }



        initTypeContext(request, db_, p);



        if ( nameTemplate==null )

            nameTemplate = menuLanguage.getIndexTemplate();



        if ( nameTemplate==null )

        {

            String errorString = "Template for page with type 'mill.index' not found";

            log.error( errorString );

            throw new Exception(errorString);

        }





        portletDescription = PortletDescription.getInstance( type );

        if (!isRequestedFromPage)

//            initPortletId( request );



        if ( log.isDebugEnabled() )

        {

            log.debug( "Portlet description "+getPortletDescription() );

            if ( getPortletDescription()==null )

                log.debug( "PortletDescription for type "+type+" not found" );

        }







    }

/*

    private void initPortletId(HttpServletRequest request)

    {

        if (portletDescription==null)

            return;



        String namePortletId = PortletTools.getStringParam( portletDescription.getPortletConfig(), PortletTools.name_portlet_id);

        if ( namePortletId!= null && namePortletId.length()!=0)

        {

            if (log.isDebugEnabled())

                log.debug("Get ID from request");



            portletId = PortletTools.getIdPortlet(namePortletId, request);



            if (log.isDebugEnabled())

                log.debug("portletId from request - "+portletId);



        }

    }

*/



    /**

     * init context type and name of template,

     * if type of context is null, set it to 'index_page'

     */

    private void initTypeContext(HttpServletRequest request, DatabaseAdapter db_, PortalInfo p)

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



                SiteCtxCatalogItemType ctx = GetSiteCtxCatalogItem.getInstance(db_, ctxId).item;

                if (ctx==null)

                {

                    log.error("Context with id "+ctxId+" not found. process as 'index' page");

                    internalInitTypeContext(request);

                    return;

                }



                SiteCtxLangCatalogItemType langMenu = GetSiteCtxLangCatalogItem.getInstance(db_, ctx.getIdSiteCtxLangCatalog()).item;

                if (langMenu==null)

                {

                    log.error("Lang Catalog with id "+ctx.getIdSiteCtxLangCatalog()+" not found. process as 'index' page");

                    internalInitTypeContext(request);

                    return;

                }



                SiteSupportLanguageItemType siteLang = GetSiteSupportLanguageItem.getInstance(db_, langMenu.getIdSiteSupportLanguage()).item;

                if (siteLang==null)

                {

                    log.error("Site language with id "+langMenu.getIdSiteSupportLanguage()+" not found. process as 'index' page");

                    internalInitTypeContext(request);

                    return;

                }

                if (!p.getSiteId().equals(siteLang.getIdSite()))

                {

                    log.error("Requested context with id "+ctxId+" is from others site. Process as 'index' page");

                    internalInitTypeContext(request);

                    return;

                }

                isRequestedFromPage = true;

                internalInitPageCtx(db_, ctx);

            }

            else

                internalInitTypeContext(request);

        }

        finally

        {



            if ( log.isDebugEnabled() )

            {

                log.debug( "getTypeContext(). type: "+type );

                log.debug( "getTypeContext(). template: "+nameTemplate );

            }

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

            type = Constants.CTX_TYPE_INDEX;

            nameTemplate = null;

            return;

        }



        type = ctxType;

        nameTemplate = ctxTemplate;

        portletId = ctx.getIdContext();

        isDynamic = true;

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

            type = Constants.CTX_TYPE_INDEX;

            nameTemplate = menuLanguage.getIndexTemplate();

            return;

        }



        type = ctxType;

        nameTemplate = ctxTemplate;

        isDynamic = true;

    }



    private Locale getPrefferedLocale(DatabaseAdapter db_, HttpServletRequest request)

        throws Exception

    {

        // determinate preffered locale

        // AcceptLanguageWithLevel[] accept =

        List acceptVector = Header.getAcceptLanguageAsList( request );



        SiteSupportLanguageListType supportLanguageList =

              GetSiteSupportLanguageWithIdSiteList.getInstance(db_, p.sites.getIdSite()).item;



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

            log.debug("hasVariant - "+hasVariant + ", hasCountry - "+hasCountry);



        for (int i=0; i<supportLanguageList.getSiteSupportLanguageCount(); i++)

        {

            SiteSupportLanguageItemType sl = supportLanguageList.getSiteSupportLanguage(i);



            if (log.isDebugEnabled())

                log.debug("SiteSupportLanguageItemType.getNameCustomLanguage - "+sl.getCustomLanguage());



            Locale cl = StringTools.getLocale( sl.getCustomLanguage() );



            if (log.isDebugEnabled())

                log.debug("SiteSupportLanguageItemType.getNameCustomLanguage locale - "+cl);



            if (!hasVariant && !hasCountry && accept.getLanguage().equalsIgnoreCase(cl.getLanguage()) )

                return sl;



            if (!hasVariant &&

                accept.getCountry().equalsIgnoreCase(cl.getCountry()) &&

                accept.getLanguage().equalsIgnoreCase(cl.getLanguage()) )

                return sl;



            if (accept.getVariant().equalsIgnoreCase(cl.getVariant()) &&

                accept.getCountry().equalsIgnoreCase(cl.getCountry()) &&

                accept.getLanguage().equalsIgnoreCase(cl.getLanguage()) )

                return sl;

        }



        return null;

    }



    private void initLocaleParameter(HttpServletRequest request, Locale loc_)

        throws ConfigException

    {

        String s = ServletUtils.getString(request, Constants.NAME_LANG_PARAM, null);

        if (s == null)

            currentLocale = (loc_ == null)?Locale.ENGLISH:loc_;

        else

            currentLocale = StringTools.getLocale(s);

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



        if (currentLocale != null)

            v.add( getHidden( Constants.NAME_LANG_PARAM, currentLocale.toString()));



        return v;

    }



    public String getAsURL()

    {

        return

                (currentLocale != null

                ?(Constants.NAME_LANG_PARAM + "=" + currentLocale.toString() + "&")

                :""

                );



    }



    public String getAsForm()

    {

        return

            (currentLocale != null)

            ?("<input type=\"hidden\" name=\"" + Constants.NAME_LANG_PARAM + "\" value=\"" + currentLocale.toString() + "\">")

            :"";

    }



    public String getAsUrlXML()

    {

        return



                (currentLocale != null

                ?(Constants.NAME_LANG_PARAM + "=" + currentLocale.toString() + "&amp;")

                :""

                );

    }



    public String getAsFormXML()

    {

        return



                (currentLocale != null

                ?("<HiddenParam><HiddenParamName>" + Constants.NAME_LANG_PARAM + "</HiddenParamName><HiddenParamValue>" + currentLocale.toString() + "</HiddenParamValue></HiddenParam>")

                :""

                );



    }



    public PortletDescription getPortletDescription()

    {

        return portletDescription;

    }



    public boolean isDynamic()

    {

        return isDynamic;

    }



}