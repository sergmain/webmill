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
package org.riverock.interfaces.portal;

import java.util.Locale;
import java.util.Map;
import java.io.Serializable;

import org.riverock.interfaces.portlet.menu.MenuLanguage;
import org.riverock.interfaces.portal.xslt.XsltTransformerManager;
import org.riverock.interfaces.portal.template.PortalTemplateManager;
import org.riverock.interfaces.portal.bean.Site;

/**
 * @author smaslyukov
 *         Date: 29.07.2005
 *         Time: 21:04:22
 *         $Id$
 */
public interface PortalInfo extends Serializable {
    public Long getSiteId();
    public Long getCompanyId();
    public Long getSupportLanguageId( Locale locale );
    public MenuLanguage getMenu(String locale);
    public XsltTransformerManager getXsltTransformerManager();
    public Map<String, String> getPortalProperties();
    public Locale getDefaultLocale();
    public PortalTemplateManager getPortalTemplateManager();
    public Site getSite();
}

