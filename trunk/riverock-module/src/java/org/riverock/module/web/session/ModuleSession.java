package org.riverock.module.web.session;

/**
 * @author Serge Maslyukov
 *         Date: 24.04.2005
 *         Time: 20:27:53
 *         $Id$
 */
public interface ModuleSession {
    public Object getAttribute(String key);
    public void setAttribute(String key, Object value);
}
