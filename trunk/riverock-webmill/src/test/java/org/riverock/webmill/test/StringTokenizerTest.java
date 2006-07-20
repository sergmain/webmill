package org.riverock.webmill.test;

import java.util.StringTokenizer;

/**
 * @author SMaslyukov
 *         Date: 29.05.2005
 *         Time: 16:05:11
 *         $Id$
 */
public class StringTokenizerTest {
    public static void main(String[] args) {
        String s = "role1, role2, role3";
        System.out.println("#1");
        StringTokenizer st = new StringTokenizer(s, ", ", false);
        while (st.hasMoreTokens()) {
            System.out.println(">"+st.nextToken()+"<");

        }
        System.out.println("#2");
        st = new StringTokenizer(s, ",");
        while (st.hasMoreElements()) {
            System.out.println(">"+st.nextElement()+"<");

        }
        System.out.println("#3");
        st = new StringTokenizer(s);
        while (st.hasMoreElements()) {
            System.out.println(">"+st.nextElement()+"<");
        }
    }
}
