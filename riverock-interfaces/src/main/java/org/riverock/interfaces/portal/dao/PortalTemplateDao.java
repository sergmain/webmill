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

import java.util.List;

import org.riverock.interfaces.portal.bean.Template;

/**
 * @author Sergei Maslyukov
 *         Date: 17.05.2006
 *         Time: 13:55:30
 */
public interface PortalTemplateDao {
    public Template getTemplate(Long templateId);
    public Template getTemplate(String templateName, Long siteLanguageId);
    public Template getDefaultDynamicTemplate(Long siteLanguageId);
    public List<Template> getTemplateLanguageList( Long siteLanguageId );
    public List<Template> getTemplateList( Long siteId );

    public Long createTemplate(Template template);
    public void updateTemplate(Template template);
    public void deleteTemplate(Long templateId);
}
