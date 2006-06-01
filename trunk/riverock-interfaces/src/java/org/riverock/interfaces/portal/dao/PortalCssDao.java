package org.riverock.interfaces.portal.dao;

import java.util.List;

import org.riverock.interfaces.portal.bean.Css;

/**
 * @author Sergei Maslyukov
 *         Date: 18.05.2006
 *         Time: 13:30:08
 */
public interface PortalCssDao {
    public Css getCssCurrent(Long siteId);
    public Css getCss(Long cssId);
    public List<Css> getCssList(Long siteId);

    public Long createCss(Css css);
    public void updateCss(Css css);
    public void deleteCss(Long cssId);
}
