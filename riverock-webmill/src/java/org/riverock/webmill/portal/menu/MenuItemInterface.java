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

package org.riverock.webmill.portal.menu;

import java.util.List;

import org.riverock.generic.port.LocalizedString;
import org.riverock.common.collections.TreeItemInterface;

/**
 * User: serg_main
 * Date: 04.02.2004
 * Time: 19:19:38
 * @author Serge Maslyukov
 * $Id$
 */
public interface MenuItemInterface extends TreeItemInterface
{
    Long getId();

    void setId(Long id);

    Long getIdTop();

    void setIdTop(Long id_top);

    Long getIdPortlet();

    void setIdPortlet(Long id_portlet);

    String getNameTemplate();

    void setNameTemplate(String nameTemplate);

    String getType();

    void setType(String type);

    LocalizedString getStr();

    void setStr(LocalizedString str);

    List getCatalogItems();

    void setCatalogItems(List catalogItems);

    String getUrl();

    void setUrl(String url);
}
