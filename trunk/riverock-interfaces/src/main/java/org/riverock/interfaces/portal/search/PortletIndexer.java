package org.riverock.interfaces.portal.search;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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

    PortletIndexerContent getContent(Long objectId, Map<String, List<String>> metadata);

    void markAsIndexed(Long objectId, Map<String, List<String>> metadata);

    void markAllForIndexing();

}
