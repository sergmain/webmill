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
package org.riverock.webmill.xslt;

import java.util.Map;
import java.util.HashMap;

import org.apache.log4j.Logger;

import org.riverock.interfaces.portal.xslt.XsltTransformer;
import org.riverock.interfaces.portal.bean.Xslt;
import org.riverock.interfaces.portal.bean.SiteLanguage;
import org.riverock.webmill.portal.dao.InternalDaoFactory;
import org.riverock.webmill.XsltTransformerManager;

/**
 * @author Sergei Maslyukov
 *         Date: 07.11.2006
 *         Time: 20:05:34
 *         <p/>
 * 
 * $Id: PortalFrontController.java 1370 2007-08-28 13:53:47Z serg_main $
 */
public class XsltTransformerManagerImpl implements XsltTransformerManager {
    private final static Logger log = Logger.getLogger(XsltTransformerManagerImpl.class);

    private static class Transformation {
        private XsltTransformer transformer;
        private Xslt xslt;
    }

    private Long siteId;
    private Map<String, Transformation> map = new HashMap<String, Transformation>();

    public void destroy() {
        if (map!=null) {
            map.clear();
            map=null;
        }
        siteId = null;
    }

    public XsltTransformerManagerImpl(Long siteId) {
        this.siteId = siteId;
    }

    public XsltTransformer getXslt(String lang) {
        Transformation tr = map.get(lang);
        if (tr==null) {
            SiteLanguage siteLanguage = InternalDaoFactory.getInternalSiteLanguageDao().getSiteLanguage(siteId, lang);
            Xslt currentXslt = InternalDaoFactory.getInternalXsltDao().getCurrentXslt(siteLanguage.getSiteLanguageId());
            if (currentXslt==null) {
                return null;
            }
            tr = new Transformation();
            tr.transformer = new PortalXslt( currentXslt );
            tr.xslt = currentXslt;
            map.put(lang, tr);
        }
        else {
            Xslt currentXslt = InternalDaoFactory.getInternalXsltDao().getXslt(tr.xslt.getId());
            if (tr.xslt.getVersion()==currentXslt.getVersion()) {
                return tr.transformer;
            }
            else if (tr.xslt.getVersion()<currentXslt.getVersion()) {
                SiteLanguage siteLanguage = InternalDaoFactory.getInternalSiteLanguageDao().getSiteLanguage(siteId, lang);
                currentXslt = InternalDaoFactory.getInternalXsltDao().getCurrentXslt(siteLanguage.getSiteLanguageId());
                tr = new Transformation();
                tr.transformer = new PortalXslt( currentXslt );
                tr.xslt = currentXslt;
                map.put(lang, tr);
            }
            else {
                log.error("runtime xslt: " + tr.xslt+", db xslt: " + currentXslt);
                throw new RuntimeException("Illegal state. Version in DB less than in runtime.");
            }
        }
        return tr.transformer;
    }
}
