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

