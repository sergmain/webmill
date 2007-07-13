package org.riverock.interfaces.portal.search;

import java.util.List;
import java.io.Serializable;

/**
 * User: SMaslyukov
 * Date: 11.07.2007
 * Time: 14:51:24
 */
public interface PortalSearchResult extends Serializable {

    List<PortalSearchResultItem> getResultItems();
}
