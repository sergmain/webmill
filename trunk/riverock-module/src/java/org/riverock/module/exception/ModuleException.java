package org.riverock.module.exception;

/**
 * Exception in module
 */
public class ModuleException extends Exception {
    public ModuleException() {
        super();
    }

    public ModuleException(String s) {
        super(s);
    }

    public ModuleException(String s, Throwable cause) {
        super(s, cause);
    }
}