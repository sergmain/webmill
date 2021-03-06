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
package org.riverock.interfaces.portlet.menu;

import java.util.List;
import java.util.Map;
import java.io.Serializable;

import org.riverock.interfaces.common.TreeItem;

/**
 * User: serg_main
 * Date: 07.10.2004
 * Time: 14:45:47
 * @author Serge Maslyukov
 * $Id: MenuItem.java 1043 2006-11-14 14:29:22Z serg_main $
 */
public interface MenuItem extends TreeItem, Serializable {
    public Long getId();
    public Long getIdTop();
    public Long getIdPortlet();
    public Long getIdTemplate();
    public String getNameTemplate();
    public Long getIdType();
    public String getType();
    public String getMenuName();
    public List<MenuItem> getCatalogItems();
    public String getUrl();
    public String getPortletRole();
    public Map<String, String> getMetadata();
}
