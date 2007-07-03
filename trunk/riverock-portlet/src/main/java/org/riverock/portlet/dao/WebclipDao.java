/*
 * org.riverock.portlet - Portlet Library
 *
 * Copyright (C) 2006, Riverock Software, All Rights Reserved.
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
package org.riverock.portlet.dao;

import java.util.Date;
import java.util.List;

import org.riverock.portlet.webclip.WebclipBean;
import org.riverock.portlet.webclip.manager.bean.WebclipStatisticBean;

/**
 * User: SergeMaslyukov
 * Date: 10.09.2006
 * Time: 17:31:48
 * <p/>
 * $Id$
 */
public interface WebclipDao {

    WebclipBean getWebclip(Long siteId, Long webclipId);
    WebclipBean getWebclip(Long siteId, Long webclipId, boolean isInitWebclipData);

    List<Long> getAllForSite(Long siteId);

    Long createWebclip(Long siteId);

    void updateWebclip(WebclipBean webclip, String resultContent);

    void setOriginContent(WebclipBean webclip, byte[] bytes);

    void deleteWebclip(Long siteId, Long weblipId);

    void markAllForReload(Long siteId);

    void markAllForProcess(Long siteId);

    WebclipStatisticBean getStatistic(Long siteId);

    long getTotalCount(Long siteId);

    long getNotIndexedCount(Long siteId);

    void markAsIndexed(Long siteId, Long webclipId);

    void markAllForIndexing(Long siteId);

    WebclipBean getFirstForIndexing(Long siteId);
}
