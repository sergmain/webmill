/*

 * org.riverock.generic -- Database connectivity classes

 * 

 * Copyright (C) 2004, Riverock Software, All Rights Reserved.

 * 

 * Riverock -- The Open-source Java Development Community

 * http://www.riverock.org

 * 

 * 

 * This library is free software; you can redistribute it and/or

 * modify it under the terms of the GNU Lesser General Public

 * License as published by the Free Software Foundation; either

 * version 2.1 of the License, or (at your option) any later version.

 *

 * This library is distributed in the hope that it will be useful,

 * but WITHOUT ANY WARRANTY; without even the implied warranty of

 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU

 * Lesser General Public License for more details.

 *

 * You should have received a copy of the GNU Lesser General Public

 * License along with this library; if not, write to the Free Software

 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA

 *

 */



/**

 * @deprecated

 * Author: mill

 * Date: Apr 1, 2003

 * Time: 5:40:22 PM

 *

 * $Id$

 */



package org.riverock.generic.tools;



import java.util.TimeZone;



public class GregorianCalendarWrapper extends

    org.riverock.common.tools.GregorianCalendarWrapper

{



    public GregorianCalendarWrapper()

    {

        super();

    }



    public GregorianCalendarWrapper( TimeZone zone )

    {

        super( zone );

    }



    public long getTimeInMillis()

    {

        return super.getTimeInMillis();

    }

}

