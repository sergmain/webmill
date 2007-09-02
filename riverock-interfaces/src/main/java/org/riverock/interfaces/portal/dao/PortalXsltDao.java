/*
 * org.riverock.interfaces - Common classes and interafces shared between projects
 *
 * Copyright (C) 2006, Riverock Software, All Rights Reserved.
 *
 * Riverock - The Open-source Java Development Community
 * http://www.riverock.org
 *
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package org.riverock.interfaces.portal.dao;

import java.util.Map;
import java.util.List;

import org.riverock.interfaces.portal.bean.Xslt;
import org.riverock.interfaces.portal.spi.PortalXsltSpi;

/**
 * @deprecated use org.riverock.interfaces.portal.spi.PortalXsltSpi
 * @author Sergei Maslyukov
 *         Date: 17.05.2006
 *         Time: 13:56:02
 */
public interface PortalXsltDao {
    /** key is language of site */
    public Map<String, Xslt> getCurrentXsltForSiteAsMap(Long siteId);
    public Xslt getCurrentXslt(Long siteLanguageId);

    public Xslt getXslt(Long xsltId);
    public Xslt getXslt(String xsltName, Long siteLanguageId);
    public List<Xslt> getXsltList(Long siteLanguageId);

    public Long createXslt(Xslt xslt);
    public void updateXslt(Xslt xslt);
    public void deleteXslt(Long xsltId);
}

