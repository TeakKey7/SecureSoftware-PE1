import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Properties;

/**
 * CEN 4078 Programming Exercise 3
 * File Name: DatabaseConfig.java
 * Modified from Liting Zhang's COP4027 examples
 * @author Caleb Metz
 * @version 1.0
 */
public record DatabaseConfig(String url, String username, String password) {
    public DatabaseConfig {
        Objects.requireNonNull(url, "url");
        Objects.requireNonNull(username, "username");
        Objects.requireNonNull(password, "password");
        if (url.isBlank()) {
            throw new IllegalArgumentException("JDBC URL must not be blank");
        }
    }

    public static DatabaseConfig load(String resourceName) throws IOException {
        Properties properties = new Properties();
        try (InputStream input = DatabaseConfig.class.getResourceAsStream(resourceName)) {
            if (input == null) {
                throw new IOException("Configuration resource not found: " + resourceName);
            }
            properties.load(input);
        }

        String url = properties.getProperty("jdbc.url");
        String username = properties.getProperty("jdbc.username");
        String password = properties.getProperty("jdbc.password");
        if (url == null || username == null || password == null) {
            throw new IOException("Database configuration is missing a required property");
        }
        return new DatabaseConfig(url, username, password);
    }

    public Connection openConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }
}