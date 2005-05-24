package org.riverock.module.exception;

/**
 * Exception in action
 */
public class ActionException extends Exception {
    public ActionException() {
        super();
    }

    public ActionException(String s) {
        super(s);
    }

    public ActionException(String s, Throwable cause) {
        super(s, cause);
    }
}