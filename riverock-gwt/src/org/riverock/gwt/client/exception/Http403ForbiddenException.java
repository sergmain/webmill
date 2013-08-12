package org.riverock.gwt.client.exception;

import java.io.Serializable;

/**
 * User: Serg
 * Date: 12.08.13
 * Time: 22:02
 */
public class Http403ForbiddenException extends RuntimeException implements Serializable {
    public Http403ForbiddenException() {
    }

    public Http403ForbiddenException(String message) {
        super(message);
    }

    public Http403ForbiddenException(String message, Throwable cause) {
        super(message, cause);
    }
}
