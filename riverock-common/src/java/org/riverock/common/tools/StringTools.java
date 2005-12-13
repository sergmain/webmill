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
package org.riverock.common.tools;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.lang.StringEscapeUtils;

import java.util.Locale;
import java.util.List;

/**
 * Public tools for work with strings.
 *
 * $Id$
 */
public class StringTools {
    private final static Log log = LogFactory.getLog(StringTools.class);

    public static String getUserName(String firstName, String middleName, String lastName) {
        if (log.isDebugEnabled()) {
            log.debug("firstName: " + firstName+ ", middleName: " +middleName+ ", lastName: " + lastName );
        }

        String s = "";
        if (!StringTools.isEmpty(firstName)) {
            s += firstName;

            if (!StringTools.isEmpty(middleName) || !StringTools.isEmpty(lastName)) {
                s += " ";
            }
        }
        if (!StringTools.isEmpty(middleName)) {
            s += middleName;
            if (!StringTools.isEmpty(lastName)) {
                s += " ";
            }
        }
        if (!StringTools.isEmpty(lastName)) {
            s += lastName;
        }
        
        if (StringTools.isEmpty(s)) {
//            return "unknown";
            return null;
        }
        else {
            return s;
//            return StringEscapeUtils.escapeXml(s);
        }
    }

    public static boolean isEmpty( final String s) {
        if (s==null || s.trim().length()==0)
            return true;
        else
            return false;
    }

    /**
     * преобразует строку вида 'SITE_LIST_SITE' -> 'SiteListSite'
     * @param f
     * @return результирующая строка
     */
    public static String capitalizeString( final String f)
    {
        String r = "";
        if (f.indexOf('_')==-1)
        {
            if (f.length()==1)
                return f.toUpperCase();

            return StringTools.capitalizeFirstChar(f);
        }

        String s_ = f;

        int pos;
        while ((pos = s_.indexOf('_')) != -1)
        {
            if (pos != s_.length())
            {
                r += StringTools.capitalizeFirstChar(s_.substring(0, pos));
                s_ = s_.substring(pos + 1, s_.length());
            }
        }
        return r + StringTools.capitalizeFirstChar(s_);
    }


    public static String capitalizeFirstChar( final String s )
    {
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

        StringBuffer s = new StringBuffer();
        for (int i=0; i<multyple; i++)
            s.append( str );

        return s.toString();
    }

    /**
     * @deprecated use decodeXml
     * @param s
     * @return
     */
    public static String toOrigin( final String s) {
        return StringEscapeUtils.unescapeXml( s );
    }


    /**
     * @deprecated use StringEscapeUtils.escapeXml( s )
     * @param s
     * @return
     */
    public static String encodeXml( final String s ) {
        return StringEscapeUtils.escapeXml( s );
    }

    /**
     * @deprecated use StringEscapeUtils.unescapeXml( s )
     * @param s
     * @return
     */
    public static String decodeXml( final String s ) {
        return StringEscapeUtils.unescapeXml( s );
    }

    /**
     * @deprecated use encodeXml
     * @param s
     * @return
     */
    public static String prepareEditForm( final String s) {
        return StringEscapeUtils.escapeXml( s );
    }

    public static String prepareToParsingSimple( final String s)
    {
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

    public static String prepareToParsing( final String s)
    {
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

    public static String toPlainHTML( final String s)
    {
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
        if (isEmpty(s))
            return def;
        else
            return s;
    }

    /**
     * Форматирует входной массив байт в столбцы по 76 символов. Применяется для base64 кодирования
     * @param bytes - byte[]. Входной массив байт
     * @return - byte[]. Выходной массив байт, отформатированный в столбцы по 76 байт.
     */
    public static byte[] formatArray( final byte bytes[])
    {
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

    public static byte[] getBytesUTF( final String s)
    {
        if (s==null)
            return new byte[0];

        try
        {
            return s.getBytes("utf-8");
        }
        catch (java.io.UnsupportedEncodingException e)
        {
            log.warn("String.getBytes(\"utf-8\") not supported");
            return new byte[0];
        }
    }

/*
	public static String parseUTFString(String s, int start, int end, int maxDBfieldLength)
	{
		if ((s==null) || s.length() <= start)
			return "";

		byte[] b = getBytesUTF(s);
		int pos = getStartUTF(byte[] b, maxDBfieldLength);
		String(b, int offset, int length, "utf-8")
	}
*/
    public static int getStartUTF( final String s, final int maxByte)
    {
        return getStartUTF(getBytesUTF(s), maxByte);
    }

    public static int getStartUTF( final byte[] b, final int maxByte)
    {
        return getStartUTF(b, maxByte, 0);
    }

    public static int getStartUTF( final byte[] b, final int maxByte, final int offset)
    {
        if (b.length <= offset)
            return -1;

        if (b.length < maxByte)
            return b.length;

        int idx = Math.min(b.length, maxByte + offset);

        for (int i = idx - 1; i > offset; i--)
        {
            int j = (int) (b[i] < 0?0x100 + b[i]:b[i]);
            if (j < 0x80)
            {
                return i + 1;
            }
        }
        return -1;
    }

    public static int lengthUTF(String s)
    {
        return getBytesUTF(s).length;
    }

    /**
     * Replace substring.
     * @param str_ - String. Source string
     * @param search_ - String which will be searching
     * @param ins - String for replace.
     * @return - String. Результирующая строка. Если один из параметров равен null, возвращается null
     */
    public static String replaceString( final String str_, final String search_, final String ins)
    {
        if ((str_ == null) || (search_ == null) || (ins == null))
            return null;

        String s_ = str_, resultStr = "";

        int pos;
        while ((pos = s_.indexOf(search_)) != -1)
        {
            if (pos != s_.length())
            {
                resultStr += (s_.substring(0, pos) + ins);
                s_ = s_.substring(pos + search_.length(), s_.length());
            }
        }
        return resultStr + s_;
    }

    /**
     * Заменяет один фрагмент строки другим во всей строке.
     * @param str_ - String. Строка в которой производится замена
     * @param search_ - String. Подстрока для поиска
     * @param ins - String. Подстрока для замены.
     * @return - String. Результирующая строка. Если один из параметров равен null, возвращается null
     */

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
    public static String replaceStringArray( final String str_, final String repl[][])
    {
        String qqq = str_;
        for (final String[] newVar : repl) {
            qqq = StringTools.replaceString(qqq, newVar[0], newVar[1]);
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
            throws java.io.UnsupportedEncodingException
    {
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
    public static String addString( final String s, final char ch, final int countAddChar, final boolean isLeft)
    {
        if (s == null)
            return null;

        if (countAddChar==0)
            return s;

        String temp = "";
        for (int i=0; i<countAddChar; i++)
            temp += ch;

        return isLeft?temp + s:s + temp;
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
    public static String appendString( final String s, final char ch, final int countCharInString, final boolean isLeft)
    {
        if (s == null)
            return null;

        if (s.length() > countCharInString)
            return s.substring(0, countCharInString - 1);

        String temp = "";
        for (int i = 0; i < (countCharInString - s.length()); i++)
            temp += ch;

        return isLeft?temp + s:s + temp;
    }

    /**
     * Укорачивает строку до указанного количества символов
     * @param s - String. Строка для укорочения
     * @param count_char - int. Количество символов
     * @return - String. Результирующая строка
     */
    public static String truncateString( final String s, int count_char)
    {
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
    public static String truncString( final String s, final int count_char)
    {
        return truncateString(s, count_char);
    }

    public static String rewriteString( final String s_)
    {
        if (s_ == null)
            return null;

        String resultStr = "";

//        int pos;
        for (int i = 0; i < s_.length(); i++)
            resultStr += "%" + convertByte(
                    s_.substring(i, i + 1).getBytes()
            ).toUpperCase();

        return resultStr;
    }

    /**
     * Convert a byte array into a printable format containing a
     * String of hexadecimal digit characters (two per byte).
     *
     * @param bytes - byte[] array representation
     */
    public static String convertByte( final byte bytes[])
    {

        StringBuffer sb = new StringBuffer(bytes.length * 2);
        for (final byte newVar : bytes) {
            sb.append(convertDigit((int) (newVar >> 4)));
            sb.append(convertDigit((int) (newVar & 0x0f)));
        }
        return (sb.toString());

    }

    private static char convertDigit( final int valueToConvert)
    {
        int value = valueToConvert;
        value &= 0x0f;
        if (value >= 10)
            return ((char) (value - 10 + 'a'));
        else
            return ((char) (value + '0'));

    }

    public static String rewriteURL( final String str_)
    {
        if (str_ == null)
            return null;

        String s_ = str_, resultStr = "";
        String rewriteStr = "'/\\%?&\"=";

        String processStr = "";
        for (int i = 0; i < s_.length(); i++)
        {
            processStr = s_.substring(i, i + 1);
            if (rewriteStr.indexOf(processStr) != -1)
                processStr = "%" + convertByte(processStr.getBytes()).toUpperCase();

            resultStr += processStr;
        }
        return resultStr;
    }

    public static String getIdByString( final List list, final String defaultForNull )
    {
        if (list.size()==0)
            return defaultForNull;

        String r = "";
        for (int i=0; i<list.size(); i++)
        {
            if (r.length()!=0)
                r += ',';

            r += list.get(i).toString();
        }
        return r;
    }
}