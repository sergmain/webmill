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
package org.riverock.webmill.portal;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

import javax.servlet.http.Cookie;

import org.riverock.interfaces.portal.CookieManager;

/**
 * User: SergeMaslyukov
 * Date: 07.01.2005
 * Time: 17:58:10
 * $Id$
 */
public final class CookieManagerImpl implements CookieManager {
    private List<Cookie> list = new ArrayList<Cookie>();

    protected void finalize() throws Throwable {
        if (list!=null) {
            list.clear();
            list = null;
        }
        super.finalize();
    }

    public void addCookie( Cookie cookie ) {
        if ( cookie!=null )
            list.add( cookie );
    }

    public List<Cookie> getCookieList() {
        return Collections.unmodifiableList(list);
    }
}
