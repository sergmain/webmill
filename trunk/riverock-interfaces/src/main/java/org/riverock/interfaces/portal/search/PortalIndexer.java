package org.riverock.interfaces.portal.search;

import java.util.List;
import java.util.Iterator;
import java.io.Serializable;

/**
 * User: SMaslyukov
 * Date: 01.06.2007
 * Time: 17:32:17
 */
public interface PortalIndexer extends Serializable {

    List<PortletIndexerShort> getPortletIndexers(Long siteId);

    PortletIndexer getPortletIndexer(Long siteId, Object portletIndexerId);

    void markAllForIndexing(Long siteId);

    void indexContent(String url, PortalIndexerParameter parameter);
}
