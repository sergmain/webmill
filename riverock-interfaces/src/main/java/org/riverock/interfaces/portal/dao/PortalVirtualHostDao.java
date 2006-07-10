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
package org.riverock.interfaces.portal.dao;

import java.util.List;

import org.riverock.interfaces.portal.bean.VirtualHost;

/**
 * @author Sergei Maslyukov
 *         Date: 17.05.2006
 *         Time: 13:57:58
 */
public interface PortalVirtualHostDao {
    public List<VirtualHost> getVirtualHostsFullList();
    public List<VirtualHost> getVirtualHosts(Long siteId);
    public Long createVirtualHost(VirtualHost virtualHost);
}
