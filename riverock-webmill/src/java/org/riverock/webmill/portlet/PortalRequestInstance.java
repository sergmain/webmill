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

 * User: Admin

 * Date: Aug 26, 2003

 * Time: 4:40:19 PM

 *

 * $Id$

 */

package org.riverock.webmill.portlet;



import java.io.ByteArrayOutputStream;

import java.util.List;



import org.riverock.generic.db.DatabaseAdapter;

import org.riverock.webmill.port.PortalXslt;

import org.riverock.webmill.schema.site.SiteTemplate;



class PortalRequestInstance {



    ByteArrayOutputStream byteArrayOutputStream = null;



    PortalXslt xslt = null;

    SiteTemplate template = null;

    List portlets = null;



    int counter;

    long startMills;

    DatabaseAdapter db = null;



    PortalRequestInstance()

    {

        startMills = System.currentTimeMillis();

    }

}

