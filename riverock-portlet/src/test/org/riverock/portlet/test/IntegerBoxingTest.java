package org.riverock.portlet.test;

/**
 * @author smaslyukov
 *         Date: 27.07.2005
 *         Time: 15:01:48
 *         $Id$
 */
public class IntegerBoxingTest {
    public static void main( String[] args ) {
        Integer i = null;
        if (1==i) {
            System.out.println( "False" );
        }
        else {
            System.out.println( "True" );
        }
    }
}
