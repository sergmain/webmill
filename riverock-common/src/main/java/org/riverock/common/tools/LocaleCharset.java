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
package org.riverock.common.tools;

import java.util.Locale;
import java.util.HashMap;
import java.util.Map;

/**
 * A mapping to determine the (somewhat arbitrarily) preferred charset for
 * a given locale.  Supports all locales recognized in JDK 1.1.
 * This class was originally written by Jason Hunter [jhunter@acm.org]
 * as part of the book "Java Servlet Programming" (O'Reilly).
 * See <a href="http://www.servlets.com/book">
 * http://www.servlets.com/book</a> for more information.
 * Used by Sun Microsystems with permission.
 */
public class LocaleCharset
{

    private static Map<String, String> map = null;

    static
    {
        map = new HashMap<String, String>();

        map.put("ar", "ISO-8859-6");
        map.put("be", "ISO-8859-5");
        map.put("bg", "ISO-8859-5");
        map.put("ca", "ISO-8859-1");
        map.put("cs", "ISO-8859-2");
        map.put("da", "ISO-8859-1");
        map.put("de", "ISO-8859-1");
        map.put("el", "ISO-8859-7");
        map.put("en", "ISO-8859-1");
        map.put("es", "ISO-8859-1");
        map.put("et", "ISO-8859-1");
        map.put("fi", "ISO-8859-1");
        map.put("fr", "ISO-8859-1");
        map.put("hr", "ISO-8859-2");
        map.put("hu", "ISO-8859-2");
        map.put("is", "ISO-8859-1");
        map.put("it", "ISO-8859-1");
        map.put("iw", "ISO-8859-8");
        map.put("ja", "Shift_JIS");
        map.put("ko", "EUC-KR");     // Requires JDK 1.1.6
        map.put("lt", "ISO-8859-2");
        map.put("lv", "ISO-8859-2");
        map.put("mk", "ISO-8859-5");
        map.put("nl", "ISO-8859-1");
        map.put("no", "ISO-8859-1");
        map.put("pl", "ISO-8859-2");
        map.put("pt", "ISO-8859-1");
        map.put("ro", "ISO-8859-2");

        map.put("ru", "Cp1251");

        map.put("sh", "ISO-8859-5");
        map.put("sk", "ISO-8859-2");
        map.put("sl", "ISO-8859-2");
        map.put("sq", "ISO-8859-2");
        map.put("sr", "ISO-8859-5");
        map.put("sv", "ISO-8859-1");
        map.put("tr", "ISO-8859-9");
        map.put("uk", "ISO-8859-5");
        map.put("zh", "GB2312");
        map.put("zh_TW", "Big5");

    }

    /**
     * Gets the preferred charset for the given locale, or null if the locale
     * is not recognized.
     *
     * @param loc the locale
     * @return the preferred charset
     */

    public static String getCharset(Locale loc)
    {
        String charset;

        // Try for an full name match (may include country)
        charset = map.get(loc.toString());
        if (charset != null)
            return charset;

        // If a full name didn't match, try just the language
        charset = map.get(loc.getLanguage());
        if (charset != null)
            return charset;

        return "ISO-8859-1";
    }
}
