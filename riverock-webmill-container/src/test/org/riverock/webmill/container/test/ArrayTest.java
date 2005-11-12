package org.riverock.webmill.container.test;

import java.util.List;
import java.util.ArrayList;

/**
 * @author SergeMaslyukov
 *         Date: 08.11.2005
 *         Time: 14:12:35
 *         $Id$
 */
public class ArrayTest {
    public static void main(String[] args) {
        List list = new ArrayList();

        list.add("aaa");
        list.add("bbb");
        list.add("ccc");

        String[] values = (String[]) list.toArray(new String[0]);


        int i = 0;
    }
}
