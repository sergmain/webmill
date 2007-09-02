package org.riverock.interfaces.portal.spi;

import java.util.List;

import org.riverock.interfaces.portal.dao.PortalUserDao;
import org.riverock.interfaces.portal.bean.User;

/**
 * User: SergeMaslyukov
 * Date: 02.09.2007
 * Time: 12:53:03
 * $Id$
 */
public interface PortalUserSpi extends PortalUserDao {
    public List<User> getUserList();
    public Long addUser(User user);
    public void updateUser(User user);
    public void deleteUser(User user);
    public User getUser(Long userId);
}
