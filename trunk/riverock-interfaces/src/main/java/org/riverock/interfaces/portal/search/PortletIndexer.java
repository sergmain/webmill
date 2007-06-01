package org.riverock.interfaces.portal.search;

import java.io.Serializable;
import java.util.Iterator;

/**
 * User: SMaslyukov
 * Date: 01.06.2007
 * Time: 17:32:45
 */
public interface PortletIndexer extends Serializable, Iterator<Long> {

    ClassLoader getClassLoader();

    Object getId();

    Long getSiteId();

    void init(Object id, Long siteId, ClassLoader classLoader);

    long getNotIndexedCount();

    long getTotal();

    String getContent(Long objectId);

    void markAsIndexed(Long objectId);

    void markAllForIndexing();

}
