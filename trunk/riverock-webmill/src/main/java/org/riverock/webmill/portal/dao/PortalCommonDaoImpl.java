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

import java.util.List;

import org.riverock.interfaces.sso.a3.AuthSession;
import org.riverock.interfaces.portal.bean.Template;
import org.riverock.interfaces.portal.dao.PortalCommonDao;

/**
 * @author SergeMaslyukov
 *         Date: 30.01.2006
 *         Time: 23:37:53
 *         $Id$
 */
public class PortalCommonDaoImpl implements PortalCommonDao {
    private AuthSession authSession = null;

    PortalCommonDaoImpl(AuthSession authSession) {
        this.authSession = authSession;
    }
    public Template getTemplate(Long templateId) {
        return InternalDaoFactory.getInternalTemplateDao().getTemplate(templateId);
    }

    public List<Template> getTemplateList( Long siteId ) {
        return InternalDaoFactory.getInternalTemplateDao().getTemplateList(siteId);
    }

    public List<Template> getTemplateLanguageList(Long siteLanguageId) {
        return InternalDaoFactory.getInternalTemplateDao().getTemplateLanguageList(siteLanguageId);
    }

}
