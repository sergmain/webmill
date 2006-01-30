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
package org.riverock.webmill.utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import org.riverock.webmill.config.WebmillConfig;


/**
 * User: serg_main
 * Date: 21.11.2003
 * Time: 19:50:58
 * @author Serge Maslyukov
 * $Id$
 *
 */
public final class ServletUtils {
    private final static Logger log = Logger.getLogger( ServletUtils.class );

    public static String getString( final HttpServletRequest request, final String f) {
        return org.riverock.common.tools.ServletTools.getString(request, f, "", WebmillConfig.getServerCharset(), WebmillConfig.getHtmlCharset());
    }

    public static String getString( final HttpServletRequest request, final String f, final String def) {
        return org.riverock.common.tools.ServletTools.getString( request, f, def, WebmillConfig.getServerCharset(), WebmillConfig.getHtmlCharset() );
    }

    public static void setContentType( final HttpServletResponse response, final String charset )
        throws Exception {
        if (log.isDebugEnabled())
            log.debug("set new charset - "+charset);

        try {
            response.setContentType("text/html; charset=" + charset);
        }
        catch(Exception e) {
            log.error("Error set new content type to "+charset);
            throw e;
        }
    }

    public static void setContentType( final HttpServletResponse response)
        throws Exception {
        setContentType(response, WebmillConfig.getHtmlCharset());
    }

}
