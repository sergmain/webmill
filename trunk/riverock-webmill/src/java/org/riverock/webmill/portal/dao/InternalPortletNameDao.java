package org.riverock.webmill.portal.dao;

import java.util.List;

import org.riverock.interfaces.portal.bean.PortletName;

/**
 * @author Sergei Maslyukov
 *         Date: 05.05.2006
 *         Time: 14:52:23
 */
public interface InternalPortletNameDao {
    public PortletName getPortletName(Long portletId);
    public PortletName getPortletName(String portletName);
    public Long createPortletName(PortletName portletName);
    public void updatePortletName( PortletName portletNameBean );
    public void deletePortletName( PortletName portletNameBean );
    public List<PortletName> getPortletNameList();
}
