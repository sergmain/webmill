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



import org.riverock.generic.config.GenericConfig;

import org.riverock.webmill.main.Constants;



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



    public static String pageid()

    {

        if (GenericConfig.contextName  == null)

            return Constants.PAGEID_SERVLET_NAME ;



        return GenericConfig.contextName + Constants.PAGEID_SERVLET_NAME ;

    }



    public static String page()

    {

        if (GenericConfig.contextName  == null)

            return Constants.PAGE_SERVLET_NAME ;



        return GenericConfig.contextName + Constants.PAGE_SERVLET_NAME ;

    }



}