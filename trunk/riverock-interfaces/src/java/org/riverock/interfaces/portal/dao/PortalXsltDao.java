package org.riverock.interfaces.portal.dao;

import java.util.Map;
import java.util.List;

import org.riverock.interfaces.portal.bean.Xslt;

/**
 * @author Sergei Maslyukov
 *         Date: 17.05.2006
 *         Time: 13:56:02
 */
public interface PortalXsltDao {
    public StringBuilder getXsltData( Long xsltId );

    /** key is language of site */
    public Map<String, Xslt> getCurrentXsltForSiteAsMap(Long siteId);
    public Xslt getCurrentXslt(Long siteLanguageId);

    public Xslt getXslt(Long siteId);
    public Xslt getXslt(String xsltName, Long siteLanguageId);
    public Long createXslt(Xslt xslt);
    public List<Xslt> getXsltList(Long siteLanguageId);
}

