package org.riverock.deploy.test;

/**
 * @author smaslyukov
 *         Date: 02.08.2005
 *         Time: 17:44:20
 *         $Id$
 */
public class OverflowTest {
    public static void main(String[] args) {
        int maxValue = Integer.parseInt( args[0] );
        System.out.println("Integer: " +maxValue );
        System.out.println("Integer: " + (maxValue + 1 ) );
    }
}
