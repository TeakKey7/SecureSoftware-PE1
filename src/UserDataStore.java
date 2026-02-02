import java.util.Optional;

/**
 * CEN 4078 Programming Exercise 1
 * File Name: UserDataStore.java
 * Interface for basic database connections
 * @author Caleb Metz
 * @version 1.0
 */
public interface UserDataStore {
    public Optional<User> findByUsername(String userName);
    public void save(User user);
    public void delete(String userName);
}
