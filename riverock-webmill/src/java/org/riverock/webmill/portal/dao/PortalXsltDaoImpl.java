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

import java.util.Map;
import java.util.List;

import org.riverock.interfaces.portal.dao.PortalXsltDao;
import org.riverock.interfaces.portal.bean.Xslt;
import org.riverock.interfaces.sso.a3.AuthSession;

/**
 * @author Sergei Maslyukov
 *         Date: 17.05.2006
 *         Time: 14:17:22
 */
public class PortalXsltDaoImpl implements PortalXsltDao {
    private AuthSession authSession = null;

    PortalXsltDaoImpl(AuthSession authSession) {
        this.authSession = authSession;
    }

    public StringBuilder getXsltData(Long xsltId) {
        return InternalDaoFactory.getInternalXsltDao().getXsltData(xsltId);
    }

    /**
     * key is language of site
     */
    public Map<String, Xslt> getCurrentXsltForSiteAsMap(Long siteId) {
        return InternalDaoFactory.getInternalXsltDao().getCurrentXsltForSiteAsMap(siteId);
    }

    public Xslt getCurrentXslt(Long siteLanguageId) {
        return InternalDaoFactory.getInternalXsltDao().getCurrentXslt(siteLanguageId);
    }

    public Xslt getXslt(Long xsltId) {
        return InternalDaoFactory.getInternalXsltDao().getXslt(xsltId);
    }

    public Xslt getXslt(String xsltName, Long siteLanguageId) {
        return InternalDaoFactory.getInternalXsltDao().getXslt(xsltName, siteLanguageId);
    }

    public Long createXslt(Xslt xslt) {
        return InternalDaoFactory.getInternalXsltDao().createXslt(xslt);
    }

    public void updateXslt(Xslt xslt) {
        InternalDaoFactory.getInternalXsltDao().updateXslt(xslt);
    }

    public void deleteXslt(Long xsltId) {
        InternalDaoFactory.getInternalXsltDao().deleteXslt(xsltId);
    }

    public List<Xslt> getXsltList(Long siteLanguageId) {
        return InternalDaoFactory.getInternalXsltDao().getXsltList(siteLanguageId);
    }
}
