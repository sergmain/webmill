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
package org.riverock.interfaces.portal.bean;

import java.util.List;
import java.io.Serializable;

import org.riverock.interfaces.common.TreeItem;

/**
 * @author Sergei Maslyukov
 *         Date: 05.05.2006
 *         Time: 20:56:29
 */
public interface CatalogItem extends TreeItem, Serializable {
    public Long getCatalogId();

    public Long getTopCatalogId();

    public Long getPortletId();

    public Long getContextId();

    public Long getTemplateId();

    public Long getCatalogLanguageId();

    public Integer getOrderField();

    public String getKeyMessage();

    public String getUrl();

    public String getTitle();

    public String getAuthor();

    public String getKeyword();

    public String getMetadata();

    public String getPortletRole();

    public List<CatalogItem> getSubCatalogItemList();
}
