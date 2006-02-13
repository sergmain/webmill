package org.riverock.interfaces.portal;

/**
 * @author SergeMaslyukov
 *         Date: 31.01.2006
 *         Time: 14:27:55
 *         $Id$
 */
public interface PortalSessionManager {
    public boolean createSession( String userLogin, String userPassword );
}
