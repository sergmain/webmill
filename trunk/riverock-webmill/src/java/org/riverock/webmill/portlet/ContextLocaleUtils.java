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

import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.webmill.port.PortalInfo;
import org.riverock.webmill.exception.PortalPersistenceException;
import org.riverock.webmill.exception.PortalException;
import org.riverock.webmill.utils.ServletUtils;
import org.riverock.webmill.main.Constants;
import org.riverock.webmill.schema.core.SiteSupportLanguageListType;
import org.riverock.webmill.schema.core.SiteSupportLanguageItemType;
import org.riverock.webmill.core.GetSiteSupportLanguageWithIdSiteList;
import org.riverock.common.tools.StringTools;
import org.riverock.common.config.ConfigException;
import org.riverock.common.html.Header;
import org.riverock.common.html.AcceptLanguageWithLevel;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Cookie;
import java.util.Locale;
import java.util.List;

/**
 * $Id$
 */
public class ContextLocaleUtils {
    private static Logger log = Logger.getLogger( ContextLocaleUtils.class );

    static Locale prepareLocale(DatabaseAdapter adapter, HttpServletRequest request, PortalInfo portalInfo) throws PortalPersistenceException, PortalException {
        // Todo - filter preferredLocale with locale defined for this site
        Locale tempLocale = null;
        tempLocale = getPreferredLocale(adapter, request, portalInfo.getSites().getIdSite());
        if (tempLocale==null)
            tempLocale = portalInfo.getDefaultLocale();

        Locale realLocale = null;
        try {
            realLocale = StringTools.getLocale( ServletUtils.getString(request, Constants.NAME_LANG_PARAM, tempLocale.toString()) );
        } catch (ConfigException e) {
            String es = "Error read config file";
            log.error(es, e);
            throw new PortalException(es, e);
        }
        if ( log.isDebugEnabled() ) log.debug( "internalInitTypeContext().realLocale: "+realLocale.toString() );

        if (realLocale==null){
            realLocale = Locale.ENGLISH;
            log.warn("Locale of request is null, process as Locale.ENGLISH");
        }
        return realLocale;
    }

    private static Locale getPreferredLocale(DatabaseAdapter db_, HttpServletRequest request, Long siteId) throws PortalPersistenceException {
        // determinate preffered locale
        // AcceptLanguageWithLevel[] accept =
        List acceptVector = Header.getAcceptLanguageAsList( request );

        SiteSupportLanguageListType supportLanguageList =
              GetSiteSupportLanguageWithIdSiteList.getInstance(db_, siteId).item;

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

        if (includedFromCookie!=null) {
            tempLocale = StringTools.getLocale(includedFromCookie.getCustomLanguage());
        }
        else {
            if (log.isDebugEnabled()){
                log.debug("Total accepted locale vector - "+ acceptVector);
                if (acceptVector!=null)
                    log.debug("Total accepted locale elements - "+ acceptVector.size());
            }

            if (acceptVector!=null){
                for (int i=0; i<acceptVector.size(); i++){
                    AcceptLanguageWithLevel accept = (AcceptLanguageWithLevel)acceptVector.get(i);

                    if (log.isDebugEnabled())
                        log.debug("Accepted locale item, locale - "+ accept.locale+", level - "+ accept.level);

                    SiteSupportLanguageItemType included = includedAccept(accept.locale, supportLanguageList );

                    if (log.isDebugEnabled()) log.debug("included - "+included);

                    if  (bestAccept==null){
                        if (included!=null){
                            bestAccept = accept;
                            tempLocale = StringTools.getLocale( included.getCustomLanguage() );
                        }
                    }
                    else{
                        if (included!=null && bestAccept.level<accept.level){
                            bestAccept = accept;
                            tempLocale = StringTools.getLocale( included.getCustomLanguage() );
                        }
                    }

                    if (log.isDebugEnabled())log.debug("tempLocale - "+tempLocale);

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
            log.debug("accept locale: "+accept.toString()+", hasVariant - "+hasVariant + ", hasCountry - "+hasCountry);

        SiteSupportLanguageItemType result = null;
        Locale rl = null;
        SiteSupportLanguageItemType resultTemp = null;
        Locale rlTemp = null;
        for (int i=0; i<supportLanguageList.getSiteSupportLanguageCount(); i++){
            SiteSupportLanguageItemType sl = supportLanguageList.getSiteSupportLanguage(i);

            Locale cl = StringTools.getLocale( sl.getCustomLanguage() );

            if (log.isDebugEnabled())
                log.debug("SiteSupportLanguageItemType.getNameCustomLanguage locale - "+cl.toString());

            if (accept.toString().equalsIgnoreCase(cl.toString()))
                return sl;

            boolean hasCountryTemp = (cl.getCountry()!=null && cl.getCountry().trim().length()>0);

            if (hasCountry && hasCountryTemp){
                if (accept.getCountry().equalsIgnoreCase(cl.getCountry()) &&
                    accept.getLanguage().equalsIgnoreCase(cl.getLanguage()) )
                {
                    if (log.isDebugEnabled()) log.debug("new resultTemp locale is: "+cl.toString());
                    resultTemp = sl;
                    rlTemp = cl;
                }
            }
            else if (accept.getLanguage().equalsIgnoreCase(cl.getLanguage()) ){
                if (log.isDebugEnabled()) log.debug("new resultTemp locale is: "+cl.toString());
                resultTemp = sl;
                rlTemp = cl;
            }

            if (result==null){
                result = resultTemp;
                rl = cl;
                if (log.isDebugEnabled()) log.debug("#1, new result locale is: "+cl.toString());
            }
            else{
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
}
