package org.riverock.webmill.portal.preference;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Collections;

/**
 * User: SMaslyukov
 * Date: 22.03.2007
 * Time: 16:27:15
 */
public class StubPortletPreferencePersistencerImpl implements PortletPreferencePersistencer {

    private static final Map<String,List<String>> HASH_MAP = Collections.unmodifiableMap(new HashMap<String, List<String>>());

    public Map<String, List<String>> load() {
        return HASH_MAP;
    }

    public void store(Map<String, List<String>> preferences) {
    }
}
