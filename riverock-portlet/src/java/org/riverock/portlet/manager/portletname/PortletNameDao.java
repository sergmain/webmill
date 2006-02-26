package org.riverock.portlet.manager.portletname;

import java.util.List;

/**
 * @author SergeMaslyukov
 *         Date: 26.02.2006
 *         Time: 15:51:23
 *         $Id$
 */
public interface PortletNameDao {
    public List<PortletNameBean> getPortletNameList();
    public Long addPortletName(PortletNameBean bean);
    public void updatePortletName(PortletNameBean bean);
    public void deletePortletName(PortletNameBean bean);

    public PortletNameBean getPortletName( Long currentPortletNameId );
}
