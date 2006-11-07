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
package org.riverock.webmill.port;

import java.util.Map;
import java.util.HashMap;

import org.apache.log4j.Logger;

import org.riverock.interfaces.portal.xslt.XsltTransformerManager;
import org.riverock.interfaces.portal.xslt.XsltTransformer;
import org.riverock.interfaces.portal.bean.Xslt;
import org.riverock.interfaces.portal.bean.SiteLanguage;
import org.riverock.webmill.portal.dao.InternalDaoFactory;

/**
 * @author Sergei Maslyukov
 *         Date: 07.11.2006
 *         Time: 20:05:34
 *         <p/>
 *         $Id$
 */
public class XsltTransformerManagerImpl implements XsltTransformerManager {
    private transient final static Logger log = Logger.getLogger(XsltTransformerManagerImpl.class);

    private static class Transformation {
        private XsltTransformer transformer;
        private Xslt xslt;
    }

    private Long siteId;
    private Map<String, Transformation> map = new HashMap<String, Transformation>();


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
