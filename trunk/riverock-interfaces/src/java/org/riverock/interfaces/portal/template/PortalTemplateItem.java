package org.riverock.interfaces.portal.template;

import java.util.List;

/**
 * @author SergeMaslyukov
 *         Date: 01.01.2006
 *         Time: 9:32:12
 *         $Id$
 */
public interface PortalTemplateItem {
    public String getType();
    public String getNamespace();
    public String getCode();
    public String getValue();
    public String getXmlRoot();
    public PortalTemplateItemType getTypeObject();
    public List<PortalTemplateParameter> getParameters();
}
