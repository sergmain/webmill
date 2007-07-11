package org.riverock.interfaces.portal.search;

import java.util.Map;
import java.io.Serializable;

/**
 * User: SMaslyukov
 * Date: 11.07.2007
 * Time: 11:37:30
 */
public interface PortalIndexerParameter extends Serializable {

    String getTitle();

    String getDescription();

    byte[] getContent();

    Map<String, Object> getParameters();
}
