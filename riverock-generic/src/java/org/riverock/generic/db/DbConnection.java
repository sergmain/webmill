package org.riverock.generic.db;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author Sergei Maslyukov
 *         Date: 04.07.2006
 *         Time: 12:10:25
 */
public interface DbConnection {
    PreparedStatement prepareStatement(String sql_) throws SQLException;

    Statement createStatement() throws SQLException;

    void commit() throws SQLException;

    void rollback() throws SQLException;
}
