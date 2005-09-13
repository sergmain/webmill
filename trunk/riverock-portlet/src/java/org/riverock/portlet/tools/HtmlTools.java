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
package org.riverock.portlet.tools;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import org.riverock.common.tools.RsetTools;

/**
 * $Id$
 */
public final class HtmlTools {

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
    public static String printYesNo( ResultSet rs, String f, boolean isEdit, ResourceBundle bundle )
        throws SQLException {
        return printYesNo( RsetTools.getInt( rs, f, 0 ), isEdit, bundle );
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
    public static String printYesNo( int val, boolean isEdit, ResourceBundle bundle ) {

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

            r += ("<OPTION value=\"1\"" + v_is_select_yes + ">" + bundle.getString("yesno.yes") + "</option>\n" +
                    "<OPTION value=\"0\"" + v_is_select_no + ">" + bundle.getString("yesno.no") + "</option>\n");
        }
        else {
            if (i_ == 1)
                r += bundle.getString("yesno.yes");
            else
                r += bundle.getString("yesno.no");
        }

        return r;
    }
}