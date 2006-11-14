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
package org.riverock.webmill.portal.context;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import org.riverock.webmill.container.ContainerConstants;

/**
 * @author Sergei Maslyukov
 *         Date: 20.04.2006
 *         Time: 12:39:24
 */
public class RequestContextFactory {
    private static Logger log = Logger.getLogger( RequestContextProcessor.class );

    public static final int UNKNOWN_SERVLET_IDX = 0;
    public static final int PAGEID_SERVLET_IDX = 1;
    public static final int PAGE_SERVLET_IDX = 2;
    public static final int CTX_SERVLET_IDX = 3;
    private static Map<String, Integer> requestContextMap = new HashMap<String, Integer>();

    static {
        requestContextMap.put(ContainerConstants.PAGEID_SERVLET_NAME, RequestContextFactory.PAGEID_SERVLET_IDX);
        requestContextMap.put(ContainerConstants.PAGE_SERVLET_NAME, RequestContextFactory.PAGE_SERVLET_IDX);
        requestContextMap.put(ContainerConstants.URI_CTX_MANAGER, RequestContextFactory.CTX_SERVLET_IDX);
    }

    /**
     * init context type and name of template,
     * if type of context is null, set it to 'index_page'
     */
    public static RequestContext createRequestContext( RequestContextParameter factoryParameter ) {

        RequestContextProcessor processor = null;
        String servletPath = factoryParameter.getRequest().getServletPath();
        Integer idx = requestContextMap.get(servletPath);
        if (idx!=null) {
            switch(idx) {
                case PAGEID_SERVLET_IDX:
                    processor = new PageidRequestContextProcessor();
                    break;
                case PAGE_SERVLET_IDX:
                    processor = new PageRequestContextProcessor();
                    break;
                case CTX_SERVLET_IDX:
                    processor = new CtxRequestContextPocessor();
                    break;
            }
        }
        if (processor==null) {
            throw new IllegalStateException(
                "Unknown servlet path mapping: "+servletPath+". " +
                    "Check servelt mapping in WEB-INF/web.xml file. " +
                    "Current valid value: /ctx, /page, /pageid"
            );
        }

        if (log.isDebugEnabled()) {
            log.debug("servletPath: " + servletPath);
            log.debug("processor: " + processor);
        }

        RequestContext requestContext = processor.parseRequest(factoryParameter);
        if ( log.isDebugEnabled() ) {
            log.debug( "#2 requestContext: "+requestContext );
            if (requestContext !=null) {
                log.debug( "requestContext.getDefaultPortletName(): "+requestContext.getDefaultPortletName() );
                log.debug( "requestContext.getTemplateName(): "+requestContext.getTemplateName() );
            }
        }

        // return as index context
        if (requestContext==null) {
            log.debug("Process this request as 'index'");
            requestContext = new IndexRequestContextProcessor().parseRequest(factoryParameter);
        }

        return requestContext;
    }

}
