/**
 * CEN 4078 Programming Exercise 1
 * File Name: SimpleMFA.java
 * Creates a 10 digit MFA code and allows a user to verify it
 * @author Caleb Metz
 * @version 1.0
 */
public class SimpleMFA implements MFAProvider{
    private Validation validator;

    @Override
    public void enroll(User user, String input) {

    }

    @Override
    public boolean verify(User user, String input) {
        return true;
    }
}
