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

/**
 * User: smaslyukov
 * Date: 09.08.2004
 * Time: 19:58:16
 * $Id$
 */
public final class SiteUtils {

    public static final String JAVA_IO_TMPDIR = "java.io.tmpdir";

    public static String getTempDir() {
        return System.getProperty( JAVA_IO_TMPDIR );
    }

}
