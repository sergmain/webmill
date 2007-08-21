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

import org.apache.log4j.Logger;

import org.riverock.interfaces.portlet.menu.MenuLanguage;
import org.riverock.interfaces.portal.PortalInfo;
import org.riverock.webmill.port.PortalInfoImpl;
import org.riverock.webmill.portal.url.UrlInterpreterResult;
import org.riverock.webmill.portal.url.UrlInterpreterParameter;

/**
 * $Id$
 */
public class IndexUrlInterpreter implements UrlInterpreter {
    private final static Logger log = Logger.getLogger(IndexUrlInterpreter.class);

    public IndexUrlInterpreter() {
    }

    public UrlInterpreterResult interpret(UrlInterpreterParameter factoryParameter) {

        PortalInfo portalInfo = PortalInfoImpl.getInstance( factoryParameter.getSiteId() );

        // process current request as 'index'
        MenuLanguage menu = portalInfo.getMenu(factoryParameter.getPredictedLocale().toString());
        if (menu==null){
            log.error( "Menu for locale: "+factoryParameter.getPredictedLocale().toString() +" not defined" );
            return new UrlInterpreterResult();
        }

        if (menu.getIndexMenuItem() == null) {
            log.warn("menu: " + menu);
            log.warn("locale: " + factoryParameter.getPredictedLocale().toString());
            log.warn("Menu item pointed to 'index' portlet not defined");
            return new UrlInterpreterResult();
        }

        Long ctxId = menu.getIndexMenuItem().getId();
        if (ctxId==null) {
            String es = "Menu item with 'index' portlet not found";
            log.error(es);
            return new UrlInterpreterResult();
        }

        return UrlInterpreterUtils.getRequestContextBean(factoryParameter, ctxId);
    }
}
