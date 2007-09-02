package org.riverock.interfaces.portal.spi;

import java.util.List;

import org.riverock.interfaces.portal.bean.Template;
import org.riverock.interfaces.portal.dao.PortalCommonDao;

/**
 * User: SergeMaslyukov
 * Date: 02.09.2007
 * Time: 12:33:57
 */
public interface PortalCommonSpi extends PortalCommonDao {
    Template getTemplate(Long templateId);

    List<Template> getTemplateList( Long siteId );

    List<Template> getTemplateLanguageList( Long siteLanguageId );
}
