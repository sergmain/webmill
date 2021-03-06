/*
 * org.riverock.webmill - Webmill portal with support jsr-168, xml/xslt and others things.
 * For more information, please visit project site http://webmill.riverock.org
 *
 * Copyright (C) 2000-2006, Riverock Software, All Rights Reserved.
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
package org.riverock.webmill.portal.dao;

import java.util.Collection;
import java.util.concurrent.ConcurrentMap;

import org.riverock.webmill.portal.bean.RequestStatisticBean;

/**
 * @author SergeMaslyukov
 *         Date: 27.01.2006
 *         Time: 0:59:51
 *         $Id: InternalDao.java 1352 2007-08-28 10:09:56Z serg_main $
 */
public interface InternalDao {
    public Collection<String> getSupportedLocales();
    public ConcurrentMap<String, Long> getUserAgentList();
    public ConcurrentMap<String, Long> getUrlList();
    public void saveRequestStatistic( ConcurrentMap<String, Long> userAgentList, ConcurrentMap<String, Long> urlList, RequestStatisticBean bean );
}
