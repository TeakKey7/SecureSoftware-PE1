import java.util.Objects;

/**
 * CEN 4078 Programming Exercise 3
 * File Name: PasswordHandler.java
 * Handles all password logic
 * @author Caleb Metz
 * @version 3.0
 */
public class PasswordHandler {
    private final Cryptographer cryptographer;
    private final Validation validator;
    private final DefaultPassword defaultPassword;
    private String alphaKey = "ARGOSROCK";

    public PasswordHandler(
            Cryptographer cryptographer,
            DefaultPassword defaultPassword,
            Validation validator
    ) {
        this.cryptographer = cryptographer;
        this.defaultPassword = defaultPassword;
        this.validator = validator;
    }

    public PasswordHandler(
            Cryptographer cryptographer,
            DefaultPassword defaultPassword,
            Validation validator,
            String alphaKey
    ) {
        this.cryptographer = cryptographer;
        this.defaultPassword = defaultPassword;
        this.validator = validator;
        this.alphaKey = alphaKey;
    }

    public boolean isPassword(String password, User user) {
        if (!validator.isValidPassword(password)) {
            return false;
        }
        return (Objects.equals(cryptographer.encrypt(alphaKey, password), user.getPassword()));
    }

    public User setPassword(String password, User user) {
        if (!validator.isValidPassword(password)) {
            return null;
        }
        user.setPassword(cryptographer.encrypt(alphaKey, password));
        return user;
    }

}
