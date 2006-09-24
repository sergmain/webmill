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
package org.riverock.webmill.test;

import org.apache.log4j.Logger;

import org.riverock.common.resource.CustomXmlResourceBundle;

/**
 * @author smaslyukov
 *         Date: 10.08.2005
 *         Time: 18:29:04
 *         $Id$
 */
public class SampleXmlResourceBundleTest extends CustomXmlResourceBundle {
    private final static Logger log = Logger.getLogger( SampleXmlResourceBundleTest.class );

    public void logError(String msg, Throwable th) {
        log.error( msg, th );
    }
}
