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
package org.riverock.webmill.portal.context;

import org.apache.log4j.Logger;

import org.riverock.interfaces.portlet.menu.MenuLanguage;

/**
 * $Id$
 */
public class IndexRequestContextProcessor implements RequestContextProcessor {
    private final static Logger log = Logger.getLogger(IndexRequestContextProcessor.class);

    public IndexRequestContextProcessor() {
    }

    public RequestContext parseRequest(RequestContextParameter factoryParameter) {

        // process current request as 'index'
        MenuLanguage menu = factoryParameter.getPortalInfo().getMenu(factoryParameter.getPredictedLocale().toString());
        if (menu==null){
            log.error( "Menu for locale: "+factoryParameter.getPredictedLocale().toString() +" not defined" );
            return null;
        }

        if (menu.getIndexMenuItem() == null) {
            log.warn("menu: " + menu);
            log.warn("locale: " + factoryParameter.getPredictedLocale().toString());
            log.warn("Menu item pointed to 'index' portlet not defined");
            return null;
        }

        Long ctxId = menu.getIndexMenuItem().getId();
        if (ctxId==null) {
            String es = "Menu item with 'index' portlet not found";
            log.error(es);
            throw new IllegalStateException(es);
        }

        return RequestContextUtils.getRequestContextBean(factoryParameter, ctxId);
    }
}
