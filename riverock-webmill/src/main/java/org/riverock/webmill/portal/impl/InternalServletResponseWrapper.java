package org.riverock.webmill.portal.impl;

import java.io.PrintWriter;
import java.io.IOException;

import javax.servlet.http.HttpServletResponseWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.ServletResponse;
import javax.servlet.ServletOutputStream;

import org.apache.log4j.Logger;

/**
 * User: SMaslyukov
 * Date: 01.06.2007
 * Time: 20:04:12
 */
public class InternalServletResponseWrapper extends HttpServletResponseWrapper {
    private final static Logger log = Logger.getLogger(InternalServletResponseWrapper.class);

    // all methos in HttpServletResponse must invoked only from PortalFrontController
    // all others invokes are wrong
    boolean isOk = false;

    public InternalServletResponseWrapper( HttpServletResponse httpServletResponse ) {
        super( httpServletResponse );
    }

    public ServletResponse getResponse(){
        if ( !isOk ) {
            log.warn( "!!! Requested getResponse() from http response" );

            try {
                throw new Exception("error");
            }
            catch(Exception e) {
                log.error("error", e);
            }
        }
        return super.getResponse();
    }

    public void setResponse(ServletResponse response){
        if ( !isOk ) {
            log.warn( "!!! Requested setResponse() from http response" );
            try {
                throw new Exception("error");
            }
            catch(Exception e) {
                log.error("error", e);
            }
        }

        super.setResponse( response );
    }

    public PrintWriter getWriter() throws IOException {
        if ( !isOk )
            log.warn( "!!! Requested getWriter() from http response" );

        return super.getWriter();
    }

    public ServletOutputStream getOutputStream() throws IOException {
        if ( !isOk )
            log.warn( "!!! Requested getOutputStream() from http response" );

        return super.getOutputStream();
    }

    public void setHeader( String name, String value ) {
        if ( !isOk )
            log.warn( "!!! Requested setHeader() from http response" );

        super.setHeader( name, value );
    }

    public void setContentLength( int length ) {
        if ( !isOk )
            log.warn( "!!! Requested setContentLength() from http response" );

        super.setContentLength( length );
    }

    public void setContentType( String type ) {
        if ( !isOk )
            log.warn( "!!! Requested setContentType() from http response" );

        super.setContentType( type );
    }

    public void addCookie(Cookie cookie) {
        if ( !isOk )
            log.warn( "!!! Requested addCookie() from http response" );

        super.addCookie( cookie );
    }
}
