package org.riverock.webmill.portal.dao;

import java.util.List;

import org.riverock.interfaces.portal.bean.User;
import org.riverock.interfaces.sso.a3.AuthSession;
import org.riverock.generic.db.DatabaseAdapter;

/**
 * @author Sergei Maslyukov
 *         Date: 22.06.2006
 *         Time: 20:56:54
 */
public interface InternalUserDao {
    public User getUser(Long portalUserId, AuthSession authSession);

    public User getUserByEMail(DatabaseAdapter db_, String eMail);

    public List<User> getUserList(AuthSession authSession);

    public Long addUser(User portalUserBean);
    public Long addUser(DatabaseAdapter db_, User portalUserBean);

    public void updateUser(User portalUserBean, AuthSession authSession);

    public void deleteUser(User portalUserBean, AuthSession authSession);
}
