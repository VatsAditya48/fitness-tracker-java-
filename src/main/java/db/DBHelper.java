package db;

import config.DBConfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * DBHelper - manages a single Connection factory.
 * Provides synchronized connection creation for thread-safety.
 */
public class DBHelper {

    static {
        try {
            Class.forName(DBConfig.DRIVER);
        } catch (ClassNotFoundException e) {
            System.err.println("JDBC Driver not found: " + e.getMessage());
        }
    }

    // Get a new connection. Caller must close it.
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DBConfig.URL, DBConfig.USER, DBConfig.PASS);
    }
}
