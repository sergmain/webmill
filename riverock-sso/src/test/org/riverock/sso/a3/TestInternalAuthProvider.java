package org.riverock.sso.a3;

import org.riverock.generic.startup.StartupApplication;
import org.riverock.generic.db.DatabaseAdapter;

/**
 * User: smaslyukov
 * Date: 28.07.2004
 * Time: 13:52:19
 * $Id$
 */
public class TestInternalAuthProvider {

    public static void main(String args[])
        throws Exception
    {
        StartupApplication.init();

        DatabaseAdapter db = DatabaseAdapter.getInstance("MYSQL");
        InternalAuthProvider auth = new InternalAuthProvider();
        AuthSessionImpl authSession = new AuthSessionImpl("login", "password");
        auth.checkAccessMySql(db, authSession, "localhost");
        DatabaseAdapter.close(db);
    }
}
