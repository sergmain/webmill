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
package org.riverock.webmill.portal;

import java.util.List;
import java.util.Locale;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import org.riverock.common.html.AcceptLanguageWithLevel;
import org.riverock.common.html.Header;
import org.riverock.common.tools.StringTools;
import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.webmill.container.ContainerConstants;
import org.riverock.webmill.exception.PortalPersistenceException;

import org.riverock.webmill.utils.ServletUtils;
import org.riverock.webmill.schema.core.WmPortalSiteLanguageListType;
import org.riverock.webmill.schema.core.WmPortalSiteLanguageItemType;
import org.riverock.webmill.core.GetWmPortalSiteLanguageWithIdSiteList;

/**
 * $Id$
 */
public final class ContextLocaleUtils {
    private final static Logger log = Logger.getLogger( ContextLocaleUtils.class );
    private final static String NAME_LOCALE_COOKIE   = "webmill.lang.cookie";

    static Locale prepareLocale( ContextFactory.ContextFactoryParameter factoryParameter) throws PortalPersistenceException {
        // Todo - filter preferredLocale with locale defined for this site
        Locale tempLocale = null;
        tempLocale = getPreferredLocale(factoryParameter.getAdapter(), factoryParameter.getRequest(), factoryParameter.getPortalInfo().getSites().getIdSite());
        if (tempLocale==null)
            tempLocale = factoryParameter.getPortalInfo().getDefaultLocale();

        Locale realLocale = null;
        realLocale = StringTools.getLocale( ServletUtils.getString(factoryParameter.getRequest(), ContainerConstants.NAME_LANG_PARAM, tempLocale.toString()) );
        if ( log.isDebugEnabled() ) log.debug( "internalInitTypeContext().realLocale: "+realLocale.toString() );

        if (realLocale==null){
            realLocale = Locale.ENGLISH;
            log.warn("Locale of request is null, process as Locale.ENGLISH");
        }
        return realLocale;
    }

    private static Locale getPreferredLocale( final DatabaseAdapter db_, final HttpServletRequest request, final Long siteId ) throws PortalPersistenceException {
        // determinate preffered locale
        // AcceptLanguageWithLevel[] accept =
        List acceptVector = Header.getAcceptLanguageAsList( request );

        WmPortalSiteLanguageListType supportLanguageList =
              GetWmPortalSiteLanguageWithIdSiteList.getInstance(db_, siteId).item;

        if (log.isDebugEnabled())
            log.debug("Start looking for preffered locale");

        // Locale not queried in URL
        Locale tempLocale = null;
        AcceptLanguageWithLevel bestAccept = null;
        WmPortalSiteLanguageItemType includedFromCookie = null;

        Cookie[] cookies_req = request.getCookies();
        if (cookies_req!=null) {
            for (Cookie c : cookies_req) {
                String name_cookie = c.getName();
                if (name_cookie.equals(NAME_LOCALE_COOKIE)) {
                    includedFromCookie =
                        includedAccept(StringTools.getLocale(c.getValue()), supportLanguageList);
                    break;
                }
            }
        }
        //Todo store prefered locale in cookie

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

                    WmPortalSiteLanguageItemType included = includedAccept(accept.locale, supportLanguageList );

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

    private static WmPortalSiteLanguageItemType includedAccept( final Locale accept, final WmPortalSiteLanguageListType supportLanguageList ) {

        boolean hasVariant = (accept.getVariant()!=null && accept.getVariant().trim().length()>0);
        boolean hasCountry = (accept.getCountry()!=null && accept.getCountry().trim().length()>0);

        if (log.isDebugEnabled()) {
            log.debug("accept locale: "+accept.toString()+", hasVariant - "+hasVariant + ", hasCountry - "+hasCountry);
        }

        WmPortalSiteLanguageItemType result = null;
        Locale rl = null;
        WmPortalSiteLanguageItemType resultTemp = null;
        Locale rlTemp = null;
        for (int i=0; i<supportLanguageList.getWmPortalSiteLanguageCount(); i++){
            WmPortalSiteLanguageItemType sl = supportLanguageList.getWmPortalSiteLanguage(i);

            Locale cl = StringTools.getLocale( sl.getCustomLanguage() );

            if (log.isDebugEnabled()) {
                if (cl==null) {
                    log.debug("WmPortalSiteLanguageItemType.getNameCustomLanguage locale is null");
                }
                else {
                    log.debug("WmPortalSiteLanguageItemType.getNameCustomLanguage locale - "+cl.toString());
                }
            }

            if (cl==null) {
                continue;
            }

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
                if (log.isDebugEnabled()) {
                    log.debug("new resultTemp locale is: "+cl.toString());
                }
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

        if (log.isDebugEnabled()) {
            log.debug("Result WmPortalSiteLanguageItemType: "+result);
            if (result!=null) {
                log.debug("Result WmPortalSiteLanguageItemType.getCustomLanguage is: "+result.getCustomLanguage());
            }
        }
        return result;
    }
}
