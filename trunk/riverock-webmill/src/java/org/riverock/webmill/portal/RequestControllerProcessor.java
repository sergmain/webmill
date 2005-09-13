package org.riverock.webmill.portal;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;

import org.riverock.webmill.container.tools.PortletService;
import org.riverock.webmill.container.ContainerConstants;
import org.riverock.webmill.container.portlet.bean.PortletDefinition;

import org.apache.log4j.Logger;

/**
 * User: SergeMaslyukov
 * Date: 24.11.2004
 * Time: 23:53:48
 * $Id$
 */
public class RequestControllerProcessor {

    private static Logger log = Logger.getLogger(RequestControllerProcessor.class);

    static void processControllerRequest(PortletDefinition portlet, PortalRequestInstance portalRequestInstance)
        throws IOException, ServletException {

//        Map map = PortletTools.getParameters(portalRequestInstance.getHttpRequest());
//        Map tempMap = null;
//        if (ctxInstance.getDefaultPortletDescription()!=null) {
//            tempMap = CtxInstance.getGlobalParameter( ctxInstance.getDefaultPortletDescription().getPortletConfig(), ctxInstance.getPortletId());
//            map.putAll( tempMap );
//
//            if (log.isDebugEnabled()) {
//                log.debug( "Print param from map" );
//                for ( Iterator it = tempMap.keySet().iterator(); it.hasNext(); ) {
//                    Object s = it.next();
//                    log.debug( "Param in map - "+s.toString()+", value - "+ tempMap.get(s) );
//                }
//            }
//        }

//        ctxInstance.setParameters(
//            map,
//            PortletService.getStringParam(portlet, PortletTools.locale_name_package)
//        );
//        ctxInstance.getPortletRequest().getPortletSession().setAttribute( Constants.PORTLET_REQUEST_SESSION, ctxInstance );

        Boolean isUrlTemp =
            PortletService.getBooleanParam( portlet, ContainerConstants.is_url );

        if ( Boolean.TRUE.equals(isUrlTemp) ) {
            RequestDispatcher dispatcher = portalRequestInstance.getHttpRequest().getRequestDispatcher( portlet.getPortletClass() );
            if ( log.isDebugEnabled() )
                log.debug( "RequestDispatcher - "+dispatcher );

            if ( dispatcher==null ) {
                String errorString = "Error get dispatcher for path "+portlet.getPortletClass();
                log.error( errorString );
                portalRequestInstance.byteArrayOutputStream.write( errorString.getBytes() );
            }
            else
                dispatcher.forward( portalRequestInstance.getHttpRequest(), portalRequestInstance.getHttpResponse() );
        }
        else {
            portalRequestInstance.byteArrayOutputStream.write(
                "Error forward to 'controller' portlet. Portlet path is null".getBytes()
            );
        }
//        ctxInstance.getPortletRequest().getPortletSession().removeAttribute( Constants.PORTLET_REQUEST_SESSION );
    }
}
