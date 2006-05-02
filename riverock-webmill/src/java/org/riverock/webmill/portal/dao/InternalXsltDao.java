package org.riverock.webmill.portal.dao;

import java.util.Map;

import org.riverock.interfaces.portal.xslt.XsltTransformer;
import org.riverock.interfaces.portal.bean.Xslt;

/**
 * @author SergeMaslyukov
 *         Date: 30.01.2006
 *         Time: 1:24:19
 *         $Id$
 */
public interface InternalXsltDao {
    public Map<String, XsltTransformer> getTransformerMap(Long siteId);
    public StringBuilder getXsltData( Long xsltId );

    public Map<String, Xslt> getXsltMap(Long siteId);
    public Xslt getXslt(Long siteId);
    public Xslt getXslt(String xsltName);
}
