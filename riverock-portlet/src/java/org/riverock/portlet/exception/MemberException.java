package org.riverock.portlet.exception;

/**
 * User: smaslyukov
 * Date: 30.07.2004
 * Time: 19:56:26
 * $Id$
 */
public class MemberException extends Exception{
    public MemberException(){
        super();
    }

    public MemberException(String s){
        super(s);
    }

    public MemberException(String s, Throwable cause){
        super(s, cause);
    }

    public String toString(){
        return super.toString();
    }

    public String getMessage(){
        return super.getMessage();
    }
}
