package org.riverock.gwt.client.commons;

/**
 * User: Serg
 * Date: 23.01.14
 * Time: 16:26
 */
public class SimpleHolder<T> {
    private T obj;

    public T getObj() {
        return obj;
    }

    public void setObj(T obj) {
        this.obj = obj;
    }
}

