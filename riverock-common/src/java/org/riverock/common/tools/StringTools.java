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

 * Public tools for work with strings.

 *

 * $Id$

 */

package org.riverock.common.tools;



import org.apache.log4j.Logger;



import java.util.Locale;



public class StringTools

{

    private static Logger cat = Logger.getLogger("org.riverock.tools.StringTools" );





    /**

     * ����������� ������ ���� 'SITE_LIST_SITE' -> 'SiteListSite'

     * @param f

     * @return �������������� ������

     */

    public static String capitalizeString(String f)

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





    public static String capitalizeFirstChar( String s )

    {

        if (s==null)

            return null;



        if (s.length()==0)

            return "";



        return s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();

    }



    /**

     * Build Locale from string. jdk1.4 have bug

     * @param string with locale

     * @return java.util.Locale

     */

    public static Locale getLocale(String string)

    {

        if (string==null)

            return null;



        int idx = string.indexOf('_');

        if (idx == -1)

            return new Locale(string, "");



        String lang = string.substring(0, idx);

        int idx1 = string.indexOf('_', idx+1);

        if (idx1==-1)

            return new Locale(lang, string.substring(idx+1));



        return new Locale(

            lang, string.substring(idx+1, idx1), string.substring(idx1+1) );

    }



    public static String getMultypleString(String str, int multyple)

    {

        if (str==null || multyple==0)

            return "";



        String s = "";

        for (int i=0; i<multyple; i++)

            s += str;



        return s;

    }



    public static String toOrigin(String s)

    {

        if (s==null)

            return null;



        return StringTools.replaceStringArray(

                s,

                new String[][]

                {

                    { "&gt;", ">"},

                    { "&lt;", "<"},

                    { "&amp;", "&"}

                }

        );

    }





    public static String encodeXml( String s )

    {

        if (s==null)

            return null;



        return StringTools.replaceStringArray(

                s,

                new String[][]

                {

                    { "&", "&amp;" },

                    { "<", "&lt;"},

                    { ">", "&gt;"}

                }

        );

    }



    public static String decodeXml( String s )

    {

        if (s==null)

            return null;



        return StringTools.replaceStringArray(

                s,

                new String[][]

                {

                    { "&gt;", ">"},

                    { "&lt;", "<"},

                    { "&amp;", "&"}

                }

        );

    }



    public static String prepareEditForm(String s)

    {

        if (s==null)

            return null;



        return StringTools.replaceStringArray(

                s,

                new String[][]

                {

                    { "&", "&amp;" },

                    { "<", "&lt;" },

                    { ">", "&gt;" },

                    { "\"", "&quot;" }



/*

                    { "&gt;", ">"},

                    { "&lt;", "<"},

                    { "&amp;", "&"}

*/

                }

        );

    }



    public static String prepareToParsingSimple(String s)

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



    public static String prepareToParsing(String s)

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



    public static String toPlainHTML(String s)

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

     * ��������� �������� ������ ���� String gecnsv (null) ��� ������ ������ � ���� �����, �� ����������

     * ������ �� ���������

     * @param s - String. ������ ��� ��������

     * @param def - String. ������, ������������, ���� ����������� ������ == null ��� ������

     * @return - String. ����� ������

     */

    public static String replaceEmpty(String s, String def)

    {

        if (s == null)

            return def;



        if (s.trim().length() == 0)

            return def;



        return s;

    }



    /**

     * ����������� ������� ������ ���� � ������� �� 76 ��������. ����������� ��� base64 �����������

     * @param bytes - byte[]. ������� ������ ����

     * @return - byte[]. �������� ������ ����, ����������������� � ������� �� 76 ����.

     */

    public static byte[] formatArray(byte bytes[])

    {

        int newLength = bytes.length + bytes.length / 57;



        if (cat.isDebugEnabled())

            cat.debug("Old length: " + bytes.length + "\nNew length: " + newLength);



        byte bf[] = new byte[newLength];



        int charCount = 0;

        for (int i = 0; i < bytes.length; i++)

        {



//cat.debug("charCount: "+charCount+" i: "+i);



            bf[charCount++] = bytes[i];



            // Add newline every 76 output chars (that's 57 input chars)

            if ((i != 0) && (i % 57 == 0))

            {

                if (cat.isDebugEnabled())

                    cat.debug(" " + i + " " + charCount);



                bf[charCount++] = (byte) '\n';

            }



        }

        return bf;

    }



    public static byte[] getBytesUTF(String s)

    {

        if (s==null)

            return new byte[0];



        try

        {

            return s.getBytes("utf-8");

        }

        catch (java.io.UnsupportedEncodingException e)

        {

            cat.warn("String.getBytes(\"utf-8\") not supported");

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

    public static int getStartUTF(String s, int maxByte)

    {

        return getStartUTF(getBytesUTF(s), maxByte);

    }



    public static int getStartUTF(byte[] b, int maxByte)

    {

        return getStartUTF(b, maxByte, 0);

    }



    public static int getStartUTF(byte[] b, int maxByte, int offset)

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

     * @return - String. �������������� ������. ���� ���� �� ���������� ����� null, ������������ null

     */

    public static String replaceString(String str_, String search_, String ins)

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

     * �������� ���� �������� ������ ������ �� ���� ������.

     * @param str_ - String. ������ � ������� ������������ ������

     * @param search_ - String. ��������� ��� ������

     * @param ins - String. ��������� ��� ������.

     * @return - String. �������������� ������. ���� ���� �� ���������� ����� null, ������������ null

     */



    /**

     * �������� ���� �������� ������ ������ �� ���� ������. ������ ��� ������ � ������ ����������

     * � ���� 2-�� ������� �������.

     *

     * @param str_ - String. ������ � ������� ������������ ������

     * @param repl - String[][]. ������ ����� ��� ������ � ������.

     * repl[][0] - ������ ��� ������

     * repl[][1] - ������ ��� ������

     * @return - String. ����� ������

     */

    public static String replaceStringArray(String str_,

                                            String repl[][])

    {

        String qqq = str_;

        for (int i = 0; i < repl.length; i++)

        {

            qqq = StringTools.replaceString(qqq, repl[i][0], repl[i][1]);

        }

        return qqq;



    }



    /*

     * @deprecated Use public static String replaceString(String str_, String search_, String ins)

     */

//    public static String replaceStr(String str_, String search_, String ins)

//    {

//        return replaceString(str_, search_, ins);

//    }



    /**

     * ����������� ������ �� ����� ��������� � ������

     * @param s - String. ������ ��� ��������������

     * @param fromCharset - String. �������� ��������� ������

     * @param toCharset - String. �������������� ��������� ������

     * @return - String.

     * @throws java.io.UnsupportedEncodingException

     */

    public static String convertString(String s, String fromCharset, String toCharset)

            throws java.io.UnsupportedEncodingException

    {

        if (s == null)

            return null;



        return new String(s.getBytes(fromCharset), toCharset);

    }



    /**

     * ��������� ������ ��������� c����� ��� �����. � ����������� �� ����� isLeft ����������

     * �������� ����� (true) ��� ������ (false)

     * @param s -  String. ������ ��� ����������

     * @param ch -  char. ������, ������������ ��� ����������.

     * @param countAddChar -  ���������� �������� ��� ����������.

     * @param isLeft - boolean. ���������� ����� ��� ������

     * @return  -  String.

     */

    public static String addString(String s, char ch, int countAddChar, boolean isLeft)

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

     * ��������� ������ ��������� �� ���������� ����������. � ����������� �� ����� isLeft ����������

     * �������� ����� (true) ��� ������ (false)

     * @param s -  String. ������ ��� ����������

     * @param ch -  char. ������, ������������ ��� ����������.

     * @param countCharInString -  String. �������������� ����� ������.

     * @param isLeft - boolean. ���������� ����� ��� ������

     * @return  -  String.

     */

    public static String appendString(String s, char ch, int countCharInString, boolean isLeft)

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

     * ����������� ������ �� ���������� ���������� ��������

     * @param s - String. ������ ��� ����������

     * @param count_char - int. ���������� ��������

     * @return - String. �������������� ������

     */

    public static String truncateString(String s, int count_char)

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

     * ����������� ������ �� ���������� ���������� ��������

     * @param s - String. ������ ��� ����������

     * @param count_char - int. ���������� ��������

     * @return - String. �������������� ������

     */

    public static String truncString(String s, int count_char)

    {

        return truncateString(s, count_char);

    }



    public static String rewriteString(String s_)

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

    public static String convertByte(byte bytes[])

    {



        StringBuffer sb = new StringBuffer(bytes.length * 2);

        for (int i = 0; i < bytes.length; i++)

        {

            sb.append(convertDigit((int) (bytes[i] >> 4)));

            sb.append(convertDigit((int) (bytes[i] & 0x0f)));

        }

        return (sb.toString());



    }



    private static char convertDigit(int value)

    {



        value &= 0x0f;

        if (value >= 10)

            return ((char) (value - 10 + 'a'));

        else

            return ((char) (value + '0'));



    }



    public static String rewriteURL(String str_)

    {

        if (str_ == null)

            return null;



        String s_ = str_, resultStr = "";

        String rewriteStr = "'/\\%?&\"=";



//        int pos;

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

}