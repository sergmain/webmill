package org.riverock.portlet.manager.users;

import java.util.List;

import org.riverock.interfaces.sso.a3.AuthSession;

/**
 * @author SergeMaslyukov
 *         Date: 26.02.2006
 *         Time: 15:51:23
 *         $Id$
 */
public interface PortalUserDao {
    public List<PortalUserBean> getPortlalUserList(AuthSession authSession);
    public Long addPortalUser(PortalUserBean beanPortal, AuthSession authSession);
    public void updatePortalUser(PortalUserBean beanPortal, AuthSession authSession);
    public void deletePortalUser(PortalUserBean beanPortal, AuthSession authSession);

    public PortalUserBean getPortalUser( Long currentPortalUserId, AuthSession authSession );
}
