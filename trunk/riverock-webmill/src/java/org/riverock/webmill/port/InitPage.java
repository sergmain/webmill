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



import java.util.Locale;

import java.util.List;



import javax.servlet.http.Cookie;

import javax.servlet.http.HttpServletRequest;

import javax.servlet.http.HttpServletResponse;



import org.apache.log4j.Logger;



import org.riverock.generic.db.DatabaseAdapter;

import org.riverock.generic.tools.StringManager;

import org.riverock.common.html.AcceptLanguageWithLevel;

import org.riverock.common.html.Header;

import org.riverock.webmill.main.Constants;

import org.riverock.webmill.core.GetSiteSupportLanguageWithIdSiteList;

import org.riverock.webmill.schema.core.SiteSupportLanguageItemType;

import org.riverock.webmill.schema.core.SiteSupportLanguageListType;

import org.riverock.webmill.portal.menu.MenuLanguage;

import org.riverock.webmill.portal.menu.SiteMenu;

import org.riverock.webmill.portal.menu.MenuLanguageInterface;

import org.riverock.webmill.tools.CrossPageParam;

import org.riverock.webmill.tools.CrossPageParamInterface;

import org.riverock.webmill.config.WebmillConfig;

import org.riverock.common.tools.StringTools;



public class InitPage

{

    private static Logger log = Logger.getLogger("org.riverock.webmill.port.InitPage");



    public MenuLanguageInterface menuLanguage = null;

    public PortalInfo p = null;

    public CrossPageParamInterface cross = null;

    public Locale currentLocale = null;

    public String addURL = "";



    public StringManager sMain = null;

    public StringManager sCustom = null;

    public String title = "&nbsp;";



//    private String localePackageName = "";



    protected void finalize() throws Throwable

    {

        menuLanguage = null;

        p = null;

        cross = null;

        currentLocale = null;

        addURL = null;

        sMain = null;

        sCustom = null;

        title = null;

//        localePackageName = null;



        super.finalize();

    }



    /**

     * @deprecated

     */

    public InitPage(DatabaseAdapter db_, HttpServletRequest request, HttpServletResponse response,

                    String nullParam, String nameLocales, String crossMask,

                    Integer year_, Integer month_)



            throws Exception

    {

        this(db_, request, response,

                        nameLocales, crossMask,

                        year_, month_);

    }



    public InitPage(DatabaseAdapter db_, HttpServletRequest request, HttpServletResponse response,

                    String nameLocales, String crossMask,

                    Integer year_, Integer month_)



            throws Exception

    {

        long jspPagePortletInfo = 0;

        try

        {

            if (log.isInfoEnabled())

            {

                if (log.isInfoEnabled())

                    log.info("start get instance of PortalInfo ");



                jspPagePortletInfo= System.currentTimeMillis();

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



        cross = new CrossPageParam(request, crossMask, tempLocale, year_, month_);

        currentLocale = cross.getLocaleInternal();

        if (currentLocale == null)

            currentLocale = tempLocale;



        addURL = cross.getAsURL();



        sMain = StringManager.getManager("mill.locale.main", currentLocale);

        if ((nameLocales != null) && (nameLocales.trim().length() != 0))

            sCustom = StringManager.getManager(nameLocales, currentLocale);



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



/*

    public void reinit(HttpServletRequest request, HttpServletResponse response)

    {

        currentLocale = cross.getLocaleInternal();

        addURL = cross.getAsURL();



        sMain = StringManager.getManager("mill.locale.main", currentLocale);

        sCustom = StringManager.getManager(localePackageName, currentLocale);

    }

*/

    public static void setContentType(HttpServletResponse response, String charset)

            throws Exception

    {

        if (log.isDebugEnabled())

            log.debug("set new charset - "+charset);



        try

        {

            response.setContentType("text/html; charset=" + charset);

        }

        catch(Exception e)

        {

            log.error("Error set new content type to "+charset);

            throw e;

        }

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



    public static void setContentType(HttpServletResponse response)

            throws Exception

    {

        setContentType(response, WebmillConfig.getHtmlCharset());

    }

}