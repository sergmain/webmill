/*
 * org.riverock.portlet - Portlet Library
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
package org.riverock.portlet.tools;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import org.riverock.portlet.login.LogoutPortlet;
import org.riverock.common.tools.StringTools;

/**
 * @author Sergei Maslyukov
 *         Date: 15.05.2006
 *         Time: 13:13:30
 */
public class DebugUtils {
    private final static Logger log = Logger.getLogger( LogoutPortlet.class );

    public static void dumpMap(Map<String, List<String>> params, String info) {
        if (!log.isDebugEnabled()) {
            return;
        }
        log.debug(info);
        if (params ==null) {
            log.debug("    Map is null");
            return;
        }
        for (Map.Entry<String, List<String>> entry : params.entrySet()) {
            List<String> list = entry.getValue();
            log.debug("    key: " + entry.getKey());
            if (list==null) {
                log.debug("    list value is null");
                continue;
            }
            if (list.isEmpty()) {
                log.debug("    list value is empty");
                continue;
            }

            for (String anArr : list) {
                log.debug("        value: " + anArr);
            }
        }
    }

    public static void dumpAnyMap(Map<String, Object> params, String info) {
        if (!log.isDebugEnabled()) {
            return;
        }
        log.debug(info);
        if (params ==null || params.entrySet().isEmpty()) {
            log.debug("    Map is null");
            return;
        }
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            log.debug("    key: " + entry.getKey());
            Object o = entry.getValue();
            if (o instanceof List) {
                List list = (List)o;
                if (list.isEmpty()) {
                    log.debug("    list value is empty");
                    continue;
                }
                for (Object anArr : list) {
                    log.debug("        value: " + anArr);
                }
            }
            else if (o instanceof String) {
                log.debug("        value: " + (String)o);
            }
            else if(o instanceof String[]) {
                log.debug("        value: " + StringTools.arrayToString((String[])o));
            }
        }
    }
}
