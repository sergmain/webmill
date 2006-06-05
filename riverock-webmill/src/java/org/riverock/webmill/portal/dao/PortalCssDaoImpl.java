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
package org.riverock.webmill.portal.dao;

import java.util.List;

import org.riverock.interfaces.portal.dao.PortalCssDao;
import org.riverock.interfaces.portal.bean.Css;
import org.riverock.interfaces.sso.a3.AuthSession;

/**
 * @author Sergei Maslyukov
 *         Date: 18.05.2006
 *         Time: 13:32:44
 */
public class PortalCssDaoImpl implements PortalCssDao {
    private AuthSession authSession = null;

    PortalCssDaoImpl(AuthSession authSession) {
        this.authSession = authSession;
    }

    public Css getCssCurrent(Long siteId) {
        return InternalDaoFactory.getInternalCssDao().getCssCurrent(siteId);
    }

    public Css getCss(Long cssId) {
        return InternalDaoFactory.getInternalCssDao().getCss(cssId);
    }

    public Long createCss(Css css) {
        return InternalDaoFactory.getInternalCssDao().createCss(css);
    }

    public void updateCss(Css css) {
        InternalDaoFactory.getInternalCssDao().updateCss(css);
    }

    public void deleteCss(Long cssId) {
        InternalDaoFactory.getInternalCssDao().deleteCss(cssId);
    }

    public List<Css> getCssList(Long siteId) {
        return InternalDaoFactory.getInternalCssDao().getCssList(siteId);
    }
}
