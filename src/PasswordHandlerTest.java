import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * CEN 4078 Programming Exercise 3
 * File Name: PasswordHandlerTest.java
 * Test suite for the PasswordHandler class
 * @author Caleb Metz
 * @version 3.0
 */
public class PasswordHandlerTest {

    public static final String encryptedPassword = "Secure123";
    public static final String decryptedPassword = "Unlocked123";
    public static final String encryptedPassword2 = "Secure1234";
    public static final String decryptedPassword2 = "Unlocked1234";
    public static final String decryptedBadPassword = "ILLEGAL";
    public static final String encryptedBadPassword = "ILLEGAL123";
    public static final String failedCryptoString = "AAAAAAAA";
    public static final int failedCryptoNumber = 999999999;
    public static final int encryptedCode = 9999;
    public static final int decryptedCode = 0000;
    User alice = new User("alice", encryptedPassword, 2000000000);
    static class MockValidation extends Validation {
        public boolean verifySQLInjectionWasCalled = false;
        public boolean verifyValidPasswordWasCalled = false;
        @Override
        public boolean noSQLInjection(String input) {
            verifySQLInjectionWasCalled = true;
            return (!input.equals(decryptedBadPassword));
        }
        @Override
        public boolean isValidPassword(String input) {
            verifyValidPasswordWasCalled = true;
            return (!input.equals(decryptedBadPassword));
        }

    }
    static class MockCryptographer implements Cryptographer {
        @Override
        public String encrypt(String alphaKey, String input) {
            if (Objects.equals(input, decryptedPassword)) {
                return encryptedPassword;
            } else if (Objects.equals(input, decryptedPassword2)){
                return encryptedPassword2;
            } else if (Objects.equals(input, decryptedBadPassword)) {
                return encryptedBadPassword;
            } else {
                return failedCryptoString;
            }
        }

        @Override
        public int encrypt(int numberKey, int input) {
            if (input == decryptedCode) {
                return encryptedCode;
            } else {
                return failedCryptoNumber;
            }
        }

        @Override
        public String decrypt(String alphaKey, String input) {
            if (Objects.equals(input, encryptedPassword)) {
                return decryptedPassword;
            } else if (Objects.equals(input, encryptedPassword2)){
                return decryptedPassword2;
            } else if (Objects.equals(input, encryptedBadPassword)) {
                return decryptedBadPassword;
            } else {
                return failedCryptoString;
            }
        }

        @Override
        public int decrypt(int numberKey, int input) {
            if (input == encryptedCode) {
                return decryptedCode;
            } else {
                return failedCryptoNumber;
            }
        }
    }

    static class MockDefaultPassword extends DefaultPassword{
        public MockDefaultPassword(Validation validator) {
            super(validator);
        }

        @Override
        public String generateDefaultPassword() {
            return decryptedPassword;
        }
    }
    @Test
    void testIsPassword() {
        PasswordHandlerTest.MockValidation mockValidation = new PasswordHandlerTest.MockValidation();
        PasswordHandlerTest.MockCryptographer mockCryptographer = new PasswordHandlerTest.MockCryptographer();
        PasswordHandlerTest.MockDefaultPassword mockDefaultPassword = new PasswordHandlerTest.MockDefaultPassword(mockValidation);

        PasswordHandler pwHandler = new PasswordHandler(mockCryptographer, mockDefaultPassword, mockValidation);

        assertTrue(pwHandler.isPassword(decryptedPassword, alice), "True on correct login");
        assertFalse(pwHandler.isPassword(decryptedPassword2, alice), "False on wrong password");
        assertFalse(pwHandler.isPassword(decryptedBadPassword, alice), "False on bad password");
    }

    @Test
    void testSetPassword() {
        PasswordHandlerTest.MockValidation mockValidation = new PasswordHandlerTest.MockValidation();
        PasswordHandlerTest.MockCryptographer mockCryptographer = new PasswordHandlerTest.MockCryptographer();
        PasswordHandlerTest.MockDefaultPassword mockDefaultPassword = new PasswordHandlerTest.MockDefaultPassword(mockValidation);

        PasswordHandler pwHandler = new PasswordHandler(mockCryptographer, mockDefaultPassword, mockValidation);
        User bob = new User("bob", encryptedPassword, 2000000000);
        assertEquals(encryptedPassword, bob.getPassword(), "Test user initializes");
        pwHandler.setPassword(decryptedPassword2, bob);
        assertEquals(encryptedPassword2, bob.getPassword(), "Return user with new password");
    }

    @Test
    void testValidPasswordCheckCalledOnIsPassword() {
        PasswordHandlerTest.MockValidation mockValidation = new PasswordHandlerTest.MockValidation();
        PasswordHandlerTest.MockCryptographer mockCryptographer = new PasswordHandlerTest.MockCryptographer();
        PasswordHandlerTest.MockDefaultPassword mockDefaultPassword = new PasswordHandlerTest.MockDefaultPassword(mockValidation);

        PasswordHandler pwHandler = new PasswordHandler(mockCryptographer, mockDefaultPassword, mockValidation);

        User bob = new User("bob", encryptedBadPassword, 2000000000);

        boolean result = pwHandler.isPassword(decryptedBadPassword, bob);
        assertFalse(result, "Ensure bad password cannot validate");
        assertTrue(mockValidation.verifyValidPasswordWasCalled, "Valid password check was called");
    }

    @Test
    void testValidPasswordCheckCalledOnSetPassword() {
        PasswordHandlerTest.MockValidation mockValidation = new PasswordHandlerTest.MockValidation();
        PasswordHandlerTest.MockCryptographer mockCryptographer = new PasswordHandlerTest.MockCryptographer();
        PasswordHandlerTest.MockDefaultPassword mockDefaultPassword = new PasswordHandlerTest.MockDefaultPassword(mockValidation);

        PasswordHandler pwHandler = new PasswordHandler(mockCryptographer, mockDefaultPassword, mockValidation);

        User bob = new User("bob", encryptedPassword, 2000000000);

        User user = pwHandler.setPassword(decryptedBadPassword, bob);
        assertNull(user);
        assertTrue(mockValidation.verifyValidPasswordWasCalled, "Valid password check was called");
    }


}
