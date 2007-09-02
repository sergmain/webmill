package org.riverock.interfaces.portal.spi;

import java.util.List;
import java.util.Map;

import org.riverock.interfaces.portal.dao.PortalPreferencesDao;

/**
 * User: SergeMaslyukov
 * Date: 02.09.2007
 * Time: 12:48:53
 * $Id$
 */
public interface PortalPreferencesSpi extends PortalPreferencesDao {
    Map<String, List<String>> load(Long catalogId);
    Map<String, List<String>> initMetadata( String metadata );
}
