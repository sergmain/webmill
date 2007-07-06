package org.riverock.portlet.test.webclip;

import java.util.HashSet;
import java.util.Set;

import junit.framework.TestCase;

/**
 * User: SMaslyukov
 * Date: 06.07.2007
 * Time: 15:10:55
 */
public class WebclipUrlCheckerTest extends TestCase {

    public void testUrlChecker() {
        Set<String> set = new HashSet<String>();
        set.add("1-aaa");
        String s1 = "1-bbb";
        set.add(s1);
        set.add("1-ccc");
        set.add("2-aaa");
        set.add("3-aaa");

        System.out.println("set = " + set);
        
        String s = ""+1 + '-' + "bbb";
        System.out.println("s = " + s);
        System.out.println("set.contains() = " + set.contains(s));

        System.out.println("s.hashCode() = " + s.hashCode());
        System.out.println("s1.hashCode() = " + s1.hashCode());
        System.out.println("Equals: " + (s.equals(s1)));

    }
}
