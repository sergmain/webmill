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
package org.riverock.webmill.portal.dao;

import java.util.Map;
import java.util.List;

import org.riverock.interfaces.portal.xslt.XsltTransformer;
import org.riverock.interfaces.portal.bean.Xslt;
import org.riverock.generic.db.DatabaseAdapter;

/**
 * @author SergeMaslyukov
 *         Date: 30.01.2006
 *         Time: 1:24:19
 *         $Id$
 */
public interface InternalXsltDao {
    /**
     *  key is language of site
     *
     * @param siteId Long
     * @return Map<String, Xslt>
     */
    public Map<String, Xslt> getCurrentXsltForSiteAsMap(Long siteId);
    public Xslt getCurrentXslt(Long siteLanguageId);

    public Xslt getXslt(Long xsltId);
    public Xslt getXslt(String xsltName, Long siteLanguageId);
    public Long createXslt(Xslt xslt);

    public List<Xslt> getXsltList(Long siteLanguageId);

    public void deleteXsltForSite(DatabaseAdapter adapter, Long siteId);
    public void deleteXsltForSiteLanguage(DatabaseAdapter adapter, Long siteLanguageId);

    public void updateXslt(Xslt xslt);

    public void deleteXslt(Long xsltId);
}
