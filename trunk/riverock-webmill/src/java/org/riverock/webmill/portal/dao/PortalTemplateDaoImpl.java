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

import org.riverock.interfaces.portal.dao.PortalTemplateDao;
import org.riverock.interfaces.portal.bean.Template;
import org.riverock.interfaces.sso.a3.AuthSession;

/**
 * @author Sergei Maslyukov
 *         Date: 17.05.2006
 *         Time: 14:12:05
 */
public class PortalTemplateDaoImpl implements PortalTemplateDao {
    private AuthSession authSession = null;

    PortalTemplateDaoImpl(AuthSession authSession) {
        this.authSession = authSession;
    }

    public Template getTemplate(Long templateId) {
        return InternalDaoFactory.getInternalTemplateDao().getTemplate(templateId);
    }

    public Template getTemplate(String templateName, Long siteLanguageId) {
        return InternalDaoFactory.getInternalTemplateDao().getTemplate(siteLanguageId);
    }

    public Template getDefaultDynamicTemplate(Long siteLanguageId) {
        return InternalDaoFactory.getInternalTemplateDao().getDefaultDynamicTemplate(siteLanguageId);
    }

    public List<Template> getTemplateLanguageList(Long siteLanguageId) {
        return InternalDaoFactory.getInternalTemplateDao().getTemplateLanguageList(siteLanguageId);
    }

    public List<Template> getTemplateList(Long siteId) {
        return InternalDaoFactory.getInternalTemplateDao().getTemplateList(siteId);
    }

    public Long createTemplate(Template template) {
        return InternalDaoFactory.getInternalTemplateDao().createTemplate(template);
    }

    public void updateTemplate(Template template) {
        InternalDaoFactory.getInternalTemplateDao().updateTemplate(template);
    }

    public void deleteTemplate(Long templateId) {
        InternalDaoFactory.getInternalTemplateDao().deleteTemplate(templateId);
    }
}
