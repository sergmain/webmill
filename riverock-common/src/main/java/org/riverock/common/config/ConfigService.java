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
package org.riverock.common.config;

import java.io.UnsupportedEncodingException;
import java.text.DateFormatSymbols;
import java.util.Locale;
import java.util.MissingResourceException;

import org.apache.log4j.Logger;

import org.riverock.common.tools.StringLocaleManager;

/**
 * User: serg_main
 * Date: 28.11.2003
 * Time: 19:54:54
 * @author Serge Maslyukov
 * $Id$
 */
public final class ConfigService {
    private final static Logger log = Logger.getLogger( ConfigService.class );

    private final static String localeLanguage[] = {"ru"};

    public static void initLocale() {
        for (String aLocaleLanguage : localeLanguage) {
            if (log.isDebugEnabled())
                log.debug("#15.001  load resource for lang " + aLocaleLanguage);

            try {
                Locale loc = new Locale(aLocaleLanguage, "");
                StringLocaleManager sm =
                    StringLocaleManager.getManager("org.riverock.common.locale", loc);

                String month[] = new String[12];
                String month_short[] = new String[12];

                for (int j = 0; j < 12; j++) {
                    try {
                        month[j] = sm.getStr("month." + (j + 1));
                    }
                    catch (UnsupportedEncodingException e) {
                        month[j] = "Error get key";
                    }
                    try {
                        month_short[j] = sm.getStr("month_short." + (j + 1));
                    }
                    catch (UnsupportedEncodingException e) {
                        month_short[j] = "Error get short key";
                    }
                }
                DateFormatSymbols dfs = new DateFormatSymbols(loc);
                dfs.setMonths(month);
                dfs.setShortMonths(month_short);
            }
            catch (MissingResourceException e) {
                final String es = "Error getting resource for language " + aLocaleLanguage;
                log.error(es, e);
                throw new ConfigException(es, e);
            }
        }
    }
}
