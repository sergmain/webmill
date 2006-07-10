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
 * Date: 10.01.2004
 * Time: 15:03:11
 * @author Serge Maslyukov
 * $Id$
 */

package org.riverock.webmill.test;

import org.riverock.common.tools.StringTools;

import org.apache.commons.httpclient.util.URIUtil;


public class EncodeString
{

    public static void main(String args[])
    {
        String s = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><ResourseRequest>	<CodeResource>webmill.as.simple</CodeResource></ResourseRequest>";

        System.out.println("s = " + StringTools.rewriteURL( s ) );
    }

}