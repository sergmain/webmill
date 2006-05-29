package org.riverock.interfaces.portal.dao;

import java.util.List;

import org.riverock.interfaces.portal.bean.User;

/**
 * @author Sergei Maslyukov
 *         Date: 29.05.2006
 *         Time: 15:20:48
 */
public interface PortalUserDao {
    public List<User> getUserList();
    public Long addUser(User user);
    public void updateUser(User user);
    public void deleteUser(User user);
    public User getUser(Long userId);
}
