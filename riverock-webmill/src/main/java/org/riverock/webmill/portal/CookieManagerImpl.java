/*
 * org.riverock.webmill - Webmill portal with support jsr-168, xml/xslt and others things.
 * For more information, please visit project site http://webmill.riverock.org
 *
 * Copyright (C) 2000-2006, Riverock Software, All Rights Reserved.
 *
 * Riverock - The Open-source Java Development Community
 * http://www.riverock.org
 *
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
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
    private ClassLoader classLoader=null;

    protected void finalize() throws Throwable {
        if (list!=null) {
            list.clear();
            list = null;
        }
        classLoader = null;
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
