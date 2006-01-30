package org.riverock.interfaces.portal.dao;

import java.util.List;

import org.riverock.interfaces.portal.bean.TemplateBean;

/**
 * @author SergeMaslyukov
 *         Date: 30.01.2006
 *         Time: 23:31:06
 *         $Id$
 */
public interface PortalCommonDao {
    public TemplateBean getTemplateBean(Long templateId);
    public List<TemplateBean> getTemplateList( Long siteId );
    public List<TemplateBean> getTemplateLanguageList( Long siteLanguageId );
}
