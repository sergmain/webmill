package org.riverock.sso.dao;

import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.startup.StartupApplication;
import org.riverock.sso.a3.AuthSessionImpl;
import org.riverock.sso.a3.InternalAuthProvider;

/**
 * User: smaslyukov
 * Date: 28.07.2004
 * Time: 13:52:19
 * $Id$
 */
public class TestInternalAuthProvider {
    private static final String USER_LOGIN = "login";
    private static final String USER_PASSWORD = "password";

    public static void main(String args[])
        throws Exception
    {
        StartupApplication.init();

        DatabaseAdapter db = DatabaseAdapter.getInstance("MYSQL");
        InternalAuthProvider auth = new InternalAuthProvider();
        AuthSessionImpl authSession = new AuthSessionImpl(USER_LOGIN, USER_PASSWORD);
        AuthDaoImpl authDao = new AuthDaoImpl();
        authDao.checkAccessMySql(db, USER_LOGIN, USER_PASSWORD, "localhost");
        DatabaseAdapter.close(db);
    }
}
