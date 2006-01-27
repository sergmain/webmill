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
        list.clear();
        list = null;
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
