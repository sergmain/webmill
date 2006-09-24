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
package org.riverock.portlet.webclip;

/**
 * @author Sergei Maslyukov
 *         Date: 11.09.2006
 *         Time: 22:07:04
 *         <p/>
 *         $Id$
 */
public interface WebclipUrlProducer {
    
    /**
     * set current href
     *
     * @param value current href
     */
    void setCurrentHrefValue(String value);

    /**
     * set new parameter
     *
     * @param name  String paramete name r
     * @param value String parameter value
     */
    void setParameter(String name, String value);

    /**
     * get new url
     *
     * @return String
     */
    String getUrl();

    /**
     * init mwethod
     */
    void init();
}
