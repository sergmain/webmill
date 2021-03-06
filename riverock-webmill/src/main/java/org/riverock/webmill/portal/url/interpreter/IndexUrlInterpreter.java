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

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import org.riverock.interfaces.portlet.menu.MenuLanguage;

/**
 * $Id: IndexUrlInterpreter.java 1430 2007-09-14 14:48:24Z serg_main $
 */
public class IndexUrlInterpreter implements UrlInterpreter {
    private final static Logger log = Logger.getLogger(IndexUrlInterpreter.class);

    public IndexUrlInterpreter() {
    }

    public UrlInterpreterResult interpret(UrlInterpreterParameter factoryParameter) {

        if (StringUtils.isNotBlank(factoryParameter.getPathInfo()) && !StringUtils.equals("/", factoryParameter.getPathInfo())) {
            return null;
        }
        
        // process current request as 'index'
        MenuLanguage menu = factoryParameter.getPortalInfo().getMenu(factoryParameter.getPredictedLocale().toString());
        if (menu==null){
            log.warn( "Menu for locale: "+factoryParameter.getPredictedLocale().toString() +" not defined" );
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
            log.warn(es);
            return null;
        }

        return UrlInterpreterUtils.getRequestContextBean(factoryParameter, ctxId);
    }
}
