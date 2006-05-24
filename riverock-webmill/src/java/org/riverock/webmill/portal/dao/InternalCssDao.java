package org.riverock.webmill.portal.dao;

import java.util.List;

import org.riverock.interfaces.portal.bean.Css;
import org.riverock.generic.db.DatabaseAdapter;

/**
 * @author Sergei Maslyukov
 *         Date: 18.05.2006
 *         Time: 13:34:07
 */
public interface InternalCssDao {
    public Css getCssCurrent(Long siteId);
    public Long createCss(Css css);
    public List<Css> getCssList(Long siteId);

    public Css getCss(Long cssId);

    public void deleteCss(DatabaseAdapter adapter, Long siteId);
}
