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



/**

 * $Id$

 */

package org.riverock.webmill.tools;



import java.sql.ResultSet;

import java.sql.SQLException;

import java.util.Locale;



import org.riverock.generic.tools.StringManager;



public class HtmlTools

{

//    private static Logger cat = Logger.getLogger("org.riverock.common.tools.RsetTools" );



    /**

     * �������� � ��������� JSP �������� ����, ��������� ���������� �������� � �������

     * 1- true, 0-false

     * � HTML ����������:<br>

     * <PRE>

     * <PRE>

     * <OPTION SELECTED value=1>��

     * <OPTION value=0>���

     * </PRE>

     * </PRE>

     * ���������:

     * <blockquote>

     * ResultSet rs - ResultSet � ������� ������� �������

     * String f - ��� ����

     * </blockquote>

     */

    public static String printYesNo(ResultSet rs, String f)

            throws java.io.UnsupportedEncodingException, SQLException

    {

        return printYesNo(rs, f, true);

    }



    public static String printYesNo(ResultSet rs, String f,

                                    boolean isEdit)

            throws java.io.UnsupportedEncodingException, SQLException

    {

        return printYesNo(rs, f, isEdit, Locale.ENGLISH);

    }



    /**

     * �������� � ��������� JSP �������� ����, ��������� ���������� �������� � �������

     * 1- true, 0-false

     * ���� isEdit==true, �� ���������� � �������:<br>

     * <PRE>

     * <PRE>

     * <OPTION SELECTED value=1>��

     * <OPTION value=0>���

     * </PRE>

     * </PRE>

     * ���� isEdit==false, �� ���������� ��� 1 - "��", ��� 0-"���"

     *

     * ���������:

     * <blockquote>

     * ResultSet rs - ResultSet � ������� ������� �������

     * String f - ��� ����

     * boolean isEdit - ������������ � ������������� ����� ��� ������ ��� ���������

     * </blockquote>

     */

    public static String printYesNo(ResultSet rs, String f,

                                    boolean isEdit, Locale loc)

            throws java.io.UnsupportedEncodingException, SQLException

    {

//        int i_ = 0;

//        if (rs != null)

//            i_ = getInt(rs, f, 0);



//        return printYesNo( i_, isEdit, loc);

        return printYesNo( org.riverock.common.tools.RsetTools.getInt(rs, f, new Integer(0)), isEdit, loc);

    }



    /**

     * �������� � ��������� JSP �������� ����, ��������� ���������� �������� � �������

     * 1- true, 0-false

     * ���� isEdit==true, �� ���������� � �������:<br>

     * <PRE>

     * <PRE>

     * <OPTION SELECTED value=1>��

     * <OPTION value=0>���

     * </PRE>

     * </PRE>

     * ���� isEdit==false, �� ���������� ��� 1 - "��", ��� 0-"���"

     *

     * ���������:

     * <blockquote>

     * int val - �������� ��� ������

     * boolean isEdit - ������������ � ������������� ����� ��� ������ ��� ���������

     * </blockquote>

     */

    public static String printYesNo(Integer val, boolean isEdit, Locale loc)

            throws java.io.UnsupportedEncodingException

    {

        StringManager sm = StringManager.getManager("mill.locale.main", loc);



//        int i_ = val;



        String v_is_select_yes;

        String v_is_select_no;

        String r = "";



        if (isEdit)

        {

            if (val.intValue() == 1)

            {

                v_is_select_yes = " selected";

                v_is_select_no = "";

            }

            else

            {

                v_is_select_yes = "";

                v_is_select_no = " selected";

            }



            r += ("<OPTION value=\"1\"" + v_is_select_yes + ">" + sm.getStr("yesno.yes") + "</option>\n" +

                    "<OPTION value=\"0\"" + v_is_select_no + ">" + sm.getStr("yesno.no") + "</option>\n");

        }

        else

        {

            if (val.intValue() == 1)

                r += sm.getStr("yesno.yes");

            else

                r += sm.getStr("yesno.no");

        }



        return r;



    }



}