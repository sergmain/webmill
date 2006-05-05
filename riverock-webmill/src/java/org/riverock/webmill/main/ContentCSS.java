/*
 * org.riverock.webmill -- Portal framework implementation
 * 
 * Copyright (C) 2004, Riverock Software, All Rights Reserved.
 * 
 * Riverock -- The Open-source Java Development Community
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
 *
 */
package org.riverock.webmill.main;

import java.util.Date;

import org.apache.commons.lang.StringUtils;

import org.riverock.generic.main.CacheFactory;
import org.riverock.interfaces.portal.bean.Css;
import org.riverock.webmill.portal.dao.InternalDaoFactory;

/**
 * @author Serge Maslyukov
 *
 * $Id$
 */
public final class ContentCSS {
    private Css css;

    private static CacheFactory cache = new CacheFactory(ContentCSS.class.getName());

    protected void finalize() throws Throwable {
        css = null;
        super.finalize();
    }

    public ContentCSS() {
    }

    public void terminate(Long id) {
        cache.reinit();
    }

    public void reinit() {
        cache.reinit();
    }

    public static ContentCSS getInstance(Long id__)
        throws Exception {
        if (id__ == null)
            return null;

        return (ContentCSS) cache.getInstanceNew(id__);
    }

    public ContentCSS(Long siteId) throws Exception {
        if (siteId == null)
            return;

        css = InternalDaoFactory.getInternalDao().getCss( siteId );
        if (css==null)
            css = new CssBean();
    }

    public String getCss() {
        return css.getCss();
    }

    public Date getDatePost() {
        return css.getDate();
    }

//    public void setCss(String css) {
//        this.css.setCss( css );
//    }

//    public void setDatePost(Date datePost) {
//        this.css.setDate( datePost );
//    }

    public boolean getIsEmpty() {
        return StringUtils.isEmpty(css.getCss());
    }
}