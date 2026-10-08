package com.jobapp.config;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Logger;
import java.util.logging.Level;

public class DatabaseConfig {
    private static final Logger LOGGER = Logger.getLogger(DatabaseConfig.class.getName());

    private static String url = "jdbc:mysql://localhost:3306/job_portal_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static String user = "root";
    private static String password = "password"; // default fallback password
    private static boolean useInMemoryFallback = false;
    private static boolean checked = false;

    static {
        loadProperties();
    }

    private static void loadProperties() {
        try (InputStream input = DatabaseConfig.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (input != null) {
                Properties prop = new Properties();
                prop.load(input);
                url = prop.getProperty("db.url", url);
                user = prop.getProperty("db.user", user);
                password = prop.getProperty("db.password", password);
            }
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "db.properties not found, using default configuration");
        }
    }

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(url, user, password);
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL JDBC Driver not found in classpath!", e);
        }
    }

    public static boolean isDatabaseAvailable() {
        if (!checked) {
            try (Connection conn = getConnection()) {
                useInMemoryFallback = false;
                LOGGER.info("Successfully connected to MySQL database!");
            } catch (SQLException e) {
                useInMemoryFallback = true;
                LOGGER.warning("Could not connect to MySQL database at " + url + " (" + e.getMessage() + "). Switching to In-Memory Collection Storage Mode.");
            }
            checked = true;
        }
        return !useInMemoryFallback;
    }

    public static void setCredentials(String dbUrl, String dbUser, String dbPass) {
        url = dbUrl;
        user = dbUser;
        password = dbPass;
        checked = false;
    }
}
