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

 * Author: mill

 * Date: Apr 2, 2003

 * Time: 4:23:10 PM

 *

 * $Id$

 */



package org.riverock.common.html;



import java.util.Locale;



import org.riverock.common.tools.StringTools;

import org.apache.log4j.Logger;



public class AcceptLanguageWithLevel

{

    private static Logger log = Logger.getLogger( "org.riverock.common.html.AcceptLanguageWithLevel" );



    public Locale locale = null;

    public float level=1;



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

        Locale localeTemp = null;

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

