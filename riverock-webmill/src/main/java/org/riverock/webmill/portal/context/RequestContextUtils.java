/*
 * org.riverock.webmill - Portal framework implementation
 *
 * Copyright (C) 2006, Riverock Software, All Rights Reserved.
 *
 * Riverock - The Open-source Java Development Community
 * http://www.riverock.org
 *
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.riverock.webmill.portal.context;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.CharEncoding;
import org.apache.log4j.Logger;

import org.riverock.common.collections.MapWithParameters;
import org.riverock.common.html.AcceptLanguageWithLevel;
import org.riverock.common.html.Header;
import org.riverock.common.tools.StringTools;
import org.riverock.interfaces.portal.bean.SiteLanguage;
import org.riverock.interfaces.portal.template.PortalTemplate;
import org.riverock.interfaces.portal.template.PortalTemplateItem;
import org.riverock.interfaces.portal.template.PortalTemplateItemType;
import org.riverock.webmill.container.ContainerConstants;
import org.riverock.webmill.container.tools.PortletService;
import org.riverock.webmill.portal.PortletParameters;
import org.riverock.webmill.portal.bean.ExtendedCatalogItemBean;
import org.riverock.webmill.portal.dao.InternalDaoFactory;
import org.riverock.webmill.portal.namespace.Namespace;
import org.riverock.webmill.portal.namespace.NamespaceFactory;
import org.riverock.webmill.utils.PortletUtils;

/**
 * $Id$
 */
public final class RequestContextUtils {
    private final static Logger log = Logger.getLogger( RequestContextUtils.class );
    private final static String NAME_LOCALE_COOKIE   = "webmill.lang.cookie";

    static Locale prepareLocale( RequestContextParameter factoryParameter) {
        // Todo - filter preferredLocale with locale defined for this site
        Locale tempLocale = getPreferredLocaleFromRequest( factoryParameter.getRequest(), factoryParameter.getPortalInfo().getSiteId() );
        if (tempLocale==null)
            tempLocale = factoryParameter.getPortalInfo().getDefaultLocale();

        Locale realLocale = StringTools.getLocale( PortletUtils.getString(factoryParameter.getRequest(), ContainerConstants.NAME_LANG_PARAM, tempLocale.toString()) );
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

    static RequestContext getRequestContextBean(RequestContextParameter factoryParameter, Long ctxId ) {
        log.debug("Start getRequestContextBean()");

        RequestContext bean = new RequestContext();
        bean.setExtendedCatalogItem( ExtendedCatalogItemBean.getInstance(factoryParameter, ctxId) );
        bean.setLocale( bean.getExtendedCatalogItem().getLocale() );

        if (log.isDebugEnabled()) {
            log.debug("    bean.getExtendedCatalogItem().getFullPortletName(): "+bean.getExtendedCatalogItem().getFullPortletName());
            if (bean.getExtendedCatalogItem().getPortletDefinition()!=null) {
                log.debug("    bean.getExtendedCatalogItem().getPortletDefinition().getPortletName(): "+bean.getExtendedCatalogItem().getPortletDefinition().getPortletName());
                log.debug("    bean.getExtendedCatalogItem().getPortletDefinition().getFullPortletName(): "+bean.getExtendedCatalogItem().getPortletDefinition().getFullPortletName());
            }
            else {
                log.debug("    bean.getExtendedCatalogItem().getPortletDefinition() is null");
            }
        }

        bean.setDefaultPortletName( bean.getExtendedCatalogItem().getFullPortletName() );
        bean.setDefaultRequestState( new RequestState() );

        initParametersMap(bean, factoryParameter);

        return bean;
    }

    static void initParametersMap( RequestContext bean, RequestContextParameter factoryParameter) {
        PortalTemplate template = factoryParameter.getPortalInfo().getPortalTemplateManager().getTemplate(
            bean.getExtendedCatalogItem().getTemplateId()
        );

        if (template == null) {
            String errorString = "Template with id " + bean.getExtendedCatalogItem().getTemplateId() + " not found";
            log.warn(errorString);
            throw new IllegalStateException(errorString);
        }
        bean.setTemplateName( template.getTemplateName() );
        // looking for dynamic portlet.
        int i=0;
        if (log.isDebugEnabled()) {
            log.debug( "template:\n" + template.toString());
            log.debug( "Process template");
        }
        for (PortalTemplateItem templateItem : template.getPortalTemplateItems()) {

            // we change request status if portlet is 'always_process_as_action'
            RequestState requestState;

            if (templateItem.getTypeObject().getType() == PortalTemplateItemType.PORTLET_TYPE) {
                String portletName = templateItem.getValueAsPortletName();

                Namespace namespace = NamespaceFactory.getNamespace(portletName, template.getTemplateName(), i++);
                if (log.isDebugEnabled()) {
                    log.debug("    template name: " +template.getTemplateName());
                    log.debug("    namespace: " +namespace.getNamespace());
                    log.debug("    portlet: " +portletName);
                    log.debug("    default namespace: " +bean.getDefaultNamespace());
                    log.debug("    default portlet: " +bean.getExtendedCatalogItem().getFullPortletName());
                    log.debug("");
                }

                if ( bean.getDefaultNamespace()!=null) {
                    if ( bean.getDefaultNamespace().equals( namespace.getNamespace() ) ) {
                        if (log.isDebugEnabled()) {
                            log.debug("    Create parameter for NS "+namespace.getNamespace() +", action state: " +bean.getDefaultRequestState());
                        }
                        requestState = initParameterForDefaultPortlet(factoryParameter, bean);
                    }
                    else {
                        requestState = new RequestState();
                        PortletParameters portletParameters = new PortletParameters(namespace.getNamespace(), requestState, new HashMap<String, List<String>>() );
                        bean.getParameters().put( namespace.getNamespace(), portletParameters );
                    }
                }
                else {
                    String tempPortletName = bean.getExtendedCatalogItem().getFullPortletName();
                    if (tempPortletName !=null && tempPortletName.equals(portletName)) {
                        requestState = initParameterForDefaultPortlet(factoryParameter, bean);
                    }
                    else {
                        requestState = new RequestState();
                        PortletParameters portletParameters = new PortletParameters(namespace.getNamespace(), requestState, new HashMap<String, List<String>>() );
                        bean.getParameters().put( namespace.getNamespace(), portletParameters );
                    }
                }
            } else if (templateItem.getTypeObject().getType() == PortalTemplateItemType.DYNAMIC_TYPE) {
                //noinspection UnusedAssignment
                Namespace namespace = NamespaceFactory.getNamespace(
                    bean.getExtendedCatalogItem().getFullPortletName(), template.getTemplateName(), i++
                );
                bean.setDefaultNamespace( namespace.getNamespace() );
                requestState = initParameterForDefaultPortlet(factoryParameter, bean);
            }
            else {
                continue;
            }
            if (PortletService.getBooleanParam(bean.getExtendedCatalogItem().getPortletDefinition(), ContainerConstants.always_process_as_action, false)) {
                log.debug("Set explicitly action status for portlet");
                requestState.setActionRequest(true);
            }
        }
    }

    public static RequestState initParameterForDefaultPortlet(RequestContextParameter factoryParameter, RequestContext bean) {
        // prepare dynamic parameters
        PortletParameters portletParameters;
        if (factoryParameter.isMultiPartRequest()) {
            portletParameters = new PortletParameters( bean.getDefaultNamespace(), bean.getDefaultRequestState(), factoryParameter.getRequestBodyFile() );
        }
        else {
            Map<String, List<String>> httpRequestParameter = prepareParameters(factoryParameter.getRequest(), factoryParameter.getPortalInfo().getSite().getPortalCharset());

            // init id of concrete portlet instance with value
            if (bean.getConcretePortletIdValue()!=null) {
                String nameId = PortletService.getStringParam( bean.getExtendedCatalogItem().getPortletDefinition(), ContainerConstants.name_portlet_id );
                if ( log.isDebugEnabled() ) {
                    log.debug( "nameId: "+nameId );
                    log.debug( "Id: "+bean.getConcretePortletIdValue() );
                    log.debug( "httpRequestParameter: "+httpRequestParameter );
                }
                if (nameId!=null) {
                    MapWithParameters.putInStringList(httpRequestParameter, nameId, bean.getConcretePortletIdValue().toString() );
                }
            }

            portletParameters = new PortletParameters( bean.getDefaultNamespace(), bean.getDefaultRequestState(), httpRequestParameter );
        }
        bean.getParameters().put( bean.getDefaultNamespace(), portletParameters );
        return bean.getDefaultRequestState();
    }

    private static Map<String, List<String>> prepareParameters( final HttpServletRequest request, final String portalCharset ) {

        boolean isMultiPartRequest = PortletUtils.isMultiPart(request);

        if (isMultiPartRequest) {
            throw new IllegalStateException("MultiPart request must processed via parseMultiPartRequest() method");
        }

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
