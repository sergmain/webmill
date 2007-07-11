package org.riverock.interfaces.portal.search;

import java.io.Serializable;

/**
 * User: SMaslyukov
 * Date: 11.07.2007
 * Time: 14:37:58
 */
public interface PortletIndexerContent extends Serializable {
    public static final int MAX_DESCRIPTION_LENGTH = 127;
    
    String getDescription();

    byte[] getContent();
}
