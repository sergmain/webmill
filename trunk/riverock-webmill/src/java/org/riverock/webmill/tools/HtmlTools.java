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

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Locale;

import org.riverock.generic.tools.StringManager;
import org.riverock.generic.exception.GenericException;
import org.riverock.common.tools.RsetTools;

/**
 * $Id$
 */
public final class HtmlTools {

    public static String printYesNo(ResultSet rs, String f, boolean isEdit)
        throws SQLException, GenericException {
        return printYesNo(rs, f, isEdit, Locale.ENGLISH);
    }

    /**
     * Печатает в контексте JSP значение поля, хранящего логическое значение в формате
     * 1- true, 0-false
     * Если isEdit==true, то печатается в формате:<br>
     * <PRE>
     * <PRE>
     * <OPTION SELECTED value=1>ДА
     * <OPTION value=0>НЕТ
     * </PRE>
     * </PRE>
     * Если isEdit==false, то печатается для 1 - "ДА", для 0-"НЕТ"
     *
     * Параметры:
     * <blockquote>
     * ResultSet rs - ResultSet с текущей записью выборки
     * String f - имя поля
     * boolean isEdit - используется в редактируемой форме или только для просмотра
     * </blockquote>
     */
    public static String printYesNo(ResultSet rs, String f, boolean isEdit, Locale loc)
        throws SQLException, GenericException {
        return printYesNo( RsetTools.getInt(rs, f, new Integer(0)).intValue(), isEdit, loc);
    }

    /**
     * Печатает в контексте JSP значение поля, хранящего логическое значение в формате
     * 1- true, 0-false
     * Если isEdit==true, то печатается в формате:<br>
     * <PRE>
     * <PRE>
     * <OPTION SELECTED value=1>ДА
     * <OPTION value=0>НЕТ
     * </PRE>
     * </PRE>
     * Если isEdit==false, то печатается для 1 - "ДА", для 0-"НЕТ"
     *
     * Параметры:
     * <blockquote>
     * int val - значение для вывода
     * boolean isEdit - используется в редактируемой форме или только для просмотра
     * </blockquote>
     */
    public static String printYesNo(int val, boolean isEdit, Locale loc) throws GenericException {
        StringManager sm = StringManager.getManager("mill.locale.main", loc);

        int i_ = val;

        String v_is_select_yes;
        String v_is_select_no;
        String r = "";

        if (isEdit) {
            if (i_ == 1) {
                v_is_select_yes = " selected";
                v_is_select_no = "";
            }
            else {
                v_is_select_yes = "";
                v_is_select_no = " selected";
            }

            r += ("<OPTION value=\"1\"" + v_is_select_yes + ">" + sm.getStr("yesno.yes") + "</option>\n" +
                    "<OPTION value=\"0\"" + v_is_select_no + ">" + sm.getStr("yesno.no") + "</option>\n");
        }
        else {
            if (i_ == 1)
                r += sm.getStr("yesno.yes");
            else
                r += sm.getStr("yesno.no");
        }

        return r;
    }
}