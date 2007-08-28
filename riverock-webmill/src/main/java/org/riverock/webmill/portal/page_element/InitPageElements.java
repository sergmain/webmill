package org.riverock.webmill.portal.page_element;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.apache.commons.lang.StringUtils;

import org.riverock.webmill.portal.PortalInstance;
import org.riverock.webmill.portal.PortalRequest;
import org.riverock.webmill.portal.preference.PreferenceFactory;
import org.riverock.webmill.portal.url.interpreter.PortletParameters;
import org.riverock.webmill.portal.url.interpreter.UrlInterpreterResult;
import org.riverock.webmill.portal.url.interpreter.RequestState;
import org.riverock.webmill.portal.namespace.Namespace;
import org.riverock.webmill.portal.namespace.NamespaceFactory;
import org.riverock.webmill.template.parser.ParsedTemplateElement;
import org.riverock.webmill.template.schema.Portlet;
import org.riverock.webmill.template.TemplateUtils;
import org.riverock.webmill.exception.PortalException;

/**
 * User: SMaslyukov
 * Date: 28.08.2007
 * Time: 12:50:28
 */
public class InitPageElements {
    private final static Logger log = Logger.getLogger( InitPageElements.class );

    public static List<PageElement> initPageElements(PortalRequest portalRequest, PortalInstance portalInstance) {
        List<PageElement> pageElements = new ArrayList<PageElement>();

        // init page element list
        int i = 0;
        for (ParsedTemplateElement o : portalRequest.getTemplate().getTemplateElements()) {

            String portletName;
            Namespace namespace = null;
            PortletParameters portletParameters = null;
            String targetTemplateName;

            UrlInterpreterResult urlInterpreterResult = portalRequest.getUrlInterpreterResult();
            if (o.isPortlet() && StringUtils.isNotBlank(o.getPortlet().getTemplate())) {
                targetTemplateName = o.getPortlet().getTemplate();
            }
            else {
                targetTemplateName = urlInterpreterResult.getTemplateName();
            }

            if (o.isPortlet()) {
                Portlet p = o.getPortlet();
                portletName = TemplateUtils.getFullPortletName( p.getName() );

                namespace = NamespaceFactory.getNamespace(portletName, targetTemplateName, NamespaceFactory.getTemplateUniqueIndex(o, i++));

                portletParameters = urlInterpreterResult.getParameters().get(namespace.getNamespace());
                if (portletParameters == null) {
                    portletParameters = new PortletParameters(namespace.getNamespace(), new RequestState(), new HashMap<String, List<String>>());
                }
            }
            else if (o.isDynamic()) {
                portletName = urlInterpreterResult.getDefaultPortletName();
                namespace = NamespaceFactory.getNamespace(portletName, targetTemplateName, NamespaceFactory.getTemplateUniqueIndex(o, i++));

                portletParameters = urlInterpreterResult.getParameters().get(namespace.getNamespace());

                if (portletParameters == null) {
                    log.error(
                        "portletParameters object is null, " +
                            "namespace: " + namespace.getNamespace() + ", " +
                            "portletName: " + portletName
                    );
                    // skip this portlet
                    continue;
                }
            }

            // chech for template item is not restricted after create namespace
            // restriction of template item must be processed after
/*
            if (!checkTemplateItemRole(o)) {
                continue;
            }
*/

            PageElement element;
            switch (o.getType()) {
                case PORTLET: {
                    Portlet p = o.getPortlet();
                    if (log.isDebugEnabled()) {
                        log.debug("PageElementPortlet, " +
                            "name: " + p.getName() + ", " +
                            ", code: " + p.getCode() + ", xmlRoot: " + p.getXmlRoot());
                        log.debug("namespace: " + namespace);
                        log.debug("portletParameters: " + portletParameters);
                        log.debug("element.getParameters(): " + p.getElementParameter());
                    }

                    element = new PageElementPortlet(
                        portalInstance, namespace, portletParameters, targetTemplateName, p.getXmlRoot(), p.getCode(), p.getElementParameter(),
                        portalRequest, TemplateUtils.getFullPortletName( p.getName() ),
                        new ArrayList<String>(),
                        PreferenceFactory.getStubPortletPreferencePersistencer()
                    );
                    break;
                }
                case DYNAMIC: {
                    if (log.isDebugEnabled()) {
                        log.debug("PageElementPortlet as dynamiic");
                        log.debug("getDefaultPortletDefinition(): " + urlInterpreterResult.getDefaultPortletName());
                        log.debug("namespace: " + namespace);
                        log.debug("portletParameters: " + portletParameters);
                    }
                    element = new PageElementPortlet(
                        portalInstance, namespace, portletParameters, targetTemplateName, null, null, null,
                        portalRequest, urlInterpreterResult.getDefaultPortletName(),
                        urlInterpreterResult.getExtendedCatalogItem().getRoleList(),
                        PreferenceFactory.getPortletPreferencePersistencer(urlInterpreterResult.getExtendedCatalogItem().getCatalogId())
                    );
                    break;
                }
                case XSLT:
                    element = new XsltPageElement(o.getXslt().getName());
                    break;
                case STRING:
                    element = new StringPageElement(o.getString());
                    break;
                case INCLUDE:
                    continue;
                default:
                    throw new PortalException("Unknown type of template element: " + o.getType());
            }
            if (log.isDebugEnabled()) {
                log.debug("#5.20");
            }

            pageElements.add(element);
        }
        return pageElements;
    }
}
