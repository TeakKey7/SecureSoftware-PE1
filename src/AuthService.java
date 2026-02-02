import java.util.Optional;

/**
 CEN 4078 Programming Exercise 1
 File Name: AuthService.java
 Implements input validation and queries the database to authenticate users
 @author Caleb Metz
 @version 1.0
 */

public class AuthService {
    private final UserDataStore userDB;
    private final Validation validator;
    private final MFAProvider mfaProvider;

    private boolean sanitizeFail(String username, String password, String mfaInput) {
        if (!validator.noSQLInjection(username) || !validator.noSQLInjection(password)) {
            //Remove in prod
            System.out.println("Invalid input detected.");
            return true;
        }
        if(!mfaProvider.validate(mfaInput)) {
            //Remove in prod
            System.out.println("Invalid code detected.");
            return true;
        }
        if(!validator.isValidPassword(password)) {
            System.out.println("Weak Password.");
            return true;
        }
        return false;
    }

    public AuthService(UserDataStore userDB, Validation validator, MFAProvider mfaProvider) {
        this.userDB = userDB;
        this.validator = validator;
        this.mfaProvider = mfaProvider;
    }
    public boolean authenticate(String username, String password, String mfaInput) {
        //Grammatically incorrect, logically good enough. Fails if SQL injection detected.
        if (sanitizeFail(username, password, mfaInput)) return false;
        Optional<User> userOpt = userDB.findByUsername(username);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (user.getPassword().equals(password)) {
                return (user.getMfaCode() == Integer.parseInt(mfaInput));
            };
        }
        return false;
    }

    public boolean saveUser(String username, String password, String mfaCode) {
        //Ensure all input is valid
        if (sanitizeFail(username, password, mfaCode)) return false;
        int code = Integer.parseInt(mfaCode);
        //Register the validated user
        userDB.save(new User(username,password,code));
        return true;
    }
}