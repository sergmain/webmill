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



package org.riverock.tools;



//import org.apache.catalina.util.CookieTools;



import javax.servlet.http.Cookie;

import javax.servlet.http.HttpServletResponse;



public class CookieWrapper

{



    public static void addCookie(HttpServletResponse response, Cookie c)

    {

/*

// xxx Not correct, but work

        c.setVersion(1);

        response.addHeader( CookieTools.getCookieHeaderName(c),

                            CookieTools.getCookieHeaderValue(c));



        c.setVersion(0);

        response.addHeader( CookieTools.getCookieHeaderName(c),

                            CookieTools.getCookieHeaderValue(c));



// xxx Correct, but not work

*/

        response.addCookie(c);



    }

}

