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

 * User: serg_main

 * Date: 28.11.2003

 * Time: 19:54:54

 * @author Serge Maslyukov

 * $Id$

 */



package org.riverock.common.config;



import java.util.Locale;

import java.io.UnsupportedEncodingException;

import java.text.DateFormatSymbols;



import org.riverock.common.tools.StringLocaleManager;



import org.apache.log4j.Logger;



public class ConfigService

{

    private static Logger log = Logger.getLogger("org.riverock.generic.config.ConfigService");



    public static void initLocale()

    throws ConfigException

    {

        String[] localeLanguage = PropertiesProvider.getLocaleLanguage();

        for (int i = 0; i < localeLanguage.length; i++)

        {

            if (log.isDebugEnabled())

                log.debug("#15.001  load resource for lang " + localeLanguage[i]);



            try

            {

                Locale loc = new Locale(localeLanguage[i], "");

                StringLocaleManager sm =

                    StringLocaleManager.getManager("org.riverock.common.locale", loc);



                String month[] = new String[12];

                String month_short[] = new String[12];



                for (int j = 0; j < 12; j++)

                {

                    try

                    {

                        month[j] = sm.getStr("month." + (j + 1));

                    }

                    catch (UnsupportedEncodingException e)

                    {

                        month[j] = "Error get key";

                    }

                    try

                    {

                        month_short[j] = sm.getStr("month_short." + (j + 1));

                    }

                    catch (UnsupportedEncodingException e)

                    {

                        month_short[j] = "Error get short key";

                    }

                }

                DateFormatSymbols dfs = new DateFormatSymbols(loc);

                dfs.setMonths(month);

                dfs.setShortMonths(month_short);

            }

            catch (java.util.MissingResourceException e)

            {

                log.error("Error getting resource for language " + localeLanguage[i], e);

                throw new ConfigException( e.toString());

            }

        }

    }

}

