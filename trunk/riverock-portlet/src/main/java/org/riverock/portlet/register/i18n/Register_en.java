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
package org.riverock.portlet.register.i18n;

import java.util.Iterator;

import org.apache.log4j.Logger;

import org.riverock.webmill.container.resource.XmlResourceBundle;

/**
 * @author Serge Maslyukov
 *         Date: 01.12.2005
 *         Time: 15:12:25
 */
public class Register_en extends XmlResourceBundle {
    private final static Logger log = Logger.getLogger( Register_en.class );

    public void logError( String msg, Throwable th ) {
        log.error( msg, th );
    }
}