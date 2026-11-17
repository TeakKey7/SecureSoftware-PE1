import java.lang.module.ResolutionException;
import java.sql.*;
import java.util.Objects;
import java.util.Optional;

/**
 * CEN 4078 Programming Exercise 1
 * File Name: JdbcRepository.java
 * Modified from Liting Zhang's COP4027 examples
 * @author Caleb Metz
 * @version 1.0
 */

public final class VulnerableJdbcRepository implements UserDataStore {
    private final DatabaseConfig config;

    public VulnerableJdbcRepository(DatabaseConfig config) {
        this.config = Objects.requireNonNull(config, "config");
        createTableIfMissing();
    }

    private Connection connect() throws SQLException {
        return config.openConnection();
    }

    private void createTableIfMissing() {
        String sql = """
                CREATE TABLE IF NOT EXISTS users (
                    username VARCHAR(100) PRIMARY KEY,
                    password VARCHAR(100) NOT NULL,
                    mfacode int
                )
                """;

        try (Connection connection = connect();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.executeUpdate();
        } catch (SQLException exception) {
            throw new RepositoryException("Could not create users table", exception);
        }
    }

    private User mapUser(ResultSet rows) throws SQLException {
        return new User(
                rows.getString("username"),
                rows.getString("password"),
                rows.getInt("mfacode")
        );
    }

    @Override
    public Optional<User> findByUsername(String username) {
        String sql = "SELECT username, password, mfacode FROM users WHERE username = '" + username + "'";
        try (Connection connection = connect();
             Statement statement = connection.createStatement()) {
            try (ResultSet rows = statement.executeQuery(sql)) {
                if (rows.next()) {
                    return Optional.of(mapUser(rows));
                } else {
                    return Optional.empty();
                }
            }
        } catch (SQLException exception) {
            throw new RepositoryException("Could not load user", exception);
        }
    }

    @Override
    public void save(User user) {
        String sql = "INSERT INTO users (username, password, mfacode) VALUES (?, ?, ?)";
        try (Connection connection = connect();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, user.getUserName());
            statement.setString(2, user.getPassword());
            statement.setInt(3, user.getMfaCode());
            statement.executeUpdate();
        } catch (SQLException exception) {
            throw new RepositoryException("Could not add user", exception);
        }
    }

    @Override
    public void delete (String username) {
        String sql = "DELETE FROM users WHERE username = '" + username + "'";
        try (Connection connection = connect();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);
            statement.executeUpdate();
        } catch (SQLException exception) {
            throw new ResolutionException("Could not delete user", exception);
        }
    }
}
