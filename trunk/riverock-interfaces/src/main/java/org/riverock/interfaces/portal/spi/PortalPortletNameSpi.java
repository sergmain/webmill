package org.riverock.interfaces.portal.spi;

import java.util.List;

import org.riverock.interfaces.portal.bean.PortletName;

/**
 * User: SergeMaslyukov
 * Date: 02.09.2007
 * Time: 12:37:23
 */
public interface PortalPortletNameSpi {
    public PortletName getPortletName(Long portletId);
    public PortletName getPortletName(String portletName);
    public Long createPortletName(PortletName portletName);
    public void updatePortletName( PortletName portletNameBean );
    public void deletePortletName( PortletName portletNameBean );
    public List<PortletName> getPortletNameList();
}
