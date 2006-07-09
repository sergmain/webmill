/*
 * org.riverock.forum - Forum portlet
 *
 * Copyright (C) 2004, Riverock Software, All Rights Reserved.
 *
 * Riverock - The Open-source Java Development Community
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
package org.riverock.forum.util;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;

import org.riverock.common.tools.StringTools;

/**
 * String Util.
 */
public class ForumStringUtils {

    /**
     * Parse a string to boolean.
     *
     * @param param string to parse
     * @return boolean value, if param begin with(1,y,Y,t,T) return true,
     *         on exception return false.
     */
    public static boolean parseBoolean(String param) {
        if (StringUtils.isBlank(param))
            return false;
        switch (param.charAt(0)) {
            case '1':
            case 'y':
            case 'Y':
            case 't':
            case 'T':
                return true;
        }
        return false;
    }

    /**
     * Display a string in html page, call methods: escapeHTMLTags, convertURL,
     * convertNewlines.
     *
     * @param input string to display
     * @return string
     */
    public static String displayHtml(String input) {
        if (input==null) {
            return "";
        }
        String str = input;
        str = createBreaks(str, 80);
        str = StringEscapeUtils.escapeXml(str);
        str = convertURL(str);
        str = convertNewlines(str);
        return str;
    }

    private static String createBreaks(String input, int maxLength) {
        char chars[] = input.toCharArray();
        int len = chars.length;
        StringBuilder buf = new StringBuilder(len);
        int count = 0;
        int cur = 0;
        for (int i = 0; i < len; i++) {
            if (Character.isWhitespace(chars[i]))
                count = 0;
            if (count >= maxLength) {
                count = 0;
                buf.append(chars, cur, i - cur).append(" ");
                cur = i;
            }
            count++;
        }
        buf.append(chars, cur, len - cur);
        return buf.toString();
    }

    /**
     * Convert new lines, \n or \r\n to <BR />.
     *
     * @param input string to convert
     * @return string
     */
    private static String convertNewlines(String input) {
        input = StringTools.replaceStringArray(
            input,
            new String[][]{{"\r\n", "\n"}, {"\n", "<BR>"}}
        );
        return input;
    }

    /**
     * Convert URL .
     *
     * @param input string to convert
     * @return string
     */
    public static String convertURL(String input) {
        if (StringUtils.isBlank(input)) {
            return "";
        }
        StringBuilder buf = new StringBuilder(input.length() + 25);
        char chars[] = input.toCharArray();
        int len = input.length();
        int index = -1;
        int i = 0;
        int j = 0;
        int oldend = 0;
        while (++index < len) {
            char cur = chars[i = index];
            j = -1;
            if ((cur == 'f' && index < len - 6 && chars[++i] == 't' && chars[++i] == 'p' || cur == 'h' && (i = index) < len - 7 && chars[++i] == 't' && chars[++i] == 't' && chars[++i] == 'p' && (chars[++i] == 's' || chars[--i] == 'p')) && i < len - 4 && chars[++i] == ':' && chars[++i] == '/' && chars[++i] == '/')
                j = ++i;
            if (j > 0) {
                if (index == 0 || (cur = chars[index - 1]) != '\'' && cur != '"' && cur != '<' && cur != '=') {
                    cur = chars[j];
                    while (j < len) {
                        if (cur == ' ' || cur == '\t' || cur == '\'' || cur == '"' || cur == '<' || cur == '[' || cur == '\n' || cur == '\r' && j < len - 1 && chars[j + 1] == '\n')
                            break;
                        if (++j < len)
                            cur = chars[j];
                    }
                    cur = chars[j - 1];
                    if (cur == '.' || cur == ',' || cur == ')' || cur == ']')
                        j--;
                    buf.append(chars, oldend, index - oldend);
                    buf.append("<a href=\"");
                    buf.append(chars, index, j - index);
                    buf.append('"');
                    buf.append(" target=\"_blank\"");
                    buf.append('>');
                    buf.append(chars, index, j - index);
                    buf.append("</a>");
                }
                else {
                    buf.append(chars, oldend, j - oldend);
                }
                oldend = index = j;
            }
            else if (cur == '[' && index < len - 6 && chars[i = index + 1] == 'u' && chars[++i] == 'r' && chars[++i] == 'l' && (chars[++i] == '=' || chars[i] == ' ')) {
                j = ++i;
                int u2;
                int u1 = u2 = input.indexOf("]", j);
                if (u1 > 0)
                    u2 = input.indexOf("[/url]", u1 + 1);
                if (u2 < 0) {
                    buf.append(chars, oldend, j - oldend);
                    oldend = j;
                }
                else {
                    buf.append(chars, oldend, index - oldend);
                    buf.append("<a href =\"");
                    String href = input.substring(j, u1).trim();
                    if (href.indexOf("javascript:") == -1 && href.indexOf("file:") == -1)
                        buf.append(href);
                    buf.append("\" target=\"_blank");
                    buf.append("\">");
                    buf.append(input.substring(u1 + 1, u2).trim());
                    buf.append("</a>");
                    oldend = u2 + 6;
                }
                index = oldend;
            }
        }
        if (oldend < len)
            buf.append(chars, oldend, len - oldend);
        return buf.toString();
    }
}