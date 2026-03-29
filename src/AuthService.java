import java.util.Optional;

/**
 CEN 4078 Programming Exercise 1
 File Name: AuthService.java
 Implements input validation and queries the database to authenticate users
 @author Caleb Metz
 @version 1.1
 */

public class AuthService {
    private final UserDataStore userDB;
    private final Validation validator;
    private final MFAProvider mfaProvider;
    private final PasswordHandler pwHandler;

    private boolean sanitizeFail(String username, String password, String mfaInput) {
        if (!validator.noSQLInjection(username) || !validator.noSQLInjection(password)) {
            //Remove in prod
            System.out.println("Invalid input detected.");
            return true;
        }
        if(!validator.noIntOverflow(mfaInput)) {
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

    public AuthService(
            UserDataStore userDB,
            Validation validator,
            MFAProvider mfaProvider,
            PasswordHandler pwHandler
    ) {
        this.userDB = userDB;
        this.validator = validator;
        this.mfaProvider = mfaProvider;
        this.pwHandler = pwHandler;
    }

    public User authenticate(String username, String password, String mfaInput) {
        //Grammatically incorrect, logically good enough. Fails if SQL injection detected.
        if (sanitizeFail(username, password, mfaInput)) return null;
        Optional<User> userOpt = userDB.findByUsername(username);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (pwHandler.isPassword(password, user)) {
                if (mfaProvider.verify(user,mfaInput)) {
                    return user;
                }
            };
        }
        return null;
    }

    public boolean saveUser(String username, String password, String mfaCode) {
        //Ensure all input is valid
        if (sanitizeFail(username, password, mfaCode)) return false;
        int code = Integer.parseInt(mfaCode);
        //Register the temporary user
        User tempUser = new User(username, null ,code);
        //Needs exception handling
        tempUser = pwHandler.setPassword(password, tempUser);
        userDB.save(tempUser);
        return true;
    }
    public boolean saveUser(User user) {
        //Ensure all input is valid
        if (sanitizeFail(user.getUserName(), user.getPassword(), String.valueOf(user.getMfaCode()))) return false;
        //Register the validated user
        userDB.save(user);
        return true;
    }
}