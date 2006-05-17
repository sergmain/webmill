package org.riverock.interfaces.portal.dao;

import java.util.List;

import org.riverock.interfaces.portal.bean.PortletName;

/**
 * @author Sergei Maslyukov
 *         Date: 17.05.2006
 *         Time: 13:53:41
 */
public interface PortalPortletNameDao {
    public PortletName getPortletName(Long portletId);
    public PortletName getPortletName(String portletName);
    public Long createPortletName(PortletName portletName);
    public void updatePortletName( PortletName portletNameBean );
    public void deletePortletName( PortletName portletNameBean );
    public List<PortletName> getPortletNameList();
}
