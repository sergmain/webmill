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

 *

 *  $Id$

 *

 */



package org.riverock.common.html;



public class TypeBrowser

{

    public static final int UNKNOWN = -1;

    public static final int IE = 1;

    public static final int NN = 2;

    public static final int OPERA = 3;

    public static final int HOT_JAVA = 4;

    public static final int LYNX = 5;

    public static final int JAVA_RUNTIME = 6;

    public static final int IBROWSE_TYPE = 7;

    public static final int MOZILLA_TYPE = 8;

    public static final int DELPHI_TYPE = 9;

    public static final int FRONTPAGE_TYPE = 10;

    public static final int MS_DATA_ACCESS_INTERNET_PUBLISHING_TYPE  = 11;

    public static final int HP_OPENVIEW_TYPE  = 12;

    public static final int MS_WEBDAV_TYPE = 13;





    public int type = UNKNOWN;

    public String version = "";



    private static final String Mozilla = "Mozilla/";

    private static final int Mozilla_len = Mozilla.length();



    private static final String Opera = "Opera";

    private static final int Opera_len = Opera.length();



    protected void finalize() throws Throwable

    {

        version = null;



        super.finalize();

    }



    // ua == User-Agent string in header

    public TypeBrowser(String ua)

    {

        if ((ua == null) || (ua.trim().length() == 0))

            return;



        int idx;



        if ((idx = ua.indexOf("Microsoft-WebDAV")) != -1)

        {

            type = MS_WEBDAV_TYPE;

            return;

        }



        if ((idx = ua.indexOf("OpenView")) != -1)

        {

            type = HP_OPENVIEW_TYPE;

            return;

        }



        if ((idx = ua.indexOf("Microsoft Data Access Internet Publishing")) != -1)

        {

/*

            version = ua.substring(idx + 7, ua.indexOf(" ", idx));



            // work around '/'

            if (version.charAt(0)=='/')

                version = version.substring(1);

*/

            type = MS_DATA_ACCESS_INTERNET_PUBLISHING_TYPE;

            return;

        }



        if ((idx = ua.indexOf("FrontPage")) != -1)

        {



            version = ua.substring(idx + 9).trim();



            // work around '/'

            if (version.charAt(0)=='/' || version.charAt(0)==' ')

                version = version.substring(1);



            int idxBrasket = version.indexOf(")");

            int idxSpace = version.indexOf(" ");

            if (idxBrasket==-1)

                idx = idxSpace;

            else if (idxSpace==-1)

                idx = idxBrasket;

            else

                idx = Math.min(idxBrasket, idxSpace);



            if (idx!=-1)

                version = version.substring(0, idx);



            type = FRONTPAGE_TYPE;

            return;

        }



        if ((idx = ua.indexOf("IBrowse")) != -1)

        {

            version = ua.substring(idx + 7, ua.indexOf(" ", idx));



            // work around '/'

            if (version.charAt(0)=='/')

                version = version.substring(1);



            type = IBROWSE_TYPE;

            return;

        }



        if ((idx = ua.indexOf("Indy Library")) != -1)

        {

            version = "";

            type = DELPHI_TYPE;

            return;

        }



        if (ua.indexOf("Opera") != -1)

        {

            idx = ua.indexOf(Opera) + Opera_len;

            if (idx == -1)

                return;



            version = ua.substring(idx).trim();

            if (version.charAt(0)=='/' || version.charAt(0)==' ')

                version = version.substring(1);



            if ((idx=version.indexOf(" "))!=-1)

                version = version.substring(0, idx);



            if ((idx=version.indexOf(";"))!=-1)

                version = version.substring(0, idx);



            type = OPERA;

            return;

        }



        if ((idx = ua.indexOf("MSIE")) != -1)

        {

            String ver = ua.substring(idx + 5, ua.indexOf(";", idx));

            version = ver;

            type = IE;

            return;

        }



        if ((idx = ua.indexOf("Sun")) != -1)

        {



            idx = ua.indexOf(Mozilla) + Mozilla_len;

            if (idx == -1)

                return;



            version = ua.substring(idx, ua.indexOf(" ", idx));

            type = HOT_JAVA;

            return;

        }



        if ((idx = ua.indexOf("Lynx")) != -1)

        {

            String ver = ua.substring(idx + 5, ua.indexOf(" ", idx));

            version = ver;

            type = LYNX;

            return;

        }



        if ((idx = ua.indexOf("Java")) != -1)

        {

            version = ua.substring(idx + 4).trim();



            // work around java 1.4 user-agent

            if (version.charAt(0)=='/')

                version = version.substring(1);



            type = JAVA_RUNTIME;

            return;

        }



        if ( (idx = ua.indexOf("Netscape")) == -1 && (idx = ua.indexOf("Gecko")) != -1)

        {

            type = TypeBrowser.MOZILLA_TYPE;

            // type browser is original Mozilla (Gecko)

            if ((idx = ua.indexOf("rv:")) != -1)

            {

                version = ua.substring(idx + 3, ua.indexOf(")", idx));

            }

            return;

        }



        idx = ua.indexOf(Mozilla) + Mozilla_len;

        if (idx == -1)

            return;



        version = ua.substring(idx, ua.indexOf(" ", idx));

        type = NN;

        // work around NN7+

        if ((idx = ua.indexOf("Netscape")) != -1)

        {

            version = ua.substring(idx + 8);

            if (version.charAt(0)=='/')

                version = version.substring(1);

            return;

        }



    }

}