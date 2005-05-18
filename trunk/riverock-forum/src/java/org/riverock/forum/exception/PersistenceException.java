package org.riverock.forum.exception;

/**
 * @author SMaslyukov
 *         Date: 24.03.2005
 *         Time: 15:47:59
 *         $Id$
 */
public class PersistenceException extends Exception {
    public PersistenceException() {
        super();
    }

    public PersistenceException(String s) {
        super(s);
    }

    public PersistenceException(String s, Throwable cause) {
        super(s, cause);
    }
}
