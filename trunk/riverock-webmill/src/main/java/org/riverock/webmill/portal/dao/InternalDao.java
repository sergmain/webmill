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
package org.riverock.webmill.portal.dao;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

import org.riverock.webmill.a3.audit.RequestStatisticBean;

/**
 * @author SergeMaslyukov
 *         Date: 27.01.2006
 *         Time: 0:59:51
 *         $Id$
 */
public interface InternalDao {
    public Collection<String> getSupportedLocales();
    public ConcurrentMap<String, Long> getUserAgentList();
    public ConcurrentMap<String, Long> getUrlList();

    /**
     * @return Map&lt;String, Long&gt;  key - virtual host name,  value - siteId
     */
    public Map<String, Long> getSiteIdMap();

    public void saveRequestStatistic( ConcurrentMap<String, Long> userAgentList, ConcurrentMap<String, Long> urlList, RequestStatisticBean bean );

}
