package org.riverock.portlet.trash;

import org.apache.commons.lang.StringUtils;

/**
 * User: SMaslyukov
 * Date: 18.07.2007
 * Time: 17:01:11
 */
public class TestStringUtils {
    public static void main(String[] args) {
        String s = "qwe321";

        s = StringUtils.substring(s, 0, 20);
        System.out.println("s = " + s);
    }
}
