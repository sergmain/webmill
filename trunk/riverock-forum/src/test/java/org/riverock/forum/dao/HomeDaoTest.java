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
        };
        ForumBean bean = dao.execute(urlProvider, 1L, true);

        bean = null;
    }
}
