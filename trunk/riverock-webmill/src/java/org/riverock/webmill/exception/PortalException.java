package org.riverock.webmill.exception;

/**
 * User: serg_main
 * Date: 11.05.2004
 * Time: 17:46:28
 * @author Serge Maslyukov
 * $Id$
 */
public class PortalException extends Exception {

    public PortalException(){
        super();
    }

    public PortalException(String s){
        super(s);
    }

    public PortalException(String s, Throwable cause){
        super(s, cause);
    }
}

