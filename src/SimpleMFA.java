/**
 * CEN 4078 Programming Exercise 1
 * File Name: SimpleMFA.java
 * Creates a 10 digit MFA code and allows a user to verify it
 * @author Caleb Metz
 * @version 1.0
 */
public class SimpleMFA implements MFAProvider{
    private final Validation validator;

    public SimpleMFA(Validation validator) {
        this.validator = validator;
    }

    @Override
    public boolean enroll(User user, String input) {
        if (validate(input)) {
            user.setMfaCode(Integer.parseInt(input));
            return true;
        }
        return false;
    }

    @Override
    public boolean verify(User user, String input) {
        if (!validate(input)) {
            return false;
        }
        return (Integer.parseInt(input) == user.getMfaCode());
    }
    //This is needed to ensure the validation occurs on the concrete classes instead of the interface
    private boolean validate(String input) {
        if (!validator.noIntOverflow(input)) {
            return false;
        }
        int code = Integer.parseInt(input);
        return (code >= 1000000000); //Longer than 10 digits
    }
}
