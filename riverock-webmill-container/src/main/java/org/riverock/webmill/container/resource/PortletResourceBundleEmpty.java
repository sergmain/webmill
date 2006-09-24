/*
 * org.riverock.webmill.container - Webmill portlet container implementation
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
package org.riverock.webmill.container.resource;

import java.util.ResourceBundle;
import java.util.Enumeration;
import java.util.Collections;
import java.util.ArrayList;

/**
 * User: SergeMaslyukov
 * Date: 10.02.2005
 * Time: 0:24:14
 * $Id$
 */
public class PortletResourceBundleEmpty extends ResourceBundle {

    public PortletResourceBundleEmpty() {
    }

    public Enumeration<String> getKeys() {
        return Collections.enumeration( new ArrayList<String>() );
    }

    protected Object handleGetObject( final String key ) {
        return null;
    }
}
