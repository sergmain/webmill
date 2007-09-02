package org.riverock.interfaces.portal.spi;

import java.util.Map;
import java.util.List;

import org.riverock.interfaces.portal.dao.PortalXsltDao;
import org.riverock.interfaces.portal.bean.Xslt;

/**
 * User: SergeMaslyukov
 * Date: 02.09.2007
 * Time: 12:56:04
 * $Id$
 */
public interface PortalXsltSpi extends PortalXsltDao {
    /** key is language of site */
    public Map<String, Xslt> getCurrentXsltForSiteAsMap(Long siteId);
    public Xslt getCurrentXslt(Long siteLanguageId);

    public Xslt getXslt(Long xsltId);
    public Xslt getXslt(String xsltName, Long siteLanguageId);
    public List<Xslt> getXsltList(Long siteLanguageId);

    public Long createXslt(Xslt xslt);
    public void updateXslt(Xslt xslt);
    public void deleteXslt(Long xsltId);
}
