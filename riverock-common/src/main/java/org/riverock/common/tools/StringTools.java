/*
 * org.riverock.common - Supporting classes and utilities
 *
 * Copyright (C) 2006, Riverock Software, All Rights Reserved.
 *
 * Riverock -- The Open-source Java Development Community
 * http://www.riverock.org
 *
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as StringBuilderpublished by the Free Software Foundation; either
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
 */
package org.riverock.common.tools;

import java.util.List;
import java.util.Locale;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * Public tools for work with strings.
 *
 * $Id$
 */
public class StringTools {
    private final static Logger log = Logger.getLogger(StringTools.class);

    /**
     * Converts a string array to a string.
     * Autor: Apache group, License: Apache2
     * @param values  the string array to convert.
     * @return a string representing the values in the string array.
     */
    public static String arrayToString(String[] values) {
		StringBuilder buffer = new StringBuilder();
		if (values == null) {
			buffer.append("null");
		} else {
			buffer.append("{");
			for (int i = 0; i < values.length; i++) {
				buffer.append(values[i]);
				if (i < values.length - 1) {
					buffer.append(",");
				}
			}
			buffer.append("}");
		}
    	return buffer.toString();
    }

    public static String getUserName(String firstName, String middleName, String lastName) {
        if (log.isDebugEnabled()) {
            log.debug("firstName: " + firstName+ ", middleName: " +middleName+ ", lastName: " + lastName );
        }

        StringBuilder s = new StringBuilder();
        if (StringUtils.isNotBlank(firstName)) {
            s.append(firstName);

            if (StringUtils.isNotBlank(middleName) || StringUtils.isNotBlank(lastName)) {
                s.append(" ");
            }
        }
        if (StringUtils.isNotBlank(middleName)) {
            s.append(middleName);
            if (StringUtils.isNotBlank(lastName)) {
                s.append(" ");
            }
        }
        if (StringUtils.isNotBlank(lastName)) {
            s.append(lastName);
        }

        String result = s.toString();
        if (StringUtils.isBlank(result)) {
            return null;
        }
        else {
            return result;
        }
    }

    /**
     * @deprecated use org.apache.commons.lang.StringUtils.isEmpty()
     * @param s String
     * @return boolean
     */
    public static boolean isEmpty( final String s) {
        return s == null || s.trim().length() == 0;
    }

    /**
     * преобразует строку вида 'SITE_LIST_SITE' -> 'SiteListSite'
     * @param f String
     * @return результирующая строка
     */
    public static String capitalizeString( final String f) {
        if (f.indexOf('_')==-1) {
            if (f.length()==1)
                return f.toUpperCase();

            return StringTools.capitalizeFirstChar(f);
        }

        String s_ = f;

        StringBuilder r = new StringBuilder();
        int pos;
        while ((pos = s_.indexOf('_')) != -1) {
            if (pos != s_.length()) {
                r.append(StringTools.capitalizeFirstChar(s_.substring(0, pos)));
                s_ = s_.substring(pos + 1, s_.length());
            }
        }
        return r.append(StringTools.capitalizeFirstChar(s_)).toString();
    }


    public static String capitalizeFirstChar( final String s ) {
        if (s==null)
            return null;

        if (s.length()==0)
            return "";

        return s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
    }

    public static Locale normalizeLocale( Locale locale ) {
        if (locale==null) {
            return null;
        }

        return new Locale(
            locale.getLanguage()!=null?locale.getLanguage().toLowerCase():"",
            locale.getCountry()!=null?locale.getCountry().toLowerCase():"",
            locale.getVariant()!=null?locale.getVariant().toLowerCase():""
        );
    }

    /**
     * Build Locale from string.
     * @param locale with locale
     * @return java.util.Locale
     */
    public static Locale getLocale( final String locale ) {
        if (locale==null) {
            return null;
        }

        String string = locale.replace( '-', '_' );

        int idx = string.indexOf('_');
        if (idx == -1)
            return new Locale(string.toLowerCase(), "");

        String lang = string.substring(0, idx).toLowerCase();
        int idx1 = string.indexOf('_', idx+1);
        if (idx1==-1)
            return new Locale(lang, string.substring(idx+1).toLowerCase() );

        return new Locale(
            lang, string.substring(idx+1, idx1).toLowerCase( ), string.substring(idx1+1).toLowerCase() );
    }

    public static String getMultypleString( final String str, final int multyple)
    {
        if (str==null || multyple==0)
            return "";

        StringBuilder s = new StringBuilder();
        for (int i=0; i<multyple; i++)
            s.append( str );

        return s.toString();
    }

    /**
     * @deprecated use decodeXml
     * @param s String
     * @return String
     */
    public static String toOrigin( final String s) {
        return StringEscapeUtils.unescapeXml( s );
    }


    /**
     * @deprecated use StringEscapeUtils.escapeXml( s )
     * @param s String
     * @return String
     */
    public static String encodeXml( final String s ) {
        return StringEscapeUtils.escapeXml( s );
    }

    /**
     * @deprecated use StringEscapeUtils.unescapeXml( s )
     * @param s String
     * @return String
     */
    public static String decodeXml( final String s ) {
        return StringEscapeUtils.unescapeXml( s );
    }

    /**
     * @deprecated use encodeXml
     * @param s String
     * @return String
     */
    public static String prepareEditForm( final String s) {
        return StringEscapeUtils.escapeXml( s );
    }

    public static String prepareToParsingSimple( final String s) {
        if (s==null)
            return null;

        return "<para>" + StringTools.replaceStringArray(
                s,
                new String[][]
                {
                    {"\n", "</para>\n<para>"},
                }
        ) + "</para>";
    }

    public static String prepareToParsing( final String s) {
        if (s==null)
            return null;

        return "<para>" + StringTools.replaceStringArray(
                s,
                new String[][]
                {{"&", "&amp;"},
                 {"<", "&lt;"},
                 {">", "&gt;"},
                 {"\t", "&#160;&#160;&#160;&#160;"},
                 {"\n", "</para>\n<para>"},
                 {" ", " &#160;"}
                }
        ) + "</para>";
    }

    public static String toPlainHTML( final String s) {
        if (s==null)
            return null;

        return StringTools.replaceStringArray(
                s,
                new String[][]
                {{"&", "&amp;"},
                 {"<", "&lt;"},
                 {">", "&gt;"},
                 {"\t", "&#160;&#160;&#160;&#160;"},
                 {"\n", "<br>\n"},
                 {" ", " &#160;"}
                }
        );
    }

    /**
     * Проверяет является объект типа String gecnsv (null) или строка пустая и если верно, то возвращает
     * строку по умолчанию
     * @param s - String. Строка для проверки
     * @param def - String. Строка, возвращаемая, если проверочная строка == null или пустая
     * @return - String. Новая строка
     */
    public static String replaceEmpty( final String s, final String def) {
        if (StringUtils.isEmpty(s))
            return def;
        else
            return s;
    }

    /**
     * Форматирует входной массив байт в столбцы по 76 символов. Применяется для base64 кодирования
     * @param bytes - byte[]. Входной массив байт
     * @return - byte[]. Выходной массив байт, отформатированный в столбцы по 76 байт.
     */
    public static byte[] formatArray( final byte bytes[]) {
        int newLength = bytes.length + bytes.length / 57;

        if (log.isDebugEnabled())
            log.debug("Old length: " + bytes.length + "\nNew length: " + newLength);

        byte bf[] = new byte[newLength];

        int charCount = 0;
        for (int i = 0; i < bytes.length; i++)
        {

//log.debug("charCount: "+charCount+" i: "+i);

            bf[charCount++] = bytes[i];

            // Add newline every 76 output chars (that's 57 input chars)
            if ((i != 0) && (i % 57 == 0))
            {
                if (log.isDebugEnabled())
                    log.debug(" " + i + " " + charCount);

                bf[charCount++] = (byte) '\n';
            }

        }
        return bf;
    }

    public static byte[] getBytesUTF( final String s) {
        if (s==null)
            return new byte[0];

        try {
            return s.getBytes("utf-8");
        }
        catch (java.io.UnsupportedEncodingException e) {
            log.warn("String.getBytes(\"utf-8\") not supported");
            return new byte[0];
        }
    }

    public static int getStartUTF( final String s, final int maxByte) {
        return getStartUTF(getBytesUTF(s), maxByte);
    }

    public static int getStartUTF( final byte[] b, final int maxByte) {
        return getStartUTF(b, maxByte, 0);
    }

    public static int getStartUTF( final byte[] b, final int maxByte, final int offset) {
        if (b.length <= offset)
            return -1;

        if (b.length < maxByte)
            return b.length;

        int idx = Math.min(b.length, maxByte + offset);

        for (int i = idx - 1; i > offset; i--)
        {
            int j = (b[i] < 0?0x100 + b[i]:b[i]);
            if (j < 0x80)
            {
                return i + 1;
            }
        }
        return -1;
    }

    public static int lengthUTF(String s) {
        return getBytesUTF(s).length;
    }

    /**
     * @deprecated use  org.apache.commons.lang.StringUtils.replace( str_, search_, ins);
     * Replace substring.
     * @param str_ - String. Source string
     * @param search_ - String which will be searching
     * @param ins - String for replace.
     *
     * @return - String. resulting string.
     */
    public static String replaceString( final String str_, final String search_, final String ins) {
        return StringUtils.replace( str_, search_, ins);
    }

    /**
     * Заменяет один фрагмент строки другим во всей строке. Строки для поиска и замены передаются
     * в виде 2-ух мерного массива.
     *
     * @param str_ - String. Строка в которой производится замена
     * @param repl - String[][]. Массив строк для поиска и замены.
     * repl[][0] - строка для поиска
     * repl[][1] - строка для замены
     * @return - String. Новая строка
     */
    public static String replaceStringArray( final String str_, final String repl[][]) {
        String qqq = str_;
        for (final String[] newVar : repl) {
            qqq = StringUtils.replace(qqq, newVar[0], newVar[1]);
        }
        return qqq;

    }

    /**
     * Преобразует строку из одной кодировки в другую
     * @param s - String. Строка для преобразования
     * @param fromCharset - String. Исходная кодировка строки
     * @param toCharset - String. Результирующая кодировка строки
     * @return - String.
     * @throws java.io.UnsupportedEncodingException
     */
    public static String convertString( final String s, final String fromCharset, final String toCharset)
            throws java.io.UnsupportedEncodingException {
        if (s == null)
            return null;

        return new String(s.getBytes(fromCharset), toCharset);
    }

    /**
     * Дополняет строку символами cправа или слева. В зависимости от флага isLeft дополнение
     * делается слева (true) или справа (false)
     * @param s -  String. Строка для дополнения
     * @param ch -  char. Символ, используемый для дополнения.
     * @param countAddChar -  количество символов для добавления.
     * @param isLeft - boolean. Дополнение слева или справа
     * @return  -  String.
     */
    public static String addString( final String s, final char ch, final int countAddChar, final boolean isLeft) {
        if (s == null)
            return null;

        if (countAddChar==0)
            return s;

        StringBuilder temp = new StringBuilder();
        for (int i=0; i<countAddChar; i++)
            temp.append(ch);

        if (isLeft)
            temp.append(s);
        else
            temp.insert(0, s);

        return temp.toString();
    }


    /**
     * Дополняет строку символами до указанного количества. В зависимости от флага isLeft дополнение
     * делается слева (true) или справа (false)
     * @param s -  String. Строка для дополнения
     * @param ch -  char. Символ, используемый для дополнения.
     * @param countCharInString -  String. результирующая длина строки.
     * @param isLeft - boolean. Дополнение слева или справа
     * @return  -  String.
     */
    public static String appendString( final String s, final char ch, final int countCharInString, final boolean isLeft) {
        if (s == null)
            return null;

        if (s.length() > countCharInString)
            return s.substring(0, countCharInString - 1);

        StringBuilder temp = new StringBuilder();
        for (int i = 0; i < (countCharInString - s.length()); i++)
            temp.append(ch);

        if (isLeft)
            temp.append(s);
        else
            temp.insert(0, s);

        return temp.toString();
    }

    /**
     * Укорачивает строку до указанного количества символов
     * @param s - String. Строка для укорочения
     * @param count_char - int. Количество символов
     * @return - String. Результирующая строка
     */
    public static String truncateString( final String s, int count_char) {
        if (s == null)
            return null;

        if (s.length() > count_char)
            return s.substring(0, count_char - 1);
        else
            return s;
    }

    /**
     * @deprecated use truncateString(String s, int count_char)
     * Укорачивает строку до указанного количества символов
     * @param s - String. Строка для укорочения
     * @param count_char - int. Количество символов
     * @return - String. Результирующая строка
     */
    public static String truncString( final String s, final int count_char) {
        return truncateString(s, count_char);
    }

    public static String rewriteString( final String s_) {
        if (s_ == null)
            return null;

        StringBuilder resultStr = new StringBuilder();

        for (int i = 0; i < s_.length(); i++) {
            resultStr.append('%')
                .append(
                    convertByte(s_.substring(i, i + 1).getBytes()).toUpperCase()
                );
        }

        return resultStr.toString();
    }

    /**
     * Convert a byte array into a printable format containing a
     * String of hexadecimal digit characters (two per byte).
     *
     * @param bytes - byte[] array representation
     * @return String
     */
    public static String convertByte( final byte bytes[]) {

        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (final byte newVar : bytes) {
            sb.append(convertDigit(newVar >> 4));
            sb.append(convertDigit(newVar & 0x0f));
        }
        return (sb.toString());
    }

    private static char convertDigit( final int valueToConvert) {
        int value = valueToConvert;
        value &= 0x0f;
        if (value >= 10)
            return ((char) (value - 10 + 'a'));
        else
            return ((char) (value + '0'));

    }

    public static String rewriteURL( final String str_) {
        if (str_ == null)
            return null;

        StringBuilder resultStr = new StringBuilder();
        String rewriteStr = "'/\\%?&\"=";

        String processStr;
        for (int i = 0; i < str_.length(); i++) {
            processStr = str_.substring(i, i + 1);
            if (rewriteStr.indexOf(processStr) != -1)
                processStr = "%" + convertByte(processStr.getBytes()).toUpperCase();

            resultStr.append(processStr);
        }
        return resultStr.toString();
    }

    public static String getIdByString( final List list, final String defaultForNull ) {
        if (list.size()==0)
            return defaultForNull;

        StringBuilder r = new StringBuilder();
        boolean isFirst = true;
        for (Object aList : list) {
            if (isFirst)
                isFirst = false;
            else
                r.append(',');

            r.append(aList);
        }
        return r.toString();
    }
}