package org.riverock.interfaces.portlet.member;

import java.util.List;

/**
 * @author SMaslyukov
 *         Date: 19.04.2005
 *         Time: 14:56:10
 *         $Id$
 */
public interface PortletGetList {
    public List<ClassQueryItem> getList(Long idSiteCtxLangCatalog, Long idContext);
}    