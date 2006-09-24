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

import java.util.Locale;

import org.riverock.common.tools.StringTools;
import org.apache.log4j.Logger;

/**
 * Author: mill
 * Date: Apr 2, 2003
 * Time: 4:23:10 PM
 *
 * $Id$
 */
public class AcceptLanguageWithLevel
{
    private static Logger log = Logger.getLogger( AcceptLanguageWithLevel.class );

    public Locale locale;
    public float level;

    public AcceptLanguageWithLevel(String locale_)
    {
        this.locale = StringTools.getLocale(locale_);
        this.level = 1;
    }

    public AcceptLanguageWithLevel(Locale locale_, float level_)
    {
        this.locale = locale_;
        this.level = level_;
    }

    public AcceptLanguageWithLevel(String locale_, float level_)
    {
        this.locale = StringTools.getLocale(locale_);
        this.level = level_;
    }

    public String toString() {
        if (locale!=null)
            return "[Locale "+locale.toString()+", level "+level+"]";
        else
            return "[Locale is null, level "+level+"]";
    }

//        "accept-language=ru,en;q=0.7,ja;q=0.3"
    /**
     *
     * @param acceptLanguage must be
     * ru
     * en;q=0.7
     * ja;q=0.3
     * @throws IllegalArgumentException
     */
    public static AcceptLanguageWithLevel getInstance(String acceptLanguage)
        throws IllegalArgumentException
    {
        Locale localeTemp;
        float levelTemp = 1;
        int idx = acceptLanguage.indexOf(';');
        if (idx==-1)
            localeTemp = StringTools.getLocale(acceptLanguage);
        else
        {
            localeTemp = StringTools.getLocale( acceptLanguage.substring(0, idx ));

            String levelString = acceptLanguage.substring(idx+1);
            if (levelString.startsWith("q="))
            {
                try
                {
                    levelTemp = Float.parseFloat(levelString.substring(2));
                }
                catch (NumberFormatException numberFormatException)
                {
                    String es = "Exception parsing string '"+levelString.substring(2)+"' ";
                    log.error( es, numberFormatException);
                    throw new IllegalArgumentException( es+ numberFormatException.toString());
                }
            }
        }
        return new AcceptLanguageWithLevel(localeTemp, levelTemp);
    }
}
