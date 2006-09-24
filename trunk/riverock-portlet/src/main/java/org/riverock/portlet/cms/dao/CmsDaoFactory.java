/*
 * org.riverock.portlet - Portlet Library
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
package org.riverock.portlet.cms.dao;

/**
 * @author Sergei Maslyukov
 *         Date: 23.08.2006
 *         Time: 16:27:57
 */
public class CmsDaoFactory {
    private final static CmsNewsDao cmsNewsDao = new CmsNewsDaoImpl();
    private final static CmsArticleDao cmsArticleDao = new CmsArticleDaoImpl();

    public static CmsNewsDao getCmsNewsDao() {
        return cmsNewsDao;
    }

    public static CmsArticleDao getCmsArticleDao() {
        return cmsArticleDao;
    }
}