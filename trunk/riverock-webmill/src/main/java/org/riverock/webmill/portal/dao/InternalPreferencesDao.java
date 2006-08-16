package org.riverock.webmill.portal.dao;

import java.util.List;
import java.util.Map;

/**
 * User: SergeMaslyukov
 * Date: 16.08.2006
 * Time: 17:26:55
 * <p/>
 * $Id$
 */
public interface InternalPreferencesDao {
    void store(Map<String, List<String>> preferences, Long contextId);
}
