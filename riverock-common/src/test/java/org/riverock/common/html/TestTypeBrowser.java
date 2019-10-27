/*
 * org.riverock.common - Supporting classes, interfaces, and utilities
 *
 * Copyright (C) 2006, Riverock Software, All Rights Reserved.
 *
 * Riverock - The Open-source Java Development Community
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
 */

package org.riverock.common.html;

import junit.framework.TestCase;

/**
 * User: Admin
 * Date: Mar 20, 2003
 * Time: 9:28:58 PM
 * <p/>
 * $Id: TestTypeBrowser.java 1040 2006-11-14 13:57:38Z serg_main $
 */
public class TestTypeBrowser extends TestCase {

    static class TypeBrowserItem {
        String s = null;
        int t;
        String v = null;

        public TypeBrowserItem(String s_, int t_, String v_) {
            this.s = s_;
            this.t = t_;
            this.v = v_;
        }

        public TypeBrowserItem(String s_, int t_) {
            this.s = s_;
            this.t = t_;
        }
    }

    private void testTypeBrowser(TypeBrowserItem tl)
        throws Exception {
        TypeBrowser tb = new TypeBrowser(tl.s);
        System.out.println("version - " + tb.version);
        assertTrue(
            "\nError get type browser. User-agent '" + tl.s + "'\n" +
                "Expected type '" + tl.t + "', but result type is '" + tb.type + "'",
            tb.type == tl.t
        );

        if (tl.v != null) {
            assertTrue(
                "\nError get type browser. User-agent '" + tl.s + "'\n" +
                    "Expected '" + tl.v + "', but result is '" + tb.version + "'",
                tl.v.equals(tb.version)
            );
        }
    }

    public void testTypeBrowser() throws Exception {
        TypeBrowserItem[] items =
            {
                // Blank type
                new TypeBrowserItem(null, TypeBrowser.UNKNOWN, ""),
                new TypeBrowserItem("", TypeBrowser.UNKNOWN, ""),

                // Unknown famaly
                new TypeBrowserItem("StackRambler/2.0 (MSIE incompatible)", TypeBrowser.UNKNOWN, ""),

                // IBrowse ???
                new TypeBrowserItem("IBrowse/1.22 (AmigaOS 3.0)", TypeBrowser.IBROWSE_TYPE, "1.22"),


                // IE famaly
                new TypeBrowserItem("Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)", TypeBrowser.IE, "6.0"),
                new TypeBrowserItem("Mozilla/4.0 (compatible; MSIE 4.01; Windows 98)", TypeBrowser.IE, "4.01"),

                // Lynx famaly
                new TypeBrowserItem("Lynx/2.8.3dev.18 libwww-FM/2.14", TypeBrowser.LYNX, "2.8.3dev.18"),

                // Java URL fanaly
                new TypeBrowserItem("Java1.3.0_01", TypeBrowser.JAVA_RUNTIME, "1.3.0_01"),
                new TypeBrowserItem("Java/1.4.1_02", TypeBrowser.JAVA_RUNTIME, "1.4.1_02"),
                new TypeBrowserItem("Java/1.6.0-beta", TypeBrowser.JAVA_RUNTIME, "1.6.0-beta"),
                new TypeBrowserItem("Java/1.6.0-beta", TypeBrowser.JAVA_RUNTIME, "1.6.0-beta"),
                new TypeBrowserItem("Wed  Jun  25  22:00:01  EDT  2003WebSurvey  Java/1.4.2-beta", TypeBrowser.JAVA_RUNTIME, "1.4.2-beta"),
                new TypeBrowserItem("Sat  Sep  20  23:51:20  CEST  2003WebSurvey  Java/1.4.1_03", TypeBrowser.JAVA_RUNTIME, "1.4.1_03"),
                new TypeBrowserItem("Mozilla/4.0  (compatible;  MSIE  6.0;  Windows  NT  5.1)  Java/1.4.1_02", TypeBrowser.JAVA_RUNTIME, "1.4.1_02"),

                // Netscape Navigator famaly
                new TypeBrowserItem("Mozilla/4.06 [en] (Win98; I)", TypeBrowser.NN, "4.06"),
                new TypeBrowserItem("Mozilla/4.51 (Macintosh; U; PPC)", TypeBrowser.NN, "4.51"),
                new TypeBrowserItem("Mozilla/4.6 [en] (Win98; I)", TypeBrowser.NN, "4.6"),
                new TypeBrowserItem("Mozilla/4.6C-SGI [en] (X11; I; IRIX 6.3 IP32)", TypeBrowser.NN, "4.6C-SGI"),
                new TypeBrowserItem("Mozilla/5.0 (Windows; U; Windows NT 5.0; en-US; rv:1.0.1) Gecko/20020823 Netscape/7.0", TypeBrowser.NN, "7.0"),

                // Mozilla famaly
                new TypeBrowserItem("Mozilla/5.0 (Windows; U; Windows NT 5.0; en-US; rv:1.1) Gecko/20020826", TypeBrowser.MOZILLA_TYPE, "1.1"),
                new TypeBrowserItem("Mozilla/5.0", TypeBrowser.MOZILLA_TYPE, "5.0"),

                // AOL famaly
                new TypeBrowserItem("Mozilla/4.0  (compatible;  MSIE  6.0;  AOL  7.0;  Windows  NT  5.1)", TypeBrowser.AOL_TYPE, "7.0"),
                new TypeBrowserItem("Mozilla/4.0  (compatible;  MSIE  6.0;  AOL  8.0;  Windows  NT  5.1)", TypeBrowser.AOL_TYPE, "8.0"),
                new TypeBrowserItem("Mozilla/4.0  (compatible;  MSIE  5.0;  AOL  6.0;  Windows  98;  DigExt)", TypeBrowser.AOL_TYPE, "6.0"),
                new TypeBrowserItem("Mozilla/4.0  (compatible;  MSIE  6.0;  AOL  9.0;  Windows  NT  5.1;  SV1;  FunWebProducts;  .NET  CLR  1.1.4322)", TypeBrowser.AOL_TYPE, "9.0"),

                // Konqueror type
                new TypeBrowserItem("Mozilla/5.0  (compatible;  Konqueror/3.1;  Linux)", TypeBrowser.KONQUEROR_TYPE, "3.1"),
                new TypeBrowserItem("Mozilla/5.0  (compatible;  Konqueror/3.1;  Linux;  ru,  ru_RU,  ru_RU.KOI8-R)", TypeBrowser.KONQUEROR_TYPE, "3.1"),
                new TypeBrowserItem("Mozilla/5.0  (compatible;  Konqueror/3.1;  Linux;  fr,  en_US,  fr_FR)  (KHTML,  like  Gecko)", TypeBrowser.KONQUEROR_TYPE, "3.1"),
                new TypeBrowserItem("Mozilla/5.0  (compatible;  Konqueror/3.2;  Linux;  en_US,  en)  (KHTML,  like  Gecko)", TypeBrowser.KONQUEROR_TYPE, "3.2"),
                new TypeBrowserItem("Mozilla/5.0  (compatible;  Konqueror/3.0-rc4;  i686  Linux;  20020416)", TypeBrowser.KONQUEROR_TYPE, "3.0-rc4"),
                new TypeBrowserItem("Mozilla/5.0  (compatible;  Konqueror/3.1-rc3;  i686  Linux;  20020317)", TypeBrowser.KONQUEROR_TYPE, "3.1-rc3"),
                new TypeBrowserItem("Mozilla/5.0  (compatible;  Konqueror/3.0-rc6;  i686  Linux;  20020227)", TypeBrowser.KONQUEROR_TYPE, "3.0-rc6"),
                new TypeBrowserItem("Mozilla/5.0  (compatible;  Konqueror/3.1-rc4;  i686  Linux;  20021123)", TypeBrowser.KONQUEROR_TYPE, "3.1-rc4"),
                new TypeBrowserItem("Mozilla/5.0  (compatible;  Konqueror/3.4;  Linux)  KHTML/3.4.1  (like  Gecko)", TypeBrowser.KONQUEROR_TYPE, "3.4"),

                // Safari type
                new TypeBrowserItem("Mozilla/5.0  (Macintosh;  U;  PPC  Mac  OS  X;  en-us)  AppleWebKit/73  (KHTML,  like  Gecko)  Safari/73", TypeBrowser.SAFARI_TYPE, "73"),
                new TypeBrowserItem("Mozilla/5.0  (Macintosh;  U;  PPC  Mac  OS  X;  en-us)  AppleWebKit/85  (KHTML,  like  Gecko)  Safari/85", TypeBrowser.SAFARI_TYPE, "85"),
                new TypeBrowserItem("Mozilla/5.0  (Macintosh;  U;  PPC  Mac  OS  X;  en-us)  AppleWebKit/85.7  (KHTML,  like  Gecko)  Safari/85.5", TypeBrowser.SAFARI_TYPE, "85.5"),
                new TypeBrowserItem("Mozilla/5.0  (Macintosh;  U;  Intel  Mac  OS  X;  en)  AppleWebKit/418.8  (KHTML,  like  Gecko)  Safari/419.3", TypeBrowser.SAFARI_TYPE, "419.3"),
                new TypeBrowserItem("Mozilla/5.0  (Macintosh;  U;  PPC  Mac  OS  X;  en)  AppleWebKit/418  (KHTML,  like  Gecko)  Safari/417.9.3", TypeBrowser.SAFARI_TYPE, "417.9.3"),
                new TypeBrowserItem("Mozilla/5.0  (Macintosh;  U;  PPC  Mac  OS  X;  en)  AppleWebKit/312.1  (KHTML,  like  Gecko)  Safari/312", TypeBrowser.SAFARI_TYPE, "312"),
                new TypeBrowserItem("Mozilla/5.0  (Macintosh;  U;  PPC  Mac  OS  X;  en)  AppleWebKit/312.8  (KHTML,  like  Gecko)  Safari/312.6", TypeBrowser.SAFARI_TYPE, "312.6"),

                // Firefox type
                new TypeBrowserItem("Mozilla/5.0  (Windows;  U;  Windows  NT  5.1;  en-US;  rv:1.6)  Gecko/20040206  Firefox/0.8", TypeBrowser.FIREFOX_TYPE, "0.8"),
                new TypeBrowserItem("Mozilla/5.0  (Windows;  U;  Windows  NT  5.1;  en-US;  rv:1.6)  Gecko/20040206  Firefox/0.8", TypeBrowser.FIREFOX_TYPE, "0.8"),
                new TypeBrowserItem("Mozilla/5.0  (X11;  U;  Linux  i686;  en-US;  rv:1.7b)  Gecko/20040224  Firefox/0.8.0+", TypeBrowser.FIREFOX_TYPE, "0.8.0+"),
                new TypeBrowserItem("Mozilla/5.0  (Windows;  U;  Windows  NT  5.1;  en-US;  rv:1.7.5)  Gecko/20041107  Firefox/1.0", TypeBrowser.FIREFOX_TYPE, "1.0"),
                new TypeBrowserItem("Mozilla/5.0  (Windows;  U;  Windows  NT  5.1;  ru-RU;  rv:1.7.8)  Gecko/20050511  Firefox/1.0.4", TypeBrowser.FIREFOX_TYPE, "1.0.4"),
                new TypeBrowserItem("Mozilla/5.0  (Windows;  U;  Windows  NT  5.1;  en-US;  rv:1.7.12)  Gecko/20050915  Firefox/1.0.7", TypeBrowser.FIREFOX_TYPE, "1.0.7"),
                new TypeBrowserItem("Mozilla/5.0  (Windows;  U;  Windows  NT  5.1;  en-US;  rv:1.8)  Gecko/20051111  Firefox/1.5", TypeBrowser.FIREFOX_TYPE, "1.5"),
                new TypeBrowserItem("Mozilla/5.0  (Windows;  U;  Windows  NT  5.1;  en-US;  rv:1.8.0.4)  Gecko/20060508  Firefox/1.5.0.4", TypeBrowser.FIREFOX_TYPE, "1.5.0.4"),
                new TypeBrowserItem("Mozilla/5.0  (X11;  U;  Linux  i686;  ru-RU;  rv:1.7.12)  Gecko/20060202  Fedora/1.0.7-1.2.fc4  Firefox/1.0.7", TypeBrowser.FIREFOX_TYPE, "1.0.7"),
                new TypeBrowserItem("Mozilla/5.0  (Windows;  U;  Windows  NT  5.1;  ru-RU;  rv:1.7.6)  Gecko/20050226  Firefox/1.0.1", TypeBrowser.FIREFOX_TYPE, "1.0.1"),
                new TypeBrowserItem("Mozilla/5.0  (Windows;  U;  Windows  NT  5.0;  en-US;  rv:1.7.5)  Gecko/20041107  Firefox/1.0", TypeBrowser.FIREFOX_TYPE, "1.0"),
                new TypeBrowserItem("Mozilla/5.0  (X11;  U;  Linux  i686;  en-US;  rv:1.8.0.4)  Gecko/20060406  Firefox/1.5.0.4  (Debian-1.5.dfsg+1.5.0.4-1)", TypeBrowser.FIREFOX_TYPE, "1.5.0.4"),
                new TypeBrowserItem("Mozilla/5.0  (Windows;  U;  Windows  NT  5.1;  zh-CN;  rv:1.7.5)  Gecko/20041124  Firefox/1.0  (ax)", TypeBrowser.FIREFOX_TYPE, "1.0"),
                new TypeBrowserItem("Mozilla/5.0  (Windows;  U;  Windows  NT  5.1;  en-US;  rv:1.7.12)  Gecko/20050915  Firefox/1.0.7  (CCK-Boeing)", TypeBrowser.FIREFOX_TYPE, "1.0.7"),
                new TypeBrowserItem("Mozilla/5.0  (Windows;  U;  Windows  NT  5.2;  en-US;  rv:1.8.0.4)  Gecko/20060612  Firefox/1.5.0.4  Flock/0.7.0.17.1", TypeBrowser.FIREFOX_TYPE, "1.5.0.4"),

                // opera famaly
                new TypeBrowserItem("Mozilla/4.0 (compatible; MSIE 5.0; Windows 2000) Opera 6.05  [ru]", TypeBrowser.OPERA, "6.05"),
                new TypeBrowserItem("Mozilla/4.0 (compatible; MSIE 5.0; Windows 98) Opera 5.02  [ru]", TypeBrowser.OPERA, "5.02"),
                new TypeBrowserItem("Opera/7.03 (Windows NT 5.0; U)  [ru]", TypeBrowser.OPERA, "7.03"),
                new TypeBrowserItem("Mozilla/3.0 (compatible; Opera/3.0; Windows 95/NT4) 3.21", TypeBrowser.OPERA, "3.0"),
                new TypeBrowserItem("Opera/9.01  (Windows  NT  5.2;  U;  ru)", TypeBrowser.OPERA, "9.01"),
                new TypeBrowserItem("Opera/7.23  (Windows  NT  5.0;  U)    [ru]", TypeBrowser.OPERA, "7.23"),
                new TypeBrowserItem("Opera/9.00  (Windows  NT  5.1;  U;  nl)", TypeBrowser.OPERA, "9.00"),
                new TypeBrowserItem("Mozilla/4.0  (compatible;  MSIE  6.0;  Windows  NT  5.1;  ru)  Opera  9.01", TypeBrowser.OPERA, "9.01"),
                new TypeBrowserItem("Opera/8.0  (Windows  NT  5.1;  U;  en)", TypeBrowser.OPERA, "8.0"),
                new TypeBrowserItem("Mozilla/4.0  (compatible;  MSIE  6.0;  Windows  NT  5.1)  Opera  7.10    [en]", TypeBrowser.OPERA, "7.10"),
                new TypeBrowserItem("Opera/6.12  (UNIX;  U)    [en]", TypeBrowser.OPERA, "6.12"),
                new TypeBrowserItem("Mozilla/4.0  (compatible;  MSIE  6.0;  MSIE  5.5;  Windows  NT  5.1)  Opera  7.0    [en]", TypeBrowser.OPERA, "7.0"),
                new TypeBrowserItem("Mozilla/5.0  (Windows  XP;  U)  Opera  6.05    [ru]", TypeBrowser.OPERA, "6.05"),
                new TypeBrowserItem("Opera/7.10  (Windows  NT  5.0;  U)    [en]  WebWasher  3.3", TypeBrowser.OPERA, "7.10"),
                new TypeBrowserItem("Mozilla/4.0  (compatible;  MSIE  6.0;  Windows  NT  5.0)  Opera  7.10    [en]", TypeBrowser.OPERA, "7.10"),
                new TypeBrowserItem("Opera/7.10  (Linux  2.4.18-4GB  i686;  U)    [en]", TypeBrowser.OPERA, "7.10"),
                new TypeBrowserItem("Mozilla/5.0  (Windows  NT  5.0;  U)  Opera  7.02  Bork-edition    [en]", TypeBrowser.OPERA, "7.02"),
                new TypeBrowserItem("Mozilla/4.0  (compatible;  MSIE  6.0;  Windows  NT  5.1;  en)  Opera  8.51", TypeBrowser.OPERA, "8.51"),
                new TypeBrowserItem("Mozilla/4.0  (compatible;  MSIE  6.0;  Windows  NT  5.1)  Opera  7.60    [en]  (IBM  EVV/3.0/EAK01AG9/LE)", TypeBrowser.OPERA, "7.60"),

                // Delphi request
                new TypeBrowserItem("Mozilla/3.0 (compatible; Indy Library)", TypeBrowser.DELPHI_TYPE, ""),

                // FrontPage
                new TypeBrowserItem("Mozilla/2.0 (compatible; MS FrontPage 4.0)", TypeBrowser.FRONTPAGE_TYPE, "4.0"),
                new TypeBrowserItem("MSFrontPage/4.0", TypeBrowser.FRONTPAGE_TYPE, "4.0"),
                new TypeBrowserItem("Mozilla/2.0 (compatible; MS FrontPage 5.0)", TypeBrowser.FRONTPAGE_TYPE, "5.0"),
                new TypeBrowserItem("MSFrontPage/5.0", TypeBrowser.FRONTPAGE_TYPE, "5.0"),
                new TypeBrowserItem("MSFrontPage/6.0", TypeBrowser.FRONTPAGE_TYPE, "6.0"),

                // Microsoft Data Access Internet Publishing
                new TypeBrowserItem("Microsoft Data Access Internet Publishing Provider DAV",
                    TypeBrowser.MS_DATA_ACCESS_INTERNET_PUBLISHING_TYPE, ""),
                new TypeBrowserItem("Microsoft Data Access Internet Publishing Provider Cache Manager",
                    TypeBrowser.MS_DATA_ACCESS_INTERNET_PUBLISHING_TYPE, ""),

                new TypeBrowserItem("HP OpenView/Network Node Manager", TypeBrowser.HP_OPENVIEW_TYPE, ""),

                new TypeBrowserItem("Microsoft-WebDAV-MiniRedir/5.1.2600", TypeBrowser.MS_WEBDAV_TYPE, "")

            };

        for (TypeBrowserItem item : items) {
            testTypeBrowser(item);
        }
    }
}



