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
package org.riverock.webmill.portal.context;

import org.apache.log4j.Logger;

import org.riverock.interfaces.portlet.menu.MenuLanguage;

/**
 * $Id$
 */
public class IndexRequestContextProcessor implements RequestContextProcessor {
    private final static Logger log = Logger.getLogger(IndexRequestContextProcessor.class);

    public IndexRequestContextProcessor() {
    }

    public RequestContext parseRequest(RequestContextParameter factoryParameter) {

        // process current request as 'index'
        MenuLanguage menu = factoryParameter.getPortalInfo().getMenu(factoryParameter.getPredictedLocale().toString());
        if (menu==null){
            log.error( "Menu for locale: "+factoryParameter.getPredictedLocale().toString() +" not defined" );
            return new RequestContext();
        }

        if (menu.getIndexMenuItem() == null) {
            log.warn("menu: " + menu);
            log.warn("locale: " + factoryParameter.getPredictedLocale().toString());
            log.warn("Menu item pointed to 'index' portlet not defined");
            return new RequestContext();
        }

        Long ctxId = menu.getIndexMenuItem().getId();
        if (ctxId==null) {
            String es = "Menu item with 'index' portlet not found";
            log.error(es);
            return new RequestContext();
        }

        return RequestContextUtils.getRequestContextBean(factoryParameter, ctxId);
    }
}
