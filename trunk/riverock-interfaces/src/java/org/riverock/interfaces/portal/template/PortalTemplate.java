package org.riverock.interfaces.portal.template;

import java.util.List;

/**
 * @author SergeMaslyukov
 *         Date: 01.01.2006
 *         Time: 9:11:56
 *         $Id$
 */
public interface PortalTemplate {
    public Long getTemplateId();
    public String getTemplateName();
    public String getRole();
    public List<PortalTemplateItem> getPortalTemplateItems();
}
