/*
 * org.riverock.generic - Database connectivity classes, part of Webmill portal
 * For more information about Webmill portal, please visit project site
 * http://webmill.askmore.info
 *
 * Copyright (C) 2000-2006, Riverock Software, All Rights Reserved.
 *
 * Riverock - The Open-source Java Development Community
 * http://www.riverock.org
 *
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.riverock.generic.test;

import java.net.URL;

/**
 * User: Admin
 * Date: Sep 14, 2003
 * Time: 10:00:05 PM
 *
 * $Id$
 */
public class TestURI
{
//    private static String addr = "http://me.askmore:8080/mill/ctx?aaa=bbb&ccc=ddd";
    private static String addr = "http://me.askmore";

    public static void main(String args[])
        throws Exception
    {
        URL url = new URL(addr);
        System.out.println( url.toString());
        System.out.println( "getProtocol " + url.getProtocol() );
        System.out.println( "getHost " + url.getHost() );
        System.out.println( "getPort " + url.getPort() );
        System.out.println( "getPath " + url.getPath() );
        System.out.println( "getFile " + url.getFile() );
        System.out.println( "getQuery " + url.getQuery() );
        System.out.println( "getRef " + url.getRef() );
    }
}
