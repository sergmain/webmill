package org.riverock.portlet.trash;

import java.util.Set;
import java.util.HashSet;

/**
 * User: SMaslyukov
 * Date: 21.05.2007
 * Time: 12:27:16
 */
public class HashSetTest {
    public static void main(String[] args) {
        Set set = new HashSet();

        set.add("aaa");
        set.add("bbb");
        set.add("ccc");
        set.add("aaa");

        System.out.println("set = " + set);
    }
}
