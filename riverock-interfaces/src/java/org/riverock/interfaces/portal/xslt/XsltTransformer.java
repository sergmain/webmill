package org.riverock.interfaces.portal.xslt;

import javax.xml.transform.Transformer;

/**
 * @author SergeMaslyukov
 *         Date: 30.12.2005
 *         Time: 15:47:12
 *         $Id$
 */
public interface XsltTransformer {
//    public void terminate(Long id_);
//    public void reinit();
//    public void reinitTransformer();
    public Transformer getTransformer();
}
