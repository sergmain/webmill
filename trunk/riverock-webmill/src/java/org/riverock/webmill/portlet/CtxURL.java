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



package org.riverock.webmill.portlet;



import javax.servlet.http.HttpServletResponse;

import javax.servlet.http.HttpServletRequest;



import org.riverock.webmill.main.Constants;

import org.riverock.webmill.utils.ServletUtils;

import org.riverock.webmill.port.InitPage;

import org.riverock.common.tools.ServletTools;

import org.riverock.common.config.ConfigException;

import org.riverock.generic.config.GenericConfig;



/**

 *

 * $Author$

 *

 * $Id$

 *

 */

public class CtxURL

{

    public CtxURL()

    {

    }



    public static String ctx()

    {

        if (GenericConfig.contextName  == null)

            return Constants.URI_CTX_MANAGER ;



        return GenericConfig.contextName + Constants.URI_CTX_MANAGER ;

    }



    public static String urlAsForm(

        String nameTemplate,

        InitPage page,

        String portlet

        )

    {

        return

            page.getAsForm()+

            ServletTools.getHiddenItem(Constants.NAME_TEMPLATE_CONTEXT_PARAM, nameTemplate)+

            ServletTools.getHiddenItem(Constants.NAME_TYPE_CONTEXT_PARAM, portlet);

    }



    public static String url(HttpServletRequest request, HttpServletResponse response,

                             InitPage page,

                             String portlet

                             )

        throws ConfigException

    {

        return response.encodeURL( CtxURL.ctx()  ) + '?' +

            page.getAsURL()+

                Constants.NAME_TEMPLATE_CONTEXT_PARAM    +'='+

                ServletUtils.getString(request, Constants.NAME_TEMPLATE_CONTEXT_PARAM)    +'&'+

                Constants.NAME_TYPE_CONTEXT_PARAM    + '=' + portlet;

    }

}