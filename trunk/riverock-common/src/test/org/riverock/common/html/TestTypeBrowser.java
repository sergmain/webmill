/*

 * org.riverock.common -- Supporting classes, interfaces, and utilities

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

 * User: Admin

 * Date: Mar 20, 2003

 * Time: 9:28:58 PM

 *

 * $Id$

 */

package org.riverock.common.html;



import junit.framework.TestCase;



public class TestTypeBrowser extends TestCase

{

    public TestTypeBrowser(){}



    public TestTypeBrowser(String testName)

    {

        super(testName);

    }



    static class TypeBrowserItem

    {

        String s = null;

        int t;

        String v = null;



        public TypeBrowserItem( String s_, int t_, String v_)

        {

            this.s = s_;

            this.t = t_;

            this.v = v_;

        }



        public TypeBrowserItem( String s_, int t_)

        {

            this.s = s_;

            this.t = t_;

        }

    }



    private void testTypeBrowser(TypeBrowserItem tl)

        throws Exception

    {

        TypeBrowser tb = new TypeBrowser(tl.s);

        System.out.println("version - "+tb.version);

        assertTrue(

            "\nError get type browser. User-agent '"+tl.s+"'\n" +

            "Expected '"+tl.t+"', but result is '"+tb.type+"'",

            tb.type==tl.t

        );



        if (tl.v!=null)

        {

            assertTrue(

                "\nError get type browser. User-agent '"+tl.s+"'\n" +

                "Expected '"+tl.v+"', but result is '"+tb.version+"'",

                tl.v.equals( tb.version )

            );

        }

    }



    public void testTypeBrowser()

        throws Exception

    {

        TypeBrowserItem[] items =

            {

                // Unknown famaly

                new TypeBrowserItem("IBrowse/1.22 (AmigaOS 3.0)", TypeBrowser.IBROWSE_TYPE, "1.22"),



                // IE famaly

                new TypeBrowserItem("Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)", TypeBrowser.IE, "6.0"),

                new TypeBrowserItem("Mozilla/4.0 (compatible; MSIE 4.01; Windows 98)", TypeBrowser.IE, "4.01"),



                // Lynx famaly

                new TypeBrowserItem("Lynx/2.8.3dev.18 libwww-FM/2.14", TypeBrowser.LYNX, "2.8.3dev.18"),



                // Java URL fanaly

                new TypeBrowserItem("Java1.3.0_01", TypeBrowser.JAVA_RUNTIME, "1.3.0_01"),

                new TypeBrowserItem("Java/1.4.1_02", TypeBrowser.JAVA_RUNTIME, "1.4.1_02"),



                // Netscape Navigator famaly

                new TypeBrowserItem("Mozilla/4.06 [en] (Win98; I)", TypeBrowser.NN, "4.06"),

                new TypeBrowserItem("Mozilla/4.51 (Macintosh; U; PPC)", TypeBrowser.NN, "4.51"),

                new TypeBrowserItem("Mozilla/4.6 [en] (Win98; I)", TypeBrowser.NN, "4.6"),

                new TypeBrowserItem("Mozilla/4.6C-SGI [en] (X11; I; IRIX 6.3 IP32)", TypeBrowser.NN, "4.6C-SGI"),

                new TypeBrowserItem("Mozilla/5.0 (Windows; U; Windows NT 5.0; en-US; rv:1.0.1) Gecko/20020823 Netscape/7.0", TypeBrowser.NN, "7.0"),



                // Mozilla famaly

                new TypeBrowserItem("Mozilla/5.0 (Windows; U; Windows NT 5.0; en-US; rv:1.1) Gecko/20020826", TypeBrowser.MOZILLA_TYPE, "1.1"),



                // opera famaly

                new TypeBrowserItem("Mozilla/4.0 (compatible; MSIE 5.0; Windows 2000) Opera 6.05  [ru]", TypeBrowser.OPERA, "6.05"),

                new TypeBrowserItem("Mozilla/4.0 (compatible; MSIE 5.0; Windows 98) Opera 5.02  [ru]", TypeBrowser.OPERA, "5.02"),

                new TypeBrowserItem("Opera/7.03 (Windows NT 5.0; U)  [ru]", TypeBrowser.OPERA, "7.03"),

                new TypeBrowserItem("Mozilla/3.0 (compatible; Opera/3.0; Windows 95/NT4) 3.21",TypeBrowser.OPERA, "3.0"),



                // Delphi request

                new TypeBrowserItem("Mozilla/3.0 (compatible; Indy Library)", TypeBrowser.DELPHI_TYPE, ""),



                // FrontPage

                new TypeBrowserItem("Mozilla/2.0 (compatible; MS FrontPage 4.0)", TypeBrowser.FRONTPAGE_TYPE, "4.0"),

                new TypeBrowserItem("MSFrontPage/4.0", TypeBrowser.FRONTPAGE_TYPE, "4.0"),

                new TypeBrowserItem("Mozilla/2.0 (compatible; MS FrontPage 5.0)", TypeBrowser.FRONTPAGE_TYPE, "5.0"),

                new TypeBrowserItem("MSFrontPage/5.0", TypeBrowser.FRONTPAGE_TYPE, "5.0"),



                // Microsoft Data Access Internet Publishing

                new TypeBrowserItem("Microsoft Data Access Internet Publishing Provider DAV",

                    TypeBrowser.MS_DATA_ACCESS_INTERNET_PUBLISHING_TYPE, ""),

                new TypeBrowserItem("Microsoft Data Access Internet Publishing Provider Cache Manager",

                    TypeBrowser.MS_DATA_ACCESS_INTERNET_PUBLISHING_TYPE, ""),



                new TypeBrowserItem("HP OpenView/Network Node Manager", TypeBrowser.HP_OPENVIEW_TYPE, ""),



                new TypeBrowserItem("Microsoft-WebDAV-MiniRedir/5.1.2600", TypeBrowser.MS_WEBDAV_TYPE, "")



            };



        for (int i=0; i<items.length; i++)

        {

            testTypeBrowser( items[i] );

        }

    }

}







