package org.riverock.portlet.trash;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;

/**
 * User: SMaslyukov
 * Date: 14.08.2007
 * Time: 20:25:28
 */
public class AccessToSuperClassTest {
    public static class A {
        public void put() {
            System.out.println("class A");
        }
    }
    public static class B extends A {
        public void put() {
            System.out.println("class B");
        }
    }
    public static void main(String[] args) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        A a = new B();
        Class<? extends A> aClass = a.getClass();
        Class<?> superclass = aClass.getSuperclass();
        Method m = superclass.getDeclaredMethod("put");
        m.invoke(a);

    }
}
