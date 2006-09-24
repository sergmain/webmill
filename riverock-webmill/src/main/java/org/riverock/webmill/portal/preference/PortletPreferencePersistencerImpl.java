/*
 * org.riverock.webmill - Portal framework implementation
 *
 * Copyright (C) 2006, Riverock Software, All Rights Reserved.
 *
 * Riverock - The Open-source Java Development Community
 * http://www.riverock.org
 *
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.riverock.webmill.portal.preference;

import org.riverock.webmill.portal.dao.InternalDaoFactory;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.Map;

/**
 * @author Sergei Maslyukov
 *         Date: 14.08.2006
 *         Time: 20:43:07
 */
public class PortletPreferencePersistencerImpl implements PortletPreferencePersistencer {
    private final static Logger log = Logger.getLogger( PortletPreferencePersistencerImpl.class );

    private Long contextId=null;

    public PortletPreferencePersistencerImpl(Long contextId) {
        this.contextId = contextId;
    }

    public void store(Map<String, List<String>> preferences) {
        if (contextId!=null) {
            InternalDaoFactory.getInternalPreferencesDao().store(preferences, contextId);
        }
    }
}

