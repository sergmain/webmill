package org.riverock.gwt.client.commons;

/**
 * User: SergeMaslyukov
 * Date: 19.06.2009
 * Time: 23:55:06
 */
public class StringUtils {
    
    public static String replaceString(String str_, String search_, String ins) {
        if ((str_ == null) || (search_ == null) || (ins == null)) {
            return null;
        }

        String s_ = str_, resultStr = "";

        int pos;
        while ((pos = s_.indexOf(search_)) != -1) {
            if (pos != s_.length()) {
                resultStr += (s_.substring(0, pos) + ins);
                s_ = s_.substring(pos + search_.length(), s_.length());
            }
        }
        return resultStr + s_;
    }

    public static boolean isBlank(String s) {
        if (s==null) {
            return true;
        }
        if (s.length()==0) {
            return true;
        }
        if (s.trim().length()==0) {
            return true;
        }
        return false;
    }
}
