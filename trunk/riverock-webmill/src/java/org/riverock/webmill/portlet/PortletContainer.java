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



/**

 * User: serg_main

 * Date: 28.01.2004

 * Time: 17:49:32

 * @author Serge Maslyukov

 * $Id$

 */



package org.riverock.webmill.portlet;



import java.util.Map;

import java.util.HashMap;



import javax.portlet.Portlet;





public class PortletContainer

{



    private static Map portletsMap = new HashMap();



    private static Object syncObj = new Object();

    public static Portlet getPortletInstance(String namePortlet)

    {

        Object obj = portletsMap.get(namePortlet);

        if (obj==null)

        {

            synchronized(syncObj)

            {

                Object temp  = portletsMap.get(namePortlet);

                if (temp!=null)

                    return (Portlet)temp;



                Portlet newPortlet = createInstance(namePortlet);

                portletsMap.put(namePortlet, newPortlet);

                return newPortlet;

            }

        }

        return (Portlet)obj;

    }



    private static Portlet createInstance(String namePortlet)

    {

        return null;

    }





}

