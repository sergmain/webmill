/*
 * org.riverock.forum - Forum portlet
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
package org.riverock.forum.dao;

import org.riverock.module.web.url.UrlProvider;
import org.riverock.module.exception.ActionException;
import org.riverock.forum.bean.ForumBean;
import org.riverock.common.startup.StartupApplication;

/**
 * @author Sergei Maslyukov
 *         Date: 29.05.2006
 *         Time: 13:56:18
 */
public class HomeDaoTest {
    public static void main(String[] args) throws ActionException {
        StartupApplication.init();
        
        HomeDAO dao = new HomeDAO();
        UrlProvider urlProvider = new UrlProvider() {

            public String getUrl(String string, String string1) {
                return "url";
            }

            public StringBuilder getUrlStringBuilder(String string, String string1) {
                return new StringBuilder("url");
            }

            public void setParameter(String key, String value) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            public String getActionUrl() {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            public String getRenderUrl() {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }
        };
        ForumBean bean = dao.execute(urlProvider, 1L, true);

        bean = null;
    }
}
