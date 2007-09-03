package org.riverock.webmill.portal.spi;

import java.util.List;

import org.riverock.interfaces.portal.bean.PortletAlias;
import org.riverock.interfaces.portal.bean.UrlAlias;
import org.riverock.interfaces.portal.spi.PortalAliasSpi;
import org.riverock.interfaces.sso.a3.AuthSession;
import org.riverock.webmill.portal.dao.InternalDaoFactory;

/**
 * User: SergeMaslyukov
 * Date: 02.09.2007
 * Time: 12:18:36
 */
public class PortalAliasSpiImpl implements PortalAliasSpi {
    private AuthSession authSession = null;
    private ClassLoader classLoader = null;
    private Long siteId = null;

    PortalAliasSpiImpl(AuthSession authSession, ClassLoader classLoader, Long siteId) {
        this.authSession = authSession;
        this.classLoader = classLoader;
        this.siteId = siteId;
    }

    public PortletAlias getPortletAlias(Long portletAliasId) {
        ClassLoader oldLoader = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader( classLoader );
            return InternalDaoFactory.getInternalAliasDao().getPortletAlias( portletAliasId );
        }
        finally {
            Thread.currentThread().setContextClassLoader( oldLoader );
        }
    }

    public Long createPortletAlias(PortletAlias portletAlias) {
        ClassLoader oldLoader = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader( classLoader );
            return InternalDaoFactory.getInternalAliasDao().createPortletAlias( portletAlias );
        }
        finally {
            Thread.currentThread().setContextClassLoader( oldLoader );
        }
    }

    public void updatePortletAlias(PortletAlias portletAlias) {
        ClassLoader oldLoader = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader( classLoader );
            InternalDaoFactory.getInternalAliasDao().updatePortletAlias( portletAlias );
        }
        finally {
            Thread.currentThread().setContextClassLoader( oldLoader );
        }
    }

    public void deletePortletAlias(PortletAlias portletAlias) {
        ClassLoader oldLoader = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader( classLoader );
            InternalDaoFactory.getInternalAliasDao().deletePortletAlias( portletAlias );
        }
        finally {
            Thread.currentThread().setContextClassLoader( oldLoader );
        }
    }

    public List<PortletAlias> getPortletAliases(Long siteId) {
        ClassLoader oldLoader = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader( classLoader );
            return InternalDaoFactory.getInternalAliasDao().getPortletAliases( siteId );
        }
        finally {
            Thread.currentThread().setContextClassLoader( oldLoader );
        }
    }

    public UrlAlias getUrlAlias(Long urlAliasId) {
        ClassLoader oldLoader = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader( classLoader );
            return InternalDaoFactory.getInternalAliasDao().getUrlAlias( urlAliasId );
        }
        finally {
            Thread.currentThread().setContextClassLoader( oldLoader );
        }
    }

    public Long createUrlAlias(UrlAlias urlAlias) {
        ClassLoader oldLoader = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader( classLoader );
            return InternalDaoFactory.getInternalAliasDao().createUrlAlias( urlAlias );
        }
        finally {
            Thread.currentThread().setContextClassLoader( oldLoader );
        }
    }

    public void updateUrlAlias(UrlAlias urlAlias) {
        ClassLoader oldLoader = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader( classLoader );
            InternalDaoFactory.getInternalAliasDao().updateUrlAlias( urlAlias );
        }
        finally {
            Thread.currentThread().setContextClassLoader( oldLoader );
        }
    }

    public void deleteUrlAlias(UrlAlias urlAlias) {
        ClassLoader oldLoader = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader( classLoader );
            InternalDaoFactory.getInternalAliasDao().deleteUrlAlias( urlAlias );
        }
        finally {
            Thread.currentThread().setContextClassLoader( oldLoader );
        }
    }

    public List<UrlAlias> getUrlAliases(Long siteId) {
        ClassLoader oldLoader = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader( classLoader );
            return InternalDaoFactory.getInternalAliasDao().getUrlAliases( siteId );
        }
        finally {
            Thread.currentThread().setContextClassLoader( oldLoader );
        }
    }
}
