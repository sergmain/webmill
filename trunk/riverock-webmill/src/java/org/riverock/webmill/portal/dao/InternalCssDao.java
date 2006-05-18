package org.riverock.webmill.portal.dao;

import java.util.List;

import org.riverock.interfaces.portal.bean.Css;

/**
 * @author Sergei Maslyukov
 *         Date: 18.05.2006
 *         Time: 13:34:07
 */
public interface InternalCssDao {
    public Css getCss(Long cssId);
    public Long createCss(Css css);
    public List<Css> getCssList(Long siteId);
}
