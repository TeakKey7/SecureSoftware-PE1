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
    private String alphaKey;
    private int numberKey;

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
            String alphaKey,
            int numberKey
    ) {
        this.cryptographer = cryptographer;
        this.defaultPassword = defaultPassword;
        this.validator = validator;
        this.alphaKey = alphaKey;
        this.numberKey = numberKey;
    }

    public boolean isPassword(String password, User user) {
        return true;
    }

    public User setPassword(String password, User user) {
        return user;
    }

}
