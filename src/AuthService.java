import java.util.Optional;

/**
 CEN 4078 Programming Exercise 1
 File Name: AuthService.java
 Implements input validation and queries the database to authenticate users
 @author Caleb Metz
 @version 1.0
 */

public class AuthService {
    private UserDataStore userDB;
    private Validation validator;
    private MFAProvider mfaProvider;

    public AuthService(UserDataStore userDB, Validation validator, MFAProvider mfaProvider) {
        this.userDB = userDB;
        this.validator = validator;
        this.mfaProvider = mfaProvider;
    }
    public boolean authenticate(String username, String password) {
        Optional<User> userOpt = userDB.findByUsername(username);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            return user.getPassword().equals(password);
        }
        return false;
    }

    public boolean enrollMfa(String username, String input) {
        return true;
    }

    public boolean verifyMFA(String username, String input) {
        return true;
    }
}