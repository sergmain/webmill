/*
 * org.riverock.interfaces - Common classes and interafces shared between projects
 *
 * Copyright (C) 2006, Riverock Software, All Rights Reserved.
 *
 * Riverock - The Open-source Java Development Community
 * http://www.riverock.org
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
package org.riverock.interfaces.portal.dao;

import java.util.Map;
import java.util.List;

import org.riverock.interfaces.portal.bean.Xslt;

/**
 * @author Sergei Maslyukov
 *         Date: 17.05.2006
 *         Time: 13:56:02
 */
public interface PortalXsltDao {
    public StringBuilder getXsltData( Long xsltId );

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

