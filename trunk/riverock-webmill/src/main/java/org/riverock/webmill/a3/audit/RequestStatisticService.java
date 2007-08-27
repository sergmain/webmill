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
package org.riverock.webmill.a3.audit;

import java.util.concurrent.ConcurrentMap;
import java.sql.Timestamp;

import org.apache.log4j.Logger;

import org.riverock.webmill.portal.dao.InternalDaoFactory;
import org.riverock.webmill.portal.dao.InternalDao;
import org.riverock.webmill.a3.bean.RequestStatisticBean;
import org.riverock.common.tools.StringTools;

/**
 * @author SergeMaslyukov
 *         Date: 09.02.2006
 *         Time: 23:32:57
 *         $Id$
 */
public class RequestStatisticService {
    private final static Logger log = Logger.getLogger(RequestStatisticService.class);

    private static RequestStatisticService service = null;

    private static final int SIZE_REFER = 200;
    private static final int SIZE_PARAMETERS = 200;

    private ConcurrentMap<String, Long> userAgent = null;
    private ConcurrentMap<String, Long> url = null;

    private RequestStatisticService() {
        InternalDao internalDao = InternalDaoFactory.getInternalDao();
        this.userAgent = internalDao.getUserAgentList();
        this.url = internalDao.getUrlList();
    }

    public void destroy() {
        if (userAgent!=null) {
            userAgent.clear();
            userAgent = null;
        }
        if (url!=null) {
            url.clear();
            url = null;
        }
    }

    public static void destroyService() {
        if (service!=null) {
            service.destroy();
        }
    }

    public static RequestStatisticService getInstance() {
        if (service!=null) {
            return service;
        }
        synchronized (RequestStatisticService.class) {
            if (service!=null) {
                return service;
            }
            RequestStatisticService requestStatisticService = new RequestStatisticService();
            service = requestStatisticService;
        }
        return service;
    }

    public void process(String userAgentString, String uri, String referer, String queryString, String serverName) {

        RequestStatisticBean bean = new RequestStatisticBean();
        bean.setAccessDate( new Timestamp(System.currentTimeMillis()) );
        bean.setServerName( serverName );

        if (userAgentString == null) {
            userAgentString = "UserAgent unknown";
        }
        else if (userAgentString.length() < 5) {
            userAgentString = "UserAgent too small";
        }
        else {
            userAgentString = StringTools.truncateString(userAgentString, 150);
        }

        bean.setUserAgent( userAgentString );
        bean.setUrl( uri );

        if (referer == null) {
            referer = "";
        }
        int lenRefer = StringTools.lengthUTF(referer);
        if (lenRefer > SIZE_REFER) {
            lenRefer = SIZE_REFER;
            bean.setReferTooBig(true);
        }
        else {
            bean.setReferTooBig(false);
        }

        bean.setRefer( new String(StringTools.getBytesUTF(referer), 0, lenRefer) );

        int lenParams = StringTools.lengthUTF(queryString);
        if (lenParams > SIZE_PARAMETERS) {
            lenParams = SIZE_PARAMETERS;
            bean.setParamTooBig(true);
        }
        else {
            bean.setParamTooBig(false);
        }

        bean.setParameters( new String(StringTools.getBytesUTF(queryString), 0, lenParams) );

        InternalDaoFactory.getInternalDao().saveRequestStatistic( userAgent, url, bean );
    }
}
