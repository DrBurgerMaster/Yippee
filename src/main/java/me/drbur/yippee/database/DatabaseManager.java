package me.drbur.yippee.database;

import org.h2.jdbcx.JdbcConnectionPool;
import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseManager {

    private static final String DB_URL = "jdbc:h2:./plugins/Yippee/mydatabase";
    private static final String DB_USER = "sa";
    private static final String DB_PASSWORD = "";

    private static JdbcConnectionPool connectionPool;

    public static void init() {
        connectionPool = JdbcConnectionPool.create(DB_URL, DB_USER, DB_PASSWORD);
    }

    public static Connection getConnection() throws SQLException {
        if (connectionPool == null) {
            init();
        }
        return connectionPool.getConnection();
    }
}
