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
    private Css css=null;

    private static CacheFactory cache = new CacheFactory(ContentCSS.class);

    public void destroy() {
        this.css=null;
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

        css = InternalDaoFactory.getInternalCssDao().getCssCurrent( siteId );
        if (css==null)
            css = new CssBean();
    }

    public String getCss() {
        return css.getCss();
    }

    public Date getDatePost() {
        return css.getDate();
    }

    public boolean getIsEmpty() {
        return StringUtils.isBlank(css.getCss());
    }
}