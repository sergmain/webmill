package org.riverock.portlet.cms.news.action;

import java.io.Serializable;

import org.apache.log4j.Logger;

import org.riverock.portlet.main.AuthSessionBean;
import org.riverock.portlet.cms.news.NewsSessionBean;
import org.riverock.portlet.cms.news.NewsDataProvider;

/**
 * @author Sergei Maslyukov
 *         Date: 23.08.2006
 *         Time: 15:47:16
 */
public class NewsSiteAction implements Serializable {
    private final static Logger log = Logger.getLogger( NewsSiteAction.class );
    private static final long serialVersionUID = 2057005511L;

    private NewsSessionBean newsSessionBean = null;
    private AuthSessionBean authSessionBean = null;
    private NewsDataProvider dataProvider = null;

    public NewsSiteAction() {
    }

    public void setDataProvider(NewsDataProvider dataProvider) {
        this.dataProvider = dataProvider;
    }

    public void setNewsSessionBean( NewsSessionBean siteSessionBean) {
        this.newsSessionBean = siteSessionBean;
    }

    public AuthSessionBean getAuthSessionBean() {
        return authSessionBean;
    }

    public void setAuthSessionBean( AuthSessionBean authSessionBean) {
        this.authSessionBean = authSessionBean;
    }

    public String selectSite() {
        if (log.isDebugEnabled()) {
            log.debug( "Select site action." );
            log.debug( "newsSessionBean type: " +newsSessionBean.getObjectType() );
        }
        newsSessionBean.setSiteExtended( dataProvider.getSiteExtended() );
        return "menu";
    }

    public String changeSite() {
        log.info( "Change site action." );
        return "news";
    }
}
