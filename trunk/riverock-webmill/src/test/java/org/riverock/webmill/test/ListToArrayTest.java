package org.riverock.webmill.test;

import java.util.List;
import java.util.ArrayList;

/**
 *
 *
 * User: SergeMaslyukov
 * Date: 16.08.2006
 * Time: 15:02:03
 *
 * $Id$
 */
public class ListToArrayTest {
    public static void main(String[] args) {
        List<String> l = new ArrayList<String>();

        l.add("aaa");
        l.add("bbb");
        l.add("ccc");

        String[] s = l.toArray(new String[0]);

        s[0] = "ddd";

        String t = l.get(0);

        System.out.println("t = " + t);
    }
}
