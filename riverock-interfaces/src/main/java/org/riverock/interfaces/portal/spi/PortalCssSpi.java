package org.riverock.interfaces.portal.spi;

import java.util.List;

import org.riverock.interfaces.portal.bean.Css;
import org.riverock.interfaces.portal.dao.PortalCssDao;

/**
 * User: SergeMaslyukov
 * Date: 02.09.2007
 * Time: 12:46:25
 * $Id$
 */
public interface PortalCssSpi extends PortalCssDao {
    public Css getCssCurrent(Long siteId);
    public Css getCss(Long cssId);
    public List<Css> getCssList(Long siteId);

    public Long createCss(Css css);
    public void updateCss(Css css);
    public void deleteCss(Long cssId);
}
