package org.riverock.webmill.portal.spi;

import java.util.List;
import java.util.Map;

import org.riverock.interfaces.portal.spi.PortalPreferencesSpi;
import org.riverock.interfaces.sso.a3.AuthSession;
import org.riverock.webmill.portal.dao.InternalDaoFactory;

/**
 * User: SMaslyukov
 * Date: 11.05.2007
 * Time: 20:24:45
 */
public class PortalPreferencesSpiImpl implements PortalPreferencesSpi {
    private AuthSession authSession = null;
    private ClassLoader classLoader = null;

    PortalPreferencesSpiImpl(AuthSession authSession, ClassLoader classLoader, Long siteId) {
        this.authSession = authSession;
        this.classLoader = classLoader;
    }

    public Map<String, List<String>> load(Long catalogId) {
        ClassLoader oldLoader = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader( classLoader );
            return InternalDaoFactory.getInternalPreferencesDao().load(catalogId);
        }
        finally {
            Thread.currentThread().setContextClassLoader( oldLoader );
        }
    }

    public Map<String, List<String>> initMetadata(String metadata) {
        ClassLoader oldLoader = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader( classLoader );
            return InternalDaoFactory.getInternalPreferencesDao().initMetadata(metadata);
        }
        finally {
            Thread.currentThread().setContextClassLoader( oldLoader );
        }
    }
}
