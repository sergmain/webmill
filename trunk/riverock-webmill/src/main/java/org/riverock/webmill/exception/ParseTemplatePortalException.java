package org.riverock.webmill.exception;

/**
 * User: serg_main
 * Date: 11.05.2004
 * Time: 17:46:28
 * @author Serge Maslyukov
 * $Id: PortalException.java 1081 2006-11-24 18:15:45Z serg_main $
 */
public class ParseTemplatePortalException extends RuntimeException {

    public ParseTemplatePortalException(){
        super();
    }

    public ParseTemplatePortalException(String s){
        super(s);
    }

    public ParseTemplatePortalException(String s, Throwable cause){
        super(s, cause);
    }
}
