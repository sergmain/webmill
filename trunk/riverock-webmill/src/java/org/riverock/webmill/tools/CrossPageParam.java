/*

 * org.riverock.webmill -- Portal framework implementation

 * 

 * Copyright (C) 2004, Riverock Software, All Rights Reserved.

 * 

 * Riverock -- The Open-source Java Development Community

 * http://www.riverock.org

 * 

 * 

 * This program is free software; you can redistribute it and/or

 * modify it under the terms of the GNU General Public

 * License as published by the Free Software Foundation; either

 * version 2 of the License, or (at your option) any later version.

 *

 * This library is distributed in the hope that it will be useful,

 * but WITHOUT ANY WARRANTY; without even the implied warranty of

 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU

 * General Public License for more details.

 *

 * You should have received a copy of the GNU General Public

 * License along with this library; if not, write to the Free Software

 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA

 *

 */



package org.riverock.webmill.tools;



import java.util.ArrayList;

import java.util.Calendar;

import java.util.List;

import java.util.Locale;



import javax.servlet.http.HttpServletRequest;



import org.riverock.common.config.ConfigException;

import org.riverock.common.tools.ServletTools;

import org.riverock.webmill.main.Constants;

import org.riverock.webmill.schema.types.HiddenParamType;

import org.riverock.webmill.utils.ServletUtils;



/**

 *

 * $Revision$

 * $Date$

 * $RCSfile$

 *

 */

public class CrossPageParam implements CrossPageParamInterface

{

    private Locale loc = null;

    private Integer year = null;

    private Integer month = null;

    private boolean isLocaleDefault = false;



    protected void finalize() throws Throwable

    {

        setLoc(null);

        setYear(null);

        setMonth(null);



        super.finalize();

    }



    private final String listParam[] =

            {Constants.NAME_LANG_PARAM,

             Constants.NAME_YEAR_PARAM,

             Constants.NAME_MONTH_PARAM

            };





    public CrossPageParam(HttpServletRequest request,

                          String param,

                          Locale loc_,

                          Integer year_,

                          Integer month_)

        throws ConfigException

    {

        if (param.indexOf(Constants.NAME_LANG_PARAM) != -1)

        {

            String s = ServletUtils.getString(request, Constants.NAME_LANG_PARAM, null);

            if (s == null)

            {

                setLoc((loc_ == null)?Locale.ENGLISH:loc_);

                setIsLocaleDefault(true);

            }

            else

            {

                setIsLocaleDefault(false);

                int pos = s.indexOf('_');

                String lang = null, country = "", variant = "";

                if (pos == -1)

                    lang = s;

                else

                {

                    lang = s.substring(0, pos);

                    s = s.substring(pos + 1);

                    pos = s.indexOf('_');

                    if (pos == -1)

                        country = s;

                    else

                    {

                        country = s.substring(0, pos);

                        variant = s.substring(pos + 1);

                    }



                }

                setLoc(new Locale(lang, country, variant));

            }

        }

        if (param.indexOf(Constants.NAME_YEAR_PARAM) != -1)

            setYear(ServletTools.getInt(request, Constants.NAME_YEAR_PARAM, (year_ == null)?new Integer(Calendar.getInstance().get(Calendar.YEAR)):year_));



        if (param.indexOf(Constants.NAME_MONTH_PARAM) != -1)

            setMonth(ServletTools.getInt(request, Constants.NAME_MONTH_PARAM, (month_ == null)?new Integer(Calendar.getInstance().get(Calendar.MONTH) + 1):month_));



    }



    /**

     * public method for using only from InitPage class

     * @return  Locale

     */

    public Locale getLocaleInternal()

    {

        return getLoc();

    }



    private static HiddenParamType getHidden(String name, String value)

    {

        HiddenParamType hidden = new HiddenParamType();

        hidden.setHiddenParamName(name);

        hidden.setHiddenParamValue(value);

        return hidden;

    }



    public List getAsList()

    {

        List v = new ArrayList(listParam.length);



        if (loc != null)

            v.add( getHidden( Constants.NAME_LANG_PARAM, loc.toString()));



        if (year != null)

            v.add( getHidden(Constants.NAME_YEAR_PARAM, "" + year.intValue()));



        if (month != null)

            v.add( getHidden(Constants.NAME_MONTH_PARAM, "" + month.intValue()));



        return v;

    }



    public String getAsURL()

    {

        return

                (getLoc() != null

                ?(Constants.NAME_LANG_PARAM + "=" + getLoc().toString() + "&")

                :""

                ) +

                (getYear() != null

                ?(Constants.NAME_YEAR_PARAM + "=" + getYear().intValue() + "&")

                :""

                ) +

                (getMonth() != null

                ?(Constants.NAME_MONTH_PARAM + "=" + getMonth().intValue() + "&")

                :""

                );



    }



    public String getAsForm()

    {

        String r = "";

        boolean flag = false;

        ;

        if (getLoc() != null)

        {

            r += ("<input type=\"hidden\" name=\"" + Constants.NAME_LANG_PARAM + "\" value=\"" + getLoc().toString() + "\">");

            flag = true;

        }

        if (getYear() != null)

        {

            if (flag) r += "\n";

            r += ("<input type=\"hidden\" name=\"" + Constants.NAME_YEAR_PARAM + "\" value=\"" + getYear().intValue() + "\">");

            flag = true;

        }



        if (getMonth() != null)

        {

            if (flag) r += "\n";

            r += ("<input type=\"hidden\" name=\"" + Constants.NAME_MONTH_PARAM + "\" value=\"" + getMonth().intValue() + "\">");

            flag = true;

        }



        return r;

    }



    public String getAsUrlXML()

    {

        return



                (getLoc() != null

                ?(Constants.NAME_LANG_PARAM + "=" + getLoc().toString() + "&amp;")

                :""

                ) +

                (getYear() != null

                ?(Constants.NAME_YEAR_PARAM + "=" + getYear().intValue() + "&amp;")

                :""

                ) +

                (getMonth() != null

                ?(Constants.NAME_MONTH_PARAM + "=" + getMonth().intValue() + "&amp;")

                :""

                );

    }



    public String getAsFormXML()

    {

        return



                (getLoc() != null

                ?("<HiddenParam><HiddenParamName>" + Constants.NAME_LANG_PARAM + "</HiddenParamName><HiddenParamValue>" + getLoc().toString() + "</HiddenParamValue></HiddenParam>")

                :""

                ) +

                (getYear() != null

                ?("<HiddenParam><HiddenParamName>" + Constants.NAME_YEAR_PARAM + "</HiddenParamName><HiddenParamValue>" + getYear().intValue() + "</HiddenParamValue></HiddenParam>")

                :""

                ) +

                (getMonth() != null

                ?("<HiddenParam><HiddenParamName>" + Constants.NAME_MONTH_PARAM + "</HiddenParamName><HiddenParamValue>" + getMonth().intValue() + "</HiddenParamValue></HiddenParam>")

                :""

                );



    }



    public Locale getLoc()

    {

        return loc;

    }



    public void setLoc(Locale loc)

    {

        this.loc = loc;

    }



    public Integer getYear()

    {

        return year;

    }



    public void setYear(Integer year)

    {

        this.year = year;

    }



    public Integer getMonth()

    {

        return month;

    }



    public void setMonth(Integer month)

    {

        this.month = month;

    }



    public boolean getIsLocaleDefault()

    {

        return isLocaleDefault;

    }



    public void setIsLocaleDefault(boolean localeDefault)

    {

        isLocaleDefault = localeDefault;

    }



    public String[] getListParam()

    {

        return listParam;

    }



}

