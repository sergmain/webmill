package org.riverock.webmill.trash;

/**
 * User: SMaslyukov
 * Date: 21.08.2007
 * Time: 17:07:45
 */
public class StringRefTest {
    public static class StringHolder {
        private String s;

        public StringHolder(String s) {
            this.s = s;
        }
    }
    
    public static void main(String[] args) {

        final String aaa = "aaa";

        a(new StringRefTest.StringHolder(aaa), aaa);
//        a1(aaa);
    }

    private static void a(StringHolder holder, String aaa1) {
        System.out.println(holder.s==aaa1);
    }

/*
    private static void a1(final String aaa1) {
        System.out.println(aaa==aaa1);
    }
*/

}
