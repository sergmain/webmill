/*
 * org.riverock.interfaces -- Common classes and interafces shared between projects
 *
 * Copyright (C) 2004, Riverock Software, All Rights Reserved.
 *
 * Riverock -- The Open-source Java Development Community
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
 *
 */
package org.riverock.interfaces.portal.bean;

import java.util.List;

import org.riverock.interfaces.common.TreeItem;

/**
 * @author Sergei Maslyukov
 *         Date: 05.05.2006
 *         Time: 20:56:29
 */
public interface CatalogItem extends TreeItem {
    public Long getCatalogId();

    public Long getTopCatalogId();

    public Long getPortletId();

    public Long getContextId();

    public Boolean getUseProperties();

    public Long getTemplateId();

    public Long getCatalogLanguageId();

    public Integer getOrderField();

    public String getStorage();

    public String getKeyMessage();

    public String getUrl();

    public String getTitle();

    public String getAuthor();

    public String getKeyword();

    public String getMetadata();

    public String getPortletRole();

    public List<CatalogItem> getSubCatalogItemList();
}
