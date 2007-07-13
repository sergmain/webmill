package org.riverock.interfaces.portal.search;

import java.io.Serializable;

/**
 * User: SMaslyukov
 * Date: 13.07.2007
 * Time: 16:30:34
 */
public interface PortalSearchParameter extends Serializable {
    
    String getQuery();
    
    Integer getResultPerPage();

    /**
     * Counter of page starts from 0. I.e. 0 - 1st page, 1 - 2nd page ...
     *
     * @return Start page. If null - start page equals 0; 
     */
    Integer getStartPage();
}
