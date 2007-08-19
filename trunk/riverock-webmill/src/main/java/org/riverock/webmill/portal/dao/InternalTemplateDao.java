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
package org.riverock.webmill.portal.dao;

import java.util.List;

import org.riverock.webmill.portal.bean.TemplateBean;

/**
 * @author Sergei Maslyukov
 *         Date: 03.05.2006
 *         Time: 17:51:14
 */
public interface InternalTemplateDao {
    public TemplateBean getTemplate(Long templateId);
    public TemplateBean getTemplate(String templateName, Long siteLanguageId);
    /**
     * Attention! template data not initalized
     *
     * @param siteId site  ID
     * @param templateName String
     * @param lang String
     * @return org.riverock.interfaces.portal.bean.Template - Attention! template data not initialized
     */
    public TemplateBean getTemplate(Long siteId, String templateName, String lang);

    public List<TemplateBean> getTemplateLanguageList( Long siteLanguageId );
    public List<TemplateBean> getTemplateList( Long siteId );

    public Long createTemplate(TemplateBean template);

    public void deleteTemplate(Long templateId);
    public void deleteTemplateForSite(Long siteId);
    public void deleteTemplateForSiteLanguage(Long siteLanguageId);

    public void updateTemplate(TemplateBean template);

    void setDefaultDynamic(Long templateId);
    public TemplateBean getDefaultDynamicTemplate(Long siteLanguageId);

    public TemplateBean getMaximizedTemplate(Long siteLanguageId);
    void setMaximizedTemplate(Long templateId);

    public TemplateBean getPopupTemplate(Long siteLanguageId);
    void setPopupTemplate(Long templateId);
}
