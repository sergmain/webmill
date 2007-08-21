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
package org.riverock.webmill.portal.url.interpreter;

import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import org.riverock.common.collections.MapWithParameters;
import org.riverock.interfaces.ContainerConstants;
import org.riverock.interfaces.portal.PortalInfo;
import org.riverock.webmill.container.tools.PortletService;
import org.riverock.webmill.port.PortalInfoImpl;
import org.riverock.webmill.portal.PortletParameters;
import org.riverock.webmill.portal.bean.ExtendedCatalogItemBean;
import org.riverock.webmill.portal.namespace.Namespace;
import org.riverock.webmill.portal.namespace.NamespaceFactory;
import org.riverock.webmill.portal.url.RequestContext;
import org.riverock.webmill.portal.url.RequestContextParameter;
import org.riverock.webmill.portal.url.RequestState;
import org.riverock.webmill.template.PortalTemplate;
import org.riverock.webmill.template.PortalTemplateManagerFactory;
import org.riverock.webmill.template.TemplateUtils;
import org.riverock.webmill.template.parser.ParsedTemplateElement;

/**
 * $Id$
 */
public final class UrlInterpreterUtils {
    private final static Logger log = Logger.getLogger( UrlInterpreterUtils.class );

    static RequestContext getRequestContextBean(RequestContextParameter factoryParameter, Long ctxId ) {
        log.debug("Start getRequestContextBean()");

        RequestContext bean = new RequestContext();
        ExtendedCatalogItemBean extendedCatalogItem = ExtendedCatalogItemBean.getInstance(factoryParameter, ctxId);
        if (extendedCatalogItem==null) {
            log.warn("page with id "+ctxId+" not found");
            return null;
        }
        bean.setExtendedCatalogItem(extendedCatalogItem);
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

        PortalInfo portalInfo = PortalInfoImpl.getInstance( factoryParameter.getSiteId() );
        initParametersMap(bean, factoryParameter, portalInfo);

        return bean;
    }

    static void initParametersMap(RequestContext requestContext, RequestContextParameter factoryParameter, PortalInfo portalInfo) {

        PortalTemplate template = PortalTemplateManagerFactory.getInstance(portalInfo.getSiteId()).getTemplate(
            requestContext.getExtendedCatalogItem().getTemplateId()
        );

        if (template == null) {
            String errorString = "Template with id " + requestContext.getExtendedCatalogItem().getTemplateId() + " not found";
            log.warn(errorString);
            throw new IllegalStateException(errorString);
        }
        requestContext.setTemplateName( template.getTemplateName() );
        // looking for dynamic portlet.
        int i=0;
        if (log.isDebugEnabled()) {
            log.debug( "template:\n" + template.toString());
            log.debug( "Process template");
        }
        for (ParsedTemplateElement templateItem : template.getTemplate().getElements()) {

            // we change request status if portlet is 'always_process_as_action'
            RequestState requestState;

            if (templateItem.isPortlet()) {
                String portletName = TemplateUtils.getFullPortletName( templateItem.getPortlet().getName() );

                Namespace namespace = NamespaceFactory.getNamespace(
                    portletName, template.getTemplateName(), NamespaceFactory.getTemplateUniqueIndex(templateItem, i++)
                );
                if (log.isDebugEnabled()) {
                    log.debug("    template name: " +template.getTemplateName());
                    log.debug("    namespace: " +namespace.getNamespace());
                    log.debug("    portlet: " +portletName);
                    log.debug("    default namespace: " +requestContext.getDefaultNamespace());
                    log.debug("    default portlet: " +requestContext.getExtendedCatalogItem().getFullPortletName());
                    log.debug("");
                }

                if ( requestContext.getDefaultNamespace()!=null) {
                    if ( requestContext.getDefaultNamespace().equals( namespace.getNamespace() ) ) {
                        if (log.isDebugEnabled()) {
                            log.debug("    Create parameter for NS "+namespace.getNamespace() +", action state: " +requestContext.getDefaultRequestState());
                        }
                        requestState = initParameterForDefaultPortlet(factoryParameter, requestContext, portalInfo);
                    }
                    else {
                        requestState = new RequestState();
                        PortletParameters portletParameters = new PortletParameters(namespace.getNamespace(), requestState, new HashMap<String, List<String>>() );
                        requestContext.getParameters().put( namespace.getNamespace(), portletParameters );
                    }
                }
                else {
                    String tempPortletName = requestContext.getExtendedCatalogItem().getFullPortletName();
                    if (tempPortletName !=null && tempPortletName.equals(portletName)) {
                        requestState = initParameterForDefaultPortlet(factoryParameter, requestContext, portalInfo);
                    }
                    else {
                        requestState = new RequestState();
                        PortletParameters portletParameters = new PortletParameters(namespace.getNamespace(), requestState, new HashMap<String, List<String>>() );
                        requestContext.getParameters().put( namespace.getNamespace(), portletParameters );
                    }
                }
            }
            else if (templateItem.isDynamic()) {
                Namespace namespace = NamespaceFactory.getNamespace(
                    requestContext.getExtendedCatalogItem().getFullPortletName(),
                    template.getTemplateName(), NamespaceFactory.getTemplateUniqueIndex(templateItem, i++)
                );
                requestContext.setDefaultNamespace( namespace.getNamespace() );
                requestState = initParameterForDefaultPortlet(factoryParameter, requestContext, portalInfo);
            }
            else {
                continue;
            }
            if (PortletService.getBooleanParam(requestContext.getExtendedCatalogItem().getPortletDefinition(), ContainerConstants.always_process_as_action, false)) {
                log.debug("Set explicitly action status for portlet");
                requestState.setActionRequest(true);
            }
        }
    }

    public static RequestState initParameterForDefaultPortlet(RequestContextParameter factoryParameter, RequestContext requestContext, PortalInfo portalInfo) {
        // prepare dynamic parameters
        PortletParameters portletParameters;
        if (factoryParameter.isMultiPartRequest()) {
            portletParameters = new PortletParameters( requestContext.getDefaultNamespace(), requestContext.getDefaultRequestState(), factoryParameter.getRequestBodyFile() );
        }
        else {
            // init id of concrete portlet instance with value
            if (requestContext.getConcretePortletIdValue()!=null) {
                String nameId = PortletService.getStringParam( requestContext.getExtendedCatalogItem().getPortletDefinition(), ContainerConstants.name_portlet_id );
                if ( log.isDebugEnabled() ) {
                    log.debug( "nameId: "+nameId );
                    log.debug( "Id: "+requestContext.getConcretePortletIdValue() );
                    log.debug( "httpRequestParameter: "+factoryParameter.getHttpRequestParameter() );
                }
                if (nameId!=null) {
                    MapWithParameters.putInStringList(factoryParameter.getHttpRequestParameter(), nameId, requestContext.getConcretePortletIdValue().toString() );
                }
            }

            portletParameters = new PortletParameters( requestContext.getDefaultNamespace(), requestContext.getDefaultRequestState(), factoryParameter.getHttpRequestParameter() );
        }
        requestContext.getParameters().put( requestContext.getDefaultNamespace(), portletParameters );
        return requestContext.getDefaultRequestState();
    }

}
