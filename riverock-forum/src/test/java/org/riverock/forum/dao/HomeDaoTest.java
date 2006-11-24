/*
 * org.riverock.generic - Database connectivity classes, part of Webmill portal
 * For more information about Webmill portal, please visit project site
 * http://webmill.askmore.info
 *
 * Copyright (C) 2000-2006, Riverock Software, All Rights Reserved.
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
package org.riverock.forum.dao;

import org.riverock.module.web.url.UrlProvider;
import org.riverock.module.exception.ActionException;
import org.riverock.forum.bean.ForumBean;
import org.riverock.generic.startup.StartupApplication;

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
