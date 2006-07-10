package org.riverock.webmill.test;

/**
 * @author Sergei Maslyukov
 *         Date: 20.04.2006
 *         Time: 18:00:12
 */
public class SwitchTest {
        public static final int INT_1 = 1;
        public static void main(String[] args) {
            Integer idx = null;
            switch(idx) {
                case INT_1:
                    System.out.println("1");
                    break;
                default:
                    System.out.println("default");
            }
        }

}
