package org.riverock.webmill.portal.dao;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

import org.riverock.interfaces.portal.bean.Css;
import org.riverock.webmill.a3.audit.RequestStatisticBean;

/**
 * @author SergeMaslyukov
 *         Date: 27.01.2006
 *         Time: 0:59:51
 *         $Id$
 */
public interface InternalDao {
    public Collection<String> getSupportedLocales();
    public ConcurrentMap<String, Long> getUserAgentList();
    public ConcurrentMap<String, Long> getUrlList();

    /**
     * in Map:<br/>
     * key - virtual host name<br/>
     * value - siteId
     */
    public Map<String, Long> getSiteIdMap();

    public Css getCss( Long siteId );

    public void saveRequestStatistic( ConcurrentMap<String, Long> userAgentList, ConcurrentMap<String, Long> urlList, RequestStatisticBean bean );

}
