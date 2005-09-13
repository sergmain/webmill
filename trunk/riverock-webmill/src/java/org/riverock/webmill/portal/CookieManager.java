package org.riverock.webmill.portal;

import java.util.List;
import java.util.LinkedList;

import javax.servlet.http.Cookie;

/**
 * User: SergeMaslyukov
 * Date: 07.01.2005
 * Time: 17:58:10
 * $Id$
 */
public final class CookieManager {
    private List list = new LinkedList();

    protected void finalize() throws Throwable {
        list.clear();
        list = null;
        super.finalize();
    }

    public void addCookie( Cookie cookie ) {
        if ( cookie!=null )
            list.add( cookie );
    }

    List getCookieList() {
        return list;
    }
}
