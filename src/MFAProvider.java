/**
 * CEN 4078 Programming Exercise 1
 * File Name: MFAProvider.java
 * An interface which provides the ability to set a User MFA code and verify against it
 * @author Caleb Metz
 * @version 1.0
 */
public interface MFAProvider {
    public void enroll(User user, String input);
    public boolean verify(User user, String input);
}
