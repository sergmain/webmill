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

    public PortalIndexerImpl(PortletContainer container) {
        this.container = container;
    }

    public List<PortletIndexerShort> getPortletIndexers(Long siteId) {
        List<PortletName> names = InternalDaoFactory.getInternalPortletNameDao().getPortletNameList();
        List<PortletIndexerShort> shorts = new ArrayList<PortletIndexerShort>();

        for (PortletName name : names) {
            PortletEntry portletEntry;
            try {
                portletEntry = container.getPortletInstance(name.getPortletName());
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
                shorts.add( new PortletIndexerShortImpl(name.getPortletId(), name.getPortletName()) );
            }
        }
        return shorts;
    }

    public PortletIndexer getPortletIndexer(Long siteId, Object portletIndexerId) {
        List<PortletIndexerShort> list = getPortletIndexers(siteId);
        for (PortletIndexerShort portletIndexerShort : list) {
            if (portletIndexerShort.getId().equals(portletIndexerId)) {
                try {
                    PortletEntry portletEntry = container.getPortletInstance(portletIndexerShort.getPortletName());
                    if (portletEntry==null || portletEntry.getPortletDefinition()==null) {
                        continue;
                    }
                    String className = PortletService.getStringParam(
                        portletEntry.getPortletDefinition(), ContainerConstants.WEBMILL_PORTLET_INDEXER_CLASS_NAME
                    );
                    if (className==null) {
                        return null;
                    }
                    ClassLoader oldLoader = Thread.currentThread().getContextClassLoader();
                    try {
                        Thread.currentThread().setContextClassLoader( portletEntry.getClassLoader() );
                        Class clazz = Class.forName(className, true, portletEntry.getClassLoader());
                        PortletIndexer portletIndexer = (PortletIndexer)clazz.newInstance();
                        portletIndexer.init(portletIndexerId, siteId, portletEntry.getClassLoader() );
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

    public void markAllForIndexing(Long siteId) {
        List<PortletIndexerShort> list = getPortletIndexers(siteId);
        for (PortletIndexerShort portletIndexerShort : list) {
            try {
                PortletEntry portletEntry = container.getPortletInstance(portletIndexerShort.getPortletName());
                if (portletEntry==null || portletEntry.getPortletDefinition()==null) {
                    continue;
                }
                String className = PortletService.getStringParam(
                    portletEntry.getPortletDefinition(), ContainerConstants.WEBMILL_PORTLET_INDEXER_CLASS_NAME
                );
                if (className==null) {
                    continue;
                }
                ClassLoader oldLoader = Thread.currentThread().getContextClassLoader();
                try {
                    Thread.currentThread().setContextClassLoader( portletEntry.getClassLoader() );
                    Class clazz = Class.forName(className, true, portletEntry.getClassLoader());
                    PortletIndexer portletIndexer = (PortletIndexer)clazz.newInstance();
                    portletIndexer.init(portletIndexerShort.getId(), siteId, portletEntry.getClassLoader() );
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
}
