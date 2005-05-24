package org.riverock.portlet.test;

/**
 * @author SMaslyukov
 *         Date: 20.05.2005
 *         Time: 13:51:58
 *         $Id$
 */
public class InstanceofNull {
    public static void main(String s[]) throws Exception {

        Long id = null;
        Long aLong = new Long(1);

        if (aLong.equals(id)) {
            System.out.println("True");
        }
        else {
            System.out.println("False");
        }

    }
}
