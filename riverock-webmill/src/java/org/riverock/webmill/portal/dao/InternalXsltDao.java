package org.riverock.webmill.portal.dao;

import java.util.Map;
import java.util.List;

import org.riverock.interfaces.portal.xslt.XsltTransformer;
import org.riverock.interfaces.portal.bean.Xslt;

/**
 * @author SergeMaslyukov
 *         Date: 30.01.2006
 *         Time: 1:24:19
 *         $Id$
 */
public interface InternalXsltDao {
    public Map<String, XsltTransformer> getTransformerForCurrentXsltMap(Long siteId);
    public StringBuilder getXsltData( Long xsltId );

    /** key is language of site */
    public Map<String, Xslt> getCurrentXsltForSiteAsMap(Long siteId);
    public Xslt getCurrentXslt(Long siteLanguageId);

    public Xslt getXslt(Long xsltId);
    public Xslt getXslt(String xsltName, Long siteLanguageId);
    public Long createXslt(Xslt xslt);

    public List<Xslt> getXsltList(Long siteLanguageId);
}
