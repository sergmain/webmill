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

 * User: serg_main

 * Date: 29.11.2003

 * Time: 20:05:19

 * @author Serge Maslyukov

 * $Id$

 */



package org.riverock.webmill.portal;



import java.util.ArrayList;

import java.util.Locale;



import javax.servlet.http.Cookie;

import javax.servlet.http.HttpServletRequest;



import org.riverock.common.html.AcceptLanguageWithLevel;

import org.riverock.common.html.Header;

import org.riverock.common.tools.StringTools;

import org.riverock.generic.db.DatabaseAdapter;

import org.riverock.webmill.main.Constants;

import org.riverock.webmill.port.PortalInfo;

import org.riverock.webmill.schema.core.SiteSupportLanguageItemType;

import org.riverock.webmill.schema.core.SiteSupportLanguageListType;

import org.riverock.webmill.utils.ServletUtils;



import org.apache.log4j.Logger;





public class LocaleProvider

{

    private static Logger log = Logger.getLogger("org.riverock.webmill.portal.LocaleProvider");



    public static Locale getLocale(DatabaseAdapter db_, HttpServletRequest request, PortalInfo portalInfo)

        throws Exception

    {

        Locale tempLocale = getPrefferedLocale(db_, request, portalInfo.supportLanguage);



        if (tempLocale==null)

            tempLocale = portalInfo.defaultLocale;



        if (log.isDebugEnabled())

            log.debug("tempLocale - "+tempLocale);



        String s = ServletUtils.getString(request, Constants.NAME_LANG_PARAM, null);

        Locale resultLocale = null;

        if (s == null)

        {

            resultLocale = (tempLocale == null)?Locale.ENGLISH:tempLocale;

        }

        else

        {

            int pos = s.indexOf('_');

            String lang = null, country = "", variant = "";

            if (pos == -1)

                lang = s;

            else

            {

                lang = s.substring(0, pos);

                s = s.substring(pos + 1);

                pos = s.indexOf('_');

                if (pos == -1)

                    country = s;

                else

                {

                    country = s.substring(0, pos);

                    variant = s.substring(pos + 1);

                }



            }

            resultLocale = new Locale(lang, country, variant);

        }

//        CrossPageParam cross = new CrossPageParam(request, crossMask, tempLocale, year_, month_);

//        public CrossPageParam(HttpServletRequest request,

//                              String param,

//                              Locale loc_,

//                              Integer year_,

//                              Integer month_)



//        currentLocale = cross.getLocaleInternal();

        if (resultLocale == null)

            resultLocale = tempLocale;



        return resultLocale;

    }



    private static Locale getPrefferedLocale(

        DatabaseAdapter db_, HttpServletRequest request, SiteSupportLanguageListType supportLanguageList

        )

            throws Exception

    {

        // determinate preffered locale

        // AcceptLanguageWithLevel[] accept =

        ArrayList acceptVector = Header.getAcceptLanguageAsList( request );



//        SiteSupportLanguageListType supportLanguageList =

//              GetSiteSupportLanguageWithIdSiteList.getInstance(db_, p.sites.getIdSite()).item;



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



    private static SiteSupportLanguageItemType includedAccept( Locale accept, SiteSupportLanguageListType supportLanguageList )

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



}

