package org.riverock.webmill.portal;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.Cookie;

/**
 * User: SergeMaslyukov
 * Date: 07.01.2005
 * Time: 17:58:10
 * $Id$
 */
public final class CookieManager {
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

    List<Cookie> getCookieList() {
        return list;
    }
}
