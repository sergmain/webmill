package org.riverock.interfaces.portal.dao;

import java.util.List;
import java.util.Map;

import org.riverock.interfaces.portal.spi.PortalPreferencesSpi;

/**
 * @deprecated use org.riverock.interfaces.portal.spi.PortalPreferencesSpi
 * User: SMaslyukov
 * Date: 11.05.2007
 * Time: 20:26:01
 */
public interface PortalPreferencesDao {
    Map<String, List<String>> load(Long catalogId);
    Map<String, List<String>> initMetadata( String metadata );
}
