package org.riverock.webmill.portal.impl;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

/**
 * User: SMaslyukov
 * Date: 01.06.2007
 * Time: 20:03:15
 */
public class InternalServletRequestWrapper extends HttpServletRequestWrapper {
    private final static Logger log = Logger.getLogger(InternalServletRequestWrapper.class);

    // all methos in HttpServletRequest must invoked only from PortalFrontController
    // all others invokes are wrong
    boolean isOk = false;

    public InternalServletRequestWrapper(HttpServletRequest request) {
        super(request);
    }

    public HttpSession getSession() {
        if (true) throw new RuntimeException("session created");
        return super.getSession();
    }

    public HttpSession getSession(boolean isCreate) {
        if (isCreate) throw new RuntimeException("session created");
        return super.getSession(isCreate);
    }

    public Enumeration getAttributeNames() {
        if ( !isOk ) {
            log.warn( "!!! Requested getAttributeNames() from http request" );
            try {
                throw new Exception("error");
            }
            catch(Exception e) {
                log.error("error", e);
            }
        }
        return super.getAttributeNames();
    }

    public Object getAttribute(String key) {
        if ( !isOk ) {
            log.warn( "!!! Requested getAttributeNames() from http request, key: " + key );
            try {
                throw new Exception("error");
            }
            catch(Exception e) {
                log.error("error", e);
            }
        }
        return super.getAttribute(key);
    }

    public void setAttribute(String key, Object value) {
        if ( !isOk ) {
            log.warn( "!!! Requested getAttributeNames() from http request, key: " + key + ", value: " + value );
            try {
                throw new Exception("error");
            }
            catch(Exception e) {
                log.error("error", e);
            }
        }
        super.setAttribute(key, value);
    }

    public void removeAttribute(String key) {
        if ( !isOk ) {
            log.warn( "!!! Requested removeAttribute() from http request" );
            try {
                throw new Exception("error");
            }
            catch(Exception e) {
                log.error("error", e);
            }
        }
        super.removeAttribute(key);
    }
}
