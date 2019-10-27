package org.riverock.interfaces.portal.search;

import java.io.Serializable;

/**
 * User: SMaslyukov
 * Date: 11.07.2007
 * Time: 14:51:36
 */
public interface PortalSearchResultItem extends Serializable {
    String getUrl();

    String getTitle();
    
    String getDescription();

    float getWeight();
}
