package config;

/**
 * DBConfig - holds DB connection constants.
 * Replace values with your DB credentials.
 */
public class DBConfig {
    public static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    public static final String URL = "jdbc:mysql://localhost:3306/fitness_db?useSSL=false&serverTimezone=UTC";
    public static final String USER = "root";
    public static final String PASS = "password";
}
