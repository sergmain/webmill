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
package org.riverock.webmill.portal.url;

import org.apache.log4j.Logger;

import org.riverock.webmill.portal.url.interpreter.*;

/**
 * @author Sergei Maslyukov
 *         Date: 20.04.2006
 *         Time: 12:39:24
 */
public class UrlInterpreterIterator {
    private final static Logger log = Logger.getLogger( UrlInterpreterIterator.class );

    private static final UrlInterpreter[] INTERPRETER = new UrlInterpreter[]{
        new UrlAliasInterpreter(),
        new PortletAliasUrlInterpreter(),
        new PageUrlInterpreter(),
        new PageidUrlInterpreter(),
        new CtxUrlInterpreter(),
        new IndexUrlInterpreter()
    };

    /**
     * init context type and name of template, if type of context is null, set it to 'index_page'
     *
     * @param factoryParameter request context parameters
     * @return request context
     */
    public static UrlInterpreterResult interpretUrl( UrlInterpreterParameter factoryParameter ) {

        for (UrlInterpreter urlInterpreter : INTERPRETER) {
            UrlInterpreterResult urlInterpreterResult = urlInterpreter.interpret(factoryParameter);
            if (log.isDebugEnabled()) {
                log.debug("urlInterpreter: " + urlInterpreter+", result: " + urlInterpreterResult);
            }
            if (urlInterpreterResult !=null) {
                return urlInterpreterResult;
            }
        }
        return null;
    }

}
