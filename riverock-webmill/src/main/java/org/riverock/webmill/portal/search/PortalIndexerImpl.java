package org.riverock.webmill.portal.search;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.commons.lang.StringUtils;

import org.riverock.interfaces.portal.bean.PortletName;
import org.riverock.interfaces.portal.search.PortalIndexer;
import org.riverock.interfaces.portal.search.PortletIndexer;
import org.riverock.interfaces.portal.search.PortletIndexerShort;
import org.riverock.webmill.container.ContainerConstants;
import org.riverock.webmill.container.portlet.PortletContainer;
import org.riverock.webmill.container.portlet.PortletEntry;
import org.riverock.webmill.container.portlet.PortletContainerException;
import org.riverock.webmill.container.portlet.PortletNotRegisteredException;
import org.riverock.webmill.container.tools.PortletService;
import org.riverock.webmill.exception.PortalSearchException;
import org.riverock.webmill.portal.dao.InternalDaoFactory;

/**
 * User: SMaslyukov
 * Date: 01.06.2007
 * Time: 17:25:36
 */
public class PortalIndexerImpl implements PortalIndexer {
    private final static Logger log = Logger.getLogger(PortalIndexerImpl.class);

    private PortletContainer container;
    private ClassLoader portalClassLoader;

    public PortalIndexerImpl(PortletContainer container, ClassLoader portalClassLoader) {
        this.container = container;
        this.portalClassLoader = portalClassLoader;
    }

    public List<PortletIndexerShort> getPortletIndexers(Long siteId) {
        ClassLoader oldLoader = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader( portalClassLoader );

            List<PortletName> names = InternalDaoFactory.getInternalPortletNameDao().getPortletNameList();
            List<PortletIndexerShort> shorts = new ArrayList<PortletIndexerShort>();

            for (PortletName name : names) {
                PortletEntry portletEntry;
                try {
                    portletEntry = container.getPortletInstance(name.getPortletName());
                }
                catch (PortletNotRegisteredException e) {
                    continue;
                }
                catch (PortletContainerException e) {
                    String es = "Error getPortletIndexers()";
                    log.error(es, e);
                    throw new PortalSearchException(es, e);
                }
                if (portletEntry==null || portletEntry.getPortletDefinition()==null) {
                    continue;
                }
                String className = PortletService.getStringParam(
                    portletEntry.getPortletDefinition(), ContainerConstants.WEBMILL_PORTLET_INDEXER_CLASS_NAME
                );

                if (StringUtils.isNotBlank(className)) {
                    shorts.add(
                        new PortletIndexerShortImpl(portletEntry.getClassLoader(), className, name.getPortletId(), name.getPortletName())
                    );
                }
            }
            return shorts;
        }
        finally {
            Thread.currentThread().setContextClassLoader( oldLoader );
        }
    }

    public PortletIndexer getPortletIndexer(Long siteId, Object portletIndexerId) {
        ClassLoader oldPortalClassLoader = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader( portalClassLoader );

            List<PortletIndexerShort> list = getPortletIndexers(siteId);
            for (PortletIndexerShort portletIndexerShort : list) {
                if (portletIndexerShort.getId().equals(portletIndexerId)) {
                    try {
                        String className = portletIndexerShort.getClassName();
                        if (className==null) {
                            return null;
                        }
                        ClassLoader oldLoader = Thread.currentThread().getContextClassLoader();
                        try {
                            Thread.currentThread().setContextClassLoader( portletIndexerShort.getClassLoader() );
                            if (log.isDebugEnabled()) {
                                log.debug("portalClassLoader: " + portalClassLoader);
                                log.debug("oldPortalClassLoader: " + oldPortalClassLoader);
                                log.debug("old portlet classLoader: " + oldLoader);
                                log.debug("portlet classLoader: " + portletIndexerShort.getClassLoader() );
                            }
                            Class clazz = Class.forName(className, true, portletIndexerShort.getClassLoader());
                            PortletIndexer portletIndexer = (PortletIndexer)clazz.newInstance();
                            portletIndexer.init(portletIndexerId, siteId, portletIndexerShort.getClassLoader() );
                            return portletIndexer;
                        }
                        finally {
                            Thread.currentThread().setContextClassLoader( oldLoader );
                        }
                    }
                    catch (Throwable e) {
                        String es = "Error getPortletIndexer()";
                        log.error(es, e);
                        throw new PortalSearchException(es, e);
                    }
                }
            }
            return null;
        }
        finally {
            Thread.currentThread().setContextClassLoader( oldPortalClassLoader );
        }
    }

    public void markAllForIndexing(Long siteId) {
        ClassLoader oldPortalClassLoader = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader( portalClassLoader );

            List<PortletIndexerShort> list = getPortletIndexers(siteId);
            for (PortletIndexerShort portletIndexerShort : list) {
                try {
                    String className = portletIndexerShort.getClassName();
                    if (className==null) {
                        continue;
                    }
                    ClassLoader oldLoader = Thread.currentThread().getContextClassLoader();
                    try {
                        Thread.currentThread().setContextClassLoader( portletIndexerShort.getClassLoader() );
                        Class clazz = Class.forName(className, true, portletIndexerShort.getClassLoader());
                        PortletIndexer portletIndexer = (PortletIndexer)clazz.newInstance();
                        portletIndexer.init(portletIndexerShort.getId(), siteId, portletIndexerShort.getClassLoader() );
                        portletIndexer.markAllForIndexing();
                    }
                    finally {
                        Thread.currentThread().setContextClassLoader( oldLoader );
                    }
                }
                catch (Throwable e) {
                    String es = "Error markAllForIndexing()";
                    log.error(es, e);
                    throw new PortalSearchException(es, e);
                }
            }
        }
        finally {
            Thread.currentThread().setContextClassLoader( oldPortalClassLoader );
        }
    }
}
