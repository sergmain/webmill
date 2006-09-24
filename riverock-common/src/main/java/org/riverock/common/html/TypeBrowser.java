/*
 * org.riverock.common - Supporting classes and utilities
 *
 * Copyright (C) 2006, Riverock Software, All Rights Reserved.
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
package org.riverock.common.html;

import org.apache.commons.lang.StringUtils;

/**
 * $Id$
 */
public class TypeBrowser {
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
    public static final int MS_DATA_ACCESS_INTERNET_PUBLISHING_TYPE = 11;
    public static final int HP_OPENVIEW_TYPE = 12;
    public static final int MS_WEBDAV_TYPE = 13;
    public static final int FIREFOX_TYPE = 14;
    public static final int SAFARI_TYPE = 15;
    public static final int KONQUEROR_TYPE = 16;
    public static final int AOL_TYPE = 17;


    public int type = UNKNOWN;
    public String version = "";

    private static final String Mozilla = "Mozilla/";
    private static final int Mozilla_len = Mozilla.length();

    private static final String Opera = "Opera";
    private static final int Opera_len = Opera.length();

    private static final String Firefox = "Firefox";
    private static final int Firefox_len = Firefox.length();

    private static final String Safari = "Safari";
    private static final int Safari_len = Safari.length();

    private static final String Konqueror = "Konqueror";
    private static final int Konqueror_len = Konqueror.length();

    private static final String NETSCAPE = "Netscape";
    private static final int NETSCAPE_LEN = NETSCAPE.length();

    private static final String AOL = "AOL";
    private static final int AOL_LEN = AOL.length();

    private static final String JAVA = "Java";
    private static final int JAVA_LEN = JAVA.length();

    // ua == User-Agent string in header
    public TypeBrowser(String ua) {
        if (StringUtils.isBlank(ua))
            return;

        int idx;

        if ((idx = ua.indexOf("Microsoft-WebDAV")) != -1) {
            type = MS_WEBDAV_TYPE;
            return;
        }

        else if ((idx = ua.indexOf("OpenView")) != -1) {
            type = HP_OPENVIEW_TYPE;
            return;
        }

        else if ((idx = ua.indexOf("Microsoft Data Access Internet Publishing")) != -1) {
/*
            version = ua.substring(idx + 7, ua.indexOf(" ", idx));

            // work around '/'
            if (version.charAt(0)=='/')
                version = version.substring(1);
*/
            type = MS_DATA_ACCESS_INTERNET_PUBLISHING_TYPE;
            return;
        }

        else if ((idx = ua.indexOf("FrontPage")) != -1) {

            version = ua.substring(idx + 9).trim();

            // work around '/'
            if (version.charAt(0) == '/' || version.charAt(0) == ' ')
                version = version.substring(1);

            int idxBrasket = version.indexOf(")");
            int idxSpace = version.indexOf(" ");
            if (idxBrasket == -1)
                idx = idxSpace;
            else if (idxSpace == -1)
                idx = idxBrasket;
            else
                idx = Math.min(idxBrasket, idxSpace);

            if (idx != -1)
                version = version.substring(0, idx);

            type = FRONTPAGE_TYPE;
            return;
        }

        else if ((idx = ua.indexOf(Firefox)) != -1) {

            version = ua.substring(idx + Firefox_len).trim();

            // work around '/'
            if (version.charAt(0) == '/' || version.charAt(0) == ' ')
                version = version.substring(1);

            int idxBrasket = version.indexOf(")");
            int idxSpace = version.indexOf(" ");
            if (idxBrasket == -1)
                idx = idxSpace;
            else if (idxSpace == -1)
                idx = idxBrasket;
            else
                idx = Math.min(idxBrasket, idxSpace);

            if (idx != -1)
                version = version.substring(0, idx);

            type = FIREFOX_TYPE;
            return;
        }

        else if ((idx = ua.indexOf(Safari)) != -1) {

            version = ua.substring(idx + Safari_len).trim();

            // work around '/'
            if (version.charAt(0) == '/' || version.charAt(0) == ' ')
                version = version.substring(1);

            int idxBrasket = version.indexOf(")");
            int idxSpace = version.indexOf(" ");
            if (idxBrasket == -1)
                idx = idxSpace;
            else if (idxSpace == -1)
                idx = idxBrasket;
            else
                idx = Math.min(idxBrasket, idxSpace);

            if (idx != -1)
                version = version.substring(0, idx);

            type = SAFARI_TYPE;
            return;
        }

        else if ((idx = ua.indexOf("IBrowse")) != -1) {
            version = ua.substring(idx + 7, ua.indexOf(" ", idx));

            // work around '/'
            if (version.charAt(0) == '/')
                version = version.substring(1);

            type = IBROWSE_TYPE;
            return;
        }

        else if ((idx = ua.indexOf("Indy Library")) != -1) {
            version = "";
            type = DELPHI_TYPE;
            return;
        }

        else if (ua.indexOf(Konqueror) != -1) {
            idx = ua.indexOf(Konqueror) + Konqueror_len;
            if (idx == -1)
                return;

            version = ua.substring(idx).trim();
            if (version.charAt(0) == '/' || version.charAt(0) == ' ')
                version = version.substring(1);

            if ((idx = version.indexOf(" ")) != -1)
                version = version.substring(0, idx);

            if ((idx = version.indexOf(";")) != -1)
                version = version.substring(0, idx);

            type = KONQUEROR_TYPE;
            return;
        }

        else if (ua.indexOf(AOL) != -1) {
            idx = ua.indexOf(AOL) + AOL_LEN;
            if (idx == -1)
                return;

            version = ua.substring(idx).trim();
            if (version.charAt(0) == '/' || version.charAt(0) == ' ')
                version = version.substring(1);

            if ((idx = version.indexOf(" ")) != -1)
                version = version.substring(0, idx);

            if ((idx = version.indexOf(";")) != -1)
                version = version.substring(0, idx);

            type = AOL_TYPE;
            return;
        }

        else if (ua.indexOf(Opera) != -1) {
            idx = ua.indexOf(Opera) + Opera_len;
            if (idx == -1)
                return;

            version = ua.substring(idx).trim();
            if (version.charAt(0) == '/' || version.charAt(0) == ' ')
                version = version.substring(1);

            if ((idx = version.indexOf(" ")) != -1)
                version = version.substring(0, idx);

            if ((idx = version.indexOf(";")) != -1)
                version = version.substring(0, idx);

            type = OPERA;
            return;
        }

        else if ((idx = ua.indexOf(JAVA)) != -1) {
            version = ua.substring(idx + JAVA_LEN).trim();

            // work around java 1.4 user-agent
            if (version.charAt(0) == '/')
                version = version.substring(1);

            type = JAVA_RUNTIME;
            return;
        }
        else if ((idx = ua.indexOf("MSIE")) != -1) {
            int index = ua.indexOf(";", idx);
            if (index!=-1) {
                String ver = ua.substring(idx + 5, index);
                version = ver;
                type = IE;
                return;
            }
        }

        
        if ((idx = ua.indexOf("Sun")) != -1) {

            idx = ua.indexOf(Mozilla) + Mozilla_len;
            if (idx == -1)
                return;

            version = ua.substring(idx, ua.indexOf(" ", idx));
            type = HOT_JAVA;
            return;
        }

        else if ((idx = ua.indexOf("Lynx")) != -1) {
            String ver = ua.substring(idx + 5, ua.indexOf(" ", idx));
            version = ver;
            type = LYNX;
            return;
        }

        else if ((idx = ua.indexOf(NETSCAPE)) == -1 && (idx = ua.indexOf("Gecko")) != -1) {
            type = TypeBrowser.MOZILLA_TYPE;
            // type browser is original Mozilla (Gecko)
            if ((idx = ua.indexOf("rv:")) != -1) {
                version = ua.substring(idx + 3, ua.indexOf(")", idx));
            }
            return;
        }

        idx = ua.indexOf(Mozilla);
        if (idx != -1) {
            type = TypeBrowser.MOZILLA_TYPE;
            idx +=  Mozilla_len;
            int index1 = ua.indexOf(" ", idx);
            if (index1!=-1) {
                version = ua.substring(idx, index1);
                type = NN;
                // work around NN7+
                if ((idx = ua.indexOf(NETSCAPE)) != -1) {
                    version = ua.substring(idx + NETSCAPE_LEN);
                    if (version.charAt(0) == '/')
                        version = version.substring(1);
                    return;
                }
            }
            else {
                version = ua.substring(idx).trim();
                if (version.charAt(0) == '/' || version.charAt(0) == ' ')
                    version = version.substring(1);

                if ((idx = version.indexOf(" ")) != -1)
                    version = version.substring(0, idx);

                if ((idx = version.indexOf(";")) != -1)
                    version = version.substring(0, idx);
            }
        }


    }
}