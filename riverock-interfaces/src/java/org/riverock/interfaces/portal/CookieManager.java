package org.riverock.interfaces.portal;

import java.util.List;

import javax.servlet.http.Cookie;

/**
 * @author SergeMaslyukov
 *         Date: 27.01.2006
 *         Time: 0:44:37
 *         $Id$
 */
public interface CookieManager {
    public void addCookie( Cookie cookie );
    public List<Cookie> getCookieList();}
