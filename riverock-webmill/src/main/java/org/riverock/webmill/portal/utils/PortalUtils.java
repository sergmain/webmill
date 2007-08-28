/*
 * org.riverock.webmill - Webmill portal with support jsr-168, xml/xslt and others things.
 * For more information, please visit project site http://webmill.riverock.org
 *
 * Copyright (C) 2000-2006, Riverock Software, All Rights Reserved.
 *
 * Riverock - The Open-source Java Development Community
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
 */
package org.riverock.webmill.portal.utils;

import java.util.Locale;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Enumeration;
import java.util.ArrayList;
import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Cookie;

import org.apache.log4j.Logger;
import org.apache.commons.lang.CharEncoding;

import org.riverock.webmill.portal.dao.InternalDaoFactory;
import org.riverock.webmill.portal.info.PortalInfoImpl;
import org.riverock.webmill.utils.PortletUtils;
import org.riverock.interfaces.portal.PortalInfo;
import org.riverock.interfaces.portal.bean.SiteLanguage;
import org.riverock.interfaces.ContainerConstants;
import org.riverock.common.tools.StringTools;
import org.riverock.common.html.AcceptLanguageWithLevel;
import org.riverock.common.html.Header;

/**
 * @author Sergei Maslyukov
 *         Date: 22.08.2006
 *         Time: 19:21:37
 */
public class PortalUtils {
    private final static Logger log = Logger.getLogger( PortalUtils.class );

    private final static String NAME_LOCALE_COOKIE   = "webmill.lang.cookie";

    public synchronized static void registerPortletName(String portletName) {
        InternalDaoFactory.getInternalPortletNameDao().registerPortletName(portletName);
    }

    public static String buildVirtualHostUrl(HttpServletRequest req) {
        StringBuilder sb = new StringBuilder();
        sb.append(req.getScheme()).append("://").append(req.getServerName());
        if (req.getServerPort()!=80) {
            sb.append(':').append(req.getServerPort());
        }
        return sb.toString();
    }

    public static Locale prepareLocale(HttpServletRequest request, Long siteId) {
        // Todo - filter preferredLocale with locale defined for this site
        Locale tempLocale = getPreferredLocaleFromRequest( request, siteId );
        if (tempLocale==null) {
            PortalInfo portalInfo = PortalInfoImpl.getInstance( siteId );
            tempLocale = portalInfo.getDefaultLocale();
        }

        Locale realLocale = StringTools.getLocale( PortletUtils.getString(request, ContainerConstants.NAME_LANG_PARAM, tempLocale.toString()) );
        if ( log.isDebugEnabled() )
            log.debug( "internalInitTypeContext().locale: "+realLocale.toString() );

        if (realLocale==null){
            realLocale = Locale.ENGLISH;
            log.warn("Locale of request is null, process as Locale.ENGLISH");
        }
        return realLocale;
    }

    private static Locale getPreferredLocaleFromRequest( final HttpServletRequest request, final Long siteId ) {
        // determinate preffered locale
        // AcceptLanguageWithLevel[] accept =
        List<AcceptLanguageWithLevel> acceptVector = Header.getAcceptLanguageAsList( request );

        List<SiteLanguage> supportLanguageList = InternalDaoFactory.getInternalSiteLanguageDao().getSiteLanguageList(siteId);

        if (log.isDebugEnabled())
            log.debug("Start looking for preffered locale");

        // Locale not queried in URL
        Locale tempLocale = null;
        AcceptLanguageWithLevel bestAccept = null;
        SiteLanguage includedFromCookie = null;

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
                for (AcceptLanguageWithLevel accept : acceptVector) {

                    if (log.isDebugEnabled())
                        log.debug("Accepted locale item, locale - " + accept.locale + ", level - " + accept.level);

                    SiteLanguage included = includedAccept(accept.locale, supportLanguageList);

                    if (log.isDebugEnabled()) log.debug("included - " + included);

                    if (bestAccept == null) {
                        if (included != null) {
                            bestAccept = accept;
                            tempLocale = StringTools.getLocale(included.getCustomLanguage());
                        }
                    } else {
                        if (included != null && bestAccept.level < accept.level) {
                            bestAccept = accept;
                            tempLocale = StringTools.getLocale(included.getCustomLanguage());
                        }
                    }

                    if (log.isDebugEnabled()) log.debug("tempLocale - " + tempLocale);

                }
            }
        }
        return tempLocale;
    }

    private static SiteLanguage includedAccept( final Locale accept, final List<SiteLanguage> supportLanguageList ) {

        boolean hasVariant = (accept.getVariant()!=null && accept.getVariant().trim().length()>0);
        boolean hasCountry = (accept.getCountry()!=null && accept.getCountry().trim().length()>0);

        if (log.isDebugEnabled()) {
            log.debug("accept locale: "+accept.toString()+", hasVariant - "+hasVariant + ", hasCountry - "+hasCountry);
        }

        SiteLanguage result = null;
        Locale rl = null;
        SiteLanguage resultTemp = null;
        Locale rlTemp = null;
        for (SiteLanguage bean : supportLanguageList) {
            Locale cl = StringTools.getLocale(bean.getCustomLanguage());

            if (log.isDebugEnabled()) {
                if (cl == null) {
                    log.debug("WmPortalSiteLanguageItemType.getNameCustomLanguage locale is null");
                } else {
                    log.debug("WmPortalSiteLanguageItemType.getNameCustomLanguage locale - " + cl.toString());
                }
            }

            if (cl == null) {
                continue;
            }

            if (accept.toString().equalsIgnoreCase(cl.toString()))
                return bean;

            boolean hasCountryTemp = (cl.getCountry() != null && cl.getCountry().trim().length() > 0);

            if (hasCountry && hasCountryTemp) {
                if (accept.getCountry().equalsIgnoreCase(cl.getCountry()) &&
                    accept.getLanguage().equalsIgnoreCase(cl.getLanguage())) {
                    if (log.isDebugEnabled()) log.debug("new resultTemp locale is: " + cl.toString());
                    resultTemp = bean;
                    rlTemp = cl;
                }
            } else if (accept.getLanguage().equalsIgnoreCase(cl.getLanguage())) {
                if (log.isDebugEnabled()) {
                    log.debug("new resultTemp locale is: " + cl.toString());
                }
                resultTemp = bean;
                rlTemp = cl;
            }

            if (result == null) {
                result = resultTemp;
                rl = cl;
                if (log.isDebugEnabled()) log.debug("#1, new result locale is: " + cl.toString());
            } else {
                boolean hasVariantResult = (rl.getVariant() != null && rl.getVariant().trim().length() > 0);
                boolean hasCountryResult = (rl.getCountry() != null && rl.getCountry().trim().length() > 0);
                boolean hasVariantResultTemp = (rlTemp.getVariant() != null && rlTemp.getVariant().trim().length() > 0);
                boolean hasCountryResultTemp = (rlTemp.getCountry() != null && rlTemp.getCountry().trim().length() > 0);
                if (hasVariantResult && !hasVariantResultTemp) {
                    result = resultTemp;
                    rl = cl;
                    if (log.isDebugEnabled()) log.debug("#2, new result locale is: " + cl.toString());
                } else if (hasCountryResult && !hasCountryResultTemp) {
                    result = resultTemp;
                    rl = cl;
                    if (log.isDebugEnabled()) log.debug("#3, new result locale is: " + cl.toString());
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

    public static Map<String, List<String>> prepareParameters( final HttpServletRequest request, final String portalCharset ) {

        Map<String, List<String>> p = new HashMap<String, List<String>>();

        Enumeration e = request.getParameterNames();
        for (; e.hasMoreElements() ;) {
            String key = (String)e.nextElement();

            String value[] = request.getParameterValues( key );
            if (value!=null) {
                List<String> list = new ArrayList<String>();
                for (String s : value) {
                    if (s==null)
                        continue;

                    try {
                        String convertedString = new String(s.getBytes(CharEncoding.ISO_8859_1), portalCharset);
                        list.add(convertedString);
                    }
                    catch (UnsupportedEncodingException e1) {
                        String es = "Error convert parameter from 'ISO-8859-1' to portalCharset '"+portalCharset+"'";
                        log.error(es, e1);
                        throw new IllegalArgumentException(es,e1);
                    }
                }
                p.put(key, list);
            }
        }
        return p;
    }
}
