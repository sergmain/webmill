package org.riverock.webmill.test;

import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;

import org.riverock.common.tools.StringTools;

/**
 * @author Sergei Maslyukov
 *         Date: 12.05.2006
 *         Time: 14:31:07
 */
public class ConvertListToArray {
    public static void main(String[] args) {
        List<String> sun = new ArrayList<String>();
        sun.add("Feel");
        sun.add("the");
        sun.add("power");
        sun.add("of");
        sun.add("the");
        sun.add("Sun");
        String[] s1 = sun.toArray(new String[0]); //Collection to array
        for (String contents : s1) {
            System.out.print(contents);
        }
        System.out.println();
        List<String> sun2 = Arrays.asList(s1); //Array back to Collection
        for (String s2 : sun2) {
            String s3 = s2;
            System.out.print(s3);
        }
        System.out.println();

        String s = StringTools.arrayToString(sun.toArray(new String[0]));
        System.out.println("s = " + s);

//        sun2.add(new String("Hello")); // throws UnsupportedOperationException
    }
}
