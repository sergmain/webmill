package org.riverock.portlet.auth.dao;

/**
 * @author SMaslyukov
 *         Date: 22.05.2005
 *         Time: 16:55:23
 *         $Id$
 */
public class AuthDAOFactory {
    public static AuthDAOFactory getDAOFactory() {
        return new AuthDAOFactory();
    }

    public BindIndexDAO getBindIndexDAO() {
        return new BindIndexDAO();
    }

    public BindAddDAO getBindAddDAO() {
        return new BindAddDAO();
    }

    public BindChangeDAO getBindChangeDAO() {
        return new BindChangeDAO();
    }

    public BindDeleteDAO getBindDeleteDAO() {
        return new BindDeleteDAO();
    }
}
