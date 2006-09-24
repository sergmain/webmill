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
 * User: SergeMaslyukov
 * Date: 17.09.2006
 * Time: 20:40:49
 * <p/>
 * $Id$
 */
public class WebclipUrlProducerImpl implements WebclipUrlProducer {

    private String newHrefPrefix=null;
    private String hrefStartPart=null;
    private String href;

    public WebclipUrlProducerImpl(String newHrefPrefix, String hrefStartPart) {
        this.newHrefPrefix = newHrefPrefix;
        this.hrefStartPart = hrefStartPart;
    }

    /**
     * set current href
     *
     * @param value current href
     */
    public void setCurrentHrefValue(String value) {
        this.href=value;
    }

    /**
     * set new parameter
     *
     * @param name  String paramete name r
     * @param value String parameter value
     */
    public void setParameter(String name, String value) {
    }

    /**
     * get new url
     *
     * @return String
     */
    public String getUrl() {
        if (href==null) {
            return "";
        }

        if (hrefStartPart!=null && newHrefPrefix!=null && href.startsWith(hrefStartPart)) {
            return newHrefPrefix + href.substring(hrefStartPart.length());
        }
        return href;
    }

    /**
     * init mwethod
     */
    public void init() {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
